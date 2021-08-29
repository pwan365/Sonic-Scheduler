package graphTests.speedTests;

import algo.IntGraph;
import algo.ParallelSearch;
import algo.SequentialSearch;
import graphTests.validateTests.InputReaderHelper;
import org.graphstream.graph.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpeedTester {


    @Test
    public void test7N_2P() {
        String inputFileName = "Nodes_7_OutTree.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 28);
    }

//    @Test
    public void test16_4() {
        String inputFileName = "Join_Nodes_16_CCR_10.01_WeightType_Random_Homogeneous-6.gxl.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, 8);


        assertEquals(best, 34);
    }

    @Test
    public void test7N_4P() {
        String inputFileName = "Nodes_7_OutTree.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 22);
    }

    @Test
    public void test8N_2P() {
        String inputFileName = "Nodes_8_Random.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 581);
    }

    @Test
    public void test8N_4P() {
        String inputFileName = "Nodes_8_Random.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 581);
    }

    @Test
    public void test9N_2P() {
        String inputFileName = "Nodes_9_SeriesParallel.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 55);
    }

    @Test
    public void test9N_4P() {
        String inputFileName = "Nodes_9_SeriesParallel.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 55);
    }

    @Test
    public void test10N_2P() {
        String inputFileName = "Nodes_10_Random.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 50);
    }

    @Test
    public void test10N_4P() {
        String inputFileName = "Nodes_10_Random.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 50);
    }

    @Test
    public void test11N_2P() {
        String inputFileName = "Nodes_11_OutTree.dot";
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
        String inputFileName = "Nodes_11_OutTree.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 227);
    }

    @Test
    public void customTest10N_2P() {
        String inputFileName = "N10-2P.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 56);
    }

    @Test
    public void customTest16N_4P() {
        String inputFileName = "N16-02-4P.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 51);
    }

    @Test
    public void customTest10N_4P() {
        String inputFileName = "N10-4P.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 51);
    }

    @Test
    public void customTest10N_6P() {
        String inputFileName = "N10-4P.dot";
        int numProc = 6;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 51);
    }

    @Test
    public void test16N_2P() {
        String inputFileName = "N16-01.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 76);
    }

    @Test
    public void test16N_4P() {
        String inputFileName = "N16-01.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 76);
    }

    @Test
    public void test16N_02_4P() {
        String inputFileName = "N16-02-4P.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 51);
    }

    @Test
    public void test16N_03_4P() {
        String inputFileName = "N16-03-4P.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 69);
    }

    @Test
    public void test16_1() {
        String inputFileName = "Join_Nodes_16_CCR_10.00_WeightType_Random#1_Homogeneous-2.gxl.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 63);
    }
    @Test
    public void test16_2() {
        String inputFileName = "Join_Nodes_16_CCR_10.00_WeightType_Random#1_Homogeneous-4.gxl.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 56);
    }

    @Test
    public void test16_3() {
        String inputFileName = "Join_Nodes_16_CCR_10.00_WeightType_Random#4_Homogeneous-2.gxl.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 69);
    }

//    @Test
    public void test16_4_1() {
        String inputFileName = "Join_Nodes_16_CCR_10.01_WeightType_Random_Homogeneous-6.gxl.dot";
        int numProc = 6;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 67);
    }

    @Test
    public void test16_4_5() {
        String inputFileName = "Random_Nodes_16_Density_4.81_CCR_10.10_WeightType_Random_Homogeneous-4.gxl.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 92);
    }

    @Test
    public void test16_4_6() {
        String inputFileName = "Random_Nodes_16_Density_1.38_CCR_0.10_WeightType_Random_Homogeneous-4.gxl.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 520);
    }





//    @Test
    public void test21_1() {
        String inputFileName = "Join_Nodes_21_CCR_0.10_WeightType_Random#3_Homogeneous-2.gxl.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 589);
    }

