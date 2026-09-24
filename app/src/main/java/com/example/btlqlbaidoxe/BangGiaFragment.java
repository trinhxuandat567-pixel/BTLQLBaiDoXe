package com.example.btlqlbaidoxe;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BangGiaFragment extends Fragment {
    private EditText[] e;
    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup p,
        @Nullable Bundle b) {
        View v=i.inflate(R.layout.giao_dien_bang_gia, p, false);
        e=new EditText[] {
            v.findViewById(R.id.et_oto_2h), v.findViewById(R.id.et_oto_gio_tiep), v.findViewById(R.id.et_xe_may_luot), v.findViewById(R.id.et_xe_may_dem), v.findViewById(R.id.et_xe_dap_luot)
        };
        for(EditText x: e) {
            x.setTextColor(Color.rgb(32, 33, 36));
            x.setHintTextColor(Color.rgb(120, 130, 154));
        }
        long[] g=XuLyXml.docBangGia(requireContext());
        for(int n=0; n<e.length; n++)e[n].setText(String.valueOf(g[n]));
        Button save=v.findViewById(R.id.btn_luu_bang_gia);
        save.setTextColor(Color.WHITE);
        save.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(31, 174, 110)));
        save.setOnClickListener(x->save());
        return v;
    }

    private void save() {
        try {
            long[] g=new long[5];
            for(int n=0; n<5; n++) {
                g[n]=Long.parseLong(e[n].getText().toString().trim());
                if(g[n]<0)throw new NumberFormatException();
            }
            XuLyXml.luuBangGia(requireContext(), g);
            Toast.makeText(requireContext(), R.string.price_updated, Toast.LENGTH_SHORT).show();
        } catch(Exception x) {
            Toast.makeText(requireContext(), R.string.price_invalid, Toast.LENGTH_SHORT).show();
        }
    }
}
