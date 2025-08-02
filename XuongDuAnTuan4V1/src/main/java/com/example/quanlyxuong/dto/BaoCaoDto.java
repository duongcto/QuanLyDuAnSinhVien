package com.example.quanlyxuong.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BaoCaoDto {
    private Integer id;

    private String hoTen;

    private String tenDuAn;

    private String homNayLamGi;

    private String gapKhoKhanGi;

    private String ngayMaiLamGi;

    private LocalDate ngayBaoCao;

    private Double thoiGian;

    private Boolean trangThai;
}
