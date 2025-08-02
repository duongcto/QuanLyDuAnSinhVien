package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.entity.Attachment;
import com.example.quanlyxuong.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/attachments")
public class AttachmentController {
    @Autowired
    private AttachmentService attachmentService;

    @GetMapping
    public List<Attachment> getAttachments(@PathVariable Integer taskId) {
        return attachmentService.getByTaskId(taskId);
    }

    @PostMapping
    public Attachment addAttachment(@PathVariable Integer taskId, @RequestBody Attachment attachment) {
        // Đảm bảo set đúng taskId
        if (attachment.getTask() == null || attachment.getTask().getId() == null) {
            com.example.quanlyxuong.entity.CongViec task = new com.example.quanlyxuong.entity.CongViec();
            task.setId(taskId);
            attachment.setTask(task);
        }
        return attachmentService.save(attachment);
    }

    @DeleteMapping("/{id}")
    public void deleteAttachment(@PathVariable Integer id) {
        attachmentService.delete(id);
    }
} 