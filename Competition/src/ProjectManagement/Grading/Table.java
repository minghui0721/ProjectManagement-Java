/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectManagement.Grading;

import ProjectManagement.Lecturer.Lecturer;
import ProjectManagement.Submission.CourseReader;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author mingh
 */
public class Table extends JTable {

    private Lecturer lecturer;
    private String projectID;
    Date dueDate;

    public Table() {
        // Default constructor
    }

    public Table(Lecturer lecturer, String projectID) {

        this.lecturer = lecturer;
        this.projectID = projectID;

        try (BufferedReader reader = new BufferedReader(new FileReader("project_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Corrected date format
                // Check if project ID match
                if (parts.length >= 11 && parts[0].equals(projectID)) {
                    String endDateStr = parts[4];
                    try {
                        dueDate = dateFormat.parse(endDateStr);
                    } catch (ParseException e) {
                        JOptionPane.showMessageDialog(null, "Error occur.");
                        // Handle parsing exception
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occur.");
        }

        Date currentDate = new Date();

        setShowHorizontalLines(true);
        setGridColor(new Color(230, 230, 230));
        setRowHeight(40);

        // Add mouse listener to handle row clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Get the selected row and column
                int row = rowAtPoint(e.getPoint());
                int column = columnAtPoint(e.getPoint());

                // Check if the click was on a valid row
                if (row >= 0 && column >= 0) {
                    // Get the value of the clicked cell
                    Object value = getValueAt(row, column);

                    // Extract student information from the clicked row
                    String id = (String) getValueAt(row, 1);

                    // Check if the student submission status
                    String status = (String) getValueAt(row, 3);

                    // Check if the grade is "Ungraded" and the user is a second marker
                    String grade = (String) getValueAt(row, 5);

                    if (lecturer.getRole().equals("Second Marker") && "Ungraded".equals(grade)) {
                        JOptionPane.showMessageDialog(null, "Grading not allowed because the supervisor hasn't marked yet.");
                    } else {
                        JFrame frame = (JFrame) SwingUtilities.getRoot(Table.this); // Use Table.this to refer to the instance of Table
                        frame.dispose();

                        if (graded(id)) {
                            EditGrade editGrade = new EditGrade(lecturer, projectID, id);
                            editGrade.setVisible(true);
                        } else {
                            LecturerViewSubmission view = new LecturerViewSubmission(lecturer, projectID, id);
                            view.setVisible(true);
                        }
                    }

                }
            }
        });

        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                TableHeader header = new TableHeader(o + "");
                if (i1 == 3) {
                    header.setHorizontalAlignment(JLabel.CENTER);
                }
                return header;
            }
        });
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean selected, boolean bln1, int i, int i1) {
                if (i1 != 3) {
                    Component com = super.getTableCellRendererComponent(jtable, o, selected, bln1, i, i1);
                    com.setBackground(Color.WHITE);
                    setBorder(noFocusBorder);
                    if (selected) {
                        com.setForeground(new Color(15, 89, 140));
                    } else {
                        com.setForeground(new Color(102, 102, 102));
                    }
                    return com;
                } else {
                    // Convert the String representation to StatusType enum instance
                    String statusTypeString = (String) o;
                    StatusType type = StatusType.valueOf(statusTypeString);

                    // Create a CellStatus panel
                    CellStatus cell = new CellStatus(type);

                    // Create a panel to hold the CellStatus panel
                    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    panel.setBackground(Color.WHITE);
                    panel.add(cell);

                    return panel;
                }
            }
        });
    }

    public Boolean graded(String studentID) {
        //To check is it graded
        // u need student ID & Project ID
        List<String> gradings = CourseReader.readCoursesFromFile("grading.txt");

        // Output the retrieved courses for verification
        for (String grading : gradings) {
            String[] parts = grading.split("\\|");
            if (parts.length >= 7 && parts[1].equals(studentID) && parts[3].equals(projectID)) {
                return true;
            }
        }
        return false;
    }

    public TableModel getCustomModel() {
        return super.getModel();
    }

    public void setCustomRowSorter(RowSorter<? extends TableModel> sorter) {
        setRowSorter(sorter);
    }

    public void filterTable(String query) {
        if (query == null) {
            JOptionPane.showMessageDialog(null, "Query cannot be null.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Escape backslashes properly for regex
            query = query.replace("\\", "\\\\");
            DefaultTableModel model = (DefaultTableModel) getModel();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            setRowSorter(sorter);
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 1));
        } catch (PatternSyntaxException e) {
            JOptionPane.showMessageDialog(null, "Invalid regex pattern: " + query, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addRow(Object[] row) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(row);
    }

}
