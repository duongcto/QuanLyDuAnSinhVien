package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.DuAn;
import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.ThanhVienDuAn;

import java.util.List;

public interface ThanhVienDuAnService {

    List<ThanhVienDuAn> findAllThanhVienDuAn();


    ThanhVienDuAn findById(Integer idTv);

    void save(ThanhVienDuAn tv);

    List<ThanhVienDuAn> findAllThanhVienDuAnChuaCoDuAn();

    void deleteByIdDuAn( DuAn duAn);

    void deleteDuAn(DuAn duAn);

    ThanhVienDuAn findIdDuAn(NguoiDung nguoiDung);
}
