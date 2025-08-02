-- Xóa database cũ nếu tồn tại và tạo mới hoàn toàn
USE master;
GO

IF DB_ID('TestXuong') IS NOT NULL
BEGIN
    ALTER DATABASE TestXuong SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE TestXuong;
END
GO

CREATE DATABASE TestXuong;
GO

USE TestXuong;
GO

-- 1. co_so
CREATE TABLE co_so (
    id_co_so INT IDENTITY(1,1) PRIMARY KEY,
    ma_co_so VARCHAR(50),
    ten_co_so NVARCHAR(100),
    dia_chi NVARCHAR(255),
    so_dien_thoai VARCHAR(20),
    email VARCHAR(100),
    ngay_thanh_lap DATE,
    ngay_tao DATE,
    ngay_update DATE,
    trang_thai BIT
);

-- 2. vai_tro
CREATE TABLE vai_tro (
    id_vai_tro INT IDENTITY(1,1) PRIMARY KEY,
    ma_vai_tro VARCHAR(50),
    ten_vai_tro NVARCHAR(100),
    id_co_so INT,
    mo_ta NVARCHAR(255),
    FOREIGN KEY (id_co_so) REFERENCES co_so(id_co_so)
);

-- 3. chuc_vu
CREATE TABLE chuc_vu(
    id_chuc_vu INT IDENTITY(1,1) PRIMARY KEY,
    ma_chuc_vu VARCHAR(50),
    ten_chuc_vu NVARCHAR(100),
    mo_ta NVARCHAR(255),
    trang_thai BIT
);

-- 4. nguoi_dung
CREATE TABLE nguoi_dung (
    id_nguoi_dung INT IDENTITY(1,1) PRIMARY KEY,
    ma_nguoi_dung VARCHAR(50),
    ten_dang_nhap NVARCHAR(50),
    ho_ten NVARCHAR(100),
    email_pt VARCHAR(100),
    email_fe VARCHAR(100),
    so_dien_thoai VARCHAR(20),
    mat_khau VARCHAR(255),
    ngay_tao DATE,
    ngay_update DATE,
    trang_thai NVARCHAR(20),
    id_vai_tro INT,
    id_chuc_vu INT,
    FOREIGN KEY (id_vai_tro) REFERENCES vai_tro(id_vai_tro),
    FOREIGN KEY (id_chuc_vu) REFERENCES chuc_vu(id_chuc_vu)
);

-- 5. bo_mon
CREATE TABLE bo_mon (
    id_bo_mon INT IDENTITY(1,1) PRIMARY KEY,
    ma_bo_mon VARCHAR(50),
    ten_bo_mon NVARCHAR(100),
    trung_bo_mon NVARCHAR(100),
    so_thanh_vien INT,
    mo_ta_chuc_nang NVARCHAR(255),
    ngay_thanh_lap DATE,
    ngay_tao DATE,
    ngay_update DATE,
    trang_thai BIT
);

-- 6. bo_mon_co_so
CREATE TABLE bo_mon_co_so (
    id_bo_mon INT,
    id_co_so INT,
    PRIMARY KEY (id_bo_mon, id_co_so),
    FOREIGN KEY (id_bo_mon) REFERENCES bo_mon(id_bo_mon),
    FOREIGN KEY (id_co_so) REFERENCES co_so(id_co_so)
);

-- 7. chuyen_nganh
CREATE TABLE chuyen_nganh (
    id_chuyen_nganh INT IDENTITY(1,1) PRIMARY KEY,
    ma_chuyen_nganh VARCHAR(50),
    ten_chuyen_nganh NVARCHAR(100),
    mo_ta NVARCHAR(255),
    id_bo_mon INT,
    ngay_tao DATE,
    ngay_update DATE,
    trang_thai BIT,
    FOREIGN KEY (id_bo_mon) REFERENCES bo_mon(id_bo_mon)
);

