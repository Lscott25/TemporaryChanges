import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SineGenerator {
    protected static final int SAMPLE_RATE = 16 * 1024;
    private int freq;
    private double amplitude;
    private boolean inclAng;
    private double modAmp;
    private int numMod;
    private double numTime;
    private int numIterations = 0;
    private SourceDataLine line;
    private boolean createExcel = false;
    private int numVisualizers = 0;
    private boolean hasCreatedExcel = false;

    public SineGenerator(int freq, double amplitude, boolean incAng, double modAmp, int numMod, double numTime) {
        this.freq = freq;
        this.amplitude = amplitude;
        this.inclAng = incAng;
        this.modAmp = modAmp;
        this.numMod = numMod;
        this.numTime = numTime;
        SpreadsheetMethods.removePreexistingFiles();
    }

    public byte[] createSinWaveBuffer() {
        int ms = (int) (numTime * 1000);
        int samples = (int) ((ms * SAMPLE_RATE) / 1000);
        byte[] output = new byte[samples];
        byte[] excelOutput = new byte[freq/10];
        double period = (double) SAMPLE_RATE / freq;

        for (int i = 0; i < output.length; i++) {
            double angle = 2.0 * Math.PI * i / (SAMPLE_RATE / (freq));
            if (modAmp <= 0) {
                output[i] = (byte) (Math.sin(angle) * amplitude);
            } else {
                output[i] = (byte) (Math.sin(recur(modAmp, angle, numMod, inclAng)) * 127f);
            }

            if (i<(freq/10)) {
                excelOutput[i] = output[i];
            }
            else {
                if  (createExcel && !hasCreatedExcel) {
                    System.out.println(!hasCreatedExcel);
                    SpreadsheetMethods.writeToExcel(excelOutput, numIterations);
                    hasCreatedExcel = true;
                    LineGraph.createLine(excelOutput);
                }    
            }
        }

        return output;
    }

    public void setExcelStatus() {
        createExcel = !createExcel;
    }
    public void startPlayback() {
        hasCreatedExcel = false;
        numIterations++;
        new Thread(() -> {
            try {
                final AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
                line = AudioSystem.getSourceDataLine(af);
                line.open(af, SAMPLE_RATE);
                line.start();

                while (true) {
                    byte[] toneBuffer = createSinWaveBuffer();

                    byte[] graphArr = new byte[freq/10];
                    for (int i = 0; i<(freq/10); i++) {
                        graphArr[i] = toneBuffer[i];
                    }
                
                    line.write(toneBuffer, 0, toneBuffer.length);
                }
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stopPlayback() {
        if (line != null) {
            line.stop();
            line.drain();
            line.close();
        }
    }

    public static double recur(int amplitude, double angle, int num) {
        if (num <= 1) {
            return angle + amplitude * Math.sin(angle);
        } else {
            return angle + amplitude * Math.sin(recur(amplitude, angle, num - 1));
        }
    }

    public static double recur(double amplitude, double angle, int num, boolean inclAng) {
        if (num <= 1) {
            if (inclAng)
                return angle + amplitude * Math.sin(angle);
            else
                return amplitude * Math.sin(angle);
        } 
        else {
            if (inclAng)
                return angle + amplitude * Math.sin(recur(amplitude, angle, num - 1, inclAng));
            else
                return amplitude * Math.sin(recur(amplitude, angle, num - 1, inclAng));
        }
    }
}
