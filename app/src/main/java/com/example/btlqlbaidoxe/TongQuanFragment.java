package com.example.btlqlbaidoxe;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TongQuanFragment extends Fragment {
    private TextView revenue, entries, exits, rate, desc, result;
    private ProgressBar progress;
    private Spinner period;

    private View a, b, c;
    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup p,
        @Nullable Bundle bnd) {
        View v=i.inflate(R.layout.giao_dien_tong_quan, p, false);
        revenue=v.findViewById(R.id.tv_doanh_thu);
        entries=v.findViewById(R.id.tv_luot_vao);
        exits=v.findViewById(R.id.tv_luot_ra);
        rate=v.findViewById(R.id.tv_ty_le_tai);
        desc=v.findViewById(R.id.tv_mo_ta_thong_ke);
        result=v.findViewById(R.id.tv_ket_qua_thong_ke);
        progress=v.findViewById(R.id.progress_tong_quat);
        period=v.findViewById(R.id.sp_moc_thoi_gian);
        a=v.findViewById(R.id.khu_a);
        b=v.findViewById(R.id.khu_b);
        c=v.findViewById(R.id.khu_c);
        a.setOnClickListener(x->go("A"));
        b.setOnClickListener(x->go("B"));
        c.setOnClickListener(x->go("C"));
        setupPeriod();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(period!=null)update(period.getSelectedItemPosition());
        updateZones();
    }

    private void setupPeriod() {
        String[] times= {"Hôm nay", "Tuần này", "Tháng này"};
        ArrayAdapter<String> ad=new ArrayAdapter<String>(requireContext(),
            android.R.layout.simple_spinner_item, times) {
            @Override
            public View getView(int p, View v, ViewGroup parent) {
                return item(super.getView(p, v, parent));
            }
            @Override
            public View getDropDownView(int p, View v, ViewGroup parent) {
                return item(super.getDropDownView(p, v, parent));
            }
            private View item(View v) {
                TextView t=(TextView)v;
                t.setTextColor(Color.rgb(32, 33, 36));
                t.setTextSize(14);
                t.setGravity(Gravity.CENTER_VERTICAL);
                t.setPadding(12, 0, 12, 0);
                return t;
            }
        };
        period.setAdapter(ad);
        period.setSelection(0);
        period.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                update(pos);
            }
            @Override
            public void onNothingSelected(AdapterView<?> p) {
            }
        }
        );
    }

    private void update(int type) {
        XuLyXml.Stats s=XuLyXml.thongKe(requireContext(), type);
        String money=new DecimalFormat("#,### VNĐ").format(s.doanhThu);
        String t=time(type);
        revenue.setText(money);
        entries.setText(String.valueOf(s.vo));
        exits.setText(String.valueOf(s.ra));
        desc.setText(getString(R.string.stats_viewing, t, range(type)));
        result.setText(getString(R.string.stats_result, t.toLowerCase(Locale.getDefault()), s.vo, s.ra,
            money));
    }

    private void updateZones() {
        List<ViTriDo> ds=XuLyXml.layViTri(requireContext());
        int[] all= {0, 0, 0}, full= {0, 0, 0};
        for(ViTriDo v: ds) {
            String m=safe(v.getMaViTri()).toUpperCase(Locale.getDefault());
            int k=m.startsWith("A")?0: m.startsWith("B")?1: m.startsWith("C")?2: -1;
            if(k>=0) {
                all[k]++;
                if(XuLyXml.CO_XE.equalsIgnoreCase(safe(v.getTrangThai())))full[k]++;
            }
        }
        setZone(a, R.string.zone_a, full[0], all[0]);
        setZone(b, R.string.zone_b, full[1], all[1]);
        setZone(c, R.string.zone_c, full[2], all[2]);
        int total=all[0]+all[1]+all[2], used=full[0]+full[1]+full[2], pc=total==0?0: Math.round(used*100f/total);
        rate.setText(getString(R.string.rate_summary, used, total, pc));
        progress.setProgress(pc);
    }

    private void setZone(View v, int name, int used, int total) {
        ((TextView)v.findViewById(R.id.tv_ten_khu)).setText(name);
        ((TextView)v.findViewById(R.id.tv_so_luong_khu)).setText(getString(R.string.zone_count, used,
            total));
        ((ProgressBar)v.findViewById(R.id.progress_khu)).setProgress(total==0?0: Math.round(used*100f/total));
    }

    private String time(int type) {
        return getString(type==1?R.string.this_week: type==2?R.string.this_month: R.string.today);
    }

    private String range(int type) {
        Calendar c=Calendar.getInstance();
        SimpleDateFormat f=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        if(type==0)return f.format(c.getTime());
        if(type==1) {
            int d=c.get(Calendar.DAY_OF_WEEK)-Calendar.MONDAY;
            if(d<0)d+=7;
            c.add(Calendar.DAY_OF_YEAR, -d);
            String s=f.format(c.getTime());
            c.add(Calendar.DAY_OF_YEAR, 6);
            return s+" - "+f.format(c.getTime());
        }
        c.set(Calendar.DAY_OF_MONTH, 1);
        String s=f.format(c.getTime());
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return s+" - "+f.format(c.getTime());
    }

    private void go(String k) {
        if(getActivity() instanceof ManHinhChinh)((ManHinhChinh)getActivity()).chuyenSangSoDo(k);
    }

    private String safe(String s) {
        return s==null?"": s;
    }
}
