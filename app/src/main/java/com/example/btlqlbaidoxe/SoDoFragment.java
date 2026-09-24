package com.example.btlqlbaidoxe;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.w3c.dom.Element;

public class SoDoFragment extends Fragment {
    public static final String KEY="khu";
    private LinearLayout list;
    private ScrollView scroll;
    private EditText search;
    private TextView all, empty, full;

    private String filter=ALL, khu;
    private List<ViTriDo> data=new ArrayList<>();
    private static final String ALL="ALL", EMPTY="EMPTY", FULL="FULL";
    private final String[] kinds= {"A", "B", "C"};
    public static SoDoFragment newInstance(String k) {
        SoDoFragment f=new SoDoFragment();
        if(k!=null) {
            Bundle b=new Bundle();
            b.putString(KEY, k);
            f.setArguments(b);
        }
        return f;
    }
    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup p,
        @Nullable Bundle b) {
        View v=i.inflate(R.layout.giao_dien_so_do, p, false);
        search=v.findViewById(R.id.et_tim_kiem_so_do);
        list=v.findViewById(R.id.ll_so_do);
        scroll=v.findViewById(R.id.scroll_so_do);
        all=v.findViewById(R.id.btn_loc_tat_ca);
        empty=v.findViewById(R.id.btn_loc_trong);
        full=v.findViewById(R.id.btn_loc_co_xe);
        all.setOnClickListener(x -> setFilter(ALL));
        empty.setOnClickListener(x -> setFilter(EMPTY));
        full.setOnClickListener(x -> setFilter(FULL));
        if(getArguments()!=null)khu=getArguments().getString(KEY);
        search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int a, int c, int d) {
            }
            public void onTextChanged(CharSequence s, int a, int b, int c) {
                render();
            }
            public void afterTextChanged(Editable e) {
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

    private void setFilter(String value) {
        filter=value;
        all.setBackgroundResource(ALL.equals(value)?R.drawable.bg_nut_loc_chon: R.drawable.bg_nut_loc_thuong);
        empty.setBackgroundResource(EMPTY.equals(value)?R.drawable.bg_nut_loc_chon: R.drawable.bg_nut_loc_thuong);
        full.setBackgroundResource(FULL.equals(value)?R.drawable.bg_nut_loc_chon: R.drawable.bg_nut_loc_thuong);
        all.setTypeface(null,
            ALL.equals(value)?android.graphics.Typeface.BOLD: android.graphics.Typeface.NORMAL);
        empty.setTypeface(null,
            EMPTY.equals(value)?android.graphics.Typeface.BOLD: android.graphics.Typeface.NORMAL);
        full.setTypeface(null,
            FULL.equals(value)?android.graphics.Typeface.BOLD: android.graphics.Typeface.NORMAL);
        render();
    }

    private void reload() {
        data=XuLyXml.layViTri(requireContext());
        render();
    }

    private void render() {
        list.removeAllViews();

        String key=search.getText().toString().trim()
                .toLowerCase(Locale.getDefault());

        View targetView=null;

        for(String k:kinds) {
            List<ViTriDo> a=new ArrayList<>();

            // Luon hien thi ca 3 khu A, B, C.
            // khu chi dung de xac dinh khu can cuon toi.
            for(ViTriDo v:data) {
                String ma=safe(v.getMaViTri()).toUpperCase(Locale.getDefault());
                String bs=safe(v.getBienSo()).toLowerCase(Locale.getDefault());
                boolean coXe=coXe(v);

                if(!ma.startsWith(k)) {
                    continue;
                }

                if(EMPTY.equals(filter) && coXe) {
                    continue;
                }

                if(FULL.equals(filter) && !coXe) {
                    continue;
                }

                if(!key.isEmpty()
                        && !ma.toLowerCase(Locale.getDefault()).contains(key)
                        && !bs.contains(key)) {
                    continue;
                }

                a.add(v);
            }

            if(a.isEmpty()) {
                continue;
            }

            View h=header(k,a.size());
            list.addView(h);

            // Ghi lai vi tri cua khu duoc chon de cuon toi sau khi ve xong.
            if(khu!=null && khu.equalsIgnoreCase(k)) {
                targetView=h;
            }

            LinearLayout row=null;

            for(int n=0;n<a.size();n++) {
                if(n%4==0) {
                    row=new LinearLayout(requireContext());
                    row.setOrientation(LinearLayout.HORIZONTAL);
                    list.addView(row);
                }
                row.addView(cell(a.get(n)));
            }

            if(row!=null && a.size()%4!=0) {
                for(int n=a.size()%4;n<4;n++) {
                    row.addView(space());
                }
            }
        }

        if(targetView!=null) {
            final View target=targetView;

            // Doi layout xong roi moi cuon, tranh truong hop target.getTop() chua tinh xong.
            scroll.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int y=target.getTop();
                    if(y<0) {
                        y=0;
                    }
                    scroll.smoothScrollTo(0,y);
                    khu=null;
                }
            },150);
        }
    }

    private View header(String k, int count) {
        LinearLayout h=new LinearLayout(requireContext());
        h.setGravity(Gravity.CENTER_VERTICAL);
        h.setPadding(10, 10, 10, 10);
        h.setBackgroundResource(R.drawable.bg_tieu_de_khu);
        int id=k.equals("A")?R.string.zone_header_a: k.equals("B")?R.string.zone_header_b: R.string.zone_header_c;
        TextView t=Ui.text(requireContext(), getString(id), 13, Color.rgb(26, 59, 122), true);
        h.addView(t, Ui.lp(requireContext(), 0, -2, 1, 0, 0, 0, 0));
        TextView n=Ui.text(requireContext(), getString(R.string.zone_spaces, count), 12, Color.rgb(26, 59,
            122), true);
        n.setPadding(10, 3, 10, 3);
        n.setBackgroundResource(R.drawable.bg_huy_hieu);
        h.addView(n);
        h.setLayoutParams(Ui.lp(requireContext(), -1, -2, 0, 0, 10, 0, 6));
        return h;
    }

    private View cell(ViTriDo v) {
        boolean full=coXe(v);
        int color=ContextCompat.getColor(
                requireContext(),
                full?R.color.mau_do:R.color.xanh_la_chu
        );
        LinearLayout c=new LinearLayout(requireContext());
        c.setGravity(Gravity.CENTER);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(6, 6, 6, 6);
        c.setBackgroundResource(
                full?R.drawable.bg_o_co_xe:R.drawable.bg_o_trong
        );
        TextView ma=Ui.text(requireContext(), safe(v.getMaViTri()), 15, color, true);
        TextView st=Ui.text(
                requireContext(),
                full?safe(v.getBienSo()):getString(R.string.spot_empty),
                10,
                color,
                true
        );
        st.setGravity(Gravity.CENTER);
        st.setMaxLines(1);
        st.setEllipsize(TextUtils.TruncateAt.END);
        c.addView(ma);
        c.addView(st, Ui.lp(requireContext(), -1, -2, 0, 0, 3, 0, 0));
        c.setClickable(true);
        c.setFocusable(true);
        c.setOnClickListener(x -> {
            if(full) {
                checkout(v);
            } else {
                checkin(v);
            }
        });
        c.setLayoutParams(Ui.lp(requireContext(), 0, Ui.dp(requireContext(), 78), 1, 4, 4, 4, 4));
        return c;
    }

    private View space() {
        Space s=new Space(requireContext());
        s.setLayoutParams(Ui.lp(requireContext(), 0, Ui.dp(requireContext(), 78), 1, 4, 4, 4, 4));
        return s;
    }

    private boolean coXe(ViTriDo v) {
        return XuLyXml.CO_XE.equalsIgnoreCase(safe(v.getTrangThai()));
    }

    private String type(String ma) {
        String m=safe(ma).toUpperCase(Locale.getDefault());
        return m.startsWith("A")?getString(R.string.type_car): m.startsWith("C")?getString(R.string.type_bike): getString(R.string.type_motor);
    }

    private String ticket() {
        return "VE-"
                +Long.toString(
                        System.currentTimeMillis(),
                        36
                ).toUpperCase(Locale.US);
    }

    private void checkin(ViTriDo v) {
        String id=ticket();
        String kind=type(v.getMaViTri());

        LinearLayout box=Ui.box(
                requireContext(),
                LinearLayout.VERTICAL,
                20
        );

        TextView t=Ui.text(
                requireContext(),
                getString(R.string.ticket_auto,id),
                14,
                Color.rgb(30,136,229),
                true
        );
        t.setPadding(0,8,0,12);

        TextView typeText=Ui.text(
                requireContext(),
                "Loại xe: "+kind,
                13,
                Color.rgb(80,90,110),
                true
        );
        typeText.setPadding(0,0,0,10);

        EditText plate=new EditText(requireContext());
        plate.setHint(R.string.enter_plate_hint);
        plate.setSingleLine();
        plate.setTextColor(Color.rgb(32,33,36));
        plate.setHintTextColor(Color.rgb(154,160,166));
        plate.setBackgroundResource(R.drawable.bg_the_trang);
        plate.setPadding(10,0,10,0);
        plate.setLayoutParams(
                new LinearLayout.LayoutParams(
                        -1,
                        Ui.dp(requireContext(),48)
                )
        );

        box.addView(t);
        box.addView(typeText);
        box.addView(plate);

        new AlertDialog.Builder(requireContext())
                .setTitle(
                        getString(
                                R.string.checkin_title,
                                v.getMaViTri()
                        )
                )
                .setView(box)
                .setPositiveButton(
                        R.string.confirm_in,
                        (d,w) -> {
                            String bs=plate.getText()
                                    .toString()
                                    .trim()
                                    .toUpperCase(Locale.getDefault());

                            if(bs.isEmpty()) {
                                toast(R.string.enter_plate);
                                return;
                            }

                            long now=System.currentTimeMillis();
                            String tg=new SimpleDateFormat(
                                    "dd/MM/yyyy HH:mm",
                                    Locale.getDefault()
                            ).format(new Date(now));

                            if(XuLyXml.themXe(
                                    requireContext(),
                                    id,
                                    bs,
                                    kind,
                                    v.getMaViTri(),
                                    tg,
                                    now
                            )) {
                                toast(R.string.checkin_success);
                                reload();
                            } else {
                                toast(R.string.save_error);
                            }
                        }
                )
                .setNegativeButton(
                        R.string.cancel,
                        null
                )
                .show();
    }

    private void checkout(ViTriDo v) {
        Element e=XuLyXml.xeDangGui(requireContext(), v.getMaViTri());
        if(e==null) {
            toast(R.string.no_parking_record);
            return;
        }
        String mv=XuLyXml.lay(e, "MaVe"), bs=XuLyXml.lay(e, "BienSo"), lx=XuLyXml.lay(e,
            "LoaiXe"), tg=XuLyXml.lay(e, "ThoiGianVao");
        long vao=num(XuLyXml.lay(e, "TimeVaoMillis"),
            System.currentTimeMillis()), now=System.currentTimeMillis(), fee=XuLyXml.tinhPhi(requireContext(),
            lx, vao, now);
        String msg=getString(R.string.checkout_message, getString(R.string.checkout_info, mv, bs, lx, tg),
            getString(R.string.checkout_fee, new DecimalFormat("#,### VNĐ").format(fee)));
        new AlertDialog.Builder(requireContext()).setTitle(getString(R.string.checkout_title,
            v.getMaViTri())).setMessage(msg).setPositiveButton(R.string.confirm_out, (d, w)-> {
            long ra=System.currentTimeMillis(); String out=new SimpleDateFormat("dd/MM/yyyy HH:mm",
                Locale.getDefault()).format(new Date(ra)); if(XuLyXml.xuatXe(requireContext(), mv, out, ra,
                fee)) {
                toast(getString(R.string.checkout_success, v.getMaViTri())); reload();
            } else toast(R.string.checkout_error);
        }
        ).setNegativeButton(R.string.close, null).show();
    }

    private long num(String s, long d) {
        try {
            return Long.parseLong(s);
        } catch(Exception e) {
            return d;
        }
    }

    private void toast(int id) {
        Toast.makeText(requireContext(), id, Toast.LENGTH_SHORT).show();
    }

    private void toast(String s) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
    }

    private String safe(String s) {
        return s==null?"": s;
    }
}
