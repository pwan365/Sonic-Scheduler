The following content is from Project1 - Planning

## Work Breakdown Structure

![Work breakdown structure](https://github.com/SoftEng306-2021/project-1-project-1-team-3/blob/master/wiki/img/WBS.png)

Develop project schedule: Establish what tasks need to be completed and at what stage of the project in order to meet the deadline. This will be done by creating a Work Breakdown Structure, Network Diagram and Gantt Chart.

Set up working environment: Relates to setting up the Github repository and general rules that team members should follow regarding the repository. Use Maven as an automation tool. (Working Environment)

GUI design, implementation and testing: The user interface will be designed and implemented using JavaFX with some related CSS elements.

UT, IT and Performance Testing: This will be done in parallel with the implementation and testing of GUI. The backend code will be tested by UT testing methods. Integration and performance tests will also be performed to test the validity and time-wise performance. 

Implementation of Parallelization: Parallel algorithm needs to be implemented on the optimal solution to improve performance. 

Implementation of Sequential: The initial and original implementation of optimal algorithms. Multithreading/multiprocessing is not involved yet. 

I/O Implementation: Parsing the input dot file and output a modified dot file.

Valid scheduling: A temporarily implemented valid solution to the problem. Does not have to be optimal.

Optimal Scheduling Algorithm: Research and implement the core algorithm to solve the problem for the client.  

Documentation: Record the processes the team goes through as the development of the project goes on.

GitHub Wiki: A wiki will be utilised in the repository to inform people on how to use it, how we progressively designed it and allow for others to collaborate.

## Project Network

![Project Network](https://github.com/SoftEng306-2021/project-1-project-1-team-3/blob/master/wiki/img/Project%20network.png)

‘Develop project schedule’ and ‘Setup working environment’ can be done concurrently as the two tasks do not interfere nor depend on each other.

After the allocation of tasks, the project is split into 5 workflows. The documentation of ‘Github wiki’ and ‘Report’ are ongoing tasks until the project is finished. Most of the tasks are split into 3 workflows: I/O implementation, Research of scheduling, and GUI design. These 3 tasks can be done at the same time as they do not depend on each other. 

The implementation of sorting requires input to be implemented and the algorithm modelled; hence it is scheduled after the two tasks. 

Unit testing requires each code section to be done, hence is scheduled after the implementation of code. GUI not only requires the code to be done but also the design of GUI, hence it is scheduled after the two tasks. The development of GUI and the testing of code do not interfere with each other, hence they are set to be done concurrently.

## Gantt Chart

![Gantt Chart](https://github.com/SoftEng306-2021/project-1-project-1-team-3/blob/master/wiki/img/Gantt.png)

The Gantt Chart is made based on the Project Network. The tasks in different branches can be done without interfering with each other; therefore, it made sense to complete multiple tasks simultaneously since the Milestone 1 due date is very close. 

As seen in the graph, we spend a lot of time on researching and modelling first. This is due to the fact that a well-designed algorithm can help us save time during actual implementation. The tasks are placed step-by-step without interfering with each other too much since we only have 6 members working on them.  The testing period takes 6 days because it includes time of finding potential faults and fixing time. Some tasks are continued across a long period of time because they need to be refined and updated throughout the time period.
