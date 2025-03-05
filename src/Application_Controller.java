import java.io.Serializable;
import java.util.List;

import javafx.scene.control.Alert;

public class Application_Controller implements Serializable{
    private static final long serialVersionUID = -2876550742232410274L;
	private DataCenter dataCenter;

    public Application_Controller(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    // Admin Authority

    public List<String> getCourseList() {
        return dataCenter.getCourseNames();
    }

    public void createCourse(Course newCourse) {
        dataCenter.createCourse(newCourse);
    }

    public void editCourse(int crn, String newTitle, int newCredits) {
        dataCenter.editCourse(crn, newTitle, newCredits);
    }

    public Course getCourseDetails(int crn) {
        return dataCenter.getCourseInfo(crn);
    }

    public List<String> getStudentList() {
        return dataCenter.getStudentNames();
    }

    public void editStudentInfo(int studentId, String newStatus,String courseName, int newScore) {
        dataCenter.editStudentInfo(studentId, newStatus,courseName, newScore);
    }

    // Student Authority

    public boolean registerStudent(Student student) {
        return DataCenter.getInstance().registerStudent(student);
    }

    public boolean loginAsStudent(String username, String password) {
        return dataCenter.findStudent(username, password) != null;
    }

    public Student getStudentDetails(int studentId) {
        return dataCenter.getStudentInfo(studentId);
    }

    public void editStudentProfile(int studentId, String newInfo) {
        dataCenter.editStudentProfile(studentId, newInfo);
    }
    public void courseEnroll(int studentId, int crn) {
        boolean enrollmentSuccess = dataCenter.enrollInCourse(studentId, crn);

        if (enrollmentSuccess) {
            showAlert("Enrollment successful!");
        } else {
            showAlert("Enrollment failed. Check if the student is already enrolled.");
        }
    }
    public void handleStatusChangeToCompleted(Student student) {
        student.changeStatusToCompleted();
        showAlert("Student status changed to Completed.");
    }
    public int generateStudentID() {
        // Use a counter or any other logic to generate the next student ID
        return DataCenter.getInstance().getNextStudentID();
    }


    // Helper method to show alerts
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // Admin Login Check

    public boolean isAdmin(String username, String password) {
        return dataCenter.isAdmin(username, password);
    }
    public boolean isStudent(String username, String password) {
        return dataCenter.isStudent(username, password);
    }
    public Student authenticateStudent(String username, String password) {
        List<Student> students = DataCenter.getInstance().getStudentList();

        for (Student student : students) {
            if (student.getUsername().equals(username) && student.getPassword().equals(password)) {
                // Authentication successful, return the authenticated student
                return student;
            }
        }
        return null;
    }

    
}
