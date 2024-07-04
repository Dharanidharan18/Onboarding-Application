package com.chainsys.onboardapplication.controller;



import com.chainsys.onboardapplication.model.Employee;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.chainsys.onboardapplication.dao.ManagerDashboardDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ManagerDashboardController {

    @Autowired
    private ManagerDashboardDAO managerDashboardDAO;

    @GetMapping("/managerdashboard")
    public String getDashboard(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        System.out.println(username);
        if (username == null || !"manager".equals(session.getAttribute("role"))) {
            return "redirect:/login";
        }

        try {
            int managerId = managerDashboardDAO.getManagerId(username);
            List<Employee> assignedEmployees = managerDashboardDAO.getAssignedEmployees(managerId);

            model.addAttribute("assignedEmployees", assignedEmployees);

            if (managerId != -1) {
                List<Employee> employeesOnBench = managerDashboardDAO.getEmployeesOnBench(managerId);
                model.addAttribute("employeesOnBench", employeesOnBench);
            }

            return "managerDashboard.jsp";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Error loading dashboard");
            return "error";
        }
    }

    @PostMapping("/dashboard")
    public String handlePost(HttpServletRequest request, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");

        if (username == null || !"manager".equals(session.getAttribute("role"))) {
            return "redirect:/login";
        }

        String action = request.getParameter("action");

        try {
            if ("assignTask".equals(action)) {
                managerDashboardDAO.assignTask(request);
            } else if ("approveTask".equals(action)) {
                managerDashboardDAO.approveTask(request);
            } else if ("evaluateEmployee".equals(action)) {
                managerDashboardDAO.evaluateEmployee(request);
            }
            return "redirect:/managerDashboard.jsp";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Error processing request");
            return "error";
        }
    }
}
