package com.example.quanlyxuong.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "bo_mon", schema = "dbo")
public class BoMon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bo_mon", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "ma_bo_mon", length = 50)
    private String maBoMon;

    @Size(max = 100)
    @Nationalized
    @Column(name = "ten_bo_mon", length = 100)
    private String tenBoMon;

    @Size(max = 100)
    @Nationalized
    @Column(name = "trung_bo_mon", length = 100)
    private String trungBoMon;

    @Column(name = "so_thanh_vien")
    private Integer soThanhVien;

    @Size(max = 255)
    @Nationalized
    @Column(name = "mo_ta_chuc_nang")
    private String moTaChucNang;

    @Column(name = "ngay_thanh_lap")
    private LocalDate ngayThanhLap;

    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @Column(name = "ngay_update")
    private LocalDate ngayUpdate;

    @Column(name = "trang_thai")
    private Boolean trangThai;

}