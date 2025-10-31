import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame{

    GridBagConstraints c;
    GridBagLayout layout;
    boolean RIGHT_TO_LEFT = false;
    char[] action;
    JLabel[] p1cards;
    JLabel[] p2cards;
    JButton[] buttons;
    JLabel p1points;
    JLabel p2points;
    JLabel msg;
    JLabel round;
    boolean turn;

    GUI(){
        layout = new GridBagLayout();
        c = new GridBagConstraints();
        action = new char[2];
        p1cards = new JLabel[6];
        p2cards = new JLabel[6];
        buttons = new JButton[13];
        turn = false;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("CoExistence");
        this.setLocationRelativeTo(null);
        this.setLayout(layout);

        populateLayout();

        this.setVisible(true);
        this.setSize(600,400);
    }

    public void updateLayout(char[][] gameboard){
        updateCards(gameboard);
        updateMsg(gameboard);

        p2points.setText(gameboard[7][38] + "");
        p1points.setText(gameboard[9][38] + "");
        round.setText("R: " + gameboard[8][38]);
    }

    public void updateMsg(char[][] gameboard){
        String msg = "";
        int limit = 38;
        int i = 17;
        int j = 1;

        while(gameboard[i][limit] == ' ') limit--;

        while(j != limit + 1) {
            msg += gameboard[i][j];
            j++;
        }

        this.msg.setText(msg);
    }

    public void updateCards(char[][] gameboard){
        for(int i = 0; i < 6; i++){
            p2cards[i].setText(checkCard(3, 3 + i*5, gameboard));
        }

        for(int i = 0; i < 6; i++){
            p1cards[i].setText(checkCard(11, 3 + i*5, gameboard));
        }
    }

    public String checkCard(int i, int j, char[][] gameboard){
        String card = "";

        if(gameboard[i][j] == '=' && gameboard[i][j + 1] == ']') card = "Hammer";
        else if(gameboard[i][j] == '^' && gameboard[i][j + 1] == ' ') card = "Arrow";
        else if(gameboard[i][j] == ' ' && gameboard[i][j + 1] == '/') card = "Sword";
        else if(gameboard[i][j] == '7' && gameboard[i][j + 1] == '>') card = "Axe";

        return card;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void populateLayout(){

        if (RIGHT_TO_LEFT) {
            this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        JButton button;
        JLabel label;
        JPanel panel;

        resetContraints();
        // 1st Row
        button = new JButton("A");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                action[1] = actionEvent.getActionCommand().toCharArray()[0];
                
            }
        });
        c.gridx = 0;
        this.add(button, c);

        this.buttons[0] = button;

        button = new JButton("B");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                action[1] = actionEvent.getActionCommand().toCharArray()[0];
                
            }
        });
        c.gridx = 1;
        this.add(button, c);

        this.buttons[1] = button;

        button = new JButton("C");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                action[1] = actionEvent.getActionCommand().toCharArray()[0];
                
            }
        });
        c.gridx = 2;
        this.add(button, c);

        this.buttons[2] = button;

        button = new JButton("D");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                action[1] = actionEvent.getActionCommand().toCharArray()[0];
                
            }
        });
        c.gridx = 3;
        this.add(button, c);

        this.buttons[3] = button;

        button = new JButton("E");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                action[1] = actionEvent.getActionCommand().toCharArray()[0];
                
            }
        });
        c.gridx = 4;
        this.add(button, c);

        this.buttons[4] = button;

        button = new JButton("F");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                action[1] = actionEvent.getActionCommand().toCharArray()[0];
                
            }
        });
        c.gridx = 5;
        this.add(button, c);

        this.buttons[5] = button;

        label = new JLabel("0");
        c.gridx = 6;
        this.add(label,c);

        p2points = label; // Save this label so we can update it later

        resetContraints();
        // 2nd row

        c.gridx = 0;
        c.gridy = 2;
        c.gridheight = 2;
        c.gridwidth = 1;
        c.weighty = 1.0;

        for(int i = 0; i < 6; i++){
            c.gridx = i;
            label = new JLabel("Card");
            panel = new JPanel();
            panel.add(label);
            panel.setBackground(new Color(255, 255, 255));

            // Add player's card to corresponding array
            p2cards[i] = label;

            this.add(panel, c);
        }

        c.weighty = 0;
        c.gridheight = 1;
        button = new JButton("PS");
        c.gridx = 6;
        this.add(button, c);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                action[0] = 'P';
                action[1] = 'S';

            }
        });

        this.buttons[6] = button;

        resetContraints();
        // Third Row
        c.gridx = 0;
        c.gridy = 4;
        c.gridheight = 2;
        c.gridwidth = 1;
        c.weighty = 1.0;

        for(int i = 0; i < 6; i++){
            c.gridx = i;
            label = new JLabel("Card");
            panel = new JPanel();
            panel.add(label);
            panel.setBackground(new Color(255, 255, 255));

            // Add player's card to corresponding array
            p1cards[i] = label;

            this.add(panel, c);
        }

        c.weighty = 0;
        c.gridheight = 1;
        label = new JLabel("R: 1");
        c.gridx = 6;
        this.add(label, c);

        round = label;  // Save this label so we can update it later

        resetContraints();
        // Fourth Row
        c.gridy = 6;

        button = new JButton("A");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                action[0] = actionEvent.getActionCommand().toCharArray()[0];
                
            }
        });
        c.gridx = 0;
        this.add(button, c);

        this.buttons[7] = button;

        button = new JButton("B");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                action[0] = actionEvent.getActionCommand().toCharArray()[0];
                
            }
        });
        c.gridx = 1;
        this.add(button, c);

        this.buttons[8] = button;

        button = new JButton("C");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                action[0] = actionEvent.getActionCommand().toCharArray()[0];
                
            }
        });
        c.gridx = 2;
        this.add(button, c);

        this.buttons[9] = button;

        button = new JButton("D");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                action[0] = actionEvent.getActionCommand().toCharArray()[0];
                
            }
        });
        c.gridx = 3;
        this.add(button, c);

        this.buttons[10] = button;

        button = new JButton("E");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                action[0] = actionEvent.getActionCommand().toCharArray()[0];
                
            }
        });
        c.gridx = 4;
        this.add(button, c);

        this.buttons[11] = button;

        button = new JButton("F");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                action[0] = actionEvent.getActionCommand().toCharArray()[0];
                
            }
        });
        c.gridx = 5;
        this.add(button, c);

        this.buttons[12] = button;

        label = new JLabel("0");
        c.gridx = 6;
        this.add(label, c);

        p1points = label; // Save this label so we can update it later

        resetContraints();
        // Fifth row
        c.gridy = 7;
        c.gridwidth = 7;
        label = new JLabel("Message Log", SwingConstants.LEFT);
        panel = new JPanel();
        panel.add(label);
        panel.setBackground(new Color(255, 255, 255));

        this.add(panel, c);

        msg = label; // Save this label so we can update it later
    }

    public String checkForMove() {
        String returnValue = "";

        if (action[0] != '\0' && action[1] != '\0') {

            returnValue = new String(action);

            // Reset actions after processing
            action[0] = '\0';
            action[1] = '\0';
        }

        return returnValue;
    }

    public void resetContraints(){
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.BOTH;
    }

    public void updateButtons(){
        if(turn) enableButtons();
        else disableButtons();
    }

    public void disableButtons(){
        for(JButton button : buttons) button.setEnabled(false);
    }

    public void enableButtons(){
        for(JButton button : buttons) button.setEnabled(true);
    }
}