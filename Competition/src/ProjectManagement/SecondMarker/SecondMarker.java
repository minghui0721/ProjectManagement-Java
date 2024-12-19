package ProjectManagement.SecondMarker;

import ProjectManagement.Lecturer.Lecturer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

public class SecondMarker extends Lecturer {


    public SecondMarker(String filePath, String lectureID) {
        super(filePath, lectureID);
    }

    // Overriding the abstract method
    @Override
    public String submissionAndGradingStatus(String studentID, String projectID) {
        String gradingStatus = checkGradingStatus(studentID, projectID) ? "Graded" : "Ungraded";
        return gradingStatus;
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
}
