
package ProjectManagement.Project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Project {
    private String projectId;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private String supervisorId;
    private String secondMarkerId;
    private String intakeCode;
    private String status;
    private String document;
    private String secondMarkerStatus;
    
    public void readProjectFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|"); // Split using '|'
                if (parts.length == 11) {
                    // Assign parts to corresponding fields
                } else {
                    System.out.println("Invalid line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public int countStatusOccurrences(String targetStatus) {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("project_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 11 && parts[8].trim().equals(targetStatus)) {
                    count++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return count;
    }
}
