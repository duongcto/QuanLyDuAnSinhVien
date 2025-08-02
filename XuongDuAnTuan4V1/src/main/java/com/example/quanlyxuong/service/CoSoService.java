package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.CoSo;

import java.util.List;

public interface CoSoService {

    public boolean isMaCoSoDaTonTai(String maCoSo);

//    Page<CoSo> getAllCoSo(int pageNo, int pageSize); // thêm dòng này
//
//    Page<CoSo> search(String tenCoSo, Boolean trangThai, int pageNo, int pageSize);

    // DuAn
    List<CoSo> findAllCoSo();
    
    List<CoSo> getAll();
}
