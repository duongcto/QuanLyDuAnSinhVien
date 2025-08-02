package com.example.quanlyxuong.repository;

import com.example.quanlyxuong.entity.VaiTro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VaiTroRepository extends JpaRepository<VaiTro, Integer> {
    // Phương thức custom để tìm VaiTro theo tên
    Optional<VaiTro> findByTenVaiTro(String tenVaiTro);
    @Query("""
SELECT n FROM VaiTro n WHERE n.tenVaiTro=?1
""")
    VaiTro findByTenVaiTro1(String tenVaiTro);
}