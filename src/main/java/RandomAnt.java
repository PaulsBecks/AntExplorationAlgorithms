/**
 * Created by paul on 27.04.17.
 */

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RandomAnt implements Ant{

    private int id;

    private Node current_node;
    private double pheromone = 0.8;


    public RandomAnt(Node base){
        this.current_node = base;
        this.addAntToNode(base);
    }

    public void walk(){
        Edge edge_to_walk = null;
        List<Edge> edges = new LinkedList<Edge>();
        for(Edge edge : current_node.getEdgeSet()){
            edges.add(edge);
        }
        Random rand = new Random();
        int i = rand.nextInt(edges.size());
        edge_to_walk = edges.get(i);

        if(edge_to_walk.getNode0() != current_node){
            this.removeAntFromNode(current_node);
            this.addAntToNode(edge_to_walk.getNode0());
            this.current_node = edge_to_walk.getNode0();
        }
        else{
            this.removeAntFromNode(current_node);
            this.addAntToNode(edge_to_walk.getNode1());
            this.current_node = edge_to_walk.getNode1();
        }

    }

    public void walkOnEdge(Edge edge_to_walk) {
        double pheromone = edge_to_walk.getAttribute("ui.label");
        pheromone += this.pheromone;
        edge_to_walk.setAttribute("ui.label", pheromone);
        edge_to_walk.setAttribute("visited", Integer.parseInt(edge_to_walk.getAttribute("visited").toString())+1);
    }

    public void addAntToNode(Node node) {
        node.setAttribute("ants", Integer.parseInt(node.getAttribute("ants").toString())+1);
        node.setAttribute("visited", Integer.parseInt(node.getAttribute("visited").toString())+1);
    }

    public void removeAntFromNode(Node node) {
        node.setAttribute("ants", Integer.parseInt(node.getAttribute("ants").toString())-1);
    }
}
