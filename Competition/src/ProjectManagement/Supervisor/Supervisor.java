package ProjectManagement.Supervisor;

import ProjectManagement.Lecturer.Lecturer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author shuen
 */
public class Supervisor extends Lecturer {
    
    
      public Supervisor(String filePath, String lectureID){
        super(filePath, lectureID);
        
        }
      // Overriding the abstract method
      @Override
      public String submissionAndGradingStatus(String studentID, String projectID){
          String submissionStatus = checkSubmissionExists(studentID, projectID) ? "Submitted" : "Unsubmitted";
          return submissionStatus;
      }
      
      
          // Method to check if submission exists for a student and project
    private static boolean checkSubmissionExists(String studentID, String projectID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("submission.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                // Check if student ID and project ID match
                if (parts.length >= 6 && parts[1].equals(studentID) && parts[3].equals(projectID)) {
                    return true; // Submitted
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }
        return false; // Unsubmitted
    }
}
