package com.example.quanlyxuong.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DuAnDto {

    private int id;

    private String maDuAn;

    private String tenDuAn;

    private Date ngayBatDau;

    private Date ngayKetThuc;

    private String chuyenNganh;

    private String tienDo;

    private boolean trangThai;

    public DuAnDto(String maDuAn, String tenDuAn, Date ngayBatDau,
                   Date ngayKetThuc, String chuyenNganh, String tienDo, boolean trangThai) {
        this.maDuAn = maDuAn;
        this.tenDuAn = tenDuAn;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.chuyenNganh = chuyenNganh;
        this.tienDo = tienDo;
        this.trangThai = trangThai;
    }
}
