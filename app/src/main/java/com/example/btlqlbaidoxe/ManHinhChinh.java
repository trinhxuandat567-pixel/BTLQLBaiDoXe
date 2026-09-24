package com.example.btlqlbaidoxe;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ManHinhChinh extends AppCompatActivity {
    private BottomNavigationView nav;
    private TextView sub;

    private String pendingZone;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.giao_dien_chinh);
        XuLyXml.docBangGia(this);
        XuLyXml.taoSoDo(this);
        nav=findViewById(R.id.bottom_navigation);
        sub=findViewById(R.id.tv_phu_de_header);
        nav.setOnItemSelectedListener(item -> {
            int id=item.getItemId();
            Fragment f;

            if(id==R.id.nav_tong_quan) {
                f=new TongQuanFragment();
            } else if(id==R.id.nav_so_do) {
                f=SoDoFragment.newInstance(pendingZone);
                pendingZone=null;
            } else if(id==R.id.nav_bang_gia) {
                f=new BangGiaFragment();
            } else {
                f=new LichSuFragment();
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,f)
                    .commit();

            if(id==R.id.nav_so_do) {
                sub.setText(R.string.title_parking_map);
            } else {
                sub.setText(item.getTitle());
            }

            return true;
        });
        if(b==null) {
            nav.setSelectedItemId(R.id.nav_tong_quan);
        }
    }
    public void chuyenSangSoDo(String khu) {
        pendingZone=khu;
        nav.setSelectedItemId(R.id.nav_so_do);
    }
}
