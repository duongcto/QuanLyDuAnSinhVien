package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.dto.MemberDto;
import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class NguoiDungRestController {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @GetMapping("")
    public List<MemberDto> getAllUsers() {
        return nguoiDungRepository.findAll().stream()
                .map(u -> new MemberDto(u.getId(), u.getHoTen(),
                    u.getIdVaiTro() != null ? u.getIdVaiTro().getTenVaiTro() : null))
                .collect(Collectors.toList());
    }
} 