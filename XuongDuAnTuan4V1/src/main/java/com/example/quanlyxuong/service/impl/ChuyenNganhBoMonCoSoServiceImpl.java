package com.example.quanlyxuong.service.impl;

import com.example.quanlyxuong.dto.ChuyenNganhBoMonCoSoDto;
import com.example.quanlyxuong.entity.BoMon;
import com.example.quanlyxuong.entity.ChuyenNganh;
import com.example.quanlyxuong.entity.ChuyenNganhBoMonCoSo;
import com.example.quanlyxuong.entity.ChuyenNganhBoMonCoSoId;
import com.example.quanlyxuong.entity.CoSo;
import com.example.quanlyxuong.repository.BoMonRepository;
import com.example.quanlyxuong.repository.ChuyenNganhBoMonCoSoRepository;
import com.example.quanlyxuong.repository.ChuyenNganhRepository;
import com.example.quanlyxuong.repository.CoSoRepository;
import com.example.quanlyxuong.service.ChuyenNganhBoMonCoSoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChuyenNganhBoMonCoSoServiceImpl implements ChuyenNganhBoMonCoSoService {

    @Autowired
    private ChuyenNganhBoMonCoSoRepository repo;
    
    @Autowired
    private ChuyenNganhRepository chuyenNganhRepository;
    
    @Autowired
    private BoMonRepository boMonRepository;
    
    @Autowired
    private CoSoRepository coSoRepository;

    @Override
    public Page<ChuyenNganhBoMonCoSoDto> getChuyenNganhBoMonCoSoPage(int page, int size) {
        Page<ChuyenNganhBoMonCoSo> pageData = repo.findAll(PageRequest.of(page, size));
        return pageData.map(this::toDto);
    }

    @Override
    public ChuyenNganhBoMonCoSoDto getById(Integer idChuyenNganh, Integer idBoMon, Integer idCoSo) {
        ChuyenNganhBoMonCoSoId id = new ChuyenNganhBoMonCoSoId();
        id.setIdChuyenNganh(idChuyenNganh);
        id.setIdBoMon(idBoMon);
        id.setIdCoSo(idCoSo);
        
        ChuyenNganhBoMonCoSo entity = repo.findById(id).orElse(null);
        if (entity != null) {
            return toDto(entity);
        }
        return null;
    }

    @Override
    public ChuyenNganhBoMonCoSoDto save(ChuyenNganhBoMonCoSoDto dto) {
        ChuyenNganhBoMonCoSoId id = new ChuyenNganhBoMonCoSoId();
        id.setIdChuyenNganh(dto.getIdChuyenNganh());
        id.setIdBoMon(dto.getIdBoMon());
        id.setIdCoSo(dto.getIdCoSo());
        
        ChuyenNganhBoMonCoSo entity = repo.findById(id).orElse(new ChuyenNganhBoMonCoSo());
        entity.setId(id);
        
        // Set related entities
        ChuyenNganh chuyenNganh = chuyenNganhRepository.findById(dto.getIdChuyenNganh()).orElse(null);
        BoMon boMon = boMonRepository.findById(dto.getIdBoMon()).orElse(null);
        CoSo coSo = coSoRepository.findById(dto.getIdCoSo()).orElse(null);
        
        entity.setIdChuyenNganh(chuyenNganh);
        entity.setIdBoMon(boMon);
        entity.setIdCoSo(coSo);
        
        ChuyenNganhBoMonCoSo saved = repo.save(entity);
        return toDto(saved);
    }

    @Override
    public void delete(Integer idChuyenNganh, Integer idBoMon, Integer idCoSo) {
        ChuyenNganhBoMonCoSoId id = new ChuyenNganhBoMonCoSoId();
        id.setIdChuyenNganh(idChuyenNganh);
        id.setIdBoMon(idBoMon);
        id.setIdCoSo(idCoSo);
        repo.deleteById(id);
    }

    @Override
    public void update(Integer oldIdChuyenNganh, Integer oldIdBoMon, Integer oldIdCoSo, ChuyenNganhBoMonCoSoDto dto) {
        // Tìm và xóa bản ghi cũ nếu tồn tại
        ChuyenNganhBoMonCoSoId oldId = new ChuyenNganhBoMonCoSoId();
        oldId.setIdChuyenNganh(oldIdChuyenNganh);
        oldId.setIdBoMon(oldIdBoMon);
        oldId.setIdCoSo(oldIdCoSo);
        if (repo.existsById(oldId)) {
            repo.deleteById(oldId);
        }
        // Tạo entity mới từ dto và lưu lại
        ChuyenNganhBoMonCoSo entity = new ChuyenNganhBoMonCoSo();
        ChuyenNganhBoMonCoSoId newId = new ChuyenNganhBoMonCoSoId();
        newId.setIdChuyenNganh(dto.getIdChuyenNganh());
        newId.setIdBoMon(dto.getIdBoMon());
        newId.setIdCoSo(dto.getIdCoSo());
        entity.setId(newId);
        // Gán các trường khác nếu có
        repo.save(entity);
    }

    @Override
    public List<ChuyenNganhBoMonCoSoDto> getAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    private ChuyenNganhBoMonCoSoDto toDto(ChuyenNganhBoMonCoSo entity) {
        ChuyenNganhBoMonCoSoDto dto = new ChuyenNganhBoMonCoSoDto();
        dto.setIdChuyenNganh(entity.getIdChuyenNganh().getId());
        dto.setIdBoMon(entity.getIdBoMon().getId());
        dto.setIdCoSo(entity.getIdCoSo().getId());
        
        // Set display fields
        dto.setTenChuyenNganh(entity.getIdChuyenNganh().getTenChuyenNganh());
        dto.setMaChuyenNganh(entity.getIdChuyenNganh().getMaChuyenNganh());
        dto.setTenBoMon(entity.getIdBoMon().getTenBoMon());
        dto.setTenCoSo(entity.getIdCoSo().getTenCoSo());
        dto.setTrangThai(entity.getIdChuyenNganh().getTrangThai());
        
        return dto;
    }
} 