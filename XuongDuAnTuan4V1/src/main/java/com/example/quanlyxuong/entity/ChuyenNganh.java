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
@Table(name = "chuyen_nganh", schema = "dbo")
public class ChuyenNganh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chuyen_nganh", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "ma_chuyen_nganh", length = 50)
    private String maChuyenNganh;

    @Size(max = 100)
    @Nationalized
    @Column(name = "ten_chuyen_nganh", length = 100)
    private String tenChuyenNganh;

    @Size(max = 255)
    @Nationalized
    @Column(name = "mo_ta")
    private String moTa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bo_mon")
    private BoMon idBoMon;

    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @Column(name = "ngay_update")
    private LocalDate ngayUpdate;

    @Column(name = "trang_thai")
    private Boolean trangThai;

}