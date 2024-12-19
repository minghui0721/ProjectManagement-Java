package ProjectManagement.Lecturer;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.PatternSyntaxException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author mingh
 */
public class Table extends JTable {

    private float opacity;
    private Timer timer;

    public Table() {
        setShowHorizontalLines(true);
        setShowVerticalLines(false);
        setGridColor(new Color(230, 230, 230));
        setRowHeight(40);

        // Initialize opacity to 0
        opacity = 0.0f;


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
                return header;
            }
        });
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean selected, boolean bln1, int i, int i1) {
                Component com = super.getTableCellRendererComponent(jtable, o, selected, bln1, i, i1);
                com.setBackground(Color.WHITE);
                setBorder(noFocusBorder);
                if (selected) {
                    com.setForeground(new Color(15, 89, 140));
                } else {
                    com.setForeground(new Color(102, 102, 102));
                }
                return com;
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
