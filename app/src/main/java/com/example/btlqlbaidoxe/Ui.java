package com.example.btlqlbaidoxe;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;
public final class Ui {
    private Ui() {
    }
    public static int dp(Context c, int n) {
        return Math.round(n*c.getResources().getDisplayMetrics().density);
    }
    public static TextView text(Context c, CharSequence s, float size, int color, boolean bold) {
        TextView t=new TextView(c);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(color);
        if(bold)t.setTypeface(null, Typeface.BOLD);
        return t;
    }
    public static LinearLayout box(Context c, int orientation, int padding) {
        LinearLayout l=new LinearLayout(c);
        l.setOrientation(orientation);
        int p=dp(c, padding);
        l.setPadding(p, p, p, p);
        return l;
    }
    public static LinearLayout.LayoutParams lp(Context c, int w, int h, float weight, int l, int t, int r,
        int b) {
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(w, h, weight);
        p.setMargins(dp(c, l), dp(c, t), dp(c, r), dp(c, b));
        return p;
    }
}
