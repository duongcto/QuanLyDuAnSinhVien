package com.example.quanlyxuong.service.impl;

import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.repository.NguoiDungRepository;
import com.example.quanlyxuong.repository.ThanhVienDuAnRepository;
import com.example.quanlyxuong.service.NguoiDungService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NguoiDungServiceImpl implements NguoiDungService {

    private final NguoiDungRepository nguoiDungRepository;
    private final ThanhVienDuAnRepository thanhVienDuAnRepository;


    public NguoiDungServiceImpl(NguoiDungRepository nguoiDungRepository, ThanhVienDuAnRepository thanhVienDuAnRepository) {
        this.nguoiDungRepository = nguoiDungRepository;
        this.thanhVienDuAnRepository = thanhVienDuAnRepository;
    }

    @Override
    public List<NguoiDung> findAllNguoiDung() {
        return nguoiDungRepository.findAll();
    }

    @Override
    public NguoiDung findById(Integer id) {
        return nguoiDungRepository.findById(id).orElse(null);
    }


    @Override
    public List<NguoiDung> nguoiDungChuaThamGiaDuAn() {
        List<Integer> nguoiDungDaThamGia = thanhVienDuAnRepository.findAll()
                .stream()
                .map(tv -> tv.getIdNguoiDung().getId())
                .toList();

        if (nguoiDungDaThamGia.isEmpty()) {
            return nguoiDungRepository.findAll();
        } else {
            return nguoiDungRepository.findAll()
                    .stream()
                    .filter(nd -> !nguoiDungDaThamGia.contains(nd.getId()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public NguoiDung findIdNguoiDung(String email) {
        return nguoiDungRepository.findNguoiDungByEmailFe(email);
    }

    @Override
    public List<NguoiDung> findAllNguoiDungDangHoc() {
        return nguoiDungRepository.findAll().stream()
                .filter(nd -> "Đang học".equals(nd.getTrangThai()))
                .collect(Collectors.toList());
    }

}

