package com.chainsys.onboardapplication.dao.impl;

import com.chainsys.onboardapplication.dao.EmployeeDashboardDAO;
import com.chainsys.onboardapplication.model.Task;
import com.chainsys.onboardingapplication.util.OnboardingApplicationDB;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeDashboardImpl implements EmployeeDashboardDAO {

    @Override
    public List<Task> getTasksByUsername(String username) {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = OnboardingApplicationDB.getConnection()) {
            String sql = "SELECT t.task_id, t.task, t.status, t.due_date FROM tasks t JOIN users u ON t.employee_id = u.employee_id WHERE u.username = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Task task = new Task();
                task.setId(resultSet.getInt("task_id"));
                task.setTask(resultSet.getString("task"));
                task.setStatus(resultSet.getString("status"));
                task.setDueDate(resultSet.getTimestamp("due_date"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    @Override
    public boolean isEmployeeApproved(String username) {
        boolean isApproved = false;
        try (Connection conn = OnboardingApplicationDB.getConnection()) {
            String sql = "SELECT is_approved FROM employee_details ed JOIN users u ON ed.employee_id = u.employee_id WHERE u.username = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isApproved = resultSet.getBoolean("is_approved");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isApproved;
    }
}
