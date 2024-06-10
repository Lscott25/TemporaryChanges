import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class ScatterPlot extends JPanel {

    private byte[] values;

    public ScatterPlot(byte[] values) {
        this.values = values;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Determine scaling
        int width = getWidth();
        int height = getHeight();
        int maxValue = Byte.MIN_VALUE;
        int minValue = Byte.MAX_VALUE;
        for (byte value : values) {
            if (value > maxValue) maxValue = value;
            if (value < minValue) minValue = value;
        }
        double xScale = (double) width / (values.length - 1);
        double yScale = (double) height / (maxValue - minValue);

        // Draw lines connecting points
        g.setColor(Color.BLUE);
        for (int i = 0; i < values.length - 1; i++) {
            int x1 = (int) (i * xScale);
            int y1 = (int) ((values[i] - minValue) * yScale);
            int x2 = (int) ((i + 1) * xScale);
            int y2 = (int) ((values[i + 1] - minValue) * yScale);
            g.drawLine(x1, height - y1, x2, height - y2);
        }
    }
}