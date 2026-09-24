# BTLQLBaiDoXe

Ứng dụng Android quản lý bãi đỗ xe, xây dựng bằng **Java + XML**, sử dụng **XML** để lưu trữ dữ liệu.

## 1. Chức năng chính

- Quản lý vị trí đỗ xe.
- Quản lý xe vào và xe ra.
- Tự động tạo mã vé.
- Tự động xác định loại xe theo khu.
- Tính phí gửi xe.
- Quản lý bảng giá.
- Quản lý lịch sử gửi xe.
- Tìm kiếm và lọc vị trí.
- Thống kê doanh thu và số lượng xe.

## 2. Phân khu bãi xe

| Khu | Loại xe | Vị trí |
|---|---|---|
| A | Ô tô | A01 – A10 |
| B | Xe máy | B01 – B10 |
| C | Xe đạp | C01 – C10 |

**Tổng cộng: 30 vị trí.**

Loại xe được xác định tự động:

```text
A → Ô tô
B → Xe máy
C → Xe đạp
```

Khi chọn ô trống trong Tab Sơ đồ, người dùng **chỉ nhập biển số**, không cần chọn lại loại xe.

## 3. Giao diện

Ứng dụng gồm 4 Tab:

### Thống kê
Hiển thị:

- Doanh thu.
- Lượt xe vào.
- Lượt xe ra.
- Tỷ lệ tải bãi.
- Trạng thái Khu A, B, C.
- Thống kê theo hôm nay, tuần này, tháng này.

Khi nhấn Khu A/B/C, ứng dụng chuyển sang Tab Sơ đồ và **tự cuộn đến khu được chọn**, đồng thời vẫn hiển thị đầy đủ cả 3 khu.

### Sơ đồ bãi
Chức năng:

- Hiển thị toàn bộ vị trí.
- Tìm kiếm theo mã ô hoặc biển số.
- Lọc `Tất cả / Trống / Có xe`.
- Chọn ô trống để gửi xe.
- Chọn ô có xe để thực hiện xe ra.

### Bảng giá
Cho phép:

- Xem bảng giá.
- Cập nhật giá gửi xe.
- Lưu thay đổi vào XML.

### Lịch sử
Hiển thị:

- Mã vé.
- Biển số.
- Loại xe.
- Vị trí.
- Thời gian vào.
- Thời gian ra.
- Trạng thái.
- Phí gửi xe.

Hỗ trợ tìm kiếm lịch sử.

## 4. Luồng gửi xe

```text
Chọn ô trống
    ↓
Xác định khu
    ↓
Tự xác định loại xe
    ↓
Nhập biển số
    ↓
Tạo mã vé
    ↓
Lưu thời gian vào
    ↓
Cập nhật XML
    ↓
Ô chuyển sang CÓ XE
```

## 5. Luồng xe ra

```text
Chọn ô có xe
    ↓
Đọc thông tin vé
    ↓
Tính thời gian gửi
    ↓
Tính phí
    ↓
Xác nhận xe ra
    ↓
Lưu lịch sử
    ↓
Cập nhật XML
    ↓
Ô chuyển về TRỐNG
```

## 6. Lưu trữ dữ liệu

Dữ liệu được lưu bằng XML:

```text
bang_gia.xml
so_do_bai_xe.xml
lich_su_gui_xe.xml
```

### `bang_gia.xml`
Lưu thông tin bảng giá.

### `so_do_bai_xe.xml`
Lưu trạng thái các vị trí và thông tin xe đang gửi.

### `lich_su_gui_xe.xml`
Lưu lịch sử xe vào, xe ra và phí gửi xe.

## 7. Công nghệ

```text
Java
XML
AndroidX
Material
Gradle
XML DOM
```

Cấu hình chính:

```text
compileSdk: 37
targetSdk: 37
minSdk: 24
```

## 8. Cấu trúc dự án

```text
BTLQLBaiDoXe/
│
├── app/
│   └── src/main/
│       ├── java/com/example/btlqlbaidoxe/
│       │   ├── ManHinhChinh.java
│       │   ├── TongQuanFragment.java
│       │   ├── SoDoFragment.java
│       │   ├── BangGiaFragment.java
│       │   ├── LichSuFragment.java
│       │   ├── XuLyXml.java
│       │   ├── ViTriDo.java
│       │   └── Ui.java
│       │
│       └── res/
│           ├── drawable/
│           ├── layout/
│           ├── menu/
│           ├── values/
│           └── xml/
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

## 9. Các lớp chính

| Lớp | Chức năng |
|---|---|
| `ManHinhChinh` | Quản lý Activity và chuyển Tab |
| `TongQuanFragment` | Thống kê và trạng thái khu |
| `SoDoFragment` | Sơ đồ, tìm kiếm, lọc, xe vào/ra |
| `BangGiaFragment` | Quản lý bảng giá |
| `LichSuFragment` | Hiển thị và tìm kiếm lịch sử |
| `XuLyXml` | Đọc, ghi và xử lý dữ liệu XML |
| `ViTriDo` | Mô hình vị trí đỗ |
| `Ui` | Hàm hỗ trợ tạo giao diện |

## 10. Cài đặt

### Clone project

```bash
git clone <URL_REPOSITORY>
```

### Mở project

Mở thư mục:

```text
BTLQLBaiDoXe
```

bằng Android Studio.

### Chạy

1. Gradle Sync.
2. Chọn Emulator hoặc điện thoại Android.
3. Nhấn `Run`.

## 11. Repository

**GitHub:** `trinhxuandat567-pixel`

**Repository:** `BTLQLBaiDoXe`

## 12. Hướng phát triển

Có thể mở rộng thêm:

- Đăng nhập và phân quyền.
- Quản lý nhân viên.
- QR Code.
- Nhận diện biển số bằng camera.
- SQLite/Room/Firebase.
- Xuất Excel/PDF.
- Đồng bộ dữ liệu trực tuyến.
- Thống kê và báo cáo nâng cao.

## 13. Tác giả

**trinhxuandat567-pixel**

---

### Tóm tắt

```text
Android + Java + XML
        ↓
Quản lý 30 vị trí
        ↓
A - Ô tô
B - Xe máy
C - Xe đạp
        ↓
Xe vào → Tính phí → Xe ra
        ↓
Lưu XML → Lịch sử → Thống kê
```
