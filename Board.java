import java.awt.*;
import java.util.ArrayList;

public class Board {
    int INITIAL_SNAKE_SIZE = 4;
    Direction INITIAL_SNAKE_DIRECTION = Direction.RIGHT;
    public final int WIDTH, HEIGHT;

    //SNAKE
    //ArrayList of points representing positions of each segment of the snake (last point in arraylist index size()-1 is a "ghost" point that only exists for tracking where to
    //                                                                        insert new segment after food is eaten and hence not tested for collision detection nor displayed)
    ArrayList<Point> snake;
    //ArrayList of directions representing velocity direction of each point
    ArrayList<Direction> snakeDirection;
    //FOOD
    //ArrayList of points representing positions of each piece of food on the screen
    ArrayList<Point> food;

    Board (int width, int height){
        this.WIDTH=width;
        this.HEIGHT=height;
        //screen = new char[width][height];
        /*
        for(int x = 0; x < WIDTH; x++){
            for(int y = 0; y < HEIGHT; y++){
                screen[x][y] = ' ';
            }
        }*/

        //initialize snake to be of size INITIAL_SNAKE_SIZE all traveling INITIAL_SNAKE_DIRECTION
        snake = new ArrayList<Point>();
        snakeDirection = new ArrayList<>();
        for(int i = 0; i <= INITIAL_SNAKE_SIZE; i++){
            snake.add(new Point(WIDTH/2-i, HEIGHT/2));
            snakeDirection.add(INITIAL_SNAKE_DIRECTION);
        }
        //initialize food ArrayList
        food = new ArrayList<>();

    }

    //update method updates the position of the snake and the board and returns false if game over, true otherwise
    public boolean update(Direction direction, int frame){
        if(direction == null){
            throw new IllegalArgumentException("direction cannot be null");
        }


        /*
        //clear screen
        for(int x = 0; x < WIDTH; x++){
            for(int y = 0; y < HEIGHT; y++){
                screen[x][y] = ' ';
            }
        }
        for(Point point : snake){
            point.x += unitVector.x;
            point.y += unitVector.y;
            //check if game is over
            if(point.x<0 || point.y<0 || point.x > WIDTH || point.y > HEIGHT){
                return false;
            }
            //update screen array
            //screen[point.x][point.y] = '*';
        }
        */

        //generate food every 10th frame
        if(frame%10 == 1){
            boolean validGeneration = false;
            Point point = new Point(0,0);
            //keep trying to generate position for the food until we find a position which is occupied by neither other foor nor the snake
            while(!validGeneration){
                int xCoordinate = (int)(Math.random()*WIDTH);
                int yCoordinate = (int)(Math.random()*HEIGHT);
                point=new Point(xCoordinate, yCoordinate);
                if(!snake.subList(0, snake.size()-2).contains(point) && !food.contains(point)){
                    validGeneration=true;
                }
            }
            food.add(point);
        }
        //record old ghost segment before updating the positions of segments in case snake eats food
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

            //derive a displacement vector from direction object
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


        //check for game over conditions on front snake segment (since all other segments are somewhere where the front segment has been)
        Point front = snake.get(0);
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
                //add new segment to snake (really just adding back old ghost segmentwhich will turn the current ghost segment into a visible segment)
                snake.add(oldGhostSegment);
                snakeDirection.add(Direction.LEFT);
            }
        }



        /*
        for(Point point : snake){
            point.x += unitVector.x;
            point.y += unitVector.y;
            //check if game is over
            if(point.x<0 || point.y<0 || point.x >= WIDTH || point.y >= HEIGHT){
                return false;
            }
        }*/

        return true;
    }

    /*
    public void printScreen(){
        System.out.println(" #".repeat(WIDTH+2));




        for(char[] column : screen){
            System.out.print(" #");
            for(char pixel : column){
                System.out.print(' ');
                System.out.print(pixel);
            }
            System.out.println(" #");
        }
        System.out.println(" #".repeat(WIDTH+2));
        System.out.println(snake);
    }*/

/*
    char[][] getScreen(){
        return this.screen;
    }
    */


    ArrayList<Point> getSnake(){
        return this.snake;
    }

    ArrayList<Direction> getSnakeDirection(){
        return this.snakeDirection;
    }

    ArrayList<Point> getFood(){
        return this.food;
    }




}
