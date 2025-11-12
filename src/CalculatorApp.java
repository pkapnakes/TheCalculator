import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class CalculatorApp extends JFrame implements ActionListener {

    private JTextField expressionDisplay;
    private JTextField resultDisplay;
    private String expression = "";

    public CalculatorApp() {
        setTitle("Κομπιουτεράκι - Modern Dark UI");
        setSize(420, 650);
        getContentPane().setBackground(new Color(25, 25, 25));
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(25, 25, 25));
        add(mainPanel, BorderLayout.CENTER);

        JPanel displayPanel = new JPanel(new GridLayout(2, 1));
        displayPanel.setBackground(new Color(25, 25, 25));

        expressionDisplay = new JTextField();
        expressionDisplay.setFont(new Font("Consolas", Font.PLAIN, 22));
        expressionDisplay.setForeground(new Color(200, 200, 200));
        expressionDisplay.setBackground(new Color(40, 40, 40));
        expressionDisplay.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        expressionDisplay.setEditable(false);

        resultDisplay = new JTextField();
        resultDisplay.setFont(new Font("Consolas", Font.BOLD, 30));
        resultDisplay.setForeground(Color.WHITE);
        resultDisplay.setBackground(new Color(40, 40, 40));
        resultDisplay.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultDisplay.setEditable(false);

        displayPanel.add(expressionDisplay);
        displayPanel.add(resultDisplay);
        mainPanel.add(displayPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 4, 10, 10)); // 6 σειρές για τα νέα κουμπιά
        buttonPanel.setBackground(new Color(25, 25, 25));

        String[] buttons = {
                "(", ")", "C", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "0", ".", "=", "%",
                "√", "^"
        };

        for (String text : buttons) {
            JButton btn = createButton(text);
            buttonPanel.add(btn);
        }

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 22));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        // 🎨 Χρώματα ανά είδος κουμπιού
        if (text.matches("[0-9\\.]")) {
            button.setBackground(new Color(70, 70, 70));
            button.setForeground(Color.WHITE);
        } else if (text.equals("=")) {
            button.setBackground(new Color(0, 140, 80));
            button.setForeground(Color.WHITE);
        } else if (text.equals("C")) {
            button.setBackground(new Color(180, 40, 40));
            button.setForeground(Color.WHITE);
        } else if (text.equals("(") || text.equals(")")) {
            button.setBackground(new Color(180, 140, 240));
            button.setForeground(Color.BLACK);
        } else {
            button.setBackground(new Color(85, 70, 150));
            button.setForeground(Color.WHITE);
        }

        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.matches("[0-9]") || command.equals(".") || command.matches("[+\\-*/%^()]")) {
            expression += command;
            expressionDisplay.setText(expression);
        }
        else if (command.equals("√")) {
            expression += "√";
            expressionDisplay.setText(expression);
        }
        else if (command.equals("C")) {
            expression = "";
            expressionDisplay.setText("");
            resultDisplay.setText("");
        }
        else if (command.equals("=")) {
            calculateResult();
        }
    }

    private void calculateResult() {
        try {
            double result = evaluate(expression);
            if (Double.isInfinite(result) || Double.isNaN(result)) {
                throw new ArithmeticException();
            }
            resultDisplay.setForeground(new Color(0, 255, 120));
            resultDisplay.setText("= " + result);
        } catch (Exception ex) {
            resultDisplay.setForeground(Color.RED);
            resultDisplay.setText("Error");
        }
    }

    private double evaluate(String expr) {
        return evaluateExpression(expr.replaceAll("\\s+", ""));
    }

    private double evaluateExpression(String expr) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> ops = new Stack<>();
        int i = 0;
        while (i < expr.length()) {
            char ch = expr.charAt(i);
            if (Character.isDigit(ch) || ch == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    sb.append(expr.charAt(i++));
                }
                numbers.push(Double.parseDouble(sb.toString()));
                continue;
            } else if (ch == '(') {
                ops.push(ch);
            } else if (ch == ')') {
                while (ops.peek() != '(') {
                    numbers.push(applyOp(ops.pop(), numbers.pop(), numbers.pop()));
                }
                ops.pop();
            } else if ("+-*/%^".indexOf(ch) != -1) {
                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(ch)) {
                    numbers.push(applyOp(ops.pop(), numbers.pop(), numbers.pop()));
                }
                ops.push(ch);
            } else if (ch == '√') {
                // Υπολογισμός τετραγωνικής ρίζας του επόμενου αριθμού
                i++;
                StringBuilder sb = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    sb.append(expr.charAt(i++));
                }
                double value = Math.sqrt(Double.parseDouble(sb.toString()));
                numbers.push(value);
                continue;
            }
            i++;
        }
        while (!ops.isEmpty()) {
            numbers.push(applyOp(ops.pop(), numbers.pop(), numbers.pop()));
        }
        return numbers.pop();
    }

    private int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/' || op == '%') return 2;
        if (op == '^') return 3;
        return 0;
    }

    private double applyOp(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("Division by zero");
                return a / b;
            case '%': return a % b;
            case '^': return Math.pow(a, b);
            default: throw new IllegalArgumentException("Άγνωστη πράξη");
        }
    }

    public static void main(String[] args) {
        new CalculatorApp();
    }
}
