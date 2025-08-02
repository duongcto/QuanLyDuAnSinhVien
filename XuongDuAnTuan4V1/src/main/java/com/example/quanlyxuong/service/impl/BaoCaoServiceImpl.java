package com.example.quanlyxuong.service.impl;

import com.example.quanlyxuong.dto.BaoCaoDto;
import com.example.quanlyxuong.entity.BaoCao;
import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.ThanhVienDuAn;
import com.example.quanlyxuong.repository.BaoCaoRepository;
import com.example.quanlyxuong.service.BaoCaoService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaoCaoServiceImpl implements BaoCaoService {
    private final BaoCaoRepository baoCaoRepository;
    private final ModelMapper modelMapper;

    public BaoCaoServiceImpl(BaoCaoRepository baoCaoRepository, ModelMapper modelMapper) {
        this.baoCaoRepository = baoCaoRepository;
        this.modelMapper = modelMapper;

        TypeMap<BaoCao, BaoCaoDto> propertyMapper = modelMapper.createTypeMap(BaoCao.class, BaoCaoDto.class);
        propertyMapper.addMappings(mapper -> {
            mapper.map(src -> src.getIdNguoiDung().getHoTen(), BaoCaoDto::setHoTen);
            mapper.map(src -> src.getIdDuAn().getTenDuAn(), BaoCaoDto::setTenDuAn);
        });
    }

    @Override
    public List<BaoCaoDto> getBaoCaoList() {
        return baoCaoRepository.findAll().stream()
                .map(i -> modelMapper.map(i, BaoCaoDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<BaoCaoDto> getBaoCaoByDate(String ngay) {
        LocalDate date = LocalDate.parse(ngay, DateTimeFormatter.ISO_LOCAL_DATE);

        List<BaoCao> entities = baoCaoRepository.findByNgayBaoCao((date));

        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private BaoCaoDto convertToDto(BaoCao entity) {
        return modelMapper.map(entity, BaoCaoDto.class);
    }

    public List<BaoCaoDto> getBaoCaoTheoNgay(LocalDate ngay) {
        return baoCaoRepository.findByNgayBaoCao(ngay).stream()
                .map(baoCao -> modelMapper.map(baoCao, BaoCaoDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void exportBaoCaoTheoThang(OutputStream outputStream, int month, int year) throws IOException {
        List<BaoCao> ds = baoCaoRepository.findByThangVaNam(month, year); // Viết truy vấn tương ứng

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Báo cáo");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Họ tên");
        header.createCell(1).setCellValue("Hôm nay làm gì");
        header.createCell(2).setCellValue("Gặp khó khăn gì");
        header.createCell(3).setCellValue("Ngày mai làm gì");
        header.createCell(4).setCellValue("Ngày báo cáo");
        header.createCell(5).setCellValue("Trạng thái");

        int rowIdx = 1;
        for (BaoCao bc : ds) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(bc.getIdNguoiDung().getHoTen());
            row.createCell(1).setCellValue(bc.getHomNayLamGi());
            row.createCell(2).setCellValue(bc.getGapKhoKhanGi());
            row.createCell(3).setCellValue(bc.getNgayMaiLamGi());
            row.createCell(4).setCellValue(bc.getNgayBaoCao().toString());
            row.createCell(5).setCellValue(bc.getTrangThai() ? "Hoàn thành" : "Chưa");
        }

        workbook.write(outputStream);
        workbook.close();
    }


}

