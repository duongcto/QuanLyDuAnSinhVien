package com.example.quanlyxuong.service;

import com.example.quanlyxuong.dto.ChuyenNganhBoMonCoSoDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChuyenNganhBoMonCoSoService {
    Page<ChuyenNganhBoMonCoSoDto> getChuyenNganhBoMonCoSoPage(int page, int size);
    ChuyenNganhBoMonCoSoDto getById(Integer idChuyenNganh, Integer idBoMon, Integer idCoSo);
    ChuyenNganhBoMonCoSoDto save(ChuyenNganhBoMonCoSoDto dto);
    void delete(Integer idChuyenNganh, Integer idBoMon, Integer idCoSo);
    List<ChuyenNganhBoMonCoSoDto> getAll();
    void update(Integer oldIdChuyenNganh, Integer oldIdBoMon, Integer oldIdCoSo, ChuyenNganhBoMonCoSoDto dto);
} 