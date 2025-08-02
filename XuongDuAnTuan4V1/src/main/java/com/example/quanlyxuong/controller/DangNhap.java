package com.example.quanlyxuong.controller;

import com.example.quanlyxuong.entity.NguoiDung;
import com.example.quanlyxuong.entity.VaiTro;
import com.example.quanlyxuong.repository.NguoiDungRepository;
import com.example.quanlyxuong.repository.VaiTroRepository;
import com.example.quanlyxuong.service.DangNhapService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class DangNhap {
    @Autowired
    private DangNhapService danhNhapService;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    @Autowired
    private DangNhapService dangNhapService;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @GetMapping("/dangNhap")
    public String dangNhap(){
        return "Admin/DangNhap";
    }
    @GetMapping("/dangNhapQuanLy")
    public String dangNhapQuanLy(){
        return "QuanLy/DangNhapQuanLy";
    }
    @PostMapping("/login")
    public String login(@RequestParam String tenDangNhap,
                        @RequestParam String matKhau,
                        @RequestParam String emailFe,
                        @RequestParam String emailPt,
                        HttpSession session,
                        Model model, RedirectAttributes redirectAttributes) {
        NguoiDung nguoidung = dangNhapService.dangNhap(tenDangNhap, matKhau, emailFe, emailPt);
        if(nguoidung != null) {
            session.setAttribute("nguoidung", nguoidung);
            if(nguoidung.getIdVaiTro().getTenVaiTro().equalsIgnoreCase("Admin")){
                return "Admin/Home";
            }else if(nguoidung.getIdVaiTro().getTenVaiTro().equalsIgnoreCase("Quản Lý")){
                return "QuanLy/HomeQuanLy";
            }else{
                return "ThanhVien/HomeThanhVien";
            }
        }
        redirectAttributes.addFlashAttribute("message","Đăng nhập thất bại, vui lòng đăng nhập lại!");
        return "redirect:/dangNhap";
    }
//    @GetMapping("/dangXuat")
//    public String dangXuat(Model model) {
//        model.addAttribute("type","Đăng xuất thành công!");
//        return "View/Security/dangNhap";
//    }

    // Danh sách các email được phép có vai trò QuanLy (có thể quản lý ở nơi khác tốt hơn)
    private static final List<String> ADMIN_EMAILS = Arrays.asList(
            "truonglsph348958@gmail.com" // Thay bằng email quản lý thực tế
    );

    private static final List<String> QUANLY_EMAILS = Arrays.asList(
            "truongsivar@gmail.com"
    );

    @GetMapping("/singinGoogle")
    public String singinGoogle(OAuth2AuthenticationToken authentication, HttpSession session) {
        Map<String, Object> attributes = authentication.getPrincipal().getAttributes();

        String tenDangNhap = (String) attributes.get("name");
        String email = (String) attributes.get("email");
        String avatar = (String) attributes.get("picture");

        System.out.println("Tên: " + tenDangNhap);
        System.out.println("Email: " + email);

        session.setAttribute("tenDangNhap",tenDangNhap);
        session.setAttribute("email",email);
        session.setAttribute("avatar",avatar);

        NguoiDung currentUser = nguoiDungRepository.findByEmailFeOrEmailPt(email, email).orElseGet(() -> {
            NguoiDung newUser = new NguoiDung();
            newUser.setTenDangNhap(tenDangNhap);
            newUser.setEmailFe(email);
            newUser.setEmailPt(email);

            // --- Logic xác định vai trò khi người dùng mới được tạo ---
            String roleName;
            if (ADMIN_EMAILS.contains(email)) {
                roleName = "Admin";
            } else if (QUANLY_EMAILS.contains(email)) {
                roleName = "Quản Lý";
            } else {
                roleName = "Thành viên"; // Vai trò mặc định cho các email khác
            }
            // --- Kết thúc logic xác định vai trò ---

            // Tìm hoặc tạo đối tượng VaiTro
            VaiTro vaiTro = vaiTroRepository.findByTenVaiTro(roleName)
                    .orElseGet(() -> vaiTroRepository.save(new VaiTro()));
            newUser.setIdVaiTro(vaiTro); // Gán đối tượng VaiTro
            return nguoiDungRepository.save(newUser);
        });

        // Bước 2: Kiểm tra vai trò của người dùng đã đăng nhập và chuyển hướng
        if (currentUser.getIdVaiTro() != null) {
            String tenVaiTro = currentUser.getIdVaiTro().getTenVaiTro();
            if ("Admin".equals(tenVaiTro)) {
                System.out.println("Người dùng " + tenDangNhap + " có vai trò Admin. Chuyển hướng đến trang Admin.");
                return "redirect:/homeAdmin"; // Trang chủ cho Admin
            } else if ("Quản Lý".equals(tenVaiTro)) {
                System.out.println("Người dùng " + tenDangNhap + " có vai trò Quản lý. Chuyển hướng đến trang Quản lý.");
                return "redirect:/homeQuanLy"; // Trang chủ cho Quản lý
            } else if ("Thành viên".equals(tenVaiTro)) { // Giả sử "Thành viên" là tên vai trò cho sinh viên
                System.out.println("Người dùng " + tenDangNhap + " có vai trò Thành viên. Chuyển hướng đến trang Thành viên.");
                return "redirect:/homeThanhVien"; // Trang chủ cho Thành viên (sinh viên)
            }
        }

        // Trường hợp fallback: Nếu không xác định được vai trò, chuyển hướng đến trang mặc định
        System.out.println("Không xác định được vai trò, chuyển hướng đến trang mặc định (Thành viên).");
        return "redirect:/homeThanhVien";
    }
}
