package com.example.quanlyxuong.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "kanban_column")
public class KanbanColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_column", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "column_name", nullable = false, length = 100)
    private String columnName;

    @NotNull
    @Column(name = "column_order", nullable = false)
    private Integer columnOrder;

    @Size(max = 255)
    @Nationalized
    @Column(name = "mo_ta")
    private String moTa;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private Instant ngayTao;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_update")
    private Instant ngayUpdate;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Boolean trangThai;

    @OneToMany(mappedBy = "idColumn")
    private Set<CongViec> congViecs = new LinkedHashSet<>();
} 