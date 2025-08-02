package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.entity.TaskMember;
import com.example.quanlyxuong.service.TaskMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/members")
public class TaskMemberController {
    @Autowired
    private TaskMemberService taskMemberService;

    @GetMapping
    public List<TaskMember> getMembers(@PathVariable Integer taskId) {
        return taskMemberService.getByTaskId(taskId);
    }

    @PostMapping
    public TaskMember addMember(@PathVariable Integer taskId, @RequestBody TaskMember taskMember) {
        // Đảm bảo set đúng taskId
        if (taskMember.getCongViec() == null || taskMember.getCongViec().getId() == null) {
            com.example.quanlyxuong.entity.CongViec task = new com.example.quanlyxuong.entity.CongViec();
            task.setId(taskId);
            taskMember.setCongViec(task);
        }
        return taskMemberService.save(taskMember);
    }

    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable Integer id) {
        taskMemberService.delete(id);
    }
} 