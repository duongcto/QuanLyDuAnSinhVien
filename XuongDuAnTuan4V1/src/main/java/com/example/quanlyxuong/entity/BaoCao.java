package com.example.quanlyxuong.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
@Table(name = "bao_cao", schema = "dbo")
public class BaoCao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bao_cao", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung idNguoiDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_du_an")
    private DuAn idDuAn;

    @Column(name = "ngay_bao_cao")
    private LocalDate ngayBaoCao;

    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @Column(name = "ngay_update")
    private LocalDate ngayUpdate;

    @Size(max = 255)
    @Nationalized
    @Column(name = "hom_nay_lam_gi")
    private String homNayLamGi;

    @Size(max = 255)
    @Nationalized
    @Column(name = "gap_kho_khan_gi")
    private String gapKhoKhanGi;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ngay_mai_lam_gi")
    private String ngayMaiLamGi;

    @Column(name = "thoi_gian")
    private Double thoiGian;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    public String getNgayBaoCaoFormatted() {
        if (ngayBaoCao != null) {
            return ngayBaoCao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return "";
    }

}