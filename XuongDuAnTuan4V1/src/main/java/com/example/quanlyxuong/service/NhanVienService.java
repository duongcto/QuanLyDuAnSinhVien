package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.VaiTro;
import com.example.quanlyxuong.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NhanVienService {
    @Autowired
    private NguoiDungRepository nguoiDungRepo;

    public NhanVienService(NguoiDungRepository nguoiDungRepository){
        this.nguoiDungRepo=nguoiDungRepository;
    }

    public Page<NguoiDung> getAllNguoiDungs(String keyword1, String keyword2, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return nguoiDungRepo.danhSachNhanVien(keyword1, keyword2, pageable);
    }
    public NguoiDung getById(int id){
        return nguoiDungRepo.findById(id);
    }

    public void saveNhanVien(NguoiDung nguoiDung){
        nguoiDungRepo.save(nguoiDung);
    }

    public Page<NguoiDung> locVaiTro(VaiTro vaiTro, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return nguoiDungRepo.findByIdVaiTro(vaiTro, pageable);
    }
    public Page<NguoiDung> locTrangThai(String trangThai, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return nguoiDungRepo.findByTrangThai(trangThai, pageable);
    }
    public Page<NguoiDung> timKiem(String tuKhoa,int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return nguoiDungRepo.findByHoTenOrEmailFeOrEmailPtOrSoDienThoai(tuKhoa,tuKhoa,tuKhoa,tuKhoa, pageable);
    }

    // Trong EmployeeService.java
    public void updateTrangThai(Integer id, String trangThaiMoi) {
        NguoiDung employee = nguoiDungRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với ID: " + id));
        employee.setTrangThai(trangThaiMoi);
        nguoiDungRepo.save(employee);
    }

    public boolean checkTrung(String maNguoiDung){
        return nguoiDungRepo.existsByMaNguoiDung(maNguoiDung);
    }
}
