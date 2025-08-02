package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.TaskMember;
import com.example.quanlyxuong.repository.TaskMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskMemberService {
    @Autowired
    private TaskMemberRepository taskMemberRepository;

    public List<TaskMember> getAll() {
        return taskMemberRepository.findAll();
    }

    public List<TaskMember> getByTaskId(Integer taskId) {
        return taskMemberRepository.findAll().stream()
                .filter(tm -> tm.getCongViec() != null && tm.getCongViec().getId().equals(taskId))
                .toList();
    }

    public TaskMember save(TaskMember taskMember) {
        return taskMemberRepository.save(taskMember);
    }

    public void delete(Integer id) {
        taskMemberRepository.deleteById(id);
    }

}