/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ProjectManagement.ProjectManager;

import static ProjectManagement.Admin.addStudent.findIntakeCode;
import ProjectManagement.Lecturer.Lecturer;
import ProjectManagement.Login.LoginPage;
import ProjectManagement.Student.Student;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mingh
 */
public class ViewProject extends javax.swing.JFrame {

    private Lecturer projectManager;
    private String managerID;

    public ViewProject(Lecturer projectManager) {
        initComponents();
        // Add hand cursor to table
        projectTable.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //find all intake code 
        List<String> intakeCodes = findIntakeCode();

        // Add "Select Module" as the first item
        selectedIntakeCode.addItem("Select Intake Code");
        // Add modules to the selectModule JComboBox
        for (String intakeCode : intakeCodes) {
            selectedIntakeCode.addItem(intakeCode);

        }

        this.projectManager = projectManager;
        this.setTitle("View Project");
        managerID = projectManager.getId();
        String managerName = projectManager.getFirstName() + " " + projectManager.getLastName();
        projectManagerNameLbl.setText(managerName);
        //populateProjectComboBox();

        //Find existing project in the intake code
        // Add ActionListener to selectIntakeCode JComboBox
        selectedIntakeCode.addActionListener(e -> {
            //clear the select project combobox
            selectedProjectCombo.removeAllItems();
            String intakeCode = (String) selectedIntakeCode.getSelectedItem();

            if (intakeCode != "Select Intake Code") {
                //Find module according to intake code
                List<String> modules = Student.findModule(intakeCode);

                // Add modules to the selectModule JComboBox
                for (String module : modules) {
                    selectedProjectCombo.addItem(module);
                }

            } else {
                return;
            }
        });
    }

    private void populateProjectComboBox() {
        try {
            // Read project names from project_details.txt and populate the combo box
            List<String> projectNames = readProjectNamesFromFile();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(projectNames.toArray(new String[0]));
            selectedProjectCombo.setModel(model);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
            // Handle file reading error
        }
    }

