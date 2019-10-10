
import java.util.*;

public class MineField {

    private int numMines; //how many mines totally
    private int dimension; //how many elements each row/column
    private String level; //game level
    private Random random = new Random();

    public boolean getMineField(int x, int y) {
        return mineField[x][y];
    }
    public void setMineField(boolean isMine, int x, int y){
        mineField[x][y]=isMine;
    }

    private boolean[][] mineField; // 2 dimension array to save mine field
    //public enum Level{EASY,NORMAL,HARD}

    public int getNumMines() {
        return numMines;
    }

    public int getDimension() {
        return dimension;
    }

    //easy level by default
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

    //init minefield according to level
    public void initChamp(String level) {
        this.level = level;
        if (level.equals("EASY")) {
            dimension = 10;
            numMines = 20;
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

    //show mines field in the console
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
        return numMinesAround;
    }

    //show mines field in the console, indicating mines around each element
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

    //jugde if it is a mine
    public boolean isMine(int x, int y) {
        return mineField[x][y];
    }

}
