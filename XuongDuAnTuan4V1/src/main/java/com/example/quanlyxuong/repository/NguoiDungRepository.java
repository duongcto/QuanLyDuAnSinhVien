package com.example.quanlyxuong.repository;

import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.VaiTro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
boolean existsByMaNguoiDung(String maNguoiDung);
NguoiDung findByTenDangNhapAndEmailFeOrEmailPt(String tenDangNhap, String emailFe, String emailPt);
    Page<NguoiDung> findAll(Pageable pageable);
    Optional<NguoiDung> findByEmailFeOrEmailPt(String emailFe, String emailPt);
    @Query("SELECT nd FROM NguoiDung nd WHERE nd.idVaiTro.tenVaiTro LIKE %:Keyword1% OR nd.idVaiTro.tenVaiTro LIKE %:Keyword2%")
    Page<NguoiDung> danhSachNhanVien(@Param("Keyword1") String keyword1, @Param("Keyword2") String keyword2, Pageable pageable);
    @Query("SELECT n FROM NguoiDung n WHERE n.idVaiTro = :tenVaiTro")
    Page<NguoiDung> findByIdVaiTro(@Param("tenVaiTro") VaiTro tenVaiTro, Pageable pageable);
    @Query("SELECT n FROM NguoiDung n WHERE (n.hoTen LIKE %:hoTen% OR n.emailFe LIKE %:emailFe% OR n.emailPt LIKE %:emailPt% OR n.soDienThoai LIKE %:soDienThoai%) AND (n.idVaiTro.tenVaiTro = 'Quản Lý' OR n.idVaiTro.tenVaiTro = 'Thành viên')")
    Page<NguoiDung> findByHoTenOrEmailFeOrEmailPtOrSoDienThoai(@Param("hoTen") String hoTen, @Param("emailFe") String emailFe, @Param("emailPt") String emailPt, @Param("soDienThoai") String soDienThoai, Pageable pageable);







    NguoiDung findNguoiDungByEmailFe(@Size(max = 100) @NotBlank(message = "Email FE không được bỏ trống!") String emailFe);

    Optional<NguoiDung> findByEmailPt(String emailPt);



    @Query("SELECT n FROM NguoiDung n WHERE n.idVaiTro.tenVaiTro LIKE :keyword")
    Page<NguoiDung> findAll_page(Pageable pageable, @Param("keyword") String keyword);

    @Query("SELECT n FROM NguoiDung n WHERE n.idVaiTro.tenVaiTro LIKE :keyword")
    List<NguoiDung> findAll_nd(@Param("keyword") String keyword);

    @Query("SELECT  n FROM NguoiDung n WHERE n.id=?1")
    NguoiDung findById(int id);


    @Query("SELECT n FROM NguoiDung n WHERE LOWER(n.hoTen) LIKE LOWER(CONCAT('%', :hoTen, '%')) AND (n.idVaiTro.tenVaiTro = 'Quản Lý' OR n.idVaiTro.tenVaiTro = 'Thành viên')")
    Page<NguoiDung> findByHoTenContainingIgnoreCase(@Param("hoTen") String hoTen, Pageable pageable);

    @Query("SELECT n FROM NguoiDung n WHERE n.trangThai = :trangThai AND (n.idVaiTro.tenVaiTro = 'Quản Lý' OR n.idVaiTro.tenVaiTro = 'Thành viên')")
    Page<NguoiDung> findByTrangThai(@Param("trangThai") String trangThai, Pageable pageable);

    @Query("SELECT n FROM NguoiDung n WHERE LOWER(n.hoTen) LIKE LOWER(CONCAT('%', :hoTen, '%')) AND n.trangThai = :trangThai AND (n.idVaiTro.tenVaiTro = 'Quản Lý' OR n.idVaiTro.tenVaiTro = 'Thành viên')")
    Page<NguoiDung> findByHoTenContainingIgnoreCaseAndTrangThai(@Param("hoTen") String hoTen,
                                                                @Param("trangThai") String trangThai,
                                                                Pageable pageable);

}