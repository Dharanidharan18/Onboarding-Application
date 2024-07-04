package com.chainsys.onboardapplication.dao.impl;

import com.chainsys.onboardapplication.dao.HRDashboardDAO;
import com.chainsys.onboardapplication.model.EmployeeDocuments;
import com.chainsys.onboardapplication.model.Employee;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

//import javax.servlet.http.HttpServletRequest;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class HRDashboardDAOImpl implements HRDashboardDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void registerEmployee(HttpServletRequest request) {
        String name = request.getParameter("employee_name");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String sql = "INSERT INTO users (employee_name, email, username, password, role) VALUES (?, ?, ?, ?, 'employee')";
        jdbcTemplate.update(sql, name, email, username, password);
    }

    @Override
    public boolean assignProject(HttpServletRequest request) {
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        int managerId = Integer.parseInt(request.getParameter("managerId"));
        String projectName = request.getParameter("projectName");

        // Check if the employee is already assigned to a project
        String checkSql = "SELECT COUNT(*) FROM project_assignments WHERE employee_id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, new Object[]{employeeId}, Integer.class);

        if (count > 0) {
            return false; // Employee is already assigned to a project
        }

        // Proceed with the assignment if not already assigned
        String sql = "INSERT INTO project_assignments (employee_id, manager_id, project_name) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, employeeId, managerId, projectName);
        return true;
    }

    @Override
    public void approveDocuments(HttpServletRequest request) {
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        String sql = "UPDATE employee_details SET is_approved = TRUE WHERE employee_id = ?";
        jdbcTemplate.update(sql, employeeId);
    }

    @Override
    public List<Employee> getEmployees() {
        String sql = "SELECT employee_id, employee_name FROM users WHERE role = 'employee'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Employee(rs.getInt("employee_id"), rs.getString("employee_name")));
    }

    @Override
    public List<Employee> getManagers() {
        String sql = "SELECT employee_id, employee_name FROM users WHERE role = 'manager'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Employee(rs.getInt("employee_id"), rs.getString("employee_name")));
    }

    @Override
    public List<EmployeeDocuments> getPendingDocumentApprovals() {
        String sql = "SELECT ed.employee_id, u.employee_name, ed.phone_num, ed.aadhar_num, ed.pan_num, ed.aadhar_img, ed.pan_img, ed.marksheet, ed.resume " +
                     "FROM employee_details ed " +
                     "JOIN users u ON ed.employee_id = u.employee_id " +
                     "WHERE ed.is_approved = FALSE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapToDocumentApproval(rs));
    }
//
//    @Override
//    public int getTotalEmployees() {
//        String sql = "SELECT COUNT(*) AS total FROM users WHERE role = 'employee'";
//        return jdbcTemplate.queryForObject(sql, Integer.class);
//    }

//    @Override
//    public void updateTotalEmployeesCount() {
//        String updateSql = "UPDATE employee_count SET total_employees = total_employees + 1";
//        jdbcTemplate.update(updateSql);
//    }

    @Override
    public List<EmployeeDocuments> getApprovedDocuments() {
        String sql = "SELECT ed.employee_id, u.employee_name, ed.phone_num, ed.aadhar_num, ed.pan_num, ed.aadhar_img, ed.pan_img, ed.marksheet, ed.resume " +
                     "FROM employee_details ed " +
                     "JOIN users u ON ed.employee_id = u.employee_id " +
                     "WHERE ed.is_approved = TRUE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapToDocumentApproval(rs));
    }

    private EmployeeDocuments mapToDocumentApproval(ResultSet rs) throws SQLException {
        int employeeId = rs.getInt("employee_id");
        String employeeName = rs.getString("employee_name");
        String phoneNum = rs.getString("phone_num");
        String aadharNum = rs.getString("aadhar_num");
        String panNum = rs.getString("pan_num");
        Blob aadharImg = rs.getBlob("aadhar_img");
        Blob panImg = rs.getBlob("pan_img");
        Blob marksheet = rs.getBlob("marksheet");
        Blob resume = rs.getBlob("resume");
        return new EmployeeDocuments(employeeId, employeeName, phoneNum, aadharNum, panNum, aadharImg, panImg, marksheet, resume);
    }
}