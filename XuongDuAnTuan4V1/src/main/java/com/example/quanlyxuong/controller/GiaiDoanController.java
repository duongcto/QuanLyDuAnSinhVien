package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.entity.BinhChonGiaiDoan;
import com.example.quanlyxuong.entity.CongViec;
import com.example.quanlyxuong.entity.DanhSachCongViec;
import com.example.quanlyxuong.entity.DuAn;
import com.example.quanlyxuong.entity.GiaiDoan;
import com.example.quanlyxuong.repository.BinhChonGiaiDoanRepository;
import com.example.quanlyxuong.repository.CongViecRepository;
import com.example.quanlyxuong.repository.DanhSachCongViecRepository;
import com.example.quanlyxuong.repository.DuAnRepository;
import com.example.quanlyxuong.repository.GiaiDoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static java.time.LocalDate.now;

@Controller
public class GiaiDoanController {
    @Autowired
    private GiaiDoanRepository giaiDoanRepository;

    @Autowired
    private CongViecRepository congViecRepository;

    @Autowired
    private DuAnRepository duAnRepository;

    @Autowired
    private DanhSachCongViecRepository danhSachCongViecRepository;

    @Autowired
    private BinhChonGiaiDoanRepository binhChonGiaiDoanRepository;

    @GetMapping("/hien-thi-giai-doan/{id}")
    public String hienThiGiaiDoanTheoDuAn(@PathVariable("id") Integer idDuAn, Model model) {
        List<GiaiDoan> dsGiaiDoan = giaiDoanRepository.findByDuAn_Id(idDuAn);
        model.addAttribute("dsGiaiDoan", dsGiaiDoan);

        Map<Integer, List<CongViec>> dsCongViecTheoDuAn = new HashMap<>();
        for (GiaiDoan gd : dsGiaiDoan) {
            List<CongViec> congViecList = congViecRepository.findByGiaiDoanId(gd.getId());
            dsCongViecTheoDuAn.put(gd.getId(), congViecList);
        }
        model.addAttribute("dsCongViecTheoDuAn", dsCongViecTheoDuAn);

        List<CongViec> dsCongViec = congViecRepository.findCongViecChuaPhanCong();
        model.addAttribute("dsCongViec", dsCongViec);

        model.addAttribute("idDuAn", idDuAn);

        return "QuanLy/DanhSachGiaiDoan";
    }

    @GetMapping("/hien-thi-giai-doan-thanh-vien/{id}")
    public String hienThiGiaiDoanThanhVienTheoDuAn(@PathVariable("id") Integer idDuAn, Model model) {
        List<GiaiDoan> dsGiaiDoan = giaiDoanRepository.findByDuAn_Id(idDuAn);
        model.addAttribute("dsGiaiDoanThanhVien", dsGiaiDoan);

        Map<Integer, List<CongViec>> dsCongViecTheoDuAnThanhVien = new HashMap<>();
        for (GiaiDoan gd : dsGiaiDoan) {
            List<CongViec> congViecList = congViecRepository.findByGiaiDoanId(gd.getId());
            dsCongViecTheoDuAnThanhVien.put(gd.getId(), congViecList);
            for (CongViec cv : congViecList) {
                System.out.println("Tên công việc: " + cv.getTenCongViec());
            }
        }

        model.addAttribute("dsCongViecTheoDuAnThanhVien", dsCongViecTheoDuAnThanhVien);
        model.addAttribute("idDuAn", idDuAn);

        return "ThanhVien/DanhSachGiaiDoanThanhVien";
    }


    @GetMapping("/deleteGiaiDoan/{id}")
    public String deleteGiaiDoan(@PathVariable("id") Integer idGiaiDoan) {
        Optional<GiaiDoan> giaiDoanOptional = giaiDoanRepository.findById(idGiaiDoan);
        if (giaiDoanOptional.isPresent()) {
            Integer idDuAn = giaiDoanOptional.get().getIdDuAn().getId();
            giaiDoanRepository.deleteById(idGiaiDoan);
            return "redirect:/hien-thi-giai-doan/" + idDuAn;
        } else {
            return "redirect:/error";
        }
    }

