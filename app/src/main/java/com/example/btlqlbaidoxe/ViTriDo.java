package com.example.btlqlbaidoxe;
public class ViTriDo {
    private final String ma, trang, bien, vao;
    public ViTriDo(String ma, String trang, String bien, String vao) {
        this.ma=ma;
        this.trang=trang;
        this.bien=bien;
        this.vao=vao;
    }
    public String getMaViTri() {
        return ma;
    }
    public String getTrangThai() {
        return trang;
    }
    public String getBienSo() {
        return bien;
    }
    public String getThoiGianVao() {
        return vao;
    }
}
