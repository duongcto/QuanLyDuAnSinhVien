package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.entity.CongViec;
import com.example.quanlyxuong.entity.KanbanColumn;
import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.TaskMember;
import com.example.quanlyxuong.repository.KanbanRepository;
import com.example.quanlyxuong.service.CongViecExcelService;
import com.example.quanlyxuong.service.CongViecService;
import com.example.quanlyxuong.service.DanhSachCongViecService;
import com.example.quanlyxuong.service.GiaiDoanService;
import com.example.quanlyxuong.entity.Comment;
import com.example.quanlyxuong.repository.CommentRepository;
import com.example.quanlyxuong.dto.CommentDTO;
import com.example.quanlyxuong.entity.DanhSachCongViec;
import com.example.quanlyxuong.dto.LabelDto;
import com.example.quanlyxuong.dto.MemberDto;
import com.example.quanlyxuong.service.NguoiDungService;
import com.example.quanlyxuong.service.TaskMemberService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/congviec")
@Controller
public class CongViecContro {
    @Autowired
    private CongViecService congViecService;

    @Autowired
    private GiaiDoanService giaoDoanService;

    @Autowired
    private DanhSachCongViecService danhSachCongViecService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private KanbanRepository kanbanRepository;

    @Autowired
    private TaskMemberService taskMemberService;

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private CongViecExcelService congViecExcelService;

