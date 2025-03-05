import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ListView;

public class DataCenter implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int nextStudentID = 1;
	private static final String admin ="Admin";
	private static final String admin_Password = "CSE148";
	
	
	
	private static DataCenter instance = null;
	
	private List<Student> students;
	private List<Course> courses;
	
	private DataCenter() {
		this.students = new ArrayList<>();
		this.courses = new ArrayList<>();
	}
	
	public static DataCenter getInstance() {
	    if (instance == null) {
	        File file = new File("Path-to-data-file");
	        if (file.exists()) {
	            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
	                instance = (DataCenter) ois.readObject();
	            } catch (IOException | ClassNotFoundException e) {
	                e.printStackTrace();
	                instance = new DataCenter();
	            }
	        } else {
	            instance = new DataCenter();
	        }
	    }
	    return instance;
	}

	public boolean save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Path-to-data-file"))) {
        	oos.writeObject(instance);
        	return true;
        }catch(IOException e) {
        	e.printStackTrace();
        	return false;
        }
		
	}
	// Student Methods
	
	
	// update SCores
	public void updateScore(int studentID, int courseCRN, int newScore) {
	    List<Student> students = getStudentList();
	    for (Student student : students) {
	        if (student.getStudentID() == studentID) {
	            // Find the student
	            student.addOrUpdateCourseScore(getCourseByCRN(courseCRN).getSubject(), newScore);

	            // Save changes to the data file if needed
	            save();
	            break;
	        }
	    }
	}

	public Course getCourseByCRN(int courseCRN) {
        List<Course> courses = getCourseList();
        for (Course course : courses) {
            if (course.getCrn() == courseCRN) {
                return course;
            }
        }
        return null;
        
	}
	//edit profile

	public void editStudentProfile(int studentId, String newInfo) {
	    Student student = getStudentInfo(studentId);

	    if (student != null) {
	        // Update student profile information
	        student.updateProfile(newInfo);

	        // Save changes to the data file
	        save();

	        System.out.println("Student profile information updated successfully!");
	    } else {
	        System.out.println("Student not found.");
	    }
	}

	public void editStudentInfo(int studentId, String newStatus, String courseName, int newScore) {
	    Student student = getStudentInfo(studentId);

	    if (student != null) {
	        // Update student status
	        student.setStatus(newStatus);

	        // Update score for the specified course
	        student.setScoreForCourse(courseName, newScore);

	        // Save changes to the data file
	        save();

	        System.out.println("Student information updated successfully!");
	    } else {
	        System.out.println("Student not found.");
	    }
	}
	public int getNextStudentID() {
        // Increment the counter and return the next student ID
        return nextStudentID++;
    }

	public boolean registerStudent(Student student) {
	    if (isUniqueStudentID(student.getStudentID())) {
	        students.add(student);
	        save(); 
	        return true;
	    } else {
	        System.out.println("StudentID already exists.");
	        return false;
	    }
	}

	public void deleteStudent(Student student) {
		if(student.getStudentID() == student.getStudentID()) {
			students.remove(student);
			
		}
	}
	public void removeStudent(Student student) {
        for (Course course : courses) {
            course.removeStudent(student);
        }
        students.remove(student);
        System.out.println("Student removed: " + student.getFirstName() + " " + student.getLastName());
    }
	
	public Student findStudent(String username, String password) {
		for(int i = 0; i<students.size();i++) {
			Student student = students.get(i);
			
			if(student.getUsername().equals(username) && student.getPassword().equals(password)) {
				return student;
			}
		}
		return null;
	}
	public Student getStudentInfo(int studentID) {
		for(int i = 0; i<students.size();i++) {
			Student student = students.get(i);
			
			if(student.getStudentID() == studentID)
				return student;
		}
		return null;
	}
	public List<String> getStudentNames() {
	    List<String> studentNames = new ArrayList<>();
	    for (Student student : DataCenter.getInstance().getStudentList()) {
	        String studentInfo = "ID: " + student.getStudentID() + ", Name: " + student.getFirstName() + " " + student.getLastName();
	        studentNames.add(studentInfo);
	    }
	    return studentNames;
	}

	// Course Methods
	
	public void createCourse(Course course) {
		if(isUniqueCrn(course.getCrn())) {
			courses.add(course);
			
		}
		else {
			System.out.println("Course already exists.");
		}
	
	}
	public List<String> getCourseNames() {
		List<String> courseNames = new ArrayList<>();
	    for (Course course : DataCenter.getInstance().getCourseList()) {
	        String courseInfo = "CRN: " + course.getCrn() + ", Title: " + course.getTitle();
	        courseNames.add(courseInfo);
	    }
	    return courseNames;
	    
	}
	public Course getCourseInfo(int crn) {
		for(Course course : courses) {
			if(course.getCrn() == crn)
				return course;
		}
		return null;
	}
	public void deleteCourse(int crn) {
	    Course courseToRemove = null;
	    for (Course course : courses) {
	        if (course.getCrn() == crn) {
	            courseToRemove = course;
	            break;
	        }
	    }

	    if (courseToRemove != null) {
	        courses.remove(courseToRemove);
	        save();
	    }
	}
	public void editCourse(int crn, String newTitle, int newCredits) {
	    Course courseToEdit = null;
	    for (Course course : courses) {
	        if (course.getCrn() == crn) {
	            courseToEdit = course;
	            break;
	        }
	    }

	    if (courseToEdit != null) {
	        courseToEdit.setTitle(newTitle);
	        courseToEdit.setCredits(newCredits);
	        save(); // Save data after editing the course
	        System.out.println("Course edited successfully!");
	    } else {
	        System.out.println("Course not found. Edit failed.");
	    }
	}

	public List<Student> getStudentList(){
		return new ArrayList<>(students);
		
	}
	public List<Course> getCourseList(){
		return new ArrayList<>(courses);
		
	}
	
	public boolean enrollInCourse(int studentId, int crn) {
	    Student student = getStudentInfo(studentId);
	    Course course = getCourseInfo(crn);

	    if (student != null && course != null) {
	        try {
	            student.enrollInCourse(course);
	            save();  
	            return true;
	        } catch (IllegalArgumentException e) {
	            System.out.println(e.getMessage());
	            return false;
	        }
	    } else {
	        System.out.println("Invalid Student ID or CRN");
	        return false;
	    }
	}


	public boolean isAdmin(String username, String password) {
	    if (username.equals(DataCenter.admin) && password.equals(DataCenter.admin_Password)) {
	        return true;
	    } else {
	        return false;
	    }
	}

	public boolean isStudent(String username, String password) {
	    if (DataCenter.getInstance().findStudent(username, password) != null) {
	        return true;
	    } else {
	        return false;
	    }
	}

	public void updateCourseListView(ListView<String> courseListView) {
	    courseListView.getItems().clear();
	    List<String> courses = DataCenter.getInstance().getCourseNames();
	    courseListView.getItems().addAll(courses);
	}

	public void removeStudentFromCourse(int courseCrn, int studentId) {
	    Course course = getCourseInfo(courseCrn);
	    Student student = getStudentInfo(studentId);

	    if (course != null && student != null) {
	        course.removeStudent(student);
	        student.removeCourse(course);
	        System.out.println("Student removed from course: " + course.getTitle());
	    } else {
	        System.out.println("Error removing student from course. Course or student not found.");
	    }
	}

	


	// Private Method to check unique StudentID
	
	private boolean isUniqueStudentID(int studentID) {
		for(Student existingStudent : students) {
			if(existingStudent.getStudentID() == studentID) {
				return false;
			}
		}
		return true;
	}

	// Private Method to check unique Crn
	
	private boolean isUniqueCrn(int crn) {
		for(Course existingCourse : courses) {
			if(existingCourse.getCrn() == crn) {
				return false;
			}
		}
		return true;
		
	}
	
}
