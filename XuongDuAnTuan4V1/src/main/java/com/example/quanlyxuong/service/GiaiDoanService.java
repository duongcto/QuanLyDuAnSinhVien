package com.example.quanlyxuong.service;


import com.example.quanlyxuong.entity.GiaiDoan;
import com.example.quanlyxuong.repository.GiaiDoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GiaiDoanService {
    @Autowired
    private GiaiDoanRepository giaiDoanRepository;

    public List<GiaiDoan> getAll() {
        return giaiDoanRepository.findAll();
    }
}