    private List<String> readProjectNamesFromFile() throws IOException {
        List<String> projectNames = new ArrayList<>();
        Path filePath = Paths.get("project_details.txt");
        BufferedReader reader = Files.newBufferedReader(filePath);
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            projectNames.add(parts[1]);
        }
        reader.close();
        return projectNames;
    }

    private String[] readProjectDetails(String projectName, String intakeCode) throws IOException {
        // Read project details from project_details.txt based on project name
        Path filePath = Paths.get("project_details.txt");
        BufferedReader reader = Files.newBufferedReader(filePath);
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts[1].equals(projectName) && parts[7].equals(intakeCode)) {
                reader.close();
                return parts;
            }
        }
        reader.close();
        return new String[0];
    }

    private int[] getStudentStats(String projectID, String intakeCode) throws IOException {
        // Read student details from student.txt based on intake code
        Path studentFilePath = Paths.get("student.txt");
        BufferedReader studentReader = Files.newBufferedReader(studentFilePath);

        // Count total students
        int totalStudents = 0;
        String studentLine;
        while ((studentLine = studentReader.readLine()) != null) {
            String[] studentParts = studentLine.split("\\|");
            if (studentParts[8].equals(intakeCode)) { // Assuming intake code is at index 8
                totalStudents++;
            }
        }
        studentReader.close();

        // Read grading details from grading.txt based on projectID
        Path gradingFilePath = Paths.get("secondmarker_grading.txt");
        BufferedReader gradingReader = Files.newBufferedReader(gradingFilePath);

        // Count total pass and total fail
        int totalPass = 0;
        int totalFail = 0;
        String gradingLine;
        while ((gradingLine = gradingReader.readLine()) != null) {
            String[] gradingParts = gradingLine.split("\\|");
            if (gradingParts[3].equals(projectID)) { // Assuming projectID is at index 1
                // Check if the student passed or failed based on marks
                int marks = Integer.parseInt(gradingParts[4]); // Assuming marks is at index 4
                if (marks >= 40) {
                    totalPass++;
                } else {
                    totalFail++;
                }
            }
        }
        gradingReader.close();

        return new int[]{totalStudents, totalPass, totalFail};
    }

    private void populateProjectTable(String projectId) {
        DefaultTableModel model = (DefaultTableModel) projectTable.getModel();
        // Clear existing rows
        model.setRowCount(0);

        try {
            // Read student details based on project ID
            List<String[]> studentDetails = readStudentDetails(projectId);

            // Populate the table with student details
            for (String[] student : studentDetails) {
                model.addRow(student);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
            // Handle file reading error
        }
    }

    private List<String[]> readStudentDetails(String projectId) throws IOException {
        // First, read the intake code from the project file
        String intakeCode = null;
        Path projectFilePath = Paths.get("project_details.txt");
        try (BufferedReader projectReader = Files.newBufferedReader(projectFilePath)) {
            String projectLine;
            while ((projectLine = projectReader.readLine()) != null) {
                String[] projectParts = projectLine.split("\\|");
                if (projectParts[0].equals(projectId)) {
                    intakeCode = projectParts[7]; // Assuming intake code is at index 7
                    break;
                }
            }
        }

        if (intakeCode == null) {
            // Intake code not found for the project ID
            return new ArrayList<>();
        }

        // Now, read student details from student.txt based on the intake code
        List<String[]> studentDetails = new ArrayList<>();
        Path studentFilePath = Paths.get("student.txt");
        try (BufferedReader studentReader = Files.newBufferedReader(studentFilePath)) {
            String studentLine;
            while ((studentLine = studentReader.readLine()) != null) {
                String[] studentParts = studentLine.split("\\|");
                if (studentParts[8].equals(intakeCode)) { // Assuming intake code is at index 6
                    String studentId = studentParts[0]; // Assuming student ID is at index 0
                    String studentName = studentParts[2] + " " + studentParts[3]; // Assuming student name is at index 2 and 3

                    // Check if the student has grading information
                    boolean hasGrading = false;
                    int marks = 0;
                    String grade = "-";
                    Path gradingFilePath = Paths.get("secondmarker_grading.txt");
                    try (BufferedReader gradingReader = Files.newBufferedReader(gradingFilePath)) {
                        String gradingLine;
                        while ((gradingLine = gradingReader.readLine()) != null) {
                            String[] gradingParts = gradingLine.split("\\|");
                            if (gradingParts[1].equals(studentId) && gradingParts[3].equals(projectId)) {
                                marks = Integer.parseInt(gradingParts[4]); // Assuming marks is at index 4
                                grade = calculateGrade(marks);
                                hasGrading = true;
                                break;
                            }
                        }
                    }

                    // Add student details to the list
                    if (hasGrading) {
                        studentDetails.add(new String[]{projectId, studentId, studentName, String.valueOf(marks), grade});
                    } else {
                        studentDetails.add(new String[]{projectId, studentId, studentName, "-", "-"});
                    }
                }
            }
        }
        return studentDetails;
    }

    private String calculateGrade(int marks) {
        if (marks >= 75 && marks <= 100) {
            return "Distinction";
        } else if (marks >= 65 && marks <= 74) {
            return "Credit";
        } else if (marks >= 40 && marks <= 64) {
            return "Pass";
        } else {
            return "Fail";
        }
    }

    private String[] getStudentInfo(String studentId) throws IOException {
        // Read student details from student.txt based on studentId
        Path studentFilePath = Paths.get("student.txt");
        BufferedReader studentReader = Files.newBufferedReader(studentFilePath);
        String studentLine;
        while ((studentLine = studentReader.readLine()) != null) {
            String[] studentParts = studentLine.split("\\|");
            if (studentParts[0].equals(studentId)) { // Assuming student ID is at index 0
                studentReader.close();
                return new String[]{studentParts[2] + " " + studentParts[3], studentParts[4]}; // Assuming student name is at index 2 and 3, and other necessary info at index 4
            }
        }
        studentReader.close();
        return null; // Student not found
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dashboardBtn = new RoundedPanel(13);
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        createProjectBtn = new RoundedPanel(13);
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        viewProjectBtn = new RoundedPanel(13);
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        viewReportBtn = new RoundedPanel(13);
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        roundedLabel = new RoundedLabel(999);
        jLabel11 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        roundedPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        roundedPanel5 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        projectManagerNameLbl = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        statusLbl = new javax.swing.JLabel();
        titleLbl = new javax.swing.JLabel();
        startDateLbl = new javax.swing.JLabel();
        endDate = new javax.swing.JLabel();
        supervisorLbl = new javax.swing.JLabel();
        applyBtnBtn = new javax.swing.JButton();
        secondMarkerLbl = new javax.swing.JLabel();
        intakeCodeLbl = new javax.swing.JLabel();
        PassInt = new javax.swing.JLabel();
        FailInt = new javax.swing.JLabel();
        StudentNumInt = new javax.swing.JLabel();
        supervisorTxt = new javax.swing.JLabel();
        secondMarkerTxt = new javax.swing.JLabel();
        statusTxt = new javax.swing.JLabel();
        startDateTime = new javax.swing.JLabel();
        endDateTime = new javax.swing.JLabel();
        totalStudentInt = new javax.swing.JLabel();
        totalPassInt = new javax.swing.JLabel();
        totalFailInt = new javax.swing.JLabel();
        selectedProjectCombo = new javax.swing.JComboBox<>();
        selectedIntakeCode = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        projectTable = new ProjectManagement.ProjectManager.Table();

        jPopupMenu1.setForeground(new java.awt.Color(255, 255, 255));
        jPopupMenu1.setOpaque(false);
        jPopupMenu1.setPopupSize(null);

        jMenuItem1.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jMenuItem1.setText("Logout");
        jMenuItem1.setComponentPopupMenu(jPopupMenu1);
        jMenuItem1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuItem1.setName(""); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jMenuItem2.setText("View Profile");
        jMenuItem2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(249, 249, 251,255));

        jPanel1.setBackground(new java.awt.Color(102, 0, 102));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("APE INST.");

        dashboardBtn.setBackground(new java.awt.Color(102, 0, 102));
        dashboardBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        dashboardBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dashboardBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dashboardBtnMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dashboardBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                dashboardBtnMouseReleased(evt);
            }
        });
        dashboardBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        dashboardBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dashboardBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dashboardBtnMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dashboardBtnMousePressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Dashboard");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/notepad (2).png"))); // NOI18N
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout dashboardBtnLayout = new javax.swing.GroupLayout(dashboardBtn);
        dashboardBtn.setLayout(dashboardBtnLayout);
        dashboardBtnLayout.setHorizontalGroup(
            dashboardBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dashboardBtnLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        dashboardBtnLayout.setVerticalGroup(
            dashboardBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dashboardBtnLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(dashboardBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        createProjectBtn.setBackground(new java.awt.Color(102, 0, 102));
        createProjectBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        createProjectBtn.setPreferredSize(new java.awt.Dimension(128, 51));
        createProjectBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                createProjectBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                createProjectBtnMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                createProjectBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                createProjectBtnMouseReleased(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Create Project");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/start-up (1).png"))); // NOI18N
        jLabel2.setText("jLabel2");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout createProjectBtnLayout = new javax.swing.GroupLayout(createProjectBtn);
        createProjectBtn.setLayout(createProjectBtnLayout);
        createProjectBtnLayout.setHorizontalGroup(
            createProjectBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, createProjectBtnLayout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        createProjectBtnLayout.setVerticalGroup(
            createProjectBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createProjectBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(createProjectBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        viewProjectBtn.setBackground(new java.awt.Color(204, 0, 204));
        viewProjectBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        viewProjectBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewProjectBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewProjectBtnMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                viewProjectBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                viewProjectBtnMouseReleased(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("View Project");

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/presentation (1).png"))); // NOI18N
        jLabel9.setText("jLabel9");

        javax.swing.GroupLayout viewProjectBtnLayout = new javax.swing.GroupLayout(viewProjectBtn);
        viewProjectBtn.setLayout(viewProjectBtnLayout);
        viewProjectBtnLayout.setHorizontalGroup(
            viewProjectBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, viewProjectBtnLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );
        viewProjectBtnLayout.setVerticalGroup(
            viewProjectBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewProjectBtnLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(viewProjectBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(viewProjectBtnLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        viewReportBtn.setBackground(new java.awt.Color(102, 0, 102));
        viewReportBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        viewReportBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewReportBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewReportBtnMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                viewReportBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                viewReportBtnMouseReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("View Report");

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/medical-result (1).png"))); // NOI18N
        jLabel10.setText("jLabel10");

        javax.swing.GroupLayout viewReportBtnLayout = new javax.swing.GroupLayout(viewReportBtn);
        viewReportBtn.setLayout(viewReportBtnLayout);
        viewReportBtnLayout.setHorizontalGroup(
            viewReportBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, viewReportBtnLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );
        viewReportBtnLayout.setVerticalGroup(
            viewReportBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewReportBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(viewReportBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, viewReportBtnLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel10))
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        roundedLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/image (4).png"))); // NOI18N

        jLabel11.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("CONTACT US");
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        roundedPanel4.setBackground(new java.awt.Color(102, 0, 102));
        roundedPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        roundedPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                roundedPanel4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                roundedPanel4MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                roundedPanel4MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                roundedPanel4MouseReleased(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Email Us");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/gmail (2).png"))); // NOI18N
        jLabel3.setText("jLabel3");

        javax.swing.GroupLayout roundedPanel4Layout = new javax.swing.GroupLayout(roundedPanel4);
        roundedPanel4.setLayout(roundedPanel4Layout);
        roundedPanel4Layout.setHorizontalGroup(
            roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
        );
        roundedPanel4Layout.setVerticalGroup(
            roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(roundedPanel4Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        roundedPanel5.setBackground(new java.awt.Color(102, 0, 102));
        roundedPanel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        roundedPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                roundedPanel5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                roundedPanel5MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                roundedPanel5MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                roundedPanel5MouseReleased(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Chat Now");

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/live-chat (1).png"))); // NOI18N

        javax.swing.GroupLayout roundedPanel5Layout = new javax.swing.GroupLayout(roundedPanel5);
        roundedPanel5.setLayout(roundedPanel5Layout);
        roundedPanel5Layout.setHorizontalGroup(
            roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel5Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel17)
                .addGap(10, 10, 10)
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        roundedPanel5Layout.setVerticalGroup(
            roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel5Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(viewReportBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(viewProjectBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(createProjectBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(dashboardBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(40, 40, 40))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(roundedLabel)
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGap(0, 57, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jLabel11))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator1)
                            .addComponent(roundedPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(roundedPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(roundedLabel)))
                .addGap(50, 50, 50)
                .addComponent(dashboardBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(createProjectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(viewProjectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(viewReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(roundedPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(roundedPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setComponentPopupMenu(jPopupMenu1);

        jLabel12.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 27)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(29, 37, 78));
        jLabel12.setText("View Project");

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/image (5).png"))); // NOI18N
        jLabel13.setText("jLabel13");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setComponentPopupMenu(jPopupMenu1);
        jPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel4MouseExited(evt);
            }
        });

        projectManagerNameLbl.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 20)); // NOI18N
        projectManagerNameLbl.setForeground(new java.awt.Color(29, 37, 78));
        projectManagerNameLbl.setText("Chua Shu En");
        projectManagerNameLbl.setComponentPopupMenu(jPopupMenu1);
        projectManagerNameLbl.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        projectManagerNameLbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                projectManagerNameLblMouseClicked(evt);
            }
        });

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/logout (1).png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(projectManagerNameLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18)
                .addContainerGap(9, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18)
                    .addComponent(projectManagerNameLbl))
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 475, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        statusLbl.setText("Status:");

        titleLbl.setText("Selected Project:");

        startDateLbl.setText("Start Date and Time:");

        endDate.setText("End Date and Time:");

        supervisorLbl.setText("Supervisor:");

        applyBtnBtn.setBackground(new java.awt.Color(102, 0, 102));
        applyBtnBtn.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 14)); // NOI18N
        applyBtnBtn.setForeground(new java.awt.Color(255, 255, 255));
        applyBtnBtn.setText("Apply");
        applyBtnBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        applyBtnBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyBtnBtnActionPerformed(evt);
            }
        });

        secondMarkerLbl.setText("Second Marker:");

        intakeCodeLbl.setText("Intake Code:");

        PassInt.setText("Total Number of Pass:");

        FailInt.setText("Total Number of Fail:");

        StudentNumInt.setText("Total Number of Student:");

        supervisorTxt.setText("-");

        secondMarkerTxt.setText("-");

        statusTxt.setText("-");

        startDateTime.setText("-");

        endDateTime.setText("-");

        totalStudentInt.setText("-");

        totalPassInt.setText("-");

        totalFailInt.setText("-");

        selectedProjectCombo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        selectedProjectCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectedProjectComboActionPerformed(evt);
            }
        });

        selectedIntakeCode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        selectedIntakeCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectedIntakeCodeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(supervisorLbl)
                    .addComponent(titleLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(secondMarkerLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusLbl)
                    .addComponent(intakeCodeLbl))
                .addGap(30, 30, 30)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(secondMarkerTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(selectedProjectCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(statusTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(44, 44, 44))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(supervisorTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(selectedIntakeCode, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(startDateLbl)
                    .addComponent(endDate)
                    .addComponent(StudentNumInt)
                    .addComponent(PassInt)
                    .addComponent(FailInt))
                .addGap(30, 30, 30)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalPassInt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(endDateTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalStudentInt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(startDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(totalFailInt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(applyBtnBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(352, 352, 352))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(intakeCodeLbl)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(titleLbl)
                            .addComponent(selectedProjectCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(supervisorLbl)
                        .addGap(18, 18, 18)
                        .addComponent(secondMarkerLbl)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(statusLbl)
                            .addComponent(statusTxt)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(startDateTime)
                                .addGap(18, 18, 18)
                                .addComponent(endDateTime)
                                .addGap(18, 18, 18)
                                .addComponent(totalStudentInt)
                                .addGap(18, 18, 18)
                                .addComponent(totalPassInt)
                                .addGap(18, 18, 18)
                                .addComponent(totalFailInt))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(startDateLbl)
                                    .addComponent(selectedIntakeCode, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(endDate)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(StudentNumInt)
                                    .addComponent(supervisorTxt))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(PassInt)
                                    .addComponent(secondMarkerTxt))
                                .addGap(18, 18, 18)
                                .addComponent(FailInt)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(applyBtnBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(102, 0, 102));
        jPanel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel19.setBackground(new java.awt.Color(105, 87, 233));
        jLabel19.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Select one Project to apply");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        projectTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Project ID", "Student ID", "Student Name", "Total Marks", "Grade"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(projectTable);
        if (projectTable.getColumnModel().getColumnCount() > 0) {
            projectTable.getColumnModel().getColumn(0).setResizable(false);
            projectTable.getColumnModel().getColumn(1).setResizable(false);
            projectTable.getColumnModel().getColumn(2).setResizable(false);
            projectTable.getColumnModel().getColumn(3).setResizable(false);
            projectTable.getColumnModel().getColumn(4).setResizable(false);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 868, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    // Method to open the default email application with the pre-filled recipient email

    private void openDefaultEmailAppWithRecipient(String recipientEmail) {
        // Subject of the email (optional)
        String subject = "Subject";
        // Body of the email (optional)
        String body = "Your message";

        // Construct the mailto: URI
        String mailtoUri = String.format("mailto:%s?subject=%s&body=%s",
                recipientEmail,
                uriEncode(subject),
                uriEncode(body));

        try {
            // Open the default email application with pre-filled recipient, subject, and body
            Desktop.getDesktop().mail(new URI(mailtoUri));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
            // Handle any errors that may occur
        }
    }

    // Helper method to encode URI components
    private String uriEncode(String value) {
        try {
            return java.net.URLEncoder.encode(value, "UTF-8").replace("+", "%20");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
            // Handle encoding errors
            return "";
        }
    }

    // Method to open a web page in the default web browser
    private void openWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
            // Handle any errors that may occur
        }
    }

    public class RoundedPanel extends JPanel {

        private int cornerRadius;

        public RoundedPanel(int cornerRadius) {
            this.cornerRadius = cornerRadius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics = (Graphics2D) g.create();

            int width = getWidth();
            int height = getHeight();
            int arc = cornerRadius * 2;

            graphics.setColor(getBackground());
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.fillRoundRect(0, 0, width, height, arc, arc);

            graphics.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            // Do not paint the border
        }
    }

    public class RoundedLabel extends JLabel {

        private int cornerRadius;

        public RoundedLabel(int cornerRadius) {
            this.cornerRadius = cornerRadius;
            setOpaque(false); // Make the label transparent
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw the rounded background
            Graphics2D graphics = (Graphics2D) g.create();
            int width = getWidth();
            int height = getHeight();
            int arc = cornerRadius * 2;

            graphics.setColor(getBackground());
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.fillRoundRect(0, 0, width, height, arc, arc);
            graphics.dispose();

            // Draw the icon
            Icon icon = getIcon();
            if (icon != null) {
                int iconWidth = icon.getIconWidth();
                int iconHeight = icon.getIconHeight();
                int x = (width - iconWidth) / 2;
                int y = (height - iconHeight) / 2;
                icon.paintIcon(this, g, x, y);
            }
        }
    }

    public void setColor(JPanel b, JLabel c) {
        if (b == jPanel4 && c == projectManagerNameLbl) {
            b.setBackground(new Color(102, 0, 102));
            c.setForeground(new Color(255, 255, 255));
        } else {
            b.setBackground(new Color(204, 0, 204));
            c.setForeground(new Color(255, 255, 255));
        }
    }

    public void resetColor(JPanel b1, JLabel c1) {
        if (b1 == jPanel4 && c1 == projectManagerNameLbl) {
            b1.setBackground(new Color(255, 255, 255));
            c1.setForeground(new Color(29, 37, 78));
        } else {
            b1.setBackground(new Color(102, 0, 102));
            c1.setForeground(new Color(255, 255, 255));
        }
    }

    public void pressColor(JPanel d) {
        d.setBackground(new Color(51, 0, 51));
    }
    private void createProjectBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_createProjectBtnMouseEntered
        setColor(createProjectBtn, jLabel5);
    }//GEN-LAST:event_createProjectBtnMouseEntered

    private void createProjectBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_createProjectBtnMouseExited
        resetColor(createProjectBtn, jLabel5);
    }//GEN-LAST:event_createProjectBtnMouseExited

    private void viewProjectBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewProjectBtnMouseEntered

    }//GEN-LAST:event_viewProjectBtnMouseEntered

    private void viewReportBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewReportBtnMouseEntered
        // TODO add your handling code here:
        setColor(viewReportBtn, jLabel7);
    }//GEN-LAST:event_viewReportBtnMouseEntered

    private void viewProjectBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewProjectBtnMouseExited

    }//GEN-LAST:event_viewProjectBtnMouseExited

    private void viewReportBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewReportBtnMouseExited
        // TODO add your handling code here:
        resetColor(viewReportBtn, jLabel7);
    }//GEN-LAST:event_viewReportBtnMouseExited

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        // Perform logout actions here

        // Customize dialog appearance
        Color customColor = new Color(105, 87, 233); // RGB values for color 6957e9
        UIManager.put("OptionPane.background", customColor);
        UIManager.put("Panel.background", customColor);
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 14));

        // Show dialog message indicating logout success
        JOptionPane.showMessageDialog(null, "Logout successful!", "Logout", JOptionPane.INFORMATION_MESSAGE);

        // Close the current frame
        Window window = SwingUtilities.getWindowAncestor(projectManagerNameLbl); // Assuming jLabel14 is contained within the frame
        window.dispose();
        LoginPage loginPage = new LoginPage(); // update when lecturer code is out
        loginPage.show();
        dispose(); //closing the current menu
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void roundedPanel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel4MouseEntered
        // TODO add your handling code here:
        setColor(roundedPanel4, jLabel15);
    }//GEN-LAST:event_roundedPanel4MouseEntered

    private void roundedPanel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel4MouseExited
        // TODO add your handling code here:
        resetColor(roundedPanel4, jLabel15);
    }//GEN-LAST:event_roundedPanel4MouseExited

    private void roundedPanel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel5MouseEntered
        // TODO add your handling code here:
        setColor(roundedPanel5, jLabel16);
    }//GEN-LAST:event_roundedPanel5MouseEntered

    private void roundedPanel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel5MouseExited
        // TODO add your handling code here:
        resetColor(roundedPanel5, jLabel16);
    }//GEN-LAST:event_roundedPanel5MouseExited

    private void jPanel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseEntered
        // TODO add your handling code here:
        setColor(jPanel4, projectManagerNameLbl);
    }//GEN-LAST:event_jPanel4MouseEntered

    private void jPanel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseExited
        // TODO add your handling code here:
        resetColor(jPanel4, projectManagerNameLbl);
    }//GEN-LAST:event_jPanel4MouseExited

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        // TODO add your handling code here:
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) { // Check if left mouse button is clicked
            if (jPopupMenu1.isVisible()) {
                // If the popup menu is currently visible, hide it
                jPopupMenu1.setVisible(false);
            } else {
                // Calculate the position for showing the JPopupMenu below the JLabel
                int popupX = 55;
                int popupY = 50;

                // Show the JPopupMenu below the 
                jPopupMenu1.show(jPanel4, popupX, popupY);
            }
        }
    }//GEN-LAST:event_jPanel4MouseClicked

    private void projectManagerNameLblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_projectManagerNameLblMouseClicked
        // TODO add your handling code here:
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) { // Check if left mouse button is clicked
            if (jPopupMenu1.isVisible()) {
                // If the popup menu is currently visible, hide it
                jPopupMenu1.setVisible(false);
            } else {
                // Calculate the position for showing the JPopupMenu below the JLabel
                int popupX = 55;
                int popupY = 50;

                // Show the JPopupMenu below the 
                jPopupMenu1.show(jPanel4, popupX, popupY);
            }
        }
    }//GEN-LAST:event_projectManagerNameLblMouseClicked

    private void dashboardBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardBtnMousePressed
        pressColor(dashboardBtn);
    }//GEN-LAST:event_dashboardBtnMousePressed

    private void createProjectBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_createProjectBtnMousePressed
        pressColor(createProjectBtn);
    }//GEN-LAST:event_createProjectBtnMousePressed

    private void viewProjectBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewProjectBtnMousePressed
        pressColor(viewProjectBtn);
    }//GEN-LAST:event_viewProjectBtnMousePressed

    private void viewReportBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewReportBtnMousePressed
        pressColor(viewReportBtn);
    }//GEN-LAST:event_viewReportBtnMousePressed

    private void dashboardBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardBtnMouseExited
        resetColor(dashboardBtn, jLabel4);
    }//GEN-LAST:event_dashboardBtnMouseExited

    private void dashboardBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardBtnMouseEntered
        setColor(dashboardBtn, jLabel4);
    }//GEN-LAST:event_dashboardBtnMouseEntered

    private void createProjectBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_createProjectBtnMouseReleased
        CreateProject createproject = new CreateProject(projectManager);
        createproject.setVisible(true); // Show the dashboard
        dispose(); //closing the current menu
    }//GEN-LAST:event_createProjectBtnMouseReleased

    private void viewProjectBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewProjectBtnMouseReleased
        ViewProject viewproject = new ViewProject(projectManager);
        viewproject.setVisible(true); // Show the dashboard
        dispose(); //closing the current menu
    }//GEN-LAST:event_viewProjectBtnMouseReleased

    private void viewReportBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewReportBtnMouseReleased
        ProjectReport projectreport = new ProjectReport(projectManager);
        projectreport.setVisible(true); // Show the dashboard
        dispose(); //closing the current menu
    }//GEN-LAST:event_viewReportBtnMouseReleased

    private void dashboardBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardBtnMouseReleased
        ProjectManagerDashboard projectManagerDashboard = new ProjectManagerDashboard(projectManager);
        projectManagerDashboard.setVisible(true); // Show the dashboard
        this.dispose();
    }//GEN-LAST:event_dashboardBtnMouseReleased

    private void roundedPanel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel4MousePressed
        pressColor(roundedPanel4);
    }//GEN-LAST:event_roundedPanel4MousePressed

    private void roundedPanel4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel4MouseReleased
        openDefaultEmailAppWithRecipient("ganminghui0000@gmail.com");
    }//GEN-LAST:event_roundedPanel4MouseReleased

    private void roundedPanel5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel5MouseReleased
        openWebpage("https://mediafiles.botpress.cloud/823ae6e1-c24e-4c72-9235-2ec93b927a5c/webchat/bot.html");
    }//GEN-LAST:event_roundedPanel5MouseReleased

    private void roundedPanel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel5MousePressed
        pressColor(roundedPanel5);
    }//GEN-LAST:event_roundedPanel5MousePressed

    private void applyBtnBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyBtnBtnActionPerformed
        String selectedProjectName = (String) selectedProjectCombo.getSelectedItem();
        String intakeCode = (String) selectedIntakeCode.getSelectedItem();
        
        if (selectedProjectName != null) {
            try {
                // Read project details based on selected project name
                String[] projectDetails = readProjectDetails(selectedProjectName, intakeCode);

                // Set supervisor name, second marker name, intake code, status, start date time, and end date time
                String supervisorName = ProjectManager.getLecturerNameById(projectDetails[5]);
                supervisorTxt.setText(supervisorName);
                String secondMarkerName = ProjectManager.getLecturerNameById(projectDetails[6]);
                secondMarkerTxt.setText(secondMarkerName);
                statusTxt.setText(projectDetails[8]);
                startDateTime.setText(projectDetails[3]);
                endDateTime.setText(projectDetails[4]);

                // Count total number of students, pass, and fail based on intake code
                int[] studentStats = getStudentStats(projectDetails[0], projectDetails[7]);
                totalStudentInt.setText(String.valueOf(studentStats[0]));
                totalPassInt.setText(String.valueOf(studentStats[1]));
                totalFailInt.setText(String.valueOf(studentStats[2]));
                populateProjectTable(projectDetails[0]);
            } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
                // Handle file reading error
            }
        } else {
            JOptionPane.showMessageDialog(null, "No module selected.");
            return;
        }
    }//GEN-LAST:event_applyBtnBtnActionPerformed

    private void selectedProjectComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectedProjectComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_selectedProjectComboActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        ViewProfile viewProfile = new ViewProfile(projectManager);
        viewProfile.setVisible(true); // Show the dashboard
        this.dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void selectedIntakeCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectedIntakeCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_selectedIntakeCodeActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ViewProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel FailInt;
    private javax.swing.JLabel PassInt;
    private javax.swing.JLabel StudentNumInt;
    private javax.swing.JButton applyBtnBtn;
    private javax.swing.JPanel createProjectBtn;
    private javax.swing.JPanel dashboardBtn;
    private javax.swing.JLabel endDate;
    private javax.swing.JLabel endDateTime;
    private javax.swing.JLabel intakeCodeLbl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel projectManagerNameLbl;
    private ProjectManagement.ProjectManager.Table projectTable;
    private javax.swing.JLabel roundedLabel;
    private javax.swing.JPanel roundedPanel4;
    private javax.swing.JPanel roundedPanel5;
    private javax.swing.JLabel secondMarkerLbl;
    private javax.swing.JLabel secondMarkerTxt;
    private javax.swing.JComboBox<String> selectedIntakeCode;
    private javax.swing.JComboBox<String> selectedProjectCombo;
    private javax.swing.JLabel startDateLbl;
    private javax.swing.JLabel startDateTime;
    private javax.swing.JLabel statusLbl;
    private javax.swing.JLabel statusTxt;
    private javax.swing.JLabel supervisorLbl;
    private javax.swing.JLabel supervisorTxt;
    private javax.swing.JLabel titleLbl;
    private javax.swing.JLabel totalFailInt;
    private javax.swing.JLabel totalPassInt;
    private javax.swing.JLabel totalStudentInt;
    private javax.swing.JPanel viewProjectBtn;
    private javax.swing.JPanel viewReportBtn;
    // End of variables declaration//GEN-END:variables
}
