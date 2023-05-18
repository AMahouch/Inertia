import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class MainMenuController implements ActionListener, MouseListener {

    public ArrayList<Creature> playerList = new ArrayList<>();

    public ArrayList<Creature> allCreatureList = new ArrayList<>();

    private JFrame mainMenu;

    private JPanel mainPanel;

    private JButton startButton;

    private JButton createButton;

    private JButton loadButton;
    private JButton saveButton;

    private JButton exitButton;

    private JPanel playerPanel;

    private JPopupMenu playerPanelClickMenu;

    private JMenuItem removeCharacter;

    private JMenuItem differentCharacter;

    private JPanel panelPressed;

    private ArrayList<JPanel> individualPlayerPanels;

    private ArrayList<JLabel> individualPlayerLabels;

    private JLabel playerLabel;

    private EditPanel editPanel;

    public StatusPanel statusPanel;

    private ArrayList<Creature> allMonsterList;

    private ArrayList<Creature> monstersToPlayList;

    private Integer[] indices;
    public MainMenuController() {

        mainMenu = new JFrame("Main Menu");
        mainMenu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainMenu.setLayout(new BorderLayout());
        mainMenu.setSize(650,650);
        mainMenu.setLocationRelativeTo(null);
        mainMenu.setResizable(false);

        statusPanel = new StatusPanel();

        configureMenu();
        configureMainPanel();

        editPanel = new EditPanel(new EditPanelListener());

        mainMenu.setVisible(true);

        showMainMenu();
    }

    private void configureMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem startItem = new JMenuItem("Start Game");
        JMenuItem createItem = new JMenuItem("Create Character");
        JMenuItem saveItem = new JMenuItem("Save Character");
        JMenuItem loadItem = new JMenuItem("Load Character");
        JMenuItem exitItem = new JMenuItem("Exit");

        startItem.addActionListener(this);
        createItem.addActionListener(this);
        saveItem.addActionListener(this);
        loadItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(startItem);
        fileMenu.add(createItem);
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        mainMenu.add(menuBar, BorderLayout.NORTH);

    }

    private void configureMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        startButton = new JButton("Start Game");
        createButton = new JButton("Create Character");
        saveButton = new JButton("Save Character");
        loadButton = new JButton("Load Character");
        exitButton = new JButton("Exit");


        if(playerList.isEmpty()) {
            saveButton.setEnabled(false);
        }

        startButton.setOpaque(true);
        createButton.setOpaque(true);
        saveButton.setOpaque(true);
        loadButton.setOpaque(true);
        exitButton.setOpaque(true);


        startButton.setBackground(Color.decode("#3d405b"));

        createButton.setBackground(Color.decode("#3d405b"));

        saveButton.setBackground(Color.decode("#3d405b"));

        loadButton.setBackground(Color.decode("#3d405b"));

        exitButton.setBackground(Color.decode("#3d405b"));

        startButton.addActionListener(this);
        createButton.addActionListener(this);
        saveButton.addActionListener(this);
        loadButton.addActionListener(this);
        exitButton.addActionListener(this);


        mainPanel.add(startButton, Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalGlue());

        mainPanel.add(createButton, Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalGlue());

        mainPanel.add(saveButton, Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalGlue());

        mainPanel.add(loadButton, Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalGlue());

        mainPanel.add(exitButton, Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalGlue());

        mainPanel.setOpaque(true);
        mainPanel.setBackground(Color.BLACK);
