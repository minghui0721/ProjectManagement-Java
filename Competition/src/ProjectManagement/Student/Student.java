package ProjectManagement.Student;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shuen
 */
public class Student {

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

    public Student(String filePath, String studentID) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data[0].equals(studentID)) {
                    this.id = data[0];
                    this.firstName = data[2];
                    this.lastName = data[3];
                    this.email = data[4];
                    this.gender = data[5];
                    this.address = data[6];
                    this.phoneNumber = data[7];
                    this.intakeCode = data[8];
                    this.icNumber = data[9];
                    
                    break; // Stop reading the file once data is found
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
public static List<String> findModule(String intakeCode) {
    List<String> modules = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader("project_details.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length >= 8 && parts[7].equals(intakeCode)) {
                // Assuming modules are listed in the sixth field (index 5)
                modules.add(parts[1]);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    // Join the module names into a single string
    return modules;
}

// Find supervisor or second marker
public static String findModuleAccordingRole(String intakeCode) {
    StringBuilder module = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader("project_details.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length >= 8 && parts[7].toLowerCase().contains(intakeCode.toLowerCase())) {
                // Assuming modules are listed in the second field (index 1)
                String bySecondMarker = parts[1] + " (Second Marker)";
                String bySupervisor = parts[1] + " (Supervisor)";
                
                if (module.length() > 0) {
                    module.append("|");
                }
                module.append(bySecondMarker).append("|").append(bySupervisor);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return module.toString();
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
}
