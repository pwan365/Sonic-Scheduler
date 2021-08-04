## What's been done

Optimal solution classes: node, (tasks), processes

main focus: cost function

I/O has been done, a tree model is constructed automatically

## Git setup

John push up tasks

## Break down tasks

Creating UML class diagrams first

## Valid solution (Greedy Algorithm, choose the shortest possible time at each level)

1. I/O (A graphStream object will be return)
2. Topological sort the graph (Might be a default library call to sort it automatically. However need to look into more since there might be multiple orders)
3. A way to queue them in a data structure, e.g. queue
4. Array list of processors
5. while queue ≠ empty, we pop the first element then find the most efficient scheduling (earliest scheduling time) among all processors. 
6. Write to output

## Next meeting

Saturday 1PM discord