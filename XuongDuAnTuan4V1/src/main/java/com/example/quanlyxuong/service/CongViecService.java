package com.example.quanlyxuong.service;

import com.example.quanlyxuong.entity.CongViec;
import com.example.quanlyxuong.repository.CongViecRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CongViecService {
    @Autowired
    private CongViecRepository congViecRepository;

    public List<CongViec> getAll() {
        return congViecRepository.findAll();
    }

    public void save(CongViec congViec) {
        congViecRepository.save(congViec);
    }

    public Page<CongViec> findAll(PageRequest pageable) {
        Page<CongViec> congViecPage = congViecRepository.findAll(pageable);
        return congViecPage;
    }

    public void deleteById(Integer id) {
        congViecRepository.deleteById(id);
    }

    public CongViec findById(Integer id) {
        return congViecRepository.findById(id).get();
    }

    public Page<CongViec> searchByTenCongViec(String keyword, PageRequest pageable) {
        return congViecRepository.findByTenCongViecContainingIgnoreCase(keyword, pageable);
    }

    @Transactional
    public void updateTenCongViec(Integer id, String newName) {
        // Tìm công việc theo ID, nếu không thấy sẽ ném ra exception
        CongViec congViec = congViecRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy công việc với ID: " + id));

        // Cập nhật tên và lưu lại
        congViec.setTenCongViec(newName);
        congViec.setNgayUpdate(LocalDate.now()); // Cập nhật cả ngày update
        congViecRepository.save(congViec);
    }

    public void saveTV(CongViec t) {
        congViecRepository.save(t);
    }

    public CongViec findByTaskId(Integer taskIds) {
        return congViecRepository.findById(taskIds).get();
    }

    public void saveCV(CongViec task) {
        congViecRepository.save(task);
    }
}
