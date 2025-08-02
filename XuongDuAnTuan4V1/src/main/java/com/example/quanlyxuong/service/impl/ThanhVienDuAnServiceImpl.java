package com.example.quanlyxuong.service.impl;

import com.example.quanlyxuong.entity.DuAn;
import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.ThanhVienDuAn;
import com.example.quanlyxuong.repository.ThanhVienDuAnRepository;
import com.example.quanlyxuong.service.ThanhVienDuAnService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThanhVienDuAnServiceImpl implements ThanhVienDuAnService {

    private final ThanhVienDuAnRepository thanhVienDuAnRepository;

    public ThanhVienDuAnServiceImpl( ThanhVienDuAnRepository thanhVienDuAnRepository) {
        this.thanhVienDuAnRepository = thanhVienDuAnRepository;
    }

    @Override
    public List<ThanhVienDuAn> findAllThanhVienDuAn() {
        return thanhVienDuAnRepository.findAll();
    }

    @Override
    public ThanhVienDuAn findById(Integer idTv) {
        return thanhVienDuAnRepository.findById(idTv).orElse(null);
    }

    @Override
    public void save(ThanhVienDuAn tv) {
        thanhVienDuAnRepository.save(tv);
    }

    @Override
    public List<ThanhVienDuAn> findAllThanhVienDuAnChuaCoDuAn() {

        return thanhVienDuAnRepository.findByIdDuAnIsNull();
    }

    @Override
    public void deleteByIdDuAn(DuAn duAn) {
        thanhVienDuAnRepository.deleteById(duAn.getId());
    }

    @Override
    public void deleteDuAn(DuAn duAn) {
        thanhVienDuAnRepository.deleteById(duAn.getId());
    }

    @Override
    public ThanhVienDuAn findIdDuAn(NguoiDung nguoiDung) {
        return thanhVienDuAnRepository.findThanhVienDuAnByIdNguoiDung(nguoiDung);
    }


}
