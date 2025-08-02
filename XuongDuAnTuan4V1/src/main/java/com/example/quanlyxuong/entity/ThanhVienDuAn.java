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
@Table(name = "thanh_vien_du_an", schema = "dbo")
public class ThanhVienDuAn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_thanh_vien", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_du_an")
    private DuAn idDuAn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung idNguoiDung;

    @Size(max = 100)
    @Nationalized
    @Column(name = "vai_tro", length = 100)
    private String vaiTro;

    @Column(name = "ngay_tham_gia")
    private LocalDate ngayThamGia;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vai_tro")
    private VaiTro idVaiTro;

    @OneToMany(
            mappedBy = "idThanhVien", // "thanhVien" là tên của trường trong entity BinhChonCV tham chiếu ngược lại đây
            cascade = CascadeType.ALL, // Xóa các BinhChonCV liên quan khi ThanhVien này bị xóa
            orphanRemoval = true
    )
    private List<BinhchonCv> danhSachBinhChon;

}