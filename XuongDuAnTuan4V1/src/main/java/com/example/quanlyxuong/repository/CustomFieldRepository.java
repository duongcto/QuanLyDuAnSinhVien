package com.example.quanlyxuong.repository;

import com.example.quanlyxuong.entity.CustomField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomFieldRepository extends JpaRepository<CustomField, Integer> {
} 