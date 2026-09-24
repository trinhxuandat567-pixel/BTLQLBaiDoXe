package com.example.btlqlbaidoxe;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class LichSuFragment extends Fragment {
    private LinearLayout list;
    private EditText search;
    private TextView total;
    private List<XuLyXml.Record> data;
    @Nullable
    @Override public View onCreateView(@NonNull android.view.LayoutInflater i, @Nullable ViewGroup p,
        @Nullable Bundle b) {
        View v=i.inflate(R.layout.giao_dien_lich_su, p, false);
        search=v.findViewById(R.id.et_tim_kiem_lich_su);
        list=v.findViewById(R.id.ll_lich_su);
        total=v.findViewById(R.id.tv_tong_luot);
        search.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s, int a, int c, int d) {
            }
            public void onTextChanged(CharSequence s, int a, int b, int c) {
                render();
            }
            public void afterTextChanged(android.text.Editable e) {
            }
        }
        );
        reload();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(list!=null)reload();
    }

    private void reload() {
        data=XuLyXml.lichSu(requireContext());
        render();
    }

    private void render() {
        list.removeAllViews();
        String key=search.getText()
                .toString()
                .trim()
                .toLowerCase(Locale.getDefault());
        int count=0;
        for(XuLyXml.Record r: data) {
            if(!key.isEmpty()&&!has(r.maVe, key)&&!has(r.bienSo, key)&&!has(r.viTri, key)&&!has(r.loaiXe,
                key))continue;
            list.addView(row(r));
            count++;
        }
        total.setText(getString(R.string.history_total, count));
    }

    private View row(XuLyXml.Record r) {
        LinearLayout box=Ui.box(requireContext(), LinearLayout.HORIZONTAL, 14);
        box.setGravity(Gravity.CENTER_VERTICAL);
        box.setLayoutParams(Ui.lp(requireContext(), -1, -2, 0, 0, 0, 0, 10));
        LinearLayout info=Ui.box(requireContext(), LinearLayout.VERTICAL, 0);
        info.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1));
        info.addView(Ui.text(requireContext(), getString(R.string.history_title, r.maVe, r.bienSo), 14,
            Color.rgb(26, 26, 46), true));
        info.addView(Ui.text(requireContext(), getString(R.string.history_detail, r.viTri, r.loaiXe,
            r.thoiGianVao), 12, Color.rgb(120, 130, 154), false));
        box.addView(info);
        TextView status=Ui.text(requireContext(), "", 11, Color.rgb(30, 136, 229), true);
        status.setGravity(Gravity.CENTER);
        status.setPadding(10, 5, 10, 5);
        if(XuLyXml.DANG_GUI.equalsIgnoreCase(r.trangThai)) {
            status.setText(R.string.history_sending);
            status.setBackgroundResource(R.drawable.bg_pill_dang_gui);
        } else {
            status.setText(getString(R.string.history_exported,
                new DecimalFormat("#,### VNĐ").format(r.tongTien)));
            status.setTextColor(Color.rgb(120, 130, 154));
            status.setBackgroundResource(R.drawable.bg_pill_da_xuat);
        }
        box.addView(status);
        return box;
    }

    private boolean has(String s, String k) {
        return s!=null&&s.toLowerCase(Locale.getDefault()).contains(k);
    }
}
