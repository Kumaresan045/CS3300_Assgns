# Default make command
default: 
	@make -s build
	@make -s run

# Build the java files 
build:
	javac P3.java
	
# Generate the microIR file
run:
	java P3 < P.java > P.microIR ;

# Clean the directory
clean:
	rm *.class
	rm visitor/*.class
	rm syntaxtree/*.class