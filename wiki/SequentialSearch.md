# DFS Branch and Bound

## Initial Method

The initial method involved brainstorming a brute force solution (no integration of cost functions) whilst also investigating possible cost functions to underestimate a 
schedule's end time. The general idea was:

 - Add task from recursive call input.
 - Grab all free tasks(no parents or parents have beeen scheduled.)
 - Recursively schedule the tasks in a nested for loop, on all processors, in DFS fashion.
 - Backtrack the Schedule state at the end of the recursive call by removing the task and changing the state accordingly.
 
 ### Development of pruning
 
 Following this cost functions for pruning, using the Load Balancer and Bottom Level(Milestone 2 markdown.) Were implemented, as well as an EST Dynamic Hieuristic(selecting the child task with the shortest start time). The use of the Heuristic was to get a good solution(close to optimal) first time, rather than the naive guess 
 that resulted from recursively calling in the for loop.(The first solution would add all tasks to the first processor, likely to be a very poor estimate of the graph and thus
 not using our cost functions effectively.)
 
 ### Issues
 
 #### Time for each iteration
 Whilst we were aware that the use of pruning with cost functions would reduce our search state and thus running time, what we did not consider were the use of our data structures
 and how they would impact the time it takes for each iteration of a recursive call to run.
 
 Initially this was very slow, due to the fact that we used Task, Processor classes that were attatched to the GraphStream Node Library.
 Refactoring these classes and represeting the graph through int/int [] arrays, whilst trying to use as many fixed array types as possible oppossed to dynamic(I.e. ArrayList)
 significantly improved the runtime per iteration. 
 
 - By the end we were able to run approximately 1,000,000 states per seconds on the Flex IT image, even after implementing a hashcode for each state and calculating the Fixed Task Order(Mileston 2 Markdown.)