-- 8. chuyen_nganh_bo_mon_co_so
CREATE TABLE chuyen_nganh_bo_mon_co_so (
    id_chuyen_nganh INT,
    id_bo_mon INT,
    id_co_so INT,
    PRIMARY KEY (id_chuyen_nganh, id_bo_mon, id_co_so),
    FOREIGN KEY (id_chuyen_nganh) REFERENCES chuyen_nganh(id_chuyen_nganh),
    FOREIGN KEY (id_bo_mon) REFERENCES bo_mon(id_bo_mon),
    FOREIGN KEY (id_co_so) REFERENCES co_so(id_co_so)
);

-- 9. loai_du_an
CREATE TABLE loai_du_an (
    id_loai_du_an INT IDENTITY(1,1) PRIMARY KEY,
    ten_loai_du_an NVARCHAR(100),
    mo_ta NVARCHAR(255)
);

-- 10. du_an
CREATE TABLE du_an (
    id_du_an INT IDENTITY(1,1) PRIMARY KEY,
    ma_du_an VARCHAR(50),
    ten_du_an NVARCHAR(100),
    ngay_update DATE,
    id_bo_mon INT,
    id_co_so INT,
    id_chuyen_nganh INT,
    id_loai_du_an INT,
    ngay_bat_dau DATE,
    ngay_ket_thuc_du_kien DATE,
    trang_thai BIT,
    FOREIGN KEY (id_bo_mon) REFERENCES bo_mon(id_bo_mon),
    FOREIGN KEY (id_co_so) REFERENCES co_so(id_co_so),
    FOREIGN KEY (id_chuyen_nganh) REFERENCES chuyen_nganh(id_chuyen_nganh),
    FOREIGN KEY (id_loai_du_an) REFERENCES loai_du_an(id_loai_du_an)
);

-- 11. giai_doan
CREATE TABLE giai_doan (
    id_giai_doan INT IDENTITY(1,1) PRIMARY KEY,
    id_du_an INT NOT NULL,
    ten_giai_doan NVARCHAR(255) NOT NULL,
    ngay_bat_dau DATE,
    ngay_ket_thuc DATE,
    ngay_update DATETIME DEFAULT GETDATE(),
    trang_thai BIT,
    FOREIGN KEY (id_du_an) REFERENCES du_an(id_du_an)
);

-- 12. danh_sach_cong_viec
CREATE TABLE danh_sach_cong_viec (
    id_danh_sach_cong_viec INT IDENTITY(1,1) PRIMARY KEY,
    id_du_an INT,
    id_giai_doan INT NULL,
    ten_danh_sach_cong_viec NVARCHAR(255),
    mo_ta NVARCHAR(255),
    ngay_tao DATE,
    ngay_update DATE,
    trang_thai BIT,
    FOREIGN KEY (id_du_an) REFERENCES du_an(id_du_an),
    FOREIGN KEY (id_giai_doan) REFERENCES giai_doan(id_giai_doan)
);

-- 13. phan_cong
CREATE TABLE phan_cong (
    id_phan_cong INT IDENTITY(1,1) PRIMARY KEY,
    id_danh_sach_cong_viec INT,
    id_nguoi_dung INT,
    ngay_phan_cong DATE,
    FOREIGN KEY (id_danh_sach_cong_viec) REFERENCES danh_sach_cong_viec(id_danh_sach_cong_viec),
    FOREIGN KEY (id_nguoi_dung) REFERENCES nguoi_dung(id_nguoi_dung)
);

-- 14. kanban_column
CREATE TABLE kanban_column (
    id_column INT IDENTITY(1,1) PRIMARY KEY,
    column_name NVARCHAR(100) NOT NULL,
    column_order INT NOT NULL,
    mo_ta NVARCHAR(255),
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_update DATETIME DEFAULT GETDATE(),
    trang_thai BIT DEFAULT 1
);

