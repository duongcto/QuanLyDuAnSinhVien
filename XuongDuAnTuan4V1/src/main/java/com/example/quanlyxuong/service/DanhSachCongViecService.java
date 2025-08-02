package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.DanhSachCongViec;
import com.example.quanlyxuong.entity.DuAn;
import com.example.quanlyxuong.repository.DanhSachCongViecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DanhSachCongViecService {
    @Autowired
    private DanhSachCongViecRepository danhSachCongViecRepository;

    public List<DanhSachCongViec> getAll() {
        return danhSachCongViecRepository.findAll();
    }

    public void deleteDuAn(DuAn duAn) {
        danhSachCongViecRepository.deleteById(duAn.getId());
    }

}
