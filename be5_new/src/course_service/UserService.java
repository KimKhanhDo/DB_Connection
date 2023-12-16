package course_service;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection_mysql.DBConnection;
import course_data.Student;

public class UserService {

	public void registerNewUser(String login_id, String password, String name) throws SQLException {
    Connection connection = DBConnection.makeConnection();

    // Check if the user already exists in the student table
    String checkQuery = "SELECT * FROM registered_student WHERE login_id = '" + login_id + "'";
    Statement checkStatement = connection.createStatement();

    if (checkStatement.executeQuery(checkQuery).next()) {
        System.out.println("This Account Already Exists. Please register a new one");
        checkStatement.close();
        connection.close();
        return;
    }

    // Insert the new user into the registered_student table
    String insertQuery = "INSERT INTO registered_student (login_id, password, name) VALUES ('" + login_id + "', '" + password + "', '" + name + "')";
    Statement insertStatement = connection.createStatement();
    insertStatement.executeUpdate(insertQuery);

    System.out.println("Register successfully");

    checkStatement.close();
    insertStatement.close();
    connection.close();
}

public Student getStudentById(String id) throws SQLException {
    Connection connection = DBConnection.makeConnection();
    String query = "SELECT * FROM registered_student WHERE login_id = '" + id + "'";
    Statement statement = connection.createStatement();

    ResultSet resultSet = statement.executeQuery(query);

    if (resultSet.next()) {
        String studentId = resultSet.getString("id");
        String password = resultSet.getString("password");
        String name = resultSet.getString("name");

        Student student = new Student(studentId, password, name);

        statement.close();
        connection.close();
        return student;
    } else {
        statement.close();
        connection.close();
        return null;
    }
}

public boolean login(String id, String password) throws SQLException {
    Connection connection = DBConnection.makeConnection();

    // Check if the user exists and the password is correct
    String query = "SELECT * FROM registered_student WHERE login_id = '" + id + "' AND password = '" + password + "'";
    Statement statement = connection.createStatement();

    ResultSet resultSet = statement.executeQuery(query);

    if (resultSet.next()) {
        // Login Successful
        System.out.println("Login Successfully");
        statement.close();
        connection.close();
        return true;
    } else {
        System.out.println("Invalid Account!!!");

        // Get a Student object by ID
        Student student = getStudentById(id);

        if (student != null) {
            int failedCount = student.getFailedCount() + 1;
            student.setFailedCount(failedCount);

            if (failedCount > 3) {
                System.out.println("Your account is locked");
                statement.close();
                connection.close();
                return false;
            }
        } else {
            System.out.println("Your Account Does Not Exist. Please Register");
        }
    }

    statement.close();
    connection.close();
    return false;
}


}
