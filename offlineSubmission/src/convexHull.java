import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.Stack;

import static java.lang.Math.*;

/**
 * Created by Ahmed on 15/04/2018 at 10:46 AM.
 */
class nodeArray{
    private int len = 1;
    private nodes[] nodes = new nodes[len];
    private double cost = 0.0;
    nodeArray(int len, nodes[] nodes){
        this.len = len;
        this.nodes = nodes;
        cost = measureCost();
        cost = Double.parseDouble(String.format("%.1f", cost));
    }

    int getLen(){
        return len;
    }

    nodes[] getNodes(){
        return nodes;
    }

    double distance(nodes node1, nodes node2){
        double xCord = abs(node1.getX() - node2.getX()) * abs(node1.getX() - node2.getX());
        double yCord = abs(node1.getY() - node2.getY()) * abs(node1.getY() - node2.getY());

        return sqrt(xCord + yCord);
    }
    private double measureCost(){
        double c = 0.0;
        for(int i = 0; i < len - 1; i++){
            c += distance(nodes[i], nodes[i + 1]);
        }
        return c;
    }
}

class nodes{
    private int X = 9999;
    private int Y = 9999;

    nodes(int X, int Y){
        this.X = X;
        this.Y = Y;
    }

    int getX(){
        return X;
    }

    int getY(){
        return Y;
    }
}


public class convexHull {
    private static Pair[] pairs;
    private static ArrayList<nodes> discard;
    /*private static int partition(int low, int high){
        Pair pivot = pairs[high];
        int i = (low-1);
        for (int j=low; j<high; j++)
        {
            assert pivot != null;
            if (Double.parseDouble(String.valueOf(pairs[j].getKey())) <= Double.parseDouble(String.valueOf(pivot.getKey())))
            {
                i++;

                Pair temp = pairs[i];
                pairs[i] = pairs[j];
                pairs[j] = temp;
            }
        }

        Pair temp = pairs[i+1];
        pairs[i+1] = pairs[high];
        pairs[high] = temp;

        return i+1;
    }
    private static void quickSort(int low, int high){
        if (low < high)
        {
            int pi = partition(low, high);

            quickSort(low, pi-1);
            quickSort(pi+1, high);
        }
    }

    private static void sorter(nodes source, nodeArray nodeArray){
        int j = 0;
        for(int i = 0; i < nodeArray.getLen(); i++){
            if(nodeArray.getNodes()[i].getX() != source.getX() || nodeArray.getNodes()[i].getY() != source.getY()){
                double xDiff = nodeArray.getNodes()[i].getX() - source.getX();
                double yDiff = nodeArray.getNodes()[i].getY() - source.getY();
                double degreeDiff = toDegrees(atan2(yDiff, xDiff));
                if(degreeDiff < 0.0){
                    degreeDiff += 360.0;
                }
                pairs[j++] = new Pair<>(degreeDiff, nodeArray.getNodes()[i]);
            }
        }
        quickSort(0, pairs.length - 1);
    }
    */

    private static void heapify(int n, int i)
    {
        int largest = i;  // Initialize largest as root
        int l = 2*i + 1;  // left = 2*i + 1
        int r = 2*i + 2;  // right = 2*i + 2

        // If left child is larger than root
        //System.out.println(l + " " + r + " " + largest);
        if (l < n && (double)pairs[l].getKey() > (double)pairs[largest].getKey())
            largest = l;

        // If right child is larger than largest so far
        if (r < n && (double)pairs[r].getKey() > (double)pairs[largest].getKey())
            largest = r;

        // If largest is not root
        if (largest != i)
        {
            Pair swap = pairs[i];
            pairs[i] = pairs[largest];
            pairs[largest] = swap;

            // Recursively heapify the affected sub-tree
            heapify(n, largest);
        }
    }

    private static void sorter2(nodes source, nodeArray nodeArray){
        int n = pairs.length;
        int j = 0;
        for(int i = 0; i < nodeArray.getLen(); i++){
            if(nodeArray.getNodes()[i].getX() != source.getX() || nodeArray.getNodes()[i].getY() != source.getY()){
                //System.out.println(j);
                double xDiff = nodeArray.getNodes()[i].getX() - source.getX();
                double yDiff = nodeArray.getNodes()[i].getY() - source.getY();
                double degreeDiff = toDegrees(atan2(yDiff, xDiff));
                if(degreeDiff < 0.0){
                    degreeDiff += 360.0;
                }
                pairs[j++] = new Pair<>(degreeDiff, nodeArray.getNodes()[i]);
            }
        }
        //System.out.println(j);

        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(n, i);

        // One by one extract an element from heap
        for (int i=n-1; i>=0; i--)
        {
            // Move current root to end
            Pair temp = pairs[0];
            pairs[0] = pairs[i];
            pairs[i] = temp;

            // call max heapify on the reduced heap
            heapify(i, 0);
        }
    }

