package connection_mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import course_data.Course;
import course_data.Mentor;
import course_service.CourseService;
import course_service.UserService;

public class Main {
	public static void main(String[] args) throws SQLException {

//		Connection connection = DBConnection.makeConnection();
//		System.out.println(connection);

		// Show all courses
		CourseService courseService = new CourseService();
		List<Course> showAllCourse = courseService.showAllCourse();

		for (Course course : showAllCourse) {
			System.out.println("ID: " + course.getId() + " | Course: " + course.getName() + " | Begin: "
					+ course.getBegin() + " - End: " + course.getEnd() + " | Fee: " + course.getFee());
		}

		// Show mentor by courseID
		Scanner scanner = new Scanner(System.in);
		System.out.print("\nEnter Course ID to match mentors: ");
		int courseId = scanner.nextInt();
		scanner.nextLine();

		Course myCourse = new Course();
		myCourse.setId(courseId);

		List<Mentor> mentors = CourseService.showMentorByCourse(myCourse);

		for (Mentor mentor : mentors) {
			System.out.println("Mentor ID: " + mentor.getId());
			System.out.println("Mentor Name: " + mentor.getName());
			System.out.println("Mentor Email: " + mentor.getEmail());
			System.out.println("Mentor Phone: " + mentor.getPhone());
			System.out.println("--------------");
		}

		// Register New User
		UserService userService = new UserService();

		System.out.println("Enter id:");
		String login_id = scanner.nextLine();

		System.out.println("Enter password:");
		String password = scanner.nextLine();

		System.out.println("Enter name:");
		String name = scanner.nextLine();

		userService.registerNewUser(login_id, password, name);

		// Log in
		System.out.println("Enter login_id:");
		String login = scanner.nextLine();

		System.out.println("Enter password:");
		String password1 = scanner.nextLine();

		userService.login(login_id, password1);

	}

}
