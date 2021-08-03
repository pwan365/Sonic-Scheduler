# Milestone 1

## Goal
For this milestone our goal was to design an algorithm that would produce a valid schedule, optimality was reserved for the second milestone and thus a simple greedy algorithm(List scheduling) was implemented.

### Planning
### Outcomes
• Read the input file and convert this to a `Graph` using graphstream.
• Apply a topological sort on the graph to get an order of nodes to be scheduled that respect dependencies between tasks.
• Implementation of the List scheduler.
• Creating an output file with the schedule produced by the algorithm.

### UML diagram
to be inserted later

### Valid Solution
1. Topological sort the graph,(Uses a graphstream library although some methods had to be override.)
    •
2. Use of an array to store the Processors.
3. while queue ≠ empty, we pop the first element and iterate over every Processor finding the earliest time this task can be scheduled.
    • Involves calculating the communication cost between a node and its parents.
    • Every task has a list of parent `Edges`, as such we can retrieve the node of the parent through thhe `Edge`.
    • Every task has an allocated processor and its finishing time, as such we can get the weight of the edge and the parent's finishing time. This addition will be the 
      communication cost for the child task if it is scheduled on a processor that is different to its parents.
    • We only need to know the maximum of all communication costs involving a child node and its parent to know its earliest time to be scheduled on a processor, thus we 
      declare an integer to keep track of this.
4. All nodes have their processor, starting time, duration time and finishing time and so we can iterate through the nodes to output the results.

### Future Issues
• Whilst alot of the code used for a valid schedule is going to remain, i.e. Communication costs, using a Topological Order and a Queue for this order will not be sufficient to
  obtain an optimal solution. We must iterate over all possible orders that tasks can be scheduled.