-- 15. cong_viec
CREATE TABLE cong_viec (
    id_cong_viec INT IDENTITY(1,1) PRIMARY KEY,
    id_danh_sach_cong_viec INT,
    id_phan_cong INT,
    id_column INT NULL,
    ten_cong_viec NVARCHAR(255) NOT NULL,
    do_uu_tien NVARCHAR(50),
    ngay_het_han DATE,
    trang_thai BIT DEFAULT 0,
    nhan_dan NVARCHAR(255),
    kieu_cv NVARCHAR(255),
    position INT DEFAULT 0,
    tai_anh NVARCHAR(MAX),
    mo_ta NVARCHAR(MAX),
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_update DATETIME DEFAULT GETDATE(),
    link_dinh_kem NVARCHAR(MAX),
    ngay_bat_dau DATE,
    ngay_ket_thuc DATE,
    thoi_gian FLOAT,
    status NVARCHAR(32) DEFAULT 'TODO',
    thanh_vien NVARCHAR(255),
    FOREIGN KEY (id_danh_sach_cong_viec) REFERENCES danh_sach_cong_viec(id_danh_sach_cong_viec),
    FOREIGN KEY (id_column) REFERENCES kanban_column(id_column),
    FOREIGN KEY (id_phan_cong) REFERENCES phan_cong(id_phan_cong)
);

-- 16. binh_chon_giai_doan
CREATE TABLE binh_chon_giai_doan (
    id_binh_chon_giai_doan INT IDENTITY(1,1) PRIMARY KEY,
    id_giai_doan INT,
    ngay_ket_thuc DATE,
    ngay_tao DATE,
    ngay_sua DATE,
    FOREIGN KEY (id_giai_doan) REFERENCES giai_doan(id_giai_doan)
);

-- 17. bao_cao
CREATE TABLE bao_cao (
    id_bao_cao INT IDENTITY(1,1) PRIMARY KEY,
    id_nguoi_dung INT,
    id_du_an INT,
    ngay_bao_cao DATE,
    ngay_tao DATE,
    ngay_update DATE,
    hom_nay_lam_gi NVARCHAR(255),
    gap_kho_khan_gi NVARCHAR(255),
    ngay_mai_lam_gi NVARCHAR(255),
    thoi_gian FLOAT,
    trang_thai BIT,
    FOREIGN KEY (id_nguoi_dung) REFERENCES nguoi_dung(id_nguoi_dung),
    FOREIGN KEY (id_du_an) REFERENCES du_an(id_du_an)
);

-- 18. thanh_vien_du_an
CREATE TABLE thanh_vien_du_an (
    id_thanh_vien INT IDENTITY(1,1) PRIMARY KEY,
    id_du_an INT,
    id_nguoi_dung INT,
    vai_tro NVARCHAR(100),
    ngay_tham_gia DATE,
    ngay_ket_thuc DATE,
    trang_thai BIT,
    id_vai_tro INT,
    FOREIGN KEY (id_du_an) REFERENCES du_an(id_du_an),
    FOREIGN KEY (id_nguoi_dung) REFERENCES nguoi_dung(id_nguoi_dung),
    FOREIGN KEY (id_vai_tro) REFERENCES vai_tro(id_vai_tro)
);

-- 19. binhchon_cv
CREATE TABLE binhchon_cv (
    id_binh_chon_cv INT IDENTITY(1,1) PRIMARY KEY,
    id_thanh_vien INT NOT NULL,
    id_binh_chon_giai_doan INT NOT NULL,
    id_cong_viec INT NOT NULL,
    ngay_tao DATE,
    ngay_sua DATE,
    trang_thai TINYINT,
    FOREIGN KEY (id_thanh_vien) REFERENCES thanh_vien_du_an(id_thanh_vien),
    FOREIGN KEY (id_binh_chon_giai_doan) REFERENCES binh_chon_giai_doan(id_binh_chon_giai_doan),
    FOREIGN KEY (id_cong_viec) REFERENCES cong_viec(id_cong_viec)
);