//    @Test
    public void test21_2() {
        String inputFileName = "Join_Nodes_21_CCR_0.10_WeightType_Random#8_Homogeneous-6.gxl.dot";
        int numProc = 6;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 239);
    }

    @Test
    public void test10_1() {
        String inputFileName = "OutTree-Balanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random#7_Homogeneous-2.gxl.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 376);
    }

    @Test
    public void test10_2() {
        String inputFileName = "OutTree-Balanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random_Homogeneous-6.gxl.dot";
        int numProc = 6;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 242);
    }

    @Test
    public void test10_3() {
        String inputFileName = "Random_Nodes_10_Density_1.70_CCR_0.10_WeightType_Random#1_Homogeneous-6.gxl.dot";
        int numProc = 6;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 547);
    }

    @Test
    public void test10_4() {
        String inputFileName = "Random_Nodes_10_Density_2.00_CCR_10.00_WeightType_Random_Homogeneous-6.gxl.dot";
        int numProc = 6;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 46);
    }

    @Test
    public void test10_5() {
        String inputFileName = "Random_Nodes_10_Density_2.20_CCR_10.00_WeightType_Random_Homogeneous-6.gxl.dot";
        int numProc = 6;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 46);
    }

    @Test
    public void test10_6() {
        String inputFileName = "Random_Nodes_10_Density_4.50_CCR_9.97_WeightType_Random_Homogeneous-4.gxl.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 67);
    }

    @Test
    public void test10_7() {
        String inputFileName = "Random_Nodes_10_Density_0.20_CCR_1.00_WeightType_Random_GB_Homogeneous-2.gxl.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 26);
    }

    @Test
    public void test10_8() {
        String inputFileName = "Random_Nodes_10_Density_0.20_CCR_1.00_WeightType_Random_GB_Homogeneous-4.gxl.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 17);
    }

    @Test
    public void test10_9() {
        String inputFileName = "Random_Nodes_10_Density_0.20_CCR_1.00_WeightType_Random_GB_Homogeneous-8.gxl.dot";
        int numProc = 8;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 17);
    }

    @Test
    public void test10_10() {
        String inputFileName = "Random_Nodes_10_Density_0.20_CCR_1.00_WeightType_Random_GB_Homogeneous-16.gxl.dot";
        int numProc = 16;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 17);
    }

    @Test
    public void test10_11() {
        String inputFileName = "Random_Nodes_10_Density_0.40_CCR_10.00_WeightType_Random_GB_Homogeneous-2.gxl.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 31);
    }

    @Test
    public void test10_12() {
        String inputFileName = "Random_Nodes_10_Density_0.40_CCR_10.00_WeightType_Random_GB_Homogeneous-4.gxl.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 17);
    }

    @Test
    public void test10_13() {
        String inputFileName = "Random_Nodes_10_Density_0.40_CCR_10.00_WeightType_Random_GB_Homogeneous-8.gxl.dot";
        int numProc = 8;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 17);
    }

    @Test
    public void test10_14() {
        String inputFileName = "Random_Nodes_10_Density_0.40_CCR_10.00_WeightType_Random_GB_Homogeneous-16.gxl.dot";
        int numProc = 16;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 17);
    }

    @Test
    public void test10_15() {
        String inputFileName = "Random_Nodes_10_Density_0.40_CCR_10.02_WeightType_Random_GB_Homogeneous-2.gxl.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 32);
    }

    @Test
    public void test10_16() {
        String inputFileName = "Random_Nodes_10_Density_0.40_CCR_10.02_WeightType_Random_GB_Homogeneous-4.gxl.dot";
        int numProc = 4;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 18);
    }

    @Test
    public void test21_3() {
        String inputFileName = "Random_Nodes_21_Density_4.81_CCR_0.99_WeightType_Random_Homogeneous-2.gxl.dot";
        int numProc = 2;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 403);
    }

    @Test
    public void test21_4() {
        String inputFileName = "Random_Nodes_21_Density_4.62_CCR_0.10_WeightType_Random_Homogeneous-6.gxl.dot";
        int numProc = 6;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 3567);
    }

    @Test
    public void test21_5() {
        String inputFileName = "Random_Nodes_21_Density_2.48_CCR_0.10_WeightType_Random_Homogeneous-6.gxl.dot";
        int numProc = 6;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 1102);
    }

//    @Test
    public void test21_6() {
        String inputFileName = "Random_Nodes_21_Density_1.76_CCR_0.10_WeightType_Random_Homogeneous-6.gxl.dot";
        int numProc = 6;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 573);
    }

    @Test
    public void test21_7() {
        String inputFileName = "Random_Nodes_21_Density_2.00_CCR_1.00_WeightType_Random_Homogeneous-6.gxl.dot";
        int numProc = 6;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 83);
    }

//    @Test
    public void test21_8() {
        String inputFileName = "Random_Nodes_21_Density_2.29_CCR_0.10_WeightType_Random_Homogeneous-6.gxl.dot";
        int numProc = 6;
        //read the graph using the absolute path
        String inputGraphPath = pathGetter(inputFileName);
        InputReaderHelper reader = new InputReaderHelper(inputGraphPath);
        Graph inputGraph = reader.read();

        int best = this.scheduling(inputGraph, numProc);

        assertEquals(best, 1001);
    }

    //----------------------------------helper--------------------------------------------

    /**
     * The method will get the full path for the filename pass into the method
     *
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
     * A helper method to scheduling and return the best time, reducing duplicating code
     * @param input input graph
     * @param numberOfProcessors number of processors
     * @return the best time
     */
    private int scheduling(Graph input, int numberOfProcessors) {
        IntGraph graph = new IntGraph(input);
        ParallelSearch s = new ParallelSearch(input, graph,numberOfProcessors,2);
        s.run();
        return s.done();
    }
}
