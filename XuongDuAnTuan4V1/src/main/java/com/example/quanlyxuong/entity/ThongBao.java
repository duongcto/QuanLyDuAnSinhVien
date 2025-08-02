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
@Table(name = "thong_bao", schema = "dbo")
public class ThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_thong_bao", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "tieu_de")
    private String tieuDe;

    @Size(max = 100)
    @Nationalized
    @Column(name = "noi_dung", length = 100)
    private String noiDung;

    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung idNguoiDung;

}