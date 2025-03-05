import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Student implements Serializable{
	private static final long serialVersionUID = 1219593492307340322L;
	private static int nextId = 1;
	private int studentID;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String major;
	private String status;
    private List<CourseScore>courseScores;
	private List<Course> selected;
	private List<Course> completed;
	private String phone;
	private String address;
	
	
	public Student(int studentID, String username, String password, String firstName, String lastName, String major) {
		this.studentID = nextId++;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.major = major;
		this.selected = new ArrayList<>();
		this.completed = new ArrayList<>();
        this.courseScores = new ArrayList<>();
        this.phone = "";
        this.address = "";
	}
	
	// getter for all 
	public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

	public int getStudentID() {
		return studentID;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getMajor() {
		return major;
	}
	
	public List<Course> getSelectedCourse() {
		return selected;
	}
	
	public List<Course> getCompletedCourse() {
		return completed;
	}
	
	public String getStatus() {
	    return status;
	}
	public List<CourseScore> getCourseScores() {
        return new ArrayList<>(courseScores);
    }


	// Setter methods

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

	public void setStatus(String newStatus) {
        this.status = newStatus;
    }

	
	public void setStudentID(int studentID) {
		this.studentID = studentID;
		
	}
	public void setUsername(String username) {
		this.username = username;
		
	}
	public void setPassword(String password) {
		this.password = password;
		
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
		
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	
	}
	public void setMajor(String major) {
		this.major = major;
		
	}
	public void setSelectedCourse(List<Course> selected) {
		this.selected = selected;
		
	}
	public void setCompletedCourse(List<Course> completed) {
		this.completed = completed;
	
	}
	public void selectedCourse(Course course) {
		selected.add(course);
		
	}
	public void completedCourses(Course course) {
		if (selected.contains(course)) {
	        selected.remove(course);
	        completed.add(course);
	    } else {
	        System.out.println("Course not found in the selected list.");
	        
	    }
		
	}
	public void enrollInCourse(Course course) {
		if(!selected.contains(course)) {
			selected.add(course);
			System.out.println("Enrolled in course: " + course.getCrn());
		}
		else {
			System.out.println("Already Enrolled: "+ course.getCrn());
		}
	}
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(!(obj instanceof Student))
			return false;
		
		Student otherStudent = (Student) obj;
		return this.studentID == otherStudent.getStudentID() &&
				this.username.equals(otherStudent.getUsername()) &&
				this.password.equals(otherStudent.getPassword()) &&
				this.firstName.equals(otherStudent.getFirstName()) &&
				this.lastName.equals(otherStudent.getLastName()) &&
				this.major.equals(otherStudent.getMajor()) &&
				listEquals(this.selected,otherStudent.getSelectedCourse())&&
				listEquals(this.completed,otherStudent.getCompletedCourse());
		
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nStudent[ \nStudentId: ").append(studentID)
		.append("\nUsername: ").append(username)
		.append("\nPassword: ").append(password)
		.append("\nFirst Name: ").append(firstName)
		.append("\nLast Name: ").append(lastName)
		.append("\nMajor: ").append(major)
		.append("\nSelected Courses: ").append(selected.isEmpty()?"None":selected)
		.append("\nCompleted Course: ").append(completed.isEmpty()?"None":completed);
		
		return sb.toString();
	}
	
	
	private boolean listEquals(List<Course> list1, List<Course> list2) {
		if (list1 == null || list2 == null)
	        return false;

		
		if(list1.size() != list2.size())
			return false;
		
		for(int i = 0; i<list1.size();i++) {
			if(!(list1.get(i).equals(list2.get(i))))
				return false;
		}
		return true;
		
	}
	public void updateProfile(String newInfo) {
        // Assuming newInfo is a comma-separated string like "John,Doe,Computer Science"
        String[] infoParts = newInfo.split(",");

        if (infoParts.length == 3) {
            this.firstName = infoParts[0].trim();
            this.lastName = infoParts[1].trim();
            this.major = infoParts[2].trim();
        } else {
            System.out.println("Invalid format for newInfo.");
        }
    }

	public void removeCourse(Course course) {
	    List<Course> courses = DataCenter.getInstance().getCourseList();
	    courses.remove(course);

	    // Remove the course from enrolled students
	    List<Student> students = DataCenter.getInstance().getStudentList();
	    for (Student student : students) {
	        student.getSelectedCourse().remove(course);
	        student.getCompletedCourse().remove(course);
	    }

	    System.out.println("Course removed: " + course.getTitle());
	}
    // Method to add a course score
    public void addCourseScore(String courseName, int score) {
        CourseScore courseScore = new CourseScore(courseName, score);
        courseScores.add(courseScore);
    }

    

	private static class CourseScore implements Serializable{
        private static final long serialVersionUID = -67455882010421031L;
		private String courseName;
        private int score;
        
        public CourseScore(String courseName, int score) {
            this.courseName = courseName;
            this.score = score;
        }

        public void setScore(int newScore) {
            this.score = newScore;
        }

		public String getCourseName() {

			return courseName;
		}

        
    }
	// Method to get the score for a course
    public int getScoreForCourse(String courseName) {
        for (CourseScore courseScore : courseScores) {
            if (courseScore.courseName.equals(courseName)) {
                return courseScore.score;
            }
        }
        return 0; 
    }

	public void setScoreForCourse(String courseName, int newScore) {
	    for (CourseScore courseScore : courseScores) {
	        if (courseScore.courseName.equals(courseName)) {
	            courseScore.setScore(newScore);
	            System.out.println("Score updated for course: " + courseName);
	            return;
	        }
	    }

	    // If the course score doesn't exist, add a new one
	    addCourseScore(courseName, newScore);
	}
	public List<Course> getEnrolledCoursesForStudent() {
        List<Course> enrolledCourses = new ArrayList<>();

        for (Course course : selected) {
            // Check if the student has completed the course
            if (!completed.contains(course)) {
                enrolledCourses.add(course);
            }
        }

        return enrolledCourses;
    }
	public void addOrUpdateCourseScore(String courseName, int newScore) {
        boolean found = false;
        for (CourseScore courseScore : courseScores) {
            if (courseScore.getCourseName().equals(courseName)) {
                // Update the score for the specific course
                courseScore.setScore(newScore);
                found = true;
                break;
            }
        }

        if (!found) {
            // Course not found in the student's scores, add a new score
            CourseScore newCourseScore = new CourseScore(courseName, newScore);
            courseScores.add(newCourseScore);
        }
    }
	
	public void changeStatusToCompleted() {
        setStatus("Completed");

        List<Course> selectedCourses = getSelectedCourse();
        getCompletedCourse().addAll(selectedCourses);
        selectedCourses.clear();

        for (Course course : getCompletedCourse()) {
            int score = promptForScore(course);
            addCourseScore(course.getTitle(), score);
        }
        DataCenter.getInstance().save();
    }

	private int promptForScore(Course course) {
		Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the score for the course " + course.getTitle() + ": ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid numeric value.");
            scanner.next(); 
        }

        int score = scanner.nextInt();
        scanner.close();
        return score;


	}

	public boolean isEnrolledInCourse(int courseCRN) {
	    for (Course course : selected) {
	        if (course.getCrn() == courseCRN) {
	            return true;
	        }
	    }
	    return false;
	}



	
	
	
}
