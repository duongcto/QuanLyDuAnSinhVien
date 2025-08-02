package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.TaskLabel;
import com.example.quanlyxuong.repository.TaskLabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskLabelService {
    @Autowired
    private TaskLabelRepository taskLabelRepository;

    public List<TaskLabel> getAll() {
        return taskLabelRepository.findAll();
    }

    public List<TaskLabel> getByTaskId(Integer taskId) {
        return taskLabelRepository.findAll().stream()
                .filter(tl -> tl.getTask() != null && tl.getTask().getId().equals(taskId))
                .toList();
    }

    public TaskLabel save(TaskLabel taskLabel) {
        return taskLabelRepository.save(taskLabel);
    }

    public void delete(Integer id) {
        taskLabelRepository.deleteById(id);
    }
} 