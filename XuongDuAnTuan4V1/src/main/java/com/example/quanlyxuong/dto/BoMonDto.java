package com.example.quanlyxuong.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class BoMonDto {
    private Integer id;

    private String maBoMon;

    private String tenBoMon;

    private String trungBoMon;

    private Integer soThanhVien;

    private LocalDate ngayThanhLap;

    private Boolean trangThai;

    public BoMonDto(Integer id, String maBoMon, String tenBoMon, String trungBoMon, Integer soThanhVien, LocalDate ngayThanhLap, Boolean trangThai) {
        this.id = id;
        this.maBoMon = maBoMon;
        this.tenBoMon = tenBoMon;
        this.trungBoMon = trungBoMon;
        this.soThanhVien = soThanhVien;
        this.ngayThanhLap = ngayThanhLap;
        this.trangThai = trangThai;
    }

}
