package com.chainsys.onboardapplication.controller;



import com.chainsys.onboardapplication.model.EmployeeTask;

import jakarta.servlet.http.HttpSession;

import com.chainsys.onboardapplication.dao.EmployeeDashboardDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import javax.servlet.http.HttpSession;
import java.util.List;

@Controller

public class EmployeeDashboardController {

    @Autowired
    private EmployeeDashboardDAO employeeDashboardDAO;

    @GetMapping("/employeedashboard")
    public String showDashboard(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/login";
        }

        List<EmployeeTask> tasks = employeeDashboardDAO.getTasksByUsername(username);
        boolean isApproved = employeeDashboardDAO.isEmployeeApproved(username);

        model.addAttribute("tasks", tasks);
        model.addAttribute("username", username);
        model.addAttribute("isApproved", isApproved);

        return "employeeDashboard.jsp";
    }
}
