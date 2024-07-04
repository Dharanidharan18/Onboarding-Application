package com.chainsys.onboardapplication.controller;

import com.chainsys.onboardapplication.dao.HRDashboardDAO;
import com.chainsys.onboardapplication.model.EmployeeDocuments;
import com.chainsys.onboardapplication.model.Employee;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HRDashboardController {

    @Autowired
    private HRDashboardDAO hrDashboardDAO;

    @Autowired
    private ApplicationContext appContext;

    @PostMapping("/hrdashboard")
    public String getDashboard(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"hr".equals(role)) {
            return "redirect:/login";
        }

        List<Employee> employees = hrDashboardDAO.getEmployees();
        List<Employee> managers = hrDashboardDAO.getManagers();
        List<EmployeeDocuments> approve = hrDashboardDAO.getApprovedDocuments();
        List<EmployeeDocuments> pendingDocumentApprovals = hrDashboardDAO.getPendingDocumentApprovals();
//        int totalEmployees = hrDashboardDAO.getTotalEmployees();

        model.addAttribute("employees", employees);
        model.addAttribute("managers", managers);
        model.addAttribute("approve", approve);
        model.addAttribute("pendingDocumentApprovals", pendingDocumentApprovals);
//        model.addAttribute("totalEmployees", totalEmployees);

        return "hrDashboard.jsp";
    }

    @PostMapping("/register")
    public String registerEmployee(HttpServletRequest request, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"hr".equals(role)) {
            return "redirect:/login";
        }

        hrDashboardDAO.registerEmployee(request);
//        hrDashboardDAO.updateTotalEmployeesCount();
        model.addAttribute("message", "Employee registered successfully.");
        return "hrdashboard";
    }

    @PostMapping("/assignProject")
    public String assignProject(HttpServletRequest request, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"hr".equals(role)) {
            return "redirect:/login";
        }

        if (hrDashboardDAO.assignProject(request)) {
            model.addAttribute("message", "Project assigned successfully.");
        } else {
            model.addAttribute("error", "Employee is already assigned to a project.");
        }
        return "hrdashboard";
    }

    @PostMapping("/approveDocuments")
    public String approveDocuments(HttpServletRequest request, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"hr".equals(role)) {
            return "redirect:/login";
        }

        hrDashboardDAO.approveDocuments(request);
        return "hrdashboard";
    }
}