-- 20. thong_bao
CREATE TABLE thong_bao (
    id_thong_bao INT IDENTITY(1,1) PRIMARY KEY,
    tieu_de VARCHAR(255),
    noi_dung NVARCHAR(100),
    ngay_tao DATE,
    id_nguoi_dung INT,
    FOREIGN KEY (id_nguoi_dung) REFERENCES nguoi_dung(id_nguoi_dung)
);

-- 21. comment
CREATE TABLE comment (
    id_comment INT IDENTITY(1,1) PRIMARY KEY,
    noi_dung NVARCHAR(100),
    ngay_binh_luan DATE,
    id_nguoi_dung INT,
    FOREIGN KEY (id_nguoi_dung) REFERENCES nguoi_dung(id_nguoi_dung)
);

-- 22. attachment
CREATE TABLE attachment (
    id_attachment INT IDENTITY(1,1) PRIMARY KEY,
    ten_file NVARCHAR(255),
    duong_dan NVARCHAR(500),
    kich_thuoc BIGINT,
    loai_file NVARCHAR(100),
    ngay_tao DATETIME DEFAULT GETDATE(),
    id_cong_viec INT,
    FOREIGN KEY (id_cong_viec) REFERENCES cong_viec(id_cong_viec)
);

-- 23. label
CREATE TABLE label (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255),
    color NVARCHAR(50)
);

-- 24. task_label
CREATE TABLE task_label (
    id INT IDENTITY(1,1) PRIMARY KEY,
    task_id INT,
    label_id INT,
    FOREIGN KEY (task_id) REFERENCES cong_viec(id_cong_viec),
    FOREIGN KEY (label_id) REFERENCES label(id)
);

-- 25. task_member
CREATE TABLE task_member (
    id INT IDENTITY(1,1) PRIMARY KEY,
    task_id INT,
    user_id INT,
    FOREIGN KEY (task_id) REFERENCES cong_viec(id_cong_viec),
    FOREIGN KEY (user_id) REFERENCES nguoi_dung(id_nguoi_dung)
);

GO

------------------------------------------------------------------------------------
-- CHÈN DỮ LIỆU THEO THỨ TỰ ĐÚNG
------------------------------------------------------------------------------------

-- BƯỚC 1: Các bảng KHÔNG có khóa ngoại
PRINT N'Bước 1: Chèn dữ liệu cho các bảng không có khóa ngoại...';

-- co_so
INSERT INTO co_so (ma_co_so, ten_co_so, dia_chi, so_dien_thoai, email, ngay_thanh_lap, ngay_tao, ngay_update, trang_thai)
VALUES
('CS_HN', N'FPT Polytechnic Hà Nội', N'Tòa nhà FPT, 17 Duy Tân, Cầu Giấy, Hà Nội', '02473001955', 'fpl.hn@fpt.edu.vn', '2010-07-01', GETDATE(), GETDATE(), 1),
('CS_HCM', N'FPT Polytechnic TP.HCM', N'391A Nam Kỳ Khởi Nghĩa, Quận 3, TP.HCM', '02835268799', 'fpl.hcm@fpt.edu.vn', '2010-07-01', GETDATE(), GETDATE(), 1),
('CS_DN', N'FPT Polytechnic Đà Nẵng', N'137 Nguyễn Thị Thập, Quận Liên Chiểu, Đà Nẵng', '02363710999', 'fpl.dn@fpt.edu.vn', '2012-08-15', GETDATE(), GETDATE(), 1);

-- chuc_vu
INSERT INTO chuc_vu (ma_chuc_vu, ten_chuc_vu, mo_ta, trang_thai)
VALUES
('GV', N'Giảng viên', N'Người hướng dẫn, giảng dạy sinh viên', 1),
('TBM', N'Trưởng bộ môn', N'Người quản lý một bộ môn cụ thể', 1),
('SV', N'Sinh viên', N'Học viên đang theo học tại trường', 1),
('QTV', N'Quản trị viên', N'Quản trị hệ thống', 1);

