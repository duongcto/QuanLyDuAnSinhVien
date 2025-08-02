package com.example.quanlyxuong.repository;

import com.example.quanlyxuong.entity.DanhSachCongViec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DanhSachCongViecRepository extends JpaRepository<DanhSachCongViec, Integer> {
    Optional<DanhSachCongViec> findByTenDanhSachCongViec(String tenDanhSachCongViec);
    @Query("SELECT g FROM DanhSachCongViec g WHERE g.duAn.id = :duAnId") // Notice the ".id"
    List<DanhSachCongViec> findByIdDuAn(@Param("duAnId") Integer duAnId);

    Optional<DanhSachCongViec> findFirstByIdGiaiDoan_Id(Integer id);
}