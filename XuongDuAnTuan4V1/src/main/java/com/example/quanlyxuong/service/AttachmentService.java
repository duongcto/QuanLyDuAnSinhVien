package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.Attachment;
import com.example.quanlyxuong.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentService {
    @Autowired
    private AttachmentRepository attachmentRepository;

    public List<Attachment> getAll() {
        return attachmentRepository.findAll();
    }

    public List<Attachment> getByTaskId(Integer taskId) {
        return attachmentRepository.findAll().stream()
                .filter(a -> a.getTask() != null && a.getTask().getId().equals(taskId))
                .toList();
    }

    public Attachment save(Attachment attachment) {
        return attachmentRepository.save(attachment);
    }

    public void delete(Integer id) {
        attachmentRepository.deleteById(id);
    }
} 