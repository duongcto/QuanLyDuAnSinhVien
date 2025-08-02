package com.example.quanlyxuong.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "du_an", schema = "dbo")
public class DuAn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_du_an", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "ma_du_an", length = 50)
    private String maDuAn;

    @NotBlank(message = "Tên dự án không được để trống")
    @Size(max = 100)
    @Nationalized
    @Column(name = "ten_du_an", length = 100)
    private String tenDuAn;

    @Column(name = "ngay_update")
    private LocalDate ngayUpdate;

    @NotNull(message = "Chưa chọn bộ môn!")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bo_mon")
    private BoMon idBoMon;

    @NotNull(message = "Chưa chọn cơ sở!")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_co_so")
    private CoSo idCoSo;

    @NotNull(message = "Chưa chọn chuyên ngành!")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chuyen_nganh")
    private ChuyenNganh idChuyenNganh;

    @NotNull(message = "Chưa chọn loại dự án!")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_loai_du_an")
    private LoaiDuAn idLoaiDuAn;

    @FutureOrPresent(message = "Ngày bắt đầu phải từ hiện tại trở đi")
    @NotNull(message = "Chưa nhập ngày bắt đầu!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @FutureOrPresent(message = "Ngày kết thúc phải từ hiện tại trở đi")
    @NotNull(message = "Chưa nhập ngày kết thúc!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "ngay_ket_thuc_du_kien")
    private LocalDate ngayKetThucDuKien;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Transient
    private List<Integer> thanhVienIds = new ArrayList<>();

    @OneToMany(mappedBy = "idDuAn", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ThanhVienDuAn> thanhVienDuAns;

}