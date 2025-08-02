package com.example.quanlyxuong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chuyen_nganh_bo_mon_co_so", schema = "dbo")
public class ChuyenNganhBoMonCoSo {
    @EmbeddedId
    private ChuyenNganhBoMonCoSoId id;

    @MapsId("idChuyenNganh")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_chuyen_nganh", nullable = false)
    private ChuyenNganh idChuyenNganh;

    @MapsId("idBoMon")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_bo_mon", nullable = false)
    private BoMon idBoMon;

    @MapsId("idCoSo")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_co_so", nullable = false)
    private CoSo idCoSo;

}