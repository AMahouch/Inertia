import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionPanel extends JPanel implements ActionListener {

    private JButton attackButton;

    private JButton disarmButton;

    private JButton finishButton;

    public ActionPanel() {
        this.setLayout(new GridLayout(3,1, 50, 50));

        attackButton = new JButton("Attack");
        disarmButton = new JButton("Disarm");
        finishButton = new JButton("End turn");

        attackButton.addActionListener(this);
        disarmButton.addActionListener(this);
        finishButton.addActionListener(this);

        attackButton.setForeground(Color.RED);
        disarmButton.setForeground(Color.RED);
        finishButton.setForeground(Color.RED);

        setBackground(Color.BLACK);

        add(attackButton);
        add(disarmButton);
        add(finishButton);

        setVisible(true);

    }

    public void setButtonsFalse() {
        attackButton.setEnabled(false);
        disarmButton.setEnabled(false);
        finishButton.setEnabled(false);
    }
    public void setButtonsTrue() {
        attackButton.setEnabled(true);
        disarmButton.setEnabled(true);
        finishButton.setEnabled(true);
    }


    @Override
    public synchronized void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();
        System.out.println(command);

        notifyAll();

    }
}
