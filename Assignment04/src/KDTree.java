import javafx.util.Pair;

import java.io.*;
import java.util.*;

/**
 * Created by Ahmed on 2/07/2018 at 10:21 PM.
 */

class Node{
    double x;
    double y;
    Node(){

    }
    Node(double x, double y){
        this.x = x;
        this.y = y;
    }
}

class compositeCoordinate implements Comparable
{
    double x, y;
    compositeCoordinate(){

    }

    compositeCoordinate(double x, double y){
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Object o) {
        compositeCoordinate compositeCoordinate = (compositeCoordinate) o;
        if(this.x < compositeCoordinate.x || (this.x == compositeCoordinate.x && this.y < compositeCoordinate.y)) {
            return -1;
        }
        return 1;
    }
}
class compositeNode{
    compositeCoordinate x;
    compositeCoordinate y;
    compositeNode(){

    }
    compositeNode(double x, double y){
        this.x = new compositeCoordinate(x, y);
        this.y = new compositeCoordinate(y, x);
    }
}

class treeNode<T extends Comparable<?>>
{
    treeNode left = null, right = null;
    compositeCoordinate node;
    treeNode(){
        node = new compositeCoordinate();
    }

    treeNode(compositeCoordinate node){
        this.node = node;
    }

    treeNode(double x, double y){
        this.node = new compositeCoordinate(x, y);
    }
}

class pointDepth implements Comparable
{
    compositeCoordinate compositeCoordinate = new compositeCoordinate();
    int depth;

    pointDepth(){
        compositeCoordinate = new compositeCoordinate();
        depth = 0;
    }

    pointDepth(compositeCoordinate compositeCoordinate, int depth){
        this.compositeCoordinate = compositeCoordinate;
        this.depth = depth;
    }

    pointDepth(double x, double y, int depth){
        this.compositeCoordinate = new compositeCoordinate(x, y);
        this.depth = depth;
    }

    @Override
    public int compareTo(Object o) {
        pointDepth pointDepth = (pointDepth) o;
        return (pointDepth.depth < this.depth)?1:-1;
    }
}

