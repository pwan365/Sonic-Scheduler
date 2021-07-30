# Milestone 1

## Goal
For this milestone our implementation needs to be able to:
•Read the input file and number of processors and create an output file
•The output file needs to contain avalidschedule (non-trivial, but does not need to be optimalat this point)

## Planning

### UML diagram
to be inserted later

### Valid Solution
1. I/O (A graphStream object will be return)
2. Topological sort the graph (Might be a default library call to sort it automatically. However need to look into more since there might be multiple orders)
3. A way to queue them in a data structure, e.g. queue
4. Array list of processors
5. while queue ≠ empty, we pop the first element then find the most efficient scheduling (earliest scheduling time) among all processors. 
6. Write to output


