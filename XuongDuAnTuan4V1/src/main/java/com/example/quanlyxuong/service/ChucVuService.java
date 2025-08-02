package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.ChucVu;
import com.example.quanlyxuong.repository.ChucVuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChucVuService {
    @Autowired
    private ChucVuRepository chucVuRepo;

    public ChucVuService(ChucVuRepository chucVuRepo){
        this.chucVuRepo=chucVuRepo;
    }

    public Page<ChucVu> getAllChucVu(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return chucVuRepo.findAll(pageable);
    }

    public void addChucVu(ChucVu chucvu){
        chucVuRepo.save(chucvu);
    }
    public ChucVu getCVById(int id){
        return chucVuRepo.findById(id).orElseGet(() -> null);
    }

    public void deleteChucVu(int id){
        chucVuRepo.deleteById(id);
    }

    public Page<ChucVu> locTheoTrangThai(Boolean trangThai, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return chucVuRepo.findByTrangThai(trangThai, pageable);
    }

    public Page<ChucVu> timKiemTheoMaHoacTenChucVu(String tuKhoa, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return chucVuRepo.findByMaChucVuOrTenChucVu(tuKhoa, tuKhoa, pageable);
    }

    public boolean checkTrungMaChucVu(String maChucVu){
        return chucVuRepo.existsByMaChucVu(maChucVu);
    }
}
