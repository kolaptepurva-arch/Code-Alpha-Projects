import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Student
 {
    String name;
    int marks;
    String grade;

    Student(String name, int marks) 
    {
        this.name = name;
        this.marks = marks;
        this.grade = calculateGrade(marks);
    }

    void updateMarks(int marks) 
    {
        this.marks = marks;
        this.grade = calculateGrade(marks);
    }

    static String calculateGrade(int marks)
    {
        if (marks >= 80 && marks <= 100)
            return "A";
        else if (marks >= 65 && marks < 80)
            return "B";
        else if (marks >= 50 && marks < 65)
            return "C";
        else
            return "Fail";
    }


public String toString() 
{
    return String.format("%-15s %-10d %-5s", name, marks, grade);
}

}

public class StudentGradeTrackerGUI extends JFrame implements ActionListener 
{

    JTextField nameField, marksField;
    JButton addButton, editButton, deleteButton, reportButton;
    JTextArea reportArea;
    JList<Student> studentList;
    DefaultListModel<Student> listModel;

    ArrayList<Student> students = new ArrayList<>();

    StudentGradeTrackerGUI() 
    {

        setTitle("Student Grade Tracker");
        setSize(600, 450);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Name:"));
        nameField = new JTextField(10);
        topPanel.add(nameField);

        topPanel.add(new JLabel("Marks:"));
        marksField = new JTextField(5);
        topPanel.add(marksField);

        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        reportButton = new JButton("Report");

        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deleteButton);
        topPanel.add(reportButton);

        add(topPanel, BorderLayout.NORTH);

        // Center Panel
        listModel = new DefaultListModel<>();
        studentList = new JList<>(listModel);
        add(new JScrollPane(studentList), BorderLayout.CENTER);

        // Bottom Panel
        reportArea = new JTextArea(6, 50);
        reportArea.setEditable(false);
        add(new JScrollPane(reportArea), BorderLayout.SOUTH);

        addButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);
        reportButton.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) 
    {

        try 
        {
            if (e.getSource() == addButton) 
            {
                String name = nameField.getText();
                int marks = Integer.parseInt(marksField.getText());

                Student s = new Student(name, marks);
                students.add(s);
                listModel.addElement(s);

                nameField.setText("");
                marksField.setText("");
            }

            if (e.getSource() == editButton) 
            {
                int index = studentList.getSelectedIndex();
                if (index == -1) 
                {
                    JOptionPane.showMessageDialog(this, "Select a student to edit");
                    return;
                }

                int newMarks = Integer.parseInt(marksField.getText());
                Student s = students.get(index);
                s.updateMarks(newMarks);

                listModel.set(index, s);
            }

            if (e.getSource() == deleteButton) 
            {
                int index = studentList.getSelectedIndex();
                if (index == -1) 
                {
                    JOptionPane.showMessageDialog(this, "Select a student to delete");
                    return;
                }

                students.remove(index);
                listModel.remove(index);
            }

            if (e.getSource() == reportButton) 
            {
                if (students.isEmpty())
                {
                    reportArea.setText("No students available.");
                    return;
                }

                int total = 0;
                int highest = students.get(0).marks;
                int lowest = students.get(0).marks;

                reportArea.setText("Name\tMarks\tGrade\n");
                reportArea.append("---------------------------------------\n");

                for (Student s : students) 
                {
                    reportArea.append(s.name + "\t" + s.marks + "\t" + s.grade + "\n");
                    total += s.marks;

                    if (s.marks > highest) highest = s.marks;
                    if (s.marks < lowest) lowest = s.marks;
                }

                double avg = (double) total / students.size();
                reportArea.append("\nAverage: " + avg);
                reportArea.append("\nHighest: " + highest);
                reportArea.append("\nLowest : " + lowest);
            }

        } 
        catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    }

    public static void main(String[] args) 
    {
        new StudentGradeTrackerGUI();
    }
}
