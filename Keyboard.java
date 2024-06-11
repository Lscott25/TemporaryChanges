Comments
47


Add a comment...


Pinned by Leo Ono
@LeoOno
2 years ago
Github deleted this project, so here's the source:

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

/**
 * Java Virtual Piano
 * 
 * reference: https://docs.oracle.com/javase/tutorial/sound/MIDI-synth.html
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class VirtualPianoView extends JPanel implements KeyListener {
    
    private Synthesizer synthesizer;
    private MidiChannel channel;
    
    private String blackKeys = "WE TYU ";
    private String whiteKeys = "ASDFGHJ";
    private String allKeys = "AWSEDFTGYHUJ";
    
    private int octave = 5; // starts at middle C
    private boolean[] keyOn = new boolean[allKeys.length()];
    private static final int KEYS_PER_OCTAVE = 12;
    
    public VirtualPianoView() {
        addKeyListener(this);
        startSynthesizer();
    }

    private void startSynthesizer()  {
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            channel = synthesizer.getChannels()[0];
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.translate(50, 50);
        
        g.drawString("Octave: " + octave, 0, -10);
        
        // draw white keys
        final int WHITE_KEY_WIDTH = 40;
        final int WHITE_KEY_HEIGHT = 100;
        for (int k = 0; k < whiteKeys.length(); k++) {
            g.setColor(keyOn[allKeys.indexOf(whiteKeys.charAt(k))] ? Color.GREEN : Color.WHITE);
            g.fillRect(k * WHITE_KEY_WIDTH, 0, WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT);
            g.setColor(Color.BLACK);
            g.drawRect(k * WHITE_KEY_WIDTH, 0, WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT);
            g.drawString(" " + whiteKeys.charAt(k), k * WHITE_KEY_WIDTH + 10, WHITE_KEY_HEIGHT - 10);
        }

        // draw black keys
        final int BLACK_KEY_WIDTH = WHITE_KEY_WIDTH / 2;
        final int BLACK_KEY_HEIGHT = WHITE_KEY_HEIGHT / 2;
        for (int k = 0; k < blackKeys.length(); k++) {
            if (blackKeys.charAt(k) == ' ') {
                continue;
            }
            int x = (k + 1) * WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2;
            g.setColor(keyOn[allKeys.indexOf(blackKeys.charAt(k))] ? Color.GREEN : Color.BLACK);
            g.fillRect(x, 1, BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
            g.setColor(Color.WHITE);
            g.drawRect(x, 1, BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
            g.drawString(" " + blackKeys.charAt(k), x + 2, BLACK_KEY_HEIGHT - 5);
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        repaint();
        
        // select octave using 0~8 keys
        if ("012345678".contains("" + e.getKeyChar())) {
            octave = e.getKeyCode() - 48;
        }
        
        // select octave using '+' or '-' keys
        if (e.getKeyChar() == '+') {
            octave = octave < 8 ? octave + 1 : 8;
        }
        else if (e.getKeyChar() == '-') {
            octave = octave > 0 ? octave - 1 : 0;
        }
        
        // play a note
        int noteIndex = allKeys.indexOf((char) e.getKeyCode()); 
        if (noteIndex < 0 || keyOn[noteIndex]) {
            return;
        }
        keyOn[noteIndex] = true;
        channel.noteOn(octave * KEYS_PER_OCTAVE + noteIndex, 90);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        repaint();
        int noteIndex = allKeys.indexOf((char) e.getKeyCode()); 
        if (noteIndex < 0) {
            return;
        }
        keyOn[noteIndex] = false;
        channel.noteOff(octave * KEYS_PER_OCTAVE + noteIndex);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VirtualPianoView view = new VirtualPianoView();
                JFrame frame = new JFrame();
                frame.setSize(400, 300);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.getContentPane().add(view);
                frame.setVisible(true);
                view.requestFocus();
            }
        });
    }
    
}