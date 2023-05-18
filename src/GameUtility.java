import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code GameUtility} class includes methods useful for running the RPG-style game.
 */
public class GameUtility {

    /**
     * Generates a random number based on the dice type.
     * @param roll (String) - type of dice rolled and numnber of dice + a constant. (e.g. 4d3+2).
     * @return an integer of the number rolled.
     */
    public static int rollDice(String roll) {
        int NUM, CONSTANT, DICE;

        if (roll.charAt(roll.length() - 1) == '+') {

            CONSTANT = 0;
        }

        boolean valid = Pattern.matches("\\d*[d]\\d+[+]{0,1}\\d*", roll);

        if (valid) {
            //there is a constant
            if(roll.contains("+")) {

                String[] parts = roll.split("d");
                String constant = parts[1].substring(parts[1].lastIndexOf("+") + 1);
                parts[1] = parts[1].substring(0, parts[1].lastIndexOf("+"));

                if(parts[0].equals("")) { //no number at beginning, empty string
                    NUM = 1;
                } else {
                    NUM = Integer.parseInt(parts[0]);
                }
                DICE = Integer.parseInt(parts[1]);
                CONSTANT = Integer.parseInt(constant);


            } else { //no constant

                String[] parts = roll.split("d");
                if(parts[0].equals("")) {
                    NUM = 1;
                } else {
                    NUM = Integer.parseInt(parts[0]);
                }
                DICE = Integer.parseInt(parts[1]);
                CONSTANT = 0;


            }

            Random random_object = new Random();
            int random_number = random_object.nextInt(DICE) + 1;
            return (NUM * random_number) + CONSTANT;


        }else {
            System.out.println("Invalid input");
            return -1;
        }


    }

    public static boolean validateName(String name) throws ParseException {

        Pattern pattern = Pattern.compile("^[A-Z][A-Za-z]{1,23}$");
        Matcher matcher = pattern.matcher(name);

        boolean valid = matcher.find();

        if(valid == false && name.length() > 24) {
            throw new ParseException("Length greater than 24.", 0);
        } else if(valid == false && (Character.isLowerCase(name.charAt(0)) == true)) {
            throw new ParseException("First character is lowercase.", 0);
        } else if(valid == false){
            throw new ParseException("Contains special characters or numbers", 0);
        } else{
            return true;
        }

    }

    public static ArrayList readWeapons() throws IOException {

        ArrayList<Weapon> Weapons = new ArrayList<>();

        String name;
        String dicetype;
        int bonus;

        String line;

        try{
            Scanner in = new Scanner(new File("weapons.csv"));

            while(in.hasNext()) {

                line=in.next();
                String parts[] = line.split(",");

                name = parts[0];
                dicetype = parts[1];
                bonus = Integer.parseInt(parts[2]);

                Weapons.add(new Weapon(name, dicetype, bonus));

            }

        } catch(FileNotFoundException e)
        {
            System.out.println(e);
        }


        return Weapons;

    }
}