-- bo_mon
INSERT INTO bo_mon (ma_bo_mon, ten_bo_mon, trung_bo_mon, so_thanh_vien, mo_ta_chuc_nang, ngay_thanh_lap, ngay_tao, ngay_update, trang_thai)
VALUES
('PTPM', N'Phát triển phần mềm', N'Nguyễn Công T', 20, N'Đào tạo và phát triển các ứng dụng phần mềm', '2011-01-10', GETDATE(), GETDATE(), 1),
('TKTW', N'Thiết kế trang web', N'Lê Thị B', 15, N'Đào tạo về thiết kế và lập trình web', '2011-02-15', GETDATE(), GETDATE(), 1),
('UDPM', N'Ứng dụng phần mềm', N'Trần Văn A', 25, N'Đào tạo chuyên sâu về các ứng dụng phần mềm thực tế', '2012-05-20', GETDATE(), GETDATE(), 1);

-- loai_du_an
INSERT INTO loai_du_an (ten_loai_du_an, mo_ta)
VALUES
(N'Dự án mẫu', N'Dự án thực hiện theo các yêu cầu có sẵn để làm quen quy trình'),
(N'Dự án tốt nghiệp', N'Dự án cuối khóa, yêu cầu tính sáng tạo và hoàn thiện cao'),
(N'Dự án xưởng', N'Dự án thực hành tại xưởng thực tập doanh nghiệp');

-- kanban_column
INSERT INTO kanban_column (column_name, column_order, mo_ta, trang_thai)
VALUES
(N'Cần làm (To Do)', 1, N'Các công việc chưa bắt đầu', 1),
(N'Đang làm (In Progress)', 2, N'Các công việc đang được thực hiện', 1),
(N'Đang chờ duyệt (Review)', 3, N'Các công việc đã hoàn thành và chờ đánh giá', 1),
(N'Hoàn thành (Done)', 4, N'Các công việc đã được duyệt và kết thúc', 1);

-- label
INSERT INTO label (name, color) VALUES
(N'Bug', '#d73a4a'),
(N'Feature', '#007bff'),
(N'Documentation', '#0075ca'),
(N'UI/UX', '#a2eeef'),
(N'Backend', '#d876e3');

PRINT N'Bước 1 hoàn thành!';

-- BƯỚC 2: Các bảng phụ thuộc cấp 1
PRINT N'Bước 2: Chèn dữ liệu cho các bảng phụ thuộc cấp 1...';

--- Chèn dữ liệu cho vai_tro
INSERT INTO vai_tro (ma_vai_tro, ten_vai_tro, id_co_so, mo_ta)
VALUES
    ('VT001', N'Admin', 1, N'Quản lý toàn bộ hệ thống'),
    ('VT002', N'Quản Lý', 1, N'Xử lý công việc hàng ngày'),
    ('VT003', N'Thành viên', 1, N'Người dùng cuối truy cập dịch vụ');

-- chuyen_nganh (phụ thuộc vào bo_mon)
INSERT INTO chuyen_nganh (ma_chuyen_nganh, ten_chuyen_nganh, mo_ta, id_bo_mon, ngay_tao, ngay_update, trang_thai)
VALUES
('LTW', N'Lập trình Web', N'Chuyên ngành về phát triển các ứng dụng web', 2, GETDATE(), GETDATE(), 1),
('LTMB', N'Lập trình Mobile', N'Chuyên ngành về phát triển ứng dụng di động', 1, GETDATE(), GETDATE(), 1),
('KTDH', N'Thiết kế đồ họa', N'Chuyên ngành về thiết kế đồ họa và đa phương tiện', 2, GETDATE(), GETDATE(), 1);

PRINT N'Bước 2 hoàn thành!';

