import java.io.IOException;
import java.util.Scanner;

public class Map {

    private int x,y;

    public Creature[][] grid = new Creature[25][25];

    public Map() {
      grid = new Creature[25][25];
    }
    public void promptMove(Creature c) throws IOException {
        int x;
        int y;

        Scanner in = new Scanner(System.in);

        System.out.println("\nSet new x value to move to:");
        x=in.nextInt();

        System.out.println("Set new y value to move to:");
        y=in.nextInt();

        move(x,y,c);


    }

    public void addCreature(int x, int y, Creature c) {
        grid[x][y] = c;
    }

    public boolean isOccupied(int x, int y) {
        if(grid[x][y] != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean inRange(int x, int y) {

        if(isOccupied(x-1, y-1) || isOccupied(x-1, y+1) || isOccupied(x+1, y-1) || isOccupied(x+1, y+1)) {
            return true;
        } else {
            return false;
        }

    }

    public Creature whichCreature(int x, int y) {
        Creature c = null;

        c = grid[x-1][y-1];
        if(c != null) {
            return c;
        }

        c = grid[x-1][y+1];
        if(c != null) {
            return c;
        }

        c = grid[x+1][y-1];
        if(c != null) {
            return c;
        }

        c = grid[x+1][y+1];
        if(c != null) {
            return c;
        }
        return null;
    }

    public void move(int x, int y, Creature c) throws IOException {

        if(x > 25 || y > 25) {
            throw new IOException("Out of map!\n");
        }

        if(isOccupied(x, y) == true) {
            throw new IOException("Another player occupies this space. Try again!\n");
        }

        grid[x][y] = c;

        System.out.printf("%s moved to point (%d,%d)\n", c.getName(),  x, y);

    }

    public void showMap() {
        char print;
        for(int i = 0; i <= 24*3; i++) {
            for(int j = 0; j<= 24*3; j++) {

                if(i % 3 == 0 || j % 3 == 0) {
                    print = '*';
                } else {
                    if(grid[i/3][j/3] != null) {
                        if(grid[i/3][j/3] instanceof Monster){
                            print = 'M';
                        } else {
                            print = 'P';
                        }
                    } else{
                        print = ' ';
                    }
                }
                System.out.printf("%2c", print);
            }

            System.out.println();
        }

    }

    public Creature[][] getGrid() {
        return grid;
    }

    public void setGrid(Creature[][] grid) {
        this.grid = grid;
    }
}
