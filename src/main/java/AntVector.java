/**
 * Created by paul on 27.04.17.
 */

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import scala.Int;

import java.util.Iterator;

public class AntVector implements Ant{

    private int id;

    private Node current_node;
    private double pheromone = 0.8;


    public AntVector(Node base){
        this.current_node = base;
        this.addAntToNode(base);
    }

    public void walk(){
        Node next_node = null;
        //System.out.println("Ant Walk");
        Iterator<Node> iter = current_node.getNeighborNodeIterator();
        while(iter.hasNext()){

            Node node = iter.next();
            //System.out.println(this.toString() + " "+ node.toString());
            if(next_node == null){
                next_node = node;
                continue;
            }
            //System.out.println(this.toString() + "| "+ node.toString() + ": "+ (Integer) node.getAttribute("s") + " ; "+ next_node.toString() + ": "+ (Integer) next_node.getAttribute("s"));
            if((Integer) node.getAttribute("s") < (Integer) next_node.getAttribute("s")){
                next_node = node;
            }
            else if((Integer) node.getAttribute("s") == (Integer) next_node.getAttribute("s") && (Integer) node.getAttribute("visited") < (Integer) next_node.getAttribute("visited")){
                next_node = node;
            }
            //System.out.println(this.toString() + " "+ next_node.toString());
        }
        removeAntFromNode(current_node);
        addAntToNode(next_node);
        current_node = next_node;
    }

    public void walkOnEdge(Edge edge_to_walk) {}

    public void addAntToNode(Node node) {
        node.setAttribute("s", AntWalk.time);
        node.setAttribute("ants", Integer.parseInt(node.getAttribute("ants").toString())+1);
        node.setAttribute("visited", Integer.parseInt(node.getAttribute("visited").toString())+1);
    }

    public void removeAntFromNode(Node node) {
        //setting s when leaving is bad because multi ants will eventually go to the same next node
        node.setAttribute("s", AntWalk.time);
        node.setAttribute("ants", Integer.parseInt(node.getAttribute("ants").toString())-1);
    }

}