-- BƯỚC 3: Các bảng phụ thuộc cấp 2
PRINT N'Bước 3: Chèn dữ liệu cho các bảng phụ thuộc cấp 2...';
--- Chèn dữ liệu cho nguoi_dung
INSERT INTO nguoi_dung (ma_nguoi_dung, ten_dang_nhap, ho_ten, email_pt, email_fe, so_dien_thoai, mat_khau, ngay_tao, ngay_update, trang_thai, id_vai_tro, id_chuc_vu)
VALUES
    ('ND001', N'admin01',N'Lưu Sơn Trường', 'admin01@pt.com', 'admin01@fe.com', '0901234567', 'hashed_password_1', '2025-07-03', '2025-07-03', N'Đang học', 1,1),
    ('ND002', N'hungtdh',N'Trần Đức Hùng', 'hungtdhph48600@gmail.com', 'hungtdhph48600@gmail.com', '0912345678', 'tdh', '2025-07-03', '2025-07-03', N'Đang học', 2,1),
    ('ND003', N'thanhvien', N'Nguyễn Tùng Dương','thanhvien01@pt.com', 'thanhvien01@fe.com', '0923456789', 'hashed_password_3', '2025-07-03', '2025-07-03', N'Dừng học', 3, 2),
    ('ND004', N'truongls48958',N'Lưu Sơn Trường', 'truonglsph48958@gmail.com', 'truonglsph48958@gmail.com', '0901234567', 'LST', '2025-07-03', '2025-07-03', N'Đang học', 1, 1),
	('ND004', N'truong',N'Lưu Sơn Trường', 'truongsivar@gmail.com', 'truongsivar@gmail.com', '0901234567', 'luusontruong', '2025-07-03', '2025-07-03', N'Đang học', 3, 1);

-- bo_mon_co_so (phụ thuộc vào bo_mon và co_so)
INSERT INTO bo_mon_co_so (id_bo_mon, id_co_so)
VALUES
(1, 1), -- PTPM ở Hà Nội
(2, 1), -- TKTW ở Hà Nội
(1, 2), -- PTPM ở TP.HCM
(3, 2), -- UDPM ở TP.HCM
(2, 3), -- TKTW ở Đà Nẵng
(3, 3); -- UDPM ở Đà Nẵng

-- chuyen_nganh_bo_mon_co_so (phụ thuộc vào chuyen_nganh, bo_mon, co_so)
INSERT INTO chuyen_nganh_bo_mon_co_so (id_chuyen_nganh, id_bo_mon, id_co_so)
VALUES
(1, 2, 1), -- Lập trình Web, bộ môn TKTW, tại Hà Nội
(2, 1, 1), -- Lập trình Mobile, bộ môn PTPM, tại Hà Nội
(1, 2, 2); -- Lập trình Web, bộ môn TKTW, tại TP.HCM

-- du_an (phụ thuộc vào bo_mon, co_so, chuyen_nganh, loai_du_an)
INSERT INTO du_an (ma_du_an, ten_du_an, ngay_update, id_bo_mon, id_co_so, id_chuyen_nganh, id_loai_du_an, ngay_bat_dau, ngay_ket_thuc_du_kien, trang_thai)
VALUES
('DA_WEB_BANHANG', N'Xây dựng Website Bán hàng Thời trang', GETDATE(), 2, 1, 1, 2, '2023-09-01', '2023-12-31', 1),
('DA_APP_DATVE', N'Ứng dụng Mobile Đặt vé xem phim', GETDATE(), 1, 1, 2, 1, '2023-10-15', '2024-01-15', 1);

PRINT N'Bước 3 hoàn thành!';

-- BƯỚC 4: Các bảng phụ thuộc cấp 3
PRINT N'Bước 4: Chèn dữ liệu cho các bảng phụ thuộc cấp 3...';

