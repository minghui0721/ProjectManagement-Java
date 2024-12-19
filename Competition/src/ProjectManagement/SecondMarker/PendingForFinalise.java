/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ProjectManagement.SecondMarker;

import ProjectManagement.Grading.GradingList;
import ProjectManagement.Supervisor.*;
import ProjectManagement.Lecturer.*;
import ProjectManagement.Login.LoginPage;
import ProjectManagement.Presentation.LecturerPending;
import ProjectManagement.Presentation.Presentation;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author mingh
 */
public class PendingForFinalise extends javax.swing.JFrame {

    private Lecturer lecturer;
    private String lecturerId;

    public PendingForFinalise(Lecturer lecturer) {

        initComponents();

        //Set lecturer
        this.lecturer = lecturer;
        this.lecturerId = lecturer.getId();
        //Read lecturer name
        readLecturerData();

        //Card
        //read total number of project
        int[] projectAndSubmissionCounts = numberOfProjectAndSubmissionHandle();
        int totalProjects = projectAndSubmissionCounts[0];
        int totalSubmissions = projectAndSubmissionCounts[1];
        // Convert the int value to a String
        String totalProjectsString = String.valueOf(totalProjects);
        String totalSubmissionString = String.valueOf(totalSubmissions);
        int totalPresentationRequest = Presentation.totalRequest(lecturer.getId());

        //Table
        //Scroll Bar
        jScrollPane1.setVerticalScrollBar(new ProjectManagement.Grading.ScrollBar());
        jScrollPane1.getVerticalScrollBar().setBackground(Color.WHITE);
        jScrollPane1.getViewport().setBackground(Color.WHITE);
        jScrollPane1.setBorder(null);

        // Modify table model based on lecturer's role
        if (lecturer.getRole().equals("Supervisor")) {
            table1.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{},
                    new String[]{
                        "ID", "Name", "Intake", "Project", "Start Date", "End Date", "Second Marker", "Status"
                    }
            ) {
                boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false, false, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            });
        }

        if (lecturer.getRole().equals("Second Marker")) {
            Object[][] rowData = getRowData();
            for (Object[] row : rowData) {
                table1.addRow(row);
            }
        } else {
            Object[][] rowData = getRowDataSupervisor();
            for (Object[] row : rowData) {
                table1.addRow(row);
            }
        }

        table1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //Searching function
        jTextField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String query = jTextField1.getText();
                table1.filterTable(query);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String query = jTextField1.getText();
                table1.filterTable(query);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Not needed for plain text components
            }
        });

        // Make the jTextField always focus
        jPanel2.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy() {
            @Override
            public Component getDefaultComponent(Container focusCycleRoot) {
                return jTextField1; // Set your search bar here
            }
        });

        // Add MouseListener to table1
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Get the selected row index
                int rowIndex = table1.getSelectedRow();
                if (rowIndex != -1) { // Ensure a row is selected
                    String project = table1.getValueAt(rowIndex, 3).toString();
                    // Find the projectID based on the projectName
                    String projectID = findProjectID(project);

                    // Redirect to GradingList with the selected projectID
                    GradingList gradingList = new GradingList(lecturer, projectID);
                    gradingList.setVisible(true);

                    // Close the current frame
                    dispose();
                }
            }
        });
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
        roundedPanel = new RoundedPanel(13);
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        roundedPanel1 = new RoundedPanel(13);
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        roundedPanel2 = new RoundedPanel(13);
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        roundedPanel3 = new RoundedPanel(13);
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
        jLabel14 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new ProjectManagement.Lecturer.Table();
        jTextField1 = new javax.swing.JTextField();

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
        setTitle("Finalise List");

        jPanel2.setBackground(new java.awt.Color(249, 249, 251,255));

        jPanel1.setBackground(new java.awt.Color(29, 37, 78));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("APE INST.");

        roundedPanel.setBackground(new java.awt.Color(29, 37, 78));
        roundedPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        roundedPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                roundedPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                roundedPanelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                roundedPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                roundedPanelMouseReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Dashboard");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/notepad (2).png"))); // NOI18N

        javax.swing.GroupLayout roundedPanelLayout = new javax.swing.GroupLayout(roundedPanel);
        roundedPanel.setLayout(roundedPanelLayout);
        roundedPanelLayout.setHorizontalGroup(
            roundedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        roundedPanelLayout.setVerticalGroup(
            roundedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(roundedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        roundedPanel1.setBackground(new java.awt.Color(29, 37, 78));
        roundedPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        roundedPanel1.setPreferredSize(new java.awt.Dimension(128, 51));
        roundedPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                roundedPanel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                roundedPanel1MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                roundedPanel1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                roundedPanel1MouseReleased(evt);
            }
        });
        roundedPanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                roundedPanel1KeyPressed(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Project");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/start-up (1).png"))); // NOI18N
        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout roundedPanel1Layout = new javax.swing.GroupLayout(roundedPanel1);
        roundedPanel1.setLayout(roundedPanel1Layout);
        roundedPanel1Layout.setHorizontalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel1Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        roundedPanel1Layout.setVerticalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        roundedPanel2.setBackground(new java.awt.Color(29, 37, 78));
        roundedPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        roundedPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                roundedPanel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                roundedPanel2MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                roundedPanel2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                roundedPanel2MouseReleased(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Presentation");

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/presentation (1).png"))); // NOI18N
        jLabel9.setText("jLabel9");

        javax.swing.GroupLayout roundedPanel2Layout = new javax.swing.GroupLayout(roundedPanel2);
        roundedPanel2.setLayout(roundedPanel2Layout);
        roundedPanel2Layout.setHorizontalGroup(
            roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );
        roundedPanel2Layout.setVerticalGroup(
            roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(roundedPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        roundedPanel3.setBackground(new java.awt.Color(89, 204, 250));

        jLabel7.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Result");

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/medical-result (1).png"))); // NOI18N
        jLabel10.setText("jLabel10");

        javax.swing.GroupLayout roundedPanel3Layout = new javax.swing.GroupLayout(roundedPanel3);
        roundedPanel3.setLayout(roundedPanel3Layout);
        roundedPanel3Layout.setHorizontalGroup(
            roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );
        roundedPanel3Layout.setVerticalGroup(
            roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel3Layout.createSequentialGroup()
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

        roundedPanel4.setBackground(new java.awt.Color(29, 37, 78));
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
                .addContainerGap(56, Short.MAX_VALUE))
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

        roundedPanel5.setBackground(new java.awt.Color(29, 37, 78));
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
                    .addComponent(roundedPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(roundedPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(roundedPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(roundedPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(40, 40, 40))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(roundedLabel)
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
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
                .addComponent(roundedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(roundedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(roundedPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(roundedPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roundedPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(roundedPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setComponentPopupMenu(jPopupMenu1);

        jLabel12.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 27)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(29, 37, 78));
        jLabel12.setText("Finalise List");

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
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel4MousePressed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 20)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(29, 37, 78));
        jLabel14.setText("GAN MING HUI");
        jLabel14.setComponentPopupMenu(jPopupMenu1);
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/logout (1).png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
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
                    .addComponent(jLabel14))
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBorder(null);

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Intake", "Project", "Start Date", "End Date", "Supervisor", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table1);

        jTextField1.setBackground(new java.awt.Color(105, 87, 233));
        jTextField1.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 12)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setText("Search by Student Name");
        jTextField1.setToolTipText("Search by Student Name");
        jTextField1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 841, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Method to find projectID based on projectName
    private String findProjectID(String projectName) {
        String projectID = null;
        try (BufferedReader reader = new BufferedReader(new FileReader("project_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 11 && parts[1].equals(projectName)) {
                    projectID = parts[0];
                    break;
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
        return projectID;
    }

    // Method to read supervisor handle from a file and store it in a map
    private Map<String, String[]> readSecondMarkerHandle() {
        Map<String, String[]> projectDetailsMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("project_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|"); // Split the line into fields using the '|' separator
                if (fields.length >= 10 && fields[6].equals(lecturer.getId())) {
                    String projectID = fields[0];
                    String projectName = fields[1];
                    String startDate = fields[3];
                    String endDate = fields[4];
                    String intake = fields[7];

                    // Create project details array
                    String[] projectDetails = {projectName, startDate, endDate, intake};

                    // Store project details in the map using project ID as key
                    projectDetailsMap.put(projectID, projectDetails);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
        return projectDetailsMap;
    }

    // Method to read supervisor handle from a file and store it in a map
    private Map<String, String[]> readSecondMarkerHandle2() {
        Map<String, String[]> projectDetailsMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("project_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|"); // Split the line into fields using the '|' separator
                if (fields.length >= 10 && fields[5].equals(lecturer.getId())) {
                    String projectID = fields[0];
                    String projectName = fields[1];
                    String startDate = fields[3];
                    String endDate = fields[4];
                    String intake = fields[7];

                    // Create project details array
                    String[] projectDetails = {projectName, startDate, endDate, intake};

                    // Store project details in the map using project ID as key
                    projectDetailsMap.put(projectID, projectDetails);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
        return projectDetailsMap;
    }

    private int[] numberOfProjectAndSubmissionHandle() {
        int totalProjects = 0; // Initialize total projects counter
        int totalSubmissions = 0; // Initialize total submissions counter
        try (BufferedReader reader = new BufferedReader(new FileReader("project_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|"); // Split the line into fields using the '|' separator
                if (fields.length >= 10 && fields[6].equals(lecturer.getId())) {

                    // Increment total projects counter
                    totalProjects++;

                    // Count submissions for the current project
                    int submissionsForProject = countSubmissionsForProject(fields[0]);
                    totalSubmissions += submissionsForProject; // Increment total submissions counter

                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");

        }

        // Return an array containing both counts
        return new int[]{totalProjects, totalSubmissions};
    }

    private int countSubmissionsForProject(String projectID) {
        Set<String> uniqueSubmissions = new HashSet<>(); // Set to store unique submission IDs
        try (BufferedReader reader = new BufferedReader(new FileReader("submission.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                // Check if project ID matches
                if (parts.length >= 6 && parts[3].equals(projectID)) {
                    String submissionID = parts[0]; // Assuming submission ID is at index 0
                    uniqueSubmissions.add(submissionID); // Add submission ID to set
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
        return uniqueSubmissions.size(); // Return the size of the set, which represents the number of unique submissions
    }

    // Define a method to get the row data
    public Object[][] getRowData() {
        // Read supervisor handle to get project details and intake
        Map<String, String[]> projectDetailsMap = readSecondMarkerHandle();

        // Iterate through the project details map to get intake and project details
        List<Object[]> rowDataList = new ArrayList<>();
        for (Map.Entry<String, String[]> entry : projectDetailsMap.entrySet()) {
            String projectID = entry.getKey(); // Get the intake from the map entry
            String[] projectDetails = entry.getValue(); // Get project details

            // Extract project details
            String projectName = projectDetails[0];
            String startDate = projectDetails[1];
            String endDate = projectDetails[2];
            String intake = projectDetails[3];

            // Retrieve student details from student.txt based on the intake
            List<String[]> studentRecords = getStudentsByIntake(intake); // Implement this method

            // Iterate through student records and add row data to the list
            for (String[] studentRecord : studentRecords) {
                String studentID = studentRecord[0];
                String fullName = studentRecord[1];

                // Check grading status
                String lecturerGradingStatus = checkGradingStatus(studentID, projectID) ? "Graded" : "Ungraded";

                // Check second marker finalise status
                String secondMarkerGradingStatus = checkSecondMarkerGradingStatus(studentID, projectID) ? "Finalised" : "Unfinalise";

                rowDataList.add(new Object[]{studentID, fullName, intake, projectName, startDate, endDate, lecturerGradingStatus, secondMarkerGradingStatus});
            }
        }

        // Convert list of row data arrays to a single array
        Object[][] rowDataArray = rowDataList.toArray(new Object[0][]);

        return rowDataArray;
    }

    // Define a method to get the row data
    public Object[][] getRowDataSupervisor() {
        // Read supervisor handle to get project details and intake
        Map<String, String[]> projectDetailsMap = readSecondMarkerHandle2();

        // Iterate through the project details map to get intake and project details
        List<Object[]> rowDataList = new ArrayList<>();
        for (Map.Entry<String, String[]> entry : projectDetailsMap.entrySet()) {
            String projectID = entry.getKey(); // Get the intake from the map entry
            String[] projectDetails = entry.getValue(); // Get project details

            // Extract project details
            String projectName = projectDetails[0];
            String startDate = projectDetails[1];
            String endDate = projectDetails[2];
            String intake = projectDetails[3];

            // Retrieve student details from student.txt based on the intake
            List<String[]> studentRecords = getStudentsByIntake(intake); // Implement this method

            // Iterate through student records and add row data to the list
            for (String[] studentRecord : studentRecords) {
                String studentID = studentRecord[0];
                String fullName = studentRecord[1];

                // Check grading status
                String secondMarkerID = findSecondMarkerID(projectID);
                String[] secondMarkerDetails = findSecondMarkerName(secondMarkerID);
                String secondMarkerName = secondMarkerDetails[0] + " " + secondMarkerDetails[1];

                // Check second marker finalise status
                String secondMarkerGradingStatus = checkSecondMarkerGradingStatus(studentID, projectID) ? "Finalised" : "Unfinalise";

                rowDataList.add(new Object[]{studentID, fullName, intake, projectName, startDate, endDate, secondMarkerName, secondMarkerGradingStatus});
            }
        }

        // Convert list of row data arrays to a single array
        Object[][] rowDataArray = rowDataList.toArray(new Object[0][]);

        return rowDataArray;
    }

// Method to check if grading exists for a student and project
    private static boolean checkGradingStatus(String studentID, String projectID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("grading.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                // Check if student ID and project ID match
                if (parts.length >= 7 && parts[1].equals(studentID) && parts[3].equals(projectID)) {
                    return true; // Graded
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");

        }
        return false; // Ungraded
    }

    // Method to check if grading exists for a student and project
    private static boolean checkSecondMarkerGradingStatus(String studentID, String projectID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("secondmarker_grading.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                // Check if student ID and project ID match
                if (parts.length >= 7 && parts[1].equals(studentID) && parts[3].equals(projectID)) {
                    return true; // Finalised
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
        return false; // haven finalised
    }

    // Method to check if grading exists for a student and project
    private static String findSecondMarkerID(String projectID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("project_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                // Check if student ID and project ID match
                if (parts.length >= 11 && parts[0].equals(projectID)) {
                    return parts[6];
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
        return null;
    }

    // Method to check if grading exists for a student and project
    private static String[] findSecondMarkerName(String secondMarkerID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("lecturer.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                // Check if student ID and project ID match
                if (parts.length >= 11 && parts[0].equals(secondMarkerID)) {
                    return new String[]{parts[2], parts[3]};
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
        return null;
    }

    // Method to retrieve student details from student.txt based on the intake
    private List<String[]> getStudentsByIntake(String intake) {
        List<String[]> studentRecords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("student.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|"); // Split the line into fields using the '|' separator
                if (fields.length >= 10 && fields[8].equals(intake)) {
                    // Check if the intake matches the specified intake
                    String studentID = fields[0];
                    String fullName = fields[2] + " " + fields[3]; // Concatenate index 2 and index 3 for full name
                    studentRecords.add(new String[]{studentID, fullName}); // Add student ID and full name to the list
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
        return studentRecords;
    }

    private void readLecturerData() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("lecturer.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|"); // Split the line into fields using the '|' separator
                if (fields.length >= 12 && fields[0].equals(lecturer.getId())) {
                    String lecturerName = fields[2] + " " + fields[3]; // Concatenate first name and last name
                    jLabel14.setText(lecturerName);
                    break; // Exit the loop after finding the record
                }
            }
            reader.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
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
        if (b == jPanel4 && c == jLabel14) {
            b.setBackground(new Color(29, 37, 78));
            c.setForeground(new Color(255, 255, 255));
        } else {
            b.setBackground(new Color(89, 204, 251));
            c.setForeground(new Color(255, 255, 255));
        }
    }

    public void resetColor(JPanel b1, JLabel c1) {
        if (b1 == jPanel4 && c1 == jLabel14) {
            b1.setBackground(new Color(255, 255, 255));
            c1.setForeground(new Color(29, 37, 78));
        } else {
            b1.setBackground(new Color(29, 37, 78));
            c1.setForeground(new Color(255, 255, 255));
        }
    }

    public void pressColor(JPanel d) {
        d.setBackground(new Color(105, 87, 233));
    }

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

    private void roundedPanel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel1MouseEntered
        // TODO add your handling code here:
        setColor(roundedPanel1, jLabel5);
    }//GEN-LAST:event_roundedPanel1MouseEntered

    private void roundedPanel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel1MouseExited
        // TODO add your handling code here:
        resetColor(roundedPanel1, jLabel5);
    }//GEN-LAST:event_roundedPanel1MouseExited

    private void roundedPanel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel2MouseEntered
        // TODO add your handling code here:
        setColor(roundedPanel2, jLabel6);
    }//GEN-LAST:event_roundedPanel2MouseEntered

    private void roundedPanel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel2MouseExited
        // TODO add your handling code here:
        resetColor(roundedPanel2, jLabel6);
    }//GEN-LAST:event_roundedPanel2MouseExited

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
        Window window = SwingUtilities.getWindowAncestor(jLabel14); // Assuming jLabel14 is contained within the frame
        window.dispose();

        //Open again the login page
        LoginPage login = new LoginPage();
        login.setVisible(true);
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
        setColor(jPanel4, jLabel14);
    }//GEN-LAST:event_jPanel4MouseEntered

    private void jPanel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseExited
        // TODO add your handling code here:
        resetColor(jPanel4, jLabel14);
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

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
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
    }//GEN-LAST:event_jLabel14MouseClicked

    private void roundedPanel1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_roundedPanel1KeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_roundedPanel1KeyPressed

    private void roundedPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel1MousePressed
        // TODO add your handling code here:
        //Change background color
        pressColor(roundedPanel1);
    }//GEN-LAST:event_roundedPanel1MousePressed

    private void roundedPanel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel1MouseReleased
        // TODO add your handling code here:

        // Close the current JFrame (enclosing frame of StudentMenu class)
        this.dispose();

        // Open the ProjectHome class
        ViewHandleProject viewHandleProject = new ViewHandleProject(lecturer);
        viewHandleProject.setVisible(true);

    }//GEN-LAST:event_roundedPanel1MouseReleased

    private void roundedPanel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel2MousePressed
        // TODO add your handling code here:
        pressColor(roundedPanel2);
    }//GEN-LAST:event_roundedPanel2MousePressed

    private void roundedPanel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel2MouseReleased
        // Close the current JFrame (enclosing frame of StudentMenu class)
        this.dispose();

        // Open the ProjectHome class
        LecturerPending pending = new LecturerPending(lecturer);
        pending.setVisible(true);


    }//GEN-LAST:event_roundedPanel2MouseReleased

    private void roundedPanel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel4MousePressed
        // TODO add your handling code here:
        pressColor(roundedPanel4);
    }//GEN-LAST:event_roundedPanel4MousePressed

    private void roundedPanel4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel4MouseReleased
        // TODO add your handling code here:
        openDefaultEmailAppWithRecipient("ganminghui0000@gmail.com");
    }//GEN-LAST:event_roundedPanel4MouseReleased

    private void roundedPanel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel5MousePressed
        // TODO add your handling code here:
        pressColor(roundedPanel5);
    }//GEN-LAST:event_roundedPanel5MousePressed

    private void roundedPanel5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel5MouseReleased
        // TODO add your handling code here:
        openWebpage("https://mediafiles.botpress.cloud/823ae6e1-c24e-4c72-9235-2ec93b927a5c/webchat/bot.html");
    }//GEN-LAST:event_roundedPanel5MouseReleased

    private void jPanel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MousePressed
        // TODO add your handling code here:
        pressColor(jPanel4);
    }//GEN-LAST:event_jPanel4MousePressed

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        // TODO add your handling code here:
        jTextField1.setText(""); // Clear the text when the field is clicked
    }//GEN-LAST:event_jTextField1FocusGained

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        if (lecturer.getRole().equals("Supervisor")) {
            ViewProfile viewProfile = new ViewProfile(lecturer);
            viewProfile.setVisible(true); // Show the dashboard
            this.dispose();
        } else {
            ViewProfile2 viewProfile2 = new ViewProfile2(lecturer);
            viewProfile2.setVisible(true); // Show the dashboard
            this.dispose();
        }


    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void roundedPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanelMouseEntered
        // TODO add your handling code here:
        setColor(roundedPanel, jLabel4);
    }//GEN-LAST:event_roundedPanelMouseEntered

    private void roundedPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanelMouseExited
        // TODO add your handling code here:
        resetColor(roundedPanel, jLabel4);
    }//GEN-LAST:event_roundedPanelMouseExited

    private void roundedPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanelMouseReleased
        // TODO add your handling code here:
        if (lecturer.getRole().equals("Supervisor")) {
            SupervisorMenu supervisor = new SupervisorMenu(lecturer);
            supervisor.setVisible(true);
            dispose();
        } else {
            SecondMarkerMenu secondMarker = new SecondMarkerMenu(lecturer);
            secondMarker.setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_roundedPanelMouseReleased

    private void roundedPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanelMousePressed
        // TODO add your handling code here:
        pressColor(roundedPanel);
    }//GEN-LAST:event_roundedPanelMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel roundedLabel;
    private javax.swing.JPanel roundedPanel;
    private javax.swing.JPanel roundedPanel1;
    private javax.swing.JPanel roundedPanel2;
    private javax.swing.JPanel roundedPanel3;
    private javax.swing.JPanel roundedPanel4;
    private javax.swing.JPanel roundedPanel5;
    private ProjectManagement.Lecturer.Table table1;
    // End of variables declaration//GEN-END:variables
}
