import java.time.LocalDateTime;

public class Monster extends Creature {

    private int cr;

    private MonsterType type;

    public int getCr() {
        return cr;
    }

    public void setCr(int cr) {
        this.cr = cr;
    }

    public Monster(int cr, MonsterType type) {
        this.cr = cr;
        this.type = type;
    }

    public Monster(String name, LocalDateTime creationDate, int hp, int ac, int str, int dex, int con, int cr, MonsterType type) {
        super(name, creationDate, hp, ac, str, dex, con);
        this.cr = cr;
        this.type = type;
    }

    private int rollHit() {
        int roll = GameUtility.rollDice("d20");
        int DEX_mod = getDex() - 5;

        return roll + DEX_mod;

    }

    public String attack(Creature creature) {
        String returnString = String.format("%s attacks %s (%d to hit)...", getName(), creature.getName(), creature.getAc());
//        System.out.printf("%s attacks %s (%d to hit)...", getName(), creature.getName(), creature.getAc());

        int roll = rollHit();

        if(roll >= creature.getAc()) {

            returnString += " HITS!";
//            System.out.printf(" HITS!\n");
            int damage = GameUtility.rollDice("d6") + getStr();

            if(damage < 1) {
                damage = 1;
            }
            creature.takeDamage(damage);
        } else {
            returnString += " MISSES!";

//            System.out.printf(" MISSES!\n");
        }

        return returnString;

    }

    @Override
    public String toString() {
        return String.format("Monster %s. HP: %d, CR: %d, STR: %d, CON: %d, DEX: %d", getName(), getHp(), getCr(), getStr(), getCon(), getDex());
    }
}
