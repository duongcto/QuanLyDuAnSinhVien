package com.example.quanlyxuong.repository;

import com.example.quanlyxuong.entity.ChuyenNganhBoMonCoSo;
import com.example.quanlyxuong.entity.ChuyenNganhBoMonCoSoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChuyenNganhBoMonCoSoRepository extends JpaRepository<ChuyenNganhBoMonCoSo, ChuyenNganhBoMonCoSoId> {
}