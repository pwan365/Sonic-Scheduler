package graphTests.speedTests;

import algo.IntGraph;
import algo.SequentialSearch;
import io.InputReader;
import org.graphstream.graph.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A automatic tester for custom graphs
 * @author Wayne Yao
 */
@RunWith(Parameterized.class)
public class AutomaticSpeedTester {

    Graph inputGraph;
    IntGraph intGraph;
    int numOfProcessors;
    int expected;

    /**
     * This constructor will receive argument for each test case
     * @param input inputGraph
     * @param intGraph intGraph object
     * @param processors number of Processors
     * @param expected expected answer
     */
    public AutomaticSpeedTester(Graph input, IntGraph intGraph, int processors, int expected){
        this.inputGraph = input;
        this.intGraph = intGraph;
        this.numOfProcessors = processors;
        this.expected = expected;
    }

    /**
     * Construct the parameter list for the test cases from all the custom test files
     * @return parameters in the constructors from each file
     */
    @Parameterized.Parameters
    public static Collection<Object[]> getParameters(){
        Collection<Object[]> parameters = new LinkedList<>();
        String folderLocation = "src" + File.separator +
                "test" + File.separator + "graphs";
        File folder = new File(folderLocation);

        for (final File file: folder.listFiles()){
            String fileName = file.getName();
            if(!fileName.contains("Custom")){
                continue;
            }
            InputReader inputReader = new InputReader(file.getPath());
            Graph inputGraph = inputReader.read();
            IntGraph intGraph = new IntGraph(inputGraph);
            int stringSlicer = fileName.length()-1-4;
            StringBuilder sb=new StringBuilder();
            while(('0' <= fileName.charAt(stringSlicer)) &&  ('9' >= fileName.charAt(stringSlicer))){
                sb.append("" + fileName.charAt(stringSlicer));
                stringSlicer--;
            }
            int processors = Integer.parseInt(sb.reverse().toString());
            int result = ((Double)inputGraph.getAttribute("Total schedule length")).intValue();
            parameters.add(new Object[]{inputGraph, intGraph, processors, result});

        }
        return parameters;
    }

    /**
     * Test whether the optimal scheduling time is correct or not
     */
    @Test(timeout = 1000)
    public void testing(){
        SequentialSearch s = new SequentialSearch(inputGraph, intGraph,numOfProcessors);
        s.run();
        int result = s.done();

        assertEquals(result, expected);
    }

}