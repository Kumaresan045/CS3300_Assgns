## About
1. This zip file contains Run Folder, run.sh file and pgi.jar.
2. To run the tcs, copy the Run Folder, run.sh file and pgi.jar to the same directory as P3.java.
3. The Test Cases can be run by executing the run.sh file.
4. Cd to the directory where P3.java is there.

## Run.sh
1. To run all tcs together, do 
```$ ./run.sh all```

2. To run a particular tc, do
```$ ./run.sh T3.java``` (for T3.java tc)

3. ```$ ./run.sh``` runs SampleTest.java tc by default.

## Working of run.sh
1. The Run Folder contains 4 subdirectories :- Tests, IROut, ExpOut and FinalOut.
2. The script basically stores the generated microIR code for each .java tc and generates the final output using pgi    interpreter for microIR code.
3. It then compares the final output with expected output (by running javac on each .java tc)
4. Initially IROut contains the correct IR for each tc, you can store it for future debugging in case some tc fails.

## Note
1. If the run.sh doesn't run, try ```$ chmod +x run.sh```.
2. If it still fails or gives some error message, try to remove all *.class files and try again.
3. If it still fails, please contact some script file expert like @HHN.

## Acknowledgements
1. The run.sh file is inspired from @Mantra7 run.sh file.
2. The Test Cases are compilation of all Public Tcs and Seniors Tcs (Mantra7) and some of my own (T1 to T8).

## Author 
1. @Kumaresan045