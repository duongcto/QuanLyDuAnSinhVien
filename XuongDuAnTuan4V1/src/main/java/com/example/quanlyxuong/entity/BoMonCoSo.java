package com.example.quanlyxuong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bo_mon_co_so", schema = "dbo")
public class BoMonCoSo {
    @EmbeddedId
    private BoMonCoSoId id;

    @MapsId("idBoMon")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_bo_mon", nullable = false)
    private BoMon idBoMon;

    @MapsId("idCoSo")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_co_so", nullable = false)
    private CoSo idCoSo;

}