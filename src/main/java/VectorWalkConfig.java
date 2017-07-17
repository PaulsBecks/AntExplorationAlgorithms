import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by paul on 28.06.17.
 */
public class VectorWalkConfig extends  AntWalkConfig{

    private String path = "src"+ File.separator+ "main"+File.separator+"resources"+File.separator+"world"+File.separator;
    private Graph graph;
    private List<Ant> ants;
    private double evaporate_rate = 0.8;

    public VectorWalkConfig(int amount_of_ants){
        super();
        //this.graph = super.loadGraphFile(this.path+"simple_world2.json");
        this.graph = super.getGridGraph(8, 4);
        this.ants = new LinkedList<Ant>();
        Viewer viewer = this.graph.display();
        viewer.disableAutoLayout();
        super.addCssToGraph(graph);

        for(int i = 0; i<amount_of_ants; i++){
            ants.add(new AntVector(graph.getNode("AA")));
        }
        graph.getNode("D").setAttribute("ui.class", "Base");
    }

    public VectorWalkConfig(int amount_of_ants, int size) {
        super();
        //this.graph = super.loadGraphFile(this.path+"simple_world2.json");
        this.graph = super.getGridGraph(size, size);
        this.ants = new LinkedList<Ant>();
        Viewer viewer = this.graph.display();
        viewer.disableAutoLayout();
        super.addCssToGraph(graph);

        for(int i = 0; i<amount_of_ants; i++){
            ants.add(new AntVector(graph.getNode("AA")));
        }
        graph.getNode("AA").setAttribute("ui.class", "Base");
    }

    public VectorWalkConfig(int amount_of_ants, int height, int width) {
        super();
        //this.graph = super.loadGraphFile(this.path+"simple_world2.json");
        this.graph = super.getGridGraph(width, height);
        this.ants = new LinkedList<Ant>();
        Viewer viewer = this.graph.display();
        viewer.disableAutoLayout();
        super.addCssToGraph(graph);

        for(int i = 0; i<amount_of_ants; i++){
            ants.add(new AntVector(graph.getNode("AA")));
        }
        graph.getNode("AA").setAttribute("ui.class", "Base");
    }


    public List<Ant> getAnts() {
        return this.ants;
    }

    public Graph getGraph() {
        return graph;
    }

    public void evaporate(){
        for(Edge edge: this.graph.getEachEdge()){
            //edge.setAttribute("ui.label", Double.parseDouble(edge.getAttribute("ui.label").toString()) * this.evaporate_rate);
        }
    }

    public void updateNodeLabel(){
        for(Node node: this.graph.getEachNode()){
            int ants = node.getAttribute("ants");
            int visited = node.getAttribute("visited");
            int s = node.getAttribute("s");
            String label = node.toString();
            node.setAttribute("ui.label", ants + " | "+ visited +" | "+ s + " | "+ label);
        }
    }

    public boolean allEdgesWalked() {
        for(Node node: graph.getEachNode()){
            //System.out.println((Integer) edge.getAttribute("visited"));
            if((Integer) node.getAttribute("visited")==0){
                return false;
            }
        }
        return true;
    }

    public void updateNodeClasses() {
        Iterator<Node> iter = graph.getNodeIterator();
        while (iter.hasNext()) {
            Node node = iter.next();
            String classes = "";
            if ((Integer) node.getAttribute("visited") > 0) {
                classes += "Visited, ";
            }
            if ((Integer) node.getAttribute("ants") > 0) {
                classes += "Ant, ";
            }
            if (classes.length() > 0) {
                classes.substring(0, classes.length() - 2);
                //System.out.println(classes);
            }
            node.setAttribute("ui.class", classes);
        }
    }
}
