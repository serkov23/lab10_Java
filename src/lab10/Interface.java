package lab10;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Interface extends JFrame {
    private static final String STATUS_FORMAT = "x=%d, y=%d";
    private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(500, 250);
    private static final Dimension MIN_WINDOW_SIZE = new Dimension(50, 100);
    private static final Dimension DEFAULT_BUTTON_SIZE = new Dimension(70, 25);
    private static final String DEFAULT_BUTTON_TEXT = "";


    private JLabel statBar = new JLabel();
    private JButton button = new JButton();

    public Interface() {
        super("I don't know");
        this.setSize(DEFAULT_WINDOW_SIZE);
        this.setMinimumSize(MIN_WINDOW_SIZE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        JPanel workPanel = new JPanel();
        workPanel.setLayout(null);
        workPanel.add(button);
        button.setLocation(0, 0);
        button.setSize(DEFAULT_BUTTON_SIZE);
        button.setText(DEFAULT_BUTTON_TEXT);
        add(workPanel);
        add(statBar, BorderLayout.SOUTH);

        workPanel.addMouseMotionListener(new MouseMotionAdapterForMove());

        addMouseListenersForButton(button, new MouseListenerForButton());

        button.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\b')
                    button.setText(button.getText().length() == 0 ? "" : button.getText().substring(0, button.getText().length() - 1));
                else if (e.getKeyChar() != '\n')
                    button.setText(button.getText() + e.getKeyChar());
            }
        });

        workPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                button.setLocation(e.getX() - button.getWidth() / 2, e.getY() - button.getHeight() / 2);
            }
        });
    }

    private void addMouseListenersForButton(JButton button, MouseListenerForButton l) {
        button.addMouseListener(l);
        button.addMouseMotionListener(l);
    }

    private class MouseListenerForButton extends MouseMotionAdapterForMove implements MouseListener {
        private Point dxy = new Point(0, 0);

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if (e.isControlDown())
                button.setLocation((int) (e.getX() + e.getComponent().getX() - dxy.getX()), (int) (e.getY() + e.getComponent().getY() - dxy.getY()));
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            dxy.setLocation(e.getPoint());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            dxy.setLocation(0, 0);
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class MouseMotionAdapterForMove extends MouseMotionAdapter {
        @Override
        public void mouseMoved(MouseEvent e) {
            statBar.setText(String.format(STATUS_FORMAT, e.getX() + e.getComponent().getX(), e.getY() + e.getComponent().getY()));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            mouseMoved(e);
        }
    }
}
