
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public class Case extends JPanel implements MouseListener {

    private final static int DIMENSION = 20;
    private int x;
    private int y;
    private AppMinesweeper app;
    private boolean clicked = false;
    private boolean isMultiplayer = false;

    public String getColor() {
        return color;
    }

    private String color = "";

    public void setColor(String color) {
        this.color = color;
    }

    public Case(int x, int y, AppMinesweeper app) {
        setPreferredSize(new Dimension(DIMENSION, DIMENSION));  //size of the case
        addMouseListener(this);
        this.x = x;
        this.y = y;
        this.app = app;
        if (app.isMultiPlayerStarted()) {
            isMultiplayer = true;
        }
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }


    public void newgame() {
        clicked = false;
        repaint();
    }

    public void paintComponent(Graphics gc) {
        if (!isMultiplayer) {  //single player
            super.paintComponent(gc);  //erase previous picture
            gc.setColor(Color.LIGHT_GRAY);
            gc.fillRect(1, 1, getWidth(), getHeight());
            BufferedImage image = null;
            if (!app.isLost()) {
                if (clicked) {
                    if (app.getMineField().isMine(x, y)) {
                        try {
                            image = ImageIO.read(new File("img/bomb.png"));
                            gc.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {  //not mine
                        gc.setColor(Color.WHITE); //set field color
                        if (app.getMineField().calculateMinesAround(x, y) == 0) {
                            gc.fillRect(1, 1, getWidth(), getHeight());
                        } else {
                            gc.fillRect(1, 1, getWidth(), getHeight());
                            gc.setColor(Color.BLUE); //set color for number
                            gc.drawString(String.valueOf(app.getMineField().calculateMinesAround(x, y)), getWidth() / 2, getHeight() / 2);
                        }
                    }
                }

            }
        } else {  //multi player
            super.paintComponent(gc);  //erase previous picture
            gc.setColor(Color.LIGHT_GRAY);
            gc.fillRect(1, 1, getWidth(), getHeight());
            BufferedImage image = null;
            if (!app.isLost()) {
                if (clicked) {
                    if (app.getMineField().isMine(x, y)) {  //if is mine
                        try {
                            image = ImageIO.read(new File("img/bomb.png"));
                            gc.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {  //not mine

                        Color couleur;
                        try {
                            Field field = Class.forName("java.awt.Color").getField(color);
                            couleur = (Color) field.get(null);
                        } catch (Exception e) {
                            couleur = null; // Not defined
                        }

                        gc.setColor(couleur); //set field color
                        if (app.getMineField().calculateMinesAround(x, y) == 0) {  //0 mines around
                            gc.fillRect(1, 1, getWidth(), getHeight());
                        } else {
                            gc.fillRect(1, 1, getWidth(), getHeight()); // has mines around
                            gc.setColor(Color.BLUE); //set color for number
                            gc.drawString(String.valueOf(app.getMineField().calculateMinesAround(x, y)), getWidth() / 2, getHeight() / 2);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!isMultiplayer) {  //single player
            if (!clicked && !app.getMineField().isMine(x, y) && !app.isLost() && app.isStarted()) {
                app.increaseNumMineDiscovered();
            }

            clicked = true;

            if (!app.isLost()) {  //not lost

                if (!app.isStarted()) {
                    app.getIhmMinesweeper().getTime().startCounter();
                    app.setStarted(true);
                    app.setLost(false);
                }

                repaint();

                if (app.getMineField().isMine(x, y)) {
                    app.getIhmMinesweeper().getTime().stopCounter();
                    Icon noob = new ImageIcon("img/noob.png");
                    playMusic();
                    JOptionPane.showMessageDialog(null, "You lose, next time!", "NOOB", 1, noob);
                    app.setLost(true);
                    app.newgame();
                }
            }

            //win
            if (app.isWin()) {
                app.getIhmMinesweeper().getTime().stopCounter();
                JOptionPane.showMessageDialog(null, "You win, slick!\n Time:" + (app.getIhmMinesweeper().getTime().getProcessTime() + 1));
                app.newgame();
            }

        } else {  //multi player

            //send position to the server
            //not clicked not mine not lost multigame started
            if (!clicked && !app.getMineField().isMine(x, y) && !app.isLost() && app.isMultiPlayerStarted()) {
                try {
                    app.getOutClient().writeInt(1);  //cmd=POS
                    app.getOutClient().writeInt(x);  //POS x
                    app.getOutClient().writeInt(y);  //POS y
                    app.getOutClient().writeUTF(app.getIhmMinesweeper().getPseudoField().getText()); //name
                    app.getOutClient().writeUTF(app.getColor()); //color
                    app.getIhmMinesweeper().addMessage(app.getIhmMinesweeper().getPseudoField().getText() + " sends pos\n");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                app.increaseNumMineDiscovered();
            }

            clicked = true;

            if (!app.isLost()) {  //not lost

                if (!app.isMultiPlayerStarted()) {
                    app.getIhmMinesweeper().getTime().startCounter();
                    app.setMultiPlayerStarted(true);
                    app.setLost(false);
                }

                repaint();

                if (app.getMineField().isMine(x, y)) {   //click mine
                    app.getIhmMinesweeper().getTime().stopCounter();

                    //send end cmd
                    try {
                        app.getOutClient().writeInt(3);  //cmd=END
                        app.getOutClient().writeUTF(app.getIhmMinesweeper().getPseudoField().getText()); //name
                        app.getOutClient().writeInt(x);  //POS x
                        app.getOutClient().writeInt(y);  //POS y
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    Icon noob = new ImageIcon("img/noob.png");
                    playMusic();
                    JOptionPane.showMessageDialog(null, "You lose, next time!", "NOOB", 1, noob);
                    app.setLost(true);
                    //app.newgame();
                }

            }

            //win
            if (app.isWin()) {
                app.getIhmMinesweeper().getTime().stopCounter();
                JOptionPane.showMessageDialog(null, "You win, slick!\n Time:" + (app.getIhmMinesweeper().getTime().getProcessTime() + 1));
                //app.newgame();
            }
        }
    }


    public void playMusic() {
        try {
            FileInputStream fileaudio = new FileInputStream("img/bomb.wav");
            AudioStream as = new AudioStream(fileaudio);
            AudioPlayer.player.start(as);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
