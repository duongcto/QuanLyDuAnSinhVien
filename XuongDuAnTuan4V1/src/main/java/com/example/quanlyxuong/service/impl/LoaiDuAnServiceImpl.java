package com.example.quanlyxuong.service.impl;

import com.example.quanlyxuong.entity.LoaiDuAn;
import com.example.quanlyxuong.repository.LoaiDuAnRepository;
import com.example.quanlyxuong.service.LoaiDuAnService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoaiDuAnServiceImpl implements LoaiDuAnService {

    private final LoaiDuAnRepository loaiDuAnRepository;

    public LoaiDuAnServiceImpl(LoaiDuAnRepository loaiDuAnRepository) {
        this.loaiDuAnRepository = loaiDuAnRepository;
    }

    @Override
    public List<LoaiDuAn> findAllLoaiDuAn() {
        return loaiDuAnRepository.findAll();
    }
}
