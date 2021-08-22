package graphTests.validateTests;

import algo.IntGraph;
import algo.SequentialSearch;
import io.OutputWriter;
import org.graphstream.graph.Graph;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This is the JUnit test suite for the validation of the scheduler
 *
 * @author Samuel Chen, Wayne Yao
 */
public class GraphTester {
    //If wanting to see certain output.dot file for following test, comment out removeOutput function
    // --------------------------------   client test cases-----------------------------

    /**
     * Test the 7 nodes graph provided by the client with two processors
     */
    @Test
    public void test7N_2P() {
        String inputFileName ="Nodes_7_OutTree.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        //generate a output graph
        String outputGraphFilename = outputGenerator(inputGraph, inputFileName, numProc);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();

        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, numProc);
        assertTrue(validator.validate());

        //delete generated output file
        removeOutput(outputGraphFilename);
    }


    /**
     * Test the 7 nodes graph provided by the client with four processors
     */
    @Test
    public void test7N_4P() {
        String inputFileName ="Nodes_7_OutTree.dot";
        int numProc = 4;
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = outputGenerator(inputGraph, inputFileName, numProc);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();

        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, 4);
        assertTrue(validator.validate());
        removeOutput(outputGraphFilename);
    }



    /**
     * Test the 8 nodes graph provided by the client with two processors
     */
    @Test
    public void test8N_2P() {
        String inputFileName ="Nodes_8_Random.dot";
        int numProc = 2;
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = outputGenerator(inputGraph, inputFileName, numProc);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();

        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, numProc);
        assertTrue(validator.validate());
        removeOutput(outputGraphFilename);
    }


    /**
     * Test the 8 nodes graph provided by the client with four processors
     */
    @Test
    public void test8N_4P() {
        String inputFileName ="Nodes_8_Random.dot";
        int numProc = 4;
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = outputGenerator(inputGraph, inputFileName, numProc);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();

        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, numProc);
        assertTrue(validator.validate());
        removeOutput(outputGraphFilename);
    }


    /**
     * Test the 9 nodes graph provided by the client with two processors
     */
    @Test
    public void test9N_2P() {
        String inputFileName ="Nodes_9_SeriesParallel.dot";
        int numProc = 2;
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = outputGenerator(inputGraph, inputFileName, numProc);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();

        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, numProc);
        assertTrue(validator.validate());
        removeOutput(outputGraphFilename);
    }

    /**
     * Test the 9 nodes graph provided by the client with four processors
     */
    @Test
    public void test9N_4P() {
        String inputFileName ="Nodes_9_SeriesParallel.dot";
        int numProc = 4;
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = outputGenerator(inputGraph, inputFileName, numProc);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();

        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, numProc);
        assertTrue(validator.validate());
        removeOutput(outputGraphFilename);
    }

    /**
     * Test the 10 nodes graph provided by the client with two processors
     */
    @Test
    public void test10N_2P() {
        String inputFileName ="Nodes_10_Random.dot";
        int numProc = 2;
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = outputGenerator(inputGraph, inputFileName, numProc);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();

        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, numProc);
        assertTrue(validator.validate());
        removeOutput(outputGraphFilename);
    }

    /**
     * Test the 10 nodes graph provided by the client with four processors
     */
    @Test
    public void test10N_4P() {
        String inputFileName ="Nodes_10_Random.dot";
        int numProc = 4;
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = outputGenerator(inputGraph, inputFileName, numProc);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();

        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, numProc);
        assertTrue(validator.validate());
        removeOutput(outputGraphFilename);
    }

    /**
     * Test the 11 nodes graph provided by the client with two processors
     */
    @Test
    public void test11N_2P() {
        String inputFileName ="Nodes_11_OutTree-output.dot";
        int numProc = 2;
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = outputGenerator(inputGraph, inputFileName, numProc);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();
        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, numProc);
        assertTrue(validator.validate());
        removeOutput(outputGraphFilename);
    }

    /**
     * Test the 11 nodes graph provided by the client with four processors
     */
    @Test
    public void test11N_4P() {
        String inputFileName ="Nodes_11_OutTree-output.dot";
        int numProc = 4;
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = outputGenerator(inputGraph, inputFileName, numProc);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();
        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, numProc);
        assertTrue(validator.validate());
        removeOutput(outputGraphFilename);
    }

    //-------------------------------customer test case------------------------------------

    /**
     * Test a one node graph with four processors
     */
    @Test
    public void test1N_4P() {
        String inputFileName ="1Node.dot";
        int numProc = 4;
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = outputGenerator(inputGraph, inputFileName, numProc);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();
        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, numProc);
        assertTrue(validator.validate());
        removeOutput(outputGraphFilename);
    }

    /**
     * Test a one node graph with two processors
     */
    @Test
    public void test1N_2P() {
        String inputFileName ="1Node.dot";
        int numProc = 2;
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = outputGenerator(inputGraph, inputFileName, numProc);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();
        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, numProc);
        assertTrue(validator.validate());
        removeOutput(outputGraphFilename);
    }

    /**
     * Test an empty graph with two processors
     */
    @Test
    public void test0N_2P() {
        String inputFileName ="EmptyGraph.dot";
        int numProc = 2;
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = outputGenerator(inputGraph, inputFileName, numProc);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();
        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, numProc);
        assertTrue(validator.validate());
        removeOutput(outputGraphFilename);
    }

    /**
     * Test an empty graph with four processors
     */
    @Test
    public void test0N_4P() {
        String inputFileName ="EmptyGraph.dot";
        int numProc = 4;
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = outputGenerator(inputGraph, inputFileName, numProc);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();
        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, numProc);
        assertTrue(validator.validate());
        removeOutput(outputGraphFilename);
    }

    /**
     *Test a graph with 5 nodes that contains maximal amount of edges with 2 processors
     */
    @Test
    public void test5N_MaxEdge_2P() {
        String inputFileName ="5NodesMaximalEdges.dot";
        int numProc = 2;
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = outputGenerator(inputGraph, inputFileName, numProc);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();
        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, numProc);
        assertTrue(validator.validate());
        removeOutput(outputGraphFilename);
    }

    /**
     *Test a graph with 5 nodes that contains maximal amount of edges with 4 processors
     */
    @Test
    public void test5N_MaxEdge_4P() {
        String inputFileName ="5NodesMaximalEdges.dot";
        int numProc = 4;
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = outputGenerator(inputGraph, inputFileName, numProc);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();
        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, numProc);
        assertTrue(validator.validate());
        removeOutput(outputGraphFilename);
    }

    /**
     *  Test a graph where each child other than root has one and only one parent with two processors
     */
    @Test
    public void test6N_NoBranch_2P() {

        String inputFileName = "6NodesNoBranch.dot";
        int numProc = 2;
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        String outputGraphFilename = outputGenerator(inputGraph, inputFileName, numProc);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();
        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, numProc);
        assertTrue(validator.validate());
        removeOutput(outputGraphFilename);
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

    /**
     * The method takes the input Graph and generate a output graph with processor assigned to each task
     * @param input
     * @param inputFileName
     * @param numberOfProcessors
     * @return a string of the name of the output file
     */
    private static String outputGenerator(Graph input, String inputFileName, int numberOfProcessors) {
        IntGraph graph = new IntGraph(input);

        SequentialSearch s = new SequentialSearch(input, graph,numberOfProcessors);
        s.run();
        s.done();
        OutputWriter writer = new OutputWriter();
        writer.write(input,inputFileName.replace(".dot", "-output.dot"));

        return inputFileName.replace(".dot", "-output.dot");

    }

    /**
     * Remove testing output files
     * @param outputFileName
     */
    private void removeOutput(String outputFileName) {
        String filename = System.getProperty("user.dir") + System.getProperty("file.separator") + outputFileName;
        try{
            File file = new File(filename);
            if (!file.delete()){
                System.gc();
                file.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
