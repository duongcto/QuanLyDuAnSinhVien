package com.example.quanlyxuong.repository;

import com.example.quanlyxuong.entity.GiaiDoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GiaiDoanRepository extends JpaRepository<GiaiDoan, Integer> {
  @Query("SELECT g FROM GiaiDoan g WHERE g.idDuAn.id = :duAnId") // Notice the ".id"
  List<GiaiDoan> findByDuAn_Id(@Param("duAnId") Integer duAnId);

  @Query("SELECT gd FROM GiaiDoan gd WHERE " +
          "(:idDuAn IS NULL OR gd.idDuAn.id = :idDuAn) AND " +
          "(:tenGiaiDoan IS NULL OR gd.tenGiaiDoan LIKE %:tenGiaiDoan%) AND " +
          "(:ngayBatDau IS NULL OR gd.ngayBatDau = :ngayBatDau)")
  List<GiaiDoan> searchGiaiDoan(@Param("idDuAn") Integer idDuAn,
                                @Param("tenGiaiDoan") String tenGiaiDoan,
                                @Param("ngayBatDau") LocalDate ngayBatDau);
}