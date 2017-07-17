/**
 * Created by paul on 27.04.17.
 */

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

public class AntII implements Ant{

    private int id;

    private Node current_node;
    private double pheromone = 0.8;


    public AntII(Node base){
        this.current_node = base;
        this.addAntToNode(base);
    }

    public void walk(){
        Edge edge_to_walk = null;
        for(Edge edge : current_node.getEdgeSet()){
            if(edge_to_walk == null){
                edge_to_walk = edge;
            }
            else{
                Double edge_pheromone = getPheromone(edge);
                Double edge_to_walk_pheromone = getPheromone(edge_to_walk);
                if (edge_pheromone < edge_to_walk_pheromone) {
                    edge_to_walk = edge;
                }
                if(edge_pheromone.equals(edge_to_walk_pheromone)){
                    if(visitedNeighbour(edge) <(Integer) visitedNeighbour(edge_to_walk)){
                        edge_to_walk = edge;
                    }
                }

            }
        }

        if(edge_to_walk.getNode0() != current_node){
            this.removeAntFromNode(current_node);
            this.walkOnEdge(edge_to_walk);
            this.addAntToNode(edge_to_walk.getNode0());
            this.current_node = edge_to_walk.getNode0();
        }
        else{
            this.removeAntFromNode(current_node);
            this.walkOnEdge(edge_to_walk);
            this.addAntToNode(edge_to_walk.getNode1());
            this.current_node = edge_to_walk.getNode1();
        }

    }

    private int visitedNeighbour(Edge edge) {
        if(edge.getNode0() != current_node){
            return edge.getNode0().getAttribute("visited");
        }
        return edge.getNode1().getAttribute("visited");
    }

    private double getPheromone(Edge edge) {
        if(edge.getNode0() == current_node){
            return edge.getAttribute("s1");
        }
        return edge.getAttribute("s2");
    }

    public void walkOnEdge(Edge edge_to_walk) {
        double pheromone = getPheromone(edge_to_walk);
        pheromone += this.pheromone;
        if(edge_to_walk.getNode0() == current_node){
            edge_to_walk.setAttribute("s1", pheromone);
        }
        else{
            edge_to_walk.setAttribute("s2", pheromone);
        }
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
