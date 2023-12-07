## About
1. This zip file contains Run Folder, run.sh file.
2. To run the tcs, copy the Run Folder, run.sh file to the same directory as P4.java.
3. The Test Cases can be run by executing the run.sh file. (use -h flag with run.sh to see help)
4. Cd to the directory where P4.java is there.

## Run.sh
1. To run all tcs together, do 
```$ ./run.sh -all```
2. For more details, do
```$ ./run.sh -h```

## TestCases
1. For IR Testcases, put them in IRTests folder.
2. For Custom Java Testcases, put them in JavaTests folder.
3. Make sure you give different names for Tcs in both folders.

## Working
1. For IR Testcases, the run.sh file will run the P4.java file with the IR Testcases as input and compare the output with the expected output.
2. For Custom Java Testcases, the run.sh file will run the P3.java file with the Custom Java Testcases as input and compare the output with the expected output.
3. To converting .java to .microIR, P3 assignment is packaged as jarfile and used.

## Note
1. If the run.sh doesn't run, try ```$ chmod +x run.sh``` or ```$ chmod u+x run.sh```
2. If it still fails or gives some error message, try to remove all *.class files and try again.
3. If it still fails, please contact some script file expert like @HHN.

## Acknowledgements
1. The run.sh file is inspired from @Mantra7 run.sh file.

## Author 
1. @Kumaresan045