package com.chainsys.onboardapplication.dao;
import com.chainsys.onboardapplication.model.Task;

import java.util.List;

public class EmployeeDashboardDAO {

	 List<Task> getTasksByUsername(String username);
	    boolean isEmployeeApproved(String username);
}
