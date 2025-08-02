package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.dto.BoMonCoSoDto;
import com.example.quanlyxuong.dto.ChuyenNganhBoMonCoSoDto;
import com.example.quanlyxuong.service.BoMonCoSoService;
import com.example.quanlyxuong.service.BoMonService;
import com.example.quanlyxuong.service.CoSoService;
import com.example.quanlyxuong.service.ChuyenNganhBoMonCoSoService;
import com.example.quanlyxuong.service.ChuyenNganhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/boMon/boMonCoSo")
public class BoMonCoSoController {
    @Autowired
    private BoMonCoSoService boMonCoSoService;
    @Autowired
    private BoMonService boMonService;
    @Autowired
    private CoSoService coSoService;
    @Autowired
    private ChuyenNganhBoMonCoSoService chuyenNganhBoMonCoSoService;
    @Autowired
    private ChuyenNganhService chuyenNganhService;

    @GetMapping
    public String hienThi(Model model,
                      @RequestParam(defaultValue = "0") int page,
                      @RequestParam(value = "editIdBoMon", required = false) Integer editIdBoMon,
                      @RequestParam(value = "editIdCoSo", required = false) Integer editIdCoSo,
                      @RequestParam(value = "openChuyenNganhBoMonCoSoModal", required = false) Boolean openChuyenNganhBoMonCoSoModal,
                      @RequestParam(value = "editIdChuyenNganh", required = false) Integer editIdChuyenNganh,
                      @RequestParam(value = "editIdBoMonCN", required = false) Integer editIdBoMonCN,
                      @RequestParam(value = "editIdCoSoCN", required = false) Integer editIdCoSoCN) {
        int pageSize = 5;
        Page<BoMonCoSoDto> pageData = boMonCoSoService.getBoMonCoSoPage(page, pageSize);
        model.addAttribute("boMonCoSos", pageData.getContent());
        model.addAttribute("currentPage", pageData.getNumber());
        model.addAttribute("totalPages", pageData.getTotalPages());
        model.addAttribute("boMons", boMonService.getBoMon());
        model.addAttribute("coSos", coSoService.getAll());
        if (editIdBoMon != null && editIdCoSo != null) {
            model.addAttribute("boMonCoSoForm", boMonCoSoService.getById(editIdBoMon, editIdCoSo));
        } else {
            model.addAttribute("boMonCoSoForm", new BoMonCoSoDto());
        }

        // Modal chuyên ngành theo cơ sở
        if (openChuyenNganhBoMonCoSoModal != null && openChuyenNganhBoMonCoSoModal) {
            model.addAttribute("openChuyenNganhBoMonCoSoModal", true);
            model.addAttribute("chuyenNganhBoMonCoSoList", chuyenNganhBoMonCoSoService.getAll());
            model.addAttribute("chuyenNganhs", chuyenNganhService.getAll());
            model.addAttribute("boMons", boMonService.getBoMon());
            model.addAttribute("coSos", coSoService.getAll());
            if (editIdChuyenNganh != null && editIdBoMonCN != null && editIdCoSoCN != null) {
                model.addAttribute("chuyenNganhBoMonCoSoForm", chuyenNganhBoMonCoSoService.getById(editIdChuyenNganh, editIdBoMonCN, editIdCoSoCN));
            } else {
                model.addAttribute("chuyenNganhBoMonCoSoForm", new ChuyenNganhBoMonCoSoDto());
            }
        } else {
            model.addAttribute("openChuyenNganhBoMonCoSoModal", false);
        }
        return "Admin/BoMon/quanLyBoMonCoSo";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("boMonCoSoForm") BoMonCoSoDto dto, RedirectAttributes redirectAttributes) {
        boMonCoSoService.save(dto);
        redirectAttributes.addFlashAttribute("message", "Lưu thành công!");
        return "redirect:/admin/boMon/boMonCoSo";
    }

    // CRUD cho ChuyenNganhBoMonCoSo
    @PostMapping("/chuyenNganhBoMonCoSo/save")
    public String saveChuyenNganhBoMonCoSo(@ModelAttribute("chuyenNganhBoMonCoSoForm") ChuyenNganhBoMonCoSoDto dto, RedirectAttributes ra) {
        chuyenNganhBoMonCoSoService.save(dto);
        ra.addFlashAttribute("message", "Lưu thành công!");
        ra.addFlashAttribute("openChuyenNganhBoMonCoSoModal", true);
        return "redirect:/admin/boMon/boMonCoSo?openChuyenNganhBoMonCoSoModal=true";
    }

    @PostMapping("/chuyenNganhBoMonCoSo/delete")
    public String deleteChuyenNganhBoMonCoSo(@RequestParam("idChuyenNganh") Integer idChuyenNganh,
                                             @RequestParam("idBoMon") Integer idBoMon,
                                             @RequestParam("idCoSo") Integer idCoSo,
                                             RedirectAttributes ra) {
        chuyenNganhBoMonCoSoService.delete(idChuyenNganh, idBoMon, idCoSo);
        ra.addFlashAttribute("message", "Xóa thành công!");
        ra.addFlashAttribute("openChuyenNganhBoMonCoSoModal", true);
        return "redirect:/admin/boMon/boMonCoSo?openChuyenNganhBoMonCoSoModal=true";
    }
} 