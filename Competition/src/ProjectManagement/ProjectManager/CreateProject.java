/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ProjectManagement.ProjectManager;

import ProjectManagement.Lecturer.Lecturer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ProjectManagement.Login.LoginPage;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;


public class CreateProject extends javax.swing.JFrame {
    private Lecturer projectManager;
    private String managerID;
    public CreateProject(Lecturer projectManager) {
        initComponents();
        this.setTitle("Create Project");
        this.projectManager= projectManager;
        managerID = projectManager.getId();
        String managerName = projectManager.getFirstName() + " " + projectManager.getLastName();
        projectManagerNameLbl.setText(managerName);
        initializeStartDateTimeSpinner();
        initializeEndDateTimeSpinner();
        ArrayList<String> intakeCodes = ProjectManager.readIntakeCodes("student.txt");
        for (String code : intakeCodes) {
            intakeTxt.addItem(code);
        }
        ArrayList<String[]> availableLecturers = ProjectManager.getAvailableLecturersandSupervisor("lecturer.txt");
        for (String[] lecturer : availableLecturers) {
            supervisorTxt.addItem(lecturer[1]); // Add the lecturer's full name to the combo box
        }
        ArrayList<String[]> availableMarkers = ProjectManager.getAvailableLecturersandSecondMarker("lecturer.txt");
        for (String[] marker : availableMarkers) {
            secondMarkerTxt.addItem(marker[1]); // Add the marker's full name to the combo box
         }

    }
    private void initializeStartDateTimeSpinner() {
        // Create a SpinnerDateModel with current date and time
        SpinnerDateModel spinnerModel = new SpinnerDateModel();
        spinnerModel.setValue(new Date());

        // Create the JSpinner with SpinnerDateModel
        StartDateTime.setModel(spinnerModel);
        StartDateTime.setModel(spinnerModel);
        // Create a DateEditor with the desired format
        JSpinner.DateEditor editor = new JSpinner.DateEditor(StartDateTime, "yyyy-MM-dd HH:mm:ss");
        StartDateTime.setEditor(editor);
    }
    private void initializeEndDateTimeSpinner() {
        // Create a SpinnerDateModel with current date and time
        SpinnerDateModel spinnerModel = new SpinnerDateModel();
        spinnerModel.setValue(new Date());

        // Create the JSpinner with SpinnerDateModel
        EndDateTime.setModel(spinnerModel);
        EndDateTime.setModel(spinnerModel);
        // Create a DateEditor with the desired format
        JSpinner.DateEditor editor = new JSpinner.DateEditor(EndDateTime, "yyyy-MM-dd HH:mm:ss");
        EndDateTime.setEditor(editor);
    }

public String getLecturerIdByName(String name, String filePath) {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length >= 12) {
                String id = parts[0];
                String fullName = parts[2] + " " + parts[3];
                if (fullName.equals(name)) {
                    return id;
                }
            }
        }
    } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
    }
    return ""; // Return empty string if ID is not found
}
private String generateProjectID() {
    // Read the last line from the file and extract the project ID
    try (BufferedReader reader = new BufferedReader(new FileReader("project_details.txt"))) {
        String line;
        String lastID = null;
        while ((line = reader.readLine()) != null) {
            lastID = line.split("\\|")[0]; // Extract the project ID from the line
        }
        if (lastID != null && !lastID.isEmpty()) {
            // Extract the numeric part and increment it
            int numericPart = Integer.parseInt(lastID.substring(1)) + 1;
            return String.format("P%03d", numericPart); // Format the ID to have leading zeros
        } else {
            return "P001"; // If file is empty or no project ID found, start with P001
        }
    } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        return null; // Return null indicating failure
    }
}


