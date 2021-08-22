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

    Graph _inputGraph;
    IntGraph _intGraph;
    String _file;
    int _numOfProcessors;
    int _expected;

    public AutomaticSpeedTester(Graph input, IntGraph intGraph, String file, int processors, int expected){
        _inputGraph = input;
        _intGraph = intGraph;
        _file = file;
        _numOfProcessors = processors;
        _expected = expected;
    }

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
            parameters.add(new Object[]{inputGraph, intGraph, file.getAbsolutePath(), processors, result});

        }
        return parameters;
    }

    @Test(timeout = 1000)
    public void testing(){
        SequentialSearch s = new SequentialSearch(_inputGraph, _intGraph,_numOfProcessors);
        s.run();
        int result = s.done();

        assertEquals(result, _expected);
    }

}