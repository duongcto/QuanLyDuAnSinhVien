package com.example.quanlyxuong.Excel_Import;

import com.example.quanlyxuong.entity.BoMon;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelExporter {

    public static ByteArrayInputStream boMonToExcel(List<BoMon> boMons) throws IOException {
        String[] columns = {"ID", "Mã Bộ Môn", "Tên Bộ Môn", "Trưởng Bộ Môn", "Số Thành Viên", "Ngày Thành Lập", "Trạng Thái"};

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("BoMon");
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowIdx = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            for (BoMon boMon : boMons) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(boMon.getId());
                row.createCell(1).setCellValue(boMon.getMaBoMon());
                row.createCell(2).setCellValue(boMon.getTenBoMon());
                row.createCell(3).setCellValue(boMon.getTrungBoMon());
                row.createCell(4).setCellValue(boMon.getSoThanhVien());
                row.createCell(5).setCellValue(boMon.getNgayThanhLap().format(formatter));
                row.createCell(6).setCellValue(boMon.getTrangThai() ? "Hoạt động" : "Ngừng");
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
