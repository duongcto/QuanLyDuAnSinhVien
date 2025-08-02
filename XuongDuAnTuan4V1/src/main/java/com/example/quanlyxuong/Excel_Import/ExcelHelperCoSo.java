package com.example.quanlyxuong.Excel_Import;

import com.example.quanlyxuong.entity.CoSo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelperCoSo {
    public static List<CoSo> readExcel(InputStream is) {
        List<CoSo> list = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // Bỏ qua header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                CoSo coSo = new CoSo();
                coSo.setMaCoSo(getCellValue(currentRow.getCell(0)));
                coSo.setTenCoSo(getCellValue(currentRow.getCell(1)));
                coSo.setDiaChi(getCellValue(currentRow.getCell(2)));
                coSo.setSoDienThoai(getCellValue(currentRow.getCell(3)));
                coSo.setEmail(getCellValue(currentRow.getCell(4)));
                coSo.setNgayThanhLap(parseDate(currentRow.getCell(5)));
                coSo.setNgayTao(parseDate(currentRow.getCell(6)));
                coSo.setNgayUpdate(parseDate(currentRow.getCell(7)));
                coSo.setTrangThai(parseBoolean(currentRow.getCell(8)));

                list.add(coSo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((long) cell.getNumericCellValue());
        return "";
    }

    private static LocalDate parseDate(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }
        return null;
    }

    private static Boolean parseBoolean(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.BOOLEAN) return cell.getBooleanCellValue();
        if (cell.getCellType() == CellType.STRING) return Boolean.parseBoolean(cell.getStringCellValue());
        if (cell.getCellType() == CellType.NUMERIC) return cell.getNumericCellValue() == 1;
        return false;
    }
}
