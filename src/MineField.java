
import java.util.*;

public class MineField {

    private int numMines;
    private int dimension;
    private String level;
    private Random random = new Random();
    private boolean[][] mineField;
    //public enum Level{EASY,NORMAL,HARD}

    public int getNumMines() {
        return numMines;
    }

    public int getDimension() {
        return dimension;
    }

    public MineField() {   //EASY by default
        this("EASY");
    }

    public MineField(String level) {
        this.level = level;
        initChamp(level);
    }

    public String getLevel() {
        return level;
    }

    public void initChamp(String level) {
        this.level = level;
        if (level.equals("EASY")) {
            dimension = 3;
            numMines = 1;
        }
        if (level.equals("NORMAL")) {
            dimension = 20;
            numMines = 80;
        }
        if (level.equals("HARD")) {
            dimension = 30;
            numMines = 350;
        }

        mineField = new boolean[dimension][dimension];
        //clear all mines
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                mineField[i][j] = false;
            }
        }
        //deploy mines
        for (int i = 0; i < numMines; i++) {
            int x = random.nextInt(dimension); // x=[0, DIM-1]
            int y = random.nextInt(dimension); // y=[0, DIM-1]
            mineField[x][y] = true;
        }
    }

    public void showText() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (mineField[i][j]) {
                    System.out.print("X ");
                } else {
                    System.out.print("O ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public int calculateMinesAround(int x, int y) {
        int numMinesAround = 0;
        boolean[][] expand = new boolean[dimension + 2][dimension + 2];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                expand[i + 1][j + 1] = mineField[i][j];
            }
        }
        int xx = x + 1;
        int yy = y + 1;
        if (expand[xx - 1][yy - 1])
            numMinesAround++;
        if (expand[xx - 1][yy])
            numMinesAround++;
        if (expand[xx - 1][yy + 1])
            numMinesAround++;
        if (expand[xx][yy - 1])
            numMinesAround++;
        if (expand[xx][yy + 1])
            numMinesAround++;
        if (expand[xx + 1][yy - 1])
            numMinesAround++;
        if (expand[xx + 1][yy])
            numMinesAround++;
        if (expand[xx + 1][yy + 1])
            numMinesAround++;
//        for (int i = 0; i < dimension + 2; i++) {
//            for (int j = 0; j < dimension + 2; j++) {
//                System.out.print(expand[i][j]+" ");
//            }
//            System.out.println();
//        }
        return numMinesAround;
    }

    public void showTextWithMinesNum() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (mineField[i][j]) {
                    System.out.print("X ");
                } else {
                    System.out.print(calculateMinesAround(i, j) + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean isMine(int x, int y) {
        return mineField[x][y];
    }

}
