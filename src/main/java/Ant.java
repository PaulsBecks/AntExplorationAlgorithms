/**
 * Created by paul on 27.04.17.
 */

import org.graphstream.graph.*;

import java.lang.reflect.Array;
import java.util.*;

public interface Ant {

    public void walk();

    void walkOnEdge(Edge edge_to_walk);


    void addAntToNode(Node node);

    void removeAntFromNode(Node node);
}
