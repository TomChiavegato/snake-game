import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Screen extends JFrame implements KeyListener {
    //dimentions
    int width, height;

    //main screen (canvas)
    private DrawCanvas canvas;

    //title
    private JLabel title;

    //game information
    private JPanel gameInformation;
    private JLabel score;
    private JLabel gameTime;

    //Board
    private Board board;


    //snake
    //ArrayList<Point> snake;

    //tile size
    public static final int TILE_SIZE = 16;

    SnakeGame snakeGame;

    Screen (int width, int height, Board board, SnakeGame snakeGame){
        this.snakeGame = snakeGame;
        this.width = width;
        this.height = height;
        double time =0;

        this.board = board;

        this.setLayout(new BorderLayout());

        //main screen (canvas)
        canvas = new DrawCanvas();
        canvas.setPreferredSize(new Dimension(width*TILE_SIZE, height*TILE_SIZE));
        this.add(canvas, BorderLayout.CENTER);

        //title
        title = new JLabel("Snake game - Tom Chiavegato");
        this.add(title, BorderLayout.NORTH);

        //game information
        gameInformation = new JPanel();
        gameInformation.setLayout(new BorderLayout());

        score = new JLabel("Score: "+(snakeGame.board.snake.size()-1));
        gameInformation.add(score, BorderLayout.EAST);

        gameTime = new JLabel(String.format("Time: %.1f", time));
        gameInformation.add(gameTime, BorderLayout.WEST);

        this.add(gameInformation, BorderLayout.SOUTH);

        this.addKeyListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Handle the CLOSE button
        setTitle("Snake game - Tom Chiavegato");
        pack(); // pack all the components in the JFrame
        setVisible(true); // show it
        this.setResizable(false);
        canvas.repaint();
        this.setFocusable(true);
        System.out.println(requestFocusInWindow()); // set the focus to JFrame to receive KeyEvent
    }

    void updateScreen(double time){
        score.setText("Score (snake size): "+(snakeGame.board.snake.size()-1));
        gameTime.setText(String.format("Time: %.1f", time));
        canvas.repaint();

    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
       //System.out.println(e.getKeyChar());
        snakeGame.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    class DrawCanvas extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //set Background
            setBackground(Color.BLACK);

            //draw snake
            g.setColor(Color.WHITE);

            ArrayList<Point> snake = board.getSnake();
            for(int i = 0; i < snake.size()-1; i++){
                Point segment = snake.get(i);
                g.fillRect(segment.x*TILE_SIZE, TILE_SIZE*(height-segment.y), TILE_SIZE, TILE_SIZE);
            }

            //draw food (apples)
            g.setColor(Color.RED);

            ArrayList<Point> food = board.getFood();
            for(int i = 0; i < food.size(); i++){
                Point foodPiece = food.get(i);
                g.fillRect(foodPiece.x*TILE_SIZE, TILE_SIZE*(height-foodPiece.y), TILE_SIZE, TILE_SIZE);
            }

            /*
            for(Point pt : board.getSnake()){
                g.fillRect(pt.x*TILE_SIZE, TILE_SIZE*(height-pt.y), TILE_SIZE, TILE_SIZE);
                System.out.println(pt);
                System.out.printf("x: %d y: %d w: %d h: %d \n", pt.x*TILE_SIZE, TILE_SIZE*(height-pt.y), TILE_SIZE, TILE_SIZE);
                System.out.printf("WIDTH: %d HEIGHT: %d\n", width, height);
            }
            System.out.println("\n\n");*/


            //draw apples
            //<draw>




        }
    }


}
