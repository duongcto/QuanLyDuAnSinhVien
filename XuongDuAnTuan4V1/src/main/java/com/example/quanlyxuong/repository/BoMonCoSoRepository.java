package com.example.quanlyxuong.repository;

import com.example.quanlyxuong.entity.BoMonCoSo;
import com.example.quanlyxuong.entity.BoMonCoSoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoMonCoSoRepository extends JpaRepository<BoMonCoSo, BoMonCoSoId> {
}