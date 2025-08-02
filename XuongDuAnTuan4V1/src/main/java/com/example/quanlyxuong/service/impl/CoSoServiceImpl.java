package com.example.quanlyxuong.service.impl;

import com.example.quanlyxuong.entity.CoSo;
import com.example.quanlyxuong.repository.CoSoRepository;
import com.example.quanlyxuong.service.CoSoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoSoServiceImpl implements CoSoService {
    @Autowired
    private CoSoRepository coSoRepository;

    @Override
    public boolean isMaCoSoDaTonTai(String maCoSo) {
        return coSoRepository.existsByMaCoSo(maCoSo);
    }

    @Override
    public List<CoSo> findAllCoSo() {
        return coSoRepository.findAll();
    }

    @Override
    public List<CoSo> getAll() {
        return coSoRepository.findAll();
    }

//    @Override
//    public Page<CoSo> getAllCoSo(int pageNo, int pageSize) {
//        Pageable pageable = PageRequest.of(pageNo, pageSize);
//        return coSoRepository.findAll(pageable);
//    }
//    @Override
//    public Page<CoSo> search(String tenCoSo, Boolean trangThai, int pageNo, int pageSize) {
//        Pageable pageable = PageRequest.of(pageNo, pageSize);
//        if (tenCoSo != null && trangThai != null) {
//            return coSoRepository.findByTenCoSoContainingAndTrangThai(tenCoSo, trangThai, pageable);
//        } else if (tenCoSo != null) {
//            return coSoRepository.findByTenCoSoContaining(tenCoSo, pageable);
//        } else if (trangThai != null) {
//            return coSoRepository.findByTrangThai(trangThai, pageable);
//        } else {
//            return coSoRepository.findAll(pageable);
//        }
//    }

}
