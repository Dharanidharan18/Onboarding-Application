package com.chainsys.onboardapplication.dao;



import com.chainsys.onboardapplication.model.Employee;

import jakarta.servlet.http.HttpServletRequest;

//import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

public interface ManagerDashboardDAO {
    int getManagerId(String username) throws SQLException;
    List<Employee> getAssignedEmployees(int managerId) throws SQLException;
    void assignTask(HttpServletRequest request) throws SQLException;
    void approveTask(HttpServletRequest request) throws SQLException;
    void evaluateEmployee(HttpServletRequest request) throws SQLException;
    List<Employee> getEmployeesOnBench(int managerId) throws SQLException;
}
