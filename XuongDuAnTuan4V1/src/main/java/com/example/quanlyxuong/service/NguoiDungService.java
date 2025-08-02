package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.NguoiDung;

import java.util.List;

public interface NguoiDungService {

    List<NguoiDung> findAllNguoiDung();

    NguoiDung findById(Integer id );

    List<NguoiDung> nguoiDungChuaThamGiaDuAn();

    NguoiDung findIdNguoiDung(String email);

    List<NguoiDung> findAllNguoiDungDangHoc();

}
