package com.example.quanlyxuong.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.quanlyxuong.repository.DuAnRepository;
import com.example.quanlyxuong.repository.NguoiDungRepository;
import com.example.quanlyxuong.entity.NguoiDung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    @Autowired
    private DuAnRepository duAnRepository;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;



    // API trả về danh sách dự án cho biểu đồ cột
    @GetMapping("/api/statistics/projects-bar")
    public Map<String, Object> getProjectsBar() {
        var duAnList = duAnRepository.getListDuAn();
        var labels = new java.util.ArrayList<String>();
        var data = new java.util.ArrayList<Double>();
        if (duAnList != null) {
            for (var da : duAnList) {
                labels.add(da.getTenDuAn() != null ? da.getTenDuAn() : "Không tên");
                String tienDoStr = da.getTienDo();
                double tienDo = 0;
                if (tienDoStr != null && tienDoStr.contains("%")) {
                    try { tienDo = Double.parseDouble(tienDoStr.replace("%", "").trim()); } catch (Exception ignored) {}
                }
                data.add(tienDo);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("data", data);
        return result;
    }

    // API trả về tiến độ tổng thể (dự án hoàn thành/tổng dự án) cho biểu đồ tròn tiến độ
    @GetMapping("/api/statistics/progress")
    public Map<String, Object> getProgressPie() {
        var duAnList = duAnRepository.getListDuAn();
        long completed = duAnList.stream().filter(da -> {
            String tienDoStr = da.getTienDo();
            double tienDo = 0;
            if (tienDoStr != null && tienDoStr.contains("%")) {
                try { tienDo = Double.parseDouble(tienDoStr.replace("%", "").trim()); } catch (Exception ignored) {}
            }
            return tienDo >= 100;
        }).count();
        long total = duAnList.size();
        Map<String, Object> result = new HashMap<>();
        result.put("completed", completed);
        result.put("notCompleted", total - completed);
        return result;
    }

    // API trả về thống kê người dùng
    @GetMapping("/api/statistics/users")
    public Map<String, Object> getUserStatistics() {
        List<NguoiDung> all = nguoiDungRepository.findAll();
        long total = all.size();
        long active = all.stream().filter(u -> u.getTrangThai() != null && u.getTrangThai().equalsIgnoreCase("đang học")).count();
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("active", active);
        result.put("inactive", total - active);
        return result;
    }

    // ... existing code ...
    // API trả về số lượng dự án theo từng loại (hoặc trạng thái)
    @GetMapping("/api/statistics/projects-list-bar")
    public Map<String, Object> getProjectsListBar() {
        List<?> duAnList = duAnRepository.getListDuAn();
        List<String> labels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        if (duAnList != null) {
            for (Object obj : duAnList) {
                try {
                    java.lang.reflect.Method getTenDuAn = obj.getClass().getMethod("getTenDuAn");
                    String tenDuAn = (String) getTenDuAn.invoke(obj);
                    labels.add(tenDuAn != null ? tenDuAn : "Không tên");
                    data.add(1);
                } catch (Exception e) {
                    labels.add("Không tên");
                    data.add(1);
                }
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("data", data);
        return result;
    }
// ... existing code ...
}