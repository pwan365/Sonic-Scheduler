package graphTests.speedTests;

import algo.CostFunctions.CriticalPath;
import algo.CostFunctions.LoadBalancer;
import algo.Schedule.Task;
import algo.Solution.AllOrders;
import algo.Solution.DuplicateStart;
import algo.Solution.SequentialSearch;
import algo.Solution.ValidScheduler;
import graphTests.validateTests.InputReaderHelper;
import graphTests.validateTests.Validator;
import io.OutputWriter;
import org.graphstream.graph.Graph;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpeedTester {


    @Test
    public void test7N_2P() {
        String inputFileName ="Nodes_7_OutTree.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);


        assertTrue(best == 28);
    }

    @Test
    public void test7N_4P() {
        String inputFileName ="Nodes_7_OutTree.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertTrue(best == 22);
    }

    @Test
    public void test8N_2P() {
        String inputFileName ="Nodes_8_Random.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertTrue(best == 581);
    }

    @Test
    public void test8N_4P() {
        String inputFileName ="Nodes_8_Random.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertTrue(best == 581);
    }

    @Test
    public void test9N_2P() {
        String inputFileName ="Nodes_9_SeriesParallel.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertTrue(best == 55);
    }

    @Test
    public void test9N_4P() {
        String inputFileName ="Nodes_9_SeriesParallel.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertTrue(best == 55);
    }

    @Test
    public void test10N_2P() {
        String inputFileName ="Nodes_10_Random.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertTrue(best == 50);
    }

    @Test
    public void test10N_4P() {
        String inputFileName ="Nodes_10_Random.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertTrue(best == 50);
    }

    @Test
    public void test11N_2P() {
        String inputFileName ="Nodes_11_OutTree.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertTrue(best == 350);
    }

    @Test
    public void test11N_4P() {
        String inputFileName ="Nodes_11_OutTree.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertTrue(best == 227);
    }

    @AfterEach
    public void clear(){
        CriticalPath.clearObject();
        LoadBalancer.clearObject();
        DuplicateStart.clearObject();
    }




    //----------------------------------helper--------------------------------------------

    /**
     * The method will get the full path for the filename pass into the method
     * @param filename
     * @return full path of file
     */
    private static String pathGetter(String filename) {
        String graphDir;

        graphDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "src" +
                System.getProperty("file.separator") + "test" + System.getProperty("file.separator") + "graphs" +
                System.getProperty("file.separator");
        return graphDir + filename;
    }

    private int scheduling(Graph input, int numberOfProcessors) {
        ValidScheduler v = new ValidScheduler(1);
        v.topologicalorder(input);

        SequentialSearch d = new SequentialSearch(numberOfProcessors,input);
        AllOrders a = AllOrders.init(input);

        HashSet<Task> empty = new HashSet<>();
        ArrayList<Task> tasks = a.getOrder(empty);
        for (Task task : tasks) {
            for(int i=0; i<numberOfProcessors;i++) {
                d.branchBound(task,i,0);
            }
        }
        int best = d.getBestSchedule();
        return best;
    }

}
