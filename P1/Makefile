# Makefile for compiling and running P1.y and P1.l

# Compiler
CC = gcc

# Bison and Flex
BISON = bison
FLEX = flex

# Compiler flags
CFLAGS = #-Wall

# Output executable name
OUTPUT = parser

# Name of File
NAME = P1

all: $(OUTPUT)

$(OUTPUT): $(NAME).tab.c lex.yy.c
	$(CC) $(CFLAGS) -o $(OUTPUT) $(NAME).tab.c lex.yy.c -lfl

$(NAME).tab.c: $(NAME).y
	$(BISON) -d $(NAME).y 

lex.yy.c: $(NAME).l
	$(FLEX) $(NAME).l 

clean:
	rm -f $(OUTPUT) $(NAME).tab.c $(NAME).tab.h lex.yy.c

run: $(OUTPUT)
	./$(OUTPUT) < P1.macrojava > P1.minijava

.PHONY: all clean run
