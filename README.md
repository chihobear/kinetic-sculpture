# kinetic-sculpture
a kinetic sculpture simulation
Read information from a file, mmmic a two-dimension kinetic sculputure
Example input file: 
delay: 1
input: RED, BLUE, YELLOW, GREEN, PURPLE, PINK, BLACK
0: input, (20,20)
1: passthrough, (70,40)
2: passthrough, (80,100)
3: passthrough, (80, 200)
4: passthrough, (140, 100)
5: passthrough, (140, 200)
6: sink, (600,150)
0 -> 1
0 -> 2
0 -> 3
1 -> 5
2 -> 4
3 -> 4
4 -> 6
5 -> 6

0, 1, 2, 3, 4, 5, 6 represent node number. So a graph can be formed according to this file.
Example input and output(video) are showed in the project.

Algorithm and technique: graph, event-driven programming, javafx
