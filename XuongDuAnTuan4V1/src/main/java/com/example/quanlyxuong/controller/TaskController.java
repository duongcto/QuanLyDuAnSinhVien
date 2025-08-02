package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.entity.CongViec;
import com.example.quanlyxuong.entity.KanbanColumn;
import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.TaskMember;
import com.example.quanlyxuong.repository.CongViecRepository;
import com.example.quanlyxuong.repository.KanbanRepository;
import com.example.quanlyxuong.repository.NguoiDungRepository;
import com.example.quanlyxuong.repository.TaskMemberRepository;
import lombok.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TaskController {

    @Autowired
    TaskMemberRepository taskMemberRepository;

    @Autowired
    CongViecRepository congViecRepository ;


    @Autowired
    KanbanRepository kanbanRepository ;

    @Autowired
    NguoiDungRepository nguoiDungRepository ;



    @PostMapping("/api/updateDescription")
    public ResponseEntity<?> updateDescription(@RequestBody Map<String, String> data) {
        int id = Integer.parseInt(data.get("cardId"));
        String moTa = data.get("description");

        CongViec cv = congViecRepository.getReferenceById(id);
        cv.setMoTa(moTa);
        congViecRepository.save(cv);

        Map<String, String> res = new HashMap<>();
        res.put("message", "Mô tả đã được cập nhật!");
        return ResponseEntity.ok(res);
    }


    @PostMapping("/api/moveCard")
    @ResponseBody
    public ResponseEntity<String> moveCard(@RequestBody Map<String, String> payload) {
        Integer cardId = Integer.parseInt(payload.get("cardId"));
        Integer newColumnId = Integer.valueOf(payload.get("newColumnId"));
        int newPosition = Integer.parseInt(payload.get("newPosition"));
        CongViec movedCard = congViecRepository.getReferenceById(cardId);
        KanbanColumn newColumn = kanbanRepository.findById(newColumnId).orElseThrow();
        movedCard.setIdColumn(newColumn);
        List<CongViec> cards = congViecRepository.findByIdColumnOrderByPositionAsc(newColumn);
        cards.removeIf(cv -> cv.getId().equals(movedCard.getId()));
        cards.add(newPosition, movedCard);
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setPosition(i);
        }
        congViecRepository.saveAll(cards);

        return ResponseEntity.ok("Thẻ đã được di chuyển!");
    }
    @PostMapping("/api/updateColumnOrder")
    @ResponseBody
    public ResponseEntity<String> moveColumn(@RequestBody Map<String, String> payload) {
        int newOrder = Integer.parseInt(payload.get("newOrder"));
        System.out.println("Column new order: " + newOrder);
        return ResponseEntity.ok("Cột đã được di chuyển!");
    }


    @GetMapping("/hien_thi_task")
    public String hienThiTask(Model model) {
        List<KanbanColumn> kanbanColumns = kanbanRepository.findAll();
        List<NguoiDung> nguoiDung = nguoiDungRepository.findAll();


        Map<KanbanColumn, List<CongViec>> kanban = new LinkedHashMap<>();
        for (KanbanColumn col : kanbanColumns) {
            List<CongViec> congViecs = congViecRepository.findByIdColumnOrderByPositionAsc(col);
            kanban.put(col, congViecs);
        }
        model.addAttribute("kanban", kanban);
        model.addAttribute("nguoiDung", nguoiDung);

        return "hien_thi_task";
    }

    @PostMapping("/them_thanh_vien")
    public ResponseEntity<?> themThanhVien(@RequestBody Map<String, String> data) {
        int userId = Integer.parseInt(data.get("userId"));
        int idCv = Integer.parseInt(data.get("idCv"));
        String action = data.get("action");

        if ("add".equals(action)) {
            // thêm userId vào công việc idCv
            CongViec cv = congViecRepository.getReferenceById(idCv);
            NguoiDung nd = nguoiDungRepository.getReferenceById(userId);
            TaskMember tm = new TaskMember();
            tm.setNguoiDung(nd);
            tm.setCongViec(cv);
            taskMemberRepository.save(tm);

        } else if ("remove".equals(action)) {
            taskMemberRepository.deleteByUserIdAndTaskId(userId, idCv);
        }

        return ResponseEntity.ok(Map.of("status", "ok"));
    }




    @PostMapping("/addColumn")
    public String addColumn(@RequestParam("addColumn") String columnName, Model model) {
        List<KanbanColumn> kanbanColumns = kanbanRepository.findAll();
        KanbanColumn kb = new KanbanColumn();
        kb.setColumnName(columnName);
        kb.setColumnOrder(kanbanColumns.size()+1);

        kanbanRepository.save(kb);
        return "redirect:/hien_thi_task";
    }
    @GetMapping("/deleteColumn/{id}")
    public String deleteColumn(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        KanbanColumn kc = kanbanRepository.findById(id).get();
        if(kc.getCongViecs().isEmpty()){
            kanbanRepository.deleteById(id);
        }else{
            redirectAttributes.addFlashAttribute("errorMessage" , "Không thể xóa khi đang có công việc");
        }

        return "redirect:/hien_thi_task";
    }

    @GetMapping("/thanh_vien_da_phan_cong/{idCv}")
    @ResponseBody
    public List<Integer> getThanhVienByCongViec(@PathVariable("idCv") Integer idCv) {
        return taskMemberRepository.getUsersByTaskId(idCv)
                .stream()
                .map(NguoiDung::getId)
                .collect(Collectors.toList());
    }
    @GetMapping("/du_an/thanh_vien_da_phan_cong/{taskId}")
    @ResponseBody
    public List<DTONguoiDung> getThanhVienCongViec(@PathVariable Integer taskId) {
        List<NguoiDung> list = taskMemberRepository.getUsersByTaskId(taskId);
        return list.stream()
                .map(nd -> new DTONguoiDung(nd.getHoTen(), nd.getEmailPt()))
                .toList();
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class DTONguoiDung {
        private String  hoTen;
        private String emailPt;

    }


    @GetMapping("/du_an/han_cua_cong_viec/{taskId}")
    @ResponseBody
    public String getDueDate(@PathVariable Integer taskId) {
        CongViec cv = congViecRepository.getReferenceById(taskId);
        if (cv.getNgayHetHan()!= null) {
            // Format sang chuỗi ngày nếu cần
            return cv.getNgayHetHan().toString(); // Hoặc dùng định dạng yyyy-MM-dd
        }
        return ""; // Không có hạ
    }

    @PostMapping("/api/save-date")
    @ResponseBody
    public ResponseEntity<?> saveDate(@RequestBody DateRequest req) throws ParseException {
        CongViec cv = congViecRepository.getReferenceById(req.cardId);

        if(req.startDate == null && req.dueDate == null){
            String message = "Vui chọn ngày giờ";
        } else if (req.startDate != null && req.dueDate != null) {
            cv.setId(req.cardId);
            cv.setNgayBatDau(getLocalDate(String.valueOf(req.startDate)));
            cv.setNgayHetHan(getLocalDate(String.valueOf(req.dueDate)));
            congViecRepository.save(cv);
        }else if (req.startDate == null) {
            cv.setId(req.cardId);
            cv.setNgayHetHan(getLocalDate(String.valueOf(req.dueDate)));
            congViecRepository.save(cv);
        }else  {
            cv.setId(req.cardId);
            cv.setNgayBatDau(getLocalDate(String.valueOf(req.startDate)));
            congViecRepository.save(cv);
        }

        Map<String, String> map = new HashMap<>();
        map.put("message", "Đã lưu thành công");
        map.put("message1", "Vui chọn ngày giờ");


        return ResponseEntity.ok(map);
    }

    @Data
    public static class DateRequest {
        private Integer cardId;
        private Date startDate;
        private Date dueDate;
    }

    public LocalDate getLocalDate(String dateStr) throws ParseException {
        // Format tương ứng với chuỗi
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", java.util.Locale.ENGLISH);
        Date date = sdf.parse(dateStr);

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }



    @PostMapping("/api/updateCardPriority")
    @ResponseBody
    public ResponseEntity<?> updateCardPriority(@RequestBody Priority priority) {
        int cardId = priority.getCardId();
        String prioritys = priority.getPriority();
        CongViec cv = congViecRepository.getReferenceById(cardId);
        cv.setDoUuTien(prioritys);
        congViecRepository.save(cv);


        Map<String, Object> response = new HashMap<>();
        response.put("cardId", cardId);
        response.put("priority", priority);
        return ResponseEntity.ok(response);
    }


    @Data
    public static class Priority {
        private Integer cardId;
        private String priority;
    }

    @GetMapping("/api/getCardPriority/{cardId}")
    @ResponseBody
    public ResponseEntity<?> getCardPriority(@PathVariable Integer cardId) {
        CongViec cv = congViecRepository.getReferenceById(cardId);


        Map<String, Object> response = new HashMap<>();
        response.put("cardId", cardId);
        response.put("priority", cv.getDoUuTien());
        return ResponseEntity.ok(response);
    }




}
