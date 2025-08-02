package com.example.quanlyxuong.repository;

import com.example.quanlyxuong.dto.DuAnDto;
import com.example.quanlyxuong.entity.DuAn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DuAnRepository extends JpaRepository<DuAn, Integer> {

  @Query(value = """
        SELECT 
            d.id_du_an AS id,
            d.ma_du_an AS maDuAn,
            d.ten_du_an AS tenDuAn,
            d.ngay_bat_dau AS ngayBatDau,
            d.ngay_ket_thuc_du_kien AS ngayKetThuc,
            cn.ten_chuyen_nganh AS chuyenNganh,
            CONCAT(
                CASE 
                    WHEN COUNT(cv.id_cong_viec) = 0 THEN 0
                    ELSE CAST(SUM(CASE WHEN cv.trang_thai = 1 THEN 1 ELSE 0 END) * 100.0 / COUNT(cv.id_cong_viec) AS DECIMAL(5,2))
                END, '%'
            ) AS tienDo,
            d.trang_thai AS trangThai
        FROM du_an d
        JOIN chuyen_nganh cn ON cn.id_chuyen_nganh = d.id_chuyen_nganh
        LEFT JOIN danh_sach_cong_viec dscv ON dscv.id_du_an = d.id_du_an
        LEFT JOIN cong_viec cv ON cv.id_danh_sach_cong_viec = dscv.id_danh_sach_cong_viec
        WHERE (:tenDuAn IS NULL OR d.ten_du_an LIKE %:tenDuAn%)
        AND (:trangThai IS NULL OR d.trang_thai = :trangThai)
        GROUP BY d.id_du_an, d.ma_du_an, d.ten_du_an, d.ngay_bat_dau, d.ngay_ket_thuc_du_kien, cn.ten_chuyen_nganh, d.trang_thai
        """,
          countQuery = """
        SELECT COUNT(*) 
        FROM du_an d
        WHERE (:tenDuAn IS NULL OR d.ten_du_an LIKE %:tenDuAn%)
        AND (:trangThai IS NULL OR d.trang_thai = :trangThai)
        """,
          nativeQuery = true)
  Page<DuAnDto> findByTenDuAnAndTrangThai(String tenDuAn, Boolean trangThai, Pageable pageable);

  @Query(value = """
        SELECT 
            d.id_du_an AS id,
            d.ma_du_an AS maDuAn,
            d.ten_du_an AS tenDuAn,
            d.ngay_bat_dau AS ngayBatDau,
            d.ngay_ket_thuc_du_kien AS ngayKetThuc,
            cn.ten_chuyen_nganh AS chuyenNganh,
            CONCAT(
                CASE 
                    WHEN COUNT(cv.id_cong_viec) = 0 THEN 0
                    ELSE CAST(SUM(CASE WHEN cv.trang_thai = 1 THEN 1 ELSE 0 END) * 100.0 / COUNT(cv.id_cong_viec) AS DECIMAL(5,2))
                END, '%'
            ) AS tienDo,
            d.trang_thai AS trangThai
        FROM du_an d
        JOIN chuyen_nganh cn ON cn.id_chuyen_nganh = d.id_chuyen_nganh
        LEFT JOIN danh_sach_cong_viec dscv ON dscv.id_du_an = d.id_du_an
        LEFT JOIN cong_viec cv ON cv.id_danh_sach_cong_viec = dscv.id_danh_sach_cong_viec
        GROUP BY d.id_du_an, d.ma_du_an, d.ten_du_an, d.ngay_bat_dau, d.ngay_ket_thuc_du_kien, cn.ten_chuyen_nganh, d.trang_thai
        """, nativeQuery = true)
  List<DuAnDto> getListDuAn();

  @Query(value = """
    SELECT 
        d.id_du_an AS id,
        d.ma_du_an AS maDuAn,
        d.ten_du_an AS tenDuAn,
        d.ngay_bat_dau AS ngayBatDau,
        d.ngay_ket_thuc_du_kien AS ngayKetThuc,
        cn.ten_chuyen_nganh AS chuyenNganh,
        CONCAT(
            CASE 
                WHEN COUNT(cv.id_cong_viec) = 0 THEN 0
                ELSE CAST(SUM(CASE WHEN cv.trang_thai = 1 THEN 1 ELSE 0 END) * 100.0 / COUNT(cv.id_cong_viec) AS DECIMAL(5,2))
            END, '%'
        ) AS tienDo,
        d.trang_thai AS trangThai
    FROM du_an d
    JOIN chuyen_nganh cn ON cn.id_chuyen_nganh = d.id_chuyen_nganh
    LEFT JOIN danh_sach_cong_viec dscv ON dscv.id_du_an = d.id_du_an
    LEFT JOIN cong_viec cv ON cv.id_danh_sach_cong_viec = dscv.id_danh_sach_cong_viec
    GROUP BY d.id_du_an, d.ma_du_an, d.ten_du_an, d.ngay_bat_dau, d.ngay_ket_thuc_du_kien, cn.ten_chuyen_nganh, d.trang_thai
""",
          countQuery = """
    SELECT COUNT(*) FROM du_an
""", nativeQuery = true)
  Page<DuAnDto> getListDuAnPage(Pageable pageable);
  Optional<DuAn> findById(Integer id);

}