package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.entity.CustomField;
import com.example.quanlyxuong.service.CustomFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/custom-fields")
public class CustomFieldController {
    @Autowired
    private CustomFieldService customFieldService;

    @GetMapping
    public List<CustomField> getCustomFields(@PathVariable Integer taskId) {
        return customFieldService.getByTaskId(taskId);
    }

    @PostMapping
    public CustomField addCustomField(@PathVariable Integer taskId, @RequestBody CustomField customField) {
        // Đảm bảo set đúng taskId
        if (customField.getTask() == null || customField.getTask().getId() == null) {
            com.example.quanlyxuong.entity.CongViec task = new com.example.quanlyxuong.entity.CongViec();
            task.setId(taskId);
            customField.setTask(task);
        }
        return customFieldService.save(customField);
    }

    @PutMapping("/{id}")
    public CustomField updateCustomField(@PathVariable Integer id, @RequestBody CustomField customField) {
        customField.setId(id);
        return customFieldService.save(customField);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomField(@PathVariable Integer id) {
        customFieldService.delete(id);
    }
} 