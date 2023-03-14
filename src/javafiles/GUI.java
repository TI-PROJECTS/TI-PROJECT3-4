package javafiles;

import java.awt.BorderLayout;
import java.awt.Color; // for using colors
import java.awt.Dimension;
import java.awt.Font; // for using fonts
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;

public class GUI {
    private JFrame frame;
    private JLabel time, labelScanCard, labelWelkom, enterPinLabel, cardDetectedLabel;
    private JPanel northPanel, centerPanel, homescreen, cardDetectedScreen;
    private JButton backButton;
    private SimpleDateFormat formatter;
    static ImageIcon bankImage = new ImageIcon("././img/banklogo.png");
    static ImageIcon bankImageIcon = new ImageIcon("././img/banklogoRond.png");
    String dateTime = "Hier moet de tijd komen";

    private void clearScreen(){
        centerPanel.remove(homescreen);
        centerPanel.remove(cardDetectedScreen);
    }

    public void updateTime(){
        Date date = new Date(System.currentTimeMillis());
        dateTime = (formatter.format(date));
        time.setText(dateTime);
        SwingUtilities.updateComponentTreeUI(time);
    }
    
    public void startup(){
        initialize();
        startGui();
        setHomeScreen();
    }

    private void initialize(){
        // onderdelen die in meerdere schermen nodig zijn
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1800, 950);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        
        northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(new Color(110, 136, 241)); // north panel
        northPanel.setPreferredSize(new Dimension(100, 50));
        centerPanel = new JPanel();
        centerPanel.setBackground(Color.white); // center panel
        centerPanel.setPreferredSize(new Dimension(100, 100));
        
        formatter = new SimpleDateFormat("dd-MM-yyyy '  ' HH:mm   ");
        time = new JLabel();
        time.setFont(new Font(null, Font.BOLD, 25));
        time.setText(dateTime);
        time.setForeground(Color.WHITE);
        
        backButton = new JButton();

        
        // onderdelen voor het hoofdscherm
        labelScanCard = new JLabel("Scan uw pas om te beginnen");
        labelScanCard.setFont(new Font(null, Font.PLAIN, 35));
        labelScanCard.setIcon(bankImage);
        labelScanCard.setVerticalTextPosition(JLabel.BOTTOM);
        labelScanCard.setHorizontalTextPosition(JLabel.CENTER);

        labelWelkom = new JLabel("Welkom bij Bank");
        labelWelkom.setVerticalTextPosition(JLabel.TOP);
        labelWelkom.setHorizontalTextPosition(JLabel.CENTER);
        labelWelkom.setFont(new Font(null, Font.PLAIN, 60));

        homescreen = new JPanel(new BorderLayout());
        homescreen.add(labelScanCard, BorderLayout.CENTER);
        homescreen.add(labelWelkom, BorderLayout.NORTH);
        homescreen.setBackground(Color.white);


        // onderdelen voor het scherm wanneer een pas gedetecteerd is
        enterPinLabel = new JLabel("Voer uw pincode in");
        enterPinLabel.setFont(new Font(null, Font.PLAIN, 35));
        enterPinLabel.setIcon(bankImage);
        enterPinLabel.setVerticalTextPosition(JLabel.BOTTOM);
        enterPinLabel.setHorizontalTextPosition(JLabel.CENTER);

        cardDetectedLabel = new JLabel("Uw pas is gedetecteerd");
        cardDetectedLabel.setVerticalTextPosition(JLabel.TOP);
        cardDetectedLabel.setHorizontalTextPosition(JLabel.CENTER);
        cardDetectedLabel.setFont(new Font(null, Font.PLAIN, 60));

        // TODO 4 labels toevoegen voor 1 t/m 4 nummers ingetoetst EN labels uitlijnen

        cardDetectedScreen = new JPanel(new BorderLayout());
        cardDetectedScreen.add(enterPinLabel, BorderLayout.CENTER);
        cardDetectedScreen.add(cardDetectedLabel, BorderLayout.NORTH);
        cardDetectedScreen.setBackground(Color.white);

    }

    private void startGui() {
        northPanel.add(time, BorderLayout.EAST);
        frame.add(northPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
    }

    public void setHomeScreen() {
        clearScreen();
        centerPanel.add(homescreen);
        SwingUtilities.updateComponentTreeUI(frame);
    }

    public void setCardDetectedScreen(){
        clearScreen();
        centerPanel.add(cardDetectedScreen);
    }
}
