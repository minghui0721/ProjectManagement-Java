/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ProjectManagement.Admin;

import ProjectManagement.Login.LoginPage;
import java.awt.Color;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
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
public class SuperAdmin extends javax.swing.JFrame {

    static String APNumber;
    static String editPassword;
    static String editFname;
    static String editLname;
    static String editemail;
    static String editgender;
    static String editaddress;
    static String editphone;
    static String editIC;
    static String editRole;

    private String password;
    private Admin admin;

    public SuperAdmin(Admin admin) {
        initComponents();
        this.setTitle("Super Admin");
        //When point table will show hand cursor
        AdminTable.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        this.admin = admin;
        //set admin name
        String fullname = admin.getFirstName() + " " + admin.getLastName();
        // Call the method to read data from "admin.txt" and set the name
        jLabel14.setText(fullname);
        jScrollPane1.getViewport().setBackground(Color.WHITE);

        try (BufferedReader reader = new BufferedReader(new FileReader("admin.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] components = line.split("\\|");

                if (components.length == 10) {
                    String AdminID = components[0];
                    String AdmintPassword = components[1];
                    this.password = AdmintPassword;
                    String firstName = components[2];
                    String lastName = components[3];
                    String email = components[4];
                    String gender = components[5];
                    String address = components[6];
                    String phoneNumber = components[7];
                    String IcNumber = components[8];
                    String role = components[9];
                    //Add data to table
                    AdminTable.addRow(new Object[]{AdminID, firstName, lastName, email, gender, address, phoneNumber, IcNumber, role});

                }
            }

        } catch (IOException e) {
            System.err.println("Issue in reading file: " + e.getMessage());
        }

        card1.setData(new ProjectManagement.Lecturer.Model_Card(new ImageIcon(getClass().getResource("/ProjectManagement/Image/stock.png")), "Total Project", "Add Admin", "to system"));
        card2.setData(new ProjectManagement.Lecturer.Model_Card(new ImageIcon(getClass().getResource("/ProjectManagement/Image/stock.png")), "Total Presentation", "Edit Admin", "Request"));
        card3.setData(new ProjectManagement.Lecturer.Model_Card(new ImageIcon(getClass().getResource("/ProjectManagement/Image/stock.png")), "Result Released", "Delete Admin", "Project Graded"));

        // Assuming card1 is a JPanel or any other component that supports mouse events
        card1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO add your handling code here:
                dispose();

                // Open the ProjectHome class
                AddAdmin addadmin = new AddAdmin(admin);
                addadmin.setVisible(true);
            }
        });

        // Assuming card1 is a JPanel or any other component that supports mouse events
        card2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                //link to supervisor presentation
                // TODO add your handling code here:
                DefaultTableModel model = (DefaultTableModel) AdminTable.getModel();
                int selectedRowIndex = AdminTable.getSelectedRow();

                if (selectedRowIndex == -1) {
                    // No row is selected, show a message to select a row
                    JOptionPane.showMessageDialog(null, "Please select a Admin to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //get TpNumber for edit form
                Object TpnumberObj = AdminTable.getValueAt(selectedRowIndex, 0);
                SuperAdmin.APNumber = TpnumberObj.toString();

                //get password for edit form
//        Object PasswordObj = studentTable.getValueAt(selectedRowIndex, 1);
//        AmendStudentTest.editPassword = PasswordObj.toString();
                //get first name for edit form
                Object FnameObj = AdminTable.getValueAt(selectedRowIndex, 1);
                SuperAdmin.editFname = FnameObj.toString();

                //get first name for edit form
                Object LnameObj = AdminTable.getValueAt(selectedRowIndex, 2);
                SuperAdmin.editLname = LnameObj.toString();

                //get first name for edit form
                Object emailObj = AdminTable.getValueAt(selectedRowIndex, 3);
                SuperAdmin.editemail = emailObj.toString();

                //get gender for edit form
                Object genderObj = AdminTable.getValueAt(selectedRowIndex, 4);
                SuperAdmin.editgender = genderObj.toString();

                //get address for edit form
                Object addressObj = AdminTable.getValueAt(selectedRowIndex, 5);
                SuperAdmin.editaddress = addressObj.toString();

                //get phone for edit form
                Object phoneObj = AdminTable.getValueAt(selectedRowIndex, 6);
                SuperAdmin.editphone = phoneObj.toString();

                //get intakecode for edit form
//        Object intakeCodeObj = AdminTable.getValueAt(selectedRowIndex, 7);
//        SuperAdmin.editintakecode = intakeCodeObj.toString();
                //get IC for edit form
                Object ICObj = AdminTable.getValueAt(selectedRowIndex, 7);
                SuperAdmin.editIC = ICObj.toString();

                Object roleObj = AdminTable.getValueAt(selectedRowIndex, 8);
                SuperAdmin.editRole = roleObj.toString();

                //EditStudent editStudent = new EditStudent(AmendStudentTest.TPNumber,AmendStudentTest.editPassword,AmendStudentTest.editFname,AmendStudentTest.editLname,AmendStudentTest.editemail,AmendStudentTest.editgender,  AmendStudentTest.editaddress, AmendStudentTest.editphone,AmendStudentTest.editintakecode,  AmendStudentTest.editIC );
                EditAdmin editadmin = new EditAdmin(admin, SuperAdmin.APNumber, password, SuperAdmin.editFname, SuperAdmin.editLname, SuperAdmin.editemail, SuperAdmin.editgender, SuperAdmin.editaddress, SuperAdmin.editphone, SuperAdmin.editIC, SuperAdmin.editRole);
                editadmin.setVisible(true);
                dispose();
            }
        });

        // Assuming card1 is a JPanel or any other component that supports mouse events
        card3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO add your handling code here:
                // TODO add your handling code here:
                int selectedRow = AdminTable.getSelectedRow(); // Get the index of the selected row

                if (selectedRow >= 0) { // Check if a row is selected
                    DefaultTableModel model = (DefaultTableModel) AdminTable.getModel();
                    String studentIDToDelete = (String) model.getValueAt(selectedRow, 0); // Get the student ID from the selected row

                    try {
                        // Read the contents of wthe text file
                        List<String> lines = Files.readAllLines(Paths.get("admin.txt"));

                        // Remove the line corresponding to the selected student ID
                        lines.removeIf(line -> line.startsWith(studentIDToDelete));

                        // Write the updated contents back to the text file
                        Files.write(Paths.get("admin.txt"), lines);

                        // Remove the selected row from the JTable
                        model.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(null, "Admin deleted successful !");
                    } catch (IOException ex) {
                        System.err.println("Error deleting Admin: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please Select a Person !");
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
        jMenuItem4 = new javax.swing.JMenuItem();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        AdminTable = new ProjectManagement.Admin.Table();
        jPanel6 = new javax.swing.JPanel();
        card2 = new ProjectManagement.Lecturer.Card();
        card3 = new ProjectManagement.Lecturer.Card();
        card1 = new ProjectManagement.Lecturer.Card();

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

        jMenuItem4.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        jMenuItem4.setText("View Profile");
        jMenuItem4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem4);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(249, 249, 251,255));

        jPanel1.setBackground(new java.awt.Color(253, 215, 113));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel1.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("APE INST.");

        roundedPanel.setBackground(new java.awt.Color(254, 239, 198));
        roundedPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
        jLabel4.setForeground(new java.awt.Color(29, 37, 78));
        jLabel4.setText("Dashboard");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/notepad (2).png"))); // NOI18N

        javax.swing.GroupLayout roundedPanelLayout = new javax.swing.GroupLayout(roundedPanel);
        roundedPanel.setLayout(roundedPanelLayout);
        roundedPanelLayout.setHorizontalGroup(
            roundedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        roundedPanelLayout.setVerticalGroup(
            roundedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(roundedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        roundedPanel1.setBackground(new java.awt.Color(253, 215, 113));
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
        jLabel5.setForeground(new java.awt.Color(29, 37, 78));
        jLabel5.setText("Register");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/start-up (1).png"))); // NOI18N
        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout roundedPanel1Layout = new javax.swing.GroupLayout(roundedPanel1);
        roundedPanel1.setLayout(roundedPanel1Layout);
        roundedPanel1Layout.setHorizontalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel1Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
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

        roundedPanel2.setBackground(new java.awt.Color(253, 215, 113));
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
        roundedPanel2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                roundedPanel2KeyReleased(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(29, 37, 78));
        jLabel6.setText("Project Manager");

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/presentation (1).png"))); // NOI18N
        jLabel9.setText("jLabel9");

        javax.swing.GroupLayout roundedPanel2Layout = new javax.swing.GroupLayout(roundedPanel2);
        roundedPanel2.setLayout(roundedPanel2Layout);
        roundedPanel2Layout.setHorizontalGroup(
            roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel2Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addContainerGap())
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

        roundedPanel3.setBackground(new java.awt.Color(253, 215, 113));
        roundedPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        roundedPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                roundedPanel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                roundedPanel3MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                roundedPanel3MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                roundedPanel3MouseReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(29, 37, 78));
        jLabel7.setText("Intake Code");

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
        jLabel11.setForeground(new java.awt.Color(29, 37, 78));
        jLabel11.setText("CONTACT US");
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        roundedPanel4.setBackground(new java.awt.Color(253, 215, 113));
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
        jLabel15.setForeground(new java.awt.Color(29, 37, 78));
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

        roundedPanel5.setBackground(new java.awt.Color(253, 215, 113));
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
        jLabel16.setForeground(new java.awt.Color(29, 37, 78));
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
                    .addComponent(roundedPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                    .addComponent(roundedPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(40, 40, 40))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(roundedLabel)
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGap(0, 71, Short.MAX_VALUE))
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
                .addGap(10, 10, 10)
                .addComponent(roundedPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setComponentPopupMenu(jPopupMenu1);

        jLabel12.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 27)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(29, 37, 78));
        jLabel12.setText("Super Admin Dashboard");

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
        jLabel14.setText("Lim Zi Chien");
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

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        AdminTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Admin ID", "First Name", "Last Name", "Email", "Gender", "Address", "Phone", "IC", "Role"
            }
        ));
        jScrollPane1.setViewportView(AdminTable);

        jPanel6.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        card2.setColor1(new java.awt.Color(204, 204, 204));
        card2.setColor2(new java.awt.Color(255, 204, 102));
        jPanel6.add(card2);

        card3.setColor1(new java.awt.Color(204, 255, 204));
        card3.setColor2(new java.awt.Color(102, 102, 102));
        jPanel6.add(card3);

        card1.setColor1(new java.awt.Color(255, 204, 204));
        card1.setColor2(new java.awt.Color(153, 153, 153));
        card1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                card1MouseReleased(evt);
            }
        });
        jPanel6.add(card1);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 901, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1))
                .addGap(35, 35, 35))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        if (b == jPanel4 && c == jLabel14) {
            b.setBackground(new Color(253, 215, 113));
            c.setForeground(new Color(255, 255, 255));
        } else {
            b.setBackground(new Color(254, 239, 198));
            c.setForeground(new Color(29, 37, 78));
        }
    }

    public void resetColor(JPanel b1, JLabel c1) {
        if (b1 == jPanel4 && c1 == jLabel14) {
            b1.setBackground(new Color(255, 255, 255));
            c1.setForeground(new Color(29, 37, 78));
        } else {
            b1.setBackground(new Color(253, 215, 113));
            c1.setForeground(new Color(29, 37, 78));
        }
    }

    public void pressColor(JPanel d) {
        d.setBackground(new Color(255, 203, 59));
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

    private void roundedPanel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel3MouseEntered
        // TODO add your handling code here:
        setColor(roundedPanel3, jLabel7);
    }//GEN-LAST:event_roundedPanel3MouseEntered

    private void roundedPanel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel2MouseExited
        // TODO add your handling code here:
        resetColor(roundedPanel2, jLabel6);
    }//GEN-LAST:event_roundedPanel2MouseExited

    private void roundedPanel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel3MouseExited
        // TODO add your handling code here:
        resetColor(roundedPanel3, jLabel7);
    }//GEN-LAST:event_roundedPanel3MouseExited

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
        this.dispose();

        // Open the ProjectHome class
        Register pmlec = new Register(admin);
        pmlec.setVisible(true);
    }//GEN-LAST:event_roundedPanel1MouseReleased

    private void roundedPanel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel2MousePressed
        // TODO add your handling code here:
        pressColor(roundedPanel2);
    }//GEN-LAST:event_roundedPanel2MousePressed

    private void roundedPanel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel2MouseReleased
        // TODO add your handling code here:
        this.dispose();

        // Open the ProjectHome class
        PM_Lecturer pmlec = new PM_Lecturer(admin);
        pmlec.setVisible(true);
    }//GEN-LAST:event_roundedPanel2MouseReleased

    private void roundedPanel3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel3MousePressed
        // TODO add your handling code here:
        pressColor(roundedPanel3);
    }//GEN-LAST:event_roundedPanel3MousePressed

    private void roundedPanel3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel3MouseReleased
        // TODO add your handling code here:
        this.dispose();

        // Open the ProjectHome class
        IntakeCodeTable intake = new IntakeCodeTable(admin);
        intake.setVisible(true);
    }//GEN-LAST:event_roundedPanel3MouseReleased

    private void roundedPanel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel4MousePressed
        // TODO add your handling code here:
        pressColor(roundedPanel4);
    }//GEN-LAST:event_roundedPanel4MousePressed

    private void roundedPanel4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel4MouseReleased
        openDefaultEmailAppWithRecipient("ganminghui0000@gmail.com");
    }//GEN-LAST:event_roundedPanel4MouseReleased

    private void roundedPanel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel5MousePressed
        // TODO add your handling code here:
        pressColor(roundedPanel5);
    }//GEN-LAST:event_roundedPanel5MousePressed

    private void roundedPanel5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanel5MouseReleased
        openWebpage("https://mediafiles.botpress.cloud/823ae6e1-c24e-4c72-9235-2ec93b927a5c/webchat/bot.html");
    }//GEN-LAST:event_roundedPanel5MouseReleased

    private void jPanel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MousePressed
        // TODO add your handling code here:
        pressColor(jPanel4);
    }//GEN-LAST:event_jPanel4MousePressed

    private void roundedPanel2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_roundedPanel2KeyReleased
        // TODO add your handling code here:
        this.dispose();

        PM_Lecturer pmlec = new PM_Lecturer(admin);
        pmlec.setVisible(true);
    }//GEN-LAST:event_roundedPanel2KeyReleased

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        ViewProfile viewProfile = new ViewProfile(admin);
        viewProfile.setVisible(true);
        dispose();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void roundedPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanelMousePressed
        pressColor(roundedPanel);
    }//GEN-LAST:event_roundedPanelMousePressed

    private void roundedPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanelMouseReleased
        String role = admin.getRole();

        if (role.equals("Admin")) {
            AdminDashboard adminDashboard = new AdminDashboard(admin);
            adminDashboard.show();
            this.dispose(); //closing the current menu
        } else if (role.equals("Super Admin")) {
            SuperAdmin superDashboard = new SuperAdmin(admin);
            superDashboard.show();
            this.dispose(); //closing the current menu
        } else {
            JOptionPane.showMessageDialog(null, "Error occur");
        }
    }//GEN-LAST:event_roundedPanelMouseReleased

    private void roundedPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanelMouseExited

    }//GEN-LAST:event_roundedPanelMouseExited

    private void roundedPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roundedPanelMouseEntered

    }//GEN-LAST:event_roundedPanelMouseEntered

    private void card1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_card1MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_card1MouseReleased

    /**
     * @param args the command line arguments
     */
    public String getTPnumber() {

        return SuperAdmin.APNumber;
    }

    public String geteditPassword() {

        return SuperAdmin.editPassword;
    }

    public String geteditFname() {

        return SuperAdmin.editFname;
    }

    public String geteditLname() {

        return SuperAdmin.editLname;
    }

    public String geteditemail() {

        return SuperAdmin.editemail;
    }

    public String geteditgender() {

        return SuperAdmin.editgender;
    }

    public String geteditaddess() {

        return SuperAdmin.editaddress;
    }

    public String geteditphone() {

        return SuperAdmin.editphone;
    }

    public String geteditIC() {

        return SuperAdmin.editIC;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ProjectManagement.Admin.Table AdminTable;
    private ProjectManagement.Lecturer.Card card1;
    private ProjectManagement.Lecturer.Card card2;
    private ProjectManagement.Lecturer.Card card3;
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
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel roundedLabel;
    private javax.swing.JPanel roundedPanel;
    private javax.swing.JPanel roundedPanel1;
    private javax.swing.JPanel roundedPanel2;
    private javax.swing.JPanel roundedPanel3;
    private javax.swing.JPanel roundedPanel4;
    private javax.swing.JPanel roundedPanel5;
    // End of variables declaration//GEN-END:variables
}