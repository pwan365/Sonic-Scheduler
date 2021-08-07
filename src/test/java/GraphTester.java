import algo.Processor;
import algo.ValidScheduler;
import io.OutputWriter;
import org.graphstream.graph.Graph;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphTester {
    //If wanting to see certain output.dot file for following test, comment out removeOutput function
    // --------------------------------   client test cases-----------------------------
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

    @Test
    public void test11N_2P() {
        String inputFileName ="Nodes_11_OutTree.dot";
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

    @Test
    public void test11N_4P() {
        String inputFileName ="Nodes_11_OutTree.dot";
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


    @Test
    public void test6N_NoBranch_2P() {

        String inputFileName = "5NodesMaximalEdges.dot";
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
    private static String pathGetter(String filename) {
        String graphDir;

        graphDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "src" +
                System.getProperty("file.separator") + "test" + System.getProperty("file.separator") + "graphs" +
                System.getProperty("file.separator");
        return graphDir + filename;
    }

    private static String outputGenerator(Graph input, String inputFileName, int numberOfProcessors) {
        Graph copyOfInput = input;

        Processor[] processorPool = new Processor[numberOfProcessors];
        for (int i = 0; i < numberOfProcessors; i++) {
            processorPool[i] = new Processor(i + 1);
        }

        ValidScheduler v = new ValidScheduler();
        v.setProcessorList(processorPool);
        v.topologicalorder(copyOfInput);
        v.scheduleTasks();
        Processor[] test = v.getProcessorList();
        OutputWriter writer = new OutputWriter();
        writer.write(copyOfInput,inputFileName.replace(".dot", "-output.dot"));

        return inputFileName.replace(".dot", "-output.dot");

    }

    private void removeOutput(String outputFileName) {
        new File(outputFileName).delete();
    }

}
