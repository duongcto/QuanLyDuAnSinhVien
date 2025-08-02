package com.example.quanlyxuong.mapper;

import com.example.quanlyxuong.dto.BaoCaoDto;
import com.example.quanlyxuong.entity.BaoCao;

public class BaoCaoMapper {
    public static BaoCaoDto toDto(BaoCao baoCao) {
        if (baoCao == null) return null;

        BaoCaoDto dto = new BaoCaoDto();
        dto.setId(baoCao.getId());
        dto.setHomNayLamGi(baoCao.getHomNayLamGi());
        dto.setGapKhoKhanGi(baoCao.getGapKhoKhanGi());
        dto.setNgayMaiLamGi(baoCao.getNgayMaiLamGi());
        dto.setNgayBaoCao(baoCao.getNgayBaoCao());
        dto.setThoiGian(baoCao.getThoiGian());
        dto.setTrangThai(baoCao.getTrangThai());

        if (baoCao.getIdNguoiDung() != null) {
            dto.setHoTen(baoCao.getIdNguoiDung().getHoTen());
        }

        if (baoCao.getIdDuAn() != null) {
            dto.setTenDuAn(baoCao.getIdDuAn().getTenDuAn());
        }

        return dto;
    }
} 