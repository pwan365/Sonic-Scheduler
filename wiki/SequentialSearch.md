# DFS Branch and Bound

The algorithm used to generate the optimal solution is a DFS Branch and Bound backtracking algorithm. This algorithm uses only 1 schedule to search through its states,
thus at the end of a function call we must backtrack and remove the designated task from the schedule. An if condition is used to see whether the schedule is better than the
current best schedule(Initially set to an INTEGER.MAX Time.) If it is then we deep copy(More on the Deep Copy Section.) the necessary details to the best schedule.

## Pruning Techniques

### Cost Functions

#### Load Balancer
The class `Schedule` contains an idle cost field which increments everytime a task is schduled, with its associated communication cost(as well as decremented whenever we backtrack
after scheduling a task.) As such we can calculate an underestimate for the time finished through ceil(Weight of all tasks + current communication cost + communication cost to schedule upcoming taks)/Number of processors. This involves a few numerical calculations, independent from the input and thus is a constant time operation.

#### Bottom Level
The Bottom level for each node(longest path) is calculated before scheduling. Thus when we are in the process of scheduling a task in the processor we add the communication cost + the bottom level for the task, as well as the latest time on the processor already. This is a guaranteed underestimate of the cost, thus we can prune a state if this calculation is larger than the time of the best schedule.
## Heuristic

## Other methods to reduce time

### Deep Copying

### Representing time as a Stack

## Psuedocode