-- giai_doan (phụ thuộc vào du_an)
INSERT INTO giai_doan (id_du_an, ten_giai_doan, ngay_bat_dau, ngay_ket_thuc, trang_thai)
VALUES
(1, N'Giai đoạn 1: Phân tích yêu cầu', '2023-09-01', '2023-09-15', 1),
(1, N'Giai đoạn 2: Thiết kế hệ thống', '2023-09-16', '2023-09-30', 1),
(1, N'Giai đoạn 3: Lập trình', '2023-10-01', '2023-12-15', 1),
(2, N'Giai đoạn 1: Khảo sát và lên ý tưởng', '2023-10-15', '2023-10-30', 1);

-- thanh_vien_du_an (phụ thuộc vào du_an, nguoi_dung, vai_tro)
INSERT INTO thanh_vien_du_an (id_du_an, id_nguoi_dung, vai_tro, ngay_tham_gia, trang_thai, id_vai_tro)
VALUES
(1, 1, N'Giảng viên hướng dẫn', '2023-09-01', 1, 2), -- GV LongNT
(1, 3, N'Nhóm trưởng', '2023-09-01', 1, 3),        -- SV MinhDV
(1, 4, N'Thành viên', '2023-09-01', 1, 3),          -- SV HoangLT
(1, 5, N'Thành viên', '2023-09-01', 1, 3),          -- SV AnhTV
(2, 1, N'Giảng viên hướng dẫn', '2023-10-15', 1, 2); -- GV LongNT

-- danh_sach_cong_viec (phụ thuộc vào du_an, giai_doan)
INSERT INTO danh_sach_cong_viec (id_du_an, id_giai_doan, ten_danh_sach_cong_viec, mo_ta, ngay_tao, trang_thai)
VALUES
(1, 2, N'Thiết kế CSDL', N'Thiết kế các bảng cho cơ sở dữ liệu của dự án', GETDATE(), 1),
(1, 2, N'Thiết kế Giao diện', N'Thiết kế mockup và wireframe cho các trang chính', GETDATE(), 1),
(1, 3, N'Xây dựng Backend', N'Lập trình các API, logic xử lý phía server', GETDATE(), 1);

-- binh_chon_giai_doan (phụ thuộc vào giai_doan)
INSERT INTO binh_chon_giai_doan (id_giai_doan, ngay_ket_thuc, ngay_tao)
VALUES
(2, '2023-10-05', GETDATE()); -- Bình chọn cho giai đoạn 2: Thiết kế hệ thống

-- bao_cao (phụ thuộc vào nguoi_dung, du_an)
INSERT INTO bao_cao (id_nguoi_dung, id_du_an, ngay_bao_cao, hom_nay_lam_gi, gap_kho_khan_gi, ngay_mai_lam_gi, thoi_gian, trang_thai)
VALUES
(3, 1, '2023-10-20', N'Thiết kế xong bảng users, products', N'Chưa rõ về mối quan hệ giữa bảng order và order_details', N'Hỏi giảng viên về CSDL và bắt đầu thiết kế giao diện đăng nhập', 8, 1);

-- thong_bao (phụ thuộc vào nguoi_dung)
INSERT INTO thong_bao (tieu_de, noi_dung, ngay_tao, id_nguoi_dung)
VALUES
('Deadline Giai đoạn 2', N'Nhắc nhở: Giai đoạn 2 sẽ hết hạn vào 30/09/2023', '2023-09-28', 3);

-- comment (phụ thuộc vào nguoi_dung)
INSERT INTO comment (noi_dung, ngay_binh_luan, id_nguoi_dung)
VALUES
(N'Thiết kế này nhìn ổn đó team!', '2023-09-25', 1);

PRINT N'Bước 4 hoàn thành!';

-- BƯỚC 5: Các bảng phụ thuộc cấp 4
PRINT N'Bước 5: Chèn dữ liệu cho các bảng phụ thuộc cấp 4...';

