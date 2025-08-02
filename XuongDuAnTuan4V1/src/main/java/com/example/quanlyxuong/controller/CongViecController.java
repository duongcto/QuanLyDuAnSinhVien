package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.entity.CongViec;
import com.example.quanlyxuong.entity.DanhSachCongViec;
import com.example.quanlyxuong.entity.GiaiDoan;
import com.example.quanlyxuong.repository.CongViecRepository;
import com.example.quanlyxuong.repository.DanhSachCongViecRepository;
import com.example.quanlyxuong.repository.GiaiDoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class CongViecController {
    @Autowired
    private CongViecRepository congViecRepository;

    @Autowired
    private GiaiDoanRepository giaiDoanRepository;

    @Autowired
    private DanhSachCongViecRepository danhSachCongViecRepository;

    @PostMapping("/add-cong-viec")
    public String addCongViec(@RequestParam("tenDanhSachCongViec") String tenDanhSachCongViec,
                              @RequestParam("idDuAn") Integer idDuAn) {
        CongViec cv = new CongViec();

        cv.setTenCongViec(tenDanhSachCongViec);
        cv.setNgayTao(Instant.now());
        cv.setNgayUpdate(LocalDate.now());
        cv.setDoUuTien("Thấp");
        cv.setTrangThai(false); // Mặc định là chưa hoàn thành

        congViecRepository.save(cv);

        // Sau khi thêm xong, quay lại trang hiển thị dự án
        return "redirect:/hien-thi-giai-doan/" + idDuAn;
    }

    @GetMapping("/deleteCongViec/{id}")
    public String deleteGiaiDoan(@PathVariable("id") Integer idCongViec,
                                 @RequestParam("idDuAn") Integer idDuAn) {
        Optional<CongViec> giaiDoanOptional = congViecRepository.findById(idCongViec);
        if (giaiDoanOptional.isPresent()) {
            congViecRepository.deleteById(idCongViec);
            return "redirect:/hien-thi-giai-doan/" + idDuAn;
        } else {
            return "redirect:/error";
        }
    }
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer idCongViec,
                         @RequestParam("idDuAn") Integer idDuAn,
                         Model model) {
        CongViec congViec = congViecRepository.findById(idCongViec).orElse(null);
        if (congViec == null) {
            return "redirect:/error";
        }

        List<CongViec> dsCongViec = congViecRepository.findCongViecChuaPhanCong();
        List<GiaiDoan> dsGiaiDoan = giaiDoanRepository.findByDuAn_Id(idDuAn);
        Map<Integer, List<CongViec>> dsCongViecTheoDuAn = new HashMap<>();
        for (GiaiDoan gd : dsGiaiDoan) {
            List<CongViec> congViecList = congViecRepository.findByGiaiDoanId(gd.getId());
            dsCongViecTheoDuAn.put(gd.getId(), congViecList);
        }

        model.addAttribute("dsCongViecTheoDuAn", dsCongViecTheoDuAn);
        model.addAttribute("congViecChiTiet", congViec);
        model.addAttribute("dsCongViec", dsCongViec);
        model.addAttribute("dsGiaiDoan", dsGiaiDoan);
        model.addAttribute("idDuAn", idDuAn);
        model.addAttribute("giaoViec", new GiaiDoan());
        model.addAttribute("congViec", new CongViec());
        model.addAttribute("openModal", true);

        return "QuanLy/DanhSachGiaiDoan";
    }
    @GetMapping("/detailThanhVien/{id}")
    public String detailThanhVien(@PathVariable("id") Integer idCongViec,
                                  @RequestParam("idDuAn") Integer idDuAn,
                                  Model model) {
        // Lấy công việc chi tiết
        CongViec congViec = congViecRepository.findById(idCongViec).orElse(null);
        if (congViec == null) {
            return "redirect:/error";
        }

        // Lấy danh sách giai đoạn của dự án
        List<GiaiDoan> dsGiaiDoan = giaiDoanRepository.findByDuAn_Id(idDuAn);
        model.addAttribute("dsGiaiDoanThanhVien", dsGiaiDoan); // ✅ THÊM vào

        // Tạo map chứa danh sách công việc theo từng giai đoạn
        Map<Integer, List<CongViec>> dsCongViecTheoDuAnThanhVien = new HashMap<>();
        for (GiaiDoan gd : dsGiaiDoan) {
            List<CongViec> congViecList = congViecRepository.findByGiaiDoanId(gd.getId());
            dsCongViecTheoDuAnThanhVien.put(gd.getId(), congViecList);
        }
        model.addAttribute("dsCongViecTheoDuAnThanhVien", dsCongViecTheoDuAnThanhVien); // ✅ THÊM vào

        // Các dữ liệu khác
        model.addAttribute("congViecChiTiet", congViec);
        model.addAttribute("idDuAn", idDuAn);
        model.addAttribute("openModal", true);

        return "ThanhVien/DanhSachGiaiDoanThanhVien";
    }


    @PostMapping("/update-cong-viec-do-uu-tien")
    public String updateDoUuTien(@RequestParam("id") Integer idCongViec,
                                 @RequestParam("doUuTien") String doUuTien,
                                 @RequestParam("idDuAn") Integer idDuAn,
                                 Model model) {
        CongViec congViec = congViecRepository.findById(idCongViec).orElse(null);
        if (congViec != null) {
            congViec.setDoUuTien(doUuTien);
            congViecRepository.save(congViec);
        }

        model.addAttribute("idDuAn", idDuAn);
        model.addAttribute("openModal", true);
        model.addAttribute("congViecChiTiet", congViec);

        // ✅ Thêm lại danh sách giai đoạn
        List<GiaiDoan> dsGiaiDoan = giaiDoanRepository.findByDuAn_Id(idDuAn);
        model.addAttribute("dsGiaiDoan", dsGiaiDoan);

        // ✅ Thêm lại map công việc theo từng giai đoạn
        Map<Integer, List<CongViec>> dsCongViecTheoDuAn = new HashMap<>();
        for (GiaiDoan gd : dsGiaiDoan) {
            List<CongViec> congViecList = congViecRepository.findByGiaiDoanId(gd.getId());
            dsCongViecTheoDuAn.put(gd.getId(), congViecList);
        }
        model.addAttribute("dsCongViecTheoDuAn", dsCongViecTheoDuAn);

        // ✅ Nếu cần danh sách công việc chưa phân công (nếu view cần)
        List<CongViec> dsCongViec = congViecRepository.findCongViecChuaPhanCong();
        model.addAttribute("dsCongViec", dsCongViec);

        return "QuanLy/DanhSachGiaiDoan";
    }

    @PostMapping("/update-mo-ta")
    public String updateMoTa(@RequestParam("id") Integer idCongViec,
                             @RequestParam("moTa") String moTa,
                             @RequestParam("idDuAn") Integer idDuAn,
                             Model model) {
        CongViec congViec = congViecRepository.findById(idCongViec).orElse(null);
        if (congViec != null) {
            congViec.setMoTa(moTa);
            congViecRepository.save(congViec);
        }

        model.addAttribute("idDuAn", idDuAn);
        model.addAttribute("congViecChiTiet", congViec);
        model.addAttribute("openModal", true);

        // ✅ Thêm lại danh sách giai đoạn
        List<GiaiDoan> dsGiaiDoan = giaiDoanRepository.findByDuAn_Id(idDuAn);
        model.addAttribute("dsGiaiDoan", dsGiaiDoan);

        // ✅ Thêm lại map công việc theo từng giai đoạn
        Map<Integer, List<CongViec>> dsCongViecTheoDuAn = new HashMap<>();
        for (GiaiDoan gd : dsGiaiDoan) {
            List<CongViec> congViecList = congViecRepository.findByGiaiDoanId(gd.getId());
            dsCongViecTheoDuAn.put(gd.getId(), congViecList);
        }
        model.addAttribute("dsCongViecTheoDuAn", dsCongViecTheoDuAn);

        // ✅ Nếu cần danh sách công việc chưa phân công (nếu view cần)
        List<CongViec> dsCongViec = congViecRepository.findCongViecChuaPhanCong();
        model.addAttribute("dsCongViec", dsCongViec);

        return "QuanLy/DanhSachGiaiDoan";
    }
    @PostMapping("/update-KieuCv")
    public String updateKieuCv(@RequestParam("id") Integer idCongViec,
                             @RequestParam("KieuCv") String KieuCv,
                             @RequestParam("idDuAn") Integer idDuAn,
                             Model model) {
        CongViec congViec = congViecRepository.findById(idCongViec).orElse(null);
        if (congViec != null) {
            congViec.setKieuCv(KieuCv);
            congViecRepository.save(congViec);
        }

        model.addAttribute("idDuAn", idDuAn);
        model.addAttribute("congViecChiTiet", congViec);
        model.addAttribute("openModal", true);

        // ✅ Thêm lại danh sách giai đoạn
        List<GiaiDoan> dsGiaiDoan = giaiDoanRepository.findByDuAn_Id(idDuAn);
        model.addAttribute("dsGiaiDoan", dsGiaiDoan);

        // ✅ Thêm lại map công việc theo từng giai đoạn
        Map<Integer, List<CongViec>> dsCongViecTheoDuAn = new HashMap<>();
        for (GiaiDoan gd : dsGiaiDoan) {
            List<CongViec> congViecList = congViecRepository.findByGiaiDoanId(gd.getId());
            dsCongViecTheoDuAn.put(gd.getId(), congViecList);
        }
        model.addAttribute("dsCongViecTheoDuAn", dsCongViecTheoDuAn);

        // ✅ Nếu cần danh sách công việc chưa phân công (nếu view cần)
        List<CongViec> dsCongViec = congViecRepository.findCongViecChuaPhanCong();
        model.addAttribute("dsCongViec", dsCongViec);

        return "QuanLy/DanhSachGiaiDoan";
    }
    @PostMapping("/update-nhan")
    public String updateNhan(@RequestParam("id") Integer idCongViec,
                             @RequestParam("nhanDan") String nhan,
                             @RequestParam("idDuAn") Integer idDuAn,
                             Model model) {
        CongViec congViec = congViecRepository.findById(idCongViec).orElse(null);
        if (congViec != null) {
            congViec.setNhanDan(nhan);
            congViecRepository.save(congViec);
        }

        model.addAttribute("idDuAn", idDuAn);
        model.addAttribute("congViecChiTiet", congViec);
        model.addAttribute("openModal", true);

        // ✅ Thêm lại danh sách giai đoạn
        List<GiaiDoan> dsGiaiDoan = giaiDoanRepository.findByDuAn_Id(idDuAn);
        model.addAttribute("dsGiaiDoan", dsGiaiDoan);

        // ✅ Thêm lại map công việc theo từng giai đoạn
        Map<Integer, List<CongViec>> dsCongViecTheoDuAn = new HashMap<>();
        for (GiaiDoan gd : dsGiaiDoan) {
            List<CongViec> congViecList = congViecRepository.findByGiaiDoanId(gd.getId());
            dsCongViecTheoDuAn.put(gd.getId(), congViecList);
        }
        model.addAttribute("dsCongViecTheoDuAn", dsCongViecTheoDuAn);

        // ✅ Nếu cần danh sách công việc chưa phân công (nếu view cần)
        List<CongViec> dsCongViec = congViecRepository.findCongViecChuaPhanCong();
        model.addAttribute("dsCongViec", dsCongViec);

        return "QuanLy/DanhSachGiaiDoan";
    }
    @PostMapping("/update-ngay-han")
    public String updateNgayHan(@RequestParam("id") Integer idCongViec,
                                @RequestParam("ngayHetHan") LocalDate ngayHetHan,
                                @RequestParam("idDuAn") Integer idDuAn,
                                Model model) {
        CongViec congViec = congViecRepository.findById(idCongViec).orElse(null);
        if (congViec != null) {
            congViec.setNgayHetHan(ngayHetHan);
            congViecRepository.save(congViec);
        }

        model.addAttribute("idDuAn", idDuAn);
        model.addAttribute("congViecChiTiet", congViec);
        model.addAttribute("openModal", true);

        // ✅ Thêm lại danh sách giai đoạn
        List<GiaiDoan> dsGiaiDoan = giaiDoanRepository.findByDuAn_Id(idDuAn);
        model.addAttribute("dsGiaiDoan", dsGiaiDoan);

        // ✅ Thêm lại map công việc theo từng giai đoạn
        Map<Integer, List<CongViec>> dsCongViecTheoDuAn = new HashMap<>();
        for (GiaiDoan gd : dsGiaiDoan) {
            List<CongViec> congViecList = congViecRepository.findByGiaiDoanId(gd.getId());
            dsCongViecTheoDuAn.put(gd.getId(), congViecList);
        }
        model.addAttribute("dsCongViecTheoDuAn", dsCongViecTheoDuAn);

        // ✅ Nếu cần danh sách công việc chưa phân công (nếu view cần)
        List<CongViec> dsCongViec = congViecRepository.findCongViecChuaPhanCong();
        model.addAttribute("dsCongViec", dsCongViec);

        return "QuanLy/DanhSachGiaiDoan";
    }
    @PostMapping("/update-anh")
    public String updateAnh(@RequestParam("id") Integer idCongViec,
                            @RequestParam("taiAnh") MultipartFile file,
                            @RequestParam("idDuAn") Integer idDuAn,
                            Model model) throws IOException {

        CongViec congViec = congViecRepository.findById(idCongViec).orElse(null);
        if (congViec != null && !file.isEmpty()) {
            // Lưu vào static/uploads/
            String fileName = file.getOriginalFilename();
            String uploadDir = new ClassPathResource("static/uploads/").getFile().getAbsolutePath();
            Path path = Paths.get(uploadDir, fileName);
            Files.write(path, file.getBytes());

            // Lưu tên file vào DB
            congViec.setTaiAnh(fileName);
            congViecRepository.save(congViec);
        }

        model.addAttribute("idDuAn", idDuAn);
        model.addAttribute("congViecChiTiet", congViec);
        model.addAttribute("openModal", true);

        // ✅ Thêm lại danh sách giai đoạn
        List<GiaiDoan> dsGiaiDoan = giaiDoanRepository.findByDuAn_Id(idDuAn);
        model.addAttribute("dsGiaiDoan", dsGiaiDoan);

        // ✅ Thêm lại map công việc theo từng giai đoạn
        Map<Integer, List<CongViec>> dsCongViecTheoDuAn = new HashMap<>();
        for (GiaiDoan gd : dsGiaiDoan) {
            List<CongViec> congViecList = congViecRepository.findByGiaiDoanId(gd.getId());
            dsCongViecTheoDuAn.put(gd.getId(), congViecList);
        }
        model.addAttribute("dsCongViecTheoDuAn", dsCongViecTheoDuAn);

        // ✅ Nếu cần danh sách công việc chưa phân công (nếu view cần)
        List<CongViec> dsCongViec = congViecRepository.findCongViecChuaPhanCong();
        model.addAttribute("dsCongViec", dsCongViec);

        return "QuanLy/DanhSachGiaiDoan";
    }

    @PostMapping("/update-link")
    public String updateLink(@RequestParam("id") Integer idCongViec,
                             @RequestParam("linkDinhKem") String linkDinhKem,
                             @RequestParam("idDuAn") Integer idDuAn,
                             Model model) {
        CongViec congViec = congViecRepository.findById(idCongViec).orElse(null);
        if (congViec != null) {
            congViec.setLinkDinhKem(linkDinhKem);
            congViecRepository.save(congViec);
        }

        model.addAttribute("idDuAn", idDuAn);
        model.addAttribute("congViecChiTiet", congViec);
        model.addAttribute("openModal", true);

        // ✅ Thêm lại danh sách giai đoạn
        List<GiaiDoan> dsGiaiDoan = giaiDoanRepository.findByDuAn_Id(idDuAn);
        model.addAttribute("dsGiaiDoan", dsGiaiDoan);

        // ✅ Thêm lại map công việc theo từng giai đoạn
        Map<Integer, List<CongViec>> dsCongViecTheoDuAn = new HashMap<>();
        for (GiaiDoan gd : dsGiaiDoan) {
            List<CongViec> congViecList = congViecRepository.findByGiaiDoanId(gd.getId());
            dsCongViecTheoDuAn.put(gd.getId(), congViecList);
        }
        model.addAttribute("dsCongViecTheoDuAn", dsCongViecTheoDuAn);

        // ✅ Nếu cần danh sách công việc chưa phân công (nếu view cần)
        List<CongViec> dsCongViec = congViecRepository.findCongViecChuaPhanCong();
        model.addAttribute("dsCongViec", dsCongViec);

        return "QuanLy/DanhSachGiaiDoan";
    }


    @PostMapping("/cong-viec/gan-giai-doan")
    public ResponseEntity<String> ganGiaiDoan(
            @RequestParam Integer idCongViec,
            @RequestParam Integer idGiaiDoan) {

        // 1. Tìm công việc cần cập nhật
        CongViec congViec = congViecRepository.findById(idCongViec)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy công việc"));

        // 2. Tìm danh sách công việc thuộc giai đoạn này
        DanhSachCongViec danhSach = danhSachCongViecRepository
                .findFirstByIdGiaiDoan_Id(idGiaiDoan) // Bạn có thể đổi sang findByIdGiaiDoan nếu muốn nhiều
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh sách công việc cho giai đoạn"));

        // 3. Gán lại danh sách công việc cho công việc
        congViec.setIdDanhSachCongViec(danhSach);
        congViecRepository.save(congViec);

        return ResponseEntity.ok("Gán công việc vào giai đoạn thành công!");
    }

    @GetMapping("/cong-viec-theo-danh-sach/{idDanhSach}")
    public String hienThiCongViecTheoDanhSach(@PathVariable("idDanhSach") Integer idDanhSach, Model model) {
        List<CongViec> dsCongViecTheoDuAn = congViecRepository.findByIdDanhSachCongViec_Id(idDanhSach);
        model.addAttribute("dsCongViecTheoDuAn", dsCongViecTheoDuAn);
        model.addAttribute("idDanhSach", idDanhSach);
        return "QuanLy/DanhSachGiaiDoan"; // Thay bằng tên file HTML bạn dùng để hiển thị
    }


}