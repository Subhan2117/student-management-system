import java.io.Serializable;
import java.util.List;


public class Course implements Serializable{
	private static final long serialVersionUID = 8401259633670261808L;

	private String subject;
	private int crn;
	private String title;
	private int credits;
	private int scores;
	
	public Course(String subject, int crn, String title, int credits) {
		this.subject = subject;
		this.crn = crn;
		this.title = title;
		this.credits = credits;
		
		
	}
	// Getters
	
	public String getSubject() {
		return subject;
		
	}
	public int getCrn() {
		return crn;
		
	}
	public String getTitle() {
		return title;
		
	}
	public int getCredits() {
		return credits;
		
	}
	public int getScores() {
		return scores;
	}
	// Setters
	
	public void setSubject(String subject) {
		this.subject = subject;
		
	}
	public void setCrn(int crn) {
		this.crn = crn;
		
	}
	public void setTitle(String title) {
		this.title = title;
		
	}
	
	public void setCredits(int credits) {
		this.credits = credits;
	}
	public void setScoreForStudent(Student student, int score) {
        this.scores = score;
        System.out.println("Score updated for course: " + this.title);
    }

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(obj == null)
			return false;
		
		if(!(obj instanceof Course))
			return false;
		
		Course otherCourse = (Course) obj;
		
		return this.subject.equals(otherCourse.getSubject()) &&
				this.crn == otherCourse.getCrn() &&
				this.title.equals(otherCourse.getTitle())&&
				this.credits == otherCourse.getCredits();
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Course [ Subject: ").append(subject)
		.append(", Crn: ").append(crn)
		.append(", Title: ").append(title)
		.append(", Credits: ").append(credits).append("]"+"\n");
		
		return sb.toString();
	}

	public void removeStudent(Student student) {
        List<Student> students = DataCenter.getInstance().getStudentList();
        students.remove(student);
        
        System.out.println("Student removed: " + student.getFirstName() + " " + student.getLastName() + " from course: " + title);
    }
	
	

}
