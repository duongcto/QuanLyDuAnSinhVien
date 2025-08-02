package com.example.quanlyxuong.dto;

public class MemberDto {
    private Integer id;
    private String hoTen;
    private String roleName;

    public MemberDto() {}
    public MemberDto(Integer id, String hoTen, String roleName) {
        this.id = id;
        this.hoTen = hoTen;
        this.roleName = roleName;
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
} 