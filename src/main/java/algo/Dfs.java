package algo;

import java.util.ArrayList;
import java.util.List;
import org.graphstream.graph.*;
import io.Reader;
import algo.Tree_Node;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.graphstream.graph.implementations.SingleGraph;

public class Dfs {

    public Graph Generate_Output(){
        int num_processor = 2;

        Reader reader = new Reader("try.dot");
        Graph raw_graph = reader.read();
        int num_node = raw_graph.getNodeCount();
        int num_edge = raw_graph.getEdgeCount();
        List<Node> graph_node_list = new ArrayList<Node>();
        for (int i = 0; i < num_edge; i++){
            graph_node_list.add(raw_graph.getNode(i));
        }

        Node Starting_node = raw_graph.getNode(0);
        List<Task> task_list = new ArrayList<Task>();
        for (int i = 0; i < raw_graph.getNodeCount(); i++){
            task_list.add(new Task(raw_graph.getNode(0)));
        }
        Tree_Node root = new Tree_Node();
        root.setCurrent_time((Double)Starting_node.getAttribute("Weight"));
        for (int i = 0; i < num_processor; i++) {
            root.addProcessors(new Processor());
        }

        Task temp_task = task_list.get(0);
        while (!task_list.isEmpty()){

            for (Processor p : root.getProcessors()){

            }
        }

        root.getProcessors().get(0).addTask(new Task(Starting_node));
        root.getProcessors().get(0).setLatest_time((Double)Starting_node.getAttribute("Weight"));

        Tree_Node temp_node = root;
        //graph_node_list.remove(Starting_node);

        List<Edge> edges = Starting_node.leavingEdges().collect(Collectors.toList());
        List<Node> nodes = new ArrayList<Node>();
        Double current_time = root.getCurrent_time();
        for (Edge e : edges){
            Node temp = e.getNode1();
            nodes.add(temp);
            Double temp_current_time = current_time + (Double) e.getAttribute("Weight") + (Double) temp.getAttribute("Weight");
            //root.addChild(new Tree_Node(temp_current_time));
            Processor second_processor = root.getProcessors().get(1);
            //second_processor.add_node(temp);
            second_processor.setLatest_time(temp_current_time);
        }

        for (Node n : nodes){
            Double temp_current_time = current_time + (Double) n.getAttribute("Weight");
        }

        return raw_graph;
    }
}
