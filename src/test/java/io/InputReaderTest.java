package io;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputReaderTest {
    @Test
    void testNode(){
        InputReader reader = new InputReader("try.dot");
        Graph g = reader.read();
        Node n = g.getNode("c");
        assertEquals(n.getAttribute("Weight"), 3.0);
    }

    @Test
    void testEdge(){
        InputReader reader = new InputReader("try.dot");
        Graph g = reader.read();
        Edge e = g.getEdge("(a;e)");
        assertEquals(e.getAttribute("Weight"), 3.0);
    }

    @Test
    void testWrite(){
        InputReader reader = new InputReader("try.dot");
        Graph g = reader.read();
        OutputWriter writer = new OutputWriter();
        writer.write(g, "outputtry.dot");
        assertTrue(true);
    }
}