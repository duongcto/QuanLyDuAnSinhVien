package com.example.quanlyxuong.Excel_Import;

import com.example.quanlyxuong.dto.TheLoaiDto;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class ExcelHelper {
    public static List<TheLoaiDto> readExcel(MultipartFile file) throws Exception {
        List<TheLoaiDto> list = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // skip header

            TheLoaiDto tl = new TheLoaiDto();
            tl.setTenLoaiDuAn(row.getCell(0).getStringCellValue());
            tl.setMoTa(row.getCell(1).getStringCellValue());
            list.add(tl);
        }
        workbook.close();
        return list;
    }
}
