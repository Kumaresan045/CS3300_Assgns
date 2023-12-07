.SILENT :
all:
	@make -s run

clean:
	rm *.class
	rm visitor/*.class
	rm syntaxtree/*.class

test:
	spim -f P.s > P.out
	sed -i '1,5d' P.out

show:
	@make -s test
	cat P.out

run:
	javac P5.java
	java P5 < P.miniRA
