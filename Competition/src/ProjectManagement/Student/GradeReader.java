package ProjectManagement.Student;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import jdk.internal.net.http.common.Pair;

public class GradeReader {

    public static List<String> findGradesByStudentId(String studentId) {
        List<String> matchingGrades = new ArrayList<>();

        // Specify the path to the grading.txt file
        String filePath = "grading.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\\|"); // Split line by whitespace
                if (fields.length >= 7 && fields[1].equals(studentId)) {
                    matchingGrades.add(fields[4]); // Add the grade to the list
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }

        return matchingGrades;
    }
}
