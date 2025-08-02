package com.example.quanlyxuong.repository;

import com.example.quanlyxuong.entity.KanbanColumn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KanbanRepository extends JpaRepository<KanbanColumn, Integer> {
} 