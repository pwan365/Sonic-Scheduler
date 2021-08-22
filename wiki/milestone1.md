# Milestone 1

## Goal
For this milestone our goal was to design an algorithm that would produce a valid schedule, optimality was reserved for the second milestone and thus a simple greedy algorithm(List scheduling) was implemented.

### Planning
#### Project
We started with breaking up milestones into goals, then breaking goals into smaller tasks and constructed a Work Breakdown Structure diagram. 
The Project Network Diagram was then developed based on the WBS, giving each tasks priority and dependency, so a workflow could be formed for the project.
The Gantt Chart was created to set the time frame for each task.

#### Milestone 1
For milestone 1, our plan is to research and develop a valid scheduler while implementing the I/O. The input will be implemented first before the valid scheduler to allow the scheduler to work with real, read data from input file. The valid scheduling would be developed concurrently with the output, and the testing could start as soon as the valid scheduler could output a scheduler to aid the develepment of the scheduler to produce a valid result.

### Outcomes
- Read the input file and convert this to a `Graph` using graphstream.
- Apply a topological sort on the graph to get an order of nodes to be scheduled that respect dependencies between tasks.
- Implementation of the List scheduler.
- Creating an output file with the schedule produced by the algorithm.

### UML diagram
![uml diagram](https://github.com/SoftEng306-2021/project-1-project-1-team-3/blob/master/wiki/img/UML%20diagram.png)

### I/O
The InputReader.java is implemented to achieve these goals:
- Read the arguments from the command line from Main.java. 
- Read the information from the input dot file and convert it to an acceptable data structure for valid scheduling algorithm to run.
- Preserve the graph name and attributes.

The OutputReader.java is implemented to achieve these goals:
- Write the scheduled data to a formatted dot file which includes multiple attributes that required.

To achieve these goals, we used GraphStream to read from and write to a dot file. Specifically, we also created a file named GraphWriter that extends the FileSinkDOT class and overrides multiple methods to fit our requirements. The I/O related classes are included in io package and designed not to effect the scheduling.




### Valid Solution
1. Topological sort the graph,(Uses a graphstream library although some methods had to be override.)
- Overrides methods in Graphstream.Algorithm and implemented a new DFS topological ordering algorithm to generate a topologically ordered task queue and support valid scheduling.
2. Use of an array to store the Processors.
3. while queue ≠ empty, we pop the first element and iterate over every Processor finding the earliest time this task can be scheduled.
- Involves calculating the communication cost between a node and its parents.
- Every task has a list of parent `Edges`, as such we can retrieve the node of the parent through the `Edge`.
- Every task has an allocated processor and its finishing time, as such we can get the weight of the edge and the parent's finishing time. This addition will be the communication cost for the child task if it is scheduled on a processor that is different to its parents.
- We only need to know the maximum of all communication costs involving a child node and its parent to know its earliest time to be scheduled on a processor, thus we declare an integer to keep track of this.
4. All nodes have their processor, starting time, duration time and finishing time and so we can iterate through the nodes to output the results.

### Future Tasks
- Whilst a lot of the code used for a valid schedule is going to remain, i.e. Communication costs, using a Topological Order and a Queue for this order will not be sufficient to obtain an optimal solution. We must iterate over all possible orders that tasks can be scheduled.
- The parallelization of the code has yet to be implemented, as the current scheduling is non-iterative, the same goes for the visualization of the interative searching.

---
### Validity Testing
- Used example graphs provided by the client to initiate the testing. And we then added more boundary and extreme cases on our own.
- We created a Validator class to validate all the constraints of the output graph.
- We used `Junit` as the testing framework.


### Constrains for validity
- The main constrains are:
    1. Parents have to be scheduled before children can be scheduled
    2. After a task is scheduled in a processor, no other tasks can go before that task in that processor
    3. In the same processor no two tasks can be overlapping with each other
- The validator is designed to test these constrains

### Validator
- Validator consists of two main methods and some helper methods. First the tester can call the `initialize()` method, which initilized the fields.
- Then the tester call the `validate()`  which calls all the helper methods to test if the scheduling violates the constrains
- If it returns true, then it means the scheduling is valid. If it returns false, then it means the scheudling has fault and the console should print out the error message
* A lot of debugging went into the validator since it is quite a complicated class

#### Issues
- found it difficult to implement the validator class due to unfamiliarity with the `GraphStream` library.

