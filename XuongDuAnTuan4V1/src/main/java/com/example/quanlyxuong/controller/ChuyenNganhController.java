package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.dto.BoMonDto;
import com.example.quanlyxuong.dto.ChuyenNganhDto;
import com.example.quanlyxuong.entity.ChuyenNganh;
import com.example.quanlyxuong.exception.ResourceNotFoundException;
import com.example.quanlyxuong.service.BoMonService;
import com.example.quanlyxuong.service.ChuyenNganhService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import org.springframework.data.domain.Page;

@Controller
@RequestMapping("/admin/boMon/chuyen-nganh")
@RequiredArgsConstructor
public class ChuyenNganhController {

    private final BoMonService boMonService;
    private final ChuyenNganhService chuyenNganhService;

    @GetMapping("/hien-thi-json")
    @ResponseBody
    public List<BoMonDto> getBoMonJson() {
        return boMonService.getBoMon();
    }

    @GetMapping
    public String quanLyChuyenNganh(Model model,
                                    @RequestParam(value = "editChuyenNganhId", required = false) Integer editId,
                                    @RequestParam(defaultValue = "0") int page) {
        int pageSize = 5;
        Page<ChuyenNganh> chuyenNganhPage = chuyenNganhService.getChuyenNganhPage(page, pageSize);
        model.addAttribute("chuyenNganhs", chuyenNganhPage.getContent());
        model.addAttribute("currentPage", chuyenNganhPage.getNumber());
        model.addAttribute("totalPages", chuyenNganhPage.getTotalPages());
        model.addAttribute("prevPage", chuyenNganhPage.getNumber() > 0 ? chuyenNganhPage.getNumber() - 1 : 0);
        model.addAttribute("nextPage", chuyenNganhPage.getNumber() < chuyenNganhPage.getTotalPages() - 1 ? chuyenNganhPage.getNumber() + 1 : chuyenNganhPage.getTotalPages() - 1);
        model.addAttribute("lastPage", chuyenNganhPage.getTotalPages() > 0 ? chuyenNganhPage.getTotalPages() - 1 : 0);
        model.addAttribute("boMons", boMonService.getBoMon());
        if (editId != null) {
            model.addAttribute("chuyenNganhForm", chuyenNganhService.getById(editId));
        } else {
            model.addAttribute("chuyenNganhForm", new ChuyenNganh());
        }
        return "Admin/BoMon/quanLyChuyenNganh";
    }

    @PostMapping("/save")
    public String saveChuyenNganh(@ModelAttribute("chuyenNganh") ChuyenNganh chuyenNganh,
                                  RedirectAttributes redirectAttributes) {
        chuyenNganhService.save(chuyenNganh);
        redirectAttributes.addFlashAttribute("message", "Lưu thành công!");
        return "redirect:/admin/boMon/chuyen-nganh";
    }

    @GetMapping("/deleteChuyenNganh/{id}")
    public ResponseEntity<String> deleteChuyenNganh(@PathVariable("id") Integer id) {

        chuyenNganhService.delete(id);

        return ResponseEntity.ok("Xóa thành công");
    }

    @GetMapping("/toggleTrangThai/{id}")
    public String toggleTrangThai(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        ChuyenNganh cn = chuyenNganhService.getById(id);
        if (cn != null) {
            cn.setTrangThai(cn.getTrangThai() == null ? Boolean.FALSE : !cn.getTrangThai());
            chuyenNganhService.save(cn);
            redirectAttributes.addFlashAttribute("message", "Đã chuyển trạng thái thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy chuyên ngành!");
        }
        return "redirect:/admin/boMon/chuyen-nganh";
    }

    @GetMapping("/detailChuyenNganh/{id}")
    public ResponseEntity<ChuyenNganhDto> getChuyenNganh(@PathVariable("id") Integer id) {
        ChuyenNganh chuyenNganh = chuyenNganhService.getById(id);
        ChuyenNganhDto dto = new ChuyenNganhDto();
        dto.setId(chuyenNganh.getId());
        dto.setMaChuyenNganh(chuyenNganh.getMaChuyenNganh());
        dto.setTenChuyenNganh(chuyenNganh.getTenChuyenNganh());
        if (chuyenNganh.getIdBoMon() != null) {
            dto.setIdBoMon(chuyenNganh.getIdBoMon().getId());
            dto.setTenBoMon(chuyenNganh.getIdBoMon().getTenBoMon());
        }
        dto.setTrangThai(chuyenNganh.getTrangThai());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/updateChuyenNganh/{id}")
    public ResponseEntity<?> updateChuyenNganh(@Valid @RequestBody ChuyenNganh chuyenNganh, @PathVariable("id") Integer id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            chuyenNganhService.updateChuyenNganh(chuyenNganh, id);
            return ResponseEntity.ok("Cập nhật thành công");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }
} 