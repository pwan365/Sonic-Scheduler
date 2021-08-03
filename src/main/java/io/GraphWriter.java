package io;

import algo.Task;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.file.FileSinkDOT;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class GraphWriter extends FileSinkDOT {
    /*
    This class is used to modify the default graph writer in Graphstream
     */
    protected String graphName = "";

    public void setGraphName(String name){
        this.graphName = name;
    }

    /**
     * Modified exportGraph, writes the nodes and edges of the output file.
     * @param graph
     */
    @Override
    protected void exportGraph(Graph graph) {
        String graphId = graph.getId();
        AtomicLong timeId = new AtomicLong(0);

        graph.attributeKeys()
                .forEach(key -> graphAttributeAdded(graphId, timeId.getAndIncrement(), key, graph.getAttribute(key)));

        for (Node node : graph) {
            String nodeId = node.getId();
            out.printf("\t%s %s;%n", nodeId, outputAttributes(node));
        }

        graph.edges().forEach(edge -> {
            String fromNodeId = edge.getNode0().getId();
            String toNodeId = edge.getNode1().getId();
            String attr = outputAttributes(edge);

            if (digraph) {
                out.printf("\t%s -> %s", fromNodeId, toNodeId);

                if (!edge.isDirected())
                    out.printf(" -> %s", fromNodeId);
            } else
                out.printf("\t%s -- %s", fromNodeId, toNodeId);

            out.printf(" %s;%n", attr);
        });
    }

    /**
     * Modified outputAttributes, writes the attributes of the output file.
     * @param e
     * @return
     */
    @Override
    protected String outputAttributes(Element e) {
        if (e.getAttributeCount() == 0)
            return "";

        StringBuilder buffer = new StringBuilder("[");
        AtomicBoolean first = new AtomicBoolean(true);


        e.attributeKeys().forEach(key -> {
            Object value = e.getAttribute(key);


            if (value instanceof Number) {
                int weightValue = ((Number) value).intValue();
                buffer.append(String.format("%s%s=%s", first.get() ? "" : ",", key, weightValue));
            }else {
                //buffer.append(String.format("%s%s=%s", first.get() ? "" : ",", key, value));
            }

            if (key.equals("Task")){
                Task nodeTask = (Task) e.getAttribute("Task");
                int startTime = (int) nodeTask.getStartingTime();
                int processor = nodeTask.getAllocatedProcessor().getProcessNum();
                buffer.append(String.format("%s%s=%s", first.get() ? "" : ",", "Start", startTime));
                buffer.append(String.format("%s%s=%s", first.get() ? "" : ",", "Processor", processor));
            }


            first.set(false);
        });

        return buffer.append(']').toString();
    }

    /**
     * Modified outputHeader, writes the header of the output file
     * @throws IOException
     */
    @Override
    protected void outputHeader() throws IOException {
        out = (PrintWriter) output;
        out.printf("%s \"%s\"{%n", digraph ? "digraph" : "graph", "output" + graphName.substring(0,1).toUpperCase() + graphName.substring(1));


    }
}
