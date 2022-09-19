import javax.swing.JPanel;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int LARGHEZZA_SCHERMO = 600;
    static final int ALTEZZA_SCHERMO = 600;
    static final int LARGHEZZA_CELLE = 25;
    static final int GAME_UNITS = (LARGHEZZA_SCHERMO * ALTEZZA_SCHERMO) / LARGHEZZA_CELLE;
    static final int VELOCITA = 75;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(LARGHEZZA_SCHERMO, ALTEZZA_SCHERMO));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(VELOCITA, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {
            for (int i = 0; i < ALTEZZA_SCHERMO / LARGHEZZA_CELLE; i++) {
                g.drawLine(i * LARGHEZZA_CELLE, 0, i * LARGHEZZA_CELLE, ALTEZZA_SCHERMO);
                g.drawLine(0, i * LARGHEZZA_CELLE, LARGHEZZA_SCHERMO, i * LARGHEZZA_CELLE);
            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, LARGHEZZA_CELLE, LARGHEZZA_CELLE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], LARGHEZZA_CELLE, LARGHEZZA_CELLE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], LARGHEZZA_CELLE, LARGHEZZA_CELLE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (LARGHEZZA_SCHERMO - metrics.stringWidth("Score: " + applesEaten)) / 2,
                    g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (LARGHEZZA_SCHERMO / LARGHEZZA_CELLE)) * LARGHEZZA_CELLE;
        appleY = random.nextInt((int) (LARGHEZZA_SCHERMO / LARGHEZZA_CELLE)) * LARGHEZZA_CELLE;

    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - LARGHEZZA_CELLE;
                break;
            case 'D':
                y[0] = y[0] + LARGHEZZA_CELLE;
                break;
            case 'L':
                x[0] = x[0] - LARGHEZZA_CELLE;
                break;
            case 'R':
                x[0] = x[0] + LARGHEZZA_CELLE;
                break;
            default:
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollision() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        if (x[0] < 0) {
            running = false;
        }

        if (x[0] > LARGHEZZA_SCHERMO) {
            running = false;
        }

        if (y[0] < 0) {
            running = false;
        }

        if (y[0] > ALTEZZA_SCHERMO) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // Score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (LARGHEZZA_SCHERMO - metrics1.stringWidth("Score: " + applesEaten)) / 2,
                g.getFont().getSize());
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (LARGHEZZA_SCHERMO - metrics2.stringWidth("Game Over")) / 2, ALTEZZA_SCHERMO / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
