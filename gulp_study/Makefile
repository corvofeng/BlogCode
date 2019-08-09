
CC := gcc
EXE := main

all : main.o
	${CC} $^ -o ${EXE}

main.o: main.c
	${CC} -c $^

.PHONEY:clean
clean:
	rm -vf ${EXE} main.o

