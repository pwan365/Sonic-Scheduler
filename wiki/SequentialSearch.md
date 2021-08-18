# DFS Branch and Bound

The algorithm used to generate the optimal solution is a DFS Branch and Bound backtracking algorithm. This algorithm uses only 1 schedule to search through its states,
thus at the end of a function call we must backtrack and remove the designated task from the schedule. An if condition is used to see whether the schedule is better than the
current best schedule(Initially set to an INTEGER.MAX Time.) If it is then we deep copy(More on the Deep Copy Section.) the necessary details to the best schedule.

## Pruning Techniques

### Cost Functions

#### Load Balancer

#### Critical Path

## Heuristic

## Other methods to reduce time

### Deep Copying

### Representing time as a Stack

## Psuedocode
