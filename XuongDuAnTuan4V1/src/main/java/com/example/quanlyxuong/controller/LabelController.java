package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.dto.LabelDto;
import com.example.quanlyxuong.entity.Label;
import com.example.quanlyxuong.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/labels")
public class LabelController {
    @Autowired
    private LabelService labelService;

    @GetMapping
    public List<LabelDto> getAll() {
        return labelService.getAll().stream()
                .map(l -> new LabelDto(l.getId(), l.getName(), l.getColor()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LabelDto getById(@PathVariable Integer id) {
        Optional<Label> opt = labelService.getById(id);
        if (opt.isPresent()) {
            Label l = opt.get();
            return new LabelDto(l.getId(), l.getName(), l.getColor());
        }
        return null;
    }

    @PostMapping
    public LabelDto create(@RequestBody Label label) {
        Label saved = labelService.save(label);
        return new LabelDto(saved.getId(), saved.getName(), saved.getColor());
    }

    @PutMapping("/{id}")
    public Label update(@PathVariable Integer id, @RequestBody Label label) {
        label.setId(id);
        return labelService.save(label);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        labelService.delete(id);
    }
} 