package com.example.quanlyxuong.service.impl;

import com.example.quanlyxuong.dto.BoMonDto;
import com.example.quanlyxuong.entity.BoMon;
import com.example.quanlyxuong.exception.ResourceNotFoundException;
import com.example.quanlyxuong.repository.BoMonRepository;
import com.example.quanlyxuong.service.BoMonService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoMonServiceImpl implements BoMonService {
    private final BoMonRepository boMonRepository;
    private final ModelMapper modelMapper;

    public BoMonServiceImpl(BoMonRepository boMonRepository, ModelMapper modelMapper) {
        this.boMonRepository = boMonRepository;
        this.modelMapper = modelMapper;

        TypeMap<BoMon, BoMonDto> propertyMapper = modelMapper.createTypeMap(BoMon.class, BoMonDto.class);

    }

    @Override
    public List<BoMonDto> getBoMon() {
        return boMonRepository.findAll().stream()
                .map(i -> modelMapper.map(i, BoMonDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public BoMon addBoMon(BoMon boMon) {
        if (boMonRepository.existsByMaBoMon(boMon.getMaBoMon())) {
            throw new IllegalArgumentException("Mã bộ môn đã tồn tại!");
        }
        return boMonRepository.save(boMon);
    }

    @Override
    public BoMon getBoMonCanTim(Integer id) {
        return boMonRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bo mon not found with id = " + id));
    }

    @Override
    public BoMon updateBoMon(BoMon boMon, Integer id) {

        BoMon exitstingBoMon = boMonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bộ môn không tồn tại với id = " + id));

        // Nếu mã bộ môn mới khác mã hiện tại thì kiểm tra trùng
        if (!exitstingBoMon.getMaBoMon().equals(boMon.getMaBoMon()) &&
                boMonRepository.existsByMaBoMon(boMon.getMaBoMon())) {
            throw new IllegalArgumentException("Mã bộ môn đã tồn tại!");
        }

        exitstingBoMon.setMaBoMon(boMon.getMaBoMon());
        exitstingBoMon.setTenBoMon(boMon.getTenBoMon());
        exitstingBoMon.setTrungBoMon(boMon.getTrungBoMon());
        exitstingBoMon.setSoThanhVien(boMon.getSoThanhVien());
        exitstingBoMon.setNgayThanhLap(boMon.getNgayThanhLap());
        exitstingBoMon.setTrangThai(boMon.getTrangThai());

        return boMonRepository.save(exitstingBoMon);
    }

    @Override
    public void deleteBoMon(Integer id) {

        BoMon exitstingBoMon = boMonRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Diem not found with id = " + id));

        boMonRepository.delete(exitstingBoMon);
    }

    @Override
    public List<BoMonDto> searchAllFields(String keyword) {
        return boMonRepository.searchAllFields(keyword).stream()
                .map(b -> modelMapper.map(b, BoMonDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<BoMon> getBoMonPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return boMonRepository.findAll(pageable);
    }

    @Override
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<BoMon> listBoMon = boMonRepository.findAll();

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ServletOutputStream outputStream = response.getOutputStream()) {

            Sheet sheet = workbook.createSheet("Bộ Môn");
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Mã bộ môn", "Tên bộ môn", "Trưởng bộ môn", "Số thành viên", "Ngày thành lập", "Trạng thái"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowIdx = 1;
            for (BoMon bm : listBoMon) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(bm.getMaBoMon());
                row.createCell(1).setCellValue(bm.getTenBoMon());
                row.createCell(2).setCellValue(bm.getTrungBoMon());
                row.createCell(3).setCellValue(bm.getSoThanhVien());

                if (bm.getNgayThanhLap() != null) {
                    CreationHelper createHelper = workbook.getCreationHelper();
                    CellStyle cellStyle = workbook.createCellStyle();
                    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
                    Cell dateCell = row.createCell(4);
                    dateCell.setCellValue(bm.getNgayThanhLap());
                    dateCell.setCellStyle(cellStyle);
                } else {
                    row.createCell(4).setCellValue("");
                }

                row.createCell(5).setCellValue(bm.getTrangThai() ? "Hoạt động" : "Ngừng hoạt động");
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            outputStream.flush();
        }
    }

    @Override
    public void saveAll(List<BoMon> boMons) {
        boMonRepository.saveAll(boMons);
    }

    @Override
    public List<String> importFromExcel(MultipartFile file) throws IOException {
        List<String> errors = new ArrayList<>();
        List<BoMon> boMons = new ArrayList<>();

        List<String> maBoMonHienCo = boMonRepository.findAll()
                .stream()
                .map(BoMon::getMaBoMon)
                .toList();

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) rows.next(); // Bỏ header

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            int rowNumber = 1;

            while (rows.hasNext()) {
                rowNumber++;
                Row currentRow = rows.next();

                String maBoMon = getCellValue(currentRow.getCell(0));
                String tenBoMon = getCellValue(currentRow.getCell(1));

                if (maBoMon.isEmpty()) {
                    errors.add(" Mã bộ môn không được để trống!");
                    continue;
                }
                if (tenBoMon.isEmpty()) {
                    errors.add(" Tên bộ môn không được để trống!");
                    continue;
                }

                if (boMons.stream().anyMatch(bm -> bm.getMaBoMon().equals(maBoMon))) {
                    errors.add(" Mã bộ môn '" + maBoMon + "' bị trùng trong file import!");
                    continue;
                }

                // Check trùng với DB
                if (maBoMonHienCo.contains(maBoMon)) {
                    errors.add(" Mã bộ môn '" + maBoMon + "' đã tồn tại trong hệ thống!");
                    continue;
                }

                BoMon boMon = new BoMon();
                boMon.setMaBoMon(maBoMon);
                boMon.setTenBoMon(tenBoMon);
                boMon.setTrungBoMon(getCellValue(currentRow.getCell(2)));

                String soThanhVienStr = getCellValue(currentRow.getCell(3));
                boMon.setSoThanhVien(soThanhVienStr.isEmpty() ? 0 : Integer.parseInt(soThanhVienStr));

                String ngayThanhLapStr = getCellValue(currentRow.getCell(4));
                if (!ngayThanhLapStr.isEmpty()) {
                    boMon.setNgayThanhLap(LocalDate.parse(ngayThanhLapStr, formatter));
                }

                String trangThaiStr = getCellValue(currentRow.getCell(5));
                boMon.setTrangThai("true".equalsIgnoreCase(trangThaiStr));

                boMons.add(boMon);
            }
            workbook.close();
        } catch (IOException e) {
            errors.add("Lỗi đọc file Excel: " + e.getMessage());
        }

        if (errors.isEmpty()) {
            boMonRepository.saveAll(boMons);
        }

        return errors;
    }


    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
            default:
                return "";
        }
    }


    @Override
    public List<BoMon> findAllBoMon() {
        return boMonRepository.findAll();
    }

}
