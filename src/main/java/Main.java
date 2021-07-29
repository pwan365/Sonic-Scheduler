import algo.Dfs;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.Iterator;

public class Main  {
    public static void main(String[] args) {
        Dfs dfs = new Dfs();
        Graph g = dfs.Generate_Output();
        Node s = g.getNode(0);
        Iterator<Node> m = s.getBreadthFirstIterator(true);
        while (m.hasNext()){
            System.out.println(m.next().getId());
        }
    }
}