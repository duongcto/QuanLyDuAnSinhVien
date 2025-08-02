package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.BoMon;
import com.example.quanlyxuong.entity.ChuyenNganh;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChuyenNganhService {
    List<ChuyenNganh> findAllChuyenNganh();

    List<ChuyenNganh> getAll();
    ChuyenNganh getById(Integer id);
    ChuyenNganh save(ChuyenNganh chuyenNganh); // Thay thế create/update
    void delete(Integer id);

    Page<ChuyenNganh> getChuyenNganhPage(int page, int size);

    ChuyenNganh updateChuyenNganh(ChuyenNganh chuyenNganh, Integer id);

}
