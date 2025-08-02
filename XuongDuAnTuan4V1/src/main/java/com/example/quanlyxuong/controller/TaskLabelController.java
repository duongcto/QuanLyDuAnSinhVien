package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.entity.TaskLabel;
import com.example.quanlyxuong.service.TaskLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/labels")
public class TaskLabelController {
    @Autowired
    private TaskLabelService taskLabelService;

    @GetMapping
    public List<TaskLabel> getLabels(@PathVariable Integer taskId) {
        return taskLabelService.getByTaskId(taskId);
    }

    @PostMapping
    public TaskLabel addLabel(@PathVariable Integer taskId, @RequestBody TaskLabel taskLabel) {
        // Đảm bảo set đúng taskId
        if (taskLabel.getTask() == null || taskLabel.getTask().getId() == null) {
            com.example.quanlyxuong.entity.CongViec task = new com.example.quanlyxuong.entity.CongViec();
            task.setId(taskId);
            taskLabel.setTask(task);
        }
        return taskLabelService.save(taskLabel);
    }

    @DeleteMapping("/{id}")
    public void deleteLabel(@PathVariable Integer id) {
        taskLabelService.delete(id);
    }
} 