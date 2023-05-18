import java.io.File;
import java.time.LocalDateTime;
import java.util.Random;

public abstract class Creature {

    private String name;

    private LocalDateTime CreationDate;

    private File avatar;

    private int hp;

    private int ac;

    private int str;

    private int dex;

    private int con;

    private ArmedStatus armedStatus;

    private int roundCount=0;

    private int x = 0;
    private int y = 0;

    private int totalMovement = 5;

    private int currentMovement = 5;

    public void move(int x, int y, Creature[][] c) {


        this.x = x;
        this.y = y;

        c[x][y] = this;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTotalMovement() {
        return totalMovement;
    }

    public void setTotalMovement(int totalMovement) {
        this.totalMovement = totalMovement;
    }

    public int getCurrentMovement() {
        return currentMovement;
    }

    public void setCurrentMovement(int currentMovement) {
        this.currentMovement = currentMovement;
    }

    public int rollInitiative() {
        Random r = new Random();
        return r.nextInt(20) + 1;
    }

    public abstract String attack(Creature c);

    public synchronized void takeDamage(int damage) {
        setHp((getHp())-damage);

        if (getHp() < 0) {

            setHp(0);
        }

        System.out.printf("%s takes %d points of damage! %s current HP: %d\n", getName(), damage, getName(), getHp());

        if (getHp() == 0) {
            System.out.printf("%s is dead!\n", getName());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        CreationDate = creationDate;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAc() {
        return ac;
    }

    public void setAc(int ac) {
        this.ac = ac;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getDex() {
        return dex;
    }

    public void setDex(int dex) {
        this.dex = dex;
    }

    public int getCon() {
        return con;
    }

    public void setCon(int con) {
        this.con = con;
    }

    public ArmedStatus getArmedStatus() {
        return armedStatus;
    }

    public void setArmedStatus(ArmedStatus armedStatus) {
        this.armedStatus = armedStatus;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }

    public File getAvatar() {
        return avatar;
    }

    public void setAvatar(File avatar) {
        this.avatar = avatar;
    }

    public Creature() { }

    public Creature(String name, LocalDateTime creationDate, int hp, int ac, int str, int dex, int con) {
        this.name = name;
        CreationDate = creationDate;
        this.hp = hp;
        this.ac = ac;
        this.str = str;
        this.dex = dex;
        this.con = con;
        this.armedStatus = armedStatus;
    }


}
