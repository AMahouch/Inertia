import java.io.*;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class GameController {
    public ArrayList<Weapon> Weapons;
    public ArrayList<Creature> CreatureList = new ArrayList<>();
    public Map m = new Map();

    public GameController(StatusPanel statusPanel) {}

    public ArrayList readWeapons() throws IOException {

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

    public void showWeapons(ArrayList<Weapon> Weapons) {

        int i = 0;

        if(Weapons == null)
        {

        }

        for(Weapon stuff : Weapons) {

            System.out.printf("%d. %s, Damage: %s, Bonus To-Hit: %d\n",i+1, stuff.getName(), stuff.getDiceType(), stuff.getBonus());

            i++;

        }
    }

    public void showMainMenu() throws IOException {

        int choice = 0;
        Scanner in = new Scanner(System.in);

        while(true) {

            System.out.printf("\n1. Start Game\n2. Create Characters\n3. Load Character\n4. Save Character\n5. Quit\n> ");

            choice = in.nextInt();

            switch(choice) {

                case 1:
                    startGame();
                    break;

                case 2:
                    createCharacters();
                    break;

                case 3:
                    //loadCharacter();
                    break;

                case 4:
                    saveCharacter();
                    break;

                case 5:
                    System.exit(0);

                default:

            }

        }

    }

    public void startGame() throws IOException {

        int round = 0;

        if(CreatureList.isEmpty() == true) {

            System.out.println("Create players first!");
        } else {
            Scanner in = new Scanner(System.in);
            int choice;

            System.out.printf("\n1. Play with Random Monsters\n2. Play with Players Only (PvP)\n3. Back\n> ");
            choice = in.nextInt();

            switch(choice) {
                case 1:
                     playWithMonsters();
                    break;
                case 2:
                     playWithPlayers();
                    break;
                case 3:
                    break;


            }



            }

        }

        public void saveCharacter() {

        if(CreatureList == null) {
            System.out.println("No players to save!");
        } else {

            try{
                Scanner in = new Scanner(System.in);

                System.out.println("\nChoose player to save:");
                for(int i = 0; i < CreatureList.size(); i++) {
                    System.out.printf("%d. %s\n", i+1, CreatureList.get(i).getName());
                }

                int choice = in.nextInt() - 1;

                Player PlayertoSave = (Player) CreatureList.get(choice);

                String filename = "/Users/ameenmahouch/IdeaProjects/Phase II/saved/players/" + PlayertoSave.getName() + ".csv";


                File file = new File(filename);
                FileWriter fileWriter = new FileWriter(file);

                String csvdata = String.format("%s,%d,%d,%d,%d,%s,%s,%d", PlayertoSave.getName(), PlayertoSave.getHp(), PlayertoSave.getStr(), PlayertoSave.getDex(), PlayertoSave.getCon(), PlayertoSave.getWeapon().getName(), PlayertoSave.getWeapon().getDiceType(), PlayertoSave.getWeapon().getBonus());
                fileWriter.write(csvdata);

                fileWriter.close();

                System.out.printf("%s saved!\n", PlayertoSave.getName());

            } catch(IOException e) {
                e.printStackTrace();
            }

            }

        }

//        public void loadCharacter() {
//
//            Scanner in = new Scanner(System.in);
////            File directory = new File("/Users/ameenmahouch/IdeaProjects/Phase II/saved/players/");
//
//            File[] all_files = directory.listFiles();
//
//            System.out.println("Choose file to load character from:");
//
//            for(int i = 0; i < all_files.length; i++) {
//                System.out.printf("%d. %s\n", i+1, all_files[i].getName());
//            }
//
//            int choice = in.nextInt() - 1;
//
//            try {
//
//                Scanner file_in = new Scanner(all_files[choice]);
//
//                for(var creature : CreatureList) {
//                    System.out.println(creature);
//                }
//
//                System.out.println(all_files[choice].getName());
//                Player loading = Player.loadFromCsv(file_in.nextLine());
//
//                if(CreatureList.contains(loading) && CreatureList == null) {
//                    System.out.printf("%s is already loaded!\n", loading.getName());
//                } else {
//                    CreatureList.add(loading);
//                    System.out.printf("Added %s\n", loading.getName());
//                }
//
//
//            } catch (IOException | CsvReadException | NullPointerException e) {
//                e.printStackTrace();
//            }
//
//        }


        public void doActions(Creature c){

        Player p = new Player();
        p = (Player) c;

            Scanner in = new Scanner(System.in);
            int choice;

            if(c.getArmedStatus() == ArmedStatus.DISARMED) {

                System.out.printf("\n\t --- %s ---\n", c.getName());
                System.out.printf("%s is disarmed and cannot perform any attack/disarming...\n",c.getName());
                c.setRoundCount(Math.max(0, c.getRoundCount() - 1));

                if(c.getRoundCount() == 0) {

                    System.out.printf("%s gets their %s back!\n",c.getName(), p.getWeapon().getName());
                    c.setArmedStatus(ArmedStatus.ARMED);
                }

                try{
                    m.promptMove(c);
                    m.showMap();
                } catch(IOException e) {
                    System.out.println(e.getMessage());
                }
            } else {

                System.out.printf("\n\t---- %s ----\n1. Attack?\n2. Disarm?\n3. Exit\n> ", c.getName());
                choice = in.nextInt();

                switch(choice) {

                    case 1:
                        int i = 0;
                        int attack_choice;
                        System.out.println("Who to attack?");
                        for(var creature : CreatureList) {
                            System.out.printf("%d. %s\n", i+1, creature.getName());
                            i++;
                        }
                        System.out.printf("> ");
                        attack_choice = in.nextInt() - 1;

                        if(CreatureList.get(attack_choice).equals(c)) {
                            System.out.println("Cannot attack yourself!");
                        } else {
                            c.attack(CreatureList.get(attack_choice));
                        }

                        try{
                            m.promptMove(c);
                            m.showMap();
                        } catch(IOException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 2:
                        int j = 0;
                        int disarm_choice;
                        System.out.println("\nWhat player to disarm?");
                        for(var creature : CreatureList) {

                            if(creature instanceof Player) {
                                System.out.printf("%d. %s\n", j+1, creature.getName());
                            }
                            j++;
                        }
                        System.out.printf("> ");
                        disarm_choice = in.nextInt() - 1;

                        if(CreatureList.get(disarm_choice).equals(c)) {
                            System.out.println("Cannot disarm yourself!");
                        } else {

                            if(CreatureList.get(disarm_choice).getArmedStatus() == ArmedStatus.DISARMED) {
                                System.out.println(CreatureList.get(disarm_choice).getName() + " is already disarmed!");
                            } else {
                                p.disarm((Player) CreatureList.get(disarm_choice));
                            }
                        }

                        try{
                            m.promptMove(c);
                            m.showMap();
                        } catch(IOException e) {
                            System.out.println(e.getMessage());
                        }

                        break;

                    case 3:
                        System.exit(0);

                    default:
                        System.out.println("Unrecognized. Try again.");

                }
            }

        }

        public void playWithMonsters() {

              int round = 0;

            Scanner in = new Scanner(System.in);
            System.out.printf("Choose the number of monsters you would like to play against\n> ");
            int monsters = in.nextInt();

            monsters = Math.min(monsters, 4); //max of 4 monsters

            try{
                monsterFromCsv(monsters, CreatureList);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            InitiativeUtility initUtil = new InitiativeUtility(CreatureList.toArray(new Creature[0]));
            Integer[] indices = initUtil.rollInitiative();

            while(true) {
                for(Integer index : indices) {
                    if(CreatureList.get(index) instanceof Monster) {
                        System.out.printf("\n\t --- Monster %s ---\n", CreatureList.get(index).getName());
                        if(m.inRange(CreatureList.get(index).getX(), CreatureList.get(index).getY())) {

                            Creature c = m.whichCreature(CreatureList.get(index).getX(), CreatureList.get(index).getY());
                            System.out.printf("%s is in range of Monster %s and will be attacked!\n", c.getName(), CreatureList.get(index).getName());
                            CreatureList.get(index).attack(c);
                        } else{
                            System.out.printf("Nothing in range for Monster %s to attack!\n", CreatureList.get(index).getName());
                        }
                    } else { //assume it is Player
                        doActions(CreatureList.get(index));
                    }
                }

                round++;

            }



        }

    public void monsterFromCsv(int monsters, ArrayList<Creature> CreatureList) throws IOException {

        Scanner in = new Scanner(Path.of("monsters.csv"));

        for(int i = 0; i < monsters; i ++) {

            String line = in.nextLine();
            String attributes[] = line.trim().split(",");

            String name = attributes[0];
            MonsterType type = MonsterType.valueOf(attributes[1]);
            int hp = Integer.parseInt(attributes[2]);
            int ac = Integer.parseInt(attributes[3]);
            int str = Integer.parseInt(attributes[4]);
            int dex = Integer.parseInt(attributes[5]);
            int con = Integer.parseInt(attributes[6]);


           Monster monster = new Monster(name, LocalDateTime.now(), hp, ac, str, dex, con, 5, type);

           Random r = new Random();

           monster.setX(r.nextInt(24)+1);

           monster.setY(r.nextInt(24)+1);

           m.grid[monster.getX()][monster.getY()] = monster;

            CreatureList.add(monster);
        }

    }

    public void playWithPlayers() {

        int round = 0;
        InitiativeUtility initUtil = new InitiativeUtility(CreatureList.toArray(new Creature[0]));
        Integer[] indices = initUtil.rollInitiative();

        while(true) {
            for(Integer index : indices) {
                doActions(CreatureList.get(index));
            }

            round++;

        }




    }


    public void createCharacters() {
        int choice = 0;
        String name;


        Scanner in = new Scanner(System.in);


            while(true) {
                System.out.printf("\nEnter Character %d name: ", CreatureList.size()+1);
                name = in.next();

                try{
                    GameUtility.validateName(name);
                    break;
                } catch(ParseException e) {
                    System.out.println(e.getMessage());
                }
            }


            System.out.printf("1. Manual Stats\n2. Random Stats\nManual or Random Stats? ");
            choice = in.nextInt();

            switch (choice) {

                case 1:
                    manualStats(name);
                    break;
                case 2:
                    randomStats(name);
                    break;
                default:
                    System.out.println("Unrecognized, try 1 or 2");
            }

        }


    public void randomStats(String name) {

        try {
            Weapons=readWeapons();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Player p = new Player();
        Scanner in = new Scanner(System.in);

        p.setName(name);

        int str = 0;
        int dex = 0;
        int con = 0;
        int counter = 10;

        Random r = new Random();
        int random_index;

        while(counter != 0) {
            System.out.printf("STR: %d\nDEX: %d\nCON: %d\nRemaining: %d\n\n1. Add STR\n2. Add DEX\n3. Add CON\n4. Reset\n5. Finish\n", str,dex,con,counter);

            random_index=r.nextInt(3);

            switch(random_index) {

                case 0:
                    str++;
                    break;
                case 1:
                    dex++;
                    break;
                case 2:
                    con++;
                    break;

                default:
                    System.out.println("try again");

            }

            counter--;

        }
        System.out.printf("\nSTR: %d\nDEX: %d\nCON: %d\n", str,dex,con);

        p.setCon(con);
        p.setDex(dex);
        p.setStr(str);

        p.setHp(50+(p.getCon()-5));
        p.setAc(15+(p.getDex()-5));

        int weapon_choice;
        System.out.println("Choose weapon: ");
        showWeapons(Weapons);
        System.out.printf("> ");
        weapon_choice = in.nextInt();

        p.setWeapon(Weapons.get(weapon_choice-1));
        p.setArmedStatus(ArmedStatus.ARMED);

        System.out.printf("---- Player %s ----\nWeapon: %s\nHP: %d\nAC: %d\n", p.getName(), p.getWeapon().getName(), p.getHp(), p.getAc());

        System.out.printf("1. Confirm\n2. Start Over\n> ");
        int choice = in.nextInt();

        switch(choice) {

            case 1:
                CreatureList.add(p);
                break;
            case 2:
                randomStats(name);
                break;
        }

    }

    public void manualStats(String name) {

        Player p = new Player();

        p.setName(name);

        int choice = 0;
        Scanner in = new Scanner(System.in);

        int str = 0;
        int dex = 0;
        int con = 0;
        int counter = 10;

        while(counter != 0) {


            System.out.printf("STR: %d\nDEX: %d\nCON: %d\nRemaining: %d\n\n1. Add STR\n2. Add DEX\n3. Add CON\n4. Reset\n5. Finish\n> ", str,dex,con,counter);

            choice = in.nextInt();

            switch(choice) {

                case 1:
                    str++;
                    counter--;
                    break;
                case 2:
                    dex++;
                    counter--;
                    break;
                case 3:
                    con++;
                    counter--;
                    break;
                case 4:
                    str = 0;
                    dex = 0;
                    con = 0;
                    counter = 10;
                    break;
                case 5:
                    counter = 0;
                    break;
            }

        }

        p.setCon(con);
        p.setDex(dex);
        p.setStr(str);

        p.setHp(50+(p.getCon()-5));
        p.setAc(15+(p.getDex()-5));


        try {
            Weapons= readWeapons();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int weapon_choice;
        System.out.println("Choose weapon: ");
        showWeapons(Weapons);
        System.out.printf("> ");
        weapon_choice = in.nextInt();

        p.setWeapon(Weapons.get(weapon_choice-1));
        p.setArmedStatus(ArmedStatus.ARMED);

        System.out.printf("---- Player %s ----\n, Weapon: %s\nHP: %d\nAC: %d\n", p.getName(), p.getWeapon().getName(), p.getHp(), p.getAc());

        System.out.printf("1. Confirm\n2. Start Over\n> ");
        choice = in.nextInt();

        switch(choice) {

            case 1:
                CreatureList.add(p);
                break;
            case 2:
                manualStats(name);
                break;
        }

    }

}
