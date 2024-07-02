package com.chainsys.onboardapplication.mapper;

import com.chainsys.onboardapplication.model.EmployeeTask;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeTaskRowMapper implements RowMapper<EmployeeTask> {
    @Override
    public EmployeeTask mapRow(ResultSet rs, int rowNum) throws SQLException {
        EmployeeTask task = new EmployeeTask();
        task.setId(rs.getInt("task_id"));
        task.setTask(rs.getString("task"));
        task.setStatus(rs.getString("status"));
        task.setDueDate(rs.getTimestamp("due_date"));
        return task;
    }
}
