package com.chainsys.onboardapplication.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

//import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@GetMapping("/home")
    public String homePage() {
        return "redirect:/Login.jsp";
    }
	

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session) {
        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
        try {
            String role = jdbcTemplate.queryForObject(sql, new Object[]{username, password}, String.class);

            if (role != null) {
                session.setAttribute("username", username);
                session.setAttribute("role", role);

                switch (role) {
                    case "manager":
                        return "redirect:/manager/dashboard";
                    case "hr":
                        return "redirect:/hr/dashboard";
                    case "employee":
                        return "redirect:/employee/dashboard";
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

    @GetMapping
    public String loginPage() {
        return "login";
    }
}