public class KDTree {
    static ArrayList<pointDepth> pointDepthArrayList = new ArrayList<>();
    static ArrayList<Node> nodeArrayList = new ArrayList<>();
    static ArrayList<compositeNode> compositeNodeArrayList = new ArrayList<>();
    static ArrayList<Double> XList = new ArrayList<>();
    static ArrayList<Double> YList = new ArrayList<>();
    static ArrayList<compositeCoordinate> XCompositeList = new ArrayList<>();
    static ArrayList<compositeCoordinate> YCompositeList = new ArrayList<>();
    private static int l = 0;
    private static void fileRead()
    {
        String line;
        try{
            FileReader fileReader = new FileReader("src/1305074_testCase2.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            int size = Integer.parseInt(line);
            for(int i = 0; i < size; i++){
                line = bufferedReader.readLine();
                XList.add(Double.parseDouble(line.split(" ")[0]));
                YList.add(Double.parseDouble(line.split(" ")[1]));
                XCompositeList.add(new compositeCoordinate(Double.parseDouble(line.split(" ")[0]), Double.parseDouble(line.split(" ")[1])));
                YCompositeList.add(new compositeCoordinate(Double.parseDouble(line.split(" ")[1]), Double.parseDouble(line.split(" ")[0])));
                Node node = new Node(Double.parseDouble(line.split(" ")[0]), Double.parseDouble(line.split(" ")[1]));
                compositeNode compositeNode = new compositeNode(Double.parseDouble(line.split(" ")[0]), Double.parseDouble(line.split(" ")[1]));
                nodeArrayList.add(node);
                compositeNodeArrayList.add(compositeNode);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static treeNode buildTree(ArrayList<compositeCoordinate> XNodes, int depth)
    {
        if(XNodes.size() == 1){
            return new treeNode(XNodes.get(0));
        }

        ArrayList<compositeCoordinate> P1 = new ArrayList<>();
        ArrayList<compositeCoordinate> P2 = new ArrayList<>();
        //int l = 0;
        if(depth % 2 == 0){
            Collections.sort(XNodes);
            l = (int) (Math.ceil((double)XNodes.size()/2) - 1);
            //System.out.println(depth + " " + XNodes.get(l).x + " " + XNodes.get(l).y);
            for(int i = 0; i <= l; i++){
                P1.add(XNodes.get(i));
            }
            for(int i = l + 1; i < XNodes.size(); i++){
                P2.add(XNodes.get(i));
            }
        }

        else{
            ArrayList<compositeCoordinate> UDown = new ArrayList<>();
            for(int i = 0; i < XNodes.size(); i++){
                UDown.add(new compositeCoordinate(XNodes.get(i).y, XNodes.get(i).x));
            }
            Collections.sort(UDown);

            l = (int) (Math.ceil((double)UDown.size()/2) - 1);
            //System.out.println("TEST " + Math.ceil(2.1));
            //System.out.println(l);
            /*for(int i = 0; i < UDown.size(); i++){
                System.out.print(UDown.get(i).x + " " + UDown.get(i).y + ", ");
            }
            System.out.println();*/

            for(int i = 0; i < UDown.size(); i++){
                double temp = UDown.get(i).x;
                UDown.get(i).x = UDown.get(i).y;
                UDown.get(i).y = temp;
            }
            //System.out.println(depth + " " + UDown.get(l).x + " " + UDown.get(l).y);
            for(int i = 0; i <= l; i++){
                P1.add(UDown.get(i));
            }
            //System.out.println(P1.get(P1.size() - 1).x + " " + P1.get(P1.size() - 1).y);
            for(int i = l + 1; i < XNodes.size(); i++){
                P2.add(UDown.get(i));
            }
        }

        //System.out.println("length " + P1.size() + " " + P2.size());
        //System.out.println(P1.get(0).x + " " + P2.get(0).x);
        pointDepthArrayList.add(new pointDepth(P1.get(l), depth));
        treeNode vLeft = buildTree(P1, depth + 1);
        //System.out.println("vLeft "+vLeft.node.x + " " + vLeft.node.y);
        treeNode vRight = buildTree(P2, depth + 1);
        //System.out.println("vRight "+vRight.node.x + " " + vRight.node.y);

        //System.out.println("l is " + l + " depth is " + depth);

        treeNode v = new treeNode(XNodes.get(l));
        //System.out.println("node is " + v.node.x + " " + v.node.y);
        v.left = vLeft;
        v.right = vRight;

        //System.out.println(v.node.x + " " + v.left.node.x);
        return v;

    }

    /*private static void printPreOrder(treeNode V){
        if(V == null){
            return;
        }
        System.out.println(V.node.x + " " + V.node.y);
        System.out.print("left ");
        printPreOrder(V.left);
        System.out.println();
        System.out.print("Right ");
        printPreOrder(V.right);
        System.out.println();
    }*/

    public static void main(String[] args) {
        fileRead();
        Collections.sort(XList);
        Collections.sort(YList);
        //Collections.sort(XCompositeList);
        Collections.sort(YCompositeList);

        /*for(int i = 0; i < XCompositeList.size(); i++){
            System.out.print(XCompositeList.get(i).x + " " + XCompositeList.get(i).y + ", ");
        }
        System.out.println();*/

        treeNode V = buildTree(XCompositeList, 0);
        //Collections.sort(pointDepthArrayList);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/1305074_buildOutput.txt", true));
            for(int i = 0; i < pointDepthArrayList.size(); i++) {
                pointDepth pointDepth = pointDepthArrayList.get(i);
                String m = pointDepthArrayList.get(i).depth + ", " + pointDepthArrayList.get(i).compositeCoordinate.x + ", " + pointDepthArrayList.get(i).compositeCoordinate.y;
                bufferedWriter.write(m);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(V.node.x);
        int maxDepth = -9999;
        for(int i = 0; i < pointDepthArrayList.size(); i++){
            if(maxDepth < pointDepthArrayList.get(i).depth){
                maxDepth = pointDepthArrayList.get(i).depth;
            }
            System.out.print("(" + pointDepthArrayList.get(i).depth + ", " + pointDepthArrayList.get(i).compositeCoordinate.x + ", " + pointDepthArrayList.get(i).compositeCoordinate.y + "), ");
        }

        System.out.println();

        //System.out.println("\nprinting");
        //printPreOrder(V);

        //BTreePrinter.printNode(V);

        /*for(int i = 0; i < XList.size(); i++){
            System.out.print(XList.get(i) + " ");
        }
        System.out.println();

        for(int i = 0; i < YList.size(); i++){
            System.out.print(YList.get(i) + " ");
        }
        System.out.println();

        for(int i = 0; i < XCompositeList.size(); i++){
            System.out.print(XCompositeList.get(i).x + " " + XCompositeList.get(i).y + ", ");
        }
        System.out.println();

        for(int i = 0; i < YCompositeList.size(); i++){
            System.out.print(YCompositeList.get(i).x + " " + YCompositeList.get(i).y + ", ");
        }
        System.out.println();*/
    }
}