private void saveProjectToFile(String fileName) throws IOException {
    String projectID = generateProjectID();
    if (projectID == null) {
        JOptionPane.showMessageDialog(null, "Failed to generate project ID.");
        throw new IOException("Failed to generate project ID.");
    }
    String title = titleTxt.getText();
    String description = desTxt.getText();
    // Format the start date and end date
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String startDate = dateFormat.format(StartDateTime.getValue());
    String endDate = dateFormat.format(EndDateTime.getValue());
    String intakeCode = intakeTxt.getSelectedItem().toString();
    String status = statusTxt.getSelectedItem().toString();
    String document = jTextField1.getText(); // Assuming this is the document file name
    String secondMarkerStatus = "pending";

    // Check if start date is after end date
    try {
        Date start = dateFormat.parse(startDate);
        Date end = dateFormat.parse(endDate);

        if (start.after(end)) {
            JOptionPane.showMessageDialog(null, "Your Start/End date is not valid! Your start date should not be after the end date.");
            throw new IOException("Invalid start/end date");
        }
    } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        JOptionPane.showMessageDialog(null, "Error parsing dates. Please ensure dates are in the correct format (yyyy-MM-dd HH:mm:ss).");
        throw new IOException("Error parsing dates");
    }

    // Get supervisor name from the combo box
    String supervisorName = supervisorTxt.getSelectedItem().toString();
    String supervisorId = getLecturerIdByName(supervisorName, "lecturer.txt");

    // Get second marker name from the combo box
    String secondMarkerName = secondMarkerTxt.getSelectedItem().toString();
    String secondMarkerId = getLecturerIdByName(secondMarkerName, "lecturer.txt");

    // Check if supervisor and second marker are the same
    if (supervisorId.equals(secondMarkerId)) {
        JOptionPane.showMessageDialog(null, "Please select different lecturers for supervisor and second marker.");
        throw new IOException("Supervisor and second marker are the same");
    }

    ProjectManager.updateSupervisorRole(supervisorId, "lecturer.txt");

    // Write project information to file
    String projectDetails = String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s%n",
            projectID, title, description, startDate, endDate, 
            supervisorId, secondMarkerId, intakeCode, status, document, secondMarkerStatus);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
        writer.write(projectDetails);
        System.out.println("Project details saved successfully.");
    }
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
        browseBtn = new javax.swing.JButton();
        documentLbl = new javax.swing.JLabel();
        titleTxt = new javax.swing.JTextField();
        titleLbl = new javax.swing.JLabel();
        supervisorTxt = new javax.swing.JComboBox<>();
        descriptionLbl = new javax.swing.JLabel();
        startDateLbl = new javax.swing.JLabel();
        intakeTxt = new javax.swing.JComboBox<>();
        endDateLbl = new javax.swing.JLabel();
        statusTxt = new javax.swing.JComboBox<>();
        supervisorLbl = new javax.swing.JLabel();
        createBtn = new javax.swing.JButton();
        secondMarkerLbl = new javax.swing.JLabel();
        intakeCodeLbl = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        clearBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        desTxt = new javax.swing.JTextArea();
        secondMarkerTxt = new javax.swing.JComboBox<>();
        StartDateTime = new javax.swing.JSpinner();
        EndDateTime = new javax.swing.JSpinner();
        jPanel5 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();

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
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        createProjectBtn.setBackground(new java.awt.Color(204, 0, 204));
        createProjectBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/start-up (1).png"))); // NOI18N
        jLabel2.setText("jLabel2");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

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

        viewProjectBtn.setBackground(new java.awt.Color(102, 0, 102));
        viewProjectBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setComponentPopupMenu(jPopupMenu1);

        jLabel12.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 27)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(29, 37, 78));
        jLabel12.setText("Create Project");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 441, Short.MAX_VALUE)
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

        browseBtn.setText("Browse");
        browseBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        browseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseBtnActionPerformed(evt);
            }
        });

        documentLbl.setText("Upload Document:");

        titleTxt.setForeground(new java.awt.Color(51, 51, 51));

        titleLbl.setText("Project Title:");

        supervisorTxt.setBackground(new java.awt.Color(204, 204, 204));
        supervisorTxt.setForeground(new java.awt.Color(51, 51, 51));

        descriptionLbl.setText("Project Description:");

        startDateLbl.setText("Start Date:");

        intakeTxt.setBackground(new java.awt.Color(204, 204, 204));
        intakeTxt.setForeground(new java.awt.Color(51, 51, 51));

        endDateLbl.setText("End Date:");

        statusTxt.setBackground(new java.awt.Color(204, 204, 204));
        statusTxt.setForeground(new java.awt.Color(51, 51, 51));
        statusTxt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pending", "Ongoing", "Completed" }));

        supervisorLbl.setText("Supervisor:");

        createBtn.setBackground(new java.awt.Color(102, 0, 102));
        createBtn.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 14)); // NOI18N
        createBtn.setForeground(new java.awt.Color(255, 255, 255));
        createBtn.setText("Create");
        createBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        createBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createBtnActionPerformed(evt);
            }
        });

        secondMarkerLbl.setText("Second Marker:");

        intakeCodeLbl.setText("Intake Code:");

        jTextField1.setForeground(new java.awt.Color(51, 51, 51));

        clearBtn.setBackground(new java.awt.Color(255, 0, 0));
        clearBtn.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 14)); // NOI18N
        clearBtn.setForeground(new java.awt.Color(255, 255, 255));
        clearBtn.setText("Clear");
        clearBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        desTxt.setColumns(20);
        desTxt.setRows(5);
        desTxt.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane1.setViewportView(desTxt);

        secondMarkerTxt.setBackground(new java.awt.Color(204, 204, 204));
        secondMarkerTxt.setForeground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(statusLbl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(statusTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(intakeCodeLbl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(intakeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(titleLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(titleTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(descriptionLbl)
                                .addGap(26, 26, 26)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGap(251, 251, 251)
                        .addComponent(createBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(supervisorLbl)
                                    .addComponent(secondMarkerLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(startDateLbl))
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(38, 38, 38)
                                        .addComponent(supervisorTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(secondMarkerTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(StartDateTime, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(endDateLbl)
                            .addComponent(documentLbl))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(EndDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(browseBtn)))))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(titleLbl)
                        .addComponent(titleTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(supervisorLbl)
                        .addComponent(supervisorTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(descriptionLbl)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(secondMarkerLbl)
                            .addComponent(secondMarkerTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(startDateLbl)
                            .addComponent(StartDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1))
                .addGap(27, 27, 27)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(intakeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(intakeCodeLbl)
                    .addComponent(endDateLbl)
                    .addComponent(EndDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusLbl)
                    .addComponent(documentLbl)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(clearBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(createBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );

        jPanel5.setBackground(new java.awt.Color(102, 0, 102));
        jPanel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel19.setBackground(new java.awt.Color(105, 87, 233));
        jLabel19.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Project Details");

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
                .addGap(32, 32, 32)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            b.setBackground(new Color(102,0,102));
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
            b1.setBackground(new Color(102,0,102));
            c1.setForeground(new Color(255, 255, 255));
        }
    }
    public void pressColor(JPanel d){
        d.setBackground(new Color(51, 0, 51));
    }
    private void createProjectBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_createProjectBtnMouseEntered

    }//GEN-LAST:event_createProjectBtnMouseEntered

    private void createProjectBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_createProjectBtnMouseExited

    }//GEN-LAST:event_createProjectBtnMouseExited

    private void viewProjectBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewProjectBtnMouseEntered
        // TODO add your handling code here:
        setColor(viewProjectBtn, jLabel6);
    }//GEN-LAST:event_viewProjectBtnMouseEntered

    private void viewReportBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewReportBtnMouseEntered
        // TODO add your handling code here:
        setColor(viewReportBtn, jLabel7);
    }//GEN-LAST:event_viewReportBtnMouseEntered

    private void viewProjectBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewProjectBtnMouseExited
        // TODO add your handling code here:
        resetColor(viewProjectBtn, jLabel6);
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

    private void browseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseBtnActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            jTextField1.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_browseBtnActionPerformed

    private void createBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createBtnActionPerformed
    try {
        String filePath = "project_details.txt";

        // Create the file if it doesn't exist
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        // Check if any text field is empty
        if (titleTxt.getText().isEmpty() || desTxt.getText().isEmpty() || jTextField1.getText().isEmpty()) {
            // Display error message to the user
            JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            return; // Exit the method without saving to file
        }

        // Save project details to file
        saveProjectToFile(filePath);
        // Display success message
        JOptionPane.showMessageDialog(null, "Project details saved successfully.");
        ProjectManagerDashboard projectManagerDashboard = new ProjectManagerDashboard(projectManager);
        projectManagerDashboard.setVisible(true); // Show the dashboard
        this.dispose();
    } catch (IOException e) {
        if (e.getMessage().equals("Supervisor and second marker are the same")) {
            // Display error message to the user
            JOptionPane.showMessageDialog(null, "Please select different lecturers for supervisor and second marker.");
        } else {
            // Handle the error gracefully, prompt the user with an error message
            JOptionPane.showMessageDialog(null, "Error creating or saving project details. Please try again.");
        }
    }
    }//GEN-LAST:event_createBtnActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
    titleTxt.setText("");
    desTxt.setText("");
    ((JTextField) StartDateTime.getEditor().getComponent(0)).setText(""); // Clear start date
    ((JTextField) EndDateTime.getEditor().getComponent(0)).setText(""); // Clear end date
    intakeTxt.setSelectedIndex(0); // Reset intake selection
    statusTxt.setSelectedIndex(0); // Reset status selection
    jTextField1.setText(""); // Clear document text field
    supervisorTxt.setSelectedIndex(0); // Reset supervisor selection
    secondMarkerTxt.setSelectedIndex(0); // Reset second marker selection

    // Set StartDateTime to current date and time "00:00:00"
    Calendar currentDateTime = Calendar.getInstance();
    currentDateTime.set(Calendar.HOUR_OF_DAY, 0);
    currentDateTime.set(Calendar.MINUTE, 0);
    currentDateTime.set(Calendar.SECOND, 0);
    currentDateTime.set(Calendar.MILLISECOND, 0);
    StartDateTime.setValue(currentDateTime.getTime());

    // Set EndDateTime to current date and time "00:00:00"
    EndDateTime.setValue(currentDateTime.getTime());
    }//GEN-LAST:event_clearBtnActionPerformed

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

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        ViewProfile viewProfile = new ViewProfile(projectManager);
        viewProfile.setVisible(true); // Show the dashboard
        this.dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

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
            java.util.logging.Logger.getLogger(CreateProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreateProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreateProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreateProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
    private javax.swing.JSpinner EndDateTime;
    private javax.swing.JSpinner StartDateTime;
    private javax.swing.JButton browseBtn;
    private javax.swing.JButton clearBtn;
    private javax.swing.JButton createBtn;
    private javax.swing.JPanel createProjectBtn;
    private javax.swing.JPanel dashboardBtn;
    private javax.swing.JTextArea desTxt;
    private javax.swing.JLabel descriptionLbl;
    private javax.swing.JLabel documentLbl;
    private javax.swing.JLabel endDateLbl;
    private javax.swing.JLabel intakeCodeLbl;
    private javax.swing.JComboBox<String> intakeTxt;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel projectManagerNameLbl;
    private javax.swing.JLabel roundedLabel;
    private javax.swing.JPanel roundedPanel4;
    private javax.swing.JPanel roundedPanel5;
    private javax.swing.JLabel secondMarkerLbl;
    private javax.swing.JComboBox<String> secondMarkerTxt;
    private javax.swing.JLabel startDateLbl;
    private javax.swing.JLabel statusLbl;
    private javax.swing.JComboBox<String> statusTxt;
    private javax.swing.JLabel supervisorLbl;
    private javax.swing.JComboBox<String> supervisorTxt;
    private javax.swing.JLabel titleLbl;
    private javax.swing.JTextField titleTxt;
    private javax.swing.JPanel viewProjectBtn;
    private javax.swing.JPanel viewReportBtn;
    // End of variables declaration//GEN-END:variables
}
