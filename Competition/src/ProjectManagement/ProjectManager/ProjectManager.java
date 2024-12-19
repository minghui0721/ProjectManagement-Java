
package ProjectManagement.ProjectManager;

import ProjectManagement.Lecturer.Lecturer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;

public class ProjectManager extends Lecturer{
    private static String managerID;
    private static String availability;

    public ProjectManager(String filePath, String lectureID){
        super(filePath, lectureID);
    }
    
        @Override
    public String submissionAndGradingStatus(String studentID, String projectID) {
        // Default or placeholder implementation
        return "Not applicable for ProjectManager";
    }
    
    // Setter and Getter for managerID
    public static void setManagerID(String id) {
        managerID = id;
    }

    public static String getManagerID() {
        return managerID;
    }

    // Setter and Getter for availability
    public static void setAvailability(String availability) {
        ProjectManager.availability = availability;
    }

    public static String getAvailability() {
        return availability;
    }

    public static ArrayList<String[]> readAllLecturerData(String filePath) {
        ArrayList<String[]> lecturers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 11) { // Assuming all properties are present
                    String id = parts[0];
                    String password = parts[1];
                    String firstName = parts[2];
                    String lastName = parts[3];
                    String email = parts[4];
                    String gender = parts[5];
                    String phoneNumber = parts[6];
                    String icNumber = parts[7];
                    String address = parts[8];
                    int startYearOfWorkingExperience = Integer.parseInt(parts[9]);
                    String role = parts[10];
                    String availability = parts[11];
                    lecturers.add(new String[]{id, password, firstName, lastName, email, gender, phoneNumber,
                            icNumber, address, String.valueOf(startYearOfWorkingExperience), role, availability});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
        return lecturers;
    }
// Create Project - to rstore Intake code into combobox
        public static ArrayList<String> readIntakeCodes(String filePath) {
        ArrayList<String> intakeCodes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 9) {
                    String intakeCode = parts[8];
                    if (!intakeCodes.contains(intakeCode)) {
                        intakeCodes.add(intakeCode);
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
        return intakeCodes;
    }
// to store lecturer name into combobox
        public static ArrayList<String[]> getAvailableLecturersandSupervisor(String filePath) {
        ArrayList<String[]> lecturers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                    if ((parts.length >= 12 && parts[10].equals("Supervisor")) || (parts.length >= 12 && parts[10].equals("Lecturer"))) {
                    String id = parts[0];
                    String name = parts[2] + " " + parts[3];
                    lecturers.add(new String[]{id, name});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
        return lecturers;
    }
// to store both lecturer and second marker into combobox
public static ArrayList<String[]> getAvailableLecturersandSecondMarker(String filePath) {
    ArrayList<String[]> lecturers = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length >= 10) {
                String id = parts[0];
                String name = parts[2] + " " + parts[3];
                String role = parts[10]; // Assuming role is at index 10
                if (role.equals("Lecturer")) {
                    lecturers.add(new String[]{id, name});
                } else if (role.equals("Second Marker")) {
                    lecturers.add(new String[]{id, name});
                }
            }
        }
    } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
    }
    return lecturers;
}
// to update lecturer role to Project Manager/Second Marker
    public static void updateSupervisorRole(String supervisorId, String filePath) {
        updateLecturerRole(supervisorId, "Supervisor", filePath);
    }

    public static void updateSecondMarkerRole(String secondMarkerId, String filePath) {
        updateLecturerRole(secondMarkerId, "Second Marker", filePath);
    }
    public static String getSupervisorName(String projectId) {
        String supervisorName = null;
        try (BufferedReader br = new BufferedReader(new FileReader("project_details.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 11 && parts[0].trim().equals(projectId.trim())) {
                    String supervisorId = parts[5].trim(); // Extract supervisor ID
                    supervisorName = getLecturerNameById(supervisorId); // Get supervisor name
                    
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
        return supervisorName;
    }

    // Method to get the second marker name based on the project ID
    public static String getSecondMarkerName(String projectId) {
     
        try (BufferedReader br = new BufferedReader(new FileReader("project_details.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 11 && parts[0].trim().equals(projectId.trim())) {
                    String secondMarkerId = parts[6].trim(); // Extract second marker ID
                    String secondMarkerName = getLecturerNameById(secondMarkerId); // Get second marker name
                    return secondMarkerName;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
        return null;
    }
        public static String getLecturerNameById(String lecturerId) {
        String lecturerName = null;
        try (BufferedReader br = new BufferedReader(new FileReader("lecturer.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 11 && parts[0].trim().equals(lecturerId.trim())) {
                    String firstName = parts[2].trim();
                    String lastName = parts[3].trim();
                    lecturerName = firstName + " " + lastName; // Combine first name and last name
                    return lecturerName;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
        return lecturerName;
    }
    private static void updateLecturerRole(String lecturerId, String newRole, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(filePath + ".tmp"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 11 && parts[0].equals(lecturerId)) {
                    // Update role
                    parts[10] = newRole;
                    parts[11] = "NULL";
                    line = String.join("|", parts);
                }
                bw.write(line + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to update role.");
            return;
        }

        // Delete the original file
        File originalFile = new File(filePath);
        if (!originalFile.delete()) {
            JOptionPane.showMessageDialog(null, "Failed to delete original file.");
            return;
        }

        // Rename the temporary file to the original file
        File tempFile = new File(filePath + ".tmp");
        if (!tempFile.renameTo(originalFile)) {
            JOptionPane.showMessageDialog(null, "Failed to rename temporary file.");
        }
    }
public static ArrayList<String[]> getAvailableLecturersandSecondMarkerForEdit(String filePath) {
    ArrayList<String[]> lecturers = new ArrayList<>();
    HashSet<String> lecturerNames = new HashSet<>(); // HashSet to store unique lecturer names
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length >= 10) {
                String id = parts[1];
                String name = parts[2] + " " + parts[3];
                String role = parts[10]; 
                if (role.equals("Lecturer") && parts[11].equals("Available")) {
                    if (!lecturerNames.contains(name)) { // Check if lecturer name is not already added
                        lecturers.add(new String[]{id, name});
                        lecturerNames.add(name); // Add lecturer name to HashSet
                    }
                } else if (role.equals("Second Marker") && !lecturerNames.contains(name)) {
                    lecturers.add(new String[]{id, name});
                    lecturerNames.add(name); // Add second marker name to HashSet
                }
            }
        }
    } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
    }
    return lecturers;
}
private String getSecondMarkerStatus(String projectId) {
    String secondMarkerStatus = "";
    try (BufferedReader reader = new BufferedReader(new FileReader("project_details.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length >= 10 && parts[0].equals(projectId)) {
                System.out.println(parts[10]);
                return parts[10]; // Extract the second marker status
            }
        }
    } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
    }
    return secondMarkerStatus; // Return empty string if status is not found
}

}
