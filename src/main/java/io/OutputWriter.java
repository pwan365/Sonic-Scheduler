package io;

import org.graphstream.graph.Graph;

import java.io.File;
import java.io.IOException;

/**
 * OutputWriter class is used to create an output .dot file that represents a scheduled digraph.
 *
 * @author Jason Wang
 */
public class OutputWriter {

    /**
     * Method to write a graph to a file with specified file name.
     * @param g graph to be outputted.
     * @param fileName the intended file name for the output file.
     */
    public void write(Graph g, String fileName){
        String dir = System.getProperty("user.dir");
        //Construct full path name to the file.
        String outputFile = dir + File.separator + fileName;
        //Calling a helper class to format the graph data.
        GraphWriter fs = new GraphWriter();
        fs.setGraphName(g.getId());
        fs.setDirected(true);
        try {
            fs.writeAll(g, outputFile);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Output failed");
        }
    }
}
