import org.graphstream.stream.file.FileSinkImages;

/**
 * Created by paul on 28.06.17.
 *
 * Diese Klasse implementiert den Algorithmus von Wagner et al. Er wurde 1999 im
 * Paper Distributed Covering By Ant-Robots Using Evaporating Traces vorgestellt
 *
 *
 */
public class AntWalk {

    private int ants_amount;
    private double evaporation;
    private AntWalkConfig config;
    public static int time = 0;

    public AntWalk(AntWalkConfig config){
        this.config = config;

    }


    /*
     * Ameisen gucken in ihrer Umgebung nach einer Kante die Sie nachgehen.
     */
    public void ants_walk(){
        for(Ant ant: this.config.getAnts()){
            ant.walk();
        }
    }


    public static void main(String... args){
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        Thread thread = new Thread(new Runnable() {
            public void run() {
                AntWalk awI = new AntWalk(new AntWalkIIConfig(4, 5, 5));
                //awI.config.getGraph()
                FileSinkImages pic = new FileSinkImages(FileSinkImages.OutputType.PNG, FileSinkImages.Resolutions.VGA);
                /*pic.setStyleSheet(
                        "graph { fill-color: white;}" +
                        "edge { text-alignment: under;}" +
                        "node.Base { fill-color: black;}" +
                        "node.Visited { fill-color: grey;}" +
                        "node.Ant { shadow-mode: plain;" +
                        "shadow-color: red;}");*/
                pic.setLayoutPolicy(FileSinkImages.LayoutPolicy.NO_LAYOUT);
                pic.setQuality(FileSinkImages.Quality.HIGH);
                pic.setRenderer(FileSinkImages.RendererType.SCALA);

                while(!awI.config.allEdgesWalked()) {
                    AntWalk.time++;
                    System.out.println("Runde "+ time);
                    awI.ants_walk();
                    awI.config.updateNodeLabel();
                    awI.config.evaporate();
                    awI.config.updateEdgeLabel();
                    awI.config.updateNodeClasses();

                    try {
                        Thread.sleep(500);
                        awI.config.getGraph().write(pic, "pics/"+timeToString(time)+"random.png");
                        //pic.writeAll(awI.config.getGraph(), time+"vector.png");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                System.out.println("Der Algorithmus AntWalk hat der Gebiet in "+ time + " Zeitschritten abgesucht.");
            }
        });
        thread.start();
    }

    private static String timeToString(int time) {
        if(time<10) return "00"+time;
        else if(time<100) return "0"+time;
        return time+"";
    }

    public AntWalkConfig getConfig() {
        return config;
    }
}
