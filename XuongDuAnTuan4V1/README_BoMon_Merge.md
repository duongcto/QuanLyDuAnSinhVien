# Hướng dẫn Merge Phần Quản lý Bộ môn

## Tổng quan
Đã merge thành công phần quản lý "Bộ môn" từ project `XuongDuAn_Quang` vào `XuongDuAnTuan4V1` với đầy đủ tính năng.

## Các tính năng đã merge

### 1. Quản lý Bộ môn cơ bản
- CRUD operations cho Bộ môn
- Tìm kiếm và lọc dữ liệu
- Import/Export Excel
- Phân trang
- Validation dữ liệu

### 2. Quản lý Chuyên ngành
- CRUD operations cho Chuyên ngành
- Liên kết với Bộ môn
- Validation mã chuyên ngành

### 3. Quản lý Bộ môn theo Cơ sở
- Gán Bộ môn cho từng Cơ sở
- Hiển thị danh sách Bộ môn theo Cơ sở
- Phân trang

### 4. Quản lý Chuyên ngành theo Bộ môn và Cơ sở
- Gán Chuyên ngành cho Bộ môn tại từng Cơ sở
- Hiển thị danh sách chi tiết
- Phân trang

## Các file đã được tạo/cập nhật

### Controllers
- `BoMonController.java` - Cập nhật với JSON endpoints và ChuyenNganhService
- `ChuyenNganhController.java` - Mới tạo
- `BoMonCoSoController.java` - Mới tạo  
- `ChuyenNganhBoMonCoSoController.java` - Mới tạo

### DTOs
- `ChuyenNganhDto.java` - Mới tạo
- `BoMonCoSoDto.java` - Mới tạo
- `ChuyenNganhBoMonCoSoDto.java` - Mới tạo

### Services
- `ChuyenNganhService.java` - Mới tạo
- `BoMonCoSoService.java` - Mới tạo
- `ChuyenNganhBoMonCoSoService.java` - Mới tạo
- `CoSoService.java` - Cập nhật thêm method `getAll()`

### Service Implementations
- `ChuyenNganhServiceImpl.java` - Mới tạo
- `BoMonCoSoServiceImpl.java` - Mới tạo
- `ChuyenNganhBoMonCoSoServiceImpl.java` - Mới tạo
- `CoSoServiceImpl.java` - Cập nhật implementation cho `getAll()`

### Repositories
- `ChuyenNganhRepository.java` - Cập nhật thêm method `existsByMaChuyenNganh`

### HTML Templates
- `quanLyBoMon.html` - Cập nhật với navigation buttons và SweetAlert2
- `quanLyChuyenNganh.html` - Mới tạo
- `quanLyBoMonCoSo.html` - Mới tạo
- `quanLyChuyenNganhBoMonCoSo.html` - Mới tạo

### CSS
- `style.css` - Cập nhật với styling cho giao diện

## Cách sử dụng

### 1. Truy cập quản lý Bộ môn
```
/admin/boMon
```

### 2. Quản lý Chuyên ngành
```
/admin/boMon/chuyenNganh
```

### 3. Quản lý Bộ môn theo Cơ sở
```
/admin/boMon/boMonCoSo
```

### 4. Quản lý Chuyên ngành theo Bộ môn và Cơ sở
```
/admin/boMon/chuyenNganhBoMonCoSo
```

## API Endpoints

### Bộ môn
- `GET /api/boMon` - Lấy danh sách Bộ môn
- `POST /api/boMon` - Tạo Bộ môn mới
- `PUT /api/boMon/{id}` - Cập nhật Bộ môn
- `DELETE /api/boMon/{id}` - Xóa Bộ môn

### Chuyên ngành
- `GET /api/chuyenNganh` - Lấy danh sách Chuyên ngành
- `POST /api/chuyenNganh` - Tạo Chuyên ngành mới
- `PUT /api/chuyenNganh/{id}` - Cập nhật Chuyên ngành
- `DELETE /api/chuyenNganh/{id}` - Xóa Chuyên ngành

### Bộ môn theo Cơ sở
- `GET /api/boMonCoSo` - Lấy danh sách Bộ môn theo Cơ sở
- `POST /api/boMonCoSo` - Tạo liên kết Bộ môn-Cơ sở
- `DELETE /api/boMonCoSo/{idBoMon}/{idCoSo}` - Xóa liên kết

### Chuyên ngành theo Bộ môn và Cơ sở
- `GET /api/chuyenNganhBoMonCoSo` - Lấy danh sách Chuyên ngành theo Bộ môn và Cơ sở
- `POST /api/chuyenNganhBoMonCoSo` - Tạo liên kết Chuyên ngành-Bộ môn-Cơ sở
- `DELETE /api/chuyenNganhBoMonCoSo/{idChuyenNganh}/{idBoMon}/{idCoSo}` - Xóa liên kết

## Tính năng đặc biệt

### 1. SweetAlert2 Notifications
- Thông báo thành công/lỗi với animation đẹp mắt
- Toast notifications không gây gián đoạn

### 2. Modal Integration
- Quản lý Chuyên ngành theo Cơ sở được hiển thị trong modal
- Giao diện responsive và user-friendly

### 3. Validation
- Kiểm tra mã bộ môn/chuyên ngành trùng lặp
- Validation dữ liệu đầu vào
- Thông báo lỗi rõ ràng

### 4. Pagination
- Phân trang cho tất cả danh sách
- Navigation dễ dàng giữa các trang

## Lưu ý
- Đảm bảo database có đủ các bảng cần thiết
- Kiểm tra các foreign key constraints
- Test kỹ các chức năng trước khi deploy production 