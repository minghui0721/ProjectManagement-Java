/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectManagement.Lecturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

/**
 *
 * @author shuen
 */
public abstract class Lecturer {

    private String id;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String phoneNumber;
    private String icNumber;
    private String address;
    private int startYearOfWorkingExperience;
    private String role;

    public Lecturer(String filePath, String lectureID) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data[0].equals(lectureID)) {
                    this.id = data[0];
                    this.firstName = data[2];
                    this.lastName = data[3];
                    this.email = data[4];
                    this.gender = data[5];
                    this.phoneNumber = data[6];
                    this.icNumber = data[7];
                    this.address = data[8];
                    this.startYearOfWorkingExperience = Integer.parseInt(data[9]);
                    this.role = data[10];

                    break; // Stop reading the file once data is found
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
    }

    public abstract String submissionAndGradingStatus(String studentID, String projectID);

    // Static method to find lecturer by module and intake code
 public static String findLecturerIDByModuleAndIntake(String selectedModule, String intakeCode) {
    try (BufferedReader br = new BufferedReader(new FileReader("project_details.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length >= 8 && parts[7].equals(intakeCode)) {
                // Remove " (Supervisor)" or " (Second Marker)" from selectedModule to compare
                String moduleName = parts[1];
                if (selectedModule.contains("Supervisor") && selectedModule.equals(moduleName + " (Supervisor)")) {
                    return parts[5]; // Assuming supervisor ID is in the 6th position
                } else if (selectedModule.contains("Second Marker") && selectedModule.equals(moduleName + " (Second Marker)")) {
                    return parts[6]; // Assuming second marker ID is in the 7th position
                }
            }
        }
    } catch (IOException e) {
             JOptionPane.showMessageDialog(null, "Error occur.");
    }
    return null; // Return null if lecturer not found
}


    public static String findLecturerName(String lecturerID) {
        try (BufferedReader br = new BufferedReader(new FileReader("lecturer.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length >= 12 && data[0].equals(lecturerID)) {
                    String lecturerName = data[2] + " " + data[3];
                    String email = data[4];
                    String gender = data[5];
                    String role = data[10];
                    //System.out.println(lecturerName);
                    return lecturerName + "|" + email + "|" + gender + "|" + role;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
        return "Lecturer not found";
    }

    public static void changeRole(String lecturerID, String newRole) {
        try (BufferedReader br = new BufferedReader(new FileReader("lecturer.txt"))) {
            String line;
            StringBuilder modifiedContent = new StringBuilder();
            boolean lecturerFound = false;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length >= 11 && data[0].equals(lecturerID)) {
                    data[10] = newRole;
                    lecturerFound = true;
                }
                modifiedContent.append(String.join("|", data)).append("\n");
            }

            if (lecturerFound) {
                try (PrintWriter writer = new PrintWriter(new FileWriter("lecturer.txt"))) {
                    writer.print(modifiedContent);
                } catch (IOException e) {
                    System.err.println("Error writing to file: " + e.getMessage());
                }
            } else {
                System.out.println("Lecturer not found.");
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    //GETTERS
    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public int getStartYearOfWorkingExperience() {
        return startYearOfWorkingExperience;
    }

    public String getRole() {
        return role;
    }

    //SETTERS
    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public void setStartYearOfWorkingExperience(int startYear) {
        this.startYearOfWorkingExperience = startYear;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
