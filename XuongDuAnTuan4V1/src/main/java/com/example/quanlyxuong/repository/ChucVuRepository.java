package com.example.quanlyxuong.repository;

import com.example.quanlyxuong.entity.ChucVu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChucVuRepository extends JpaRepository<ChucVu, Integer> {
    boolean existsByMaChucVu(String maChucVu);
    Page<ChucVu> findAll(Pageable pageable);
    Page<ChucVu> findByTrangThai(Boolean trangThai, Pageable pageable);
    Page<ChucVu> findByMaChucVuOrTenChucVu(String maChucVu, String tenChucVu, Pageable pageable);
    @Query("SELECT n FROM ChucVu n WHERE n.maChucVu='CV003'")
    ChucVu getChucVuByIdThanhVien();
}
