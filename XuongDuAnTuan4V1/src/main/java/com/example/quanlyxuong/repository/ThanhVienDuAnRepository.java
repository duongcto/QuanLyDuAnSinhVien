package com.example.quanlyxuong.repository;

import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.ThanhVienDuAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThanhVienDuAnRepository extends JpaRepository<ThanhVienDuAn, Integer> {

  List<ThanhVienDuAn> findByIdDuAnIsNull();

  ThanhVienDuAn findThanhVienDuAnByIdNguoiDung(NguoiDung idNguoiDung);

  ThanhVienDuAn findByIdNguoiDung_Id(Integer idNguoiDung);

  List<ThanhVienDuAn> findByIdDuAn_Id(Integer idDuAn);
}