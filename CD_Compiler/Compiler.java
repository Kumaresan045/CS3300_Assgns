import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Compiler {
    public static void main(String[] args) {
        try {
            
            //P1
            String path1 = "./P1.exe";
            ProcessBuilder processBuilder1 = new ProcessBuilder(path1);
            File inputFile1 =  new File("./Input.macrojava");
            File outputFile1 = new File("./TempOuts/P1.out");
            processBuilder1.redirectInput(inputFile1);
            processBuilder1.redirectOutput(outputFile1);
            Process process1 = processBuilder1.start();
            int exitCode = process1.waitFor();

            //P2
            String path2 = "./P2.jar";
            ProcessBuilder processBuilder2 = new ProcessBuilder("java", "-jar", path2);
            File inputFile2 =  new File("./TempOuts/P1.out");
            File outputFile2 = new File("./TempOuts/P2.out");
            processBuilder2.redirectInput(inputFile2);
            processBuilder2.redirectOutput(outputFile2);
            Process process2 = processBuilder2.start();
            exitCode = process2.waitFor();

            //Type Check Successful?
            String filePath = "./TempOuts/P2.out";
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String typecheckResult;
            typecheckResult = reader.readLine();
            if(!(typecheckResult.equals("Program type checked successfully")))
            {
                System.out.println(typecheckResult);
                return;
            }
            
            //P3
            String path3 = "./P3.jar";
            ProcessBuilder processBuilder3 = new ProcessBuilder("java", "-jar", path3);
            File inputFile3 =  new File("./TempOuts/P1.out");
            File outputFile3 = new File("./TempOuts/P3.out");
            processBuilder3.redirectInput(inputFile3);
            processBuilder3.redirectOutput(outputFile3);
            Process process3 = processBuilder3.start();
            exitCode = process3.waitFor();

            //P4
            String path4 = "./P4.jar";
            ProcessBuilder processBuilder4 = new ProcessBuilder("java", "-jar", path4);
            File inputFile4 =  new File("./TempOuts/P3.out");
            File outputFile4 = new File("./TempOuts/P4.out");
            processBuilder4.redirectInput(inputFile4);
            processBuilder4.redirectOutput(outputFile4);
            Process process4 = processBuilder4.start();
            exitCode = process4.waitFor();

            //P5
            String path5 = "./P5.jar";
            ProcessBuilder processBuilder5 = new ProcessBuilder("java", "-jar", path5);
            File inputFile5 =  new File("./TempOuts/P4.out");
            File outputFile5 = new File("./Output.s");
            processBuilder5.redirectInput(inputFile5);
            processBuilder5.redirectOutput(outputFile5);
            Process process5 = processBuilder5.start();
            exitCode = process5.waitFor();
           
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
