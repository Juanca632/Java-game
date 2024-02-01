package TP2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameRender extends JPanel {
    private Dungeon dungeon;
    private Hero hero;
    private long lastTime;
    private int frameCount = 0;
    private int fpsCounter = 0;
    private Timer fpsTimer;

    public GameRender(Dungeon dungeon, DynamicThings hero) {
        this.dungeon = dungeon;
        this.hero = Hero.getInstance();
        this.lastTime = System.nanoTime();

        fpsTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fpsCounter = frameCount;
                frameCount = 0;
                repaint();
            }
        });
        fpsTimer.start();

        Timer frameCounterTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameCount++;
            }
        });
        frameCounterTimer.start();
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Things t : dungeon.getRenderList()) {
            t.draw(g);
        }
        hero.draw(g);

        drawFPS(g);
    }

    private void drawFPS(Graphics g) {
        Font originalFont = g.getFont();
        Font largerFont = new Font(originalFont.getName(), originalFont.getStyle(), 30); // Puedes ajustar el tamaño
        g.setFont(largerFont);

        g.setColor(Color.WHITE);
        g.drawString("FPS: " + fpsCounter, 10, 40);

        g.setFont(originalFont);
        g.setColor(Color.BLACK);
    }

}
