import java.io.File;
import java.time.LocalDateTime;
import java.util.Random;

public class Player extends Creature {

    private Weapon weapon;

    public Player(Weapon weapon, ArmedStatus armedStatus) {
        this.weapon = weapon;
    }

    public Player(String name, LocalDateTime creationDate, int hp, int ac, int str, int dex, int con, Weapon weapon, ArmedStatus armedStatus) {
        super(name, creationDate, hp, ac, str, dex, con);
        this.weapon = weapon;

        setHp(50+getCONMod());
        setAc(15+getDEXMod());
    }


    public int getDEXMod() {
        return (getDex()-5);
    }

    public int getCONMod() {
        return (getCon()-5);
    }

    public int getSTRMod() {
        return (getStr()-5);
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }


    public Player() {

    }
    public int rollInitiative() {
        Random r = new Random();
        return r.nextInt(20) + 1;
    }



    private int rollHit() {
        int roll = GameUtility.rollDice("d20");
        int DEX_mod = getDex() - 5;
        int weapon_bonus = getWeapon().getBonus();

        return roll + DEX_mod + weapon_bonus;
    }

    public String attack(Creature creature) {

        String returnString = String.format("%s attacks %s with %s (%d to hit)...", getName(), creature.getName(), getWeapon(), creature.getAc());
//        System.out.printf("%s attacks %s with %s (%d to hit)...", getName(), creature.getName(), getWeapon(), creature.getAc());

        int roll = rollHit();

        if(roll >= creature.getAc()) {

            returnString += " HITS!";
//            System.out.printf(" HITS!\n");
            int damage = getWeapon().rollDamage() + (getStr() - 5);

            if(damage < 0) {
                damage = 0;
            }

            creature.takeDamage(damage);

        } else {

            returnString += " MISSES!";
//            System.out.printf(" MISSES!\n");
        }

        return returnString;
    }

    public String toString() {
        return String.format("Player %s with weapon %s. HP: %d, AC: %d, STR: %d, CON: %d, DEX: %d", getName(), getWeapon().getName(), getHp(), getAc(), getStr(), getCon(), getDex());
    }

    public void disarm(Player target) {

        System.out.printf("%s attempts to disarm %s...", getName(), target.getName());
        int target_roll = GameUtility.rollDice("d20") + (target.getStr()-5);
        int attacker_roll = GameUtility.rollDice("d20") + (getStr()-5);

        if(attacker_roll > target_roll) {

            System.out.printf("Disarmed! %s loses %s.\n", target.getName(), target.getWeapon().getName());

            target.setArmedStatus(ArmedStatus.DISARMED);
            target.setRoundCount(2);

        } else {
            System.out.printf("%s failed to disarm %s!\n", getName(), target.getName());
        }

    }

    public static Player loadFromCsv(String data) throws CsvReadException {

        try {
            String parts[] = data.trim().split(",");

            if (parts.length != 9) {
                throw new CsvReadException(data);
            }

            Player p = new Player();

            p.setName(parts[0]);
            p.setHp(Integer.parseInt(parts[1]));
            p.setStr((Integer.parseInt(parts[2])));
            p.setDex(Integer.parseInt(parts[3]));
            p.setCon(Integer.parseInt(parts[4]));

            Weapon w = new Weapon();

            w.setName(parts[5]);
            w.setDiceType(parts[6]);
            w.setBonus(Integer.parseInt(parts[7]));

            p.setWeapon(w);

            p.setAvatar(new File(parts[8]));

            return p;


        } catch(NumberFormatException e) {
            throw new CsvReadException(data);
        }
    }
}
