package com.example.quanlyxuong.repository;

import com.example.quanlyxuong.entity.BaoCao;
import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.ThanhVienDuAn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BaoCaoRepository extends JpaRepository<BaoCao, Integer> {
    List<BaoCao> findByIdDuAn_IdAndIdNguoiDung_IdAndNgayBaoCao(
            Integer duAnId, Integer ndId, LocalDate ngayBaoCao);
    List<BaoCao> findByIdDuAn_IdAndNgayBaoCao(Integer duAnId, LocalDate ngayBaoCao);

    List<BaoCao> findByNgayBaoCao(LocalDate ngayBaoCao);

    List<String> findBaoCaoByNgayBaoCao(LocalDate ngayBaoCao);

    LocalDate id(Integer id);

    List<String> findDistinctByNgayBaoCao(LocalDate ngayBaoCao);

    // Thêm các method cho báo cáo thành viên
    Page<BaoCao> findByIdNguoiDung_IdAndIdDuAn_Id(Integer nguoiDungId, Integer duAnId, Pageable pageable);
    
    Optional<BaoCao> findByIdNguoiDung_IdAndIdDuAn_IdAndNgayBaoCao(Integer nguoiDungId, Integer duAnId, LocalDate ngayBaoCao);
    
    boolean existsByIdNguoiDung_IdAndIdDuAn_IdAndNgayBaoCao(Integer nguoiDungId, Integer duAnId, LocalDate ngayBaoCao);

    @Query(value = "SELECT * FROM bao_cao WHERE MONTH(ngay_bao_cao) = :month AND YEAR(ngay_bao_cao) = :year", nativeQuery = true)
    List<BaoCao> findByThangVaNam(@Param("month") int month, @Param("year") int year);


}