//        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainMenu.add(mainPanel, BorderLayout.WEST);

        configurePlayerPanel();

        mainMenu.add(playerPanel, BorderLayout.CENTER);

        mainMenu.add(statusPanel, BorderLayout.SOUTH);

    }


    private void updatePlayerPanel() {

        int size = playerList.size();
        JLabel updateLabel;
        Player p;

        if(playerList.isEmpty()) {
            for(int i = 0; i < 4; i++) {
                individualPlayerLabels.get(i).setText("Player " + (i+1) + ": Empty");
                individualPlayerLabels.get(i).setIcon(null);
            }

            saveButton.setEnabled(false);
        } else if(playerList.size() > 4) {
            return;
        } else {
            for(int j = 0; j < 4; j++) {

                if(j > playerList.size()-1) {
                    individualPlayerLabels.get(j).setText("Player " + (j+1) + ": Empty");
                    individualPlayerLabels.get(j).setIcon(null);
                } else {
                    updateLabel = individualPlayerLabels.get(j);

                    p = (Player) playerList.get(j);



                    ImageIcon icon = new ImageIcon(p.getAvatar().getAbsolutePath());
                    Image image = icon.getImage();
                    Image newImage = image.getScaledInstance(120,120, Image.SCALE_SMOOTH);

                    updateLabel.setIcon(new ImageIcon(newImage));
                    updateLabel.setText(p.getName());
                }

            }

        }
    }

    private void configurePlayerPanel() {

        individualPlayerPanels = new ArrayList<>();
        individualPlayerLabels = new ArrayList<>();

        playerPanel = new JPanel();
        playerLabel = new JLabel();

        playerPanel.setMaximumSize(new Dimension(10, 10));

        JPanel player1 = new JPanel();
        JPanel player2 = new JPanel();
        JPanel player3 = new JPanel();
        JPanel player4 = new JPanel();

        player1.setName("Panel 1");
        player2.setName("Panel 2");
        player3.setName("Panel 3");
        player4.setName("Panel 4");

        playerPanelClickMenu = new JPopupMenu();
        removeCharacter = new JMenuItem("Remove character");
        differentCharacter = new JMenuItem("Select different character");

        removeCharacter.addActionListener(this);
        differentCharacter.addActionListener(this);

        playerPanelClickMenu.add(differentCharacter);
        playerPanelClickMenu.add(removeCharacter);


        player1.addMouseListener(this);
        player2.addMouseListener(this);
        player3.addMouseListener(this);
        player4.addMouseListener(this);

        JLabel player1Label = new JLabel("Player 1: Empty");
        player1Label.setVerticalTextPosition(SwingConstants.BOTTOM);
        player1Label.setHorizontalTextPosition(SwingConstants.CENTER);

        JLabel player2Label = new JLabel("Player 2: Empty");
        player2Label.setVerticalTextPosition(SwingConstants.BOTTOM);
        player2Label.setHorizontalTextPosition(SwingConstants.CENTER);

        JLabel player3Label = new JLabel("Player 3: Empty");
        player3Label.setVerticalTextPosition(SwingConstants.BOTTOM);
        player3Label.setHorizontalTextPosition(SwingConstants.CENTER);

        JLabel player4Label = new JLabel("Player 4: Empty");
        player4Label.setVerticalTextPosition(SwingConstants.BOTTOM);
        player4Label.setHorizontalTextPosition(SwingConstants.CENTER);


        player1Label.setFont(new Font("Courier", Font.PLAIN, 20));
        player2Label.setFont(new Font("Courier", Font.PLAIN, 20));
        player3Label.setFont(new Font("Courier", Font.PLAIN, 20));
        player4Label.setFont(new Font("Courier", Font.PLAIN, 20));

        player1Label.setVerticalAlignment(SwingConstants.CENTER);
        player2Label.setVerticalAlignment(SwingConstants.CENTER);
        player3Label.setVerticalAlignment(SwingConstants.CENTER);
        player4Label.setVerticalAlignment(SwingConstants.CENTER);


        individualPlayerPanels.add(player1);
        individualPlayerPanels.add(player2);
        individualPlayerPanels.add(player3);
        individualPlayerPanels.add(player4);

        individualPlayerLabels.add(player1Label);
        individualPlayerLabels.add(player2Label);
        individualPlayerLabels.add(player3Label);
        individualPlayerLabels.add(player4Label);

        player1.add(player1Label);
        player2.add(player2Label);
        player3.add(player3Label);
        player4.add(player4Label);

        player1.setBounds(0,0,50,50);
        player2.setBounds(50,0,50,50);
        player3.setBounds(0,50,50,50);
        player4.setBounds(50,50,50,50);


        player1.setBackground(Color.white);
        player2.setBackground(Color.white);
        player3.setBackground(Color.white);
        player4.setBackground(Color.white);

        playerPanel.setLayout(new GridLayout(2,2, 5, 5));

        playerPanel.add(player1);
        playerPanel.add(player2);
        playerPanel.add(player3);
        playerPanel.add(player4);

        playerPanel.setOpaque(true);
        playerPanel.setBackground(Color.DARK_GRAY);

        mainPanel.setBorder(new EmptyBorder(10,40,5,5));
        mainPanel.add(playerPanel, BorderLayout.PAGE_END);



    }


    private void showMainMenu() {
        if(mainPanel.isShowing()) {
            return;
        }

        if(!playerList.isEmpty()) {
            saveButton.setEnabled(true);
        }

        mainMenu.remove(playerPanel);
        mainMenu.remove(editPanel);

        updatePlayerPanel();

        mainMenu.add(playerPanel);
        mainMenu.add(mainPanel);

        playerPanel.revalidate();
        playerPanel.repaint();

        mainMenu.revalidate();
        mainMenu.repaint();
    }

    private void showEditPanel() {
        if (editPanel.isShowing()) {
            return;
        }

        mainMenu.remove(mainPanel);
        mainMenu.remove(playerPanel);
        mainMenu.add(editPanel);
        mainMenu.revalidate();  // recalculates the layout
        mainMenu.repaint();

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        panelPressed = (JPanel) e.getComponent();
        playerPanelClickMenu.show(e.getComponent(), e.getX(), e.getY());

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private class EditPanelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();

            switch(action) {
                case "Edit.create":
                    int[] stats = editPanel.getCharacterStats();
                    int str = stats[0];
                    int dex = stats[1];
                    int con = stats[2];

                    File avatar = editPanel.getAvatarFile();

                    Weapon w = editPanel.getWeapon();

                    String name = editPanel.getName();

                    Player p = new Player(name, LocalDateTime.now(), 0, 0, str, dex, con, w, ArmedStatus.ARMED);

                    p.setAvatar(avatar);

                    System.out.println("HP: " + p.getHp());
                    System.out.println("AC: " + p.getAc());

                    playerList.add(p);

                    editPanel.clearEditPanel();

                    statusPanel.setStatusText("Player " + p.getName() + " created");
                    showMainMenu();

                    break;
                    case "Edit.cancel":
                        editPanel.clearEditPanel();
                        showMainMenu();

            }
        }
    }

    public void saveCharacterToFile() {

        boolean file_exists = false;
        Player p = null;

            try{
                DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
                JComboBox comboBox;
                JPanel panel = new JPanel();

                panel.add(new JLabel("Which player to save?"));

                for(var creature : playerList) {
                    comboBoxModel.addElement(creature.getName());
                }

                comboBox = new JComboBox(comboBoxModel);

                panel.add(comboBox);

                int result = JOptionPane.showConfirmDialog(null, panel, "Player selection", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

                switch (result) {
                    case JOptionPane.OK_OPTION:
                        p = (Player) playerList.get(comboBox.getSelectedIndex());
                        break;

                        case JOptionPane.CANCEL_OPTION:
                            return;

                }



                // getting last Player created in list
                Player playerToSave = p;
//                Player playerToSave = (Player) CreatureList.get(CreatureList.size()-1);

                // opening /saved/players/ directory
                File directory = new File("saved/players/");

                // filename to search
                String filename = playerToSave.getName() + ".csv";

                FileWriter fw;
                File f = null;

                File[] all_files = directory.listFiles();



                if(all_files != null) {
                    for(var file : all_files) {
                        if(file.getName().equals(filename)) {
                            file_exists = true;
                            f = file;
                            break;
                        }
                    }
                }

                if(file_exists) {
                    //overwrite data in existing file
                    String csvdata = String.format("%s,%d,%d,%d,%d,%s,%s,%d,%s", playerToSave.getName(), playerToSave.getHp(), playerToSave.getStr(), playerToSave.getDex(), playerToSave.getCon(), playerToSave.getWeapon().getName(), playerToSave.getWeapon().getDiceType(), playerToSave.getWeapon().getBonus(), playerToSave.getAvatar());

                    fw = new FileWriter(f, false);
                    fw.write(csvdata);
                    fw.close();
                    statusPanel.setStatusText("Player " + playerToSave.getName() + " saved");

                } else {
                    //new file with csv data

                    int responseStatus;

                    f = new File(filename);
                    String csvdata = String.format("%s,%d,%d,%d,%d,%s,%s,%d,%s", playerToSave.getName(), playerToSave.getHp(), playerToSave.getStr(), playerToSave.getDex(), playerToSave.getCon(), playerToSave.getWeapon().getName(), playerToSave.getWeapon().getDiceType(), playerToSave.getWeapon().getBonus(), playerToSave.getAvatar().getPath());

                    JFileChooser selector = new JFileChooser(new File("saved/players"));
                    selector.setSelectedFile(f);
                    selector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    selector.setFileFilter(new Filter());
                    responseStatus = selector.showSaveDialog(null);

                    if(responseStatus == JFileChooser.APPROVE_OPTION) {
                        try {

                            File file = selector.getSelectedFile();

                            fw = new FileWriter(file);
                            fw.write(csvdata);
                            fw.close();
                            statusPanel.setStatusText("Player " + playerToSave.getName() + " saved");

                        } catch (Exception e) {
                            statusPanel.setStatusText("Failed to save");
                            JOptionPane.showMessageDialog(null, "Error when saving file.");
                        }
                    }



                }


            } catch(IOException e) {
                e.printStackTrace();
            }

        }

    public void loadCharacter() {


        File directory = new File("saved/players");

        File[] all_files = directory.listFiles();


        try {
            int responseStatus;

            JFileChooser selector = new JFileChooser(new File("saved/players"));
            selector.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
            selector.setFileFilter(new Filter());
            responseStatus = selector.showOpenDialog(null);

            if (responseStatus == JFileChooser.APPROVE_OPTION) {

                File f = new File(selector.getSelectedFile().getAbsolutePath());
                Scanner in = new Scanner(f);

                String csvLine = in.nextLine();
                Player loading = Player.loadFromCsv(csvLine);

                in.close();

                playerList.add(loading);

                statusPanel.setStatusText("Loaded " + loading.getName());

            }


        } catch (CsvReadException | NullPointerException | FileNotFoundException e) {
            statusPanel.setStatusText("Problem with loading file!\n");
        }
    }

    public void differentCharacter(JPanel j) {
        int index = 0;

        for (int i = 0; i < individualPlayerPanels.size(); i++) {
            if (panelPressed.equals(individualPlayerPanels.get(i))) {
                index = i;
            }
        }

        File directory = new File("saved/players");

        File[] all_files = directory.listFiles();


        try {
            int responseStatus;

            JFileChooser selector = new JFileChooser(new File("saved/players"));
            selector.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
            selector.setFileFilter(new Filter());
            responseStatus = selector.showOpenDialog(null);

            if (responseStatus == JFileChooser.APPROVE_OPTION) {

                File f = new File(selector.getSelectedFile().getAbsolutePath());
                Scanner in = new Scanner(f);

                String csvLine = in.nextLine();
                Player loading = Player.loadFromCsv(csvLine);

                in.close();

                playerList.set(index, loading);

                statusPanel.setStatusText("Loaded " + loading.getName() + " as Player " + (index+1));


            }


        } catch (CsvReadException e) {
            statusPanel.setStatusText("Problem with loading player");
        } catch (FileNotFoundException e) {
            statusPanel.setStatusText("Problem with loading player");
        }
    }

        private void removeCharacter(JPanel panelPressed) {
        int index = 0;
        for(int i = 0; i < individualPlayerPanels.size(); i++) {
            if(panelPressed.equals(individualPlayerPanels.get(i))) {
                index = i;
            }
        }

            statusPanel.setStatusText("Removed " + playerList.get(index).getName() + " from Player " + (index+1));
            playerList.remove(index);



    }

    public ArrayList<Creature> monsterFromCsv(int monsters, ArrayList<Creature> monsterList) throws IOException {

        Scanner in = new Scanner(Path.of("monsters.csv"));

        monsterList = new ArrayList<>();


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

            monster.setAvatar(new File("1925435-200.png"));

            monsterList.add(monster);

        }

        return monsterList;

    }

    public synchronized void startGame() {



        GUIController guiController = new GUIController(allCreatureList, indices);



        guiController.game();
    }





    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();

        switch(command) {
            case "Create Character":
                showEditPanel();
                break;

            case "Edit Character":
                showEditPanel();
                break;

            case "Save Character":
                saveCharacterToFile();
                break;

            case "Load Character":
                loadCharacter();
                updatePlayerPanel();
                break;

            case "Remove character":
                removeCharacter(panelPressed);
                updatePlayerPanel();
                break;

            case "Select different character":
                differentCharacter(panelPressed);
                updatePlayerPanel();
                break;

            case "Exit":
                System.exit(0);
                break;

            case "Start Game":
                if(playerList.isEmpty()) {
                    statusPanel.setStatusText("Create players first!");
                } else {

                    Object[] numberOfMonsters = {0, 1, 2, 3, 4 ,5};

                    int n = JOptionPane.showOptionDialog(null, "Select number of monsters to battle against:", "Monster Selection",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                            numberOfMonsters, numberOfMonsters);

                    statusPanel.setStatusText("Loading " + n + " monsters to game");

                    try {
                        monstersToPlayList = monsterFromCsv(n, monstersToPlayList);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    allCreatureList.addAll(playerList);
                    allCreatureList.addAll(monstersToPlayList);

                    InitiativeUtility initUtil = new InitiativeUtility(allCreatureList.toArray(new Creature[0]));
                    indices = initUtil.rollInitiative();

                    String order = "Order: ";

                    for(int i = 0; i < indices.length; i++) {

                        order = order + String.format("%d. %s | ", i+1, allCreatureList.get(indices[i]).getName());
                    }

                    statusPanel.setStatusText(order);

                    startGame();



                }

        }

    }

}


