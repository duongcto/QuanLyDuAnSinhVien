package com.example.quanlyxuong.service;


import com.example.quanlyxuong.entity.NguoiDung;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExporterNguoiDung {

    public static ByteArrayInputStream exportToExcel(List<NguoiDung> nguoiDungList) throws IOException {
        String[] columns = {
                "ID", "Họ tên", "Mã người dùng", "Tên đăng nhập", "Email PT", "Email FE",
                "SĐT", "Ngày tạo", "Ngày cập nhật", "Trạng thái", "Vai trò"
        };

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            Sheet sheet = workbook.createSheet("NguoiDung");

            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
            }

            // Data
            int rowIdx = 1;
            for (NguoiDung nd : nguoiDungList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(nd.getId());
                row.createCell(1).setCellValue(nd.getHoTen());
                row.createCell(2).setCellValue(nd.getMaNguoiDung());
                row.createCell(3).setCellValue(nd.getTenDangNhap());
                row.createCell(4).setCellValue(nd.getEmailPt());
                row.createCell(5).setCellValue(nd.getEmailFe());
                row.createCell(6).setCellValue(nd.getSoDienThoai());
                row.createCell(7).setCellValue(nd.getNgayTao() != null ? nd.getNgayTao().toString() : "");
                row.createCell(8).setCellValue(nd.getNgayUpdate() != null ? nd.getNgayUpdate().toString() : "");
                row.createCell(9).setCellValue(nd.getTrangThai());
                row.createCell(10).setCellValue(nd.getIdVaiTro() != null ? nd.getIdVaiTro().getTenVaiTro() : "");
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
