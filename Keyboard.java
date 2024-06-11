import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Keyboard extends JPanel implements KeyListener {

    private MidiChannel channel;
    private boolean[] whiteKeysPlayed = {false, false, false, false, false, false, false};
    private int[] whiteKeyCodes = {65, 83, 68, 70, 71, 72, 74};
    private boolean[] blackKeysPlayed = {false, false, false, false, false};
    private int[] blackKeyCodes = {87, 69, 84, 98, 95};
    private int[] blackKeyXPos = {27, 90, 205, 263, 320};

    
    public Keyboard () {
        this.addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case 65: whiteKeysPlayed[0] = true; 
                    break;
            case 83: whiteKeysPlayed[1] = true;
                    break;
            case 68: whiteKeysPlayed[2] = true;
                    break;
            case 70: whiteKeysPlayed[3] = true;
                    break;
            case 71: whiteKeysPlayed[4] = true;
                    break;
            case 72: whiteKeysPlayed[5] = true;
                    break;
            case 74: whiteKeysPlayed[6] = true;
                    break;
    
            case 87: blackKeysPlayed[0] = true;
                    break;
            case 69: blackKeysPlayed[1] = true;
                    break;
            case 84: blackKeysPlayed[2] = true;
                    break;
            case 89: blackKeysPlayed[3] = true;
                    break;
            case 85: blackKeysPlayed[4] = true;
                    break;
        }
        this.repaint();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.green);
        

        g.setColor(Color.red);
        for (int i = 0; i<5; i++) {
            g.setColor(Color.red);
            if (blackKeysPlayed[i]) {
                g.setColor(Color.white);
            }

            g.fillRect(blackKeyXPos[i], this.getHeight()-210, 53, 96);
        }
        
        for (int i = 0; i<7; i++) {
            g.setColor(Color.black);
            if (whiteKeysPlayed[i]) {
                g.setColor(Color.white);
            }
            g.fillRect(i*58, this.getHeight()-110, 53, 110);
        }

        

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Keyboard view = new Keyboard();
                JFrame frame = new JFrame();
                frame.setSize(396, 300);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                frame.getContentPane().add(view);
                frame.setVisible(true);
                view.requestFocus();
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case 65: whiteKeysPlayed[0] = false; 
                    break;
            case 83: whiteKeysPlayed[1] = false;
                    break;
            case 68: whiteKeysPlayed[2] = false;
                    break;
            case 70: whiteKeysPlayed[3] = false;
                    break;
            case 71: whiteKeysPlayed[4] = false;
                    break;
            case 72: whiteKeysPlayed[5] = false;
                    break;
            case 74: whiteKeysPlayed[6] = false;
                    break;
    
            case 87: blackKeysPlayed[0] = false;
                    break;
            case 69: blackKeysPlayed[1] = false;
                    break;
            case 84: blackKeysPlayed[2] = false;
                    break;
            case 89: blackKeysPlayed[3] = false;
                    break;
            case 85: blackKeysPlayed[4] = false;
                    break;
        }
        this.repaint();
    }

    
}