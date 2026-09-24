package com.example.btlqlbaidoxe;

import android.content.Context;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
public final class XuLyXml {
    private XuLyXml() {
    }
    public static final String BANG_GIA="bang_gia.xml", LICH_SU="lich_su_gui_xe.xml", SO_DO="so_do_bai_xe.xml";
    public static final String DANG_GUI="DangGui", DA_XUAT="DaXuat", CO_XE="DA_CO_XE", TRONG="TRONG";
    private static final long GIO=3600000L;
    public static String lay(Element e, String tag) {
        if(e==null)return "";
        NodeList n=e.getElementsByTagName(tag);
        if(n.getLength()==0)return "";
        String s=n.item(0).getTextContent();
        return s==null?"": s.trim();
    }
    private static long num(String s, long d) {
        try {
            return s==null||s.trim().isEmpty()?d: Long.parseLong(s.trim());
        } catch(Exception e) {
            return d;
        }
    }
    private static Element first(Document d, String tag) {
        NodeList n=d.getElementsByTagName(tag);
        return n.getLength()>0?(Element)n.item(0): null;
    }
    private static void add(Document d, Element p, String tag, String value) {
        Element e=d.createElement(tag);
        e.setTextContent(value==null?"": value);
        p.appendChild(e);
    }
    private static void set(Document d, Element p, String tag, String value) {
        NodeList n=p.getElementsByTagName(tag);
        if(n.getLength()>0)n.item(0).setTextContent(value==null?"": value);
        else add(d, p, tag, value);
    }
    private static Document newDoc(String root)throws Exception {
        Document d=DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        d.appendChild(d.createElement(root));
        return d;
    }
    private static Document doc(Context c, String file) {
        try {
            File f=new File(c.getFilesDir(), file);
            return f.exists()?DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f): null;
        } catch(Exception e) {
            return null;
        }
    }
    private static void save(Context c, Document d, String file)throws Exception {
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(d),
            new StreamResult(new File(c.getFilesDir(), file)));
    }
    private static void init(Context c, String file, String root) {
        if(new File(c.getFilesDir(), file).exists())return;
        try {
            save(c, newDoc(root), file);
        } catch(Exception ignored) {
        }
    }
    public static long[] docBangGia(Context c) {
        long[] g= {20000, 10000, 5000, 15000, 3000};
        File f=new File(c.getFilesDir(), BANG_GIA);
        if(!f.exists()) {
            luuBangGia(c, g);
            return g;
        }
        Document d=doc(c, BANG_GIA);
        if(d==null)return g;
        Element o=first(d, "OTo"), m=first(d, "XeMay"), b=first(d, "XeDap");
        if(o!=null) {
            g[0]=num(o.getAttribute("gia2GioDau"), g[0]);
            g[1]=num(o.getAttribute("giaGioTiepTheo"), g[1]);
        }
        if(m!=null) {
            g[2]=num(m.getAttribute("giaLuot"), g[2]);
            g[3]=num(m.getAttribute("giaQuaDem"), g[3]);
        }
        if(b!=null)g[4]=num(b.getAttribute("giaLuot"), g[4]);
        return g;
    }
    public static void luuBangGia(Context c, long[] g) {
        if(g==null||g.length<5)return;
        try {
            Document d=newDoc("BangGiaDichVu");
            Element r=d.getDocumentElement();
            Element o=d.createElement("OTo");
            o.setAttribute("gia2GioDau", String.valueOf(g[0]));
            o.setAttribute("giaGioTiepTheo", String.valueOf(g[1]));
            r.appendChild(o);
            Element m=d.createElement("XeMay");
            m.setAttribute("giaLuot", String.valueOf(g[2]));
            m.setAttribute("giaQuaDem", String.valueOf(g[3]));
            r.appendChild(m);
            Element b=d.createElement("XeDap");
            b.setAttribute("giaLuot", String.valueOf(g[4]));
            r.appendChild(b);
            save(c, d, BANG_GIA);
        } catch(Exception ignored) {
        }
    }
    public static long tinhPhi(Context c, String loai, long vao, long ra) {
        long[] g=docBangGia(c);
        long gio=Math.max(1, (Math.max(0, ra-vao)+GIO-1)/GIO);
        if(loai!=null&&loai.contains("Ô tô"))return gio<=2?g[0]: g[0]+(gio-2)*g[1];
        return loai!=null&&loai.contains("Xe máy")?(gio>12?g[3]: g[2]): g[4];
    }
    public static void taoSoDo(Context c) {
        if(new File(c.getFilesDir(), SO_DO).exists())return;
        try {
            Document d=newDoc("SoDoBaiXe");
            Element r=d.getDocumentElement();
            for(String k: new String[] {
                "A", "B", "C"
            }
            )for(int i=1; i<=10; i++) {
                Element e=d.createElement("ViTri");
                add(d, e, "MaViTri", String.format(Locale.getDefault(), "%s%02d", k, i));
                add(d, e, "TrangThai", TRONG);
                add(d, e, "BienSo", "");
                add(d, e, "ThoiGianVao", "");
                r.appendChild(e);
            }
            save(c, d, SO_DO);
        } catch(Exception ignored) {
        }
    }
    public static List<ViTriDo> layViTri(Context c) {
        taoSoDo(c);
        List<ViTriDo> a=new ArrayList<>();
        Document d=doc(c, SO_DO);
        if(d==null)return a;
        NodeList n=d.getElementsByTagName("ViTri");
        for(int i=0; i<n.getLength(); i++) {
            Element e=(Element)n.item(i);
            a.add(new ViTriDo(lay(e, "MaViTri"), lay(e, "TrangThai"), lay(e, "BienSo"), lay(e,
                "ThoiGianVao")));
        }
        return a;
    }
    public static boolean datViTri(Context c, String ma, String bien, String tg) {
        try {
            Document d=doc(c, SO_DO);
            if(d==null)return false;
            NodeList n=d.getElementsByTagName("ViTri");
            for(int i=0; i<n.getLength(); i++) {
                Element e=(Element)n.item(i);
                if(ma.equalsIgnoreCase(lay(e, "MaViTri"))) {
                    set(d, e, "TrangThai", bien==null||bien.trim().isEmpty()?TRONG: CO_XE);
                    set(d, e, "BienSo", bien);
                    set(d, e, "ThoiGianVao", tg);
                    save(c, d, SO_DO);
                    return true;
                }
            }
        } catch(Exception ignored) {
        }
        return false;
    }
    public static boolean themXe(Context c, String maVe, String bien, String loai, String viTri,
        String tg, long time) {
        init(c, LICH_SU, "DanhSachLuotGui");
        Document d=doc(c, LICH_SU);
        if(d==null)return false;
        try {
            Element e=d.createElement("LuotGui");
            add(d, e, "MaVe", maVe);
            add(d, e, "BienSo", bien);
            add(d, e, "LoaiXe", loai);
            add(d, e, "ViTri", viTri);
            add(d, e, "ThoiGianVao", tg);
            add(d, e, "TimeVaoMillis", String.valueOf(time));
            add(d, e, "ThoiGianRa", "");
            add(d, e, "TimeRaMillis", "0");
            add(d, e, "TongTien", "0");
            add(d, e, "TrangThai", DANG_GUI);
            d.getDocumentElement().appendChild(e);
            save(c, d, LICH_SU);
            if(datViTri(c, viTri, bien, tg))return true;
            d.getDocumentElement().removeChild(e);
            save(c, d, LICH_SU);
        } catch(Exception ignored) {
        }
        return false;
    }
    public static Element xeDangGui(Context c, String viTri) {
        Document d=doc(c, LICH_SU);
        if(d==null)return null;
        NodeList n=d.getElementsByTagName("LuotGui");
        for(int i=0; i<n.getLength(); i++) {
            Element e=(Element)n.item(i);
            if(viTri.equalsIgnoreCase(lay(e, "ViTri"))&&DANG_GUI.equalsIgnoreCase(lay(e,
                "TrangThai")))return e;
        }
        return null;
    }
    public static boolean xuatXe(Context c, String maVe, String tgRa, long timeRa, long tien) {
        Document d=doc(c, LICH_SU);
        if(d==null)return false;
        NodeList n=d.getElementsByTagName("LuotGui");
        for(int i=0; i<n.getLength(); i++) {
            Element e=(Element)n.item(i);
            if(!maVe.equals(lay(e, "MaVe")))continue;
            String viTri=lay(e, "ViTri"), old1=lay(e, "ThoiGianRa"), old2=lay(e,
                "TimeRaMillis"), old3=lay(e, "TongTien"), old4=lay(e, "TrangThai");
            try {
                set(d, e, "ThoiGianRa", tgRa);
                set(d, e, "TimeRaMillis", String.valueOf(timeRa));
                set(d, e, "TongTien", String.valueOf(tien));
                set(d, e, "TrangThai", DA_XUAT);
                save(c, d, LICH_SU);
                if(datViTri(c, viTri, "", ""))return true;
                set(d, e, "ThoiGianRa", old1);
                set(d, e, "TimeRaMillis", old2);
                set(d, e, "TongTien", old3);
                set(d, e, "TrangThai", old4);
                save(c, d, LICH_SU);
            } catch(Exception ignored) {
            }
            return false;
        }
        return false;
    }
    public static List<Record> lichSu(Context c) {
        List<Record> a=new ArrayList<>();
        Document d=doc(c, LICH_SU);
        if(d==null)return a;
        NodeList n=d.getElementsByTagName("LuotGui");
        for(int i=n.getLength()-1; i>=0; i--) {
            Element e=(Element)n.item(i);
            a.add(new Record(lay(e, "MaVe"), lay(e, "BienSo"), lay(e, "LoaiXe"), lay(e, "ViTri"), lay(e,
                "ThoiGianVao"), lay(e, "TrangThai"), num(lay(e, "TongTien"), 0)));
        }
        return a;
    }
    public static Stats thongKe(Context c, int type) {
        Stats s=new Stats();
        Document d=doc(c, LICH_SU);
        if(d==null)return s;
        long[] m=moc(System.currentTimeMillis(), type);
        NodeList n=d.getElementsByTagName("LuotGui");
        for(int i=0; i<n.getLength(); i++) {
            Element e=(Element)n.item(i);
            String tt=lay(e, "TrangThai");
            long vao=num(lay(e, "TimeVaoMillis"), 0);
            if(vao<=0)continue;
            if(DANG_GUI.equalsIgnoreCase(tt))s.dangDo++;
            if(vao>=m[0]&&vao<m[1])s.vo++;
            if(DA_XUAT.equalsIgnoreCase(tt)) {
                long ra=num(lay(e, "TimeRaMillis"), vao);
                if(ra>=m[0]&&ra<m[1]) {
                    s.ra++;
                    s.doanhThu+=num(lay(e, "TongTien"), 0);
                }
            }
        }
        return s;
    }
    private static long[] moc(long now, int type) {
        Calendar a=Calendar.getInstance(), b;
        a.setTimeInMillis(now);
        a.set(Calendar.HOUR_OF_DAY, 0);
        a.set(Calendar.MINUTE, 0);
        a.set(Calendar.SECOND, 0);
        a.set(Calendar.MILLISECOND, 0);
        if(type==1) {
            int d=a.get(Calendar.DAY_OF_WEEK)-Calendar.MONDAY;
            if(d<0)d+=7;
            a.add(Calendar.DAY_OF_YEAR, -d);
            b=(Calendar)a.clone();
            b.add(Calendar.DAY_OF_YEAR, 7);
        } else if(type==2) {
            a.set(Calendar.DAY_OF_MONTH, 1);
            b=(Calendar)a.clone();
            b.add(Calendar.MONTH, 1);
        } else {
            b=(Calendar)a.clone();
            b.add(Calendar.DAY_OF_YEAR, 1);
        }
        return new long[] {
            a.getTimeInMillis(), b.getTimeInMillis()
        };
    }
    public static class Record {
        public final String maVe, bienSo, loaiXe, viTri, thoiGianVao, trangThai;
        public final long tongTien;
        Record(String a, String b, String c, String d, String e, String f, long g) {
            maVe=a;
            bienSo=b;
            loaiXe=c;
            viTri=d;
            thoiGianVao=e;
            trangThai=f;
            tongTien=g;
        }
    }
    public static class Stats {
        public long doanhThu;
        public int vo, ra, dangDo;
    }
}
