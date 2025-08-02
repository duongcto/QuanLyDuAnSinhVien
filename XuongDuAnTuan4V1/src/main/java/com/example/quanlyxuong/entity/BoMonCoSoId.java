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
public class BoMonCoSoId implements Serializable {
    private static final long serialVersionUID = -6624101840555906345L;
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
        BoMonCoSoId entity = (BoMonCoSoId) o;
        return Objects.equals(this.idBoMon, entity.idBoMon) &&
                Objects.equals(this.idCoSo, entity.idCoSo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBoMon, idCoSo);
    }

}