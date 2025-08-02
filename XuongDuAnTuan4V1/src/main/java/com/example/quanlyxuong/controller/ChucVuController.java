package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.entity.ChucVu;
import com.example.quanlyxuong.repository.ChucVuRepository;
import com.example.quanlyxuong.service.ChucVuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ChucVuController {
    @Autowired
    private ChucVuService chucvuService;

    @GetMapping("/hienThiChucVu")
    public String hienThiChucVu(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "5") int size){
        Page<ChucVu> listChucVu = chucvuService.getAllChucVu(page, size);
        model.addAttribute("listCV",listChucVu);
        model.addAttribute("trangDau",page);
        model.addAttribute("tongTrang",listChucVu.getTotalPages());
        model.addAttribute("newCV", new ChucVu());
        return "Admin/ChucVu/ChucVuList";
    }

    @PostMapping("/updateChucVu")
    public String updateChucVu(@Valid @ModelAttribute("chucvu") ChucVu chucVu, Model model, Errors errors,
                            RedirectAttributes redirectAttributes){
        if(errors.hasErrors()){
            model.addAttribute("error","Update nhân viên thất bại!");
            return "Admin/ChucVu/ChucVuList";
        }
        chucvuService.addChucVu(chucVu);
        model.addAttribute("newCV", new ChucVu());
        redirectAttributes.addFlashAttribute("message","Update chức vụ thành công!");
        return "redirect:/hienThiChucVu";
    }
    @PostMapping("/addChucVu")
    public String addChucVu(@Valid @ModelAttribute("newCV") ChucVu chucVu, Errors errors,Model model,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "size", defaultValue = "5") int size,
                            RedirectAttributes redirectAttributes){
        if(errors.hasErrors()){
            Page<ChucVu> listChucVu = chucvuService.getAllChucVu(page, size);
            model.addAttribute("listCV",listChucVu);
            model.addAttribute("trangDau",page);
            model.addAttribute("tongTrang",listChucVu.getTotalPages());
            model.addAttribute("error","Thêm nhân viên thất bại!");
            return "Admin/ChucVu/ChucVuList";
        }
        if(chucvuService.checkTrungMaChucVu(chucVu.getMaChucVu())){
            Page<ChucVu> listChucVu = chucvuService.getAllChucVu(page, size);
            model.addAttribute("listCV",listChucVu);
            model.addAttribute("trangDau",page);
            model.addAttribute("tongTrang",listChucVu.getTotalPages());
            model.addAttribute("error","Chức vụ đã tồn tai!");
            return "Admin/ChucVu/ChucVuList";
        }

        chucvuService.addChucVu(chucVu);
        model.addAttribute("newCV", new ChucVu());
        redirectAttributes.addFlashAttribute("message","Thêm chức vụ thành công!");
        return "redirect:/hienThiChucVu";
    }

    @GetMapping("/deleteChucVu/{id}")
    public String deleteChucVu(@PathVariable("id") int id, Model model,
                               RedirectAttributes redirectAttributes){
       ChucVu listCV = chucvuService.getCVById(id);
        chucvuService.deleteChucVu(id);
        redirectAttributes.addFlashAttribute("message","Xóa chức vụ thành công");
        return "redirect:/hienThiChucVu";
    }
    @GetMapping("/editChucVu/{id}")
    public String editChucVu(@PathVariable("id") int id, Model model,@RequestParam(name = "page", defaultValue = "0") int page,
                             @RequestParam(name = "size", defaultValue = "5") int size){
        ChucVu chucvu = chucvuService.getCVById(id);
        model.addAttribute("newCV", chucvu);
        model.addAttribute("listCV", chucvuService.getAllChucVu(page, size));
        return "Admin/ChucVu/ChucVuUpdate";
    }

    @GetMapping("/locChucVuTheoTrangThai")
    public String locChucVuTheoTrangThai(@RequestParam("trangThai") Boolean trangThai, Model model, @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "5") int size){
        Page<ChucVu> locTheoTrangThai = chucvuService.locTheoTrangThai(trangThai, page, size);
        model.addAttribute("listCV", locTheoTrangThai);
        model.addAttribute("trangDau",page);
        model.addAttribute("tongTrang",locTheoTrangThai.getTotalPages());
        model.addAttribute("newCV", new ChucVu());
        return "Admin/ChucVu/ChucVuList";
    }
    @GetMapping("/timKiemChucVu")
    public String timKiemChucVu(@RequestParam("tuKhoa") String tuKhoa, Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                                         @RequestParam(name = "size", defaultValue = "5") int size){
        Page<ChucVu> timKiemChucVu = chucvuService.timKiemTheoMaHoacTenChucVu(tuKhoa, page, size);
        model.addAttribute("listCV", timKiemChucVu);
        model.addAttribute("trangDau",page);
        model.addAttribute("tongTrang",timKiemChucVu.getTotalPages());
        model.addAttribute("newCV", new ChucVu());
        return "Admin/ChucVu/ChucVuList";
    }
}
