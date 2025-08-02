package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.dto.DuAnDto;
import com.example.quanlyxuong.entity.DuAn;
import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.ThanhVienDuAn;
import com.example.quanlyxuong.repository.DanhSachCongViecRepository;
import com.example.quanlyxuong.repository.DuAnRepository;
import com.example.quanlyxuong.repository.NguoiDungRepository;
import com.example.quanlyxuong.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/du-an")
public class DuAnController {

    private final DuAnService duAnService;
    private final BoMonService boMonService;
    private final ChuyenNganhService chuyenNganhService;
    private final CoSoService coSoService;
    private final LoaiDuAnService loaiDuAnService;
    private final ThanhVienDuAnService thanhVienDuAnService;
    private final NguoiDungRepository nguoiDungRepository;
    private final NguoiDungService nguoiDungService;
    private final DuAnRepository duAnRepository;


    public DuAnController(DuAnService duAnService, BoMonService boMonService,
                          ChuyenNganhService chuyenNganhService, CoSoService coSoService,
                          LoaiDuAnService loaiDuAnService, ThanhVienDuAnService thanhVienDuAnService, NguoiDungRepository nguoiDungRepository, NguoiDungService nguoiDungService, DuAnRepository duAnRepository, DanhSachCongViecRepository danhSachCongViecRepository, DanhSachCongViecService danhSachCongViecService) {
        this.duAnService = duAnService;
        this.boMonService = boMonService;
        this.chuyenNganhService = chuyenNganhService;
        this.coSoService = coSoService;
        this.loaiDuAnService = loaiDuAnService;
        this.thanhVienDuAnService = thanhVienDuAnService;
        this.nguoiDungRepository = nguoiDungRepository;
        this.nguoiDungService = nguoiDungService;
        this.duAnRepository = duAnRepository;
    }

    private void loadCommonData(Model model, int page) {
        Page<DuAnDto> pageDuAn = duAnService.getListDuAnPage(PageRequest.of(page, 5));
        model.addAttribute("pageDuAn", pageDuAn);
        model.addAttribute("listBm", boMonService.findAllBoMon());
        model.addAttribute("listCn", chuyenNganhService.findAllChuyenNganh());
        model.addAttribute("listCs", coSoService.findAllCoSo());
        model.addAttribute("listLda", loaiDuAnService.findAllLoaiDuAn());
        model.addAttribute("listNd", nguoiDungService.findAllNguoiDungDangHoc());
    }

    @GetMapping
    public String getDuAnList(Model model, @RequestParam(defaultValue = "0") int page) {
        loadCommonData(model, page);

        model.addAttribute("newDuAn", new DuAn());
        model.addAttribute("updateDuAn", new DuAn());

        model.addAttribute("listNd", nguoiDungService.findAllNguoiDungDangHoc());
        return "Admin/DuAn/QuanLyDuAn";
    }

    @PostMapping("/them-moi")
    public String themMoi(@ModelAttribute("newDuAn") @Valid DuAn duAn,
                          BindingResult result,
                          @RequestParam(value = "thanhVienIds", required = false) List<Integer> thanhVienIds,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            loadCommonData(model, 0);
            model.addAttribute("showModal", true);
            model.addAttribute("updateDuAn", new DuAn());

            model.addAttribute("listNd", nguoiDungService.findAllNguoiDungDangHoc());
            return "Admin/DuAn/QuanLyDuAn";
        }

