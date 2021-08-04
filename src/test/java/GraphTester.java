import algo.Processor;
import algo.Task;
import algo.ValidScheduler;
import com.sun.org.apache.xpath.internal.objects.XString;
import io.InputReader;
import io.OutputWriter;
import io.Validator;
import jdk.internal.util.xml.impl.Input;
import org.graphstream.graph.Graph;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Queue;

import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphTester {
    @Test
    public void test7N_2P() {
        String inputGraphPath = pathGetter("Nodes_7_OutTree.dot");
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = scheduleHelper(inputGraph, 2);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();

        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, 2);
        assertTrue(validator.validate());
    }

    @Test
    public void test8N_2P() {
        String inputGraphPath = pathGetter("Nodes_8_Random.dot");
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = scheduleHelper(inputGraph, 2);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();

        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, 2);
        assertTrue(validator.validate());
    }

    @Test
    public void test9N_2P() {
        String inputGraphPath = pathGetter("Nodes_9_SeriesParallel.dot");
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = scheduleHelper(inputGraph, 2);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();

        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, 2);
        assertTrue(validator.validate());
    }

    @Test
    public void test10N_2P() {
        String inputGraphPath = pathGetter("Nodes_10_Random.dot");
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = scheduleHelper(inputGraph, 2);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();

        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, 2);
        assertTrue(validator.validate());
    }

    @Test
    public void test11N_2P() {
        String inputGraphPath = pathGetter("Nodes_11_OutTree.dot");
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);

        Graph inputGraph = reader.read();
        String outputGraphFilename = scheduleHelper(inputGraph, 2);
        InputReaderHelper outputReader = new InputReaderHelper(outputGraphFilename);

        Graph outputGraph = outputReader.read();
        Validator validator = new Validator();
        validator.initialize(inputGraph, outputGraph, 2);
        assertTrue(validator.validate());
    }




    //----------------------------------helper--------------------------------------------
    private static String pathGetter(String filename) {
        String graphDir;

        graphDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "src" +
                System.getProperty("file.separator") + "test" + System.getProperty("file.separator") + "graphs" +
                System.getProperty("file.separator");
        return graphDir + filename;
    }

    private static String scheduleHelper(Graph input, int numberOfProcessors) {
        Graph copyOfInput = input;
        Processor[] processorPool = new Processor[numberOfProcessors];
        for (int i = 0; i < numberOfProcessors; i++) {
            processorPool[i] = new Processor(i + 1);
        }

        ValidScheduler v = new ValidScheduler();
        v.setProcessorList(processorPool);
        Queue<Task> taskQueue = v.topologicalorder(copyOfInput);
        v.scheduleTasks();
        Processor[] test = v.getProcessorList();
        OutputWriter writer = new OutputWriter();
        writer.write(copyOfInput,"outputTest.dot");

        return "outputTest.dot";

    }

}
