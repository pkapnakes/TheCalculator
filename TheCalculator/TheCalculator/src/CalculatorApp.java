import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorApp extends JFrame implements ActionListener {

    private JTextField display;
    private double num1, num2, result;
    private char operator;

    public CalculatorApp() {
        // Ρυθμίσεις παραθύρου
        setTitle("Κομπιουτεράκι - Ανανεωμένο UI");
        setSize(420, 600);
        getContentPane().setBackground(new Color(45, 45, 45)); // dark φόντο
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Πεδίο εμφάνισης
        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 28));
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        // Κουμπιά
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 10, 10));
        panel.setBackground(new Color(60, 60, 60));

        String[] buttons = {
                "7","8","9","/",
                "4","5","6","*",
                "1","2","3","-",
                "0","C","=","+"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 22));
            button.setFocusPainted(false);
            // χρώματα
            if (text.equals("C")) {
                button.setBackground(new Color(200,70,70));
                button.setForeground(Color.WHITE);
            } else if (text.equals("=")) {
                button.setBackground(new Color(70,130,180));
                button.setForeground(Color.WHITE);
            } else if (text.matches("[/\\*\\-\\+]")) {
                button.setBackground(new Color(90,90,90));
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(new Color(100,100,100));
                button.setForeground(Color.WHITE);
            }
            button.addActionListener(this);
            panel.add(button);
        }

        add(panel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (Character.isDigit(command.charAt(0))) {
            display.setText(display.getText() + command);
            return;
        }

        if (command.equals("C")) {
            display.setText("");
            num1 = num2 = result = 0;
            return;
        }

        if (command.equals("=")) {
            try {
                String[] parts = display.getText().split("[\\+\\-\\*/]");
                if (parts.length < 2) return;

                num1 = Double.parseDouble(parts[0]);
                num2 = Double.parseDouble(parts[1]);

                switch (operator) {
                    case '+': result = num1 + num2; break;
                    case '-': result = num1 - num2; break;
                    case '*': result = num1 * num2; break;
                    case '/':
                        if (num2 == 0) { display.setText("Σφάλμα: 0"); return; }
                        result = num1 / num2; break;
                }
                display.setText(String.valueOf(result));
            } catch (Exception ex) {
                display.setText("Σφάλμα");
            }
            return;
        }

        // αν είναι τελεστής
        if (!display.getText().isEmpty()) {
            operator = command.charAt(0);
            display.setText(display.getText() + operator);
        }
    }
}
