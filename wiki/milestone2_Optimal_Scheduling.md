# Optimal Scheduling

## Overview

In milestone1 we used a simple greedy algorithm to pick the best partial solution, however that way will not guarantee
 us an optimal solution.

In milestone2 we use the DFS branch-and-bound algorithm to search through all possible
schedules and find the one with the shortest time.
We use various way to optimize the algorithm and successfully reduced the running time
to a reasonable amount.

## The Algorithm

### DFS Branch and Bound
The algorithm used to generate the optimal solution is a DFS Branch and Bound backtracking algorithm. This algorithm uses only 1 schedule to search through its states, thus at the end of a function call we must backtrack and remove the designated task from the schedule. An if condition is used to see whether the schedule is better than the current best schedule(Initially set to an INTEGER.MAX Time.) If it is then we deep copy(More on the Deep Copy Section.) the necessary details to the best schedule

## Optimisation

### Heuristic

Dynamic List Scheduling
https://www.semanticscholar.org/paper/Static-vs.-Dynamic-List-Scheduling-Performance-Hagras-Janecek/7c4ca98ca39e20a3c17535b6f53278f9d60162c0

As per the above study, dynamic heuristics outperform static ones. As such we have chosen to use DLS(Dynamic List Scheduling) which explores potential candidate nodes in order of their Bottom Level subtracted from their earliest start time on a given processor, in non-increasing order.

### Pruning

- <b>Fixed Task Order</b>

Idea reference: https://www.sciencedirect.com/science/article/abs/pii/S0305054813002542

According to the report, to solve the fork or join graphs the best way is to use a Fixed Task Order,
where candidate tasks satisfying the following requirement:
   - all the tasks have either no child or has exactly one same child
   - all the tasks have either no parent, or the exactly one parent is scheduled on the same processor
   - The tasks are sorted in non-decreasing data ready time (finish time + communication cost of parent)
   - The tasks are also sorted in non-increasing child edge communication cost.
   
This is extremely helpful for the fork and join graph because once we get the FTO, we can reduce
the amount of task ordering and hence pruning states.
FTO is checked every recursive call due to the reason candidate tasks are constantly changing.

- <b>Duplicate Nodes</b>

When two nodes have the same weight, same edges and same communication cost, they are essentially can be treated as
equivalent, therefore when we consider scheduling a node, if a duplicate node has been scheduled before at the same processor,
we no longer interested in that state.

- <b>Identical state</b>

Identical state is when exactly the same tasks that are scheduled at the same time allocation but on the 
same/different processors. Hence we can ignore them if we detect this sort of state happens.
We use a hash set of stacks to represent different processors, this is because hash set is a non-ordered data strcture
with constant operations. Then, we store hashcode of this hash set into another "seenState" hash set, so that whenever
a duplicate schedule happens we can prune everything from that point.

#### Cost Functions

- <b>Load Balancer</b>
The class Schedule contains an idle cost field which increments everytime a task is schduled, with its associated communication cost(as well as decremented whenever we backtrack after scheduling a task.) As such we can calculate an underestimate for the time finished through ceil(Weight of all tasks + current communication cost + communication cost to schedule upcoming taks)/Number of processors. This involves a few numerical calculations, independent from the input and thus is a constant time operation.

- <b>Bottom Level</b>
Bottom Level for each node(the longest path) is calculated before scheduling. Thus when we are in the process of scheduling a task in the processor we add the communication cost + the bottom level for the task, as well as the latest time on the processor already. This is a guaranteed underestimate of the cost, thus we can prune a state if this calculation is larger than the time of the best schedule.

## Parallelization
The parallaized version of DFS has the same underlying algorithm for searching and pruning, the difference being that now each thread has its own copy of the schedule, and hence no backtracking is needed.
