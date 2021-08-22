# Optimal Scheduling

## Overview

In milestone1 we used a simple greedy algorithm to pick the best partial solution, however that way will not guarantee
 us an optimal solution.

In milestone2 we use the DFS branch-and-bound algorithm to search through all possible
schedules and find the one with the shortest time.
We use various way to optimize the algorithm and successfully reduced the running time
to a reasonable amount.

## The Algorithm
When we get the input, we will make a list of all the nodes that can be scheduled at the next. 
Then we add that list of nodes into a queue. Then we will keep recursively call the function until
all the complete scheduling are examined in a DFS style.

## Optimisation


###Pruning

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

- <b>Duplicate states</b>

When two nodes have the same weight, same edges and same communication cost, they are essentially can be treated as
equivalent, therefore when we consider scheduling a node, if a duplicate node has been scheduled before at the same processor,
we no longer interested in that state.

- <b>Identical state</b>

## Parallelization
The parallaized version of DFS has the same underlying algorithm for searching and pruning, the difference being that now each thread has its own copy of the schedule, and hence no backtracking is needed.
