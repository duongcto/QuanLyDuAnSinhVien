package com.example.quanlyxuong.repository;

import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.TaskMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TaskMemberRepository extends JpaRepository<TaskMember, Integer> {
    
    @Query("SELECT tm.nguoiDung FROM TaskMember tm WHERE tm.congViec.id = :taskId")
    List<NguoiDung> getUsersByTaskId(@Param("taskId") Integer taskId);

    @Modifying
    @Transactional
    @Query("DELETE FROM TaskMember tm WHERE tm.nguoiDung.id = :userId AND tm.congViec.id = :taskId")
    void deleteByUserIdAndTaskId(@Param("userId") Integer userId, @Param("taskId") Integer taskId);

} 