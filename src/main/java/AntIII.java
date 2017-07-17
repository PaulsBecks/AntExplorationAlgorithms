/**
 * Created by paul on 27.04.17.
 */

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import java.util.Iterator;

public class AntIII implements Ant{

    private int id;

    private Node current_node;
    private double pheromone = 0.8;
    private int searchLevel = 1;

    public AntIII(Node base){
        this.current_node = base;
        this.addAntToNode(base);
        setNodeInOverBase(base);
    }

    public void walk() {
        //System.out.println("Ant Walk");
        //choose edge to walk
        Iterator<Node> neighbours = current_node.getNeighborNodeIterator();

        while (neighbours.hasNext()) {
            Node neighbour = neighbours.next();
            Edge edge = current_node.getEdgeBetween(neighbour);
            Double pheromone = getPheromone(edge);
            //System.out.println("Pheromone: " + pheromone + "\t Search Level: " + searchLevel);
            if (pheromone < searchLevel) {
                this.walkOnEdge(edge);
                //System.out.println(edge.getAttribute("tin") );
                if ((Integer) neighbour.getAttribute("tin") < searchLevel) {
                    this.removeAntFromNode(current_node);
                    this.addAntToNode(neighbour);
                    this.current_node = neighbour;
                    return;
                }
            }
        }
        //System.out.println("No Good neighbour found");
        //System.out.println(current_node.getAttribute("tin") + " " + current_node.getAttribute("tout"));
        if ((Integer) current_node.getAttribute("tin") > (Integer) current_node.getAttribute("tout")
                && (Integer) current_node.getAttribute("tout") >= searchLevel) {
            searchLevel = AntWalk.time;
            //System.out.println("Search Level of Ant updated to " + searchLevel);
            setNodeInOverBase(this.current_node);
        }

        for (Edge edge : current_node.getEachEdge()) {
            //System.out.println(getReversePheromone(edge) + " "+ current_node.getAttribute("tin"));
            if ((int) getReversePheromone(edge) == (Integer) current_node.getAttribute("tin")) {
                this.walkOnEdge(edge);
                if (edge.getNode0() != current_node) {
                    this.removeAntFromNode(current_node);
                    edge.getNode0().setAttribute("ants", Integer.parseInt(edge.getNode0().getAttribute("ants").toString())+1);
                    edge.getNode0().setAttribute("visited", Integer.parseInt(edge.getNode0().getAttribute("visited").toString())+1);
                    this.current_node = edge.getNode0();
                } else {
                    this.removeAntFromNode(current_node);
                    edge.getNode1().setAttribute("ants", Integer.parseInt(edge.getNode1().getAttribute("ants").toString())+1);
                    edge.getNode1().setAttribute("visited", Integer.parseInt(edge.getNode1().getAttribute("visited").toString())+1);
                    this.current_node = edge.getNode1();
                }
                //System.out.println("Reverse Step");
                return;
            }
        }



    }

    private double getReversePheromone(Edge edge) {
        if(edge.getNode0() != current_node){
            return edge.getAttribute("s1");
        }
        return edge.getAttribute("s2");
    }

    private double getPheromone(Edge edge) {
        if(edge.getNode0() == current_node){
            return edge.getAttribute("s1");
        }
        return edge.getAttribute("s2");
    }

    public void walkOnEdge(Edge edge_to_walk) {
        if(edge_to_walk.getNode0() == current_node){
            edge_to_walk.setAttribute("s1", (double) AntWalk.time);
        }
        else{
            edge_to_walk.setAttribute("s2", (double) AntWalk.time);
        }
        edge_to_walk.setAttribute("visited", Integer.parseInt(edge_to_walk.getAttribute("visited").toString())+1);
    }

    public void addAntToNode(Node node) {
        //System.out.println(node.getAttribute("visited") + " visited | " + node.toString());

        node.setAttribute("tin", AntWalk.time);

        node.setAttribute("ants", Integer.parseInt(node.getAttribute("ants").toString())+1);
        node.setAttribute("visited", Integer.parseInt(node.getAttribute("visited").toString())+1);
        //System.out.println(node.getAttribute("visited") + " ants: "+ node.getAttribute("ants"));
    }

    public void removeAntFromNode(Node node) {
        //System.out.println(node.getAttribute("ants"));
        node.setAttribute("tout", AntWalk.time);
        node.setAttribute("ants", Integer.parseInt(node.getAttribute("ants").toString())-1);
        //System.out.println(node.getAttribute("ants"));
    }

    public void setNodeInOverBase(Node node) {
        node.setAttribute("tin", searchLevel+2);
    }
}
