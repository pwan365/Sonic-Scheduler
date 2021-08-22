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
