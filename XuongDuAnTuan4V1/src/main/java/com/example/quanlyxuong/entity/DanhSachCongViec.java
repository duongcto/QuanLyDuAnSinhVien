package com.example.quanlyxuong.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "danh_sach_cong_viec", schema = "dbo")
public class DanhSachCongViec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_danh_sach_cong_viec", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_du_an")
    private DuAn duAn;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ten_danh_sach_cong_viec")
    private String tenDanhSachCongViec;

    @Size(max = 255)
    @Nationalized
    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @Column(name = "ngay_update")
    private LocalDate ngayUpdate;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_giai_doan")
    private GiaiDoan idGiaiDoan;

    @OneToMany(mappedBy = "idDanhSachCongViec", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhanCong> phanCongs;

}