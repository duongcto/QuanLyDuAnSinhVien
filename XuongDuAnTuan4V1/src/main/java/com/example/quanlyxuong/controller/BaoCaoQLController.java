package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.dto.BaoCaoDto;
import com.example.quanlyxuong.entity.BaoCao;
import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.ThanhVienDuAn;
import com.example.quanlyxuong.service.BaoCaoService;
import com.example.quanlyxuong.service.NguoiDungService;
import com.example.quanlyxuong.service.ThanhVienDuAnService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/quanLy/baoCao")
@RequiredArgsConstructor
public class BaoCaoQLController {

    private final BaoCaoService baoCaoService;
    private final NguoiDungService nguoiDungService;
    private final ThanhVienDuAnService thanhVienDuAnService;


    @GetMapping
    public String getBaoCaoList(Model model ,@AuthenticationPrincipal OAuth2User principal) {
        // Lay ra thong tin nguoi dung
        String email = principal.getAttribute("email");
        NguoiDung nguoiDung = nguoiDungService.findIdNguoiDung(email);
        ThanhVienDuAn thanhVienDuAn = thanhVienDuAnService.findIdDuAn(nguoiDung);
        if (thanhVienDuAn == null) {
            model.addAttribute("error", "Bạn chưa tham gia dự án nào!");
            model.addAttribute("tenDuAn", "");
            model.addAttribute("thanhVienThamGiaDuAn", null);
            model.addAttribute("baoCaoHomNay", null);
            model.addAttribute("ngayHomNay", LocalDate.now().toString());
            return "QuanLy/report";
        }
        model.addAttribute("tenDuAn", thanhVienDuAn.getIdDuAn().getTenDuAn());
        model.addAttribute("thanhVienThamGiaDuAn", thanhVienDuAn.getIdDuAn().getThanhVienDuAns());

        String ngayHienTai = LocalDate.now().toString(); // yyyy-MM-dd
        List<BaoCaoDto> bcHomNay = baoCaoService.getBaoCaoByDate(ngayHienTai);

        model.addAttribute("baoCaoHomNay", bcHomNay);
        model.addAttribute("ngayHomNay", ngayHienTai);

        return "QuanLy/report";
    }

    @GetMapping("/ngay")
    public ResponseEntity<List<BaoCaoDto>> getBaoCaoTheoNgay(@RequestParam String date) {
        List<BaoCaoDto> ds = baoCaoService.getBaoCaoByDate(date);
        return ResponseEntity.ok(ds);
    }


    @GetMapping("/export")
    public void exportBaoCao(@RequestParam int month,
                             @RequestParam int year,
                             HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");
        String fileName = "bao-cao-" + month + "-" + year + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        baoCaoService.exportBaoCaoTheoThang(response.getOutputStream(), month, year);
    }

}
