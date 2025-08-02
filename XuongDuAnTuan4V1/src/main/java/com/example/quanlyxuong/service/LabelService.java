package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.Label;
import com.example.quanlyxuong.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;

    public List<Label> getAll() {
        return labelRepository.findAll();
    }

    public Optional<Label> getById(Integer id) {
        return labelRepository.findById(id);
    }

    public Label save(Label label) {
        return labelRepository.save(label);
    }

    public void delete(Integer id) {
        labelRepository.deleteById(id);
    }
} 