package com.example.quanlyxuong.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;

import com.example.quanlyxuong.entity.ChucVu;
import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.VaiTro;
import com.example.quanlyxuong.repository.ChucVuRepository;
import com.example.quanlyxuong.repository.NguoiDungRepository;
import com.example.quanlyxuong.repository.VaiTroRepository;
import com.example.quanlyxuong.service.ExcelExporterNguoiDung;
import com.example.quanlyxuong.service.ExcelImporterNguoiDung;
import jakarta.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class SinhVienController {


    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;


    @Autowired
    private ChucVuRepository chucvuRepository;
    @GetMapping("/hienThiSinhVien")
    public String hienThi(Model model,@RequestParam(value = "pageNo",required = false,defaultValue = "0") int pageNo) {
        Pageable pageable= PageRequest.of(pageNo, 4);
        Page<NguoiDung> nd = nguoiDungRepository.findAll_page(pageable,"Thành viên");
        model.addAttribute("NguoiDung", new NguoiDung());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", nd.getTotalPages());
        model.addAttribute("nguoiDung", nd.getContent());
        model.addAttribute("listVaiTro", vaiTroRepository.findAll());
        return "hien_thi";
    }
    @PostMapping("addSinhVien")
    public String add(@ModelAttribute NguoiDung nguoiDung, Model model, RedirectAttributes redirectAttributes , @RequestParam(value = "pageNo",required = false,defaultValue = "0") int pageNo) {
        LocalDate ngayTao = LocalDate.now();
        VaiTro vt = vaiTroRepository.findByTenVaiTro1("Thành viên");
        ChucVu cn = chucvuRepository.getChucVuByIdThanhVien();
        nguoiDung.setNgayTao(ngayTao);
        nguoiDung.setIdVaiTro(vt);
        nguoiDung.setTrangThai("Đang làm");
        nguoiDung.setIdChucVu(cn);
        // Giả định nguoiDung là đối tượng đã được khởi tạo
        boolean kiemTra = true; // Biến để theo dõi trạng thái hợp lệ của dữ liệu

        if (nguoiDung.getMaNguoiDung() == null || nguoiDung.getMaNguoiDung().isEmpty()) {
            model.addAttribute("messager1", "Vui lòng điền mã người dùng.");
            kiemTra = false;
        }

        if (nguoiDung.getHoTen() == null || nguoiDung.getHoTen().isEmpty()) {
            model.addAttribute("messager2", "Vui lòng điền họ tên.");
            kiemTra = false;
        }

        if (nguoiDung.getTenDangNhap() == null || nguoiDung.getTenDangNhap().isEmpty()) {
            model.addAttribute("messager3", "Vui lòng điền tên đăng nhập.");
            kiemTra = false;
        }

        if (nguoiDung.getEmailFe() == null || nguoiDung.getEmailFe().isEmpty()) {
            model.addAttribute("messager4", "Vui lòng điền email fe.");
            kiemTra = false;
        } else if (!nguoiDung.getEmailFe().endsWith("@fe.edu.vn")) {
            model.addAttribute("messager4", "Email FE phải có đuôi '@fe.edu.vn'.");
            kiemTra = false;
        }

        if (nguoiDung.getEmailPt() == null || nguoiDung.getEmailPt().isEmpty()) {
            model.addAttribute("messager5", "Vui lòng điền email pt.");
            kiemTra = false;
        } else if (!nguoiDung.getEmailPt().endsWith("@pt.edu.vn")) { // Sửa thành @FT.edu.vn theo yêu cầu
            model.addAttribute("messager5", "Email PT phải có đuôi '@pt.edu.vn'.");
            kiemTra = false;
        }

        if (nguoiDung.getSoDienThoai() == null || nguoiDung.getSoDienThoai().isEmpty()) {
            model.addAttribute("messager6", "Vui lòng điền số điện thoại.");
            kiemTra = false;
        } else if (!nguoiDung.getSoDienThoai().matches("^\\d{10,11}$")) { // Ví dụ: kiểm tra số điện thoại 10-11 chữ số
            model.addAttribute("messager6", "Số điện thoại không hợp lệ (10-11 chữ số).");
            kiemTra = false;
        }

        if (nguoiDung.getMatKhau() == null || nguoiDung.getMatKhau().isEmpty()) {
            model.addAttribute("messager7", "Vui lòng điền mật khẩu.");
            kiemTra = false;
        } else if (nguoiDung.getMatKhau().length() < 6) { // Ví dụ: kiểm tra mật khẩu ít nhất 6 ký tự
            model.addAttribute("messager7", "Mật khẩu phải có ít nhất 6 ký tự.");
            kiemTra = false;
        }

        if (!kiemTra) {
            model.addAttribute("showModal", true);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi khi thêm nhân viên. Vui lòng kiểm tra lại thông tin.");
        }else {
            nguoiDungRepository.save(nguoiDung);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm sinh viên thành công!");
            return "redirect:/hienThiSinhVien";
        }

        Pageable pageable= PageRequest.of(pageNo, 4);
        Page<NguoiDung> nd = nguoiDungRepository.findAll_page(pageable,"Thành viên");
        model.addAttribute("NguoiDung", nguoiDung);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", nd.getTotalPages());
        model.addAttribute("nguoiDung", nd.getContent());

        return "hien_thi";
    }
    @GetMapping("deleteSinhVien/{id}")
    public String delete(@PathVariable("id") int id) {
        NguoiDung nd = nguoiDungRepository.getReferenceById(id);
        nguoiDungRepository.delete(nd);
        return "redirect:/hienThiSinhVien";
    }
    @GetMapping("detailSinhVien/{id}")
    public String detail(@PathVariable("id") int id, Model model,@RequestParam(value = "pageNo",required = false,defaultValue = "0") int pageNo) {
        NguoiDung getnd = nguoiDungRepository.findById(id);
        model.addAttribute("sinhVien", getnd);
        return "hien_thi";
    }

    @RequestMapping(value = "/searchSinhVien", method = {RequestMethod.GET, RequestMethod.POST})
    public String search(Model model,
                         @RequestParam(value = "studentName", required = false) String studentName,
                         @RequestParam(value = "status", required = false) String status,
                         @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo) {

        if (pageNo < 0) pageNo = 0;
        Pageable pageable = PageRequest.of(pageNo, 4);
        Page<NguoiDung> nd;

        boolean hasName = studentName != null && !studentName.trim().isEmpty();
        boolean hasStatus = status != null && (status.equalsIgnoreCase("Đang học") || status.equalsIgnoreCase("Dừng học"));

        if (!hasName && hasStatus) {
            nd = nguoiDungRepository.findByTrangThai(status, pageable);
        } else if (hasName && !hasStatus) {
            nd = nguoiDungRepository.findByHoTenContainingIgnoreCase(studentName, pageable);
        } else if (hasName && hasStatus) {
            nd = nguoiDungRepository.findByHoTenContainingIgnoreCaseAndTrangThai(studentName, status, pageable);
        } else {
            nd = nguoiDungRepository.findAll_page(pageable, "Thành viên");
        }

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", nd.getTotalPages());
        model.addAttribute("nguoiDung", nd.getContent());

        model.addAttribute("NguoiDung", new NguoiDung());
        model.addAttribute("searchName", studentName);
        model.addAttribute("searchStatus", status);
        model.addAttribute("listVaiTro", vaiTroRepository.findAll());

        return "hien_thi";
    }

    @GetMapping("/locVaiTroSinhVien")
    public String locVaiTroSinhVien(@RequestParam("idVaiTro") Integer idVaiTro, Model model,
                                   @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo) {
        if (pageNo < 0) pageNo = 0;
        Pageable pageable = PageRequest.of(pageNo, 4);
        Page<NguoiDung> nd;

        if (idVaiTro != null) {
            VaiTro vaiTro = vaiTroRepository.findById(idVaiTro).orElse(null);
            if (vaiTro != null) {
                nd = nguoiDungRepository.findByIdVaiTro(vaiTro, pageable);
            } else {
                nd = nguoiDungRepository.findAll_page(pageable, "Thành viên");
            }
        } else {
            nd = nguoiDungRepository.findAll_page(pageable, "Thành viên");
        }

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", nd.getTotalPages());
        model.addAttribute("nguoiDung", nd.getContent());
        model.addAttribute("NguoiDung", new NguoiDung());
        model.addAttribute("searchRole", idVaiTro != null ? idVaiTro.toString() : "");
        model.addAttribute("listVaiTro", vaiTroRepository.findAll());

        return "hien_thi";
    }

    @GetMapping("/locTrangThaiSinhVien")
    public String locTrangThaiSinhVien(@RequestParam("trangThai") String trangThai, Model model,
                                      @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo) {
        if (pageNo < 0) pageNo = 0;
        Pageable pageable = PageRequest.of(pageNo, 4);
        Page<NguoiDung> nd;

        if (trangThai != null && !trangThai.trim().isEmpty()) {
            nd = nguoiDungRepository.findByTrangThai(trangThai, pageable);
        } else {
            nd = nguoiDungRepository.findAll_page(pageable, "Thành viên");
        }

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", nd.getTotalPages());
        model.addAttribute("nguoiDung", nd.getContent());
        model.addAttribute("NguoiDung", new NguoiDung());
        model.addAttribute("searchStatus", trangThai);
        model.addAttribute("listVaiTro", vaiTroRepository.findAll());

        return "hien_thi";
    }


    @GetMapping("/view-updateSinhVien/{id}")
    public String view_update(@PathVariable("id") int id,Model model) {
        NguoiDung nd = nguoiDungRepository.findById(id);
        model.addAttribute("sinhVien", nd);
        return "/updatesv";
    }

    @PostMapping("/updateSinhVien/{id}")
    public String update(@PathVariable("id") int id,Model model,@ModelAttribute NguoiDung nguoiDung,RedirectAttributes redirectAttributes) {
        LocalDate ngayUpdate = LocalDate.now();
        NguoiDung nd = nguoiDungRepository.findById(id);
        nd.setId(id);
        nd.setMaNguoiDung(nguoiDung.getMaNguoiDung());
        nd.setHoTen(nguoiDung.getHoTen());
        nd.setTenDangNhap(nguoiDung.getTenDangNhap());
        nd.setEmailPt(nguoiDung.getEmailPt());
        nd.setEmailFe(nguoiDung.getEmailFe());
        nd.setTrangThai(nguoiDung.getTrangThai());
        nd.setSoDienThoai(nguoiDung.getSoDienThoai());
        nd.setMatKhau(nguoiDung.getMatKhau());
        nd.setNgayUpdate(ngayUpdate);
        nguoiDungRepository.save(nd);
        redirectAttributes.addFlashAttribute("successMessage", "Update thành công!");
        return "redirect:/hienThiSinhVien";
    }
    @GetMapping("/thoat")
    public String thoat(HttpServletResponse response) throws IOException {

        return "redirect:/hienThiSinhVien";
    }

    @GetMapping("/export-excelSinhVien")
    public ResponseEntity<InputStreamResource> exportExcel() throws Exception {
        List<NguoiDung> list = nguoiDungRepository.findAll_nd("Thành viên");
        ByteArrayInputStream in = ExcelExporterNguoiDung.exportToExcel(list);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=nguoidung.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @PostMapping("/importSinhVien")
    public String importNguoiDung(@RequestParam("file") MultipartFile file,Model model) {
        try {
            List<NguoiDung> list = ExcelImporterNguoiDung.readFromExcel(file.getInputStream());
            nguoiDungRepository.saveAll(list);
            model.addAttribute("nguoiDung", list);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Import thất bại: " + e.getMessage());
        }
        return "hien_thi_import";
    }



}
