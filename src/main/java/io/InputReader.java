package io;
import java.io.File;
import java.io.IOException;
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
        Graph g = new DefaultGraph("Tut");
        FileSource fs = new FileSourceDOT();
        String dir = System.getProperty("user.dir");
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
