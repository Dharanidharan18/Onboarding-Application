package com.chainsys.onboardapplication.dao.impl;

import com.chainsys.onboardapplication.dao.EmployeeDashboardDAO;
import com.chainsys.onboardapplication.mapper.EmployeeTaskRowMapper;
import com.chainsys.onboardapplication.model.EmployeeTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeDashboardImpl implements EmployeeDashboardDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String GET_TASKS_BY_USERNAME_SQL = "SELECT t.task_id, t.task, t.status, t.due_date FROM tasks t JOIN users u ON t.employee_id = u.employee_id WHERE u.username = ?";
    private static final String IS_EMPLOYEE_APPROVED_SQL = "SELECT is_approved FROM employee_details ed JOIN users u ON ed.employee_id = u.employee_id WHERE u.username = ?";

    @SuppressWarnings("deprecation")
	@Override
    public List<EmployeeTask> getTasksByUsername(String username) {
        return jdbcTemplate.query(GET_TASKS_BY_USERNAME_SQL, new Object[]{username}, new EmployeeTaskRowMapper());
    }

    @SuppressWarnings("deprecation")
	@Override
    public boolean isEmployeeApproved(String username) {
        return jdbcTemplate.queryForObject(IS_EMPLOYEE_APPROVED_SQL, new Object[]{username}, Boolean.class);
    }
}
