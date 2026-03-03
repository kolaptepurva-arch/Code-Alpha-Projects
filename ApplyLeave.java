package com.codeaplha;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ApplyLeave {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Apply Leave");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6,2));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField empId = new JTextField();
        JTextField startDate = new JTextField();
        JTextField endDate = new JTextField();
        JTextField reason = new JTextField();

        JButton applyBtn = new JButton("Apply Leave");

        frame.add(new JLabel("Employee ID:"));
        frame.add(empId);
        frame.add(new JLabel("Start Date (YYYY-MM-DD):"));
        frame.add(startDate);
        frame.add(new JLabel("End Date (YYYY-MM-DD):"));
        frame.add(endDate);
        frame.add(new JLabel("Reason:"));
        frame.add(reason);
        frame.add(new JLabel(""));
        frame.add(applyBtn);

        applyBtn.addActionListener(e -> {

            if(empId.getText().isEmpty() ||
               startDate.getText().isEmpty() ||
               endDate.getText().isEmpty() ||
               reason.getText().isEmpty()) {

                JOptionPane.showMessageDialog(frame, "All fields are required!");
                return;
            }

            try {
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/CodeAlpha",
                        "root",
                        "vansh@123");

                // Check if Employee Exists
                String checkQuery = "SELECT * FROM employees WHERE emp_id = ?";
                PreparedStatement checkPs = con.prepareStatement(checkQuery);
                checkPs.setInt(1, Integer.parseInt(empId.getText()));
                ResultSet rs = checkPs.executeQuery();

                if(!rs.next()) {
                    JOptionPane.showMessageDialog(frame,
                            "Employee ID does not exist!");
                    con.close();
                    return;
                }

                // Insert Leave
                String query = "INSERT INTO leaves (emp_id, start_date, end_date, reason, status) VALUES (?, ?, ?, ?, 'Pending')";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setInt(1, Integer.parseInt(empId.getText()));
                ps.setDate(2, Date.valueOf(startDate.getText()));
                ps.setDate(3, Date.valueOf(endDate.getText()));
                ps.setString(4, reason.getText());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Leave Applied Successfully!");

                // Clear Fields
                empId.setText("");
                startDate.setText("");
                endDate.setText("");
                reason.setText("");

                con.close();

            } catch(SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Foreign Key Error! Check Employee ID.");
            } catch(IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid Date Format! Use YYYY-MM-DD.");
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
