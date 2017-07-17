import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by paul on 02.07.17.
 */
public class Benchmark {

    public static int[] sizes = new int[]{4,5,6,7}; //abhaengig von diameter
    public static int[] diameter = new int[]{4,8,16,32};
    public static int[] ants = new int[]{1,2,3,4};

    public static String path = "random_walk_output2.csv";
    public static void main(String... args){
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        Thread thread = new Thread(new Runnable() {
            public void run() {
                removeOldStuff();

                for (int diameter: diameter) {
                    for (int i = 1; i<=(diameter+2)/2; i++) {
                        for (int ants_amount : ants) {
                            AntWalk awI = new AntWalk(new RandomAntWalkConfig(ants_amount, diameter+2-i, i));
                            while (!awI.getConfig().allEdgesWalked()) {
                                AntWalk.time++;
                                if(AntWalk.time>10000){
                                    break;
                                }
                                awI.ants_walk();
                                awI.getConfig().updateNodeLabel();
                                awI.getConfig().evaporate();
                                awI.getConfig().updateEdgeLabel();
                                awI.getConfig().updateNodeClasses();
                                try {
                                    Thread.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                            try {
                                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));
                                out.println(diameter + ","+(diameter+2-i)+","+ i + ","+ ants_amount + "," + AntWalk.time);
                                out.close();
                            } catch (IOException e) {
                                //exception handling left as an exercise for the reader
                            }
                            System.out.println("Der Algorithmus AntWalk hat der Gebiet in " + AntWalk.time + " Zeitschritten abgesucht.");
                            AntWalk.time = 0;
                        }
                    }
                }
            }
        });
        thread.start();
    }

    private static void removeOldStuff() {
        //remove old stuff out of output
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path, false)));
            writer.println("diameter,height,width,ants,time");
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
