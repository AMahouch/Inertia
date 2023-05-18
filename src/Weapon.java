public class Weapon {

    private String Name;

    private String DiceType;

    private int Bonus;


    /**
     * Creates a new {@code Weapon} object.
     * @param Name (String) The name of the weapon.
     * @param DiceType (String) The type of dice and number of dice, (e.g. "4d5").
     * @param Bonus (int) The to-hit bonus of the weapon.
     */
    public Weapon(String Name, String DiceType, int Bonus) {

        this.Name = Name;
        this.DiceType = DiceType;
        this.Bonus = Bonus;
    }

    public Weapon() {

    }

    public String getName() {
        return Name;
    }

    public String getDiceType() {
        return DiceType;
    }

    public int getBonus() {
        return Bonus;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setDiceType(String DiceType) {
        this.DiceType = DiceType;
    }

    public void setBonus(int Bonus) {
        this.Bonus = Bonus;
    }

    @Override
    public String toString() {
        return String.format("%s", Name);
    }

    /**
     * Rolls amount of damage based on the {@code DiceType} of the weapon.
     * @return a random roll using the {@code GameUtility} class method {@code rollDice}.
     */

    public int rollDamage() {

        return GameUtility.rollDice(DiceType);


    }


}

