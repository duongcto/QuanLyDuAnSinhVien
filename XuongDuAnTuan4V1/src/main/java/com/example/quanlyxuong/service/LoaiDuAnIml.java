package com.example.quanlyxuong.service;

import com.example.quanlyxuong.dto.TheLoaiDto;

import java.util.List;

public interface LoaiDuAnIml {
    List<TheLoaiDto> searchByTen(String tenLoaiDuAn);
}
