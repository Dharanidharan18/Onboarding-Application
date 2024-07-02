package com.chainsys.onboardapplication.dao;

import com.chainsys.onboardapplication.model.EmployeeTask;
import java.util.List;

public interface EmployeeDashboardDAO {
    List<EmployeeTask> getTasksByUsername(String username);
    boolean isEmployeeApproved(String username);
}
