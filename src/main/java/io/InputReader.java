package io;
import java.io.*;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;

/**
 * The InputReader class is used to read in the data of a graph.
 *
 * @author John Jia, Jason Wang
 */
public class InputReader {

    private final String filePath;

    /**
     * The constructor of InputReader.
     * @param input_file the file name of the graph file to be read.
     */
    public InputReader(String input_file){
        String dir = System.getProperty("user.dir");

        //Construct full path name to the file.
        this.filePath = dir + File.separator + input_file;
    }

    /**
     * Using this read() method will return a Graph object that represents the file read.
     * @return Graph object of the file read.
     */
    public Graph read() {
        String line;
        String graphName = "";
        //Extracting the title of the graph.
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null ){
                if (line.contains("digraph")){
                    graphName = line.split("\"")[1];
                    break;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Construct a graph with the read graph name
        Graph g = new DefaultGraph(graphName);
        FileSource fs = new FileSourceDOT();
        fs.addSink(g);
        try {
            fs.readAll(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fs.removeSink(g);
        }
        return g;
    }
}