    @PostMapping("/add-giai-doan")
    public String luuGiaiDoan(@RequestParam("tenGiaoViec") String tenGiaiDoan,
                              @RequestParam("idDuAn") Integer idDuAn) {

        Optional<DuAn> duAnOptional = duAnRepository.findById(idDuAn);
        if (duAnOptional.isEmpty()) {
            return "redirect:/error";
        }

        List<GiaiDoan> giaiDoanList = giaiDoanRepository.findByDuAn_Id(idDuAn);

        // Đảm bảo ngày bắt đầu không trùng
        LocalDate proposedStartDate = now();
        boolean isDuplicate;

        do {
            isDuplicate = false;
            for (GiaiDoan gd : giaiDoanList) {
                if (proposedStartDate.equals(gd.getNgayBatDau())) {
                    proposedStartDate = proposedStartDate.plusDays(1);
                    isDuplicate = true;
                    break;
                }
            }
        } while (isDuplicate);

        // Tạo và lưu GiaiDoan trước để lấy ID
        GiaiDoan giaiDoan = new GiaiDoan();
        giaiDoan.setTenGiaiDoan(tenGiaiDoan);
        giaiDoan.setNgayBatDau(proposedStartDate);
        giaiDoan.setNgayKetThuc(proposedStartDate.plusDays(7));
        giaiDoan.setNgayUpdate(Instant.now());
        giaiDoan.setTrangThai(true);
        giaiDoan.setIdDuAn(duAnOptional.get());

        giaiDoan = giaiDoanRepository.save(giaiDoan); // <-- ID được sinh tại đây

        // Tạo DanhSachCongViec và gán GiaiDoan
        DanhSachCongViec danhSachCongViec = new DanhSachCongViec();
        danhSachCongViec.setIdGiaiDoan(giaiDoan); // gán object giai đoạn
        danhSachCongViec.setDuAn(duAnOptional.get());
        danhSachCongViec.setTenDanhSachCongViec("Danh sách - " + tenGiaiDoan);
        danhSachCongViec.setNgayTao(proposedStartDate);
        danhSachCongViec.setNgayUpdate(proposedStartDate.plusDays(7));
        danhSachCongViec.setTrangThai(true);

        danhSachCongViecRepository.save(danhSachCongViec);

        return "redirect:/hien-thi-giai-doan/" + idDuAn;
    }

    @GetMapping("/search-giai-doan")
    public String searchGiaiDoanTheoTenVaNgay(@RequestParam("idDuAn") Integer idDuAn,
                                              @RequestParam(name = "tenGiaiDoan", required = false) String tenGiaiDoan,
                                              @RequestParam(name = "ngayBatDau", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayBatDau,
                                              Model model) {

        List<GiaiDoan> dsGiaiDoan = giaiDoanRepository.searchGiaiDoan(idDuAn, tenGiaiDoan, ngayBatDau);
        model.addAttribute("dsGiaiDoanThanhVien", dsGiaiDoan);

        Map<Integer, List<CongViec>> dsCongViecTheoDuAn = new HashMap<>();
        for (GiaiDoan gd : dsGiaiDoan) {
            List<CongViec> congViecList = congViecRepository.findByGiaiDoanId(gd.getId());
            dsCongViecTheoDuAn.put(gd.getId(), congViecList);
        }
        model.addAttribute("dsCongViecTheoDuAnThanhVien", dsCongViecTheoDuAn);

        // ✅ THÊM DÒNG NÀY ĐỂ ĐẢM BẢO BIẾN idDuAn CÓ TRONG FRAGMENT
        model.addAttribute("idDuAn", idDuAn);

        // Chỉ trả về fragment, không phải toàn trang
        return "ThanhVien/DanhSachGiaiDoanThanhVien :: stageList";
    }

    @PostMapping("/add-binh-chon")
    public String luuBinhChon(@RequestParam("idGiaiDoan") Integer idGiaiDoan,
                              @RequestParam("ngayBatDau")
                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDate ngayBatDau,
                              @RequestParam("ngayKetThuc")
                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDate ngayKetThuc,
                              @RequestParam("idDuAn") Integer idDuAn) {

        BinhChonGiaiDoan binhChon = new BinhChonGiaiDoan();
        GiaiDoan giaiDoan = new GiaiDoan();
        GiaiDoan giaiDnDuocChon = giaiDoanRepository.findById(idGiaiDoan)
                .orElseThrow(() -> new IllegalArgumentException("Invalid GiaoViec Id:" + idGiaiDoan));
        binhChon.setIdGiaiDoan(giaiDnDuocChon);
        binhChon.setNgayTao(ngayBatDau);
        binhChon.setNgayKetThuc(ngayKetThuc);
        LocalDate today = LocalDate.now();
        binhChon.setNgaySua(today);

        // Lấy đối tượng dự án
        Optional<DuAn> duAnOptional = duAnRepository.findById(idDuAn);
        if (duAnOptional.isEmpty()) {
            return "redirect:/error";
        }

        giaiDoan.setIdDuAn(duAnOptional.get());

        binhChonGiaiDoanRepository.save(binhChon);
        return "redirect:/hien-thi-giai-doan/" + idDuAn;
    }

    @GetMapping("/hien-thi-chi-tiet-binh-chon")
    public String hienThiDanhSach(Model model,
                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "size", defaultValue = "10") int size) {

        // Tạo đối tượng Pageable để truy vấn có phân trang
        Pageable pageable = PageRequest.of(page, size);

        // Lấy dữ liệu từ Repository
        Page<BinhChonGiaiDoan> binhChonPage = binhChonGiaiDoanRepository.findAll(pageable);

        // Đưa đối tượng Page vào model
        model.addAttribute("binhChonPage", binhChonPage);

        // Trả về tên file view
        return "/QuanLy/chiTietBinhChon"; // Ví dụ: "giai-doan/danh-sach"
    }

    @GetMapping("/deleteBinhChon/{id}")
    public String deleteBinhChon(@PathVariable("id") Integer idBinhChon) {
        binhChonGiaiDoanRepository.deleteById(idBinhChon);
        return "redirect:/hien-thi-chi-tiet-binh-chon";
    }
}
