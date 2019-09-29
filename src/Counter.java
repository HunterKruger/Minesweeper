
import javax.swing.*;

public class Counter extends JLabel implements Runnable {

    private Thread thread;
    private long processTime;

    public long getProcessTime() {
        return processTime;
    }

    Counter() {
    }

    public void startCounter() {
        processTime = 0;
        thread = new Thread(this);
        thread.start();
    }

    public void stopCounter() {
        thread = null;
    }

    @Override
    public void run() {
        while (thread != null) {
            try {
                thread.sleep(1000);
                processTime++;
                this.setText(String.valueOf(processTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
