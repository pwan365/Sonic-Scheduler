# Team 3 - Sonic Schedulers

## Project description
The project is about using sequential and parallel processing to solve a difficult NP scheduling problem for 'Big-As Parallel Computing Centre'.

This release is for `MILESTONE 2`

## How to run

```
java -jar scheduler.jar input.dot P [OPTION]
```
* `scheduler.jar` - the jar program name (*.jar)
* `input.dot` - the input graph dot file (*.dot)
* `P` - the number of processors (integer)

OPTIONS:
* `-v` run with the visualisation on
* `-p N` run the application with N number of cores in parallel (replace the N with an integer)
* `-o [OUTPUT]` return the output graph file in a specific name (default:[INPUT-output.dot])

Example:

The following would read the input graph named "Nodes_11_OutTree.dot",allocate each
node across 4 processors, with the GUI turn on, run the application with 6 cores in parallel
with an output file named "custom-name.dot"

```
java -jar scheduler.jar Nodes_11_OutTree.dot 4 -v -p 6 -o custom-name
```


## Requirement

For this program to run you will need the following requirements installed
```
Java (1.8)
```
and
```
maven
```
To build and generate the executable jar file, type following command under the project folder：
```
mvn clean install
```
Also the jar file has to be executable, which can be done using command 
```
chmod 777 scheduler.jar
```
The jar file should work on the FlexIT as specified.

## Members
* Samuel Chen - [Sparye](https://github.com/Sparye)
* Luxman Jeyarajah - [ljey973](https://github.com/ljey973)
* John Jia - [justcrossheaven](https://github.com/justcrossheaven)
* Kayla Kautai - [Kayla](https://github.com/kkau201)
* Jason Wang - [pwan365](https://github.com/pwan365)
* Wayne Yao - [myao209](https://github.com/myao209)

## Reference
* [GraphStream](http://graphstream-project.org/)
* [JUnit 4](https://junit.org/junit4/)
* [TopologicalSort](https://www.javatips.net/api/gs-algo-master/src/org/graphstream/algorithm/TopologicalSort.java)
