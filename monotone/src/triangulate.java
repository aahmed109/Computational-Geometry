import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ahmed on 24/05/2018 at 10:12 AM.
 */
class edges{
    private nodes A =  new nodes(9999.0, 9999.0);
    private nodes B = new nodes(9999.0, 9999.0);
    private double len = 0.0;
    private static double findLength(nodes a, nodes b){
        return Math.sqrt((a.getX() - b.getX()) * (a.getX() - b.getX()) + ((a.getY() - b.getY()) * (a.getY() - b.getY())));
    }

    edges(){
        A = new nodes(9999.0, 9999.0);
        B = new nodes(9999.0, 9999.0);
        len = findLength(A, B);
    }
    edges(nodes A, nodes B){
        this.A = A;
        this.B = B;
        len = findLength(A, B);
    }

    double getLen(){
        return len;
    }
    void setA(nodes A) { this.A = A; }
    void setB(nodes B) { this.B = B; }
    nodes getA(){ return A; }
    nodes getB(){ return B; }
}


class Polygon {
    ArrayList<nodes> nodesArrayList = new ArrayList<>();
    ArrayList<edges> edgesArrayList = new ArrayList<>();
    ArrayList<ArrayList<nodes>> adjacentNodeList = new ArrayList<>();

    Polygon(){
        ArrayList<nodes> tempList = new ArrayList<>();
        for(int j = 0; j < 100; j++){
            tempList.add(new nodes(9999.0, 9999.0));
        }
        for(int i = 0; i < 100; i++){
            adjacentNodeList.add(tempList);
        }
    }

    void setAdjacency(){
        for(int i = 0; i < nodesArrayList.size(); i++){
            //edge discovered = edgeArrayList.get(i);
            nodes nodes = nodesArrayList.get(i);
            ArrayList<nodes> adjacencies = new ArrayList<>();
            int s = nodesArrayList.indexOf(nodes);
            for(int j = 0; j < edgesArrayList.size(); j++){
                edges discovered = edgesArrayList.get(j);
                if(discovered.getA().equals(nodes)){
                    adjacencies.add(discovered.getB());
                }
                else if(discovered.getB().equals(nodes)){
                    adjacencies.add(discovered.getA());
                }
            }
            adjacentNodeList.add(s, adjacencies);
        }
    }

    void addNode(nodes nodes){
        nodesArrayList.add(nodes);
    }

    void addEdge(edges edges){
        edgesArrayList.add(edges);
    }
}
public class triangulate {
    private static ArrayList<nodes> realNode = new ArrayList<>();
    private static ArrayList<nodes> graphNode = new ArrayList<>();
    static Polygon polygon = new Polygon();
    private static void fileReader(String fileName){
        String line = null;
        try{
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int size = Integer.parseInt(bufferedReader.readLine());
            for(int i = 0; i < size; i++){
                line = bufferedReader.readLine();
                nodes node = new nodes(Double.parseDouble(line.split(" ")[0]), Double.parseDouble(line.split(" ")[1]));
                graphNode.add(node);
                polygon.addNode(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < graphNode.size() - 1; i++){
            edges newEdge = new edges(graphNode.get(i), graphNode.get(i + 1));
            polygon.addEdge(newEdge);
        }
        polygon.addEdge(new edges(graphNode.get(graphNode.size() - 1), graphNode.get(0)));

        polygon.setAdjacency();


    }

    public static void main(String[] args) {
        String fileName = "src/Triangulate.txt";
        fileReader(fileName);


    }
}
