package com.example.quanlyxuong.service.impl;

import com.example.quanlyxuong.dto.DuAnDto;
import com.example.quanlyxuong.entity.ChuyenNganh;
import com.example.quanlyxuong.entity.DuAn;
import com.example.quanlyxuong.repository.*;
import com.example.quanlyxuong.service.DanhSachCongViecService;
import com.example.quanlyxuong.service.DuAnService;
import com.example.quanlyxuong.service.ThanhVienDuAnService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class DuAnServiceImpl implements DuAnService {

    private final DuAnRepository duAnRepository;
    private final BoMonRepository boMonRepository;
    private final CoSoRepository coSoRepository;
    private final ChuyenNganhRepository chuyenNganhRepository;
    private final LoaiDuAnRepository loaiDuAnRepository;
    private final DanhSachCongViecService danhSachCongViecService;
    private final ThanhVienDuAnService thanhVienDuAnService;


    public DuAnServiceImpl(DuAnRepository duAnRepository, BoMonRepository boMonRepository, CoSoRepository coSoRepository, ChuyenNganhRepository chuyenNganhRepository, LoaiDuAnRepository loaiDuAnRepository, DanhSachCongViecService danhSachCongViecService, ThanhVienDuAnService thanhVienDuAnService) {
        this.duAnRepository = duAnRepository;
        this.boMonRepository = boMonRepository;
        this.coSoRepository = coSoRepository;
        this.chuyenNganhRepository = chuyenNganhRepository;
        this.loaiDuAnRepository = loaiDuAnRepository;
        this.danhSachCongViecService = danhSachCongViecService;
        this.thanhVienDuAnService = thanhVienDuAnService;
    }

    @Override
    public List<DuAnDto> getListDuAn() {
        return duAnRepository.getListDuAn();
    }

    @Override
    public DuAn insertDuAn(DuAn duAn) {

        String prefix = "DA";
        long count = duAnRepository.count() + 1;
        String maDuAn = prefix + String.format("%04d", count);
        duAn.setMaDuAn(maDuAn);

        duAn.setNgayUpdate(null);
        LocalDate today = LocalDate.now();
        if (duAn.getNgayBatDau().isEqual(today)) {
            duAn.setTrangThai(true);
        }else{
            duAn.setTrangThai(false);
        }
        return duAnRepository.save(duAn);
    }

    @Override
    public Page<DuAnDto> getListDuAnPage(Pageable pageable) {
        return duAnRepository.getListDuAnPage(pageable);
    }

    @Override
    public void deleteDuAn(DuAn duAn) {
        danhSachCongViecService.deleteDuAn(duAn);
        thanhVienDuAnService.deleteDuAn(duAn);
        duAnRepository.deleteById(duAn.getId());
    }

    @Override
    public DuAn findById(Integer id) {
        return duAnRepository.findById(id).get();
    }

    @Override
    public DuAn updateDuAn(DuAn duAn) {
        LocalDate today = LocalDate.now();
        duAn.setNgayUpdate(today);
        String maDuAn = duAn.getMaDuAn();
        duAn.setMaDuAn(maDuAn);
        if (duAn.getNgayBatDau().isEqual(today)) {
            duAn.setTrangThai(true);
        }else{
            duAn.setTrangThai(false);
        }

        return duAnRepository.save(duAn);
    }

    @Override
    public void exportDuAnToExcel(HttpServletResponse response) throws IOException {
        List<DuAnDto> duAnList = getListDuAn();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Danh sách Dự án");

        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "Mã Dự Án", "Tên Dự Án", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Chuyên Ngành", "Tiến Độ", "Trạng Thái"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        int rowNum = 1;
        for (DuAnDto duAn : duAnList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(duAn.getId());
            row.createCell(1).setCellValue(duAn.getMaDuAn());
            row.createCell(2).setCellValue(duAn.getTenDuAn());
            row.createCell(3).setCellValue(duAn.getNgayBatDau().toString());
            row.createCell(4).setCellValue(duAn.getNgayKetThuc().toString());
            row.createCell(5).setCellValue(duAn.getChuyenNganh());
            row.createCell(6).setCellValue(duAn.getTienDo());
            row.createCell(7).setCellValue(duAn.isTrangThai() ? "Đang diễn ra" : "Chưa diễn ra");
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=danh_sach_du_an.xlsx");

        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        workbook.close();
    }

    @Override
    public void importDuAnFromExcel(MultipartFile file) throws IOException {

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                DuAn duAn = new DuAn();
                try {
                    Cell tenDuAnCell = row.getCell(2);
                    if (tenDuAnCell != null) {
                        duAn.setTenDuAn(tenDuAnCell.getStringCellValue());
                    } else {
                        continue;
                    }

                    Cell ngayBatDauCell = row.getCell(3);
                    if (ngayBatDauCell != null) {
                        String ngayBatDauStr = ngayBatDauCell.getStringCellValue();
                        LocalDate ngayBatDau = LocalDate.parse(ngayBatDauStr, dateFormatter);
                        duAn.setNgayBatDau(ngayBatDau);
                    }

                    Cell ngayKetThucCell = row.getCell(4);
                    if (ngayKetThucCell != null) {
                        String ngayKetThucStr = ngayKetThucCell.getStringCellValue();
                        LocalDate ngayKetThuc = LocalDate.parse(ngayKetThucStr, dateFormatter);
                        duAn.setNgayKetThucDuKien(ngayKetThuc);
                    }

                    Cell chuyenNganhCell = row.getCell(5);
                    if (chuyenNganhCell != null) {
                        String tenChuyenNganh = chuyenNganhCell.getStringCellValue();
                        ChuyenNganh chuyenNganh = chuyenNganhRepository.findByTenChuyenNganh(tenChuyenNganh)
                                .orElseThrow(() -> new IllegalArgumentException("Chuyên ngành không tồn tại: " + tenChuyenNganh));
                        duAn.setIdChuyenNganh(chuyenNganh);
                    }

//                    Cell tienDoCell = row.getCell(6);
//                    if (tienDoCell != null) {
//                        duAn.set(tienDoCell.getStringCellValue());
//                    }

                    Cell trangThaiCell = row.getCell(7);
                    if (trangThaiCell != null) {
                        String trangThaiStr = trangThaiCell.getStringCellValue();
                        duAn.setTrangThai(trangThaiStr.equalsIgnoreCase("Đang diễn ra"));
                    }

                    String prefix = "DA";
                    long count = duAnRepository.count() + 1;
                    String maDuAn = prefix + String.format("%04d", count);
                    duAn.setMaDuAn(maDuAn);

                    duAn.setIdBoMon(null);
                    duAn.setIdCoSo(null);
                    duAn.setIdLoaiDuAn(null);

                    duAnRepository.save(duAn);

                } catch (Exception e) {
                    System.err.println("Lỗi khi xử lý hàng " + i + ": " + e.getMessage());
                }
            }
        }

    }

    @Override
    public Page<DuAnDto> findByTenDuAnAndTrangThai(String tenDuAn, Boolean trangThai, Pageable pageable) {
        return duAnRepository.findByTenDuAnAndTrangThai(tenDuAn,trangThai,pageable);
    }


}
