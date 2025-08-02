package com.example.quanlyxuong.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "chuc_vu")
public class ChucVu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chuc_vu")
    private int idChucVu;
    @Column(name = "ma_chuc_vu")
    @NotBlank(message = "Mã chức vụ không được bỏ trống!")
    private String maChucVu;
    @Column(name = "ten_chuc_vu")
    @NotBlank(message = "Tên chức vụ không được bỏ trống!")
    private String tenChucVu;
    @Column(name = "mo_ta")
    @NotBlank(message = "Mô tả chức vụ không được bỏ trống!")
    private String moTa;
    @Column(name = "trang_thai")
    private Boolean trangThai = true;
}
