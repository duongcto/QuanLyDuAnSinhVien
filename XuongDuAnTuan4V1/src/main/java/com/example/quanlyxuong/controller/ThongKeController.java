package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.repository.DuAnRepository;
import com.example.quanlyxuong.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThongKeController {
    @Autowired
    private DuAnRepository duAnRepository;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    // Trả về trang giao diện (nếu cần)
    @GetMapping("/statistics")
    public String statistics() {
        return "Admin/statistics";
    }
}
