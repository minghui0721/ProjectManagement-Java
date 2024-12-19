/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ProjectManagement.Login;

import ProjectManagement.Admin.Admin;
import ProjectManagement.Admin.AdminDashboard;
import ProjectManagement.Admin.SuperAdmin;
import ProjectManagement.Admin.SuperAdmin;
import ProjectManagement.Lecturer.Lecturer;
import ProjectManagement.Lecturer.LecturerMenu;
import ProjectManagement.ProjectManager.ProjectManager;
import ProjectManagement.ProjectManager.ProjectManagerDashboard;
import ProjectManagement.SecondMarker.SecondMarker;
import javax.swing.JLabel;
import ProjectManagement.SecondMarker.SecondMarkerMenu;
import ProjectManagement.Student.StudentMenu;
import ProjectManagement.Student.Student;
import ProjectManagement.Supervisor.Supervisor;
import ProjectManagement.Supervisor.SupervisorMenu;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author shuen
 */
public class LoginPage extends javax.swing.JFrame {

    private int loginAttempts = 0;

    public LoginPage() {
        initComponents();
        this.setTitle("Login Page");
        userIDTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });
        // Add ActionListener to passwordTxt to handle Enter key press
        passwordTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });
        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Toggle password visibility based on checkbox state
                if (showPasswordCheckBox.isSelected()) {
                    passwordTxt.setEchoChar((char) 0); // Show password
                    passwordTxt.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12)); // Set font when showing password
                    passwordTxt.setForeground(new Color(51, 51, 51));
                } else {
                    passwordTxt.setEchoChar('*');// Hide password
                    passwordTxt.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));  // Set default font when hiding password
                    passwordTxt.setForeground(new Color(51, 51, 51));
                }
            }
        });
    }
    private void loginUser() {
        String inputUserID = userIDTxt.getText().toUpperCase(); // Convert to lowercase
        String password = passwordTxt.getText();

        if (inputUserID.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both user ID and password.");
        } else {
            String[] userData = Login.validateCredentials(inputUserID, password);

            if (userData != null) {
                String userRole = userData[0];
                String userID = userData[1];

                JOptionPane.showMessageDialog(this, "Login successful! Welcome back!");
                // Reset login attempts on successful login
                loginAttempts = 0;

               switch (userRole) {
                    case "Student":
                        String filepath = "student.txt";
                        Student student = new Student(filepath, userID);

                        if (student.getId() != null) {
                            this.dispose();
                            //need to change here, direct link to student dashboard
                            StudentMenu menu = new StudentMenu(student);
                            menu.setVisible(true);
                        } else {
                        }
                        // Fetch student data
                        //Student student = StudentDataFetcher.fetchStudentData(validatedUserID);
                        break;
                    case "Project Manager":
                        ProjectManager projectManager = new ProjectManager("lecturer.txt", userID);
                        ProjectManagerDashboard projectManagerDashboard = new ProjectManagerDashboard(projectManager);
                        projectManagerDashboard.setVisible(true); // Show the dashboard
                        this.dispose();
                        break;
                    case "Lecturer":
                        Lecturer lecturer = new Supervisor("lecturer.txt", userID);
                        LecturerMenu lecturerMenu = new LecturerMenu(lecturer);
                        lecturerMenu.setVisible(true);
                        this.dispose();
                        break;
                    case "Supervisor":
                        Supervisor supervisor = new Supervisor("lecturer.txt", userID);
                   
                        SupervisorMenu supervisorMenu = new SupervisorMenu(supervisor);
                        supervisorMenu.setVisible(true);
                        this.dispose();
                        break;
                    case "Second Marker":
                        SecondMarker secondMarker = new SecondMarker("lecturer.txt", userID);
                
                        SecondMarkerMenu secondMarkerMenu = new SecondMarkerMenu(secondMarker);  // update when second marker code is out
                        secondMarkerMenu.show();
                        dispose(); 
                        break;
                    case "Admin":
                        Admin admin = new Admin("admin.txt", userID);
                        AdminDashboard adminDashboard = new AdminDashboard(admin); 
                        adminDashboard.show();
                        dispose(); //closing the current menu
                        break;
                    case "Super Admin":
                        Admin superadmin = new Admin("admin.txt", userID);
                        SuperAdmin superDashboard = new SuperAdmin(superadmin); 
                        superDashboard.show();
                        dispose(); //closing the current menu
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Unknown user role");
                }
            } else {
                loginAttempts++;
                if (loginAttempts >= 3) {
                    JOptionPane.showMessageDialog(this, "If you have forgotten your password, kindly click 'Forgot Password' below to reset your password.");
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect login credentials. Please try again.");
                }
            }
        }
    }
    public class RoundedLabel extends JLabel {

        private int cornerRadius;

        public RoundedLabel(int cornerRadius) {
            this.cornerRadius = cornerRadius;
            setOpaque(false); // Make the label transparent
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

        jPanel1 = new javax.swing.JPanel();
        kGradientPanel2 = new keeptoo.KGradientPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        userIDTxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        passwordTxt = new javax.swing.JPasswordField();
        forgotPassLbl = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        kGradientPanel1 = new keeptoo.KGradientPanel();
        loginBtn = new javax.swing.JButton();
        bgLbl = new javax.swing.JLabel();
        roundedLabel = new RoundedLabel(999);
        showPasswordCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        kGradientPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        kGradientPanel2.setkEndColor(new java.awt.Color(153, 204, 255));
        kGradientPanel2.setkStartColor(new java.awt.Color(255, 255, 255));
        kGradientPanel2.setPreferredSize(new java.awt.Dimension(974, 550));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("User ID:");

        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Password:");

        userIDTxt.setForeground(new java.awt.Color(102, 102, 102));
        userIDTxt.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Welcome Back.");

        passwordTxt.setForeground(new java.awt.Color(51, 51, 51));
        passwordTxt.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        forgotPassLbl.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 10)); // NOI18N
        forgotPassLbl.setForeground(new java.awt.Color(102, 102, 102));
        forgotPassLbl.setText("forgot password?");
        forgotPassLbl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        forgotPassLbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                forgotPassLblMousePressed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Bradley Hand ITC", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("APE INST.");

        kGradientPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        kGradientPanel1.setkStartColor(new java.awt.Color(102, 153, 255));

        loginBtn.setBackground(new java.awt.Color(102, 102, 102, 0));
        loginBtn.setFont(new java.awt.Font("Franklin Gothic Medium", 1, 14)); // NOI18N
        loginBtn.setForeground(new java.awt.Color(255, 255, 255));
        loginBtn.setText("Log in");
        loginBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(loginBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
        );

        bgLbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Image/ApeUni.png"))); // NOI18N

        roundedLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectManagement/Submission/image (4).png"))); // NOI18N

        showPasswordCheckBox.setBackground(new java.awt.Color(255, 255, 255));
        showPasswordCheckBox.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 10)); // NOI18N
        showPasswordCheckBox.setForeground(new java.awt.Color(102, 102, 102));
        showPasswordCheckBox.setText("Show Password");
        showPasswordCheckBox.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        showPasswordCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPasswordCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(bgLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(passwordTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(userIDTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(forgotPassLbl)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(showPasswordCheckBox))))
                                .addGap(62, 62, 62))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addContainerGap())))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(82, 82, 82)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(156, 156, 156)
                                .addComponent(roundedLabel)))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
                .addComponent(roundedLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userIDTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(showPasswordCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(passwordTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(forgotPassLbl)
                .addGap(27, 27, 27)
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
            .addComponent(bgLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout kGradientPanel2Layout = new javax.swing.GroupLayout(kGradientPanel2);
        kGradientPanel2.setLayout(kGradientPanel2Layout);
        kGradientPanel2Layout.setHorizontalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(108, Short.MAX_VALUE))
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(81, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(kGradientPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 978, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtnActionPerformed
        loginUser();
    }//GEN-LAST:event_loginBtnActionPerformed

    private void forgotPassLblMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_forgotPassLblMousePressed
        ForgotPassword forgotPassword = new ForgotPassword();
        forgotPassword.show();
        dispose(); //closing the current menu
    }//GEN-LAST:event_forgotPassLblMousePressed

    private void showPasswordCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPasswordCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_showPasswordCheckBoxActionPerformed


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
            JOptionPane.showMessageDialog(null, "Error occur.");
        } catch (InstantiationException ex) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        } catch (IllegalAccessException ex) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bgLbl;
    private javax.swing.JLabel forgotPassLbl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private keeptoo.KGradientPanel kGradientPanel1;
    private keeptoo.KGradientPanel kGradientPanel2;
    private javax.swing.JButton loginBtn;
    private javax.swing.JPasswordField passwordTxt;
    private javax.swing.JLabel roundedLabel;
    private javax.swing.JCheckBox showPasswordCheckBox;
    private javax.swing.JTextField userIDTxt;
    // End of variables declaration//GEN-END:variables
}
