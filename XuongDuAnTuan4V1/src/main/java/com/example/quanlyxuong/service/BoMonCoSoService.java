package com.example.quanlyxuong.service;

import com.example.quanlyxuong.dto.BoMonCoSoDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BoMonCoSoService {
    Page<BoMonCoSoDto> getBoMonCoSoPage(int page, int size);
    BoMonCoSoDto getById(Integer idBoMon, Integer idCoSo);
    BoMonCoSoDto save(BoMonCoSoDto dto);
    void delete(Integer idBoMon, Integer idCoSo);
    List<BoMonCoSoDto> getAll();
} 