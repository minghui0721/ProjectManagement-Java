package ProjectManagement.Admin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author shuen
 */
public class Admin {

    private String id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String gender;
    private String address;
    private String phoneNumber;
    private String intakeCode;
    private String icNumber;
    private String role;

    public Admin(String filePath, String adminID) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data[0].equals(adminID)) {
                    this.id = data[0];
                    this.firstName = data[2];
                    this.lastName = data[3];
                    this.email = data[4];
                    this.gender = data[5];
                    this.address = data[6];
                    this.phoneNumber = data[7];
                    this.icNumber = data[8];
                    this.role = data[9];
                    
                    break; // Stop reading the file once data is found
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
    }

    // SETTERS
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

    public void setIntakeCode(String intakeCode) {
        this.intakeCode = intakeCode;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }
    
    public void setRole(String role) {
        this.role = role;
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

    public String getIntakeCode() {
        return intakeCode;
    }

    public String getIcNumber() {
        return icNumber;
    }
    public String getRole() {
        return role;
    }
}
