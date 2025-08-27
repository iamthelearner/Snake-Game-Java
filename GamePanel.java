import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.JPanel;


public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 700;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 200;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyparts = 6;
    int FoodEaten;
    int foodX;
    int foodY;
    char direction = 'R';
    boolean running = false;
    boolean startScreen = true;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        newFood();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        //g.setColor(Color.GRAY);
        if(startScreen){
            showStartScreen(g);
        }
        else if(running) {
            /*
            // Vertical lines
            for (int i = 0; i < (SCREEN_WIDTH / UNIT_SIZE); i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            }
            // Horizontal lines
            for (int i = 0; i < (SCREEN_HEIGHT / UNIT_SIZE); i++) {
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            */

            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyparts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0)); //rgb color value
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255))); // multicolor snake
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.RED);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score "+FoodEaten, (SCREEN_WIDTH - metrics.stringWidth("Score "+FoodEaten)) / 2, g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
    }
    public void newFood(){
        foodX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        foodY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }
    public void move(){
        for(int i = bodyparts; i > 0; i--){
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkFood(){
        if(x[0] == foodX && y[0] == foodY){
            bodyparts++;
            FoodEaten++;
            newFood();

            if(DELAY > 50){
                timer.setDelay(DELAY - 5);
            }
        }
    }
    public void checkCollisions(){
        //check if head collides with body
        for(int i = bodyparts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        //check if head touches left border
        if(x[0] < 0){
            running = false;
        }
        //check if head touches right border
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }
        //check if head touches top border
        if(y[0] < 0){
            running = false;
        }
        //check if head touches bottom border
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }

        if(running == false){
            timer.stop();
        }
    }
    public void showStartScreen(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Snake Game", (SCREEN_WIDTH - metrics.stringWidth("Snake Game")) / 2, SCREEN_HEIGHT / 3);

        g.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Press SPACE to Start", (SCREEN_WIDTH - metrics2.stringWidth("Press SPACE to Start")) / 2, SCREEN_HEIGHT / 2);
    }

    public void gameOver(Graphics g){
        //score
        g.setColor(Color.RED);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score "+FoodEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score "+FoodEaten)) / 2, g.getFont().getSize());

        //game over text
        g.setColor(Color.RED);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (startScreen) {
                        startScreen = false;
                        startGame();
                    }
                    break;
            }
        }
    }
}
