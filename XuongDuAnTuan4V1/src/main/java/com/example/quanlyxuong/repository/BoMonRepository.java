package com.example.quanlyxuong.repository;

import com.example.quanlyxuong.entity.BoMon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoMonRepository extends JpaRepository<BoMon, Integer> {
    boolean existsByMaBoMon(String maBoMon);

    boolean existsByMaBoMonAndIdNot(String maBoMon, Integer id);


    @Query(value = "SELECT * FROM bo_mon WHERE " +
            "ma_bo_mon LIKE %:keyword% OR " +
            "ten_bo_mon LIKE %:keyword% OR " +
            "trung_bo_mon LIKE %:keyword% OR " +
            "mo_ta_chuc_nang LIKE %:keyword% OR " +
            "CAST(trang_thai AS VARCHAR) = :keyword OR " +
            "CAST(ngay_thanh_lap AS VARCHAR) LIKE %:keyword% OR " +
            "CAST(ma_bo_mon AS VARCHAR) LIKE %:keyword%", nativeQuery = true)
    List<BoMon> searchAllFields(@Param("keyword") String keyword);

    Optional<BoMon> findByMaBoMon(String maBoMon);
}