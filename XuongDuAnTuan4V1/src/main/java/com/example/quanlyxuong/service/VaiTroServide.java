package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.VaiTro;
import com.example.quanlyxuong.repository.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VaiTroServide {
    @Autowired
    private VaiTroRepository vaiTroRepository;

    public VaiTroServide(VaiTroRepository vaiTroRepository){
        this.vaiTroRepository = vaiTroRepository;
    }
    public List<VaiTro> getAllVaiTro(){
        return vaiTroRepository.findAll();
    }
    
    public List<VaiTro> getQuanLyAndThanhVien(){
        return vaiTroRepository.findAll().stream()
                .filter(vt -> "Quản Lý".equals(vt.getTenVaiTro()) || "Thành viên".equals(vt.getTenVaiTro()))
                .collect(java.util.stream.Collectors.toList());
    }
}
