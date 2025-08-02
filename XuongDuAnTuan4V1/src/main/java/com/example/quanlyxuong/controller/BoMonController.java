package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.dto.BoMonDto;
import com.example.quanlyxuong.dto.ChuyenNganhDto;
import com.example.quanlyxuong.entity.BoMon;
import com.example.quanlyxuong.entity.ChuyenNganh;
import com.example.quanlyxuong.exception.ResourceNotFoundException;
import com.example.quanlyxuong.service.BoMonService;
import com.example.quanlyxuong.service.ChuyenNganhService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

//@CrossOrigin(origins = "http://localhost:5173")
@Controller
@RequestMapping("/admin/boMon")
@RequiredArgsConstructor
public class BoMonController {

    private final BoMonService boMonService;
    private final ChuyenNganhService chuyenNganhService;

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String fileName = "Danh_sach_bo_mon.xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            boMonService.exportToExcel(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/import")
    public String importBoMon(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn file Excel để import!");
            return "redirect:/admin/boMon";
        }

        try {
            List<String> errors = boMonService.importFromExcel(file);
            if (!errors.isEmpty()) {
                String errorMessage = String.join("<br>", errors);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            } else {
                redirectAttributes.addFlashAttribute("message", "Import thành công!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Import lỗi: " + e.getMessage());
        }

        return "redirect:/admin/boMon";
    }

    @GetMapping("/hien-thi-json")
    @ResponseBody
    public List<BoMonDto> getBoMonJson() {
        return boMonService.getBoMon();
    }

    @GetMapping("/hien-thi")
    public String getBoMon(Model model) {
        List<BoMonDto> boMons = boMonService.getBoMon();
        model.addAttribute("boMons", boMons);
        return "Admin/BoMon/quanLyBoMon";
    }

    @PostMapping("/addBoMon")
    public ResponseEntity<?> addBoMon(@RequestBody @Valid BoMon boMon, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            BoMon saved = boMonService.addBoMon(boMon);
            return ResponseEntity.ok("Thêm bộ môn thành công");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/detailBoMon/{id}")
    public ResponseEntity<BoMon> getBoMonCanTim(@PathVariable("id") Integer id) {

        BoMon boMon = boMonService.getBoMonCanTim(id);

        return ResponseEntity.ok(boMon);
    }

    @PostMapping("/updateBoMon/{id}")
    public ResponseEntity<?> updateBoMon(@Valid @RequestBody BoMon boMon, @PathVariable("id") Integer id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            boMonService.updateBoMon(boMon, id);
            return ResponseEntity.ok("Cập nhật thành công");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

    @GetMapping("/deleteBoMon/{id}")
    public ResponseEntity<String> deleteBoMon(@PathVariable("id") Integer id) {

        boMonService.deleteBoMon(id);

        return ResponseEntity.ok("Xóa thành công");
    }

    @GetMapping("/searchBoMon")
    public ResponseEntity<List<BoMonDto>> searchBoMon(@RequestParam("keyword") String keyword) {
        List<BoMonDto> results = boMonService.searchAllFields(keyword);
        return ResponseEntity.ok(results);
    }

    @GetMapping
    public String getAllBoMon(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 5;
        Page<BoMon> boMons = boMonService.getBoMonPage(page, pageSize);
        int currentPage = boMons.getNumber();
        int totalPages = boMons.getTotalPages();
        model.addAttribute("boMons", boMons.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("prevPage", currentPage > 0 ? currentPage - 1 : 0);
        model.addAttribute("nextPage", currentPage < totalPages - 1 ? currentPage + 1 : totalPages - 1);
        model.addAttribute("lastPage", totalPages > 0 ? totalPages - 1 : 0);
        return "Admin/BoMon/quanLyBoMon";
    }
}
