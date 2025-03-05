import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.scene.*;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
public class Student_Management_GUI extends Application {
	
	private Scene login, register, adminScene, studentScene;
	private DataCenter dataCenter;
	private Application_Controller controller;
	private Tab tabPersonalInfo, tabBrowseCourses, tabCoursesEnrolled;
	
	
	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		GridPane root = new GridPane();
		
		login = new Scene(root,500,500);
		register = new Scene(demoRegister(), 500, 500);
		
		adminScene = new Scene(demoAdminScene(),500,500);
		
		dataCenter = DataCenter.getInstance();
		controller = new Application_Controller(dataCenter);

		root.setAlignment(Pos.CENTER);
		
		stage.setOnCloseRequest(e ->{
			DataCenter.getInstance().save();
			Platform.exit();
			
			
		});
		
		
		stage.setScene(login);
		stage.setTitle("Student_Management");
		root.getChildren().add(demoLogin());
		
		
		stage.show();
	}

	// Login Page
	
	public TabPane demoLogin() {
	    TabPane tabPane = new TabPane();
	    tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

	    Tab adminTab = new Tab("Admin Login");
	    adminTab.setContent(createLoginTab(true));

	    Tab studentTab = new Tab("Student Login");
	    studentTab.setContent(createLoginTab(false));

	    tabPane.getTabs().addAll(adminTab, studentTab);

	    return tabPane;
	}

	public VBox createLoginTab(boolean isAdmin) {
	    VBox vbox = new VBox();
	    vbox.setAlignment(Pos.CENTER);
	    vbox.setSpacing(20); 
	    
	    GridPane gpane = new GridPane();
	    gpane.setAlignment(Pos.CENTER);
	    gpane.setHgap(8);
	    gpane.setVgap(8);

	    Label lblUsername = new Label("Username");
	    Label lblPassword = new Label("Password");

	    TextField tfUsername = new TextField();
	    PasswordField tfPassword = new PasswordField(); 

	    HBox hbox = new HBox();
	    hbox.setPadding(new Insets(10, 0, 10, 0)); 
	    hbox.setAlignment(Pos.CENTER_RIGHT);
	    hbox.setSpacing(6);

	    Button btnLogin = new Button("Login");
	    btnLogin.setDisable(true);

	    Button btnRegister = new Button("Register");

	    Button btnExit = new Button("Exit");

	    hbox.getChildren().addAll(btnLogin, btnRegister, btnExit);

	    gpane.add(lblUsername, 0, 0);
	    gpane.add(lblPassword, 0, 1);
	    gpane.add(tfUsername, 1, 0, 2, 1);
	    gpane.add(tfPassword, 1, 1, 2, 1);
	    gpane.add(hbox, 1, 2, 2, 1);

	    btnLogin.setOnAction(e -> {
	        String username = tfUsername.getText();
	        String password = tfPassword.getText();

	        if (isAdmin) {
	            if (controller.isAdmin(username, password)) {
	                showAlert("Admin login successful!");
	                setScene(adminScene, e);
	                tfUsername.clear();
	                tfPassword.clear();
	            } else {
	                showAlert1("Invalid username or password");
	                
	                tfUsername.clear();
	                tfPassword.clear();
	            }
	        } else {
	            if (controller.isStudent(username, password)) {
	                showAlert("Student login successful!");
	                studentScene = createStudentScene(controller.authenticateStudent(username, password));

	                setScene(studentScene, e);
	                tfUsername.clear();
	                tfPassword.clear();
	            } else {
	                showAlert1("Invalid username or password");
	                
	                tfUsername.clear();
	                tfPassword.clear();
	            }
	        }
	    });

	    btnRegister.setOnAction(e -> {
	        setScene(register, e);
	    });

	    btnExit.setOnAction(e -> {
	        DataCenter.getInstance().save();
	        Platform.exit();
	    });

	    tfUsername.setOnKeyTyped(e -> {
	        String username = tfUsername.getText();
	        String password = tfPassword.getText();
	        boolean isValid = isValidCredentials(username, password);
	        btnLogin.setDisable(!isValid);
	    });

	    tfPassword.setOnKeyTyped(e -> {
	        String username = tfUsername.getText();
	        String password = tfPassword.getText();
	        boolean isValid = isValidCredentials(username, password);
	        btnLogin.setDisable(!isValid);
	    });
	    vbox.setStyle("-fx-background-color: #f0f0f0;"); 
	    gpane.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-padding: 10px;");


	    vbox.getChildren().addAll(gpane);
	    return vbox;
	}
	private void showAlert1(String message) {
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
	
	// used for register and login
	
	private boolean isValidCredentials(String username, String password) {
	    return username.length() >= 5 && password.length() >= 6;
	}

	
	public Pane demoRegister() {
	    GridPane gpane = new GridPane();
	    gpane.setAlignment(Pos.CENTER);
	    gpane.setHgap(8);
	    gpane.setVgap(8);

	    Label lblFirstName = new Label("First Name");
	    Label lblLastName = new Label("Last Name");
	    Label lblUsername = new Label("Username");
	    Label lblPassword = new Label("Password");
	    Label lblMajor = new Label("Major");
	    Label lblPhone = new Label("Phone");
	    Label lblAddress = new Label("Address");

	    TextField tfFirstName = new TextField();
	    TextField tfLastName = new TextField();
	    TextField tfUsername = new TextField();
	    TextField tfPassword = new TextField();
	    TextField tfMajor = new TextField();
	    TextField tfPhone = new TextField();
	    TextField tfAddress = new TextField();

	    HBox hbox = new HBox();
	    hbox.setPadding(new Insets(2, 0, 2, 0));
	    hbox.setAlignment(Pos.CENTER_RIGHT);
	    hbox.setSpacing(6);

	    Button btnRegister = new Button("Register");
	    Button btnCancel = new Button("Cancel");

	    hbox.getChildren().addAll(btnRegister, btnCancel);

	    gpane.add(lblFirstName, 0, 0);
	    gpane.add(lblLastName, 0, 1);
	    gpane.add(lblUsername, 0, 2);
	    gpane.add(lblPassword, 0, 3);
	    gpane.add(lblMajor, 0, 4);
	    gpane.add(lblPhone, 0, 5);
	    gpane.add(lblAddress, 0, 6);
	    gpane.add(tfFirstName, 1, 0, 2, 1);
	    gpane.add(tfLastName, 1, 1, 2, 1);
	    gpane.add(tfUsername, 1, 2, 2, 1);
	    gpane.add(tfPassword, 1, 3, 2, 1);
	    gpane.add(tfMajor, 1, 4, 2, 1);
	    gpane.add(tfPhone, 1, 5, 2, 1);
	    gpane.add(tfAddress, 1, 6, 2, 1);
	    gpane.add(hbox, 1, 7, 2, 1);

	    btnRegister.setOnAction(e -> {
	        
	        int studentID = controller.generateStudentID();

	        // Get other user inputs
	        String firstName = tfFirstName.getText();
	        String lastName = tfLastName.getText();
	        String username = tfUsername.getText();
	        String password = tfPassword.getText();
	        String major = tfMajor.getText();
	        String phone = tfPhone.getText();
	        String address = tfAddress.getText();

	        // Check if the username is already taken
	        if (isUsernameAvailable(username)) {
	            // Validate username and password
	            if (isValidCredentials(username, password)) {
	                // Create a new Student object with an automatically generated ID
	                Student student = new Student(studentID, username, password, firstName, lastName, major);
	                student.setPhone(phone);
	                student.setAddress(address);

	                // Register the student
	                DataCenter dataCenter = DataCenter.getInstance();
	                dataCenter.registerStudent(student);

	                showAlert("Registration Successful");

	                // Clear input fields
	                tfFirstName.clear();
	                tfLastName.clear();
	                tfUsername.clear();
	                tfPassword.clear();
	                tfMajor.clear();
	                tfPhone.clear();
	                tfAddress.clear();

	                dataCenter.save();

	                // Set the scene to the login page
	                setScene(login, e);
	            } else {
	                showAlert("Invalid username or password (minimum 6 characters)");
	            }
	        } else {
	            showAlert("Username already taken. Please choose a different username.");
	        }
	    });

	    btnCancel.setOnAction(e -> {
	      
	    	tfFirstName.clear();
	    	tfLastName.clear();
	    	tfUsername.clear();
	    	tfPassword.clear();
	    	tfMajor.clear();
	    	tfPhone.clear();
	    	tfAddress.clear();

	        setScene(login, e);
	    });

	    return gpane;
	}
    // Check if the username is already in use

    public boolean isUsernameAvailable(String username) {
        List<Student> students = DataCenter.getInstance().getStudentList();
        return students.stream().noneMatch(student -> student.getUsername().equals(username));
    }


	
	// Admin Scene
	
	public Pane demoAdminScene() {
	    GridPane gpane = new GridPane();
	    gpane.setAlignment(Pos.CENTER);
	    gpane.setHgap(8);
	    gpane.setVgap(8);
	    gpane.setStyle("-fx-background-color: #f0f0f0;");  
	    Text title = new Text("Admin Dashboard");
	    title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	    title.setFill(Color.DARKBLUE);
	    GridPane.setHalignment(title, HPos.CENTER);
	    gpane.add(title, 0, 0, 2, 1);


	    Button btnCourseList = new Button("Course Browse List");
	    Button btnStudentList = new Button("Student Browse List");
	    Button backToLogin = new Button("Sign Out");

	    HBox hbox = new HBox();
	    hbox.setPadding(new Insets(10, 0, 10, 0));
	    hbox.setAlignment(Pos.CENTER);
	    hbox.setSpacing(20);

	  
	    btnCourseList.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
	    btnStudentList.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white;");
	    backToLogin.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

	    hbox.getChildren().addAll(btnCourseList, btnStudentList);

	    gpane.add(hbox, 0, 1);
	    gpane.add(backToLogin, 0, 2);
	    GridPane.setValignment(backToLogin, VPos.BOTTOM);
	    GridPane.setHalignment(backToLogin, HPos.CENTER);

	    btnCourseList.setOnAction(e -> {
	        setScene(browseCourseListScene(), e);
	    });

	    btnStudentList.setOnAction(e -> {
	        setScene(browseStudentListScene(), e);
	    });

	    backToLogin.setOnAction(e -> {
	        setScene(login, e);
	    });

	    return gpane;
	}

	// Browse Course List
	public Scene browseCourseListScene() {
	    VBox vbox = new VBox();
	    vbox.setAlignment(Pos.CENTER);
	    vbox.setSpacing(10);
	    vbox.setStyle("-fx-background-color: #f0f0f0;");

	    Label lblTitle = new Label("Course List");
	    lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

	    ListView<String> courseListView = new ListView<>();
	    List<String> courses = DataCenter.getInstance().getCourseNames();
	    courseListView.getItems().addAll(courses);

	    Button btnBack = new Button("Back to Admin Scene");
	    Button btnCreate = new Button("Create Course");
	    
	    Button btnDelete = new Button("Delete");
	    Button btnShowInfo = new Button("Show Info");
	    btnBack.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white;");
	    btnCreate.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
	    btnDelete.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
	    btnShowInfo.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");


	    btnBack.setOnAction(e -> {
	        setScene(adminScene, e);
	    });

	    btnCreate.setOnAction(e -> {
	        setScene(createCourseScene(), e);
	    });
	    btnDelete.setOnAction(e -> {
	        // Get the selected course info from the list
	        String selectedCourseInfo = courseListView.getSelectionModel().getSelectedItem();

	        if (selectedCourseInfo != null) {
	            try {
	            
	            	String crnString = "";
	            	for (char c : selectedCourseInfo.toCharArray()) {
	            	    if (Character.isDigit(c)) {
	            	        crnString += c;
	            	    }
	            	}
	                int crn = Integer.parseInt(crnString);

	                
	                DataCenter.getInstance().deleteCourse(crn);

	                // Update the course list view
	                dataCenter.updateCourseListView(courseListView);

	                showAlert("Course deleted successfully!");
	            } catch (NumberFormatException ex) {
	                showAlert("Invalid CRN");
	            }
	        } else {
	            showAlert("Please select a course to delete");
	        }
	    });
	    btnShowInfo.setOnAction(e -> {
	        // Get the selected course info from the list
	        String selectedCourseInfo = courseListView.getSelectionModel().getSelectedItem();

	        if (selectedCourseInfo != null) {
	            try {
	                
	            	String crnString = "";
	            	for (char c : selectedCourseInfo.toCharArray()) {
	            	    if (Character.isDigit(c)) {
	            	        crnString += c;
	            	    }
	            	}
	                int crn = Integer.parseInt(crnString);

	                // Get the course information from the data center
	                Course selectedCourse = DataCenter.getInstance().getCourseInfo(crn);

	                if (selectedCourse != null) {
	                   
	                    TextField tfTitle = new TextField(selectedCourse.getTitle());
	                    TextField tfCredits = new TextField(String.valueOf(selectedCourse.getCredits()));

	                    
	                    Button btnUpdate = new Button("Update");
	                    Button btnClear = new Button("Clear");

	                    VBox editBox = new VBox(10);
	                    editBox.getChildren().addAll(new Label("Title:"), tfTitle, new Label("Credits:"), tfCredits, btnUpdate, btnClear);
	                    
	                    btnUpdate.setOnAction(updateEvent -> {
	                        try {
	                            
	                            String updatedTitle = tfTitle.getText();
	                            int updatedCredits = Integer.parseInt(tfCredits.getText());

	                            // Update the course information
	                            selectedCourse.setTitle(updatedTitle);
	                            selectedCourse.setCredits(updatedCredits);
	                            showAlert("Course information updated successfully!");

	                            // Update the course list view
	                            dataCenter.updateCourseListView(courseListView);
	                            
	                            ((Node) updateEvent.getSource()).getScene().getWindow().hide();
	                        } catch (NumberFormatException ex) {
	                            showAlert("Invalid input for Credits. Please enter a valid numeric value.");
	                        }
	                    });
	                    btnClear.setOnAction(clearEvent -> {
	                        tfTitle.clear();
	                        tfCredits.clear();
	                    });
	                    Stage editStage = new Stage();
	                    editStage.setScene(new Scene(editBox, 300, 200));
	                    editStage.showAndWait();
	                } else {
	                    showAlert("Selected course not found.");
	                }
	            } catch (NumberFormatException ex) {
	                showAlert("Invalid CRN");
	            }
	        } else {
	            showAlert("Please select a course to show information");
	        }
	    });


    vbox.getChildren().addAll(lblTitle, courseListView, btnCreate, btnDelete, btnShowInfo, btnBack);

    return new Scene(vbox, 500, 500);

	
}
	// Create Courses
	
	public Scene createCourseScene() {
	    Label lblCreateCourse = new Label("Create New Course");
	    lblCreateCourse.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

	   
	    Label lblSubject = new Label("Subject:");
	    Label lblCrn = new Label("CRN:");
	    Label lblTitle = new Label("Title:");
	    Label lblCredits = new Label("Credits:");
	    
	    TextField tfSubject = new TextField();
	    TextField tfCrn = new TextField();
	    TextField tfTitle = new TextField();
	    TextField tfCredits = new TextField();

	    Button btnCreate = new Button("Create");
	    Button btnBack = new Button("Back to Course List");
	    
	    // Styling for labels
	    lblSubject.setStyle("-fx-font-weight: bold;");
	    lblCrn.setStyle("-fx-font-weight: bold;");
	    lblTitle.setStyle("-fx-font-weight: bold;");
	    lblCredits.setStyle("-fx-font-weight: bold;");

	    // Styling for text fields
	    tfSubject.setStyle("-fx-border-color: #2196F3;");
	    tfCrn.setStyle("-fx-border-color: #2196F3;");
	    tfTitle.setStyle("-fx-border-color: #2196F3;");
	    tfCredits.setStyle("-fx-border-color: #2196F3;");

	    // Styling for buttons
	    btnCreate.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
	    btnBack.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white;");


	    btnCreate.setOnAction(e -> {
	        try {

	            String subject = tfSubject.getText();
	            int crn = Integer.parseInt(tfCrn.getText());
	            String title = tfTitle.getText();
	            int credits = Integer.parseInt(tfCredits.getText());

	            Course newCourse = new Course(subject, crn, title, credits);

	            // Add the new course to DataCenter
	            DataCenter.getInstance().createCourse(newCourse);

	            
	            showAlert("Course created successfully!");

	            // Return to the course list scene
	            setScene(browseCourseListScene(), e);
	        } catch (NumberFormatException ex) {
	            showAlert("Invalid input. Please enter valid numeric values for CRN and Credits.");
	        }
	    });
	    

	    btnBack.setOnAction(e -> {
	        setScene(browseCourseListScene(), e);
	    });

	    GridPane gridPane = new GridPane();
	    gridPane.setAlignment(Pos.CENTER);
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setStyle("-fx-background-color: #f0f0f0;");

	    // Add labels and text fields to the grid pane
	    gridPane.add(lblSubject, 0, 0);
	    gridPane.add(tfSubject, 1, 0);
	    gridPane.add(lblCrn, 0, 1);
	    gridPane.add(tfCrn, 1, 1);
	    gridPane.add(lblTitle, 0, 2);
	    gridPane.add(tfTitle, 1, 2);
	    gridPane.add(lblCredits, 0, 3);
	    gridPane.add(tfCredits, 1, 3);
	    gridPane.add(btnCreate, 0, 4);
	    gridPane.add(btnBack, 1, 4);

	    return new Scene(gridPane, 500, 500);
	}
	
	// Browse through Students
	
	
	public Scene browseStudentListScene() {
	    VBox vbox = new VBox();
	    vbox.setAlignment(Pos.CENTER);
	    vbox.setSpacing(10);
	    vbox.setStyle("-fx-background-color: #f0f0f0;"); 

	    Label lblTitle = new Label("Student List");
	    lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

	    ListView<String> studentListView = new ListView<>();
	    List<String> studentNames = DataCenter.getInstance().getStudentNames();
	    studentListView.getItems().addAll(studentNames);

	    Button btnBack = new Button("Back to Admin Scene");
	    Button btnEdit = new Button("Edit Student");
	    Button btnDelete = new Button("Delete Student");
	    Button btnShowInfo = new Button("Show-Info");
	    // Styling for labels
	    lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

	    // Styling for buttons
	    btnBack.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
	    btnEdit.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white;");
	    btnDelete.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
	    btnShowInfo.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

	    btnBack.setOnAction(e -> {
	        setScene(adminScene, e);
	    });
	    
	    btnDelete.setOnAction(deleteEvent -> {
	        // Get the selected student from the list
	        String selectedStudentInfo = studentListView.getSelectionModel().getSelectedItem();

	        if (selectedStudentInfo != null) {
	            try {
	            	
	            	String studentIdString = "";
	            	for (char c : selectedStudentInfo.toCharArray()) {
	            	    if (Character.isDigit(c)) {
	            	        studentIdString += c;
	            	    }
	            	}

	                int studentId = Integer.parseInt(studentIdString);

	                // Get the student information from the data center
	                Student selectedStudent = DataCenter.getInstance().getStudentInfo(studentId);

	                if (selectedStudent != null) {
	                    // Call the deleteStudent method with the selected student
	                	DataCenter.getInstance().deleteStudent(selectedStudent);

	                    // Refresh the student list view after deletion
	                    studentListView.getItems().remove(selectedStudentInfo);
	                } else {
	                    showAlert("Selected student not found.");
	                }
	            } catch (NumberFormatException ex) {
	                showAlert("Invalid Student ID");
	            }
	        } else {
	            showAlert("Please select a student to delete");
	        }
	    });

	    btnEdit.setOnAction(editEvent -> {
	        // Get the selected student from the list
	        String selectedStudentInfo = studentListView.getSelectionModel().getSelectedItem();

	        if (selectedStudentInfo != null) {
	            try {
	                

	            	String studentIdString = "";
	            	for (char c : selectedStudentInfo.toCharArray()) {
	            	    if (Character.isDigit(c)) {
	            	        studentIdString += c;
	            	    }
	            	}

	                int studentId = Integer.parseInt(studentIdString);

	                // Get information from the data center
	                Student selectedStudent = DataCenter.getInstance().getStudentInfo(studentId);

	                if (selectedStudent != null) {
	                    // Call the updated editStudentInformation method with the selected student
	                	
	                    editStudentInformation(selectedStudent);
	                } else {
	                    showAlert("Selected student not found.");
	                }
	            } catch (NumberFormatException ex) {
	                showAlert("Invalid Student ID");
	            }
	        } else {
	            showAlert("Please select a student to edit information");
	        }
	    });
	    
	    btnShowInfo.setOnAction(infoEvent -> {
            // Get the selected student info from the list
            String selectedStudentInfo = studentListView.getSelectionModel().getSelectedItem();

            if (selectedStudentInfo != null) {
                try {
                    
                	String studentIdString = "";
                	for (char c : selectedStudentInfo.toCharArray()) {
                	    if (Character.isDigit(c)) {
                	        studentIdString += c;
                	    }
                	}

                    int studentId = Integer.parseInt(studentIdString);

                    // Get the student information from the data center
                    Student selectedStudent = DataCenter.getInstance().getStudentInfo(studentId);

                    if (selectedStudent != null) {
                        // showStudentInfo method with the selected student
                        showStudentInfo(selectedStudent, false);
                    } else {
                        showAlert("Selected student not found.");
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Invalid Student ID");
                }
            } else {
                showAlert("Please select a student to view information");
            }
        });

        vbox.getChildren().addAll(lblTitle, studentListView, btnEdit, btnShowInfo,btnDelete, btnBack);

        return new Scene(vbox, 500, 500);
    }
	
	// Edit Student Info
	
	public void editStudentInformation(Student student) {
	    // Create ChoiceBox for course selection
		
	    ChoiceBox<Course> courseChoiceBox = new ChoiceBox<>();
	    courseChoiceBox.getItems().addAll(student.getSelectedCourse());
	    courseChoiceBox.setConverter(new StringConverter<Course>() {
	        @Override
	        public String toString(Course course) {
	            return course != null ? course.getTitle() : "";
	        }

	        @Override
	        public Course fromString(String string) {
	            
	            return null;
	        }
	    });

	    TextField tfNewStatus = new TextField(student.getStatus());
	    TextField tfNewScore = new TextField();


	    Button btnUpdate = new Button("Update");
	    Button btnClear = new Button("Clear");

	    VBox editBox = new VBox(10);
	    editBox.getChildren().addAll(
	            new Label("Select Course:"),
	            courseChoiceBox,
	            new Label("New Status:"),
	            tfNewStatus,
	            new Label("New Score:"),
	            tfNewScore,
	            btnUpdate,
	            btnClear
	    );

	    // Update the student information
	    btnUpdate.setOnAction(updateEvent -> {
	        try {
	            Course selectedCourse = courseChoiceBox.getValue();
	            if (selectedCourse != null) {
	                
	                String newStatus = tfNewStatus.getText();
	                int newScore = Integer.parseInt(tfNewScore.getText());
	                
	                controller.editStudentInfo(student.getStudentID(), newStatus, selectedCourse.getTitle(), newScore);

	                showAlert("Student information updated successfully!");

	                ((Node) updateEvent.getSource()).getScene().getWindow().hide();
	            } else {
	                showAlert("Please select a course.");
	            }
	        } catch (NumberFormatException ex) {
	            showAlert("Invalid input for Score. Please enter a valid numeric value.");
	        }
	    });
	    

	    btnClear.setOnAction(clearEvent -> {
	        tfNewStatus.clear();
	        tfNewScore.clear();
	    });

	    Stage editStage = new Stage();
	    editStage.setScene(new Scene(editBox, 300, 400));
	    editStage.showAndWait();
	}
	
	
	// Show Student Info 
	
	public void showStudentInfo(Student student, boolean refresh) {
	    VBox vbox = new VBox();
	    vbox.setAlignment(Pos.CENTER);
	    vbox.setSpacing(10);
	    vbox.setStyle("-fx-background-color: #f0f0f0;");
	    
	    Label lblTitle = new Label("Student Information");
	    lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

	    Label lblName = new Label("Name: " + student.getFirstName() + " " + student.getLastName());
	    Label lblID = new Label("Student ID: " + student.getStudentID());
	    Label lblMajor = new Label("Major: " + student.getMajor());

	    // Display enrolled courses with scores
	    VBox coursesBox = new VBox();
	    coursesBox.getChildren().add(new Label("Enrolled Courses:"));
	    for (Course course : student.getSelectedCourse()) {
	        Label lblCourse = new Label(course.getTitle() + " - Score: " + student.getScoreForCourse(course.getTitle()));
	        coursesBox.getChildren().add(lblCourse);
	    }

	    Label lblStatus = new Label("Status: " + student.getStatus());

	    // ChoiceBox for action selection
	    ChoiceBox<String> actionChoiceBox = new ChoiceBox<>();
	    actionChoiceBox.getItems().addAll("Add to Course", "Remove from Course");
	    actionChoiceBox.setValue("Add to Course");

	    // ChoiceBox for course selection
	    ChoiceBox<Course> courseChoiceBox = new ChoiceBox<>();

	    courseChoiceBox.getItems().addAll(DataCenter.getInstance().getCourseList());

	    Button btnPerformAction = new Button("Perform Action");
	    btnPerformAction.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

	    btnPerformAction.setOnAction(actionEvent -> {
	        String selectedAction = actionChoiceBox.getValue();
	        Course selectedCourse = courseChoiceBox.getValue();

	        if (selectedCourse != null) {
	            if (selectedAction.equals("Add to Course")) {
	               
	            	// Add the student to the course using DataCenter
	                DataCenter.getInstance().enrollInCourse(student.getStudentID(), selectedCourse.getCrn());
	                showAlert("Student added to course: " + selectedCourse.getTitle());
	                
	            } else if (selectedAction.equals("Remove from Course")) {
	                
	            	// Remove the student from the course using DataCenter
	            	
	                DataCenter.getInstance().removeStudentFromCourse(selectedCourse.getCrn(), student.getStudentID());
	                showAlert("Student removed from course: " + selectedCourse.getTitle());
	                
	            } else if (selectedAction.equals("Update Score")) {
	                
	            	// update Score...
	                updateScore(student, selectedCourse);
	            }
	        }
	    });

	    vbox.getChildren().addAll(lblTitle, lblName, lblID, lblMajor, coursesBox, lblStatus, actionChoiceBox, courseChoiceBox, btnPerformAction);
	    
	    Stage infoStage = new Stage();
	    infoStage.setScene(new Scene(vbox, 400, 400));
	    infoStage.showAndWait();

	    if (refresh) {
	        showStudentInfo(student, false);
	    }
	}

	// update Method
	
	public void updateScore(Student student, Course selectedCourse) {

		TextField tfNewScore = new TextField(String.valueOf(student.getScoreForCourse(selectedCourse.getSubject())));

	    Button btnUpdateScore = new Button("Update Score");
	    Button btnClearScore = new Button("Clear");

	    VBox scoreBox = new VBox(10);
	    scoreBox.getChildren().addAll(new Label("New Score:"), tfNewScore, btnUpdateScore, btnClearScore);

	    // Update the score 
	    btnUpdateScore.setOnAction(updateEvent -> {
	        try {
	            int newScore = Integer.parseInt(tfNewScore.getText());

	            student.addOrUpdateCourseScore(selectedCourse.getSubject(), newScore);

	            showAlert("Score updated successfully!");

	            ((Node) updateEvent.getSource()).getScene().getWindow().hide();
	            // Refresh the information after updating the score
	            showStudentInfo(student, true);
	        } catch (NumberFormatException ex) {
	            showAlert("Invalid input for Score. Please enter a valid numeric value.");
	        }
	    });

	   
	    btnClearScore.setOnAction(clearEvent -> {
	        tfNewScore.clear();
	    });

	    Stage scoreStage = new Stage();
	    scoreStage.setScene(new Scene(scoreBox, 300, 200));
	    scoreStage.showAndWait();
	}

		// Admin up till here
	
		// Student Scene
	
	
	    public Scene createStudentScene(Student student) {
	    
        TabPane tabPane = new TabPane();

        tabPersonalInfo = createPersonalInfoTab(student);
        tabPersonalInfo.setClosable(false);

        tabBrowseCourses = createBrowseCoursesTab(student);
        tabBrowseCourses.setClosable(false);

        tabCoursesEnrolled = createCoursesEnrolledTab(student);
        tabCoursesEnrolled.setClosable(false);

        tabPane.getTabs().addAll(tabPersonalInfo, tabBrowseCourses, tabCoursesEnrolled);

        tabPane.getStyleClass().add("tabpane-top");

        // Sign out.
        
        Button signOutButton = new Button("Sign Out");
        signOutButton.setOnAction(e -> {
            setScene(login, e);
            
        });
        
        VBox vbox = new VBox(tabPane, signOutButton);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setSpacing(10);
        vbox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20;");

       
        return new Scene(vbox, 800, 600);
    }


    public Tab createPersonalInfoTab(Student student) {
        Tab tab = new Tab("Personal Info");

        // Create labels to display personal information
        Label lblFirstName = new Label("First Name: " + student.getFirstName());
        Label lblLastName = new Label("Last Name: " + student.getLastName());
        Label lblMajor = new Label("Major: " + student.getMajor());
        Label lblPhone = new Label("Phone: " + student.getPhone());
        Label lblAddress = new Label("Address: " + student.getAddress());

        
        Button btnEdit = createEditButton(student, lblFirstName, lblLastName, lblMajor, lblPhone, lblAddress, tab);

       
        VBox infoVBox = new VBox(lblFirstName, lblLastName, lblMajor, lblPhone, lblAddress);
        infoVBox.setSpacing(10);
       
        infoVBox.setAlignment(Pos.CENTER);

       
        VBox btnVBox = new VBox(btnEdit);
      
        btnVBox.setAlignment(Pos.CENTER);

        VBox display = new VBox(infoVBox,btnVBox);
        display.setAlignment(Pos.CENTER);

        tab.setContent(display);
        display.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20;");

        return tab;
    }

    public VBox createInfoVBox(Student student, Tab tabPersonalInfo) {
       
    	// Create labels to display personal information
        Label lblFirstName = new Label("First Name: " + student.getFirstName());
        Label lblLastName = new Label("Last Name: " + student.getLastName());
        Label lblMajor = new Label("Major: " + student.getMajor());
        Label lblPhone = new Label("Phone: " + student.getPhone());
        Label lblAddress = new Label("Address: " + student.getAddress());
      
        Button btnEdit = createEditButton(student, lblFirstName, lblLastName, lblMajor, lblPhone, lblAddress, tabPersonalInfo);

        VBox vbox = new VBox(lblFirstName, lblLastName, lblMajor, lblPhone, lblAddress, btnEdit);
        
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 20;");

        return vbox;
    }

    public Button createEditButton(Student student, Label lblFirstName, Label lblLastName, Label lblMajor, Label lblPhone, Label lblAddress, Tab tabPersonalInfo) {
        Button btnEdit = new Button("Edit Personal Info");
        btnEdit.setOnAction(e -> {
          
        	//  editable text fields for each piece of personal information
            TextField tfFirstName = new TextField(student.getFirstName());
            tfFirstName.setPromptText("Enter First Name");

            TextField tfLastName = new TextField(student.getLastName());
            tfLastName.setPromptText("Enter Last Name");

            TextField tfMajor = new TextField(student.getMajor());
            tfMajor.setPromptText("Enter Major");

            TextField tfPhone = new TextField(String.valueOf(student.getPhone()));
            tfPhone.setPromptText("Enter Phone");

            TextField tfAddress = new TextField(student.getAddress());
            tfAddress.setPromptText("Enter Address");

            // Replace labels with editable text fields
            VBox editVBox = new VBox(tfFirstName, tfLastName, tfMajor, tfPhone, tfAddress);
            editVBox.setSpacing(10);
            editVBox.setAlignment(Pos.CENTER);

           
            Button btnSave = new Button("Save");
            btnSave.setOnAction(saveEvent -> {
                // Update the student information with the values from text fields
                student.setFirstName(tfFirstName.getText());
                student.setLastName(tfLastName.getText());
                student.setMajor(tfMajor.getText());
                student.setPhone(tfPhone.getText());
                student.setAddress(tfAddress.getText());

                // Update the labels with the new values
                lblFirstName.setText("First Name: " + student.getFirstName());
                lblLastName.setText("Last Name: " + student.getLastName());
                lblMajor.setText("Major: " + student.getMajor());
                lblPhone.setText("Phone: " + student.getPhone());
                lblAddress.setText("Address: " + student.getAddress());

                
                showAlert("Personal Info Updated");

                // Set the original content of the tab back
                tabPersonalInfo.setContent(createInfoVBox(student, tabPersonalInfo));
            });

           
           editVBox.getChildren().add(btnSave);

           
           tabPersonalInfo.setContent(editVBox);
        });
        btnEdit.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        return btnEdit;
    }

    public Tab createBrowseCoursesTab(Student student) {
        Tab tab = new Tab("Browse Course List");

       
        ListView<String> listView = createCourseListView(student);
        tab.setContent(listView);

        return tab;
    }

    public ListView<String> createCourseListView(Student student) {
        ObservableList<String> courses = FXCollections.observableArrayList(
                DataCenter.getInstance().getCourseNames());

        ListView<String> listView = new ListView<>(courses);
        listView.setEditable(false);

        // TextFieldListCell to allow editing of course names
        
        listView.setCellFactory(TextFieldListCell.forListView());
        listView.setStyle("-fx-background-color: #f2f2f2; -fx-border-color: #d9d9d9;");

        listView.setCellFactory(cell -> new ListCell<>() {
            Button enrollButton = new Button("Enroll");

            {
                enrollButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                enrollButton.setOnAction(event -> {
                    String selectedCourse = getItem();
                    if (selectedCourse != null) {
                        openEnrollStage(student, selectedCourse);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setGraphic(enrollButton);
                }
            }
        });

        return listView;
    }
    public void openEnrollStage(Student student, String courseName) {
        
        Stage enrollStage = new Stage();
        enrollStage.setTitle("Enroll Student");

        ChoiceBox<String> actionChoiceBox = new ChoiceBox<>();
        actionChoiceBox.getItems().addAll("Add to Course", "Remove from Course");
        actionChoiceBox.setValue("Add to Course");

        ChoiceBox<Course> courseChoiceBox = new ChoiceBox<>();
        courseChoiceBox.getItems().addAll(DataCenter.getInstance().getCourseList());

        
        Button btnEnroll = new Button("Enroll");
        btnEnroll.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        btnEnroll.setOnAction(actionEvent -> {
            String selectedAction = actionChoiceBox.getValue();
            Course selectedCourse = courseChoiceBox.getValue();

            if (selectedCourse != null) {
                // Check if the student is already enrolled in the selected course
                if (student.isEnrolledInCourse(selectedCourse.getCrn())) {
                    showAlert("Student is already enrolled in the selected course: " + selectedCourse.getTitle());
                } else {
                    if (selectedAction.equals("Add to Course")) {
                        // Add the student to the course using DataCenter
                        DataCenter.getInstance().enrollInCourse(student.getStudentID(), selectedCourse.getCrn());
                        showAlert("Student added to course: " + selectedCourse.getTitle());

                        enrollStage.close();
                    }
                }
            }
        });


        
        VBox enrollmentLayout = new VBox(10);
        enrollmentLayout.getChildren().addAll(
                new Label("Select Course:"),
                courseChoiceBox,
                btnEnroll
        );

        enrollmentLayout.setAlignment(Pos.CENTER);
        Scene enrollScene = new Scene(enrollmentLayout, 300, 200);

        enrollStage.setScene(enrollScene);
        enrollStage.show();
    }
    
    
    
    
   public Tab createCoursesEnrolledTab(Student student) {
        Tab tab = new Tab("Courses Enrolled");
        PieChart pieChart = createCoursePieChart(student);
        tab.setContent(pieChart);

        return tab;
    }

    public PieChart createCoursePieChart(Student student) {
        // Get the enrolled courses and their scores for the student
        List<Course> enrolledCourses = student.getEnrolledCoursesForStudent();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        double totalScore = 0;
        int totalCredits = 0;

        // data and calculate total score and credits
        for (Course course : enrolledCourses) {
            int score = student.getScoreForCourse(course.getTitle());
            int credits = course.getCredits();

            totalScore += score * credits;
            totalCredits += credits;

            pieChartData.add(new PieChart.Data(course.getTitle() + " (" + score + ")", score));
        }

        // Calculate GPA
        double gpa = totalCredits > 0 ? totalScore / totalCredits : 0;

        
        pieChartData.add(new PieChart.Data("GPA: " + String.format("%.2f", gpa), gpa));

        
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Enrolled Courses, Scores, and GPA");

        return pieChart;
    }




	private void showAlert(String message) {
	    Alert alert = new Alert(AlertType.CONFIRMATION);
	    alert.setContentText(message);
	    alert.showAndWait();
	}


	
	private void setScene(Scene scene, ActionEvent e) {
		Node node = (Node)e.getSource();
		Stage stage = (Stage)node.getScene().getWindow();
		stage.setScene(scene);
		
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			launch(args);
	}

}
