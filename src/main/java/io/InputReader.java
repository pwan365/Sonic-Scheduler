package io;
import java.io.*;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;
public class InputReader {

    private String input_file;
    public InputReader(String input_file){
        this.input_file = input_file;
    }
    public Graph read() {
        String dir = System.getProperty("user.dir");
        String line;
        String graphName = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(dir + File.separator + input_file));
            while ((line = br.readLine()) != null ){
                if (line.contains("digraph")){
                    graphName = line.split("\"")[1];
                    break;
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
            fs.readAll(dir + File.separator + input_file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fs.removeSink(g);
        }
        return g;
    }
}
