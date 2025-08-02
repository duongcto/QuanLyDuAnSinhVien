package com.example.quanlyxuong.repository;

import com.example.quanlyxuong.entity.CoSo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoSoRepository extends JpaRepository<CoSo, Integer> {
    boolean existsByMaCoSo(String maCoSo);

    @Query("""
    SELECT c FROM CoSo c
    WHERE (:tenCoSo IS NULL OR c.tenCoSo LIKE %:tenCoSo%)
      AND (:trangThai IS NULL OR c.trangThai = :trangThai)
           """)
    List<CoSo> search(@Param("tenCoSo") String tenCoSo, @Param("trangThai") boolean trangThai);


    // Lọc theo cả tên và trạng thái
    List<CoSo> findByTenCoSoContainingIgnoreCaseAndTrangThai(String tenCoSo, Boolean trangThai);

    // Lọc theo tên
    List<CoSo> findByTenCoSoContainingIgnoreCase(String tenCoSo);

    // Toàn bộ
    List<CoSo> findAll();
}