    private static void discardWriter(ArrayList arrayList){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/P/1305074_Discard.txt"));
            for(int j = 0; j < arrayList.size(); j++) {
                nodes graphNode = (nodes) arrayList.get(j);
                String m = graphNode.getX() + " " + graphNode.getY() + "\n";
                bufferedWriter.write(m);
            }

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void fileWriter(Stack stack){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/P/1305074_Result.txt"));
            for(int j = 0; j < stack.size(); j++) {
                nodes graphNode = (nodes) stack.get(j);
                String m = graphNode.getX() + " " + graphNode.getY() + "\n";
                bufferedWriter.write(m);
            }

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static nodeArray fileReader(String filename) throws IOException {
        String line;

        nodeArray nodeArray = null;
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int i = 0;
            int size = Integer.parseInt(bufferedReader.readLine());
            nodes[] graphNode = new nodes[size];
            while ((line = bufferedReader.readLine()) != null) {
                graphNode[i++] = new nodes(Integer.parseInt(line.split(" ")[0]), Integer.parseInt(line.split(" ")[1]));
            }
            if(size < 3 && size > 0){
                PrintWriter writer = new PrintWriter("src/P/1305074_Discard.txt");
                writer.print("");
                writer.close();
                Stack stack = new Stack();
                for(int u = size - 1; u >= 0; u--){
                    stack.push(graphNode[u]);
                }
                fileWriter(stack);
                double len = 0.0;
                for (int u = 1; u < stack.size(); u++) {
                    nodes nodes1 = (nodes)stack.get(u - 1);
                    nodes nodes2 =(nodes)stack.get(u);
                    len += sqrt((abs(nodes1.getX() - nodes2.getX()) * abs(nodes1.getX() - nodes2.getX())) + (abs(nodes1.getY() - nodes2.getY()) * abs(nodes1.getY() - nodes2.getY())));
                }
                nodes nodes1 = (nodes)stack.get(0);
                nodes nodes2 =(nodes)stack.get(stack.size() - 1);
                len += sqrt((abs(nodes1.getX() - nodes2.getX()) * abs(nodes1.getX() - nodes2.getX())) + (abs(nodes1.getY() - nodes2.getY()) * abs(nodes1.getY() - nodes2.getY())));
                System.out.println(len);
                for (int s = 0; s < stack.size(); s++) {
                    nodes nodes = (nodes)stack.get(s);
                    System.out.println(nodes.getX() + " " + nodes.getY());
                }
                return null;
            }
            discard = new ArrayList<>(size);
            nodeArray = new nodeArray(size, graphNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nodeArray;
    }

    private static void remove(nodes removal){
        nodes nodes1 = new nodes(removal.getX(), removal.getY());
        for(int i = 0; i < pairs.length; i++){
            if(pairs[i] != null) {
                nodes nodes = (nodes) pairs[i].getValue();
                if (nodes.getX() == nodes1.getX() && nodes.getY() == nodes1.getY()) {
                    System.arraycopy(pairs, i + 1, pairs, i + 1 - 1, pairs.length - (i + 1));
                    pairs[pairs.length - 1] = null;
                    break;

                }
            }
        }
    }

    public static void main(String[] args) {

        String filename = "src/P/1305074_testCase1.txt";
        nodeArray nodeArray = null;
        try {
            nodeArray = fileReader(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(nodeArray != null) {
            int maxCol = -9999;
            int minRow;
            nodes minNode = new nodes(9999, 9999);
            for (int i = 0; i < nodeArray.getLen(); i++) {
                int X = nodeArray.getNodes()[i].getX();
                if (maxCol < X) {
                    maxCol = X;
                    minNode = nodeArray.getNodes()[i];
                } else if (maxCol == X) {
                    if (minNode.getY() > nodeArray.getNodes()[i].getY()) {
                        minRow = nodeArray.getNodes()[i].getY();
                        minNode = new nodes(maxCol, minRow);
                    }
                }

            }

            pairs = new Pair[nodeArray.getLen() - 1];
            for (int i = 0; i < pairs.length; i++) {
                pairs[i] = new Pair<>(0.0, new nodes(9999, 9999));
            }

            //sorter(minNode, nodeArray);
            sorter2(minNode, nodeArray);
            for (int i = 1; i < pairs.length; i++) {
                if (pairs[i - 1] != null && pairs[i] != null) {
                    if (((double) pairs[i - 1].getKey() == (double) pairs[i].getKey())) {
                        nodes removal1 = (nodes) pairs[i - 1].getValue();
                        nodes removal2 = (nodes) pairs[i].getValue();
                        if (nodeArray.distance(minNode, removal1) < nodeArray.distance(minNode, removal2)) {
                            remove(removal1);
                            discard.add(removal1);
                            i--;
                        } else {
                            remove(removal2);
                            discard.add(removal2);
                            i--;
                        }
                    }
                }
            }

        /*System.out.println("after remove");
        for (Pair pair : pairs) {
            if (pair != null) {
                nodes nodes = (nodes) pair.getValue();
                System.out.println((double) pair.getKey() + " - " + nodes.getX() + " " + nodes.getY());
            }
        }*/

            Stack<nodes> stack = new Stack<>();
            stack.push(minNode);
            stack.push((nodes) pairs[0].getValue());
            int i = 1;
            int j = 1;
            //System.out.println(pairs.length);
            while (j < pairs.length && pairs[j] != null) {
                //System.out.println(i);
                nodes stackNodes0 = stack.get(i - 1);
                nodes stackNodes1 = stack.get(i);
                nodes discoveredNodes = (nodes) pairs[j].getValue();
                int checkVal = ((stackNodes1.getY() - stackNodes0.getY()) * (discoveredNodes.getX() - stackNodes1.getX())) - ((discoveredNodes.getY() - stackNodes1.getY()) * (stackNodes1.getX() - stackNodes0.getX()));
                if (checkVal < 0) {
                    //System.out.println("at left " + discoveredNodes.getX() + " " + discoveredNodes.getY());
                    stack.push(discoveredNodes);
                    i++;
                    j++;
                } else {
                    nodes s = stack.peek();
                    discard.add(s);
                    stack.pop();
                    i--;
                }
            }

            double len = 0.0;
            for (int u = 1; u < stack.size(); u++) {
                len += nodeArray.distance(stack.get(u - 1), stack.get(u));
            }
            len += nodeArray.distance(stack.get(0), stack.get(stack.size() - 1));
            System.out.println(len);
            for (nodes aStack : stack) {
                System.out.println(aStack.getX() + " " + aStack.getY());
            }
            fileWriter(stack);
            discardWriter(discard);
        }
    }
}
