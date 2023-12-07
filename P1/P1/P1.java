import java.io.File;
import java.io.IOException;

public class P1 {
    public static void main(String[] args) {
        try {
            
            String exePath = "./parser.exe";

            // Create a ProcessBuilder for the executable
            ProcessBuilder processBuilder = new ProcessBuilder(exePath);

            // Specify input and output files for redirection
            File inputFile =  new File("./Input.macrojava");
            File outputFile = new File("./TempOuts/P1.out");

            // Redirect input from the input file
            processBuilder.redirectInput(inputFile);

            // Redirect output to the output file
            processBuilder.redirectOutput(outputFile);

            // Start the process
            Process process = processBuilder.start();

            // Wait for the process to complete
            int exitCode = process.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