        duAnService.insertDuAn(duAn);
        if (thanhVienIds != null && !thanhVienIds.isEmpty()) {
            for (Integer idNguoiDung : thanhVienIds) {
                nguoiDungRepository.findById(idNguoiDung).ifPresent(nguoiDung -> {
                    ThanhVienDuAn tv = new ThanhVienDuAn();
                    tv.setIdDuAn(duAn);
                    tv.setIdNguoiDung(nguoiDung);
                    thanhVienDuAnService.save(tv);
                });
            }
        }
        redirectAttributes.addFlashAttribute("success", "Thêm dự án thành công!");
        return "redirect:/admin/du-an";
    }


    @GetMapping("/{id}")
    public String deleteDuAn(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            // Không cần thiết phải tìm DuAn trước nếu service của bạn đã có phương thức deleteById
            // Nhưng nếu phương thức deleteDuAn(DuAn duAn) của bạn có logic đặc biệt thì cứ giữ
            DuAn duAn = duAnService.findById(id);
            if (duAn == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy dự án với ID này!");
                return "redirect:/admin/du-an";
            }
            duAnService.deleteDuAn(duAn); // Hoặc duAnService.deleteById(id)
            redirectAttributes.addFlashAttribute("success", "Xóa dự án thành công!");

        } catch (DataIntegrityViolationException e) {
            // BẮT LỖI QUAN TRỌNG Ở ĐÂY!
            // Đây là lỗi khi cố xóa một "cha" mà vẫn còn "con" tham chiếu đến.
            redirectAttributes.addFlashAttribute("error", "Không thể xóa dự án này vì đang có dữ liệu phân công liên quan. Vui lòng xóa các phân công trước.");

        } catch (Exception e) {
            // Bắt các lỗi không mong muốn khác
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi không mong muốn khi xóa dự án!");
            e.printStackTrace(); // In lỗi ra console để debug
        }

        return "redirect:/admin/du-an";
    }

    @GetMapping("/hien-thi-cap-nhat/{id}")
    public String hienThiModalCapNhat(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            DuAn duAn = duAnService.findById(id);

            List<Integer> thanhVienIds = duAn.getThanhVienDuAns()
                    .stream()
                    .map(tv -> tv.getIdNguoiDung().getId())
                    .collect(Collectors.toList());

            duAn.setThanhVienIds(thanhVienIds);

            loadCommonData(model, 0);

            model.addAttribute("updateDuAn", duAn);

            List<NguoiDung> tatCaThanhVien = nguoiDungService.findAllNguoiDungDangHoc();
            model.addAttribute("listNd", tatCaThanhVien);

            model.addAttribute("showUpdateModal", true);
            model.addAttribute("newDuAn", new DuAn());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy dự án với ID: " + id);
            return "redirect:/admin/du-an";
        }
        return "Admin/DuAn/QuanLyDuAn";
    }




    @PostMapping("/update/{id}")
    public String updateDuAn(@PathVariable("id") Integer id,
                             @ModelAttribute("updateDuAn") @Valid DuAn duAn,
                             BindingResult result,
                             @RequestParam(value = "thanhVienIds", required = false) List<Integer> thanhVienIds,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            loadCommonData(model, 0);
            model.addAttribute("showUpdateModal", true);
            model.addAttribute("newDuAn", new DuAn());

            model.addAttribute("listNd", nguoiDungService.findAllNguoiDungDangHoc());
            return "Admin/DuAn/QuanLyDuAn";
        }

        DuAn duAnCu = duAnService.findById(id);
        duAn.setMaDuAn(duAnCu.getMaDuAn());

        duAnService.updateDuAn(duAn);

        thanhVienDuAnService.deleteByIdDuAn(duAn);

        if (thanhVienIds != null && !thanhVienIds.isEmpty()) {
            for (Integer idNguoiDung : thanhVienIds) {
                nguoiDungRepository.findById(idNguoiDung).ifPresent(nguoiDung -> {
                    ThanhVienDuAn tv = new ThanhVienDuAn();
                    tv.setIdDuAn(duAn);
                    tv.setIdNguoiDung(nguoiDung);
                    thanhVienDuAnService.save(tv);
                });
            }
        }

        redirectAttributes.addFlashAttribute("success", "Cập nhật dự án thành công!");
        return "redirect:/admin/du-an";
    }




    @GetMapping("/tim-kiem")
    public String getDuAnList(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(required = false) String tenDuAn,
                              @RequestParam(required = false) String trangThai,
                              RedirectAttributes redirectAttributes) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<DuAnDto> pageDuAn;

        Boolean trangThaiBool = null;
        if (trangThai != null && !trangThai.isEmpty()) {
            trangThaiBool = trangThai.equals("active");
        }

        if (tenDuAn != null && !tenDuAn.trim().isEmpty() || trangThaiBool != null) {
            pageDuAn = duAnService.findByTenDuAnAndTrangThai(tenDuAn != null ? tenDuAn.trim() : null, trangThaiBool, pageable);
        } else {
            pageDuAn = duAnService.getListDuAnPage(pageable);
        }

        model.addAttribute("pageDuAn", pageDuAn);
        model.addAttribute("listBm", boMonService.findAllBoMon());
        model.addAttribute("listCn", chuyenNganhService.findAllChuyenNganh());
        model.addAttribute("listCs", coSoService.findAllCoSo());
        model.addAttribute("listLda", loaiDuAnService.findAllLoaiDuAn());
        model.addAttribute("listTv", thanhVienDuAnService.findAllThanhVienDuAn());
        model.addAttribute("newDuAn", new DuAn());
        model.addAttribute("updateDuAn", new DuAn());
        model.addAttribute("tenDuAn", tenDuAn);
        model.addAttribute("trangThai", trangThai);
        return "Admin/DuAn/QuanLyDuAn";
    }


    @GetMapping("/export-excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        duAnService.exportDuAnToExcel(response);
    }

    @PostMapping("/import-excel")
    public String importDuAnFromExcel(@RequestParam("file") MultipartFile file,
                                      RedirectAttributes redirectAttributes) {
        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng chọn file Excel để nhập!");
                return "redirect:/admin/du-an";
            }
            duAnService.importDuAnFromExcel(file);
            redirectAttributes.addFlashAttribute("success", "Nhập dự án thành công!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi nhập file Excel: " + e.getMessage());
        }
        return "redirect:/admin/du-an";
    }

    @GetMapping("/hien-thi-du-an")
    public String hienThiDuAn(Model model) {
        List<DuAn> danhSachDuAn = duAnRepository.findAll(); // hoặc duAnService.findAll()
        model.addAttribute("danhSachDuAn", danhSachDuAn);
        return "QuanLy/DanhSachDuAn";
    }
    @GetMapping("/hien-thi-du-an-thanh-vien")
    public String hienThiDuAnThanhVien(Model model) {
        List<DuAn> danhSachDuAn = duAnRepository.findAll(); // hoặc duAnService.findAll()
        model.addAttribute("danhSachDuAn", danhSachDuAn);
        return "ThanhVien/DanhSachDuAnThanhVien";
    }

}