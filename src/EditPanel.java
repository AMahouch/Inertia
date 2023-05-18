import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class EditPanel extends JPanel implements DocumentListener {

    private JLabel avatarLabel;

    private JTextArea header;

    private JPanel headerPanel;

    private NamePanel namePanel;

    private StatsPanel statPanel;

    private WeaponsPanel weaponsPanel;

    private AvatarPanel avatarPanel;

    private JButton createButton;

    private JButton cancelButton;
    private class WeaponsPanel extends JPanel {

        private JPanel weaponsPanel;

        private JComboBox weaponDropdown;

        private String[] weaponNames;

        private ArrayList<Weapon> weapons;

        public WeaponsPanel() {
            configureWeapons();
        }
        public void configureWeapons() {
            try{
              weapons = GameUtility.readWeapons();

            } catch(IOException e) {
                System.out.println(e.getMessage());
            }

            weaponDropdown = new JComboBox();

            for(var weapon : weapons) {
                weaponDropdown.addItem(weapon);
            }

            weaponsPanel = new JPanel();
            weaponsPanel.setBorder(BorderFactory.createTitledBorder("Weapon"));
            weaponsPanel.add(weaponDropdown);

            add(weaponsPanel);

        }

        public Weapon getWeapon() {
            return (Weapon) weaponDropdown.getSelectedItem();
        }

    }

    private class StatsPanel extends JPanel implements ActionListener {

        private JPanel statsPanel;

        private JPanel pointsPanel;

        private JLabel pointsLabel;

        private JTextField pointsText;

        private String[] stats;

        private JLabel statsLabel;

        private JSlider slider;

        private JPanel statPanel;

        private JButton randomButton;

        private ArrayList<JSlider> skillSliders = new ArrayList<>();

        private ArrayList<JPanel> panels = new ArrayList<>();

        private int totalStats;

        public StatsPanel() {
            configureStats();
        }



        private void configureStats() {
            pointsLabel = new JLabel("Remaining points:");
            pointsText = new JTextField("15", 5);
            pointsText.setEditable(false);

            pointsPanel = new JPanel();
            pointsPanel.add(pointsLabel);
            pointsPanel.add(pointsText);

            String[] stats = {"STR", "DEX", "CON"};
            int i = 0;

            for(String stat : stats) {
                statsLabel = new JLabel((stat));

                slider = new JSlider(JSlider.HORIZONTAL, 0, 15, 0);
                slider.setMajorTickSpacing(5);
                slider.setMinorTickSpacing(1);
                slider.setPaintLabels(true);
                slider.setPaintTicks(true);
                slider.addChangeListener((new Listener()));
                skillSliders.add(i, slider);

                statPanel = new JPanel();
                statPanel.add(statsLabel);
                statPanel.add(slider);
                panels.add(i, statPanel);
                i++;

            }

            statsPanel = new JPanel();
            statsPanel.setLayout(new GridLayout(5,1));
            statsPanel.setPreferredSize(new Dimension(300,300));
            statsPanel.setBorder((BorderFactory.createTitledBorder("Character stats:")));

            statsPanel.add(pointsPanel);

            randomButton = new JButton("Randomize Stats");
            randomButton.setPreferredSize(new Dimension(2,2));
            randomButton.addActionListener(this);
            randomButton.setActionCommand("Random");

            for(int j = 0; j <panels.size(); j++) {
                statsPanel.add(panels.get(j));
            }
            statsPanel.add(randomButton);

            add(statsPanel);

        }

        public void clearStatPanel() {
            for(var slider : skillSliders) {
                slider.setValue(0);
            }

            pointsText.setText(String.valueOf(15));
        }

        public void randomizeStats() {
            clearStatPanel();

            int random_Index;
            Random r = new Random();

            for(int i = 0; i < 15; i++) {
                random_Index = r.nextInt(3);

                skillSliders.get(random_Index).setValue(skillSliders.get(random_Index).getValue() + 1);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            switch (command) {
                case "Random":
                    randomizeStats();
                    break;
            }
        }

        private class Listener implements ChangeListener {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();

                if(slider.getValueIsAdjusting() == false) {
                    totalStats = 0;

                    for(int i = 0; i < skillSliders.size(); i++) {
                        totalStats+=skillSliders.get(i).getValue();

                        if(15 - totalStats >= 0) {
                            pointsText.setText(String.valueOf(15 - totalStats));
                        } else {
                            JOptionPane.showMessageDialog(null, "Out of points, max is 15!");
                            slider.setValue(0);
                        }
                    }
                }
            }
        }

        public int[] getStats(){
            int[] statsArray = new int[3];
            int i = 0;

            for(var slider : skillSliders) {
                statsArray[i] = slider.getValue();
                i++;
            }

            return statsArray;
        }

    }

    private class AvatarPanel extends JPanel implements ActionListener {

        private JPanel avatarPanel;
        private JComboBox avatarBox;
        private JLabel avatarLabel;
        private File imageFiles[] = new File("img/").listFiles();

        public void configureAvatar() {

            avatarPanel = new JPanel();
            avatarBox = new JComboBox<>(imageFiles);
            avatarBox.addActionListener(this);
            avatarLabel = new JLabel();

            avatarPanel.add(avatarBox);
            avatarPanel.add(avatarLabel);

            add(avatarPanel);


        }

        public AvatarPanel() {
            configureAvatar();
        }

        public void clearAvatarPanel() {
            avatarLabel.setText("");
            avatarLabel.setIcon(null);
        }
        public File getAvatar() {

            File f = (File) avatarBox.getSelectedItem();

            return f;

        }

        @Override
        public void actionPerformed(ActionEvent e) {

            int index = avatarBox.getSelectedIndex();
            File f = (File) avatarBox.getItemAt(index);

            ImageIcon i = new ImageIcon(f.getAbsolutePath());
            Image image = i.getImage();
            Image newImage = image.getScaledInstance(120,120, Image.SCALE_SMOOTH);

            avatarLabel.setIcon(new ImageIcon(newImage));

        }
    }

    private class NamePanel extends JPanel implements ActionListener {
        private JLabel name;
        private JTextField input;

            public NamePanel() {
            name = new JLabel();
            name.setText("Enter Character Name and Press Enter: ");
            input = new JTextField(25);
            input.addActionListener(this);

            add(name);
            add(input);
        }

        public void clearNamePanel() {
                name.setText("Enter Character Name and Press Enter: ");
                input.setText("");
        }

        public String getName() {
                return input.getText();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String nameInput = input.getText();

            if(nameInput.length() < 25 && (Character.isUpperCase((nameInput.charAt(0))) == true)) {
                String characterName = nameInput;
                createButton.setEnabled(true);
                name.setText("Enter Character Name: " + characterName);

            } else {
                JOptionPane.showMessageDialog(null, "Please enter a name less than 25 characters with a" +
                        " capitalized first character", "Try again!", JOptionPane.INFORMATION_MESSAGE);
                input.setText("");
            }

        }
    }

    public EditPanel(ActionListener actionListener) {


        namePanel = new NamePanel();

        header = new JTextArea("Create your character!\n\nChoose a name, assign 15 points of stats, choose a weapon, and choose an avatar!");

        header.setEditable(false);
        header.setWrapStyleWord(true);
        header.setLineWrap(true);
        header.setBounds(0,0,300,300);
        headerPanel = new JPanel();
        headerPanel.setOpaque(true);
        headerPanel.add(header);
        headerPanel.setBackground(Color.black);

        statPanel = new StatsPanel();
        statPanel.setVisible(true);

        weaponsPanel = new WeaponsPanel();
        weaponsPanel.setVisible(true);

        avatarPanel = new AvatarPanel();
        avatarPanel.setVisible(true);

        createButton = new JButton("Create");
        cancelButton = new JButton("Cancel");

        createButton.addActionListener(actionListener);
        createButton.setActionCommand("Edit.create");
        createButton.setEnabled(false);

        cancelButton.addActionListener(actionListener);
        cancelButton.setActionCommand("Edit.cancel");

        add(headerPanel);
        add(namePanel);
        add(statPanel);
        add(weaponsPanel);
        add(avatarPanel);

        add(createButton);
        add(cancelButton);

        namePanel.setBackground(Color.decode("#f1faee"));
        statPanel.setBackground(Color.decode("#f1faee"));
        weaponsPanel.setBackground(Color.decode("#f1faee"));
        avatarPanel.setBackground(Color.decode("#f1faee"));
        this.setBackground(Color.decode("#f1faee"));

    }

    public void clearEditPanel() {
        avatarPanel.clearAvatarPanel();
        namePanel.clearNamePanel();
        statPanel.clearStatPanel();

        createButton.setEnabled(false);
    }

    public int[] getCharacterStats() {
        return statPanel.getStats();
    }

    public File getAvatarFile() {
        return avatarPanel.getAvatar();
    }

    public Weapon getWeapon() {
        return weaponsPanel.getWeapon();
    }

    public String getName() {
        return namePanel.getName();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        createButton.setEnabled(getName().length() != 0);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        createButton.setEnabled(getName().length() != 0);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }
}
