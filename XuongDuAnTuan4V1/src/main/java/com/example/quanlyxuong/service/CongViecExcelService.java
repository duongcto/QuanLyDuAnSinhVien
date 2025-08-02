package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.CongViec;
import com.example.quanlyxuong.entity.DanhSachCongViec;
import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.PhanCong;
import com.example.quanlyxuong.repository.CongViecRepository;
import com.example.quanlyxuong.repository.DanhSachCongViecRepository;
import com.example.quanlyxuong.repository.GiaiDoanRepository;
import com.example.quanlyxuong.repository.PhanCongRepository;
import com.example.quanlyxuong.repository.NguoiDungRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class CongViecExcelService {

    @Autowired
    private CongViecRepository congViecRepository;
    @Autowired
    private DanhSachCongViecRepository danhSachCongViecRepository;
    @Autowired
    private GiaiDoanRepository giaiDoanRepository;
    @Autowired
    private PhanCongRepository phanCongRepository;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    private List<String> importErrors = new ArrayList<>();

    // Phương thức import công việc từ file Excel
    public void importCongViecFromExcel(MultipartFile file) throws IOException {
        importErrors.clear(); // Xóa lỗi cũ trước khi import mới
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // Bỏ qua hàng tiêu đề
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                List<String> rowData = new ArrayList<>();
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    rowData.add(getCellValueAsString(currentCell));
                }

                if (rowData.size() < 7) { // Kiểm tra số lượng cột tối thiểu
                    importErrors.add("Lỗi hàng " + (rowNumber + 1) + ": Số lượng cột không đủ.");
                    rowNumber++;
                    continue;
                }

                try {
                    CongViec congViec = new CongViec();
                    congViec.setTenCongViec(rowData.get(0));

                    // Xử lý Ngày bắt đầu và Ngày kết thúc
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Hoặc định dạng bạn đang dùng
                    congViec.setNgayBatDau(LocalDate.parse(rowData.get(1), formatter));
                    congViec.setNgayKetThuc(LocalDate.parse(rowData.get(2), formatter));

                    congViec.setThoiGian(Double.parseDouble(rowData.get(3)));
                    congViec.setTrangThai(Boolean.parseBoolean(rowData.get(4)));

                    // Tìm DanhSachCongViec theo tên (hoặc ID)
                    String tenDsCongViec = rowData.get(5);
                    Optional<DanhSachCongViec> danhSachCongViecOptional = danhSachCongViecRepository.findByTenDanhSachCongViec(tenDsCongViec);
                    if (danhSachCongViecOptional.isPresent()) {
                        congViec.setIdDanhSachCongViec(danhSachCongViecOptional.get());
                    } else {
                        importErrors.add("Lỗi hàng " + (rowNumber + 1) + ": Danh sách công việc '" + tenDsCongViec + "' không tồn tại.");
                        rowNumber++;
                        continue;
                    }

                    // Tìm NguoiDung theo email (hoặc ID)
                    String emailNguoiDung = rowData.get(6);
                    Optional<NguoiDung> nguoiDungOptional = nguoiDungRepository.findByEmailPt(emailNguoiDung);
                    if (nguoiDungOptional.isPresent()) {
                        // Tạo hoặc tìm PhanCong nếu cần
                        // Trong ví dụ này, tôi giả định PhanCong được tạo tự động hoặc tồn tại.
                        // Nếu PhanCong là một entity độc lập, bạn cần tìm hoặc tạo nó tại đây.
                        PhanCong phanCong = new PhanCong();
                        phanCong.setIdNguoiDung(nguoiDungOptional.get());
                        phanCong.setIdDanhSachCongViec(congViec.getIdDanhSachCongViec()); // Gán lại danh sách công việc
                        phanCong.setNgayPhanCong(LocalDate.now());
                        phanCongRepository.save(phanCong);
                        congViec.setIdPhanCong(phanCong);
                    } else {
                        importErrors.add("Lỗi hàng " + (rowNumber + 1) + ": Người dùng '" + emailNguoiDung + "' không tồn tại.");
                        rowNumber++;
                        continue;
                    }

                    congViecRepository.save(congViec);
                } catch (Exception e) {
                    importErrors.add("Lỗi hàng " + (rowNumber + 1) + ": " + e.getMessage());
                }
                rowNumber++;
            }
        }
    }

    // Helper method to get cell value as string
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new java.text.SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue());
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    // Phương thức tạo template Excel
    public void exportCongViecTemplate(HttpServletResponse response) throws IOException {
        String[] headers = {"Tên Công Việc", "Ngày Bắt Đầu (dd/MM/yyyy)", "Ngày Kết Thúc (dd/MM/yyyy)", "Thời Gian (giờ)", "Trạng Thái (true/false)", "Tên Danh Sách Công Việc", "Email Người Dùng"};
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Mẫu Công Việc");
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < headers.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers[col]);
            }

            // Thiết lập header cho response
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=cong_viec_template.xlsx");

            workbook.write(response.getOutputStream());
        }
    }

    // Phương thức xuất lỗi import
    public void exportImportErrors(HttpServletResponse response) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Lỗi Import Công Việc");

            Row headerRow = sheet.createRow(0);
            Cell headerCell = headerRow.createCell(0);
            headerCell.setCellValue("Thông báo lỗi");

            int rowNum = 1;
            for (String error : importErrors) {
                Row row = sheet.createRow(rowNum++);
                Cell cell = row.createCell(0);
                cell.setCellValue(error);
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=cong_viec_import_errors.xlsx");

            workbook.write(response.getOutputStream());
        }
    }
} 