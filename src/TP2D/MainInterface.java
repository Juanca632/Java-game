package TP2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainInterface extends JFrame implements KeyListener {
    TileManager tileManager = new TileManager(48, 48, "./img/tileSet.png");
    Dungeon dungeon = new Dungeon("./gameData/level1.txt", tileManager);
    Hero hero = Hero.getInstance();
    GameRender panel = new GameRender(dungeon, hero);

    private int timerSeconds = 120; // Inicializado a 2 minutos
    private Timer timer;
    private JLabel timerLabel;

    public MainInterface() throws HeadlessException {
        super();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(panel, BorderLayout.CENTER);

        ActionListener animationTimer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
                final int speed = 10;
                if (hero.isWalking()) {
                    switch (hero.getOrientation()) {
                        case LEFT:
                            hero.moveIfPossible(-speed, 0, dungeon);
                            break;
                        case RIGHT:
                            hero.moveIfPossible(speed, 0, dungeon);
                            break;
                        case UP:
                            hero.moveIfPossible(0, -speed, dungeon);
                            break;
                        case DOWN:
                            hero.moveIfPossible(0, speed, dungeon);
                            break;
                    }

                    // Verifica si el héroe está fuera de los límites del mapa y muestra el mensaje
                    if (isHeroOutOfBounds()) {
                        showLevelCompleteMessage();
                    }
                }
            }
        };
        Timer fpsTimer = new Timer(50, animationTimer);
        fpsTimer.start();

        timerLabel = new JLabel("Time: 02:00");
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        this.getContentPane().add(timerLabel, BorderLayout.NORTH);

        ActionListener timerAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimer();
            }
        };
        timer = new Timer(1000, timerAction);
        timer.start();

        this.setVisible(true);
        this.setSize(new Dimension(dungeon.getWidth() * tileManager.getWidth(), dungeon.getHeight() * tileManager.getHeigth()));
        this.addKeyListener(this);
    }

    private void updateTimer() {
        if (timerSeconds > 0) {
            timerSeconds--;
            int minutes = timerSeconds / 60;
            int seconds = timerSeconds % 60;
            timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
        } else {
            timer.stop();
            timerLabel.setText("Time's up!");
        }
    }

    public static void main(String[] args) {
        MainInterface mainInterface = new MainInterface();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                hero.setOrientation(Orientation.LEFT);
                hero.setWalking(true);
                break;
            case KeyEvent.VK_RIGHT:
                hero.setOrientation(Orientation.RIGHT);
                hero.setWalking(true);
                break;
            case KeyEvent.VK_UP:
                hero.setOrientation(Orientation.UP);
                hero.setWalking(true);
                break;
            case KeyEvent.VK_DOWN:
                hero.setOrientation(Orientation.DOWN);
                hero.setWalking(true);
                break;
        }
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        hero.setWalking(false);
    }

    private boolean isHeroOutOfBounds() {
        int heroX = (int) hero.getX();
        int heroY = (int) hero.getY();
        return heroX < 0 || heroY < 0 || heroX >= dungeon.getWidth() * 48 || heroY >= dungeon.getHeight() * 48;
    }

    private void showLevelCompleteMessage() {
        JOptionPane.showMessageDialog(this, "Level complete", "Congrats!", JOptionPane.INFORMATION_MESSAGE);
    }


}
