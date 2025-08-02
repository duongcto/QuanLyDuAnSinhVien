package com.example.quanlyxuong.service.impl;

import com.example.quanlyxuong.entity.BoMon;
import com.example.quanlyxuong.entity.ChuyenNganh;
import com.example.quanlyxuong.exception.ResourceNotFoundException;
import com.example.quanlyxuong.repository.ChuyenNganhRepository;
import com.example.quanlyxuong.service.ChuyenNganhService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class ChuyenNganhServiceImpl implements ChuyenNganhService {

    private final ChuyenNganhRepository chuyenNganhRepository;

    public ChuyenNganhServiceImpl(ChuyenNganhRepository chuyenNganhRepository) {
        this.chuyenNganhRepository = chuyenNganhRepository;
    }

    @Override
    public List<ChuyenNganh> findAllChuyenNganh() {
        return chuyenNganhRepository.findAll();
    }

    @Override
    public List<ChuyenNganh> getAll() {
        return chuyenNganhRepository.findAll();
    }

    @Override
    public ChuyenNganh getById(Integer id) {
        return chuyenNganhRepository.findById(id).orElse(null);
    }

    @Override
    public ChuyenNganh save(ChuyenNganh chuyenNganh) {
        if (chuyenNganh.getId() == null) {
            chuyenNganh.setNgayTao(LocalDate.now());
        }
        chuyenNganh.setNgayUpdate(LocalDate.now());
        return chuyenNganhRepository.save(chuyenNganh);
    }

    @Override
    public void delete(Integer id) {
        chuyenNganhRepository.deleteById(id);
    }

    @Override
    public Page<ChuyenNganh> getChuyenNganhPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return chuyenNganhRepository.findAll(pageable);
    }

    @Override
    public ChuyenNganh updateChuyenNganh(ChuyenNganh chuyenNganh, Integer id) {

        ChuyenNganh exitstingChuyenNganh = chuyenNganhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bộ môn không tồn tại với id = " + id));

        // Nếu mã bộ môn mới khác mã hiện tại thì kiểm tra trùng
        if (!exitstingChuyenNganh.getMaChuyenNganh().equals(chuyenNganh.getMaChuyenNganh()) &&
                chuyenNganhRepository.existsByMaChuyenNganh(chuyenNganh.getMaChuyenNganh())) {
            throw new IllegalArgumentException("Mã bộ môn đã tồn tại!");
        }

        exitstingChuyenNganh.setMaChuyenNganh(chuyenNganh.getMaChuyenNganh());
        exitstingChuyenNganh.setTenChuyenNganh(chuyenNganh.getTenChuyenNganh());
        exitstingChuyenNganh.setIdBoMon(chuyenNganh.getIdBoMon());
        exitstingChuyenNganh.setTrangThai(chuyenNganh.getTrangThai());

        return chuyenNganhRepository.save(exitstingChuyenNganh);
    }
}
