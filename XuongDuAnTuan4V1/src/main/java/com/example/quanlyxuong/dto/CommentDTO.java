package com.example.quanlyxuong.dto;

import java.time.LocalDate;

public class CommentDTO {
    public Integer id;
    public String noiDung;
    public LocalDate ngayBinhLuan;
    public String tenNguoiDung;

    public CommentDTO(Integer id, String noiDung, LocalDate ngayBinhLuan, String tenNguoiDung) {
        this.id = id;
        this.noiDung = noiDung;
        this.ngayBinhLuan = ngayBinhLuan;
        this.tenNguoiDung = tenNguoiDung;
    }
} 