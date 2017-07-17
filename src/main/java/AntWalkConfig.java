import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
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
import java.util.LinkedList;
import java.util.List;

/**
 * Created by paul on 28.06.17.
 */
public class AntWalkConfig {

    private String path = "src"+ File.separator+ "main"+File.separator+"resources"+File.separator+"world"+File.separator;
    private Graph graph;
    private List<Ant> ants;
    private double evaporate_rate = 0.8;

    public AntWalkConfig(){

    }

    protected Graph loadRandomGraph()  {
        Graph graph = new SingleGraph("Random");

        Generator gen = new RandomGenerator(3);
        gen.addSink(graph);
        gen.begin();
        for(int i=0; i<10; i++)
            gen.nextEvents();
        gen.end();
        graph.display();
        //viewer.disableAutoLayout();

        return graph;
    }

    protected Graph loadGraphFile(String location)  {
        String jsonString = "";

        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {

            Object obj = parser.parse(new FileReader(location));

            json = (JSONObject) obj;
        }
        catch (Exception e){
            //TODO: handle exception
            //System.out.println("No file at given location");
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
            n.addAttribute("s", 0);
            n.addAttribute("tin", 0);
            n.addAttribute("tout", 0);
            System.out.println(n.getAttribute("ants"));
        }

        //add edges to graph
        for(int i=0; i<edges.size(); i++){
            JSONObject edge = (JSONObject) edges.get(i);
            Edge e = graph.addEdge((String)edge.get("label"), (String)edge.get("node1"), (String)edge.get("node2"));


            // TODO: remove this, once all code is uptdated to use food and nest pheromones
            e.addAttribute("s1", 0);
            e.addAttribute("s2", 0);
            e.addAttribute("pheromone", 0.0);
            e.addAttribute("ui.label", 0.0);
            e.addAttribute("visited", 0);
            e.addAttribute("s1", 0.0);
            e.addAttribute("s2", 0.0);

        }


        return graph;
    }

    protected void addCssToGraph(Graph graph){
        graph.addAttribute("ui.stylesheet", "node { " +
                "size: 50px, 40px;" +
                "padding: 50px;" +
                "shape: box;" +
                "text-color: red;" +
                "fill-color: white;" +
                "text-alignment: center;" +
                "stroke-mode: plain;" +
                "}" +
                "graph { fill-color: white;" +
                "padding: 0px,20px,0px,0px;}" +
                "edge { text-alignment: under;}" +
                "node.Base { fill-color: black;}" +
                "node.Visited { fill-color: grey;}" +
                "node.Ant { shadow-mode: plain;" +
                "shadow-color: red;}"
        );
    }

    private Graph new_graph() {

        Graph graph = new SingleGraph("AntWalk");

        //Node A
        Node a = graph.addNode("A");
        a.setAttribute("xy",  0, 0);
        a.setAttribute("ants",  0);
        a.setAttribute("visited",  0);
        a.setAttribute("ui.label", "AL");
        a.setAttribute("ui.class", "Base");

        //Node B
        Node b = graph.addNode("B");
        b.addAttribute("xy",  0, 1);
        b.addAttribute("ants",  0);
        b.setAttribute("visited",  0);
        b.addAttribute("ui.label", "B");

        //Node C
        Node c = graph.addNode("C");
        c.addAttribute("xy",  0, 2);
        c.addAttribute("ants",  0);
        c.setAttribute("visited",  0);
        c.addAttribute("ui.label", "C");

        //Node D
        Node d = graph.addNode("D");
        d.addAttribute("xy",  1, 2);
        d.addAttribute("ants",  0);
        d.setAttribute("visited",  0);
        d.addAttribute("ui.label", "D");

        Edge ab = graph.addEdge("AB", "A", "B");
        ab.addAttribute("pheromone", 0.0);
        ab.setAttribute("visited",  0);
        ab.addAttribute("ui.label", 0.0);

        Edge bc = graph.addEdge("BC", "B", "C");
        bc.addAttribute("pheromone", 0.0);
        bc.setAttribute("visited",  0);
        bc.addAttribute("ui.label", 0.0);

        Edge cd = graph.addEdge("CD", "C", "D");
        cd.addAttribute("pheromone", 0.0);
        cd.setAttribute("visited",  0);
        cd.addAttribute("ui.label", 0.0);

        Edge bd = graph.addEdge("BD", "B", "D");
        bd.addAttribute("pheromone", 0.0);
        bd.setAttribute("visited",  0);
        bd.addAttribute("ui.label", 0.0);

        Edge ad = graph.addEdge("AD", "A", "D");
        ad.addAttribute("pheromone", 0.0);
        ad.setAttribute("visited",  0);
        ad.addAttribute("ui.label", 0.0);

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
            edge.setAttribute("ui.label", (Double) edge.getAttribute("ui.label") * this.evaporate_rate);
        }
    }

    public boolean allEdgesWalked() {
        for(Edge edge: graph.getEachEdge()){
            //System.out.println((Integer) edge.getAttribute("visited"));
            if((Integer) edge.getAttribute("visited")==0){
                return false;
            }
        }
        return true;
    }

    public void updateEdgeLabel() {}

    public void updateNodeClasses() {}

    public Graph getGridGraph(int n, int m){
        Graph graph = new SingleGraph("GridGraph");
        String s = "AA";
        for(int i = 0; i<n; i++){
            for(int j = 0; j<m; j++){
                Node node = graph.addNode(s);
                node.addAttribute("xy", i, j);
                node.addAttribute("visited", 0);
                node.addAttribute("ants", 0);
                node.addAttribute("ui.label", s);
                node.addAttribute("s", 0);
                node.addAttribute("tin", 0);
                node.addAttribute("tout", 0);

                if(j > 0) {
                    String last = befor(s, 1);
                    Edge e = graph.addEdge(s + last, s, last);
                    e.addAttribute("s1", 0);
                    e.addAttribute("s2", 0);
                    e.addAttribute("pheromone", 0.0);
                    e.addAttribute("ui.label", 0.0);
                    e.addAttribute("visited", 0);
                    e.addAttribute("s1", 0.0);
                    e.addAttribute("s2", 0.0);
                }
                if(i>0){
                    String last_row = befor(s, m);
                    Edge e = graph.addEdge(s + last_row, s, last_row);
                    e.addAttribute("s1", 0);
                    e.addAttribute("s2", 0);
                    e.addAttribute("pheromone", 0.0);
                    e.addAttribute("ui.label", 0.0);
                    e.addAttribute("visited", 0);
                    e.addAttribute("s1", 0.0);
                    e.addAttribute("s2", 0.0);
                }

                s = next(s);
            }

        }
        return graph;
    }

    public static String befor(String str, int i){
        for(int j=0; j<i; j++){
            str = decrement(str);
        }
        return str;
    }

    public static String decrement(String str){
        if((str.charAt(str.length()-1)) == 'A'){
            return decrement(str.substring(0, str.length()-1)) + 'Z';
        }
        return str.substring(0, str.length()-1)+((char)(str.charAt(str.length()-1)-1));
    }

    public static String next(String s) {
        int length = s.length();
        char c = s.charAt(length - 1);

        if(c == 'Z')
            return length > 1 ? next(s.substring(0, length - 1)) + 'A' : "AA";

        return s.substring(0, length - 1) + ++c;
    }
}
