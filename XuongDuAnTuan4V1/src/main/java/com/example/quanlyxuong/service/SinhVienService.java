package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.VaiTro;
import com.example.quanlyxuong.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SinhVienService {
    @Autowired
    private NguoiDungRepository nguoiDungRepo;

    public Page<NguoiDung> getAllSinhVien(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return nguoiDungRepo.danhSachNhanVien("Sinh viên", "Thành viên", pageable);
    }
    public NguoiDung getById(int id){
        return nguoiDungRepo.findById(id);
    }
    public void saveSinhVien(NguoiDung nguoiDung){
        nguoiDungRepo.save(nguoiDung);
    }
    public void deleteById(int id){
        nguoiDungRepo.deleteById(id);
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
    public boolean checkTrung(String maNguoiDung){
        return nguoiDungRepo.existsByMaNguoiDung(maNguoiDung);
    }
    public void updateTrangThai(Integer id, String newStatus) {
        NguoiDung nd = getById(id);
        if (nd != null) {
            nd.setTrangThai(newStatus);
            nguoiDungRepo.save(nd);
        }
    }
} 