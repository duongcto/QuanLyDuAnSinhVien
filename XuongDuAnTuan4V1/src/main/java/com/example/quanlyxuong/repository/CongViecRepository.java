package com.example.quanlyxuong.repository;

import com.example.quanlyxuong.entity.CongViec;
import com.example.quanlyxuong.entity.KanbanColumn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CongViecRepository extends JpaRepository<CongViec, Integer> {
    List<CongViec> findByIdColumnOrderByPositionAsc(KanbanColumn idColumn);

    Page<CongViec> findByTenCongViecContainingIgnoreCase(String keyword, PageRequest pageable);

    List<CongViec> findByIdDanhSachCongViec_Id(Integer id);


    // CongViecRepository.java
    @Query("SELECT c FROM CongViec c WHERE c.idDanhSachCongViec.idGiaiDoan.id = :idGiaiDoan")
    List<CongViec> findByGiaiDoanId(@Param("idGiaiDoan") Integer idGiaiDoan);

    @Query("SELECT c FROM CongViec c WHERE c.idDanhSachCongViec IS NULL")
    List<CongViec> findCongViecChuaPhanCong();
}