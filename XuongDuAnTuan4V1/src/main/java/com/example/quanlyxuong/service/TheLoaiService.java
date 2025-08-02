package com.example.quanlyxuong.service;

import com.example.quanlyxuong.Excel_Import.ExcelHelper;
import com.example.quanlyxuong.dto.TheLoaiDto;
import com.example.quanlyxuong.entity.LoaiDuAn;
import com.example.quanlyxuong.mapper.TheLoaiMapper;
import com.example.quanlyxuong.repository.LoaiDuAnRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheLoaiService implements LoaiDuAnIml {
    private final LoaiDuAnRepository loaiDuAnRepository;


    public TheLoaiService(LoaiDuAnRepository loaiDuAnRepository) {
        this.loaiDuAnRepository = loaiDuAnRepository;
    }

//    // Chuyển Entity sang DTO
//    private TheLoaiDto covertDTO(LoaiDuAn loaiDuAn) {
//        if (loaiDuAn == null) {
//            return null;
//        }
//        return new TheLoaiDto(loaiDuAn.getId(),loaiDuAn.getTenLoaiDuAn(),loaiDuAn.getMoTa());
//    }
//
//    // Chuyển DTO sang Entity
//    private LoaiDuAn covertToEntity(TheLoaiDto theLoaiDto) {
//        if (theLoaiDto == null) {
//            return null;
//        }
//        return new LoaiDuAn(theLoaiDto.getId(),theLoaiDto.getTenLoaiDuAn(),theLoaiDto.getMoTa());
//    }


//    public List<TheLoaiDto> getAll() {
//        return loaiDuAnRepository.findAll().
//                stream().map(TheLoaiMapper::toDto).collect(Collectors.toList());
//    }

    public TheLoaiDto save(@Valid TheLoaiDto theLoaiDto) {
        theLoaiDto.setId(null);
        LoaiDuAn loaiDuAn = TheLoaiMapper.toEntity(theLoaiDto);
        LoaiDuAn save = loaiDuAnRepository.save(loaiDuAn);
        return TheLoaiMapper.toDto(save);
    }

    public boolean deleteById(Integer id) {
        if (loaiDuAnRepository.existsById(id)) {
            loaiDuAnRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public TheLoaiDto update(@Valid TheLoaiDto theLoaiDto) {
        LoaiDuAn loaiDuAn = TheLoaiMapper.toEntity(theLoaiDto);
        LoaiDuAn save = loaiDuAnRepository.save(loaiDuAn);
        return TheLoaiMapper.toDto(save);
    }

    public LoaiDuAn getLoaiDuAnById(Integer id) {
        return loaiDuAnRepository.findById(id).get();
    }

    @Override
    public List<TheLoaiDto> searchByTen(String tenLoaiDuAn) {
        if (tenLoaiDuAn == null || tenLoaiDuAn.isBlank()) {
            return loaiDuAnRepository.findAll()
                    .stream()
                    .map(TheLoaiMapper::toDto)
                    .collect(Collectors.toList());
        }


        return loaiDuAnRepository.findByTenContainingIgnoreCase(tenLoaiDuAn.trim())
                .stream()
                .map(TheLoaiMapper::toDto)
                .collect(Collectors.toList());
    }

    public void importExcel(MultipartFile file) throws Exception {
        List<TheLoaiDto> ds = ExcelHelper.readExcel(file);
        List<LoaiDuAn> dsEntity = ds.stream()
                .map(dto -> {
                    LoaiDuAn entity = new LoaiDuAn();
                    entity.setTenLoaiDuAn(dto.getTenLoaiDuAn());
                    entity.setMoTa(dto.getMoTa());
                    return entity;
                })
                .collect(Collectors.toList());
        loaiDuAnRepository.saveAll(dsEntity);
    }

    public Page<TheLoaiDto> findAll(Pageable pageable) {
        Page<LoaiDuAn> LoaiDuAnPage = loaiDuAnRepository.findAll(pageable);
        return LoaiDuAnPage.map(TheLoaiMapper::toDto);
    }

}
