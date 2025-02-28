import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class CodeBreaker extends JFrame {
    private String correctCode;
    private StringBuilder currentGuess;
    private JLabel display, timerLabel, wireHint;
    private JButton[] numberButtons;
    private JButton enterButton, resetButton, cutWireButton;
    private int attempts = 0;
    private Random random = new Random();
    private Timer countdownTimer;
    private int timeLeft = 60; // 60 seconds to defuse
    private String[] fakeClues = {
            "The first digit is even... or is it?",
            "One of the digits is 7, maybe.",
            "The code is definitely not 0000... probably.",
            "Trust me, it's mostly prime numbers!",
            "Cut the yellow wire. Or was it blue?"
    };
    private String[] wireColors = {"Red", "Blue", "Green", "Yellow"};
    private String correctWire;

    public CodeBreaker() {
z   
        super("Code Breaker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);


        correctCode = generateCode();
        currentGuess = new StringBuilder();


        display = new JLabel("Enter your guess: ", SwingConstants.CENTER);
        display.setOpaque(true);
        display.setBackground(Color.YELLOW); // Sticky note style
        display.setFont(new Font("Arial", Font.BOLD, 18));
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        timerLabel = new JLabel("Time left: 60 seconds", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        timerLabel.setOpaque(true);
        timerLabel.setBackground(Color.LIGHT_GRAY);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));


        wireHint = new JLabel("Hint: " + fakeClues[random.nextInt(fakeClues.length)], SwingConstants.CENTER);
        wireHint.setOpaque(true);
        wireHint.setBackground(Color.PINK);
        wireHint.setFont(new Font("Arial", Font.BOLD, 16));
        wireHint.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        numberButtons = new JButton[10];
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].setFont(new Font("Arial", Font.BOLD, 16));
            numberButtons[i].setBackground(Color.CYAN);
            numberButtons[i].addActionListener(new NumberButtonListener(i));
        }

        // Control buttons with redesigned style
        enterButton = new JButton("Enter");
        enterButton.setFont(new Font("Arial", Font.BOLD, 18));
        enterButton.setBackground(Color.GREEN);
        enterButton.addActionListener(new EnterButtonListener());

        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.BOLD, 18));
        resetButton.setBackground(Color.ORANGE);
        resetButton.addActionListener(e -> resetInput());

        cutWireButton = new JButton("Cut Wire");
        cutWireButton.setFont(new Font("Arial", Font.BOLD, 18));
        cutWireButton.setBackground(Color.RED);
        cutWireButton.setForeground(Color.WHITE);
        cutWireButton.addActionListener(new CutWireListener());


        JPanel buttonPanel = new JPanel(new GridLayout(4, 3, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        createShuffledButtons(buttonPanel);


        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.add(enterButton);
        controlPanel.add(resetButton);
        controlPanel.add(cutWireButton);


        add(display, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(timerLabel, BorderLayout.WEST);
        add(wireHint, BorderLayout.EAST);

        pack();
        setVisible(true);


        startCountdown();
    }

    private void createShuffledButtons(JPanel buttonPanel) {
        List<JButton> buttons = Arrays.asList(numberButtons);
        Collections.shuffle(buttons);

        for (JButton button : buttons) {
            buttonPanel.add(button);
        }
    }

    private void resetInput() {
        currentGuess.setLength(0);
        updateDisplay();
    }

    private void updateDisplay() {
        display.setText("Current guess: " + currentGuess.toString());
    }

    private String generateCode() {
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            code.append(random.nextInt(10));
        }

        return code.toString();
    }

    private class NumberButtonListener implements ActionListener {
        private int number;

        public NumberButtonListener(int number) {
            this.number = number;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentGuess.length() < 4) {
                currentGuess.append(number);
            }
        }
    }

    private class EnterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String guess = currentGuess.toString();
            attempts++;

            if (guess.equals(correctCode)) {
                JOptionPane.showMessageDialog(CodeBreaker.this, "Success! You defused the bomb in " + attempts + " attempts!");
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(CodeBreaker.this, "Incorrect! Try again.");
            }

            resetInput();
        }
    }

    private class CutWireListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String guessedWire = wireColors[random.nextInt(wireColors.length)];

            if (guessedWire.equals(correctWire)) {
                JOptionPane.showMessageDialog(CodeBreaker.this, "Success! You cut the correct wire and defused the bomb!");
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(CodeBreaker.this, "Boom! The bomb exploded.");
                System.exit(0);
            }
        }
    }

    private void shuffleButtons() {
        List<JButton> buttons = Arrays.asList(numberButtons);
        Collections.shuffle(buttons);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 3));
        for (JButton button : buttons) {
            buttonPanel.add(button);
        }
    }

    private void mutateCode() {

        correctCode = generateCode();
        shuffleButtons();
        resetInput();
    }

    private void startCountdown() {
        countdownTimer = new Timer();
        countdownTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timeLeft > 0) {
                    timeLeft--;
                    timerLabel.setText("Time left: " + timeLeft + " seconds");
                } else {
                    countdownTimer.cancel();
                    JOptionPane.showMessageDialog(CodeBreaker.this, "Game Over! The bomb exploded!");
                    System.exit(0);
                }
            }
        }, 0, 1000);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(CodeBreaker::new);
    }
}
