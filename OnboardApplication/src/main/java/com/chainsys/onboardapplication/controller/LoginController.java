package com.chainsys.onboardapplication.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;


//import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
	
	@GetMapping("/home")
    public String homePage() {
        return "Login.jsp";
    }
	

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session) {
        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
        try {
            @SuppressWarnings("deprecation")
			String role = jdbcTemplate.queryForObject(sql, new Object[]{username, password}, String.class);

            if (role != null) {
                session.setAttribute("username", username);
                session.setAttribute("role", role);

                switch (role) {
                    case "manager":
                        return "redirect:/managerdashboard";
                    case "hr":
                        return "/hrdashboard";
                    case "employee":
                        return "redirect:/employeedashboard";
                    default:
                        return "redirect:/login?error=Invalid role";
                }
            } else {
                return "redirect:/login?error=Invalid username or password";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/login?error=Database error occurred";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "Login.jsp";
    }
    
    @PostMapping("/logout")
    public String logoutPost(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}
