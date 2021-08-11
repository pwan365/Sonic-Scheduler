package graphTests.validateTests;//package io;
import java.io.*;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;

/**
 * A input reader that read input graph from a sub directory
 * @author Samuel Chen
 */
public class InputReaderHelper {

    private String input_file;
    public InputReaderHelper(String input_file){
        this.input_file = input_file;
    }
    public Graph read() {
        String line ="";

        String graphName = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(input_file));
            while ((line = br.readLine()) != null ){
                if (line.contains("digraph")){
                    graphName = line.split("\"")[1];
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Graph g = new DefaultGraph(graphName);
        FileSource fs = new FileSourceDOT();
        fs.addSink(g);
        try {
            fs.readAll(input_file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fs.removeSink(g);
        }
        return g;
    }
}
