package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.dto.BaoCaoDto;
import com.example.quanlyxuong.entity.BaoCao;
import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.ThanhVienDuAn;
import com.example.quanlyxuong.mapper.BaoCaoMapper;
import com.example.quanlyxuong.repository.BaoCaoRepository;
import com.example.quanlyxuong.repository.NguoiDungRepository;
import com.example.quanlyxuong.repository.ThanhVienDuAnRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/thanhVien/baoCao")
@RequiredArgsConstructor
public class BaoCaoTVController {

    private final BaoCaoRepository baoCaoRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final ThanhVienDuAnRepository thanhVienDuAnRepository;

    @GetMapping
    public String hienThiBaoCao(
            Model model,
            @AuthenticationPrincipal OAuth2User principal,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        String email = principal.getAttribute("email");
        Optional<NguoiDung> optionalNguoiDung = nguoiDungRepository.findByEmailPt(email);
        if (optionalNguoiDung.isEmpty()) {
            model.addAttribute("error", "Không tìm thấy người dùng.");
            model.addAttribute("baoCaoPage", org.springframework.data.domain.Page.empty());
            return "ThanhVien/baoCao";
        }

        NguoiDung nguoiDung = optionalNguoiDung.get();
        ThanhVienDuAn thanhVien = thanhVienDuAnRepository.findByIdNguoiDung_Id(nguoiDung.getId());
        if (thanhVien == null || thanhVien.getIdDuAn() == null) {
            model.addAttribute("error", "Bạn chưa được phân công dự án.");
            model.addAttribute("baoCaoPage", org.springframework.data.domain.Page.empty());
            return "ThanhVien/baoCao";
        }

        Pageable pageable = PageRequest.of(page, 10, Sort.by("ngayBaoCao").descending());
        Page<BaoCao> baoCaoPage = baoCaoRepository.findByIdNguoiDung_IdAndIdDuAn_Id(
                nguoiDung.getId(), thanhVien.getIdDuAn().getId(), pageable
        );

        model.addAttribute("baoCaoPage", baoCaoPage);
        model.addAttribute("currentPage", page);

        model.addAttribute("totalPages", baoCaoPage.getTotalPages());
        model.addAttribute("baoCaoList", baoCaoPage.getContent());

        return "ThanhVien/baoCao";
    }


    @PostMapping("/them")
    public String themBaoCao(
            @ModelAttribute BaoCao baoCao,
            @RequestParam("ngayBaoCao") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayBaoCao,
            @AuthenticationPrincipal OAuth2User principal,
            RedirectAttributes redirectAttributes
    ) {
        String email = principal.getAttribute("email");
        Optional<NguoiDung> optionalNguoiDung = nguoiDungRepository.findByEmailPt(email);

        if (optionalNguoiDung.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng.");
            return "redirect:/thanhVien/baoCao";
        }

        NguoiDung nguoiDung = optionalNguoiDung.get();
        ThanhVienDuAn thanhVien = thanhVienDuAnRepository.findByIdNguoiDung_Id(nguoiDung.getId());

        if (thanhVien == null || thanhVien.getIdDuAn() == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn chưa được phân công dự án.");
            return "redirect:/thanhVien/baoCao";
        }

        boolean daBaoCao = baoCaoRepository.existsByIdNguoiDung_IdAndIdDuAn_IdAndNgayBaoCao(
                nguoiDung.getId(), thanhVien.getIdDuAn().getId(), ngayBaoCao
        );

        if (daBaoCao) {
            redirectAttributes.addFlashAttribute("error", "Bạn đã nộp báo cáo cho ngày này rồi.");
            return "redirect:/thanhVien/baoCao";
        }

        baoCao.setIdNguoiDung(nguoiDung);
        baoCao.setIdDuAn(thanhVien.getIdDuAn());
        baoCao.setNgayBaoCao(ngayBaoCao);
        baoCao.setTrangThai(true);
        baoCao.setNgayTao(LocalDate.now());
        baoCao.setNgayUpdate(LocalDate.now()); // nếu cần


        baoCaoRepository.save(baoCao);
        redirectAttributes.addFlashAttribute("message", "Gửi báo cáo thành công.");
        return "redirect:/thanhVien/baoCao";
    }

    @GetMapping("/xem-json")
    @ResponseBody
    public BaoCaoDto xemBaoCaoJson(
            @RequestParam("ngay") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayBaoCao,
            @AuthenticationPrincipal OAuth2User principal
    ) {
        String email = principal.getAttribute("email");
        Optional<NguoiDung> optionalNguoiDung = nguoiDungRepository.findByEmailPt(email);
        if (optionalNguoiDung.isEmpty()) return null;

        NguoiDung nguoiDung = optionalNguoiDung.get();

        ThanhVienDuAn tv = thanhVienDuAnRepository.findByIdNguoiDung_Id(nguoiDung.getId());
        if (tv == null || tv.getIdDuAn() == null) return null;

        Integer nguoiDungId = nguoiDung.getId();
        Integer duAnId = tv.getIdDuAn().getId();

        Optional<BaoCao> baoCaoOpt = baoCaoRepository.findByIdNguoiDung_IdAndIdDuAn_IdAndNgayBaoCao(
                nguoiDungId,
                duAnId,
                ngayBaoCao
        );

        return baoCaoOpt.map(BaoCaoMapper::toDto).orElse(null);
    }


    @PostMapping("/capNhat/{id}")
    public String capNhatBaoCao(
            @PathVariable Integer id,
            @ModelAttribute BaoCao baoCao,
            @AuthenticationPrincipal OAuth2User principal,
            RedirectAttributes redirectAttributes
    ) {
        String email = principal.getAttribute("email");
        Optional<NguoiDung> optionalNguoiDung = nguoiDungRepository.findByEmailPt(email);

        if (optionalNguoiDung.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng.");
            return "redirect:/thanhVien/baoCao";
        }

        NguoiDung nguoiDung = optionalNguoiDung.get();
        ThanhVienDuAn thanhVien = thanhVienDuAnRepository.findByIdNguoiDung_Id(nguoiDung.getId());

        if (thanhVien == null || thanhVien.getIdDuAn() == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn chưa được phân công dự án.");
            return "redirect:/thanhVien/baoCao";
        }

        Optional<BaoCao> baoCaoOpt = baoCaoRepository.findById(id);
        if (baoCaoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy báo cáo để cập nhật.");
            return "redirect:/thanhVien/baoCao";
        }

        BaoCao existing = baoCaoOpt.get();

        // ✅ Kiểm tra quyền cập nhật báo cáo: chỉ chủ nhân được phép sửa
        if (!existing.getIdNguoiDung().getId().equals(nguoiDung.getId())) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền sửa báo cáo này.");
            return "redirect:/thanhVien/baoCao";
        }

        // Cập nhật nội dung
        existing.setHomNayLamGi(baoCao.getHomNayLamGi());
        existing.setGapKhoKhanGi(baoCao.getGapKhoKhanGi());
        existing.setNgayMaiLamGi(baoCao.getNgayMaiLamGi());
        existing.setNgayUpdate(LocalDate.now());

        baoCaoRepository.save(existing);
        redirectAttributes.addFlashAttribute("message", "Cập nhật báo cáo thành công.");
        return "redirect:/thanhVien/baoCao";
    }

} 