package com.example.quanlyxuong.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ChuyenNganhBoMonCoSoId implements Serializable {
    private static final long serialVersionUID = -6358054149531192849L;
    @NotNull
    @Column(name = "id_chuyen_nganh", nullable = false)
    private Integer idChuyenNganh;

    @NotNull
    @Column(name = "id_bo_mon", nullable = false)
    private Integer idBoMon;

    @NotNull
    @Column(name = "id_co_so", nullable = false)
    private Integer idCoSo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChuyenNganhBoMonCoSoId entity = (ChuyenNganhBoMonCoSoId) o;
        return Objects.equals(this.idChuyenNganh, entity.idChuyenNganh) &&
                Objects.equals(this.idBoMon, entity.idBoMon) &&
                Objects.equals(this.idCoSo, entity.idCoSo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idChuyenNganh, idBoMon, idCoSo);
    }

}