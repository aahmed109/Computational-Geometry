import javafx.util.Pair;

import java.io.*;
import java.util.*;

import static java.lang.Math.round;

/**
 * Created by Ahmed on 7/05/2018 at 8:43 PM.
 */
class nodes{
    private double X = 9999.0;
    private double Y = 9999.0;

    nodes(double X, double Y){
        this.X = X;
        this.Y = Y;
    }

    double getX(){
        return X;
    }

    double getY(){
        return Y;
    }
}

class edge{
    private nodes A =  new nodes(9999.0, 9999.0);
    private nodes B = new nodes(9999.0, 9999.0);
    private double len = 0.0;
    private static double findLength(nodes a, nodes b){
        return Math.sqrt((a.getX() - b.getX()) * (a.getX() - b.getX()) + ((a.getY() - b.getY()) * (a.getY() - b.getY())));
    }

    edge(){
        A = new nodes(9999.0, 9999.0);
        B = new nodes(9999.0, 9999.0);
        len = findLength(A, B);
    }
    edge(nodes A, nodes B){
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
public class createMonotone {
    private static double sweepLine = 0.0;
    private static double trueSweepLine = 0.0;
    private static double lastNodeY = 0.0;
    private static ArrayList<ArrayList<edge>> polygons = new ArrayList<>();
    private static ArrayList<nodes> realNode = new ArrayList<>();
    private static ArrayList<nodes> graphNode = new ArrayList<>();
    private static ArrayList<nodes> start = new ArrayList<>();
    private static ArrayList<nodes> end = new ArrayList<>();
    private static ArrayList<nodes> split = new ArrayList<>();
    private static ArrayList<nodes> merge = new ArrayList<>();
    private static ArrayList<nodes> regular = new ArrayList<>();
    private static ArrayList<Pair<nodes, edge>> helper = new ArrayList<>();
    private static ArrayList<Pair<Integer, edge>> diagonals = new ArrayList<>();
    private static ArrayList<edge> bst = new ArrayList<>();

    private static double pointToLine(nodes point, edge edge){
        double Q = point.getX() - edge.getA().getX();
        double R = point.getY() - edge.getA().getY();
        double S = edge.getB().getX() - edge.getA().getX();
        double T = edge.getB().getY() - edge.getA().getY();
        double U = -T;

        double dot = Q * U + R * S;
        double lenSq = U * U + S * S;

        return dot * dot / lenSq;
    }
    private static double findLength(nodes a, nodes b){
        return Math.sqrt((a.getX() - b.getX()) * (a.getX() - b.getX()) + ((a.getY() - b.getY()) * (a.getY() - b.getY())));
    }

    private static nodes lineLineIntersection(nodes A, nodes B, nodes C, nodes D){
        double a1 = B.getY() - A.getY();
        double b1 = A.getX() - B.getX();
        double c1 = a1 * A.getX() + b1 * A.getY();

        double a2 = D.getY() - C.getY();
        double b2 = C.getX() - D.getX();
        double c2 = a2 * C.getX() + b2 * C.getY();

        double determinant = a1 * b2 - a2 * b1;

        if (determinant == 0){
            return new nodes(9999.0, 9999.0);
        }

        else{
            double x = (b2 * c1 - b1 * c2) / determinant;
            double y = (a1 * c2 - a2 * c1) / determinant;
            return new nodes(x, y);
        }
    }

    private static void setHelper(edge edge, nodes node){
        helper.add(new Pair<>(node, edge));
    }
    private static void sorter(ArrayList<nodes> arrayList){
        arrayList.sort(Comparator.comparingDouble(nodes::getY));
        Collections.reverse(arrayList);

        for(int i = 0; i < arrayList.size() - 1; i++){
            if(Double.compare(arrayList.get(i).getY(), arrayList.get(i + 1).getY()) == 0){
                if(Double.compare(arrayList.get(i).getX(), arrayList.get(i + 1).getX()) > 0){
                    nodes temp = arrayList.get(i);
                    arrayList.set(i, arrayList.get(i + 1));
                    arrayList.set(i + 1, temp);
                }
            }
        }

        if(Double.compare(arrayList.get(arrayList.size() - 1).getY(), arrayList.get(arrayList.size() - 2).getY()) == 0){
            if(Double.compare(arrayList.get(arrayList.size() - 2).getX(), arrayList.get(arrayList.size() - 1).getX()) > 0){
                nodes temp = arrayList.get(arrayList.size() - 2);
                arrayList.set(arrayList.size() - 2, arrayList.get(arrayList.size() - 1));
                arrayList.set(arrayList.size() - 1, temp);
            }
        }
    }

    private static void startHandle(nodes theNode){ //checked and okay
        nodes newNode = new nodes(9999.0, 9999.0);
        int index = -1;
        for(int i = 0; i < realNode.size(); i++){
            if(Double.compare(realNode.get(i).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(i).getY(), theNode.getY()) == 0){
                index = i;
                break;
            }
        }

        if(index == realNode.size() - 1) {
            newNode = realNode.get(0);
        }
        else newNode = realNode.get(index + 1);

        System.out.println("index is " + index);
        edge newEdge = new edge(theNode, newNode);
        bst.add(newEdge);
        setHelper(newEdge, theNode);
    }

    private static void endHandle(nodes theNode){
        nodes newNode = new nodes(9999.0, 9999.0);
        int index = -1;
        for(int i = 0; i < realNode.size(); i++){
            if(Double.compare(realNode.get(i).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(i).getY(), theNode.getY()) == 0){
                index = i;
                break;
            }
        }
        if(index == 0) {
            newNode = realNode.get(realNode.size() - 1);
        }
        else newNode = realNode.get(index - 1);

        edge newEdge = new edge(newNode, theNode);

        trueSweepLine = sweepLine;
        boolean found = false;
        nodes helperEI_1 = new nodes(9999.0, 9999.0);
        while(round(sweepLine * 10.0) / 10.0 <= newEdge.getA().getY()){
            //System.out.println("yesyes");
            sweepLine += 0.1;
            for(int i = 0; i < realNode.size(); i++){
                if(Double.compare(realNode.get(i).getY(), round(sweepLine * 10.0) / 10.0) == 0){
                    System.out.println("nonono");
                    helperEI_1 = realNode.get(i);
                    found = true;
                    break;
                }
            }
            if(found) break;
        }

        if(found){
            System.out.println("y");
            System.out.println(helperEI_1.getX() + " " + helperEI_1.getY());
            for(int i = 0; i < merge.size(); i++) {
                if (Double.compare(merge.get(i).getX(), helperEI_1.getX()) == 0 && Double.compare(merge.get(i).getY(), helperEI_1.getY()) == 0) {
                    System.out.println("inside");
                    for(int j = 0; j < realNode.size(); j++){
                        if(Double.compare(realNode.get(j).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(j).getY(), theNode.getY()) == 0) {
                            diagonals.add(new Pair<>(j, new edge(theNode, helperEI_1)));
                            break;
                        }
                    }
                }
            }
        }
        bst.remove(newEdge);
        sweepLine = trueSweepLine;
    }

    private static void splitHandle(nodes theNode){
        System.out.print("split node: ");
        System.out.println(theNode.getX() + " " + theNode.getY());
        ArrayList<edge> upX = new ArrayList<>();
        edge anEdge = new edge();
        double min = 9999.0;
        int minIndex = -1;
        for(int i = 0; i < bst.size(); i++){
            anEdge = bst.get(i);
            System.out.print("value is ");
            //System.out.println(((theNode.getX() - anEdge.getA().getX()) * (anEdge.getA().getY() - anEdge.getB().getY()) - (anEdge.getA().getX() - anEdge.getB().getX()) * (theNode.getY() - anEdge.getA().getY())));
            if(theNode.getY() < anEdge.getA().getY() && theNode.getY() > anEdge.getB().getY() && ((theNode.getX() - anEdge.getA().getX()) * (anEdge.getA().getY() - anEdge.getB().getY()) - (anEdge.getA().getX() - anEdge.getB().getX()) * (theNode.getY() - anEdge.getA().getY())) > 0){
                upX.add(anEdge);
            }
        }

        System.out.println("size " + upX.size());
        for(int i = 0; i < upX.size(); i++){
            double dist = pointToLine(theNode, upX.get(i));
            if(dist < min){
                min = dist;
                minIndex = i;
            }
        }
        System.out.println("what?");
        System.out.println(minIndex);

        trueSweepLine = sweepLine;
        boolean found = false;
        edge newEdge = upX.get(minIndex);
        nodes helperEJ = new nodes(9999.0, 9999.0);
        while(round(sweepLine * 10.0) / 10.0 <= newEdge.getA().getY()){
            //System.out.println("yesyes");
            sweepLine += 0.1;
            for(int i = 0; i < realNode.size(); i++){
                if(Double.compare(realNode.get(i).getY(), round(sweepLine * 10.0) / 10.0) == 0){
                    System.out.println("nonono");
                    helperEJ = realNode.get(i);
                    found = true;
                    break;
                }
            }
            if(found) break;
        }

        if(found){
            System.out.println("y");
            System.out.println(helperEJ.getX() + " " + helperEJ.getY());
            for(int i = 0; i < merge.size(); i++) {
                if (Double.compare(merge.get(i).getX(), helperEJ.getX()) == 0 && Double.compare(merge.get(i).getY(), helperEJ.getY()) == 0) {
                    System.out.println("inside");
                    for(int j = 0; j < realNode.size(); j++){
                        if(Double.compare(realNode.get(j).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(j).getY(), theNode.getY()) == 0) {
                            diagonals.add(new Pair<>(j, new edge(theNode, helperEJ)));
                            break;
                        }
                    }
                }
            }
        }

        sweepLine = trueSweepLine;
        setHelper(upX.get(minIndex), theNode);
        int index, index1 = 0;
        for(int i = 0; i < realNode.size(); i++){
            if(Double.compare(realNode.get(i).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(i).getY(), theNode.getY()) == 0){
                index1 = i;
            }
        }
        if(index1 == realNode.size() - 1) index = 0;
        else index = index1 + 1;
        edge ei = new edge(theNode, realNode.get(index));
        bst.add(ei);
        setHelper(ei, theNode);

    }

    private static void mergeHandle(nodes theNode){
        nodes newNode = new nodes(9999.0, 9999.0);
        int index = -1;
        for(int i = 0; i < realNode.size(); i++){
            if(Double.compare(realNode.get(i).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(i).getY(), theNode.getY()) == 0){
                index = i;
                break;
            }
        }
        if(index == 0) {
            newNode = realNode.get(realNode.size() - 1);
        }
        else newNode = realNode.get(index - 1);

        edge newEdge = new edge(newNode, theNode);

        trueSweepLine = sweepLine;
        boolean found = false;
        nodes helperEI_1 = new nodes(9999.0, 9999.0);
        while(round(sweepLine * 10.0) / 10.0 <= newEdge.getA().getY()){
            sweepLine += 0.1;
            for(int i = 0; i < realNode.size(); i++){
                if(Double.compare(realNode.get(i).getY(), round(sweepLine * 10.0) / 10.0) == 0){
                    System.out.println("nonono");
                    helperEI_1 = realNode.get(i);
                    found = true;
                }
            }
            if(found) break;
        }

        if(found){
            System.out.println("y");
            System.out.println(helperEI_1.getX() + " " + helperEI_1.getY());
            for(int i = 0; i < merge.size(); i++) {
                if (Double.compare(merge.get(i).getX(), helperEI_1.getX()) == 0 && Double.compare(merge.get(i).getY(), helperEI_1.getY()) == 0) {
                    System.out.println("inside");
                    for(int j = 0; j < realNode.size(); j++){
                        if(Double.compare(realNode.get(j).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(j).getY(), theNode.getY()) == 0) {
                            diagonals.add(new Pair<>(j, new edge(theNode, helperEI_1)));
                            break;
                        }
                    }
                }
            }
        }
        bst.remove(newEdge);
        sweepLine = trueSweepLine;

        ArrayList<edge> upX = new ArrayList<>();
        edge anEdge = new edge();
        double min = 9999.0;
        int minIndex = -1;
        for(int i = 0; i < bst.size(); i++){
            anEdge = bst.get(i);
            System.out.print("value is ");
            //System.out.println(((theNode.getX() - anEdge.getA().getX()) * (anEdge.getA().getY() - anEdge.getB().getY()) - (anEdge.getA().getX() - anEdge.getB().getX()) * (theNode.getY() - anEdge.getA().getY())));
            if(theNode.getY() < anEdge.getA().getY() && theNode.getY() > anEdge.getB().getY() && ((theNode.getX() - anEdge.getA().getX()) * (anEdge.getA().getY() - anEdge.getB().getY()) - (anEdge.getA().getX() - anEdge.getB().getX()) * (theNode.getY() - anEdge.getA().getY())) > 0){
                upX.add(anEdge);
            }
        }

        System.out.println("size " + upX.size());
        for(int i = 0; i < upX.size(); i++){
            double dist = pointToLine(theNode, upX.get(i));
            if(dist < min){
                min = dist;
                minIndex = i;
            }
        }
        System.out.println("what?");
        System.out.println(minIndex);

        trueSweepLine = sweepLine;
        boolean foundJ = false;
        nodes helperEJ = new nodes(9999.0, 9999.0);
        while(round(sweepLine * 10.0) / 10.0 <= newEdge.getA().getY()){
            sweepLine += 0.1;
            for(int i = 0; i < realNode.size(); i++){
                if(Double.compare(realNode.get(i).getY(), round(sweepLine * 10.0) / 10.0) == 0){
                    System.out.println("nonono");
                    helperEJ = realNode.get(i);
                    foundJ = true;
                }
            }
            if(foundJ) break;
        }

        if(foundJ){
            System.out.println("y");
            System.out.println(helperEJ.getX() + " " + helperEJ.getY());
            for(int i = 0; i < merge.size(); i++) {
                if (Double.compare(merge.get(i).getX(), helperEJ.getX()) == 0 && Double.compare(merge.get(i).getY(), helperEJ.getY()) == 0) {
                    System.out.println("inside");
                    for(int j = 0; j < realNode.size(); j++){
                        if(Double.compare(realNode.get(j).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(j).getY(), theNode.getY()) == 0) {
                            diagonals.add(new Pair<>(j, new edge(theNode, helperEJ)));
                            break;
                        }
                    }
                }
            }
        }
        sweepLine = trueSweepLine;
        setHelper(upX.get(minIndex), theNode);

    }

    private static void regularHandle(nodes theNode){

        boolean right = true;
        nodes nodes = new nodes(9999.0, 9999.0);
        //System.out.println(realNode.indexOf(new nodes(theNode.getX(), theNode.getY())));
        int index = -1;
        for(int i = 0; i < realNode.size(); i++){
            if(Double.compare(realNode.get(i).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(i).getY(), theNode.getY()) == 0){
                index = i;
                break;
            }
        }
        if(index == 0) {
            nodes = realNode.get(realNode.size() - 1);
        }
        else nodes = realNode.get(index - 1);
        if(nodes.getY() < theNode.getY()) right = false;

        if(right){
            nodes newNode = new nodes(9999.0, 9999.0);
            int index1 = -1;
            for(int i = 0; i < realNode.size(); i++){
                if(Double.compare(realNode.get(i).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(i).getY(), theNode.getY()) == 0){
                    index1 = i;
                    break;
                }
            }
            if(index1 == 0) {
                newNode = realNode.get(realNode.size() - 1);
            }
            else newNode = realNode.get(index1 - 1);

            edge newEdge = new edge(newNode, theNode);

            trueSweepLine = sweepLine;
            boolean found = false;
            nodes helperEI_1 = new nodes(9999.0, 9999.0);
            while(round(sweepLine * 10.0) / 10.0 <= newEdge.getA().getY()){
                sweepLine += 0.1;
                for(int i = 0; i < realNode.size(); i++){
                    if(Double.compare(realNode.get(i).getY(), round(sweepLine * 10.0) / 10.0) == 0){
                        System.out.println("nonono");
                        helperEI_1 = realNode.get(i);
                        found = true;
                    }
                }
                if(found) break;
            }

            if(found){
                System.out.println("y");
                System.out.println(helperEI_1.getX() + " " + helperEI_1.getY());
                for(int i = 0; i < merge.size(); i++) {
                    if (Double.compare(merge.get(i).getX(), helperEI_1.getX()) == 0 && Double.compare(merge.get(i).getY(), helperEI_1.getY()) == 0) {
                        System.out.println("inside");
                        for(int j = 0; j < realNode.size(); j++){
                            if(Double.compare(realNode.get(j).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(j).getY(), theNode.getY()) == 0) {
                                diagonals.add(new Pair<>(j, new edge(theNode, helperEI_1)));
                                break;
                            }
                        }
                    }
                }
            }
            bst.remove(newEdge);
            sweepLine = trueSweepLine;

            int index2, index3 = 0;
            for(int i = 0; i < realNode.size(); i++){
                if(Double.compare(realNode.get(i).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(i).getY(), theNode.getY()) == 0){
                    index3 = i;
                }
            }
            if(index3 == realNode.size() - 1) index2 = 0;
            else index2 = index3 + 1;
            edge ei = new edge(theNode, realNode.get(index2));
            bst.add(ei);
            setHelper(ei, theNode);
        }

        else{
            nodes newNode = new nodes(9999.0, 9999.0);
            int index1 = -1;
            for(int i = 0; i < realNode.size(); i++){
                if(Double.compare(realNode.get(i).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(i).getY(), theNode.getY()) == 0){
                    index1 = i;
                    break;
                }
            }
            if(index1 == 0) {
                newNode = realNode.get(realNode.size() - 1);
            }
            else newNode = realNode.get(index1 - 1);

            edge newEdge = new edge(newNode, theNode);

            ArrayList<edge> upX = new ArrayList<>();
            edge anEdge = new edge();
            double min = 9999.0;
            int minIndex = -1;
            for(int i = 0; i < bst.size(); i++){
                anEdge = bst.get(i);
                System.out.print("value is ");
                //System.out.println(((theNode.getX() - anEdge.getA().getX()) * (anEdge.getA().getY() - anEdge.getB().getY()) - (anEdge.getA().getX() - anEdge.getB().getX()) * (theNode.getY() - anEdge.getA().getY())));
                if(theNode.getY() < anEdge.getA().getY() && theNode.getY() > anEdge.getB().getY() && ((theNode.getX() - anEdge.getA().getX()) * (anEdge.getA().getY() - anEdge.getB().getY()) - (anEdge.getA().getX() - anEdge.getB().getX()) * (theNode.getY() - anEdge.getA().getY())) > 0){
                    upX.add(anEdge);
                }
            }

            System.out.println("size " + upX.size());
            for(int i = 0; i < upX.size(); i++){
                double dist = pointToLine(theNode, upX.get(i));
                if(dist < min){
                    min = dist;
                    minIndex = i;
                }
            }
            System.out.println("what?");
            System.out.println(minIndex);

            trueSweepLine = sweepLine;
            boolean foundJ = false;
            nodes helperEJ = new nodes(9999.0, 9999.0);
            while(round(sweepLine * 10.0) / 10.0 <= newEdge.getA().getY()){
                sweepLine += 0.1;
                for(int i = 0; i < realNode.size(); i++){
                    if(Double.compare(realNode.get(i).getY(), round(sweepLine * 10.0) / 10.0) == 0){
                        System.out.println("nonono");
                        helperEJ = realNode.get(i);
                        foundJ = true;
                    }
                }
                if(foundJ) break;
            }

            if(foundJ){
                System.out.println("y");
                System.out.println(helperEJ.getX() + " " + helperEJ.getY());
                for(int i = 0; i < merge.size(); i++) {
                    if (Double.compare(merge.get(i).getX(), helperEJ.getX()) == 0 && Double.compare(merge.get(i).getY(), helperEJ.getY()) == 0) {
                        System.out.println("inside");
                        for(int j = 0; j < realNode.size(); j++){
                            if(Double.compare(realNode.get(j).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(j).getY(), theNode.getY()) == 0) {
                                diagonals.add(new Pair<>(j, new edge(theNode, helperEJ)));
                                break;
                            }
                        }
                    }
                }
            }
            sweepLine = trueSweepLine;
            setHelper(upX.get(minIndex), theNode);
        }
        /*if(right){
            System.out.println("in right baby " + theNode.getX() + " " + theNode.getY());
            nodes newNode = new nodes(9999.0, 9999.0);
            //System.out.println(realNode.indexOf(new nodes(theNode.getX(), theNode.getY())));
            int index1 = -1;
            for(int i = 0; i < realNode.size(); i++){
                if(Double.compare(realNode.get(i).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(i).getY(), theNode.getY()) == 0){

                    System.out.println("wow " + i);
                    index1 = i;
                    System.out.println(index1);
                    break;
                }
            }
            if(index1 == 0) {
                newNode = realNode.get(realNode.size() - 1);
            }
            else newNode = realNode.get(index1 - 1);

            edge newEdge = new edge(newNode, theNode);
            nodes find = new nodes(9999.0, 9999.0);
            for(int i = 0; i < helper.size(); i++){
                if(Double.compare(helper.get(i).getValue().getA().getX(), newEdge.getA().getX()) == 0 && Double.compare(helper.get(i).getValue().getA().getY(), newEdge.getA().getY()) == 0 && Double.compare(helper.get(i).getValue().getB().getX(), newEdge.getB().getX()) == 0 && Double.compare(helper.get(i).getValue().getB().getY(), newEdge.getB().getY()) == 0){
                    System.out.println("founddddddddddd");
                    find = helper.get(i).getKey();
                    if(Double.compare(find.getX(), 9999.0) != 0 && Double.compare(find.getY(), 9999.0) != 0){
                        for(int ii = 0; ii < merge.size(); ii++)
                            if(Double.compare(merge.get(ii).getX(),find.getX()) == 0 && Double.compare(merge.get(ii).getY(),find.getY()) == 0) { //merge.contains(find)
                                System.out.println("555");
                                int ind = -1;
                                for(int i2 = 0; i2 < realNode.size(); i2++){
                                    if(Double.compare(realNode.get(i2).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(i2).getY(), theNode.getY()) == 0){
                                        ind = i2;
                                        break;
                                    }
                                }
                                diagonals.add(new Pair<>(ind, new edge(theNode, find)));
                            }
                    }
                }
            }

            bst.remove(newEdge);

            int ind, ind2 = 0;
            for(int i2 = 0; i2 < realNode.size(); i2++){
                if(Double.compare(realNode.get(i2).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(i2).getY(), theNode.getY()) == 0){
                    ind2 = i2;
                    break;
                }
            }
            if(ind2 == realNode.size() - 1) ind = 0;
            else ind = ind2 + 1;
            System.out.println(ind);
            edge ei = new edge(theNode, realNode.get(ind));
            System.out.println("Adding:");
            System.out.println(ei.getA().getX() + " " + ei.getA().getY());
            System.out.println("And");
            System.out.println(ei.getB().getX() + " " + ei.getB().getY());
            bst.add(ei);
            setHelper(ei, theNode);
        }

        else{
            ArrayList<edge> upX = new ArrayList<>();
            edge anEdge = new edge();
            nodes intersect = new nodes(9999.0, 9999.0);
            double min = 9999.0;
            double minDist = 9999.0;
            int minIndex = -1;
            for(int i = 0; i < bst.size(); i++){
                anEdge = bst.get(i);
                if(theNode.getY() < anEdge.getA().getY() && theNode.getY() > anEdge.getB().getY() && ((anEdge.getA().getX() < theNode.getX() && anEdge.getB().getX() < theNode.getX()) || (anEdge.getA().getX() > theNode.getX() && anEdge.getB().getX() < theNode.getX()) || (anEdge.getA().getX() < theNode.getX() && anEdge.getB().getX() > theNode.getX()))){
                    upX.add(anEdge);
                }
            }

            for(int i = 0; i < realNode.size(); i++){
                if(Double.compare(realNode.get(i).getX(), min) < 0) min = realNode.get(i).getX();
            }
            edge edge = new edge(theNode, new nodes(min, theNode.getY()));

            for(int i = 0; i < upX.size(); i++){
                intersect = lineLineIntersection(edge.getA(), edge.getB(), upX.get(i).getA(), upX.get(i).getB());
                double dist = findLength(theNode, intersect);
                if(dist < minDist){
                    minDist = dist;
                    minIndex = i;
                }
            }

            if(minIndex > -1) {
                edge leftEdge = upX.get(minIndex);
                nodes helpee = new nodes(9999.0, 9999.0);
                for(int i = 0; i < helper.size(); i++){
                    if(helper.get(i).getValue().equals(leftEdge)){
                        helpee = helper.get(i).getKey();
                        if(Double.compare(helpee.getX(), 9999.0) != 0 && Double.compare(helpee.getY(), 9000.0) != 0 && merge.contains(helpee)){
                            System.out.println("666");
                            int ind = -1;
                            for(int ii = 0; ii < realNode.size(); ii++){
                                if(Double.compare(realNode.get(ii).getX(), theNode.getX()) == 0 && Double.compare(realNode.get(ii).getY(), theNode.getY()) == 0){
                                    ind = ii;
                                    break;
                                }
                            }
                            diagonals.add(new Pair<>(ind, new edge(theNode, helpee)));
                        }
                        helper.remove(i);
                        helper.add(i, new Pair<>(theNode, leftEdge));
                    }
                }

            }
        }*/

    }

    private static void workerFunc(nodes a, nodes b, nodes c, ArrayList<nodes> points, ArrayList<nodes> start, ArrayList<nodes> end, ArrayList<nodes> split, ArrayList<nodes> merge, ArrayList<nodes> regular){
        //System.out.println(a.getX() + " " + a.getY() + ", " + b.getX() + " " +  b.getY() + ", " + c.getX() + " " + c.getY());
        if(b.getY() > c.getY() && b.getY() > a.getY() || b.getY() >= c.getY() && b.getY() > a.getY() || b.getY() > c.getY() && b.getY() >= a.getY()){
            //System.out.println("1");
            if(b.getX() <= a.getX()){
                start.add(b);
            }
            else split.add(b);
        }
        /*else if(b.getY() >= c.getY() && b.getY() > a.getY() || b.getY() > c.getY() && b.getY() >= a.getY()){

        }*/
        else if(b.getY() < c.getY() && b.getY() < a.getY() || b.getY() <= c.getY() && b.getY() < a.getY() || b.getY() < c.getY() && b.getY() <= a.getY()){
            //System.out.println("2");
            if(b.getX() >= a.getX()){
                end.add(b);
            }
            else merge.add(b);
        }
        else regular.add(b);
    }
    private static void categorize(ArrayList<nodes> points, ArrayList<nodes> start, ArrayList<nodes> end, ArrayList<nodes> split, ArrayList<nodes> merge, ArrayList<nodes> regular){
        for(int i = 0; i < points.size(); i++){
            if(i == 0) workerFunc(points.get(points.size() - 1), points.get(i), points.get(i + 1), points, start, end, split, merge, regular);
            else if(i == points.size() - 1) workerFunc(points.get(points.size() - 2), points.get(points.size() - 1), points.get(0), points, start, end, split, merge, regular);
            else workerFunc(points.get(i - 1), points.get(i), points.get(i + 1), points, start, end, split, merge, regular);
        }

        System.out.println("Start");
        for (nodes aStart : start) {
            System.out.println(aStart.getX() + " " + aStart.getY());
        }

        System.out.println("End");
        for (nodes anEnd : end) {
            System.out.println(anEnd.getX() + " " + anEnd.getY());
        }

        System.out.println("Split");
        for (nodes aSplit : split) {
            System.out.println(aSplit.getX() + " " + aSplit.getY());
        }

        System.out.println("Merge");
        for (nodes aMerge : merge) {
            System.out.println(aMerge.getX() + " " + aMerge.getY());
        }

        System.out.println("Regular");
        for (nodes aRegular : regular) {
            System.out.println(aRegular.getX() + " " + aRegular.getY());
        }

        System.out.println();
    }

    private static void fileReader(String filename) throws IOException {
        String line = null;
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int size = Integer.parseInt(bufferedReader.readLine());

            for(int i = 0; i < size; i++) {
                line = bufferedReader.readLine();
                //System.out.println(line);
                nodes node = new nodes(Double.parseDouble(line.split(" ")[0]), Double.parseDouble(line.split(" ")[1]));
                graphNode.add(node);
                realNode.add(node);
            }

            categorize(realNode, start, end, split, merge, regular);

            sorter(graphNode);
            sorter(start);
            sorter(end);
            sorter(split);
            sorter(merge);
            sorter(regular);

            /*for (nodes aGraphNode : graphNode) {
                System.out.println(aGraphNode.getX() + " " + aGraphNode.getY());
            }*/

            sweepLine = start.get(0).getY();

            while(!graphNode.isEmpty()){
                for(int i = 0; i < graphNode.size(); i++){
                    nodes foundNode = new nodes(9999.0, 9999.0);
                    if(Double.compare(graphNode.get(i).getY(), round(sweepLine * 10.0) / 10.0) == 0) {
                        //System.out.println("wtf?");
                        foundNode = graphNode.get(i);

                        System.out.println(foundNode.getX() + " " + foundNode.getY());
                        if (i != 0) {
                            lastNodeY = graphNode.get(i - 1).getY();
                        }
                        for (int j = 0; j < start.size(); j++){
                            if (Double.compare(start.get(j).getX(), foundNode.getX()) == 0 && Double.compare(start.get(j).getY(), foundNode.getY()) == 0) {
                                System.out.println("1");
                                startHandle(foundNode);
                            }
                        }
                        for (int j = 0; j < end.size(); j++){
                            if (Double.compare(end.get(j).getX(), foundNode.getX()) == 0 && Double.compare(end.get(j).getY(), foundNode.getY()) == 0) {
                                System.out.println("2");
                                endHandle(foundNode);
                            }
                        }
                        for (int j = 0; j < split.size(); j++){
                            if (Double.compare(split.get(j).getX(), foundNode.getX()) == 0 && Double.compare(split.get(j).getY(), foundNode.getY()) == 0) {
                                System.out.println("3");
                                splitHandle(foundNode);
                            }
                        }
                        for (int j = 0; j < merge.size(); j++){
                            if (Double.compare(merge.get(j).getX(), foundNode.getX()) == 0 && Double.compare(merge.get(j).getY(), foundNode.getY()) == 0) {
                                System.out.println("4");
                                mergeHandle(foundNode);
                            }
                        }
                        for (int j = 0; j < regular.size(); j++){
                            if (Double.compare(regular.get(j).getX(), foundNode.getX()) == 0 && Double.compare(regular.get(j).getY(), foundNode.getY()) == 0) {
                                System.out.println("5");
                                regularHandle(foundNode);
                            }
                        }

                        /*if(start.contains(new nodes(foundNode.getX(), foundNode.getY()))){
                            System.out.println("mew");
                            startHandle(foundNode);
                        }
                        else if(end.contains(foundNode)){
                            endHandle(foundNode);
                        }
                        else if(split.contains(foundNode)){
                            System.out.println("hmm?");
                            splitHandle(foundNode);
                        }
                        else if(merge.contains(foundNode)){
                            mergeHandle(foundNode);
                        }
                        else if(regular.contains(foundNode)){
                            regularHandle(foundNode);
                        }*/
                        graphNode.remove(i);
                        i--;
                    }
                }
                sweepLine -= 0.1;
            }

            System.out.println("number of diagonals = " + diagonals.size());
            for(int i = 0; i < diagonals.size(); i++){
                System.out.println(diagonals.get(i).getValue().getA().getX() + ", " + diagonals.get(i).getValue().getA().getY() + " " + diagonals.get(i).getValue().getB().getX() + ", " + diagonals.get(i).getValue().getB().getY());
            }
            /*for (nodes aGraphNode : graphNode) {
                System.out.println(aGraphNode.getX() + " " + aGraphNode.getY());
            }*/
            ArrayList<nodes> copyNodes = new ArrayList<>();
            for(int i = 0; i < realNode.size(); i++){
                copyNodes.add(i, realNode.get(i));
            }
            int q = 0;


            ArrayList<edge> monotone = new ArrayList<>();
            for(int i = 0; i < diagonals.size() - 1; i++){
                for(int j = 0; j < realNode.size() - 1; j++){
                    edge diag = new edge(new nodes(9999.0, 9999.0), new nodes(9999.0, 9999.0));
                    if(j == diagonals.get(i).getKey()){
                        System.out.println("inside");
                        diag = diagonals.get(i).getValue();
                        edge newDiag = diag;
                        newDiag.setA(new nodes(Double.parseDouble("111" + diag.getA().getX()), diag.getA().getY()));

                        for(int k = j + 1; k < realNode.size(); k++){
                            if(!diag.getB().equals(realNode.get(k))) {
                                copyNodes.remove(realNode.get(k));
                                j++;
                            }
                        }
                        monotone.add(newDiag);
                    }
                    monotone.add(new edge(realNode.get(i), realNode.get(i+1)));
                }
                ArrayList<edge> temp = monotone;
                System.out.println(monotone.get(0).getA().getX());
                polygons.add(temp);
                //monotone.clear();
            }
            /*for(int i = 0; i < realNode.size() - 1; i++){
                for(int j = 0; j < diagonals.size(); j++){
                    edge diag = new edge(new nodes(9999.0, 9999.0), new nodes(9999.0, 9999.0));
                    if(i == diagonals.get(j).getKey()){
                        diag = diagonals.get(j).getValue();
                        edge newDiag = diag;
                        newDiag.setA(new nodes(Double.parseDouble("111" + diag.getA().getX()), diag.getA().getY()));
                        monotone.add(diag);
                        for(int k = 0; k < realNode.size(); k++){
                            if(!diag.getB().equals(realNode.get(k))) {
                                copyNodes.remove(realNode.get(k));
                                i++;
                            }
                        }
                    }
                }
                monotone.add(q++, new edge(realNode.get(i), realNode.get(i+1)));
                //System.out.println(monotone.get(0).getA().getX());
                polygons.add(monotone);
                //monotone.clear();
                //q = 0;
                System.out.println(polygons.get(0).get(0).getA().getX());
            }*/
            //String[] arguments = new String[]{"123"};
            System.out.println(polygons.size());
            for(int i = 0; i < polygons.size(); i++){
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/1305074_Result_" + i+1 + ".txt"));
                    for(int j = 0; j < polygons.get(i).size(); j++) {
                        ArrayList<edge> aPolygon = polygons.get(j);
                        String m = aPolygon.get(j).getA().getX() + " " + aPolygon.get(j).getA().getY() + "\n";
                        bufferedWriter.write(m);
                    }

                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Graphical graphical = new Graphical(polygons.get(i));

                //String[] arguments = new String[]{"123"};
                //Graphical.main(arguments);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String fileName = "src/1305074_testcase_1.txt";
        /*nodes a = new nodes(3, 2);
        nodes b = new nodes(4, 3);
        nodes c = new nodes(5, 2);
        System.out.println(findAngle(a, b, c));*/
        try {
            fileReader(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
