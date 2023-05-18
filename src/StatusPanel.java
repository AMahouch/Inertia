import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.util.TimerTask;

public class StatusPanel extends JPanel {

    public JLabel statusLabel;

    public StatusPanel() {
        this.setBackground(Color.decode("#3b56d1"));
        this.setBorder(new BevelBorder(BevelBorder.LOWERED));

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        statusLabel = new JLabel("Status: ");
        statusLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD | Font.ITALIC, 20));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusLabel.setHorizontalTextPosition(SwingConstants.CENTER);

        add(statusLabel);
    }


    class TimerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            setBackground(Color.decode("#3b56d1"));
        }
    }
    public void setStatusText(String text) {
        setBackground(Color.decode("#a4f26f"));
        statusLabel.setText(text);


        Timer timer = new Timer(1250, new TimerListener());

        timer.setRepeats(false);
        timer.start();

    }

    public void resetStatusColor() {
        this.setBackground(Color.decode("#3b56d1"));
    }

}
