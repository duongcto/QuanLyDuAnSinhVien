package com.example.quanlyxuong.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "cong_viec", schema = "dbo")
public class CongViec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cong_viec", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_danh_sach_cong_viec")
    private DanhSachCongViec idDanhSachCongViec;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ten_cong_viec")
    private String tenCongViec;

    @Column(name = "do_uu_tien")
    private String doUuTien;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @Column(name = "thoi_gian")
    private Double thoiGian;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "ngay_update")
    private LocalDate ngayUpdate;

    @Column(name = "status")
    private String status; // TODO, IN_PROGRESS, DONE

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "ngay_het_han")
    private LocalDate ngayHetHan;

    @Column(name = "thanh_vien")
    private String thanhVien; // comma-separated user ids or names

    @Transient
    private java.util.List<Integer> thanhVienIds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_column")
    private KanbanColumn idColumn;

    @Column(name = "position")
    private Integer position;

    @Size(max = 255)
    @Nationalized
    @Column(name = "nhan_dan")
    private String nhanDan;

    @Nationalized
    @Lob
    @Column(name = "tai_anh")
    private String taiAnh;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private Instant ngayTao;

    @Nationalized
    @Lob
    @Column(name = "link_dinh_kem")
    private String linkDinhKem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_phan_cong")
    private PhanCong idPhanCong;

    @Size(max = 255)
    @Nationalized
    @Column(name = "kieu_cv")
    private String kieuCv;

}