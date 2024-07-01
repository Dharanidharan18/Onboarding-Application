package com.chainsys.onboardapplication.dao;

import com.chainsys.onboardapplication.model.DocumentApproval;
import com.chainsys.onboardapplication.model.Employee;

import jakarta.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface HRDashboardDAO {
    void registerEmployee(HttpServletRequest request);
    boolean assignProject(HttpServletRequest request);
    void approveDocuments(HttpServletRequest request);
    List<Employee> getEmployees();
    List<Employee> getManagers();
    List<DocumentApproval> getPendingDocumentApprovals();
    int getTotalEmployees();
    void updateTotalEmployeesCount();
    List<DocumentApproval> getApprovedDocuments();
}
