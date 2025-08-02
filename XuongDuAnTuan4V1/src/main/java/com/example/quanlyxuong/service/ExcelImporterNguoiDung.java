package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.NguoiDung;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelImporterNguoiDung {

    public static List<NguoiDung> readFromExcel(InputStream is) throws Exception {
        List<NguoiDung> list = new ArrayList<>();

        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = sheet.iterator();

        // Bỏ qua dòng tiêu đề
        if (iterator.hasNext()) {
            iterator.next();
        }

        while (iterator.hasNext()) {
            Row row = iterator.next();
            NguoiDung nd = new NguoiDung();

            nd.setHoTen(getCellValue(row.getCell(0)));
            nd.setMaNguoiDung(getCellValue(row.getCell(1)));
            nd.setTenDangNhap(getCellValue(row.getCell(2)));
            nd.setEmailPt(getCellValue(row.getCell(3)));
            nd.setEmailFe(getCellValue(row.getCell(4)));

            String soDienThoai = getCellValue(row.getCell(5));
            nd.setSoDienThoai(soDienThoai.length() > 20 ? soDienThoai.substring(0, 20) : soDienThoai);

            nd.setMatKhau(getCellValue(row.getCell(6)));
            nd.setNgayTao(LocalDate.now());
            nd.setNgayUpdate(LocalDate.now());
            nd.setTrangThai("Đang học");

            // Xử lý idVaiTro nếu cần, hoặc để null rồi set sau
            list.add(nd);
        }

        workbook.close();
        return list;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
}
