package com.lanlq.jersey;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;






@Path("/employees")
public class EmployeeResource {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/employees_management";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private static final Logger logger = LogManager.getLogger(EmployeeResource.class);
  
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResult<List<Employee>> getAllEmployees() throws ClassNotFoundException {
    	logger.info("Kết nối database");
        Connection conn = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
//            logger.info("Lấy danh sách thông tin nhân viên");

            // Prepare the SQL SELECT statement
            String sql = "SELECT * FROM nhanvien";
            Statement statement = conn.createStatement();

            // Execute the SQL SELECT statement
            ResultSet resultSet = statement.executeQuery(sql);

            List<Employee> employees = new ArrayList<>();

            // Iterate through the result set and add employees to the list
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getLong("manhanvien"));
                employee.setFirstName(resultSet.getString("ten"));
                employee.setLastName(resultSet.getString("ho"));
                employees.add(employee);
            }

//            return Response.ok(employees).build();
            return new ServiceResult<>(CustomResult.SUCCESS, "Danh sách nhân viên", employees);
            
            
        } catch (SQLException e) {
            e.printStackTrace();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//            logger.error("Không kết nối được serve");
            return new ServiceResult<>(CustomResult.ERROR, "Lấy danh sách nhân viên thất bại",null );
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
}
