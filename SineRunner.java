import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class SineRunner {
    public static void main(String[] args) throws FileNotFoundException {
        JFrame frame = new JFrame("Sound Player");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JButton playButton = new JButton("Play");
        JButton stopButton = new JButton("Stop");
        JCheckBox excelBox = new JCheckBox("Create Excel File with Outputs");
        JButton removeSpreadsheetsButton = new JButton("Remove Pre-existing Spreadsheets");

        SineGenerator generator = new SineGenerator(440, 1, false, 3, 3, 30);


        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generator.startPlayback();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generator.stopPlayback();
            }
        });

        excelBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generator.setExcelStatus();
            }
        });

        removeSpreadsheetsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SpreadsheetMethods.removePreexistingFiles();
            }
        });

        

        panel.add(playButton);
        panel.add(stopButton);
        panel.add(excelBox);
        panel.add(removeSpreadsheetsButton);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}