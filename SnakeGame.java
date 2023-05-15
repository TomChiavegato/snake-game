import java.awt.event.KeyEvent;
import java.util.Scanner;

/**
 * Class represents instance of snake game. Call constructor to launch game.
 */
public class SnakeGame{
    Board board;
    Difficulty difficulty;
    int delay;
    Direction direction;
    Screen screen;

    public static final int UP = KeyEvent.VK_UP;
    public static final int DOWN = KeyEvent.VK_DOWN;
    public static final int LEFT = KeyEvent.VK_LEFT;
    public static final int RIGHT = KeyEvent.VK_RIGHT;

    /**
     * Initialize new snake game.
     */
    public SnakeGame(){
        //prompt user and take input for board dimensions
        boolean valid = false;
        Scanner scanner = new Scanner(System.in);
        int width, height;
        String widthStr = "", heightStr = "";
        direction = Direction.RIGHT;
        while(!valid){
            System.out.println("Choose board dimentions");
            System.out.print("Width: ");
            widthStr = scanner.nextLine();
            System.out.print("Height: ");
            heightStr = scanner.nextLine();
            if(widthStr.matches("^[0-9]+$") && widthStr.matches("^[0-9]+$")){
                valid = true;
                break;
            }
            System.out.println("Invalid, only type integers");
        }
        width = Integer.parseInt(widthStr);
        height = Integer.parseInt(heightStr);

        board = new Board(width, height);

        //prompt user and take input for difficulty
        valid = false;
        String difficulty = "";
        while(!valid){
            System.out.println("Choose difficulty: E => Easy, M => Medium, H => Hard");
            System.out.print("Difficulty: ");
            difficulty = scanner.nextLine();
            difficulty = difficulty.toUpperCase();
            if(difficulty.matches("[EMH]")){
                valid = true;
                break;
            }
            System.out.println("Invalifd, only type either E, M, or H");
        }

        //set difficulty using the valid input
        switch(difficulty){
            case "E":
                this.difficulty = Difficulty.EASY;
                this.delay = 200;
                break;
            case "M":
                this.difficulty = Difficulty.MEDIUM;
                this.delay = 100;
                break;
            case "H":
                this.difficulty = Difficulty.HARD;
                this.delay = 50;
                break;
        }

        //create screen
        screen = new Screen(width, height, board, this);

        //START
        double totalTimeElapsed = 0;
        boolean dead = false;
        long start;
        long fin;
        screen.updateScreen(totalTimeElapsed);
        while(true){
            start = System.currentTimeMillis();
            dead = !board.update(direction);
            if(dead){
                break;
            }
            screen.updateScreen(totalTimeElapsed);
            fin = System.currentTimeMillis();
            int timeElapsed = (int) (fin - start);
            if(delay >= timeElapsed) {
                try {
                    Thread.sleep(delay - timeElapsed);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else{
                System.out.println("LAGGING");
            }
            totalTimeElapsed += this.delay/1000.0;

        }

    }

    /**
     * Determines the direction of the head of the snake based off user input.
     * W and up arrow correspond to UP, A and left arrow correspond to LEFT, S and down arrow correspond to DOWN, and D and right arrow correspond to RIGHT
     * @param e the event to be processed
     */
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();
        if(c == UP || c == DOWN || c == LEFT || c == RIGHT ||
        c == 'W' || c == 'A' || c == 'S' || c == 'D' ){
            if(c == UP || c == 'W'){
                direction = Direction.UP;
            }else if(c == DOWN || c == 'S'){
                direction = Direction.DOWN;
            }else if(c == LEFT || c == 'A'){
                direction = Direction.LEFT;
            }else if(c == RIGHT || c == 'D'){
                direction = Direction.RIGHT;
            }else{
                throw new IllegalStateException("direction key cannot be anything besides w, a, s, d, up, down, left, right");
            }
        }


    }
}
