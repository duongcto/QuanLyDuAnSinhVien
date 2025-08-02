package com.example.quanlyxuong.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TheLoaiDto {

    private Integer id;

    @NotBlank(message = "Không được để trống")
    private String tenLoaiDuAn;

    @NotBlank(message = "Không được để trống")
    private String moTa;

}
