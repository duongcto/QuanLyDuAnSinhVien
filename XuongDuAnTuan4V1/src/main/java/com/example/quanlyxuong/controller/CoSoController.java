package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.Excel_Import.ExcelHelperCoSo;
import com.example.quanlyxuong.entity.CoSo;
import com.example.quanlyxuong.repository.CoSoRepository;
import com.example.quanlyxuong.service.CoSoService;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class CoSoController {

    @Autowired
    CoSoRepository coSoRepository;

    @Autowired
    CoSoService coSoService;

    @GetMapping("/hien-thi-co-so")
    public String hienThi(Model model, CoSo coSo){
        model.addAttribute("coSo", new CoSo());
        model.addAttribute("listCoSo", coSoRepository.findAll());
        return "Admin/CoSo/QuanLyCoSo";
    }
    @PostMapping("/addCoSo")
    public String addCoSo(@Valid @ModelAttribute("coSo") CoSo coSo, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("listCoSo", coSoRepository.findAll());
            model.addAttribute("openAddModal", true);
            return "Admin/CoSo/QuanLyCoSo";
        }
        if (coSoService.isMaCoSoDaTonTai(coSo.getMaCoSo())){
            model.addAttribute("listCoSo", coSoRepository.findAll());
            model.addAttribute("openAddModal", true);
            model.addAttribute("maCoSoError", "Mã cơ sở đã tồn tại!");
            return "Admin/CoSo/QuanLyCoSo";
        }
        coSoRepository.save(coSo);
        return "redirect:/hien-thi-co-so";
    }
    @GetMapping("/openAddCoSo")
    public String openAddCoSo(Model model) {
        model.addAttribute("coSo", new CoSo()); // reset object trống
        model.addAttribute("listCoSo", coSoRepository.findAll());
        model.addAttribute("openAddModal", true); // để mở modal thêm
        return "Admin/CoSo/QuanLyCoSo";
    }

    @PostMapping("/updateCoSo/{id}")
    public String updateCoSo(@Valid @ModelAttribute("coSo") CoSo coSo, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("listCoSo", coSoRepository.findAll());
            model.addAttribute("openUpdateModal", true);
            return "Admin/CoSo/QuanLyCoSo";
        }
        coSoRepository.save(coSo);
        return "redirect:/hien-thi-co-so";
    }
    @GetMapping("/deleteCoSo/{id}")
    public String deleteCoSo(@PathVariable("id") Integer id, CoSo coSo){
        coSoRepository.deleteById(id);
        return "redirect:/hien-thi-co-so";
    }
    @GetMapping("/detailCoSo/{id}")
    public String detail(@PathVariable("id") Integer id, Model model){
        CoSo coSo = coSoRepository.findById(id).get();
        model.addAttribute("coSo", coSo);
        model.addAttribute("listCoSo", coSoRepository.findAll());
        model.addAttribute("openUpdateModal", true);
        return "Admin/CoSo/QuanLyCoSo";
    }
    @GetMapping("/status/{id}")
    public String trangThai(@PathVariable("id") Integer id) {
        CoSo coSo = coSoRepository.findById(id).orElse(null);
        if (coSo != null) {
            Boolean currentStatus = coSo.getTrangThai();
            coSo.setTrangThai(!currentStatus);
            coSoRepository.save(coSo);
        }
        return "redirect:/hien-thi-co-so";
    }
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String tenCoSo,
                         @RequestParam(required = false) Boolean trangThai,
                         Model model) {

        if (tenCoSo != null && tenCoSo.isEmpty()) {
            tenCoSo = null;
        }

        List<CoSo> danhSach;

        // Nếu trạng thái không null => lọc theo tên và trạng thái
        if (trangThai != null) {
            danhSach = coSoRepository.findByTenCoSoContainingIgnoreCaseAndTrangThai(tenCoSo == null ? "" : tenCoSo, trangThai);
        } else if (tenCoSo != null) {
            // Chỉ lọc theo tên
            danhSach = coSoRepository.findByTenCoSoContainingIgnoreCase(tenCoSo);
        } else {
            // Không lọc gì cả, trả toàn bộ
            danhSach = coSoRepository.findAll();
        }

        model.addAttribute("listCoSo", danhSach);
        model.addAttribute("coSo", new CoSo());
        return "Admin/CoSo/QuanLyCoSo";
    }

    //    @GetMapping("/phan-trang")
//    public String hienThiPhanTrang(Model model,
//                                   @RequestParam(value = "pageNo", defaultValue = "0") int pageNo) {
//        int pageSize = 3; // số bản ghi mỗi trang
//        Page<CoSo> page = coSoService.getAllCoSo(pageNo, pageSize);
//        List<CoSo> listCoSo = page.getContent();
//
//        model.addAttribute("listCoSo", listCoSo);
//        model.addAttribute("currentPage", pageNo);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("totalItems", page.getTotalElements());
//
//        model.addAttribute("coSo", new CoSo()); // form thêm mới nếu có
//
//        return "QuanLyCoSo"; // tên file HTML
//    }
@GetMapping("/export-excel")
public ResponseEntity<byte[]> exportExcel() {
    try (Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("DanhSachCoSo");

        // Tạo style cho header (bold)
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        // Tạo header
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Mã Cơ Sở", "Tên Cơ Sở", "Địa Chỉ", "Số Điện Thoại", "Email",
                "Ngày Thành Lập", "Ngày Tạo", "Ngày Update", "Trạng Thái"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        List<CoSo> listCoSo = coSoRepository.findAll();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        int rowNum = 1;
        for (CoSo cs : listCoSo) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(cs.getId());
            row.createCell(1).setCellValue(cs.getMaCoSo());
            row.createCell(2).setCellValue(cs.getTenCoSo());
            row.createCell(3).setCellValue(cs.getDiaChi());
            row.createCell(4).setCellValue(cs.getSoDienThoai());
            row.createCell(5).setCellValue(cs.getEmail());
            row.createCell(6).setCellValue(cs.getNgayThanhLap() != null ? cs.getNgayThanhLap().format(dateFormatter) : "");
            row.createCell(7).setCellValue(cs.getNgayTao() != null ? cs.getNgayTao().format(dateFormatter) : "");
            row.createCell(8).setCellValue(cs.getNgayUpdate() != null ? cs.getNgayUpdate().format(dateFormatter) : "");
            row.createCell(9).setCellValue(cs.getTrangThai() != null && cs.getTrangThai() ? "Hoạt động" : "Ngừng hoạt động");
        }

        // Auto-size cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        byte[] bytes = out.toByteArray();

        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CoSo.xlsx");

        return ResponseEntity.ok()
                .headers(headersResponse)
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bytes);

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).build();
    }
}
    @PostMapping("/co-so/import")
    public String importExcel(@RequestParam("file") MultipartFile file, RedirectAttributes ra) {
        try {
            List<CoSo> list = ExcelHelperCoSo.readExcel(file.getInputStream());
            coSoRepository.saveAll(list);
            ra.addFlashAttribute("message", "Import thành công " + list.size() + " cơ sở.");
        } catch (Exception e) {
            ra.addFlashAttribute("message", "Lỗi khi import: " + e.getMessage());
        }
        return "redirect:/hien-thi-co-so";
    }


}
