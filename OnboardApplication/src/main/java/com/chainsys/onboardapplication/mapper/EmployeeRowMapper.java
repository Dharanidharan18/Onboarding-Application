package com.chainsys.onboardapplication.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.springframework.jdbc.core.RowMapper;
import com.chainsys.onboardapplication.model.Employee;

public class EmployeeRowMapper implements RowMapper<Employee> {
    
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("employee_id");
        String name = rs.getString("employee_name");
        Date startDate = rs.getDate("start_date");
        Date endDate = rs.getDate("end_date");
        String certificationType = rs.getString("certification_type");
        String completionOfCertification = rs.getString("completion_of_certification");

        // Check if startDate, endDate, certificationType, and completionOfCertification are available in the ResultSet
        if (startDate == null && endDate == null && certificationType == null && completionOfCertification == null) {
            return new Employee(id, name);
        } else {
            return new Employee(id, name, startDate, endDate, certificationType, completionOfCertification);
        }
    }
}
