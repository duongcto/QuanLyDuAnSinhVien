package com.example.quanlyxuong.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChuyenNganhBoMonCoSoDto {
    private Integer idChuyenNganh;
    private Integer idBoMon;
    private Integer idCoSo;
    
    // Display fields
    private String tenChuyenNganh;
    private String maChuyenNganh;
    private String tenBoMon;
    private String tenCoSo;
    private Boolean trangThai;
} 