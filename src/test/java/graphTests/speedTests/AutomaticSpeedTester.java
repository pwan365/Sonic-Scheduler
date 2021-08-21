package graphTests.speedTests;

import algo.Solution.IntGraph;
import algo.Solution.SequentialSearch;
import io.InputReader;
import org.graphstream.graph.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Parallelized.class)
public class AutomaticSpeedTester {

    IntGraph _graph;
    String _file;
    int _numOfProcessors;
    int _expected;


    @Parameterized.Parameters
    public static Collection<Object[]> getParameters(){
        Collection<Object[]> parameters = new LinkedList<>();
        String folderLocation = "src" + File.separator +
                "test" + File.separator + "graphs";
        File folder = new File(folderLocation);

        for (final File file: folder.listFiles()){
            if(file.getName().charAt(0) != 'N'){
                continue;
            }
            InputReader inputReader = new InputReader(file.getPath());
            Graph inputGraph = inputReader.read();
            IntGraph intGraph = new IntGraph(inputGraph);

            int processors = 2;

            int result = ((Double)inputGraph.getAttribute("Total schedule length")).intValue();


            parameters.add(new Object[]{intGraph, file.getAbsolutePath(), processors, result});

        }
        return parameters;
    }

    public AutomaticSpeedTester(IntGraph intGraph, String file, int processors, int expected){
        _graph = intGraph;
        _file = file;
        _numOfProcessors = processors;
        _expected = expected;
    }

    @Test(timeout = 10000)
    public void testing(){
        System.out.println(_file);
        SequentialSearch s = new SequentialSearch(_graph,_numOfProcessors);
        s.run();
        int result = s.done();

        assertEquals(result, _expected);
    }

}