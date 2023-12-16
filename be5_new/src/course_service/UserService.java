package course_service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection_mysql.DBConnection;
import course_data.Student;

public class UserService {

	public void registerNewUser(String login_id, String password, String name) throws SQLException {
		Connection connection = DBConnection.makeConnection();

		// Check if the user already exists in the student table
		String checkQuery = "SELECT * FROM registered_student WHERE login_id = ?";
		PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
		checkStatement.setString(1, login_id);

		if (checkStatement.executeQuery().next()) {
			System.out.println("This Account Already Exists. Please register a new one");
			return;
		}

		// Insert the new user into the registered_student table
		String insertQuery = "INSERT INTO registered_student (login_id, password, name) VALUES (?, ?, ?)";
		PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
		insertStatement.setString(1, login_id);
		insertStatement.setString(2, password);
		insertStatement.setString(3, name);

		insertStatement.executeUpdate();
		System.out.println("Register successfully");

		// Close resources
		checkStatement.close();
		insertStatement.close();
		connection.close();
	}

	public Student getStudentById(String id) throws SQLException {
		Connection connection = DBConnection.makeConnection();
		String query = "SELECT * FROM registered_student WHERE login_id = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, id);

		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next()) {
			String studentId = resultSet.getString("id");
			String password = resultSet.getString("password");
			String name = resultSet.getString("name");

			Student student = new Student(studentId, password, name);
			
			connection.close();
			return student;
		} else {
			connection.close();
			return null;
		}
	}

	public boolean login(String id, String password) throws SQLException {
		Connection connection = DBConnection.makeConnection();

		// Check if the user exists and the password is correct
		String query = "SELECT * FROM registered_student WHERE login_id = ? AND password = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, id);
		statement.setString(2, password);

		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next()) {
			// Login Successful
			System.out.println("Login Successfully");
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
					connection.close();
					return false;
				}
			} else {
				System.out.println("Your Account Does Not Exist. Please Register");
			}
		}

		connection.close();
		return false;
	}

}
