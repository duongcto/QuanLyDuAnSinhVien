package com.example.quanlyxuong.service;

import com.example.quanlyxuong.dto.BaoCaoDto;
import com.example.quanlyxuong.entity.BaoCao;
import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.ThanhVienDuAn;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

public interface BaoCaoService {
    List<BaoCaoDto> getBaoCaoList();

    List<BaoCaoDto> getBaoCaoByDate(String ngay);

    void exportBaoCaoTheoThang(OutputStream outputStream, int month, int year) throws IOException;

}
