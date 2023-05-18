import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GUIController implements SpriteMoveListener {
    private MainPanel gamePanel;

    private ActionPanel buttonPanel;
    private ArrayList<Creature> players;

    private StatusPanel statusPanel;

    private Integer[] indices;

    private Creature[][] c;

    private JFrame frame;

    public void configureGUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("RPG Game");
        frame.setLayout(new BorderLayout());
        gamePanel = new MainPanel();
        gamePanel.addSpriteMoveListener(this);
        buttonPanel = new ActionPanel();
        statusPanel = new StatusPanel();

        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(statusPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);


        System.out.println("GUI Configured...");
        synchronized (this){
            notifyAll();
        }
    }
    public GUIController(ArrayList<Creature> allPlayersMonsters, Integer[] indices) {

        configureGUI();

        this.indices = indices;
        c = new Creature[15][15];

        players = new ArrayList<>();
        Random r = new Random();
        int[][] occupied = new int[15][15];
        int x, y;
        for(var creature : allPlayersMonsters) {
            x = r.nextInt(15);
            y = r.nextInt(15) ;

            while (occupied[x][y] == 1) {
                x = r.nextInt(15);
                y = r.nextInt(15);
            }

            occupied[x][y] = 1;

            creature.move(x, y, c);

            if(creature instanceof Monster) {
                creature.setCurrentMovement(0);
            }
            addPlayer(creature, creature.getAvatar().getAbsolutePath());
        }

        String order = "Order: ";

        for(int i = 0; i < indices.length; i++) {

            order = order + String.format("%d. %s | ", i+1, allPlayersMonsters.get(indices[i]).getName());
        }

        statusPanel.setStatusText(order);

        synchronized(this) {
            notifyAll();
        }


    }

    public synchronized void game() {

        GameController gc = new GameController(statusPanel);

        int index = 0;

//        for (Integer index : indices) {
            if (players.get(index) instanceof Monster) {
                statusPanel.setStatusText("PLAYING: Monster " + players.get(index).getName());
                buttonPanel.setButtonsTrue();

                if (inRange(players.get(index).getX(), players.get(index).getY())) {
                    Creature c = whichCreature(players.get(index).getX(), players.get(index).getY());
                    String s = players.get(index).attack(c);
                    statusPanel.setStatusText(s);
//
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    statusPanel.setStatusText("Nothing in range for Monster " + players.get(index).getName() + " to attack! Skipping turn...");
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }

            } else {
                Player p = (Player) players.get(index);
                p.setCurrentMovement(0);

                if (p.getArmedStatus() == ArmedStatus.DISARMED) {

                    buttonPanel.setButtonsFalse();

                    System.out.printf("\n\t --- %s ---\n", p.getName());
                    System.out.printf("%s is disarmed and cannot perform any attack/disarming...\n", p.getName());
                    p.setRoundCount(Math.max(0, p.getRoundCount() - 1));

                    if (p.getRoundCount() == 0) {

                        System.out.printf("%s gets their %s back!\n", p.getName(), p.getWeapon().getName());
                        p.setArmedStatus(ArmedStatus.ARMED);
                    }

                    p.setCurrentMovement(5);
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }


                } else {

                    System.out.println("here3");

                    if (inRange(p.getX(), p.getY())) {
                        Creature target = whichCreature(p.getX(), p.getY());
                        buttonPanel.setButtonsTrue();

                        try {
                            wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        p.setCurrentMovement(5);
                        statusPanel.setStatusText("No creature in range! " + p.getName() + " can move!");


                    }


                }

            }
        }
//    }



    public void addPlayer(Creature c, String assetPath) {
        players.add(c);
        int idx = gamePanel.addSprite(assetPath, c.getX(), c.getY(), c);
    }

    @Override
    public void spriteMoved(int id, int x, int y) {
        Creature p = players.get(id);
        p.move(x, y, c);
        statusPanel.setStatusText(p.getName() + " moved to point (" + x + "," + y + ")");
    }

    @Override
    public boolean canMove(int id) {
        Creature p = players.get(id);

        return p.getCurrentMovement() > 0;
    }

    @Override
    public boolean canMoveTo(int id, int x, int y) {
        // incoming x and y are in relative pixel coordinates, convert to grid coordinates
        Point p = gamePanel.getGridLocation(x, y);

        Creature player = players.get(id);
        int dx = Math.abs(p.x - player.getX());
        int dy = Math.abs(p.y - player.getY());
        int min = Math.min(dx, dy);
        int max = Math.max(dx, dy);
        int diagonalSteps = min;
        int straightSteps = max - min;

        int distance = (int) (Math.sqrt(2) * diagonalSteps + straightSteps);

        return player.getCurrentMovement() >= distance;
    }

    @Override
    public Creature isClicked(int index) {
        return players.get(index);
    }

    public boolean isOccupied(int x, int y) {
        if(c[x][y] != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean inRange(int x, int y) {

        if(x == 0 && y == 0) {
            if((c[x+1][y] != null) || c[x][y+1] != null) {
                return true;
            } else {
                return false;
            }
        } else if((x == 0 && y == 14)) {
            if(c[x][y-1] != null || c[x+1][y] != null) {
                return true;
            } else {
                return false;
            }
        } else if((x == 14 && y == 14)) {
            if(c[x-1][y] != null || c[x][y-1] != null) {
                return true;
            } else {
                return false;
            }
        } else if((x == 14 && y == 0)) {
            if(c[x-1][y] != null || c[x][y+1] != null) {
                return true;
            } else {
                return false;
            }
        } else if(x == 0) {
            if(c[x][y+1] != null || c[x+1][y] != null || c[x][y-1] != null) {
                return true;
            } else {
                return false;
            }
        } else if( y == 0) {
            if(c[x-1][y] != null || c[x][y+1] != null || c[x+1][y] != null) {
                return true;
            } else {
                return false;
            }
        } else if( x == 14) {
            if(c[x][y-1] != null || c[x-1][y] != null || c[x][y+1] != null) {
                return true;
            } else {
                return false;
            }
        } else if(y == 14) {
            if(c[x-1][y] != null || c[x][y-1] != null || c[x+1][y] != null) {
                return true;
            } else {
                return false;
            }
        } else {
            if(c[x-1][y] != null || c[x][y-1] != null || c[x+1][y] != null || c[x][y+1] != null) {
                return true;
            } else {
                return false;
            }
        }

    }

    public Creature whichCreature(int x, int y) {
        Creature creature = null;

        creature = c[x][y-1];
        if(creature != null) {
            return creature;
        }

        creature = c[x+1][y];
        if(creature != null) {
            return creature;
        }

        creature = c[x][y+1];
        if(creature != null) {
            return creature;
        }

        creature = c[x-1][y];
        if(c != null) {
            return creature;
        }
        return null;
    }

}