    @GetMapping // Giả sử đây là endpoint của bạn
    public String getTheLoaiPage(@RequestParam(defaultValue = "0") Optional<Integer> page, // Mặc định là 0 nếu không có
                                 @RequestParam(defaultValue = "5") Optional<Integer> size, // Mặc định là 10 nếu không có
                                 Model model) {

        int currentPage = page.orElse(0); // Lấy giá trị hoặc mặc định là 0
        int pageSize = size.orElse(5);  // Lấy giá trị hoặc mặc định là 5

        // Giả sử bạn có một service để lấy dữ liệu phân trang
        Page<CongViec> theLoaiPage = congViecService.findAll(PageRequest.of(currentPage, pageSize));

        int totalPages = theLoaiPage.getTotalPages();
        if (totalPages == 0) { // Đảm bảo totalPages không âm hoặc 0 nếu không có dữ liệu
            totalPages = 1; // Đặt ít nhất là 1 trang để tránh lỗi khi sequence(0, -1)
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("congViec", theLoaiPage.getContent());
        model.addAttribute("giaiDoan", giaoDoanService.getAll());
        model.addAttribute("danhSachCongViec", danhSachCongViecService.getAll());// Dữ liệu của trang hiện tại
        if (!model.containsAttribute("congViecnew")) {
            model.addAttribute("congViecnew", new CongViec());
        }
        return "QuanLy/congviec";
    }

    @GetMapping ("/thanhvien")// Giả sử đây là endpoint của bạn
    public String getThanhVienTheLoaiPage(@RequestParam(defaultValue = "0") Optional<Integer> page, // Mặc định là 0 nếu không có
                                          @RequestParam(defaultValue = "5") Optional<Integer> size,
                                          @RequestParam(name = "keyword", required = false) String keyword,// Mặc định là 10 nếu không có
                                          Model model) {

        int currentPage = page.orElse(0); // Lấy giá trị hoặc mặc định là 0
        int pageSize = size.orElse(5);  // Lấy giá trị hoặc mặc định là 5

        // Giả sử bạn có một service để lấy dữ liệu phân trang
        Page<CongViec> theLoaiPage = congViecService.findAll(PageRequest.of(currentPage, pageSize));

        if (keyword != null && !keyword.trim().isEmpty()) {
            theLoaiPage = congViecService.searchByTenCongViec(keyword, PageRequest.of(currentPage, pageSize));
            model.addAttribute("keyword", keyword); // giữ lại từ khóa trên input
        } else {
            theLoaiPage = congViecService.findAll(PageRequest.of(currentPage, pageSize));
        }

        int totalPages = theLoaiPage.getTotalPages();
        if (totalPages == 0) { // Đảm bảo totalPages không âm hoặc 0 nếu không có dữ liệu
            totalPages = 1; // Đặt ít nhất là 1 trang để tránh lỗi khi sequence(0, -1)
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("congViec", theLoaiPage.getContent());
        model.addAttribute("giaiDoan", giaoDoanService.getAll());
        model.addAttribute("danhSachCongViec", danhSachCongViecService.getAll());// Dữ liệu của trang hiện tại
        if (!model.containsAttribute("congViecnew")) {
            model.addAttribute("congViecnew", new CongViec());
        }
        return "ThanhVien/congviec";
    }

    @PostMapping("/addCongViec")
    public String addCongViec(@ModelAttribute CongViec congViec, RedirectAttributes redirectAttributes) {
        if (congViec.getTenCongViec() == null || congViec.getTenCongViec().trim().isEmpty()) {
            congViec.setTenCongViec("Không có tiêu đề");
        } else {
            congViec.setTenCongViec(congViec.getTenCongViec().trim());
        }
        congViec.setTrangThai(true);
        congViec.setNgayTao(Instant.now());
        congViec.setNgayUpdate(LocalDate.now());
        if (congViec.getStatus() == null || congViec.getStatus().isEmpty()) {
            congViec.setStatus("TODO");

        }
        List<KanbanColumn> ak = kanbanRepository.findAll();
        KanbanColumn kanbanColumn =kanbanRepository.getReferenceById(ak.get(0).getId()) ;
        congViec.setIdColumn(kanbanColumn);

        congViecService.save(congViec);
        redirectAttributes.addFlashAttribute("message", "Thêm công việc thành công!");
        return "redirect:/congviec";
    }

    @GetMapping("/delete/{id}")
    public String deleteCongViec(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            congViecService.deleteById(id); // Xóa công việc bằng ID
            redirectAttributes.addFlashAttribute("message", "Xóa công việc thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa công việc: " + e.getMessage());
        }
        return "redirect:/congviec";
    }

    @PostMapping("/update/{id}")
    @ResponseBody // Rất quan trọng! Để trả về dữ liệu (JSON) thay vì tên một view.
    public ResponseEntity<?> updateCongViecName(
            @PathVariable("id") Integer id,
            @RequestBody Map<String, String> payload) { // Nhận dữ liệu JSON từ AJAX

        try {
            String newName = payload.get("tenCongViec");
            if (newName == null || newName.trim().isEmpty()) {
                // Trả về lỗi nếu tên mới rỗng
                return ResponseEntity.badRequest().body("Tên công việc không được để trống.");
            }

            // Gọi service để cập nhật tên công việc trong database
            congViecService.updateTenCongViec(id, newName);

            // Trả về một thông báo thành công.
            // .ok() tương đương với status 200 (OK)
            return ResponseEntity.ok().body(Map.of("message", "Cập nhật thành công!"));

        } catch (Exception e) {
            // Nếu có lỗi xảy ra (ví dụ: không tìm thấy công việc với id đó),
            // trả về lỗi 500 (Internal Server Error)
            return ResponseEntity.internalServerError().body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @PostMapping("/import")
    public String importCongViec(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            congViecExcelService.importCongViecFromExcel(file);
            redirectAttributes.addFlashAttribute("message", "Import công việc thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Import công việc thất bại: " + e.getMessage());
        }
        return "redirect:/congviec";
    }

    @GetMapping("/download-template")
    public void downloadTemplate(HttpServletResponse response) {
        try {
            congViecExcelService.exportCongViecTemplate(response);
        } catch (Exception e) {
            // Xử lý lỗi nếu cần
            e.printStackTrace();
        }
    }

    @GetMapping("/history")
    public String viewImportHistory(Model model) {
        // TODO: Implement logic to fetch and display import history
        model.addAttribute("message", "Chức năng lịch sử đang được phát triển.");
        return "QuanLy/congviec-history"; // Tạo một view mới cho lịch sử nếu cần
    }

    @GetMapping("/download-import-errors")
    public void downloadImportErrors(HttpServletResponse response) {
        try {
            congViecExcelService.exportImportErrors(response);
        } catch (Exception e) {
            // Xử lý lỗi nếu cần
            e.printStackTrace();
        }
    }

    @GetMapping("/chon-thanh-vien/{id}")
    public String chonThanhVien(@PathVariable("id") Integer id, Model model) {
        CongViec congViec = congViecService.findById(id);
        model.addAttribute("congViec", congViec);

        List<NguoiDung> thanhVienList = nguoiDungService.findAllNguoiDung();
        model.addAttribute("thanhVienList", thanhVienList);

        // ✅ Danh sách thành viên đã được gán
        List<TaskMember> daChon = taskMemberService.getByTaskId(id);
        List<Integer> selectedIds = daChon.stream()
                .map(tm -> tm.getNguoiDung().getId())
                .collect(Collectors.toList());

        model.addAttribute("selectedIds", selectedIds);
        model.addAttribute("taskId", id);

        return "ThanhVien/modal-chon-thanh-vien";
    }




    @PostMapping("/luu-thanh-vien/{taskId}")
    public String luuThanhVien(@PathVariable("taskId") Integer taskId,
                               @RequestParam(value = "selectedMembers", required = false) List<Integer> memberIds,
                               RedirectAttributes redirectAttributes) {

        CongViec task = congViecService.findById(taskId);

        // Xóa cũ
        List<TaskMember> oldMembers = taskMemberService.getByTaskId(taskId);
        for (TaskMember tm : oldMembers) {
            taskMemberService.delete(tm.getId());
        }

        // Lưu mới nếu có chọn
        if (memberIds != null) {
            for (Integer uid : memberIds) {
                TaskMember tm = new TaskMember();
                tm.setCongViec(task);
                tm.setNguoiDung(nguoiDungService.findById(uid));
                taskMemberService.save(tm);
            }

            // Gán chuỗi ID
            String ids = memberIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            task.setThanhVien(ids);
        } else {
            task.setThanhVien(""); // Không chọn thì xóa hết
        }

        congViecService.save(task);

        redirectAttributes.addFlashAttribute("message", "Cập nhật thành viên thành công!");
        return "redirect:/congviec/thanhvien";
    }

    @GetMapping("/chon-thanh-vien-CV/{id}")
    public String chonThanhVienCV(@PathVariable("id") Integer id, Model model) {
        CongViec congViec = congViecService.findById(id);
        model.addAttribute("congViec", congViec);

        List<NguoiDung> thanhVienList = nguoiDungService.findAllNguoiDung();
        model.addAttribute("thanhVienList", thanhVienList);

        // ✅ Danh sách thành viên đã được gán
        List<TaskMember> daChon = taskMemberService.getByTaskId(id);
        List<Integer> selectedIds = daChon.stream()
                .map(tm -> tm.getNguoiDung().getId())
                .collect(Collectors.toList());

        model.addAttribute("selectedIds", selectedIds);
        model.addAttribute("taskId", id);

        return "QuanLy/modal-chon-thanh-vien";
    }


    @PostMapping("/luu-thanh-vien-CV/{taskId}")
    public String luuThanhVienCV(@PathVariable("taskId") Integer taskId,
                               @RequestParam(value = "selectedMembers", required = false) List<Integer> memberIds,
                               RedirectAttributes redirectAttributes) {

        CongViec task = congViecService.findById(taskId);

        // Xóa cũ
        List<TaskMember> oldMembers = taskMemberService.getByTaskId(taskId);
        for (TaskMember tm : oldMembers) {
            taskMemberService.delete(tm.getId());
        }

        // Lưu mới nếu có chọn
        if (memberIds != null) {
            for (Integer uid : memberIds) {
                TaskMember tm = new TaskMember();
                tm.setCongViec(task);
                tm.setNguoiDung(nguoiDungService.findById(uid));
                taskMemberService.save(tm);
            }

            // Gán chuỗi ID
            String ids = memberIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            task.setThanhVien(ids);
        } else {
            task.setThanhVien(""); // Không chọn thì xóa hết
        }

        congViecService.saveCV(task);

        redirectAttributes.addFlashAttribute("message", "Cập nhật thành viên thành công!");
        return "redirect:/congviec";
    }

    // DTO cho Kanban task
    class TaskKanbanDto {
        public Integer id;
        public String tenCongViec;
        public String status;
        public String moTa;
        public java.time.LocalDate ngayHetHan;
        public java.util.List<LabelDto> labels;
        public java.util.List<MemberDto> members;
        public TaskKanbanDto(Integer id, String tenCongViec, String status, String moTa, java.time.LocalDate ngayHetHan, java.util.List<LabelDto> labels, java.util.List<MemberDto> members) {
            this.id = id;
            this.tenCongViec = tenCongViec;
            this.status = status;
            this.moTa = moTa;
            this.ngayHetHan = ngayHetHan;
            this.labels = labels;
            this.members = members;
        }
    }

    @RestController
    @RequestMapping("congviec/api/kanban/tasks")
    public class KanbanTaskRestController {
        @Autowired
        private CongViecService congViecService;
        @Autowired
        private com.example.quanlyxuong.service.TaskLabelService taskLabelService;
        @Autowired
        private com.example.quanlyxuong.service.TaskMemberService taskMemberService;
        @Autowired
        private com.example.quanlyxuong.service.LabelService labelService;
        @Autowired
        private com.example.quanlyxuong.service.NguoiDungService nguoiDungService;

        @GetMapping
        public java.util.List<TaskKanbanDto> getAllTasks() {
            try {
                java.util.List<CongViec> tasks = congViecService.getAll();
                java.util.List<TaskKanbanDto> result = new java.util.ArrayList<>();
                for (CongViec t : tasks) {
                    if (t.getIdDanhSachCongViec() == null) continue;
                    java.util.List<LabelDto> labels = new java.util.ArrayList<>();
                    java.util.List<com.example.quanlyxuong.entity.TaskLabel> taskLabels = taskLabelService.getByTaskId(t.getId());
                    if (taskLabels != null) {
                        for (var tl : taskLabels) {
                            if (tl != null && tl.getLabel() != null) {
                                var l = tl.getLabel();
                                labels.add(new LabelDto(l.getId(), l.getName(), l.getColor()));
                            }
                        }
                    }
                    java.util.List<MemberDto> members = new java.util.ArrayList<>();
                    java.util.List<com.example.quanlyxuong.entity.TaskMember> taskMembers = taskMemberService.getByTaskId(t.getId());
                    if (taskMembers != null) {
                        for (var tm : taskMembers) {
                            if (tm != null && tm.getNguoiDung() != null) {
                                var u = tm.getNguoiDung();
                                members.add(new MemberDto(u.getId(), u.getHoTen(), u.getIdVaiTro() != null ? u.getIdVaiTro().getTenVaiTro() : null));
                            }
                        }
                    }
                    result.add(new TaskKanbanDto(
                        t.getId(),
                        t.getTenCongViec(),
                        t.getStatus(),
                        t.getMoTa(),
                        t.getNgayHetHan(),
                        labels,
                        members
                    ));
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return java.util.Collections.emptyList();
            }
        }

        @PostMapping
        public CongViec addTask(@RequestBody CongViec task) {
            task.setId(null);
            if (task.getStatus() == null) task.setStatus("TODO");
            // Nếu chưa có idDanhSachCongViec thì gán mặc định là danh sách đầu tiên
            if (task.getIdDanhSachCongViec() == null) {
                List<DanhSachCongViec> ds = danhSachCongViecService.getAll();
                if (!ds.isEmpty()) {
                    task.setIdDanhSachCongViec(ds.get(0));
                } else {
                    // Nếu không có danh sách nào thì trả về lỗi rõ ràng
                    throw new RuntimeException("Không tồn tại danh sách công việc nào để gán cho task!");
                }
            }
            congViecService.save(task);
            return task;
        }

        @PutMapping("/{id}")
        public CongViec updateTask(@PathVariable Integer id, @RequestBody CongViec task) {
            CongViec existing = congViecService.findById(id);
            if (existing != null) {
                if (task.getTenCongViec() != null) existing.setTenCongViec(task.getTenCongViec());
                if (task.getStatus() != null) existing.setStatus(task.getStatus());
                if (task.getMoTa() != null) existing.setMoTa(task.getMoTa());
                if (task.getNgayHetHan() != null) existing.setNgayHetHan(task.getNgayHetHan());
                if (task.getThanhVien() != null) existing.setThanhVien(task.getThanhVien());
                congViecService.save(existing);
            }
            return existing;
        }

        @DeleteMapping("/{id}")
        public void deleteTask(@PathVariable Integer id) {
            congViecService.deleteById(id);
        }

        @PutMapping("/{id}/status")
        public TaskKanbanDto updateTaskStatus(@PathVariable Integer id, @RequestBody String status) {
            CongViec existing = congViecService.findById(id);
            if (existing != null) {
                String cleanStatus = status.replaceAll("\"", "");
                existing.setStatus(cleanStatus);
                if ("DONE".equals(cleanStatus)) {
                    existing.setTrangThai(false);
                } else {
                    existing.setTrangThai(true);
                }
                congViecService.save(existing);
                java.util.List<LabelDto> labels = new java.util.ArrayList<>();
                java.util.List<com.example.quanlyxuong.entity.TaskLabel> taskLabels = taskLabelService.getByTaskId(existing.getId());
                if (taskLabels != null) {
                    for (var tl : taskLabels) {
                        if (tl != null && tl.getLabel() != null) {
                            var l = tl.getLabel();
                            labels.add(new LabelDto(l.getId(), l.getName(), l.getColor()));
                        }
                    }
                }
                java.util.List<MemberDto> members = new java.util.ArrayList<>();
                java.util.List<com.example.quanlyxuong.entity.TaskMember> taskMembers = taskMemberService.getByTaskId(existing.getId());
                if (taskMembers != null) {
                    for (var tm : taskMembers) {
                        if (tm != null && tm.getNguoiDung() != null) {
                            var u = tm.getNguoiDung();
                            members.add(new MemberDto(u.getId(), u.getHoTen(), u.getIdVaiTro() != null ? u.getIdVaiTro().getTenVaiTro() : null));
                        }
                    }
                }
                return new TaskKanbanDto(
                    existing.getId(),
                    existing.getTenCongViec(),
                    existing.getStatus(),
                    existing.getMoTa(),
                    existing.getNgayHetHan(),
                    labels,
                    members
                );
            }
            return null;
        }

        @GetMapping("/{id}")
        public TaskKanbanDto getTask(@PathVariable Integer id) {
            CongViec t = congViecService.findById(id);
            if (t == null || t.getIdDanhSachCongViec() == null) return null;
            java.util.List<LabelDto> labels = new java.util.ArrayList<>();
            java.util.List<com.example.quanlyxuong.entity.TaskLabel> taskLabels = taskLabelService.getByTaskId(t.getId());
            if (taskLabels != null) {
                for (var tl : taskLabels) {
                    if (tl != null && tl.getLabel() != null) {
                        var l = tl.getLabel();
                        labels.add(new LabelDto(l.getId(), l.getName(), l.getColor()));
                    }
                }
            }
            java.util.List<MemberDto> members = new java.util.ArrayList<>();
            java.util.List<com.example.quanlyxuong.entity.TaskMember> taskMembers = taskMemberService.getByTaskId(t.getId());
            if (taskMembers != null) {
                for (var tm : taskMembers) {
                    if (tm != null && tm.getNguoiDung() != null) {
                        var u = tm.getNguoiDung();
                        members.add(new MemberDto(u.getId(), u.getHoTen(), u.getIdVaiTro() != null ? u.getIdVaiTro().getTenVaiTro() : null));
                    }
                }
            }
            return new TaskKanbanDto(
                t.getId(),
                t.getTenCongViec(),
                t.getStatus(),
                t.getMoTa(),
                t.getNgayHetHan(),
                labels,
                members
            );
        }

        // API lấy danh sách bình luận của task (trả về DTO)
        @GetMapping("/{id}/comments")
        public ResponseEntity<?> getComments(@PathVariable Integer id) {
            try {
                List<CommentDTO> comments = commentRepository.findByCongViec_Id(id)
                    .stream()
                    .map(c -> new CommentDTO(
                        c.getId(),
                        c.getNoiDung(),
                        c.getNgayBinhLuan(),
                        c.getIdNguoiDung() != null ? c.getIdNguoiDung().getHoTen() : "Ẩn danh"
                    ))
                    .collect(Collectors.toList());
                return ResponseEntity.ok(comments);
            } catch (Exception e) {
                e.printStackTrace(); // log lỗi
                return ResponseEntity.ok(new java.util.ArrayList<>()); // Trả về mảng rỗng thay vì lỗi 500
            }
        }

        // API thêm bình luận cho task
        @PostMapping("/{id}/comments")
        public Comment addComment(@PathVariable Integer id, @RequestBody Comment comment) {
            CongViec task = congViecService.findById(id);
            comment.setCongViec(task);
            comment.setNgayBinhLuan(java.time.LocalDate.now());
            return commentRepository.save(comment);
        }

        // API cập nhật thành viên cho task
        @PutMapping("/{id}/members")
        public TaskKanbanDto updateTaskMembers(@PathVariable Integer id, @RequestBody java.util.List<Integer> memberIds) {
            CongViec task = congViecService.findById(id);
            if (task != null) {
                // Xóa hết các thành viên cũ của task này trong bảng task_member
                java.util.List<com.example.quanlyxuong.entity.TaskMember> oldMembers = taskMemberService.getByTaskId(id);
                for (var tm : oldMembers) {
                    taskMemberService.delete(tm.getId());
                }
                // Thêm lại các thành viên mới
                for (Integer uid : memberIds) {
                    com.example.quanlyxuong.entity.TaskMember tm = new com.example.quanlyxuong.entity.TaskMember();
                    tm.setCongViec(task);
                    tm.setNguoiDung(nguoiDungService.findById(uid));
                    taskMemberService.save(tm);
                }
                // Lưu danh sách id thành viên dạng chuỗi, ví dụ: "1,2,3" (nếu cần cho modal)
                String ids = memberIds.stream().map(String::valueOf).collect(java.util.stream.Collectors.joining(","));
                task.setThanhVien(ids);
                congViecService.save(task);
                // Trả về DTO
                java.util.List<LabelDto> labels = new java.util.ArrayList<>();
                java.util.List<com.example.quanlyxuong.entity.TaskLabel> taskLabels = taskLabelService.getByTaskId(task.getId());
                if (taskLabels != null) {
                    for (var tl : taskLabels) {
                        if (tl != null && tl.getLabel() != null) {
                            var l = tl.getLabel();
                            labels.add(new LabelDto(l.getId(), l.getName(), l.getColor()));
                        }
                    }
                }
                java.util.List<MemberDto> members = new java.util.ArrayList<>();
                java.util.List<com.example.quanlyxuong.entity.TaskMember> taskMembers = taskMemberService.getByTaskId(task.getId());
                if (taskMembers != null) {
                    for (var tm : taskMembers) {
                        if (tm != null && tm.getNguoiDung() != null) {
                            var u = tm.getNguoiDung();
                            members.add(new MemberDto(u.getId(), u.getHoTen(), u.getIdVaiTro() != null ? u.getIdVaiTro().getTenVaiTro() : null));
                        }
                    }
                }
                return new TaskKanbanDto(
                    task.getId(),
                    task.getTenCongViec(),
                    task.getStatus(),
                    task.getMoTa(),
                    task.getNgayHetHan(),
                    labels,
                    members
                );
            }
            return null;
        }

        // API cập nhật nhãn cho task
        @PutMapping("/{id}/labels")
        public TaskKanbanDto updateTaskLabels(@PathVariable Integer id, @RequestBody java.util.List<Integer> labelIds) {
            CongViec task = congViecService.findById(id);
            if (task != null) {
                // Xóa hết các nhãn cũ của task này trong bảng task_label
                java.util.List<com.example.quanlyxuong.entity.TaskLabel> oldLabels = taskLabelService.getByTaskId(id);
                for (var tl : oldLabels) {
                    taskLabelService.delete(tl.getId());
                }
                // Thêm lại các nhãn mới
                for (Integer lid : labelIds) {
                    com.example.quanlyxuong.entity.TaskLabel tl = new com.example.quanlyxuong.entity.TaskLabel();
                    tl.setTask(task);
                    var labelOpt = labelService.getById(lid);
                    labelOpt.ifPresent(tl::setLabel);
                    taskLabelService.save(tl);
                }
                // Trả về DTO chỉ gồm id, tên, màu nhãn, không trả về entity lồng nhau
                java.util.List<LabelDto> labels = new java.util.ArrayList<>();
                java.util.List<com.example.quanlyxuong.entity.TaskLabel> taskLabels = taskLabelService.getByTaskId(task.getId());
                if (taskLabels != null) {
                    for (var tl : taskLabels) {
                        if (tl != null && tl.getLabel() != null) {
                            var l = tl.getLabel();
                            labels.add(new LabelDto(l.getId(), l.getName(), l.getColor()));
                        }
                    }
                }
                java.util.List<MemberDto> members = new java.util.ArrayList<>();
                java.util.List<com.example.quanlyxuong.entity.TaskMember> taskMembers = taskMemberService.getByTaskId(task.getId());
                if (taskMembers != null) {
                    for (var tm : taskMembers) {
                        if (tm != null && tm.getNguoiDung() != null) {
                            var u = tm.getNguoiDung();
                            members.add(new MemberDto(u.getId(), u.getHoTen(), u.getIdVaiTro() != null ? u.getIdVaiTro().getTenVaiTro() : null));
                        }
                    }
                }
                return new TaskKanbanDto(
                    task.getId(),
                    task.getTenCongViec(),
                    task.getStatus(),
                    task.getMoTa(),
                    task.getNgayHetHan(),
                    labels,
                    members
                );
            }
            return null;
        }
    }
}
