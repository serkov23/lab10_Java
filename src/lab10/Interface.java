package lab10;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Interface extends JFrame {

    private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(800, 250);
    private static final Dimension MIN_WINDOW_SIZE = new Dimension(800, 100);
    private static final String INPUT_PAT = "[+\\-]?((?:(?:\\d{1,3})(?: ?\\d{3})*)|(?:\\d+))(?:[.,]\\d+)?";


    public Interface() {
        super("economy calc");
        this.setSize(DEFAULT_WINDOW_SIZE);
        this.setMinimumSize(MIN_WINDOW_SIZE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        JLabel myInf = new JLabel("Ковалевский Сергей Александрович, 4 курс, 4 группа, 2020");
        add(myInf, BorderLayout.NORTH);
        JPanel calcPanel = new JPanel(new BorderLayout());
        JPanel inPanel = new JPanel(new GridLayout(1, 4));
        JComboBox<String> operation = new JComboBox<String>(new String[]{"+", "-", "*", "/"});
        JTextArea oper1 = new JTextArea();
        oper1.setRows(1);
        JTextArea oper2 = new JTextArea();
        JButton calculate = new JButton("=");
        JTextArea result = new JTextArea();
        result.setEditable(false);
        inPanel.add(oper1);
        inPanel.add(operation);
        inPanel.add(oper2);
        inPanel.add(calculate);
        calcPanel.add(inPanel, BorderLayout.NORTH);

        calcPanel.add(new JLabel("result: "), BorderLayout.WEST);
        calcPanel.add(result, BorderLayout.CENTER);

        calculate.addActionListener(e -> {
            String op1 = oper1.getText();
            String op2 = oper2.getText();

            if (op1.matches(INPUT_PAT) && op2.matches(INPUT_PAT)) {
                BigDecimal decOp1 = new BigDecimal(op1.replace(',', '.').replace(" ", ""));
                BigDecimal decOp2 = new BigDecimal(op2.replace(',', '.').replace(" ", ""));
                if (decOp1.compareTo(new BigDecimal("1e12")) > 0 ||
                        decOp1.compareTo(new BigDecimal("-1e12")) < 0 ||
                        decOp2.compareTo(new BigDecimal("1e12")) > 0 ||
                        decOp2.compareTo(new BigDecimal("-1e12")) < 0) {
                    result.setText("ERROR: Invalid input (operand is out of range)");
                    return;
                }
                if (operation.getSelectedIndex() == 0)
                    result.setText(process(decOp1.add(decOp2)));
                else if (operation.getSelectedIndex() == 1)
                    result.setText(process(decOp1.add(decOp2.negate())));
                else if (operation.getSelectedIndex() == 2)
                    result.setText(process(decOp1.multiply(decOp2)));
                else if (decOp2.equals(new BigDecimal("0")))
                    result.setText("ERROR: zero division is not allowed");
                else
                    result.setText(process(decOp1.divide(decOp2, new MathContext(20, RoundingMode.DOWN))));
            } else {
                result.setText("ERROR: Invalid input (invalid operand)");
            }
        });
        add(calcPanel);


    }

    private String process(BigDecimal res) {
        if (res.compareTo(new BigDecimal("-1e12")) < 0 ||
                res.compareTo(new BigDecimal("1e12")) > 0)
            return "ERROR: Invalid input (result out of bound)";
        String resStr = res.add(new BigDecimal("5e-7").multiply(new BigDecimal(res.signum()))).toPlainString();
        int ptPos = resStr.indexOf('.');
        if (ptPos != -1)
            try {
                resStr = resStr.substring(0, ptPos + 7);
            } catch (Exception ignored) {
            }
        int zeroesPosL = resStr.length();
        for (; ; zeroesPosL--)
            if (resStr.charAt(zeroesPosL - 1) != '0')
                break;
        resStr = resStr.substring(0, zeroesPosL);
        StringBuilder tmp = new StringBuilder();
        tmp.append(resStr.charAt(0));
        for (int i = 1; i < ptPos; i++) {
            if (i % 3 == ptPos % 3)
                tmp.append(' ');
            tmp.append(resStr.charAt(i));
        }
        tmp.append(resStr.substring(ptPos));


        resStr = tmp.toString();
        resStr = resStr.replace("- ", "-");
        if (resStr.charAt(resStr.length() - 1) == '.')
            resStr = resStr.substring(0, resStr.length() - 1);
        return resStr;
    }


}
