import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Time;
import java.util.Scanner;

public class SnakeGame{
    Board board;
    Difficulty difficulty;
    int delay;
    Direction direction;
    double time;
    Screen screen;
    int frame;

    public static final char UP = (char)KeyEvent.VK_UP;
    public static final char DOWN = (char)KeyEvent.VK_DOWN;
    public static final char LEFT = (char)KeyEvent.VK_LEFT;
    public static final char RIGHT = (char)KeyEvent.VK_RIGHT;
    public SnakeGame(){
        //initialize variables
        frame = 0;
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
            System.out.println("--"+difficulty+"--");
            if(difficulty.matches("[EMH]")){
                valid = true;
                break;
            }
            System.out.println("Invalifd, only type either E, M, or H");
        }

        //set difficulty using (valid) input
        if(difficulty.equals("E")){
            this.difficulty = Difficulty.EASY;
            this.delay = 200;
        }else if(difficulty.equals("M")){
            this.difficulty = Difficulty.MEDIUM;
            this.delay = 100;
        }else if(difficulty.equals("H")){
            this.difficulty = Difficulty.HARD;
            this.delay = 50;
        }

        //create screen
        screen = new Screen(width, height, board, this);

        //START
        double totalTimeElapsed = 0;
        boolean stillAlive = true;
        long start;
        long fin;
        screen.updateScreen(totalTimeElapsed);
        while(true){
            start = System.currentTimeMillis();
            if(!board.update(direction, frame)){
                break;
            }
            screen.updateScreen(totalTimeElapsed);
            frame ++;
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
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        if(c == UP || c == DOWN || c == LEFT || c == RIGHT ||
        c == 'w' || c == 'a' || c == 's' || c == 'd' ){
            if(c == UP || c == 'w'){
                direction = Direction.UP;
            }else if(c == DOWN || c == 's'){
                direction = Direction.DOWN;
            }else if(c == LEFT || c == 'a'){
                direction = Direction.LEFT;
            }else if(c == RIGHT || c == 'd'){
                direction = Direction.RIGHT;
            }else{
                throw new IllegalStateException("direction key cannot be anything besides w, a, s, d, up, down, left, right");
            }
        }


    }
}
