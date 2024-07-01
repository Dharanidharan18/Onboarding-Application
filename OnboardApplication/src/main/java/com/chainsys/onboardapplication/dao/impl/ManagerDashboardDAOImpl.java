package com.chainsys.onboardapplication.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.chainsys.onboardapplication.dao.ManagerDashboardDAO;
import com.chainsys.onboardapplication.model.Employee;

import jakarta.servlet.http.HttpServletRequest;

//import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ManagerDashboardDAOImpl implements ManagerDashboardDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int getManagerId(String username) throws SQLException {
        String sql = "SELECT employee_id FROM users WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, username);
    }

    @Override
    public List<Employee> getAssignedEmployees(int managerId) throws SQLException {
        String sql = "SELECT u.employee_id, u.employee_name FROM users u JOIN project_assignments pa ON u.employee_id = pa.employee_id WHERE pa.manager_id = ?";
        return jdbcTemplate.query(sql, new EmployeeRowMapper(), managerId);
    }

    @Override
    public void assignTask(HttpServletRequest request) throws SQLException {
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        String task = request.getParameter("task");
        String dueDate = request.getParameter("dueDate");
        String sql = "INSERT INTO tasks (employee_id, task, due_date) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, employeeId, task, dueDate);
    }

    @Override
    public void approveTask(HttpServletRequest request) throws SQLException {
        int taskId = Integer.parseInt(request.getParameter("taskId"));
        String sql = "UPDATE tasks SET status = 'approved' WHERE task_id = ?";
        jdbcTemplate.update(sql, taskId);
    }

    @Override
    public void evaluateEmployee(HttpServletRequest request) throws SQLException {
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        int technicalSkill = Integer.parseInt(request.getParameter("technicalSkill"));
        int communicationSkill = Integer.parseInt(request.getParameter("communicationSkill"));
        String sql = "INSERT INTO performance (employee_id, technical_skill, communication_skill) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, employeeId, technicalSkill, communicationSkill);
    }

    @Override
    public List<Employee> getEmployeesOnBench(int managerId) throws SQLException {
        String sql = "SELECT u.employee_id, u.employee_name, b.start_date, b.end_date, b.certification_type, b.completion_of_certification " +
                     "FROM users u " +
                     "JOIN bench b ON u.employee_id = b.employee_id " +
                     "WHERE EXISTS (SELECT 1 FROM project_assignments pa WHERE u.employee_id = pa.employee_id AND pa.manager_id = ?)";
        return jdbcTemplate.query(sql, new EmployeeRowMapper(), managerId);
    }
}
