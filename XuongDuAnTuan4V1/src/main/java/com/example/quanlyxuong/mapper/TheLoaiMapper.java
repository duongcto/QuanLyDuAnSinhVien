package com.example.quanlyxuong.mapper;

import com.example.quanlyxuong.dto.TheLoaiDto;
import com.example.quanlyxuong.entity.LoaiDuAn;

public class TheLoaiMapper {

    public static TheLoaiDto toDto(LoaiDuAn entity) {
        if (entity == null) return null;
        return new TheLoaiDto(
                entity.getId(),
                entity.getTenLoaiDuAn(),
                entity.getMoTa()
        );
    }

    public static LoaiDuAn toEntity(TheLoaiDto dto) {
        if (dto == null) return null;
        return new LoaiDuAn(
                dto.getId(),
                dto.getTenLoaiDuAn(),
                dto.getMoTa()
        );
    }


}
