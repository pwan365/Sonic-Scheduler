package io;

import algo.Task;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.file.FileSinkDOT;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class modifies some methods of FileSinkDOT class in GraphStream and make a custom format for the output .dot
 * file.
 *
 * @author Jason Wang
 */
public class GraphWriter extends FileSinkDOT {

    private String graphName = "";

    /**
     * This method is used to set the graph name that will be written to the output file.
     * @param name Name of the graph.
     */
    public void setGraphName(String name){
        this.graphName = name;
    }

    /**
     * Modified exportGraph, scan through every node and edge of the graph and outputs them.
     * @param graph Graph to be outputted.
     */
    @Override
    protected void exportGraph(Graph graph) {
        String graphId = graph.getId();
        AtomicLong timeId = new AtomicLong(0);

        graph.attributeKeys()
                .forEach(key -> graphAttributeAdded(graphId, timeId.getAndIncrement(), key, graph.getAttribute(key)));

        //Outputting every node
        for (Node node : graph) {
            String nodeId = node.getId();
            out.printf("\t%s %s;%n", nodeId, outputAttributes(node));
        }

        //Outputting every edge
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
     * Modified outputAttributes, writes the attributes of the given element into a string.
     * @param e The element of the graph to be outputted.
     * @return a string that represents all attributes of an element.
     */
    @Override
    protected String outputAttributes(Element e) {
        if (e.getAttributeCount() == 0)
            return "";

        StringBuilder buffer = new StringBuilder("[");
        AtomicBoolean first = new AtomicBoolean(true);

        //Loop through every attribute of an element.
        e.attributeKeys().forEach(key -> {
            Object value = e.getAttribute(key);

            if (value instanceof Number) {
                //Formatting for the "Weight" of the element.
                int weightValue = ((Number) value).intValue();
                buffer.append(String.format("%s%s=%s", first.get() ? "" : ",", key, weightValue));
            }else if (key.equals("Task")){
                //Formatting if the element contains its start time and processor information.
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
        //Formatting the output graph name. Adds "output" at the start and capitalizes first letter.
        String outputName = "output" + graphName.substring(0,1).toUpperCase() + graphName.substring(1);
        out = (PrintWriter) output;
        out.printf("%s \"%s\"{%n", digraph ? "digraph" : "graph", outputName);
    }
}
