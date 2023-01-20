import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class DDMGUI implements ActionListener {

    private static final int tmanhoDaTelaDaInterfaceWidth = 800;
    private static final int tmanhoDaTelaDaInterfaceHeigh = 800;
    private static final JLabel display1 = new JLabel();
    private static final JLabel display2 = new JLabel();
    private static final JLabel buttonsFrame = new JLabel();
    private static final JLabel consoleFrame = new JLabel();
    private static final JLabel Console0 = new JLabel();
    private static final JLabel Console1 = new JLabel();
    private static final JLabel Console2 = new JLabel();
    private static final JLabel Console3 = new JLabel();
    private static final JButton pressButton99 = new JButton();
    private static final JButton pressButton0 = new JButton();
    private static final JButton pressButton1 = new JButton();
    private static final JButton pressButton2 = new JButton();
    private static final JButton pressButton3 = new JButton();
    private static final JButton pressButton4 = new JButton();
    private static final JButton pressButton5 = new JButton();
    private static final JButton pressButton6 = new JButton();
    private static final JButton pressButton7 = new JButton();
    private static final JButton pressButton8 = new JButton();
    private static String ConsoleText1 = "";
    private static String ConsoleText2 = "";
    private static String ConsoleText3 = "";
    private static ArrayList<Integer> CoreCallsRequests;
    private static ImageIcon Display1Image;
    private static ImageIcon Display2Image;
    private static ImageIcon consoleIcon;
    private static ImageIcon logoImage;
    private static JFrame programFrame;
    public static int butZero = 0;
    public static int but99= 0;
    private static Color terminalGreen;

    public void start(ArrayList<Integer> InputCoreCallsRequests){
        terminalGreen = new Color(74,246,38);
        //label
        CoreCallsRequests = InputCoreCallsRequests;
        Border consoleBorder = BorderFactory.createLineBorder(Color.green,3);
        URL URLImg = super.getClass().getResource("Dalesoft.jpg");
        Display1Image = new ImageIcon(URLImg);
        URL URLImg2 = super.getClass().getResource("UFRJ.png");
        Display2Image = new ImageIcon(URLImg2);
        URL URLImg3 = super.getClass().getResource("ConsoleIcon.png");
        consoleIcon = new ImageIcon(URLImg3);
        URL URLImg4 = super.getClass().getResource("logo.png");
        logoImage = new ImageIcon(URLImg4);
        Image imageFromFile = Display1Image.getImage();
        Image imageFromFileWithFit = imageFromFile.getScaledInstance((((tmanhoDaTelaDaInterfaceWidth-200)/100)*90),(((tmanhoDaTelaDaInterfaceHeigh-100)/100)*90), Image.SCALE_SMOOTH);
        Display1Image = new ImageIcon(imageFromFileWithFit);

        //DISPLAY 1
        display1.setForeground(Color.green);
        display1.setText("Tratamento de imagens fornecidas pelo INPE: Canal 13, GOES-16.");
        display1.setIcon(Display1Image);
        display1.setHorizontalTextPosition(JLabel.CENTER);
        display1.setVerticalTextPosition(JLabel.TOP);
        display1.setHorizontalAlignment(JLabel.CENTER);
        display1.setVerticalAlignment(JLabel.CENTER);
        display1.setBounds(0,61,tmanhoDaTelaDaInterfaceWidth-200,tmanhoDaTelaDaInterfaceHeigh-100);
        display1.setBackground(Color.black);
        display1.setOpaque(true);
        display1.setBorder(consoleBorder);

        //DISPLAY 2
        display2.setForeground(Color.green);
        display2.setText("DaleDataMiner - 2022");
        display2.setIcon(Display2Image);
        display2.setHorizontalTextPosition(JLabel.CENTER);
        display2.setVerticalTextPosition(JLabel.TOP);
        display2.setHorizontalAlignment(JLabel.CENTER);
        display2.setVerticalAlignment(JLabel.CENTER);
        display2.setBounds(tmanhoDaTelaDaInterfaceWidth-200-3,tmanhoDaTelaDaInterfaceHeigh-439,187,400);
        display2.setBackground(Color.black);
        display2.setOpaque(true);
        display2.setBorder(consoleBorder);

        //buttonsFrame
        buttonsFrame.setBounds(tmanhoDaTelaDaInterfaceWidth-200-3,0,187,tmanhoDaTelaDaInterfaceHeigh-436);
        buttonsFrame.setBackground(Color.black);
        buttonsFrame.setOpaque(true);
        buttonsFrame.setBorder(consoleBorder);

        //ConsoleFrame
        consoleFrame.setBounds(47,0,tmanhoDaTelaDaInterfaceWidth-247,64);
        consoleFrame.setBackground(Color.black);
        consoleFrame.setOpaque(true);
        consoleFrame.setBorder(consoleBorder);

        //Console 0
        Console0.setBackground(Color.black);
        Console0.setBounds(0,0,50,(tmanhoDaTelaDaInterfaceHeigh-(tmanhoDaTelaDaInterfaceHeigh-64)));
        Console0.setForeground(Color.green);
        Console0.setHorizontalTextPosition(JLabel.CENTER);
        Console0.setVerticalAlignment(JLabel.CENTER);
        Console0.setIcon(consoleIcon);
        Console0.setOpaque(true);
        Console0.setOpaque(true);
        Console0.setBorder(consoleBorder);

        //Console 1
        Console1.setHorizontalTextPosition(JLabel.LEFT);
        Console1.setVerticalAlignment(JLabel.CENTER);
        Console1.setForeground(Color.green);
        Console1.setText("");
        Console1.setBackground(Color.black);
        Console1.setBounds(50,3,tmanhoDaTelaDaInterfaceWidth-253,(58/3));
        Console1.setOpaque(true);

        //Console 2
        Console2.setHorizontalTextPosition(JLabel.LEFT);
        Console2.setVerticalAlignment(JLabel.CENTER);
        Console2.setForeground(Color.green);
        Console2.setText("");
        Console2.setBackground(Color.black);
        Console2.setBounds(50,58/3,tmanhoDaTelaDaInterfaceWidth-253,(tmanhoDaTelaDaInterfaceHeigh-(tmanhoDaTelaDaInterfaceHeigh-58))/3);
        Console2.setOpaque(true);

        //Console 3
        Console3.setHorizontalTextPosition(JLabel.LEFT);
        Console3.setVerticalAlignment(JLabel.CENTER);
        Console3.setForeground(Color.green);
        Console3.setText("");
        Console3.setBackground(Color.black);
        Console3.setBounds(50,58/3+58/3,tmanhoDaTelaDaInterfaceWidth-253,(tmanhoDaTelaDaInterfaceHeigh-(tmanhoDaTelaDaInterfaceHeigh-58))/3);
        Console3.setOpaque(true);

        //button-1
        pressButton99.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-387,169,40);
        pressButton99.addActionListener(this);
        pressButton99.setFocusable(false);
        pressButton99.setText("Pausar Compare mode");
        pressButton99.setBackground(Color.black);
        pressButton99.setOpaque(true);
        pressButton99.setForeground(Color.green);
        pressButton99.setBorder(consoleBorder);

        //button0
        pressButton0.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-430,169,40);
        pressButton0.addActionListener(this);
        pressButton0.setFocusable(false);
        pressButton0.setText("Voltar");
        pressButton0.setBackground(Color.black);
        pressButton0.setOpaque(true);
        pressButton0.setForeground(Color.green);
        pressButton0.setBorder(consoleBorder);

        //DEV button1
        pressButton1.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-742,169,40); // POS 7
        //pressButton1.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-484,169,40); // devPOS 1
        pressButton1.addActionListener(this);
        pressButton1.setFocusable(false);
        pressButton1.setText("Gerar Links");
        pressButton1.setBackground(Color.black);
        pressButton1.setOpaque(true);
        pressButton1.setForeground(Color.green);
        pressButton1.setBorder(consoleBorder);

        //DEV button2
        pressButton2.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-699,169,40); //POS 6
        //pressButton2.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-527,169,40); // devPOS 2
        pressButton2.addActionListener(this);
        pressButton2.setFocusable(false);
        pressButton2.setText("Baixar Imagens");
        pressButton2.setBackground(Color.black);
        pressButton2.setOpaque(true);
        pressButton2.setForeground(Color.green);
        pressButton2.setBorder(consoleBorder);

        //button3
        pressButton3.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-656,169,40); // POS 5
        //pressButton3.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-570,169,40); // devPOS 3
        pressButton3.addActionListener(this);
        pressButton3.setFocusable(false);
        pressButton3.setText("Calibrar Separador (IA)");
        pressButton3.setBackground(Color.black);
        pressButton3.setOpaque(true);
        pressButton3.setForeground(Color.green);
        pressButton3.setBorder(consoleBorder);

        //button4
        pressButton4.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-785,169,40); // POS 8
        //pressButton4.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-613,169,40); // devPOS 4
        pressButton4.addActionListener(this);
        pressButton4.setFocusable(false);
        pressButton4.setText("Carregar config.json");
        pressButton4.setBackground(Color.black);
        pressButton4.setOpaque(true);
        pressButton4.setForeground(Color.green);
        pressButton4.setBorder(consoleBorder);

        //button5
        pressButton5.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-484,169,40);// POS 1
        //pressButton5.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-656,169,40); // devPOS 5
        pressButton5.addActionListener(this);
        pressButton5.setFocusable(false);
        pressButton5.setText("Sair");
        pressButton5.setBackground(Color.black);
        pressButton5.setOpaque(true);
        pressButton5.setForeground(Color.green);
        pressButton5.setBorder(consoleBorder);

        //button6
        pressButton6.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-613,169,40); // POS 4
        //pressButton6.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-699,169,40); // devPOS 6
        pressButton6.addActionListener(this);
        pressButton6.setFocusable(false);
        pressButton6.setText("Mapear pasta Data");
        pressButton6.setBackground(Color.black);
        pressButton6.setOpaque(true);
        pressButton6.setForeground(Color.green);
        pressButton6.setBorder(consoleBorder);

        //button7
        pressButton7.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-570,169,40); // POS 3
        //pressButton7.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-742,169,40); // devPOS 7
        pressButton7.addActionListener(this);
        pressButton7.setFocusable(false);
        pressButton7.setText("Separar Imagens (IA)");
        pressButton7.setBackground(Color.black);
        pressButton7.setOpaque(true);
        pressButton7.setForeground(Color.green);
        pressButton7.setBorder(consoleBorder);

        pressButton8.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-527,169,40); // POS 2
        //pressButton8.setBounds(tmanhoDaTelaDaInterfaceWidth-200+6,tmanhoDaTelaDaInterfaceHeigh-785,169,40); // devPOS 8
        pressButton8.addActionListener(this);
        pressButton8.setFocusable(false);
        pressButton8.setText("Comparar Imagens (IA)");
        pressButton8.setBackground(Color.black);
        pressButton8.setOpaque(true);
        pressButton8.setForeground(Color.green);
        pressButton8.setBorder(consoleBorder);

        //frame
        programFrame = new JFrame();
        programFrame.getContentPane().setBackground(Color.black);
        programFrame.setIconImage(logoImage.getImage());
        programFrame.setTitle("DDM - DaleDataMiner");
        programFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        programFrame.setSize(tmanhoDaTelaDaInterfaceWidth,tmanhoDaTelaDaInterfaceHeigh);
        programFrame.setResizable(false);
        programFrame.setVisible(true);
        programFrame.setLayout(null);
        programFrame.add(display1,0);
        programFrame.add(display2,0);
        programFrame.add(buttonsFrame,0);
        programFrame.add(consoleFrame,0);
        programFrame.add(Console0,1);
        programFrame.add(Console1,1);
        programFrame.add(Console2,1);
        programFrame.add(Console3,1);
        programFrame.add(pressButton1,1);
        programFrame.add(pressButton2,1);
        programFrame.add(pressButton3,1);
        programFrame.add(pressButton4,1);
        programFrame.add(pressButton5,1);
        programFrame.add(pressButton6,1);
        programFrame.add(pressButton7,1);
        programFrame.add(pressButton8,1);

        //contato
        ConsoleText("Contato: dalessandropv@gmail.com");
    }
    public void setDisplay1ImageTypeOfBufferedImage(BufferedImage newImage){
        Image downloadedImageRefit = newImage.getScaledInstance((((tmanhoDaTelaDaInterfaceWidth-200)/100)*90),(((tmanhoDaTelaDaInterfaceHeigh-100)/100)*90), Image.SCALE_FAST);
        display1.setIcon(new ImageIcon(downloadedImageRefit));
    }
    public void addButton0() throws InterruptedException {
        display2.setText(null);
        display2.setIcon(null);
        programFrame.add(pressButton0,2);
        pressButton0.updateUI();
        pressButton0.updateUI();
        pressButton0.updateUI();
        pressButton0.updateUI();
    }
    public void updateUIButton0() throws InterruptedException {
        pressButton0.updateUI();
    }
    public void addButton99() throws InterruptedException {
        programFrame.add(pressButton99,2);
        pressButton99.updateUI();
    }
    public void updateUIButton99() throws InterruptedException {
        pressButton99.updateUI();
    }
    public void removeButton0(){
        programFrame.remove(pressButton0);
    }

    public void removeButton99(){
        programFrame.remove(pressButton99);
    }

    public void setDisplay1text(String text){
        display1.setText(text);
    }

    public void setDisplay2text(String text){
        display2.setText(text);
    }

    public void setDisplay2ImageTypeOfBufferedImage(BufferedImage newImage){
        Image imagemComCropResize = newImage.getScaledInstance((((tmanhoDaTelaDaInterfaceWidth-200-3)/100)*50),(((tmanhoDaTelaDaInterfaceHeigh-439)/100)*50), Image.SCALE_FAST);
        display2.setIcon(new ImageIcon(imagemComCropResize));
    }
    public void ConsoleText(String newText){
        ConsoleText3 = ConsoleText2;
        ConsoleText2 = ConsoleText1;
        ConsoleText1 = newText;
        Console1.setText(ConsoleText1);
        Console2.setText(ConsoleText2);
        Console3.setText(ConsoleText3);
    }

    public void ToggleButtons(boolean state){
        if(state == false){
            pressButton1.setEnabled(false);
            pressButton2.setEnabled(false);
            pressButton3.setEnabled(false);
            pressButton4.setEnabled(false);
            pressButton5.setEnabled(false);
            pressButton6.setEnabled(false);
            pressButton7.setEnabled(false);
            pressButton8.setEnabled(false);
        }
        if(state == true){
            pressButton1.setEnabled(true);
            pressButton2.setEnabled(true);
            pressButton3.setEnabled(true);
            pressButton4.setEnabled(true);
            pressButton5.setEnabled(true);
            pressButton6.setEnabled(true);
            pressButton7.setEnabled(true);
            pressButton8.setEnabled(true);
        }
    }
    public void resetDisplay(){
        display1.setText("Tratamento de imagens fornecidas pelo INPE: Canal 13, GOES-16.");
        display1.setIcon(Display1Image);
        display2.setText("Dalessandropv@gmail.com");
        display2.setIcon(Display2Image);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == pressButton1){
            ToggleButtons(false);
            CoreCallsRequests.add(1);
        }
        if(e.getSource() == pressButton2){
            ToggleButtons(false);
            CoreCallsRequests.add(2);
        }
        if(e.getSource() == pressButton3){
            ToggleButtons(false);
            CoreCallsRequests.add(3);
        }
        if(e.getSource() == pressButton4){
            ToggleButtons(false);
            CoreCallsRequests.add(4);
        }
        if(e.getSource() == pressButton5){
            ToggleButtons(false);
            CoreCallsRequests.add(5);
        }
        if(e.getSource() == pressButton6){
            ToggleButtons(false);
            CoreCallsRequests.add(6);
        }
        if(e.getSource() == pressButton7){
            ToggleButtons(false);
            CoreCallsRequests.add(7);
        }
        if(e.getSource() == pressButton8){
            ToggleButtons(false);
            CoreCallsRequests.add(8);
        }
        if(e.getSource() == pressButton0){
            CoreCallsRequests.add(0); //voltar (especial)
            butZero = 1;
        }
        //pause (especial)
        if(e.getSource() == pressButton99){
            if(but99 == 1){
                but99 = 0;
                pressButton99.setText("Pausar Compare mode");
            } else {
                but99 = 1;
                pressButton99.setText("Resumir Compare mode");
            }
        }
    }
    public void reset(){
        display1.setText("Tratamento de imagens fornecidas pelo INPE: Canal 13, GOES-16.");
        display1.setIcon(Display1Image);
        display2.setText("DaleDataMiner - 2022");
        display2.setIcon(Display2Image);
    }
}
