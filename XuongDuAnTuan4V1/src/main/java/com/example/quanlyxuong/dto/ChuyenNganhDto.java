package com.example.quanlyxuong.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChuyenNganhDto {
    private Integer id;
    private String maChuyenNganh;
    private String tenChuyenNganh;
    private Integer idBoMon;
    private String tenBoMon;
    private Boolean trangThai;
} 