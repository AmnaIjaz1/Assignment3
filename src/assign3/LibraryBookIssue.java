package assign3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// ==================== CUSTOM EXCEPTIONS ====================

class EmptyFieldException extends Exception {
    public EmptyFieldException(String field) {
        super("Field is empty: " + field);
    }
}

class InvalidRollNumberException extends Exception {
    public InvalidRollNumberException(String roll) {
        super("Invalid Roll Number: " + roll + " (numbers only)");
    }
}

class InvalidDateException extends Exception {
    public InvalidDateException(String msg) {
        super(msg);
    }
}

class NullSelectionException extends Exception {
    public NullSelectionException(String item) {
        super("Please select: " + item);
    }
}

class BookNotAvailableException extends Exception {
    private String bookName;
    public BookNotAvailableException(String bookName) {
        super("Book not available: " + bookName);
        this.bookName = bookName;
    }
    public String getBookName() {
        return bookName;
    }
}

// ==================== MAIN CLASS ====================

public class LibraryBookIssue extends JFrame implements ActionListener {

    JTextField tfName, tfRoll, tfBookTitle, tfIssueDate, tfReturnDate, tfRemarks;
    JComboBox<String> cbCategory;
    JRadioButton rbNew, rbOld;
    ButtonGroup bg;
    JButton btnIssue, btnReset, btnExit;

    String[] unavailableBooks = {"Java Black Book", "DBMS Navathe"};

