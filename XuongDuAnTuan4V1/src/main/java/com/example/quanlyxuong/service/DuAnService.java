package com.example.quanlyxuong.service;

import com.example.quanlyxuong.dto.DuAnDto;
import com.example.quanlyxuong.entity.DuAn;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DuAnService {

    List<DuAnDto> getListDuAn();

    DuAn insertDuAn(DuAn duAn);

    Page<DuAnDto> getListDuAnPage(Pageable pageable);

    void deleteDuAn(DuAn duAn);

    DuAn findById(Integer id);

    DuAn updateDuAn( DuAn duAn);

    void exportDuAnToExcel(HttpServletResponse response) throws IOException;

    void importDuAnFromExcel(MultipartFile file) throws IOException;

    Page<DuAnDto> findByTenDuAnAndTrangThai(String tenDuAn, Boolean trangThai, Pageable pageable);
}
