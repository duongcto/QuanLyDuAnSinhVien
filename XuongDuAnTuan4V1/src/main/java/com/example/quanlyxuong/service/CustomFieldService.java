package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.CustomField;
import com.example.quanlyxuong.repository.CustomFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomFieldService {
    @Autowired
    private CustomFieldRepository customFieldRepository;

    public List<CustomField> getAll() {
        return customFieldRepository.findAll();
    }

    public List<CustomField> getByTaskId(Integer taskId) {
        return customFieldRepository.findAll().stream()
                .filter(cf -> cf.getTask() != null && cf.getTask().getId().equals(taskId))
                .toList();
    }

    public CustomField save(CustomField customField) {
        return customFieldRepository.save(customField);
    }

    public void delete(Integer id) {
        customFieldRepository.deleteById(id);
    }
} 