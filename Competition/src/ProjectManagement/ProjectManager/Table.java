/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectManagement.ProjectManager;

import ProjectManagement.Grading.*;
import ProjectManagement.Submission.ViewSubmission;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author mingh
 */
public class Table extends JTable {

    public Table() {

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

                    // Open the ViewSubmission frame if the value is valid (adjust condition as needed)
                    if (value != null) {
                        // Close the current frame
                        JFrame frame = (JFrame) getRootPane().getParent();
                        // Edit 
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
                    header.setHorizontalAlignment(JLabel.LEFT);
                }
                return header;
            }
        });
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean selected, boolean bln1, int i, int i1) {
                if (i1 != -1) {
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
                    // Handle rendering of lecturer name column
                    JLabel label = (JLabel) super.getTableCellRendererComponent(jtable, o, selected, bln1, i, i1);
                    label.setForeground(new Color(102, 102, 102));
                    return label;
                }
            }
        });
    }
    

    public TableModel getCustomModel() {
        return super.getModel();
    }

    public void setCustomRowSorter(RowSorter<? extends TableModel> sorter) {
        setRowSorter(sorter);
    }

    public void filterTable(String query) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 1)); // "(?i)" makes the filter case-insensitive
    }

    public void addRow(Object[] row) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(row);
    }

}
