import javax.swing.*;

public class LineGraph {
    public static void createLine(byte[] values) {
        JFrame frame = new JFrame("Byte Line Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ScatterPlot graph = new ScatterPlot(values);
        frame.add(graph);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}
