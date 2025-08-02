package com.example.quanlyxuong.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder()
@Table(name = "nguoi_dung", schema = "dbo")
@JsonIgnoreProperties({"idVaiTro", "idChucVu", "hibernateLazyInitializer", "handler"})
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nguoi_dung", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "ma_nguoi_dung", length = 50)
    @NotBlank(message = "Mã không được bỏ trống!")
    private String maNguoiDung;

    @Size(max = 50)
    @Column(name = "ten_dang_nhap", length = 50)
    private String tenDangNhap;

    @Size(max = 50)
    @Column(name = "ho_ten", length = 50)
    @NotBlank(message = "Họ tên không được bỏ trống!")
    private String hoTen;

    @Size(max = 100)
    @Column(name = "email_pt", length = 100)
    @NotBlank(message = "Email FPT không được bỏ trống!")
    private String emailPt;

    @Size(max = 100)
    @Column(name = "email_fe", length = 100)
    @NotBlank(message = "Email FE không được bỏ trống!")
    private String emailFe;

    @Size(max = 20)
    @Column(name = "so_dien_thoai", length = 20)
    @NotBlank(message = "Số điện thoại không được bỏ trống!")
    private String soDienThoai;

    @Size(max = 255)
    @Column(name = "mat_khau")
    private String matKhau;

    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @Column(name = "ngay_update")
    private LocalDate ngayUpdate;

    @Builder.Default
    @Column(name = "trang_thai")
    private String trangThai = "Đang học";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vai_tro")
    @NotNull(message = "Vai trò không được bỏ trống!")
    private VaiTro idVaiTro;

    @ManyToOne
    @JoinColumn(name = "id_chuc_vu")
    private ChucVu idChucVu;

}