-- phan_cong (phụ thuộc vào danh_sach_cong_viec, nguoi_dung)
INSERT INTO phan_cong (id_danh_sach_cong_viec, id_nguoi_dung, ngay_phan_cong)
VALUES
(1, 3, GETDATE()), -- Giao việc "Thiết kế CSDL" cho sinh viên MinhDV
(2, 4, GETDATE()), -- Giao việc "Thiết kế Giao diện" cho sinh viên HoangLT
(3, 5, GETDATE()); -- Giao việc "Xây dựng Backend" cho sinh viên AnhTV

PRINT N'Bước 5 hoàn thành!';

-- BƯỚC 6: Bảng core cong_viec
PRINT N'Bước 6: Chèn dữ liệu cho bảng cong_viec...';

INSERT INTO cong_viec (id_danh_sach_cong_viec, id_phan_cong, id_column, ten_cong_viec, do_uu_tien, ngay_het_han, trang_thai, nhan_dan, kieu_cv, position, status, thanh_vien)
VALUES
(1, 1, 1, N'Tạo bảng Users', N'Cao', '2023-09-20', 0, N'Database', N'Task', 1, 'TODO', N'3'), -- MinhDV
(1, 1, 1, N'Tạo bảng Products', N'Cao', '2023-09-22', 0, N'Database', N'Task', 2, 'TODO', N'3'),
(2, 2, 2, N'Thiết kế trang chủ', N'Trung bình', '2023-09-25', 0, N'UI/UX', N'Task', 1, 'IN_PROGRESS', N'4'), -- HoangLT
(3, 3, 1, N'Setup project ExpressJS', N'Cao', '2023-10-05', 0, N'Backend', N'Task', 1, 'TODO', N'5'); -- AnhTV

PRINT N'Bước 6 hoàn thành!';

-- BƯỚC 7: Các bảng liên kết cuối cùng
PRINT N'Bước 7: Chèn dữ liệu cho các bảng liên kết cuối cùng...';

-- attachment (đính kèm cho công việc)
INSERT INTO attachment (ten_file, duong_dan, kich_thuoc, loai_file, id_cong_viec)
VALUES
(N'sodo_csdl.png', '/attachments/sodo_csdl.png', 102400, 'image/png', 1),
(N'mockup_trangchu.fig', '/attachments/mockup_trangchu.fig', 512000, 'application/figma', 3);

-- task_label (gán nhãn cho công việc)
INSERT INTO task_label (task_id, label_id)
VALUES
(1, 5), -- Task 'Tạo bảng Users' có nhãn 'Backend'
(2, 5), -- Task 'Tạo bảng Products' có nhãn 'Backend'
(3, 4), -- Task 'Thiết kế trang chủ' có nhãn 'UI/UX'
(3, 2); -- Task 'Thiết kế trang chủ' cũng có nhãn 'Feature'

-- task_member (gán thành viên cụ thể cho công việc)
INSERT INTO task_member (task_id, user_id)
VALUES
(1, 3), -- MinhDV làm task 'Tạo bảng Users'
(2, 3), -- MinhDV làm task 'Tạo bảng Products'
(3, 4); -- HoangLT làm task 'Thiết kế trang chủ'

-- binhchon_cv (thành viên bình chọn cho công việc)
INSERT INTO binhchon_cv (id_thanh_vien, id_binh_chon_giai_doan, id_cong_viec, ngay_tao, trang_thai)
VALUES
(3, 1, 3, GETDATE(), 1), -- Thành viên dự án HoangLT (id_thanh_vien_du_an=3) bình chọn cho task "Thiết kế trang chủ" (id_cong_viec=3) trong đợt bình chọn số 1
(4, 1, 3, GETDATE(), 1); -- Thành viên dự án AnhTV (id_thanh_vien_du_an=4) cũng bình chọn cho task "Thiết kế trang chủ"

PRINT N'Bước 7 hoàn thành!';

PRINT N'=== CHÈN DỮ LIỆU THÀNH CÔNG! ===';
GO 

SELECT * FROM nguoi_dung