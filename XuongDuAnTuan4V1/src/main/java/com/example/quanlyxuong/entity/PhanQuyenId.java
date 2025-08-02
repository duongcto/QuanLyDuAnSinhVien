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
public class PhanQuyenId implements Serializable {
    private static final long serialVersionUID = -8078421744397053395L;
    @NotNull
    @Column(name = "id_nguoi_dung", nullable = false)
    private Integer idNguoiDung;

    @NotNull
    @Column(name = "id_co_so", nullable = false)
    private Integer idCoSo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PhanQuyenId entity = (PhanQuyenId) o;
        return Objects.equals(this.idCoSo, entity.idCoSo) &&
                Objects.equals(this.idNguoiDung, entity.idNguoiDung);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCoSo, idNguoiDung);
    }

}