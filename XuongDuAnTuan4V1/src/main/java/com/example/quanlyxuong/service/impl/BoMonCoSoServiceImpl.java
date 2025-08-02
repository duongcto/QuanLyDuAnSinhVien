package com.example.quanlyxuong.service.impl;

import com.example.quanlyxuong.dto.BoMonCoSoDto;
import com.example.quanlyxuong.entity.BoMonCoSo;
import com.example.quanlyxuong.repository.BoMonCoSoRepository;
import com.example.quanlyxuong.repository.BoMonRepository;
import com.example.quanlyxuong.repository.CoSoRepository;
import com.example.quanlyxuong.service.BoMonCoSoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.example.quanlyxuong.entity.BoMonCoSoId;

@Service
public class BoMonCoSoServiceImpl implements BoMonCoSoService {
    @Autowired
    private BoMonCoSoRepository repo;
    @Autowired
    private BoMonRepository boMonRepository;
    @Autowired
    private CoSoRepository coSoRepository;

    @Override
    public Page<BoMonCoSoDto> getBoMonCoSoPage(int page, int size) {
        Page<BoMonCoSo> pageData = repo.findAll(PageRequest.of(page, size));
        List<BoMonCoSoDto> dtos = pageData.getContent().stream().map(this::toDto).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageData.getPageable(), pageData.getTotalElements());
    }

    @Override
    public BoMonCoSoDto getById(Integer idBoMon, Integer idCoSo) {
        BoMonCoSoId id = new BoMonCoSoId();
        id.setIdBoMon(idBoMon);
        id.setIdCoSo(idCoSo);
        BoMonCoSo entity = repo.findById(id).orElse(null);
        if (entity == null) return null;
        BoMonCoSoDto dto = new BoMonCoSoDto();
        dto.setIdBoMon(entity.getId().getIdBoMon());
        dto.setIdCoSo(entity.getId().getIdCoSo());
        dto.setTenBoMon(entity.getIdBoMon().getTenBoMon());
        dto.setTenCoSo(entity.getIdCoSo().getTenCoSo());
        return dto;
    }

    @Override
    public BoMonCoSoDto save(BoMonCoSoDto dto) {
        BoMonCoSoId id = new BoMonCoSoId();
        id.setIdBoMon(dto.getIdBoMon());
        id.setIdCoSo(dto.getIdCoSo());

        BoMonCoSo entity = repo.findById(id).orElse(new BoMonCoSo());
        entity.setId(id);
        entity.setIdBoMon(boMonRepository.findById(dto.getIdBoMon()).orElse(null));
        entity.setIdCoSo(coSoRepository.findById(dto.getIdCoSo()).orElse(null));

        BoMonCoSo saved = repo.save(entity);

        BoMonCoSoDto result = new BoMonCoSoDto();
        result.setIdBoMon(saved.getId().getIdBoMon());
        result.setIdCoSo(saved.getId().getIdCoSo());
        result.setTenBoMon(saved.getIdBoMon().getTenBoMon());
        result.setTenCoSo(saved.getIdCoSo().getTenCoSo());
        return result;
    }

    @Override
    public void delete(Integer idBoMon, Integer idCoSo) {
        BoMonCoSoId id = new BoMonCoSoId();
        id.setIdBoMon(idBoMon);
        id.setIdCoSo(idCoSo);
        repo.deleteById(id);
    }

    @Override
    public List<BoMonCoSoDto> getAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    private BoMonCoSoDto toDto(BoMonCoSo entity) {
        BoMonCoSoDto dto = new BoMonCoSoDto();
        dto.setIdBoMon(entity.getIdBoMon() != null ? entity.getIdBoMon().getId() : null);
        dto.setTenBoMon(entity.getIdBoMon() != null ? entity.getIdBoMon().getTenBoMon() : null);
        dto.setIdCoSo(entity.getIdCoSo() != null ? entity.getIdCoSo().getId() : null);
        dto.setTenCoSo(entity.getIdCoSo() != null ? entity.getIdCoSo().getTenCoSo() : null);
        return dto;
    }
} 