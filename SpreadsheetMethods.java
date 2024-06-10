import java.io.File;
import java.util.Objects;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SpreadsheetMethods {
    public static void removePreexistingFiles() {
        File directory = new File("./OutputFiles/");

        directory.listFiles();
        for (File f : Objects.requireNonNull(directory.listFiles())) {
            f.delete();
        }
    }

    public static void writeToExcel(byte[] arr, int numIterations) {
        try {
         
            PrintWriter writer = new PrintWriter(new File("./OutputFiles/output" + numIterations + ".csv"));
            for (byte b : arr) {
                writer.println(b);
            }
            writer.close();
        }
        catch(FileNotFoundException e) {
            System.out.println(e);
        }
    }
}
