package ProjectManagement.Login;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Login {
    //Login Funcition
    public static String[] validateCredentials(String userID, String password) {
        try {
            // Check if user is a student
            if (userID.startsWith("TP")) {
                try (BufferedReader br = new BufferedReader(new FileReader("student.txt"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split("\\|");
                        if (parts.length >= 9 && parts[0].equals(userID) && parts[1].equals(password)) {
                            return new String[] { "Student", userID };
                        }
                    }
                }
            } else if (userID.startsWith("SP")) {
                // Check if user is a lecturer, project manager, or second marker
                try (BufferedReader br = new BufferedReader(new FileReader("lecturer.txt"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split("\\|");
                        if (parts.length >= 12 && parts[0].equals(userID) && parts[1].equals(password)) {
                            if (parts[10].equals("Project Manager") || parts[10].equals("Lecturer") || parts[10].equals("Second Marker") || parts[10].equals("Supervisor")) {
                                return new String[] { parts[10], userID };
                            }
                        }
                    }
                }
            } else if (userID.startsWith("AP")) {
                try (BufferedReader br = new BufferedReader(new FileReader("admin.txt"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split("\\|");
                        if (parts.length >= 7 && parts[0].equals(userID) && parts[1].equals(password)) {
                            if (parts[9].equals("Admin") || parts[9].equals("Super Admin")) {
                                return new String[] { parts[9], userID };
                            }
                            
                        }
                    }
                } 
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }

        return null; // Credentials not found or invalid
    }

    //Password Reset Function
    public static boolean verifyEmail(String email, String userID) {
        String fileName;
        if (userID.startsWith("TP")) {
            fileName = "student.txt";
        } else if (userID.startsWith("SP")) {
            fileName = "lecturer.txt";
        } else if (userID.startsWith("AP")) {
            fileName = "admin.txt";
        } else {
            return false; // Invalid user ID prefix
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2 && parts[0].equals(userID) && parts[4].equals(email)) {
                    return true; // Email verified
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }

        return false; // Email not verified
    }
    
    public static boolean resetPassword(String email, String userID, String newPassword) {
        String fileName;
        if (userID.startsWith("TP")) {
            fileName = "student.txt";
        } else if (userID.startsWith("SP")) {
            fileName = "lecturer.txt";
        } else if (userID.startsWith("AP")){
            fileName = "admin.txt";
        } else {
            return false; // Invalid user ID prefix
        }

        // Read all lines from the file into a list
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
            return false; // Error reading file
        }

        // Update the password for the user with the specified email and userID
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2 && parts[0].equals(userID) && parts[4].equals(email)) {
                    parts[1] = newPassword; // Assuming password is at index 2 in the line
                    line = String.join("|", parts);
                }
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
            return false; // Error writing to file
        }

        System.out.println("Password reset for email: " + email + ", userID: " + userID + " to: " + newPassword);
        return true; // Password reset successful
    }
}
