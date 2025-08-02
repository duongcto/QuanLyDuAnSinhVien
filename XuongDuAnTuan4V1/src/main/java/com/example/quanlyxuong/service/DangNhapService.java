package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DangNhapService {
    @Autowired
    private NguoiDungRepository nguoiDungRepo;

    public DangNhapService(NguoiDungRepository nguoiDungRepository){
        this.nguoiDungRepo=nguoiDungRepository;
    }

    public List<NguoiDung> getAlNguoiDungs(){
        return nguoiDungRepo.findAll();
    }

    public NguoiDung dangNhap(String tenDangNhap, String matKhau, String emailFe, String emailPt) {
        // Tìm người dùng theo tên đăng nhập
        NguoiDung nguoidung = nguoiDungRepo.findByTenDangNhapAndEmailFeOrEmailPt(tenDangNhap, emailFe, emailPt);

        // Kiểm tra người dùng tồn tại và mật khẩu đúng
        if(nguoidung != null && 
           nguoidung.getTenDangNhap().equals(tenDangNhap) &&
           nguoidung.getMatKhau().equals(matKhau) && 
           (nguoidung.getEmailFe().equals(emailFe) || nguoidung.getEmailPt().equals(emailPt))) {
            return nguoidung;
        }
        return null;
    }
}