    public LibraryBookIssue() {
        setTitle("Library Book Issue System");
        setSize(549, 564);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // ← changed so we control exit ourselves
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        // ── Exit confirmation on window X button ──
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });

        // Student Name
        JLabel label = new JLabel("Student Name:");
        label.setBounds(50, 14, 122, 42);
        getContentPane().add(label);
        tfName = new JTextField();
        tfName.setBounds(244, 19, 239, 33);
        getContentPane().add(tfName);

        // Roll Number
        JLabel label_1 = new JLabel("Roll Number:");
        label_1.setBounds(50, 63, 122, 42);
        getContentPane().add(label_1);
        tfRoll = new JTextField();
        tfRoll.setBounds(244, 63, 239, 34);
        getContentPane().add(tfRoll);

        // Book Title
        JLabel label_2 = new JLabel("Book Title:");
        label_2.setBounds(50, 106, 122, 42);
        getContentPane().add(label_2);
        tfBookTitle = new JTextField();
        tfBookTitle.setBounds(244, 110, 239, 34);
        getContentPane().add(tfBookTitle);

        // Book Category
        JLabel label_3 = new JLabel("Book Category:");
        label_3.setBounds(50, 159, 127, 42);
        getContentPane().add(label_3);
        cbCategory = new JComboBox<>(new String[]{
            "-- Select --", "Programming", "AI", "Databases", "Networking"
        });
        cbCategory.setBounds(244, 159, 239, 34);
        getContentPane().add(cbCategory);

        // Book Edition
        JLabel label_4 = new JLabel("Book Edition:");
        label_4.setBounds(50, 212, 122, 42);
        getContentPane().add(label_4);
        JPanel editionPanel = new JPanel();
        editionPanel.setBounds(244, 212, 239, 42);
        rbNew = new JRadioButton("New");
        rbOld = new JRadioButton("Old");
        bg = new ButtonGroup();
        bg.add(rbNew);
        bg.add(rbOld);
        editionPanel.add(rbNew);
        editionPanel.add(rbOld);
        getContentPane().add(editionPanel);

        // Issue Date
        JLabel label_5 = new JLabel("Issue Date (yyyy-MM-dd):");
        label_5.setBounds(30, 265, 159, 42);
        getContentPane().add(label_5);
        tfIssueDate = new JTextField(
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
        tfIssueDate.setBounds(244, 265, 239, 34);
        getContentPane().add(tfIssueDate);

        // Return Date
        JLabel label_6 = new JLabel("Return Date (yyyy-MM-dd):");
        label_6.setBounds(30, 306, 159, 42);
        getContentPane().add(label_6);
        tfReturnDate = new JTextField();
        tfReturnDate.setBounds(244, 310, 239, 34);
        getContentPane().add(tfReturnDate);

        // Remarks
        JLabel label_7 = new JLabel("Remarks (optional):");
        label_7.setBounds(50, 355, 172, 42);
        getContentPane().add(label_7);
        tfRemarks = new JTextField();
        tfRemarks.setBounds(244, 355, 239, 42);
        getContentPane().add(tfRemarks);

        // Buttons
        btnIssue = new JButton("Issue Book");
        btnIssue.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnIssue.setBackground(new Color(50, 205, 50));
        btnIssue.setBounds(27, 420, 195, 34);

        btnReset = new JButton("Reset");
        btnReset.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnReset.setBackground(new Color(255, 160, 122));
        btnReset.setBounds(298, 420, 185, 34);

        btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnExit.setBackground(new Color(255, 0, 0));
        btnExit.setBounds(148, 470, 239, 34);

        btnIssue.addActionListener(this);
        btnReset.addActionListener(this);
        btnExit.addActionListener(this);

        getContentPane().add(btnIssue);
        getContentPane().add(btnReset);
        getContentPane().add(btnExit);

        setVisible(true);
    }

    // ==================== BUTTON ACTIONS ====================

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnIssue) {
            handleIssue();
        } else if (e.getSource() == btnReset) {
            handleReset();
        } else if (e.getSource() == btnExit) {
            confirmExit(); // ← calls the confirmation method
        }
    }

    // ==================== EXIT CONFIRMATION ====================

    private void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
        // if NO — do nothing, window stays open
    }

    // ==================== ISSUE BOOK ====================

    private void handleIssue() {
        String result = "PENDING";

        try {
            String name      = tfName.getText().trim();
            String roll      = tfRoll.getText().trim();
            String bookTitle = tfBookTitle.getText().trim();
            String issueStr  = tfIssueDate.getText().trim();
            String returnStr = tfReturnDate.getText().trim();
            String category  = (String) cbCategory.getSelectedItem();

            // 1. EmptyFieldException
            if (name.isEmpty())      throw new EmptyFieldException("Student Name");
            if (roll.isEmpty())      throw new EmptyFieldException("Roll Number");
            if (bookTitle.isEmpty()) throw new EmptyFieldException("Book Title");
            if (issueStr.isEmpty())  throw new EmptyFieldException("Issue Date");
            if (returnStr.isEmpty()) throw new EmptyFieldException("Return Date");

            // 2. NullSelectionException
            if (category.equals("-- Select --"))
                throw new NullSelectionException("Book Category");
            if (!rbNew.isSelected() && !rbOld.isSelected())
                throw new NullSelectionException("Book Edition (New or Old)");

            // 3. InvalidRollNumberException
            if (!roll.matches("\\d+"))
                throw new InvalidRollNumberException(roll);

            // 4. NumberFormatException
            try {
                Integer.parseInt(roll);
            } catch (NumberFormatException nfe) {
                throw new NumberFormatException("Roll number is not a valid number: " + roll);
            }

            // 5. InvalidDateException — format
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate issueDate, returnDate;

            try {
                issueDate = LocalDate.parse(issueStr, fmt);
            } catch (DateTimeParseException ex) {
                throw new InvalidDateException("Issue Date format is wrong. Use yyyy-MM-dd");
            }

            try {
                returnDate = LocalDate.parse(returnStr, fmt);
            } catch (DateTimeParseException ex) {
                throw new InvalidDateException("Return Date format is wrong. Use yyyy-MM-dd");
            }

            // 5b. InvalidDateException — logic
            if (!returnDate.isAfter(issueDate))
                throw new InvalidDateException(
                    "Return date must be AFTER issue date.\n" +
                    "Issue: " + issueStr + ", Return: " + returnStr
                );

            // 6. BookNotAvailableException
            for (String unavailable : unavailableBooks) {
                if (bookTitle.equalsIgnoreCase(unavailable))
                    throw new BookNotAvailableException(bookTitle);
            }

            // All passed
            result = "SUCCESS";
            String edition = rbNew.isSelected() ? "New" : "Old";
            JOptionPane.showMessageDialog(this,
                "Book Issued Successfully!\n\n" +
                "Student : " + name + "\n" +
                "Roll No : " + roll + "\n" +
                "Book    : " + bookTitle + " (" + edition + " Edition)\n" +
                "Category: " + category + "\n" +
                "Period  : " + issueStr + " to " + returnStr,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (EmptyFieldException ex) {
            result = "FAILED - Empty Field";
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Empty Field", JOptionPane.ERROR_MESSAGE);

        } catch (NullSelectionException ex) {
            result = "FAILED - No Selection";
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Selection Missing", JOptionPane.ERROR_MESSAGE);

        } catch (InvalidRollNumberException ex) {
            result = "FAILED - Invalid Roll";
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Roll Number", JOptionPane.ERROR_MESSAGE);

        } catch (NumberFormatException ex) {
            result = "FAILED - Number Format";
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Number Error", JOptionPane.ERROR_MESSAGE);

        } catch (InvalidDateException ex) {
            result = "FAILED - Invalid Date";
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Date Error", JOptionPane.ERROR_MESSAGE);

        } catch (BookNotAvailableException ex) {
            result = "FAILED - Book Not Available";
            JOptionPane.showMessageDialog(this,
                ex.getMessage() + "\nPlease choose a different book.",
                "Book Not Available",
                JOptionPane.WARNING_MESSAGE
            );

        } catch (Exception ex) {
            result = "FAILED - Unknown Error";
            JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            System.out.println("Operation completed. Result: " + result);
            JOptionPane.showMessageDialog(this,
                "Operation Completed.\nStatus: " + result,
                "Done",
                JOptionPane.PLAIN_MESSAGE
            );
        }
    }

    // ==================== RESET ====================

    private void handleReset() {
        try {
            tfName.setText("");
            tfRoll.setText("");
            tfBookTitle.setText("");
            tfIssueDate.setText(
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            );
            tfReturnDate.setText("");
            cbCategory.setSelectedIndex(0);
            bg.clearSelection();
            tfRemarks.setText(""); // ← fixed: was taRemarks (which didn't exist)
            JOptionPane.showMessageDialog(this, "Form has been reset.", "Reset", JOptionPane.INFORMATION_MESSAGE);
        } finally {
            System.out.println("Form was reset.");
        }
    }

    // ==================== MAIN ====================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryBookIssue::new);
    }
}