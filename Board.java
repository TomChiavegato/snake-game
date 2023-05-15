import java.awt.*;
import java.util.ArrayList;

/**
 * Class represents the current state of the game (board size, snake position and direction, food positions) and determines
 * the next state of the board based on the direction of the current direction of the snake head which is determined in seperate
 * class.
 */
public class Board {
    int INITIAL_SNAKE_SIZE = 4;
    Direction INITIAL_SNAKE_DIRECTION = Direction.RIGHT;
    public final int WIDTH, HEIGHT;

    int frame;

    //SNAKE
    /**
     * ArrayList of points representing positions of each segment of the snake. There is a ghost segment of the snake which is
     * a segment of the snake that for all intents and purposes does not actually exist and which we only have in order
     * to determine where to add a new segment if necessary. Index zero is the head of the snake, index length-2 is the tail of the snake, and
     * index length-1 is the ghost segment of the snake
     */
    ArrayList<Point> snake;
    /**
     * ArrayList of directions representing velocity direction of each point
     */
    ArrayList<Direction> snakeDirection;

    //FOOD
    /**
     * ArrayList of points representing positions of each piece of food on the screen
     */
    ArrayList<Point> food;

    /**
     * Creates new board object if dimentions width, height consisting of a snake and no food initially
     * @param width width of board
     * @param height height of board
     */
    Board (int width, int height){
        //Handle illegal arguments
        if(width<20 || height<20 || width>200 || height>200){
            throw new IllegalArgumentException("Width and height must be from 20 to 200 (inclusive)");
        }
        this.WIDTH=width;
        this.HEIGHT=height;

        //initialize snake to be of size INITIAL_SNAKE_SIZE with all segments traveling INITIAL_SNAKE_DIRECTION
        snake = new ArrayList<Point>();
        snakeDirection = new ArrayList<>();
        for(int i = 0; i <= INITIAL_SNAKE_SIZE; i++){
            snake.add(new Point(WIDTH/2-i, HEIGHT/2));
            snakeDirection.add(INITIAL_SNAKE_DIRECTION);
        }
        //initialize food ArrayList
        food = new ArrayList<>();

        //initialize frame to 0
        this.frame = 0;
    }

    /**
     * Updates the state of the board based on its current state as well as the direction of the snake, and the frame
     * @param direction the direction of the head of the snake
     * @return The snake is still alive
     */
    public boolean update(Direction direction){
        frame++;
        //Handle illegal arguments
        if(direction == null){
            throw new IllegalArgumentException("direction cannot be null");
        }

        //generate food every 10th frame
        if(frame%10 == 1){
            boolean isValidGeneration = false;
            Point point = new Point(0,0);
            //keep trying to generate position for the food until we find a position which is occupied by neither other food nor the snake
            while(!isValidGeneration){
                int xCoordinate = (int)(Math.random()*WIDTH);
                int yCoordinate = (int)(Math.random()*HEIGHT);
                point=new Point(xCoordinate, yCoordinate);
                if(!snake.subList(0, snake.size()-2).contains(point) && !food.contains(point)){
                    isValidGeneration=true;
                }
            }
            food.add(point);
        }

        //record old ghost segment before updating the positions of segments in case snake eats food, so we know where to put new ghost segment
        Point ghostSegment = snake.get(snake.size()-1);
        Point oldGhostSegment = new Point(ghostSegment.x, ghostSegment.y);
        ghostSegment = null;

        //update direction of velocity for each segment of snake
        for(int i = snakeDirection.size()-1; i >0; i--){
            snakeDirection.set(i, snakeDirection.get(i-1));
        }
        snakeDirection.set(0, direction);

        //update position of each segment of snake based on velocity direction of that segment
        Point unitVector;
        for(int i = 0; i <snake.size(); i++){

            //derive the displacement vector of segment i for this frame from direction object
            if(snakeDirection.get(i).equals(Direction.UP)){unitVector = new Point(0, 1);}
            else if(snakeDirection.get(i).equals(Direction.DOWN)){unitVector = new Point(0, -1);}
            else if(snakeDirection.get(i).equals(Direction.LEFT)){unitVector = new Point(-1, 0);}
            else if(snakeDirection.get(i).equals(Direction.RIGHT)){unitVector = new Point(1, 0);}
            else{throw new IllegalArgumentException("direction must be left right up or down");}

            //add the displacement vector to the position of the corresponding segment of the snake
            Point segment = snake.get(i);
            segment.x += unitVector.x;
            segment.y += unitVector.y;
            snake.set(i, segment);
        }

        Point front = snake.get(0);
        //check for game over conditions on front snake segment (since all other segments are somewhere where the front segment has been)
        //check if snake is out of bounds
        if(front.x<0 || front.y<0 || front.x >= WIDTH || front.y >= HEIGHT){
            return false;
        }
        //check if snake has collided with itself
        if(snake.subList(1, snake.size()-1).contains(front)){
            return false;
        }

        //check if snake has eaten a piece of food
        for(int i =0; i<food.size(); i++){
            //if so increase size of snake by 1
            if(food.get(i).equals(front)){
                //remove food from screen
                food.remove(i);
                //add new segment to snake (really just adding back old ghost segment which will turn the current ghost segment into a visible segment)
                snake.add(oldGhostSegment);
                snakeDirection.add(Direction.LEFT);
            }
        }
        return true;
    }

    /**
     * Returns the Point array list representing the positions of each segment of the snake as points on the screen
     * where index zero is the head of the snake, index length-2 is the tail of the snake, and index length-1 is the
     * ghost segment of the snake
     * @return The positions of each segment of the snake
     */
    ArrayList<Point> getSnake(){
        return this.snake;
    }

    /**
     * Returns the Direction array list representing the directions of each segment of the snake where index zero is
     * the head of the snake, index length-2 is the tail of the snake, and index length-1 is the ghost segment of the snake
     * @return The velocity direction of each segment of the snake
     */
     ArrayList<Direction> getSnakeDirection(){
        return this.snakeDirection;
    }

    /**
     * Returns the Point array list representing the positions of each piece of food as points on the screen
     * @return The positions of each piece of food on the screen
     */
    ArrayList<Point> getFood(){
        return this.food;
    }
}
