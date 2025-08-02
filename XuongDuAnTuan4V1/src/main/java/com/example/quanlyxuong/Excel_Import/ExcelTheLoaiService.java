package com.example.quanlyxuong.Excel_Import;

import com.example.quanlyxuong.entity.LoaiDuAn;
import com.example.quanlyxuong.repository.LoaiDuAnRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelTheLoaiService {
    @Autowired
    private LoaiDuAnRepository loaiDuAnRepository;


    public void exportStaffMajorTemplate(HttpServletResponse response) throws IOException {
        List<LoaiDuAn> list = loaiDuAnRepository.findAll();

        // Create workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách thể loại");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Tên Loại Dự Án");
        headerRow.createCell(2).setCellValue("Mô tả");

        // Add staff data to Excel sheet
//        int rowCount = 1;
//        for (LoaiDuAn loaiDuAn : list) {
//            Row row = sheet.createRow(rowCount++);
//            row.createCell(0).setCellValue(loaiDuAn.getId());
//            row.createCell(1).setCellValue(loaiDuAn.getTenLoaiDuAn());
//            row.createCell(2).setCellValue(loaiDuAn.getMoTa());
//        }

        // Set response headers for downloading the Excel file
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=loai_du_an_template.xlsx");

        // Write to response OutputStream
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
