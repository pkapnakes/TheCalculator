//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CalculatorApp extends JFrame implements ActionListener {
    private JTextField display;
    private double num1;
    private double num2;
    private double result;
    private char operator;

    public CalculatorApp() {
        this.setTitle("Κομπιουτεράκι - Ανανεωμένο UI");
        this.setSize(420, 600);
        this.getContentPane().setBackground(new Color(45, 45, 45));
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(3);

        this.display = new JTextField();
        this.display.setFont(new Font("Arial", 1, 28));
        this.display.setEditable(false);
        this.add(this.display, "North");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 10, 10));
        panel.setBackground(new Color(60, 60, 60));

        // ✅ Δεν αλλάζουμε δομή — κρατάμε ίδια κουμπιά
        String[] buttons = new String[]{
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", "C", "=", "+"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", 1, 22));
            button.setFocusPainted(false);

            if (text.equals("C")) {
                button.setBackground(new Color(200, 70, 70));
                button.setForeground(Color.WHITE);
            } else if (text.equals("=")) {
                button.setBackground(new Color(70, 130, 180));
                button.setForeground(Color.WHITE);
            } else if (text.matches("[/\\*\\-\\+]")) {
                button.setBackground(new Color(90, 90, 90));
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(new Color(100, 100, 100));
                button.setForeground(Color.WHITE);
            }

            button.addActionListener(this);
            panel.add(button);
        }

        // ✅ Προσθέτουμε τρία νέα κουμπιά ξεχωριστά (χωρίς να αλλάξει δομή πλέγματος)
        JButton sqrtButton = new JButton("√");
        JButton powButton = new JButton("^");
        JButton modButton = new JButton("%");

        JButton[] extraButtons = {sqrtButton, powButton, modButton};
        for (JButton b : extraButtons) {
            b.setFont(new Font("Arial", Font.BOLD, 22));
            b.setFocusPainted(false);
            b.setBackground(new Color(90, 90, 90));
            b.setForeground(Color.WHITE);
            b.addActionListener(this);
            panel.add(b);
        }

        this.add(panel, "Center");
        this.setLocationRelativeTo((Component) null);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // ✅ αριθμοί
        if (Character.isDigit(command.charAt(0))) {
            this.display.setText(this.display.getText() + command);
        }

        // ✅ καθαρισμός
        else if (command.equals("C")) {
            this.display.setText("");
            this.num1 = this.num2 = this.result = 0.0;
        }

        // ✅ τετραγωνική ρίζα
        else if (command.equals("√")) {
            try {
                double value = Double.parseDouble(this.display.getText());
                if (value < 0) {
                    this.display.setText("Σφάλμα: Αρνητικός");
                } else {
                    this.result = Math.sqrt(value);
                    this.display.setText(String.valueOf(this.result));
                }
            } catch (Exception ex) {
                this.display.setText("Σφάλμα");
            }
        }

        // ✅ υπολογισμός (=)
        else if (command.equals("=")) {
            try {
                String[] parts = this.display.getText().split("[\\+\\-\\*/\\^%]");
                if (parts.length < 2) return;

                this.num1 = Double.parseDouble(parts[0]);
                this.num2 = Double.parseDouble(parts[1]);

                switch (this.operator) {
                    case '*':
                        this.result = this.num1 * this.num2;
                        break;
                    case '+':
                        this.result = this.num1 + this.num2;
                        break;
                    case '-':
                        this.result = this.num1 - this.num2;
                        break;
                    case '/':
                        if (this.num2 == 0.0) {
                            this.display.setText("Σφάλμα: 0");
                            return;
                        }
                        this.result = this.num1 / this.num2;
                        break;
                    case '^':
                        this.result = Math.pow(this.num1, this.num2);
                        break;
                    case '%':
                        this.result = (this.num1 * this.num2) / 100.0;
                        break;
                }

                this.display.setText(String.valueOf(this.result));
            } catch (Exception ex) {
                this.display.setText("Σφάλμα");
            }
        }

        // ✅ τελεστές (συμπεριλαμβάνονται ^ και %)
        else {
            if (!this.display.getText().isEmpty()) {
                this.operator = command.charAt(0);
                this.display.setText(this.display.getText() + this.operator);
            }
        }
    }

    public static void main(String[] args) {
        new CalculatorApp();
    }
}
