package com.example.quanlyxuong.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "giai_doan")
public class GiaiDoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_giai_doan", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_du_an", nullable = false)
    private DuAn idDuAn;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ten_giai_doan", nullable = false)
    private String tenGiaiDoan;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_update")
    private Instant ngayUpdate;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    // Trong class GiaiDoan
    @OneToMany(mappedBy = "idGiaiDoan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DanhSachCongViec> danhSachCongViec;

}