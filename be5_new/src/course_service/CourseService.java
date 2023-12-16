package course_service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connection_mysql.DBConnection;
import course_data.Course;
import course_data.Mentor;

public class CourseService {

	public List<Course> showAllCourse() throws SQLException {

		Connection connection = DBConnection.makeConnection();
		String SQL = "SELECT * FROM course";

		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery(SQL);

		List<Course> courseList = new ArrayList<Course>();

		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String name = resultSet.getString("name");
			Date beginDate = resultSet.getDate("begin_date");
			Date endDate = resultSet.getDate("end_date");
			int fee = resultSet.getInt("fee");
			Course course = new Course(id, name, beginDate, endDate, fee);
			courseList.add(course);
		}

		return courseList;
	}

	
	public static List<Mentor> showMentorByCourse2(Course course) throws SQLException {
	    List<Mentor> mentors = new ArrayList<>();

	    Connection connection = DBConnection.makeConnection();
	    String SQL = "SELECT teacher.id, teacher.name, teacher.email, teacher.phone " +
	                 "FROM teacher " +
	                 "JOIN teaching_info ON teacher.id = teaching_info.teacher_id " +
	                 "WHERE teaching_info.course_id = " + course.getId();  // Note: Directly embedding the course ID

	    Statement stm = connection.createStatement();
	    ResultSet resultSet = stm.executeQuery(SQL);

	    while (resultSet.next()) {
	        int mentorId = resultSet.getInt("id");
	        String mentorName = resultSet.getString("name");
	        String mentorEmail = resultSet.getString("email");
	        String mentorPhone = resultSet.getString("phone");

	        Mentor mentor = new Mentor(mentorId, mentorName, mentorEmail, mentorPhone);
	        mentors.add(mentor);
	    }

	    resultSet.close();
	    stm.close();
	    connection.close();

	    return mentors;
	}



}
