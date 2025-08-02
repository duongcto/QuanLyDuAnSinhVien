package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.VaiTro;
import com.example.quanlyxuong.repository.NguoiDungRepository;
import com.example.quanlyxuong.repository.VaiTroRepository;
import com.example.quanlyxuong.service.NhanVienService;
import com.example.quanlyxuong.service.VaiTroServide;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Controller
public class NhanVienController {

    @Autowired
    private VaiTroServide vaiTroService;
    @Autowired
    private VaiTroRepository vaiTroRepository;
    @Autowired
    private NguoiDungRepository nguoiDungRepo;
    @Autowired
    private NhanVienService nhanVienService;
    private static final Logger log = LoggerFactory.getLogger(NhanVienController.class);

    @GetMapping("/danhSachNhanVien")
    public String danhSachNhanVien(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "5") int size){
        Page<NguoiDung> listND = nhanVienService.getAllNguoiDungs("Quản Lý", "Thành viên", page, size);
        model.addAttribute("listNguoiDung", listND);
        model.addAttribute("trangDau", page);
        model.addAttribute("tongTrang", listND.getTotalPages());
        model.addAttribute("listVaiTro",vaiTroService.getQuanLyAndThanhVien());
        model.addAttribute("newND",new NguoiDung());
        return "Admin/NhanVienList";
    }

    @PostMapping("/saveNhanVien")
    public String saveNhanVien(Model model, @Valid @ModelAttribute("newND") NguoiDung nguoiDung, Errors errors,@RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "5") int size,
                               RedirectAttributes redirectAttributes){
        model.addAttribute("listVaiTro",vaiTroService.getAllVaiTro());
        if(errors.hasErrors()){
            Page<NguoiDung> listND = nhanVienService.getAllNguoiDungs("Quản Lý", "Thành viên", page, size);
            model.addAttribute("listNguoiDung", listND);
            model.addAttribute("trangDau", page);
            model.addAttribute("tongTrang", listND.getTotalPages());
        model.addAttribute("error","Thêm nhân viên thất bại!");
            model.addAttribute("listNguoiDung", nhanVienService.getAllNguoiDungs("Quản Lý","Thành viên", page, size));
            return "Admin/NhanVienList";
        }
        if(nhanVienService.checkTrung(nguoiDung.getMaNguoiDung())){
            Page<NguoiDung> listND = nhanVienService.getAllNguoiDungs("Quản Lý", "Thành viên", page, size);
            model.addAttribute("listNguoiDung", listND);
            model.addAttribute("trangDau", page);
            model.addAttribute("tongTrang", listND.getTotalPages());
            model.addAttribute("error","Nhân viên đã tồn tại");
            model.addAttribute("listNguoiDung", nhanVienService.getAllNguoiDungs("Quản Lý","Thành viên", page, size));
            return "Admin/NhanVienList";
        }
        nhanVienService.saveNhanVien(nguoiDung);
        model.addAttribute("listNguoiDung", nhanVienService.getAllNguoiDungs("Quản Lý","Thành viên", page, size));
        redirectAttributes.addFlashAttribute("message","Thêm nhân viên thành công!");
        return "redirect:/danhSachNhanVien";
    }
    @GetMapping("/deleteNV/{id}")
    public String deleteNV(@PathVariable("id") int id, RedirectAttributes redirectAttributes){
        NguoiDung getById = nhanVienService.getById(id);
        nguoiDungRepo.deleteById(id);
        redirectAttributes.addFlashAttribute("message","Xóa nhân viên thành công");
        return "redirect:/danhSachNhanVien";
    }

    @GetMapping("/locVaiTro")
    public String locVaiTro(@RequestParam("idVaiTro") VaiTro idVaiTro, Model model,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "size", defaultValue = "5") int size){
        Page<NguoiDung> locVaiTro = nhanVienService.locVaiTro(idVaiTro, page, size);
        model.addAttribute("listNguoiDung", locVaiTro);
        model.addAttribute("trangDau", page);
        model.addAttribute("tongTrang", locVaiTro.getTotalPages());
        model.addAttribute("listVaiTro",vaiTroService.getQuanLyAndThanhVien());
        model.addAttribute("newND",new NguoiDung());
        return "Admin/NhanVienList";
    }
    @GetMapping("/locTrangThai")
    public String locTrangThai(@RequestParam("trangThai") String trangThai, Model model,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "size", defaultValue = "5") int size){
        Page<NguoiDung> locTrangThai = nhanVienService.locTrangThai(trangThai, page, size);
        model.addAttribute("listNguoiDung", locTrangThai);
        model.addAttribute("trangDau", page);
        model.addAttribute("tongTrang", locTrangThai.getTotalPages());
        model.addAttribute("listVaiTro",vaiTroService.getQuanLyAndThanhVien());
        model.addAttribute("newND",new NguoiDung());
        return "Admin/NhanVienList";
    }
    @GetMapping("/timKiem")
    public String timKiem(@RequestParam("tuKhoa") String tuKhoa, Model model,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "5") int size){
        Page<NguoiDung> timKiem = nhanVienService.timKiem(tuKhoa, page, size);
        model.addAttribute("listNguoiDung", timKiem);
        model.addAttribute("trangDau", page);
        model.addAttribute("tongTrang", timKiem.getTotalPages());
        model.addAttribute("listVaiTro",vaiTroService.getQuanLyAndThanhVien());
        model.addAttribute("newND",new NguoiDung());
        return "Admin/NhanVienList";
    }

    @PostMapping("/nhanVien/thayDoiTrangThai")
    public String toggleEmployeeStatus(@RequestParam("id") Integer id,
                                       @RequestParam("thayDoiTrangThai") String thayDoiTrangThai,
                                       RedirectAttributes redirectAttributes) {
        try {
            String newStatus = thayDoiTrangThai.equals("Đang học") ? "Dừng học" : "Đang học";
            nhanVienService.updateTrangThai(id, newStatus);
            redirectAttributes.addFlashAttribute("message", "Đã cập nhật trạng thái" + " thành " + newStatus);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/danhSachNhanVien";
    }

    @GetMapping("/export-headers-excel")
    public void exportHeadersToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        // Sửa lỗi ở dòng này
        response.setHeader("Content-Disposition", "attachment; filename=tieu_de_nhan_vien.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("TieuDe");

        String[] headers = {
                "Mã nhân viên", "Tên nhân viên", "Email FE", "Email FPT",
                "Số điện thoại", "Vai trò", "Trạng thái"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @PostMapping("/import-staff")
    public ResponseEntity<Map<String, Object>> importStaff(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            int successCount = 0;
            int errorCount = 0;

            // Bỏ qua dòng header (dòng 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    NguoiDung nguoidung = new NguoiDung();
                    nguoidung.setMaNguoiDung(getStringValue(row.getCell(0)));
                    nguoidung.setHoTen(getStringValue(row.getCell(1)));
                    nguoidung.setEmailFe(getStringValue(row.getCell(2)));
                    nguoidung.setEmailPt(getStringValue(row.getCell(3)));
                    nguoidung.setSoDienThoai(getStringValue(row.getCell(4)));
                    String tenVaiTro = getStringValue(row.getCell(5));
                    if (tenVaiTro == null || tenVaiTro.isEmpty()) {
                        throw new IllegalArgumentException("Vai trò không được để trống.");
                    }
                    tenVaiTro = tenVaiTro.trim(); // Chuẩn hóa tên vai trò

                    Optional<VaiTro> optionalVaiTro = vaiTroRepository.findByTenVaiTro(tenVaiTro);

                    if (optionalVaiTro.isPresent()) {
                        nguoidung.setIdVaiTro(optionalVaiTro.get()); // Gán đối tượng VaiTro
                    } else {
                        throw new IllegalArgumentException("Vai trò '" + tenVaiTro + "' không tồn tại trong hệ thống.");
                    }
                    nguoidung.setTrangThai(getStringValue(row.getCell(6)));

                    nguoiDungRepo.save(nguoidung);
                    successCount++;
                } catch (Exception e) {
                    log.error("Error importing row {}: {}", i + 1, e.getMessage());
                    errorCount++;
                }
            }

            response.put("success", true);
            response.put("message", "Import thành công: " + successCount + " nhân viên, Lỗi: " + errorCount);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("Error processing Excel file: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "Lỗi khi xử lý file Excel");
            return ResponseEntity.badRequest().body(response);
        }
    }

    private String getStringValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return "";
        }
    }
}
