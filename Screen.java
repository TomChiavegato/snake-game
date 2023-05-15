import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * This class handles all to do with displaying the GUI of the game and receives user input.
 */
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


    //tile size
    public static final int TILE_SIZE = 16;

    SnakeGame snakeGame;

    /**
     * Create new screen object which will display the GUI of the game
     * @param width width of game screen
     * @param height height of game screen
     * @param board reference to board object
     * @param snakeGame reference to snakeGame object
     */
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

        //Configure our JFrame

        this.addKeyListener(this); //Add our keyListener
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Handle the CLOSE button
        setTitle("Snake game - Tom Chiavegato");
        pack(); // pack all the components in the JFrame
        setVisible(true); // show it
        this.setResizable(false); //Non-resizable
        canvas.repaint(); //Show the first frame
        this.setFocusable(true);
        System.out.println(requestFocusInWindow()); // set the focus to JFrame to receive KeyEvent
    }

    /**
     * Updates the JFrame window based off of board state and time parameter
     * @param time time elapsed since game has started in seconds
     */
    void updateScreen(double time){
        score.setText("Score (snake size): "+(board.snake.size()-1));
        gameTime.setText(String.format("Time: %.1f", time));
        canvas.repaint();

    }

    /**
     * Invoked when a key is pressed, passes argument to the other keyPressed method in snakeGame
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
       //System.out.println(e.getKeyChar());
        snakeGame.keyPressed(e);
    }

    //Unused methods from keyListener interface
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    /**
     * Class for managing the game screen
     */
    class DrawCanvas extends JPanel {
        /**
         * Update the screen in window
         * @param g the <code>Graphics</code> object to protect
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //Set background to black
            setBackground(Color.BLACK);

            //Draw snake

            g.setColor(Color.WHITE);

            ArrayList<Point> snake = board.getSnake();
            for(int i = 0; i < snake.size()-1; i++){
                Point segment = snake.get(i);
                g.fillRect(segment.x*TILE_SIZE, TILE_SIZE*(height-segment.y), TILE_SIZE, TILE_SIZE);
            }

            //Draw food

            g.setColor(Color.RED);

            ArrayList<Point> food = board.getFood();
            for(int i = 0; i < food.size(); i++){
                Point foodPiece = food.get(i);
                g.fillRect(foodPiece.x*TILE_SIZE, TILE_SIZE*(height-foodPiece.y), TILE_SIZE, TILE_SIZE);
            }
        }
    }
}
