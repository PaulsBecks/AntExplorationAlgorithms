import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by paul on 28.06.17.
 */
public class AntWalkIIModConfig extends  AntWalkConfig{

    private String path = "src"+ File.separator+ "main"+File.separator+"resources"+File.separator+"world"+File.separator;
    private Graph graph;
    private List<Ant> ants;
    private double evaporate_rate = 0.8;
    NumberFormat formatter = new DecimalFormat("#0.00");

    public AntWalkIIModConfig(int amount_of_ants){
        super();
        this.graph = this.loadGraphFile(this.path+"simple_world.json");
        this.ants = new LinkedList<Ant>();
        Viewer viewer = this.graph.display();
        viewer.disableAutoLayout();
        super.addCssToGraph(graph);

        for(int i = 0; i<amount_of_ants; i++){
            ants.add(new AntII(graph.getNode("A")));
        }
        graph.getNode("A").setAttribute("ui.class", "Base");
    }

    public AntWalkIIModConfig(int amount_of_ants, int size) {
        super();
        this.graph = super.getGridGraph(size, size);
        this.ants = new LinkedList<Ant>();
        Viewer viewer = this.graph.display();
        viewer.disableAutoLayout();
        super.addCssToGraph(graph);

        for(int i = 0; i<amount_of_ants; i++){
            ants.add(new AntII(graph.getNode("AA")));
        }
        graph.getNode("AA").setAttribute("ui.class", "Base");
    }

    public AntWalkIIModConfig(int amount_of_ants, int height, int width) {
        super();
        //this.graph = super.loadGraphFile(this.path+"simple_world2.json");
        this.graph = super.getGridGraph(width, height);
        this.ants = new LinkedList<Ant>();
        Viewer viewer = this.graph.display();
        viewer.disableAutoLayout();
        super.addCssToGraph(graph);

        for(int i = 0; i<amount_of_ants; i++){
            ants.add(new AntII(graph.getNode("AA")));
        }
        graph.getNode("AA").setAttribute("ui.class", "Base");
    }

    public Graph loadGraphFile(String location)  {
        String jsonString = "";

        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {

            Object obj = parser.parse(new FileReader(location));

            json = (JSONObject) obj;
        }
        catch (Exception e){
            //TODO: handle exception
            e.printStackTrace();
        }
        JSONObject world = (JSONObject) json.get("world");
        JSONArray nodes = (JSONArray) world.get("nodes");
        JSONArray edges = (JSONArray) world.get("edges");

        String name = (String) world.get("name");

        Graph graph = new SingleGraph("AntWalk");

        //add nodes to graph
        for(int i=0; i<nodes.size(); i++){
            JSONObject node = (JSONObject) nodes.get(i);
            Node n = graph.addNode((String) node.get("label"));
            n.addAttribute("xy", (Long) node.get("x"), (Long) node.get("y"));
            n.addAttribute("visited", 0);
            n.addAttribute("ants", 0);

            n.addAttribute("ui.label", (String)node.get("label"));
        }

        //add edges to graph
        for(int i=0; i<edges.size(); i++){
            JSONObject edge = (JSONObject) edges.get(i);
            Edge e = graph.addEdge((String)edge.get("label"), (String)edge.get("node1"), (String)edge.get("node2"));


            // TODO: remove this, once all code is uptdated to use food and nest pheromones
            e.addAttribute("pheromone", 0.0);
            e.addAttribute("ui.label", 0.0);
            e.addAttribute("visited", 0);
            e.addAttribute("tin", 0);
            e.addAttribute("tout", 0);
            e.addAttribute("s1", 0.0);
            e.addAttribute("s2", 0.0);
        }


        return graph;
    }

    public List<Ant> getAnts() {
        return ants;
    }

    public Graph getGraph() {
        return graph;
    }

    public void updateNodeLabel(){
        for(Node node: this.graph.getEachNode()){
            int ants = node.getAttribute("ants");
            int visited = node.getAttribute("visited");
            String label = node.toString();
            node.setAttribute("ui.label", ants + " | "+ visited +" | "+ label);
        }
    }

    public void evaporate(){
        for(Edge edge: this.graph.getEachEdge()){
            edge.setAttribute("s1", (Double) edge.getAttribute("s1") * this.evaporate_rate);
            edge.setAttribute("s2", (Double) edge.getAttribute("s2") * this.evaporate_rate);
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

    public void updateEdgeLabel(){
        for(Edge edge: graph.getEdgeSet()){
            double s1 = edge.getAttribute("s1");
            double s2 = edge.getAttribute("s2");
            edge.setAttribute("ui.label", formatter.format(s1) + " | " + formatter.format(s2));
        }
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
            }
            node.setAttribute("ui.class", classes);
        }
    }
}
