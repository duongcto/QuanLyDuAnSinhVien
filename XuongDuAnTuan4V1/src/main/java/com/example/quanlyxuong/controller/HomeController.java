package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.entity.CongViec;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    @GetMapping("/homeDangNhap")
    public String homeDangNhap(){
        return "Security/HomeDangNhap";
    }
    @GetMapping("/hienThiHome")
    public String hienThiHome(){
        return "Admin/Home";
    }
    @GetMapping("/homeAdmin")
    public String homeAdmin(){
        return "Admin/Home";
    }
    @GetMapping("/homeQuanLy")
    public String homeQuanLy(){
        return "QuanLy/HomeQuanLy";
    }
    @GetMapping("/homeThanhVien")
    public String homeThanhVien(){
        return "ThanhVien/HomeThanhVien";
    }
    @GetMapping("/quanLy/bang-cong-viec")
    public String bangCongViec() {
        return "redirect:/hien_thi_task";
    }

}
