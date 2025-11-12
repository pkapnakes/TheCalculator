import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

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
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Πεδίο εμφάνισης
        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 28));
        display.setEditable(false);
        this.add(display, BorderLayout.NORTH);

        // === Κύριο panel με αριθμούς και βασικούς τελεστές ===
        JPanel panelMain = new JPanel();
        panelMain.setLayout(new GridLayout(4, 4, 10, 10));
        panelMain.setBackground(new Color(60, 60, 60));

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", "C", "=", "+"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 22));
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
            panelMain.add(button);
        }

        // === ΝΕΟ panel για √ ^ % ===
        JPanel panelExtra = new JPanel();
        panelExtra.setLayout(new GridLayout(1, 3, 10, 10));
        panelExtra.setBackground(new Color(60, 60, 60));

        String[] extras = {"√", "^", "%"};
        for (String text : extras) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 22));
            button.setFocusPainted(false);
            button.setBackground(new Color(90, 90, 90));
            button.setForeground(Color.WHITE);
            button.addActionListener(this);
            panelExtra.add(button);
        }

        // Ενώνουμε τα δύο panels σε ένα κεντρικό container
        JPanel allPanels = new JPanel(new BorderLayout(10, 10));
        allPanels.add(panelExtra, BorderLayout.NORTH);
        allPanels.add(panelMain, BorderLayout.CENTER);

        this.add(allPanels, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (Character.isDigit(command.charAt(0))) {
            display.setText(display.getText() + command);
        } else if (command.equals("C")) {
            display.setText("");
            num1 = num2 = result = 0;
        } else if (command.equals("√")) {
            try {
                double value = Double.parseDouble(display.getText());
                if (value < 0) {
                    display.setText("Σφάλμα: Αρνητικός");
                } else {
                    result = Math.sqrt(value);
                    display.setText(String.valueOf(result));
                }
            } catch (Exception ex) {
                display.setText("Σφάλμα");
            }
        } else if (command.equals("=")) {
            try {
                String[] parts = display.getText().split("[\\+\\-\\*/\\^%]");
                if (parts.length < 2) return;

                num1 = Double.parseDouble(parts[0]);
                num2 = Double.parseDouble(parts[1]);

                switch (operator) {
                    case '+': result = num1 + num2; break;
                    case '-': result = num1 - num2; break;
                    case '*': result = num1 * num2; break;
                    case '/':
                        if (num2 == 0) {
                            display.setText("Σφάλμα: 0");
                            return;
                        }
                        result = num1 / num2; break;
                    case '^': result = Math.pow(num1, num2); break;
                    case '%': result = (num1 * num2) / 100; break;
                }
                display.setText(String.valueOf(result));
            } catch (Exception ex) {
                display.setText("Σφάλμα");
            }
        } else {
            if (!display.getText().isEmpty()) {
                operator = command.charAt(0);
                display.setText(display.getText() + operator);
            }
        }
    }

    public static void main(String[] args) {
        new CalculatorApp();
    }
}
