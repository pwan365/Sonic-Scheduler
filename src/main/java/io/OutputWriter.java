package io;

import org.graphstream.graph.Graph;
import org.graphstream.stream.file.FileSinkDOT;

import java.io.File;
import java.io.IOException;

public class OutputWriter {
    private String outputFile;

    public void write(Graph g, String fileName){
        String dir = System.getProperty("user.dir");
        outputFile = dir + File.separator + fileName;
        GraphWriter fs = new GraphWriter();
        fs.setGraphName(g.getId());
        System.out.print(fs.graphName.length());
        fs.setDirected(true);
        try {
            fs.writeAll(g, outputFile);
        }catch (IOException e){}
    }
}
