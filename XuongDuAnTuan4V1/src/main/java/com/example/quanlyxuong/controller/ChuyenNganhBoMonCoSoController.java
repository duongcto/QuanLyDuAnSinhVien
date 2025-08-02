package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.dto.ChuyenNganhBoMonCoSoDto;
import com.example.quanlyxuong.entity.BoMonCoSo;
import com.example.quanlyxuong.service.BoMonCoSoService;
import com.example.quanlyxuong.service.BoMonService;
import com.example.quanlyxuong.service.ChuyenNganhBoMonCoSoService;
import com.example.quanlyxuong.service.ChuyenNganhService;
import com.example.quanlyxuong.service.CoSoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.stream.Collectors;
import com.example.quanlyxuong.dto.BoMonCoSoDto;

@Controller
@RequestMapping("/admin/boMon/chuyenNganhBoMonCoSo")
public class ChuyenNganhBoMonCoSoController {
    
    @Autowired
    private ChuyenNganhBoMonCoSoService chuyenNganhBoMonCoSoService;
    
    @Autowired
    private ChuyenNganhService chuyenNganhService;
    
    @Autowired
    private BoMonService boMonService;
    
    @Autowired
    private CoSoService coSoService;
    
    @Autowired
    private BoMonCoSoService boMonCoSoService;

    @GetMapping
    public String hienThi(Model model,
                      @ModelAttribute("chuyenNganhBoMonCoSoForm") ChuyenNganhBoMonCoSoDto form,
                      @RequestParam(value = "editIdChuyenNganh", required = false) Integer editIdChuyenNganh,
                      @RequestParam(value = "editIdBoMon", required = false) Integer editIdBoMon,
                      @RequestParam(value = "editIdCoSo", required = false) Integer editIdCoSo,
                      @RequestParam(defaultValue = "0") int page) {
        int pageSize = 5;
        Page<ChuyenNganhBoMonCoSoDto> pageData = chuyenNganhBoMonCoSoService.getChuyenNganhBoMonCoSoPage(page, pageSize);
        model.addAttribute("chuyenNganhBoMonCoSos", pageData.getContent());
        model.addAttribute("currentPage", pageData.getNumber());
        model.addAttribute("totalPages", pageData.getTotalPages());
        model.addAttribute("chuyenNganhs", chuyenNganhService.getAll());
        model.addAttribute("boMons", boMonService.getBoMon());

        ChuyenNganhBoMonCoSoDto dto;
        if (editIdChuyenNganh != null && editIdBoMon != null && editIdCoSo != null) {
            dto = chuyenNganhBoMonCoSoService.getById(editIdChuyenNganh, editIdBoMon, editIdCoSo);
        } else {
            dto = form; // Dùng lại object nhận từ form khi submit GET
        }
        model.addAttribute("chuyenNganhBoMonCoSoForm", dto);

        Integer idBoMon = dto.getIdBoMon();
        if (idBoMon != null) {
            List<BoMonCoSoDto> boMonCoSoListForm = boMonCoSoService.getAll().stream()
                .filter(bmcs -> bmcs.getIdBoMon().equals(idBoMon))
                .collect(Collectors.toList());
            List<Integer> coSoIdsForm = boMonCoSoListForm.stream().map(BoMonCoSoDto::getIdCoSo).collect(Collectors.toList());
            model.addAttribute("coSosForm", coSoService.getAll().stream().filter(cs -> coSoIdsForm.contains(cs.getId())).collect(Collectors.toList()));
        } else {
            model.addAttribute("coSosForm", coSoService.getAll());
        }
        return "Admin/BoMon/quanLyChuyenNganhBoMonCoSo";
    }

    @PostMapping("/save")
    public String save(
        @ModelAttribute("chuyenNganhBoMonCoSoForm") ChuyenNganhBoMonCoSoDto dto,
        @RequestParam(value = "oldIdChuyenNganh", required = false) Integer oldIdChuyenNganh,
        @RequestParam(value = "oldIdBoMon", required = false) Integer oldIdBoMon,
        @RequestParam(value = "oldIdCoSo", required = false) Integer oldIdCoSo,
        RedirectAttributes redirectAttributes) {

        if (oldIdChuyenNganh != null && oldIdBoMon != null && oldIdCoSo != null) {
            // Đang sửa, gọi service update với id cũ và dto mới
            chuyenNganhBoMonCoSoService.update(oldIdChuyenNganh, oldIdBoMon, oldIdCoSo, dto);
            redirectAttributes.addFlashAttribute("message", "Cập nhật thành công!");
        } else {
            // Thêm mới
            chuyenNganhBoMonCoSoService.save(dto);
            redirectAttributes.addFlashAttribute("message", "Lưu thành công!");
        }
        return "redirect:/admin/boMon/chuyenNganhBoMonCoSo";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("idChuyenNganh") Integer idChuyenNganh,
                        @RequestParam("idBoMon") Integer idBoMon,
                        @RequestParam("idCoSo") Integer idCoSo,
                        RedirectAttributes redirectAttributes) {
        chuyenNganhBoMonCoSoService.delete(idChuyenNganh, idBoMon, idCoSo);
        redirectAttributes.addFlashAttribute("message", "Xóa thành công!");
        return "redirect:/admin/boMon/chuyenNganhBoMonCoSo";
    }
} 