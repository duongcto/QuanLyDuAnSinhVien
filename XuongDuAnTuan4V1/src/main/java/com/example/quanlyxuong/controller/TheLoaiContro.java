package com.example.quanlyxuong.controller;


import com.example.quanlyxuong.Excel_Import.ExcelTheLoaiService;
import com.example.quanlyxuong.dto.TheLoaiDto;
import com.example.quanlyxuong.entity.LoaiDuAn;
import com.example.quanlyxuong.service.TheLoaiService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class TheLoaiContro {
    private final TheLoaiService theLoaiService;
    private final ExcelTheLoaiService excelTheLoaiService;

    public TheLoaiContro(TheLoaiService theLoaiService, ExcelTheLoaiService excelTheLoaiService) {
        this.theLoaiService = theLoaiService;
        this.excelTheLoaiService = excelTheLoaiService;
    }

    @GetMapping("/the_loai") // Giả sử đây là endpoint của bạn
    public String getTheLoaiPage(@RequestParam(defaultValue = "0") Optional<Integer> page, // Mặc định là 0 nếu không có
                                 @RequestParam(defaultValue = "5") Optional<Integer> size, // Mặc định là 10 nếu không có
                                 Model model) {

        int currentPage = page.orElse(0); // Lấy giá trị hoặc mặc định là 0
        int pageSize = size.orElse(5);  // Lấy giá trị hoặc mặc định là 5

        // Giả sử bạn có một service để lấy dữ liệu phân trang
        Page<TheLoaiDto> theLoaiPage = theLoaiService.findAll(PageRequest.of(currentPage, pageSize));

        int totalPages = theLoaiPage.getTotalPages();
        if (totalPages == 0) { // Đảm bảo totalPages không âm hoặc 0 nếu không có dữ liệu
            totalPages = 1; // Đặt ít nhất là 1 trang để tránh lỗi khi sequence(0, -1)
        }


        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("theLoai", theLoaiPage.getContent()); // Dữ liệu của trang hiện tại
        if (!model.containsAttribute("theLoaiDto")) {
            model.addAttribute("theLoaiDto", new TheLoaiDto());
        }
        return "Admin/TheLoai/theloai";
    }

    @PostMapping("/addTheLoai")
    public String add(@Valid @ModelAttribute("theLoaiDto") TheLoaiDto theLoaiDto,
                      BindingResult bindingResult, RedirectAttributes redirectAttributes,Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("theLoaiDto", theLoaiDto); // Giữ lại dữ liệu đã nhập và các lỗi
            model.addAttribute("showAddModal", true);
            return "Admin/TheLoai/theloai";
        }
        try {
            theLoaiService.save(theLoaiDto);
            redirectAttributes.addFlashAttribute("message", "Thêm thể loại thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi khi thêm thể loại: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/the_loai";
    }

    @GetMapping("/editTheLoai/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        LoaiDuAn theLoai = theLoaiService.getLoaiDuAnById(id);
        model.addAttribute("theLoaiDto", theLoai);
        return "Admin/TheLoai/updateTL"; // Tạo một form riêng để sửa
    }

    @PostMapping("/updateTheLoai")
    public String update(@Valid @ModelAttribute("theLoaiDto") TheLoaiDto theLoaiDto,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/Admin/TheLoai/updateTL";
        }
        try {
            theLoaiService.update(theLoaiDto); // Spring Data JPA sẽ tự động cập nhật nếu ID tồn tại
            redirectAttributes.addFlashAttribute("message", "Cập nhật thể loại thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi khi cập nhật thể loại: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/the_loai";
    }

    @GetMapping("/deleteTheLoai/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            theLoaiService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Xóa thể loại thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi khi xóa thể loại: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/the_loai";
    }

    @GetMapping("/excel")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        excelTheLoaiService.exportStaffMajorTemplate(response);
    }


    @PostMapping("/import")
    public String importExcel(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            theLoaiService.importExcel(file);
            redirectAttributes.addFlashAttribute("message", "Import thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Import thất bại: " + e.getMessage());
        }
        return "redirect:/the_loai";
    }

    @GetMapping("/timkiem")
    public String searchTheLoai(@RequestParam("tenLoaiDuAn") String tenLoaiDuAn, Model model) {
        List<TheLoaiDto> results = theLoaiService.searchByTen(tenLoaiDuAn);
        model.addAttribute("theLoai", results);
        model.addAttribute("tenLoaiDuAn", tenLoaiDuAn);
        return "Admin/TheLoai/theloai"; // View dùng để hiển thị kết quả
    }
}
