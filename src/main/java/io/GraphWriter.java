package io;

import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.file.FileSinkDOT;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class GraphWriter extends FileSinkDOT {
    protected String graphName = "";

    public void setGraphName(String name){
        this.graphName = name;
    }

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

    @Override
    protected String outputAttributes(Element e) {
        if (e.getAttributeCount() == 0)
            return "";

        StringBuilder buffer = new StringBuilder("[");
        AtomicBoolean first = new AtomicBoolean(true);


        e.attributeKeys().forEach(key -> {
            boolean quote = true;
            Object value = e.getAttribute(key);

            if (value instanceof Number) {
                int weightValue = ((Number) value).intValue();
                quote = false;
                buffer.append(String.format("%s%s=%s%s%s", first.get() ? "" : ",", key, quote ? "\"" : "", weightValue,
                        quote ? "\"" : ""));
            }else {

                buffer.append(String.format("%s%s=%s%s%s", first.get() ? "" : ",", key, quote ? "\"" : "", value,
                        quote ? "\"" : ""));
            }

            first.set(false);
        });

        return buffer.append(']').toString();
    }

    @Override
    protected void outputHeader() throws IOException {
        out = (PrintWriter) output;
        out.printf("%s \"%s\"{%n", digraph ? "digraph" : "graph", graphName);


    }
}
