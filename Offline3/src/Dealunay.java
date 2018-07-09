import java.io.*;
import java.util.ArrayList;

/**
 * Created by Ahmed on 13/06/2018 at 4:51 PM.
 */

class Point{
    private double x, y;
    int special;
    private boolean s = false;
    Point(){

    }
    Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    Point(int s){
        special = s;
        if(special == -1){
            x = 9999.0;
            y = -9999.0;
        }
        else{
            x = -9999.0;
            y = 9999.0;
        }
        this.s = true;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

    boolean getSpecial() { return s; }
}

class Edge{
    Point P1, P2;
    Edge(){
        this.P1 = new Point();
        this.P2 = new Point();
    }
    Edge(Point P1, Point P2){
        this.P1 = P1;
        this.P2 = P2;
    }
    Edge(double x1, double y1, double x2, double y2){
        this.P1 = new Point(x1, y1);
        this.P2 = new Point(x2, y2);
    }

}
class Triangle{
    ArrayList<Point> trianglePoints = new ArrayList<>();
    ArrayList<Edge> triangleLines = new ArrayList<>();
    ArrayList<edgeAndOpposite> opposites = new ArrayList<>();

    Triangle(){

    }

    void setOpposites(ArrayList<Triangle> triangles, int index){
        for (int i = 0; i < triangles.size(); i++){
            if(i != index){
                Triangle rev = triangles.get(i);
                for(int j = 0; j < 3; j++){
                    for(int k = 0; k < 3; k++){
                        if((this.triangleLines.get(j).P1.getX() == rev.triangleLines.get(k).P1.getX() && this.triangleLines.get(j).P1.getY() == rev.triangleLines.get(k).P1.getY()) ||(this.triangleLines.get(j).P1.getX() == rev.triangleLines.get(k).P1.getY() && this.triangleLines.get(j).P1.getY() == rev.triangleLines.get(k).P1.getX())){
                            if((this.triangleLines.get(j).P2.getX() == rev.triangleLines.get(k).P2.getX() && this.triangleLines.get(j).P2.getY() == rev.triangleLines.get(k).P2.getY()) || (this.triangleLines.get(j).P2.getX() == rev.triangleLines.get(k).P2.getY() && this.triangleLines.get(j).P2.getY() == rev.triangleLines.get(k).P2.getX())){
                                opposites.add(new edgeAndOpposite(this.triangleLines.get(j), rev));
                            }
                        }
                    }
                }
            }
        }
    }

    private String decide(int a, int b){
        if(trianglePoints.get(a).getSpecial() && trianglePoints.get(b).getSpecial()){
            return b + " " + b;
        }
        if(trianglePoints.get(a).getSpecial() && !trianglePoints.get(b).getSpecial()){
            if(trianglePoints.get(a).special == -1){
                return b + " " + a;
            }

            return a + " " + b;
        }

        if(!trianglePoints.get(a).getSpecial() && trianglePoints.get(b).getSpecial()){
            if(trianglePoints.get(b).special == -1){
                return a + " " + b;
            }

            return b + " " + a;
        }
        if(trianglePoints.get(a).getY() > trianglePoints.get(b).getY() || (trianglePoints.get(a).getY() == trianglePoints.get(b).getY() && trianglePoints.get(a).getX() > trianglePoints.get(b).getX())){
            return a + " " + b;
        }
        return b + " " + a;
    }

    private void setLines(){
        for(int i = 0; i < 3; i++){
            String d = decide(i, (i+1)%3);
            triangleLines.add(new Edge(trianglePoints.get(i), trianglePoints.get((i+1)%3)));
        }
    }

    Triangle(Point a, Point b, Point c){
        trianglePoints.add(a);
        trianglePoints.add(b);
        trianglePoints.add(c);
        setLines();
    }

}

class Triangulation{
    ArrayList<Triangle> triangles = new ArrayList<>();
    Triangulation(){

    }
    void fix(){
        for(int i = 0; i < triangles.size(); i++){
            triangles.get(i).setOpposites(triangles, i);
        }
    }
}

class edgeAndOpposite {
    Edge edge = new Edge();
    Triangle triangle = new Triangle();
    edgeAndOpposite(){

    }
    edgeAndOpposite(Edge edge, Triangle triangle){
        this.edge = edge;
        this.triangle = triangle;
    }
}

public class Dealunay {
    static Triangle init;
    //private static ArrayList<Triangle> triangulation = new ArrayList<>();
    static Triangulation triangulation = new Triangulation();
    private static ArrayList<Point> points = new ArrayList<>();
    private static void fileRead(String fileName){
        String line;
        try{
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int size = Integer.parseInt(bufferedReader.readLine());
            for(int i = 0; i < size; i++){
                line = bufferedReader.readLine();
                Point newPoint = new Point(Double.parseDouble(line.split(", ")[0]), Double.parseDouble(line.split(", ")[1]));
                points.add(newPoint);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static int index(Point point){
        int ind = -1;
        for(int i = 0; i < points.size(); i++){
            if((Double.compare(points.get(i).getX(), point.getX()) == 0 && Double.compare(points.get(i).getY(), point.getY()) == 0) || (Double.compare(points.get(i).getX(), point.getY()) == 0 && Double.compare(points.get(i).getY(), point.getX()) == 0)){
                ind = i;
                break;
            }
        }
        return ind;
    }

    private static int side(Point point, Edge edge)
    {
        if(!edge.P1.getSpecial() && !edge.P2.getSpecial()) {
            double a = (edge.P2.getX() - edge.P1.getX()) * (point.getY() - edge.P1.getY()) - (edge.P2.getY() - edge.P1.getY()) * (point.getX() - edge.P1.getX());
            if (Double.compare(a, 0.0000) > 0) { //left
                return 1;
            } else if (Double.compare(a, 0.0000) < 0) { //right
                return -1;
            }

            return 0;
        }
        else{
            /*if((edge.P1.getSpecial() && edge.P1.special != -2) || (edge.P2.getSpecial() && edge.P2.special != -1)){
                System.out.println("okokok + " + edge.P1.getX() + " " + edge.P2.getX());

                Point temp = edge.P1;
                edge.P1 = edge.P2;
                edge.P2 = temp;
            }*/

            if(edge.P1.getSpecial() && !edge.P2.getSpecial()){
                if(edge.P1.special == -2){
                    System.out.println("nop11");
                    Point lex = lexicograph(edge.P2, point);
                    if((lex.getX() == point.getX() && lex.getY() == point.getY()) || (lex.getX() == point.getY() && lex.getY() == point.getX())){
                        return 1;
                    }
                    if((lex.getX() == edge.P2.getX() && lex.getY() == edge.P2.getY()) || (lex.getX() == edge.P2.getY() && lex.getY() == edge.P2.getX())){
                        return -1;
                    }
                }

                else if(edge.P1.special == -1){
                    System.out.println("nop12");
                    Point lex = lexicograph(edge.P2, point);
                    if((lex.getX() == point.getX() && lex.getY() == point.getY()) || (lex.getX() == point.getY() && lex.getY() == point.getX())){
                        return -1;
                    }
                    if((lex.getX() == edge.P2.getX() && lex.getY() == edge.P2.getY()) || (lex.getX() == edge.P2.getY() && lex.getY() == edge.P2.getX())){
                        return 1;
                    }
                }
            }
            if(!edge.P1.getSpecial() && edge.P2.getSpecial()){
                if(edge.P2.special == -1){
                    System.out.println("nop21");
                    Point lex = lexicograph(edge.P1, point);
                    if((Double.compare(lex.getX(), point.getX()) == 0 && Double.compare(lex.getY(), point.getY()) == 0) || (Double.compare(lex.getX(), point.getY()) == 0 && Double.compare(lex.getY(), point.getX()) == 0)){
                        //System.out.println("yeah1");
                        return 1;
                    }
                    if((Double.compare(lex.getX(), edge.P1.getX()) == 0 && Double.compare(lex.getY(), edge.P1.getY()) == 0) || (Double.compare(lex.getX(), edge.P1.getY()) == 0 && Double.compare(lex.getY(), edge.P1.getX()) == 0)){
                        //System.out.println("yeah2");
                        return -1;
                    }
                }
                else if(edge.P2.special == -2){
                    System.out.println("nop22");
                    Point lex = lexicograph(edge.P1, point);
                    if((Double.compare(lex.getX(), point.getX()) == 0 && Double.compare(lex.getY(), point.getY()) == 0) || (Double.compare(lex.getX(), point.getY()) == 0 && Double.compare(lex.getY(), point.getX()) == 0)){
                        //System.out.println("yeah1");
                        return -1;
                    }
                    if((Double.compare(lex.getX(), edge.P1.getX()) == 0 && Double.compare(lex.getY(), edge.P1.getY()) == 0) || (Double.compare(lex.getX(), edge.P1.getY()) == 0 && Double.compare(lex.getY(), edge.P1.getX()) == 0)){
                        //System.out.println("yeah2");
                        return 1;
                    }
                }
            }
            else{
                return -1;
            }
            return 0;
        }
    }

    private static String triangleInsideOf(int n)
    {
        Point focus = points.get(n);
        //Point focus = new Point(4, 4);
        System.out.println(focus.getX() + " " + focus.getY());
        for(int i = 0; i < triangulation.triangles.size(); i++){
            boolean found = true;
            Triangle newTr = triangulation.triangles.get(i);
            if(triangulation.triangles.size() == 1){
                //System.out.println("inside");
                for(int j = 0; j < 3; j++){
                    if(newTr.trianglePoints.get(j).getX() != init.trianglePoints.get(j).getX() && newTr.trianglePoints.get(j).getY() != init.trianglePoints.get(j).getY()){
                        //return 0 + ", " + 1;
                        found = false;
                    }
                }
                if(found) return 0 + ", " + 1;
            }

            if(i == 0) continue;

            ArrayList<Edge> edges = newTr.triangleLines;
            System.out.println("pos edges are " + edges.get(0).P1.getX() + " " + edges.get(0).P1.getY() + " " + edges.get(0).P2.getX() + " " +edges.get(0).P2.getY());
            int pos = side(focus, edges.get(0));

            System.out.println("pos is " + pos);
            if(pos == 0) return i + ", " + pos + ", " + 0;
            for(int j = 1; j < edges.size(); j++){
                System.out.println("s edges are " + edges.get(j).P1.getX() + " " + edges.get(j).P1.getY() + " " + edges.get(j).P2.getX() + " " +edges.get(j).P2.getY());
                int s = side(focus, edges.get(j));
                System.out.println("s is " + s);
                if(s == 0) return i + ", " + s + ", " + j;
                if(s != pos){
                    found = false;
                }
            }
            if(found) {
                //System.out.println("here");
                return i + ", " + pos;
            }
        }
        return "-1";
    }

    private static int triangleIndex(Triangle triangle){
        for(int i = 0; i < triangulation.triangles.size(); i++){
            Triangle one = triangulation.triangles.get(i);
            boolean found = true;
            for(int j = 0; j < 3; j++){
                if(!one.triangleLines.get(j).equals(triangle.triangleLines.get(j))){
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    private static Point lexicograph(Point a, Point b){
        if(a.getY() > b.getY() || (a.getY() == b.getY() && a.getX() > b.getX())){
            return a;
        }
        return b;
    }

    private static Point circum(Triangle triangle){
        Edge e = triangle.triangleLines.get(0);
        Point mid1 = new Point((e.P1.getX() + e.P2.getX() / 2), ((e.P1.getY() + e.P2.getY()) / 2));
        double slope1 = (e.P2.getY() - e.P1.getY()) / (e.P2.getX() - e.P1.getX());
        double bisectorSlope1 = -1 / slope1;

        for (int k = 1; k < triangle.triangleLines.size(); k++) {
            if (e.P1.getX() == triangle.triangleLines.get(k).P1.getX() && e.P1.getY() == triangle.triangleLines.get(k).P1.getY() || (e.P1.getX() == triangle.triangleLines.get(k).P2.getX() && e.P1.getY() == triangle.triangleLines.get(k).P2.getY())) {
                System.out.println("not really");
                e = triangle.triangleLines.get(k);
            }
        }

        Point mid2 = new Point((e.P1.getX() + e.P2.getX() / 2), ((e.P1.getY() + e.P2.getY()) / 2));
        double slope2 = (e.P2.getY() - e.P1.getY()) / (e.P2.getX() - e.P1.getX());
        double bisectorSlope2 = -1 / slope2;

        double circumX = (bisectorSlope2 * mid2.getX() - mid2.getY() - bisectorSlope1 * mid1.getX() + mid1.getY()) / (bisectorSlope2 - bisectorSlope1);
        double circumY = bisectorSlope1 * (circumX - mid1.getX()) + mid1.getY();

        return new Point(circumX, circumY);
    }

    private static void legalizeEdge(Point thePoint, Point maxPoint, Edge edge)
    {
        if(edge.P1.getSpecial() && edge.P2.getSpecial()){
            return;
        }
        else if(edge.P1.getSpecial() && (edge.P2.getX() == maxPoint.getX() && edge.P2.getY() == maxPoint.getY()) || (edge.P2.getX() == maxPoint.getY() && edge.P2.getY() == maxPoint.getX())){
            return;
        }
        else if(edge.P2.getSpecial() && (edge.P1.getX() == maxPoint.getX() && edge.P1.getY() == maxPoint.getY()) || (edge.P1.getX() == maxPoint.getY() && edge.P1.getY() == maxPoint.getX())){
            return;
        }

        else{
            System.out.println("passed");
            ArrayList<Triangle> common = new ArrayList<>();
            ArrayList<Point> unique = new ArrayList<>();
            ArrayList<Point> overlap = new ArrayList<>();
            overlap.add(edge.P1);
            overlap.add(edge.P2);

            for(int i = 0; i < triangulation.triangles.size(); i++){
                Triangle one = triangulation.triangles.get(i);
                for(int j = 0; j < 3; j++){
                    System.out.println(edge.P1.getX() + " " + edge.P1.getY() + " " +  edge.P2.getX() + " " + edge.P2.getY()
                            + " " +one.triangleLines.get(j).P1.getX() + " "+ one.triangleLines.get(j).P1.getY() + " " +
                            one.triangleLines.get(j).P2.getX() + " "+ one.triangleLines.get(j).P2.getY());
                    //System.out.println("in 0");
                    if((edge.P1.getX() == one.triangleLines.get(j).P1.getX() && edge.P1.getY() == one.triangleLines.get(j).P1.getY()
                            && edge.P2.getX() == one.triangleLines.get(j).P2.getX() && edge.P2.getY() == one.triangleLines.get(j).P2.getY()) ||
                            (edge.P1.getX() == one.triangleLines.get(j).P2.getX() && edge.P1.getY() == one.triangleLines.get(j).P2.getY() &&
                                    edge.P2.getX() == one.triangleLines.get(j).P1.getX() && edge.P2.getY() == one.triangleLines.get(j).P1.getY())){
                        System.out.println("in 1");
                        common.add(one);
                        for(int k = 0; k < 3; k++){
                            if(one.trianglePoints.get(k).getX() != edge.P1.getX() && one.trianglePoints.get(k).getX() != edge.P2.getX()){
                                System.out.println("in 2");
                                unique.add(one.trianglePoints.get(k));
                            }
                        }
                    }
                }
            }

            boolean neg = false;
            for(int i = 0; i < unique.size(); i++){
                if(unique.get(i).getSpecial()) neg = true;
            }

            for(int i = 0; i < overlap.size(); i++){
                if(overlap.get(i).getSpecial()) neg = true;
            }

            if(!neg){
                Triangle triangle = common.get(0);
                Point circumCenter = circum(triangle);
                double circumRadius = Math.sqrt((circumCenter.getX() - triangle.trianglePoints.get(0).getX()) * (circumCenter.getX() - triangle.trianglePoints.get(0).getX()) + (circumCenter.getY() - triangle.trianglePoints.get(0).getY()) * (circumCenter.getY() - triangle.trianglePoints.get(0).getY()));

                Point aPoint = new Point();
                for (int k = 0; k < unique.size(); k++) {
                    if (unique.get(k).getX() != thePoint.getX() && unique.get(k).getY() != thePoint.getY()) {
                        aPoint.setX(unique.get(k).getX());
                        aPoint.setY(unique.get(k).getY());
                    }
                }
                double pointDistance = Math.sqrt((circumCenter.getX() - aPoint.getX()) * (circumCenter.getX() - aPoint.getX()) + (circumCenter.getY() - aPoint.getY()) * (circumCenter.getY() - aPoint.getY()));
                if(pointDistance < circumRadius){
                    /*illegal*/
                    ArrayList<Integer> indices = new ArrayList<>();
                    for(int i = 0; i < common.size(); i++){
                        indices.add(triangleIndex(common.get(i)));
                    }
                    for(int i = 0; i < indices.size(); i++){
                        if(indices.get(i) != -1){
                            System.out.println("1. removing " + triangulation.triangles.get((int) indices.get(i)).trianglePoints.get(0).getX() + " " + triangulation.triangles.get((int) indices.get(i)).trianglePoints.get(0).getY() + " " + triangulation.triangles.get((int) indices.get(i)).trianglePoints.get(1).getX() + " " + triangulation.triangles.get((int) indices.get(i)).trianglePoints.get(1).getY() + " " + triangulation.triangles.get((int) indices.get(i)).trianglePoints.get(2).getX() + " " + triangulation.triangles.get((int) indices.get(i)).trianglePoints.get(2).getY());
                            triangulation.triangles.remove((int) indices.get(i));
                            indices.remove(i);

                            for(int j = i; j < indices.size(); j++){
                                indices.set(j, indices.get(j) - 1);
                            }
                            i--;
                        }
                    }
                    if(unique.size() == 1){
                        triangulation.triangles.add(new Triangle(unique.get(0), overlap.get(0), overlap.get(1)));
                        triangulation.fix();
                    }
                    else{
                        triangulation.triangles.add(new Triangle(unique.get(0), overlap.get(0), unique.get(1)));
                        triangulation.triangles.add(new Triangle(unique.get(0), unique.get(1), overlap.get(1)));
                        triangulation.fix();

                        for(int i = 0; i < unique.size(); i++){
                            if (unique.get(i).getX() == thePoint.getX() && unique.get(i).getY() == thePoint.getY()){
                                unique.remove(i);
                                break;
                            }
                        }
                        legalizeEdge(thePoint, maxPoint, new Edge(overlap.get(0), unique.get(0)));
                        legalizeEdge(thePoint, maxPoint, new Edge(unique.get(0), overlap.get(1)));
                    }
                }
            }
            else {
                int val1 = 9999;
                for(int i = 0; i < unique.size(); i++){
                    if(unique.get(i).getSpecial()){
                        val1 = unique.get(i).special;
                    }
                }
                int val2 = -9999;
                for(int i = 0; i < overlap.size(); i++){
                    if(overlap.get(i).getSpecial()){
                        val2 = overlap.get(i).special;
                    }
                }

                if(val1 >= val2){
                    /*illegal*/
                    ArrayList<Integer> indices = new ArrayList<>();
                    for(int i = 0; i < common.size(); i++){
                        indices.add(triangleIndex(common.get(i)));
                    }
                    for(int i = 0; i < indices.size(); i++){
                        System.out.print(indices.get(i) + " ");
                        if(indices.get(i) != -1){
                            System.out.println("2. removing " + triangulation.triangles.get((int) indices.get(i)).trianglePoints.get(0).getX() + " " + triangulation.triangles.get((int) indices.get(i)).trianglePoints.get(0).getY() + " " + triangulation.triangles.get((int) indices.get(i)).trianglePoints.get(1).getX() + " " + triangulation.triangles.get((int) indices.get(i)).trianglePoints.get(1).getY() + " " + triangulation.triangles.get((int) indices.get(i)).trianglePoints.get(2).getX() + " " + triangulation.triangles.get((int) indices.get(i)).trianglePoints.get(2).getY());
                            triangulation.triangles.remove((int) indices.get(i));
                            indices.remove(i);

                            for(int j = i; j < indices.size(); j++){
                                indices.set(j, indices.get(j) - 1);
                            }
                            i--;
                        }
                    }

                    System.out.println();

                    if(unique.size() == 1){
                        triangulation.triangles.add(new Triangle(unique.get(0), overlap.get(0), overlap.get(1)));
                        triangulation.fix();
                    }
                    else{
                        System.out.println(unique.size() + " " + overlap.size());
                        triangulation.triangles.add(new Triangle(unique.get(0), overlap.get(0), unique.get(1)));
                        triangulation.triangles.add(new Triangle(unique.get(0), unique.get(1), overlap.get(1)));
                        triangulation.fix();

                        for(int i = 0; i < unique.size(); i++){
                            if (unique.get(i).getX() == thePoint.getX() && unique.get(i).getY() == thePoint.getY()){
                                unique.remove(i);
                                break;
                            }
                        }
                        legalizeEdge(thePoint, maxPoint, new Edge(overlap.get(0), unique.get(0)));
                        legalizeEdge(thePoint, maxPoint, new Edge(unique.get(0), overlap.get(1)));
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        String fileName = "src/1305074_testcase1.txt";
        fileRead(fileName);
        if(points != null){
            double maxCol = -9999.0;
            double maxRow = -9999.0;
            double minRow = 9999.0;

            Point maxPoint = new Point();

            for (Point point : points) {
                double Y = point.getY();
                if (Double.compare(Y, maxRow) > 0) {
                    maxRow = Y;
                    maxPoint = point;
                } else if (Y == maxRow) {
                    if (Double.compare(maxPoint.getX(), point.getX()) < 0) {
                        maxCol = point.getX();
                        maxPoint = new Point(maxCol, maxRow);
                    }
                }
            }

            maxCol = -9999.0;
            Point P_1 = new Point(-1);
            Point P_2 = new Point(-2);
            for (Point point : points) {
                if(Double.compare(point.getY(), maxCol) >= 0){
                    maxCol = point.getY();
                }
            }

            for (Point point : points) {
                if(Double.compare(point.getY(), maxCol) >= 0){
                    maxCol = point.getY();
                }
            }
            //System.out.println(maxPoint.getX() + " " + maxPoint.getY());

            init = new Triangle(maxPoint, P_1, P_2);
            int maxIndex = index(maxPoint);
            //System.out.println(maxIndex);
            points.remove(maxIndex);

            //Collections.shuffle(points);
            //System.out.println("TEST");
            //System.out.println(side(new Point(5, 3), new Edge(0, 0, 4, 4)));
            triangulation.triangles.add(init);
            points.add(0, maxPoint);
            for(int i = 1; i < points.size(); i++){
                System.out.println("index " + i + " triangulation size " + triangulation.triangles.size());
                String indexAndDirection = triangleInsideOf(i);
                System.out.println(indexAndDirection);
                int ind = Integer.parseInt(indexAndDirection.split(", ")[0]);
                //System.out.println(ind);
                Triangle outTriangle = triangulation.triangles.get(ind);
                int position = Integer.parseInt(indexAndDirection.split(", ")[1]);
                if(position != 0){
                    //System.out.println("problem not here");
                    Triangle one = new Triangle(outTriangle.trianglePoints.get(0), outTriangle.trianglePoints.get(1), points.get(i));
                    Triangle two = new Triangle(outTriangle.trianglePoints.get(1), outTriangle.trianglePoints.get(2), points.get(i));
                    Triangle three = new Triangle(outTriangle.trianglePoints.get(2), outTriangle.trianglePoints.get(0), points.get(i));

                    triangulation.triangles.add(one);
                    triangulation.triangles.add(two);
                    triangulation.triangles.add(three);
                    triangulation.fix();
                    /*legalize*/
                    legalizeEdge(points.get(i), maxPoint, new Edge(outTriangle.trianglePoints.get(0), outTriangle.trianglePoints.get(1)));
                    legalizeEdge(points.get(i), maxPoint, new Edge(outTriangle.trianglePoints.get(1), outTriangle.trianglePoints.get(2)));
                    legalizeEdge(points.get(i), maxPoint, new Edge(outTriangle.trianglePoints.get(2), outTriangle.trianglePoints.get(0)));
                }

                else{
                    //Point thirdPoint = outTriangle.trianglePoints.get(Integer.parseInt(indexAndDirection.split(" ")[2]));
                    System.out.println("problem here");
                    Edge edge = outTriangle.triangleLines.get(Integer.parseInt(indexAndDirection.split(", ")[2]));
                    ArrayList<Point> pointArrayList = new ArrayList<>();
                    for(int j = 0; j < 3; j++){
                        pointArrayList.add(outTriangle.trianglePoints.get(j));
                    }
                    //System.out.println(pointArrayList.size());
                    int jj = 0;
                    while (pointArrayList.size() > 1){
                        //System.out.println(jj);
                        if(edge.P1.getX() == pointArrayList.get(jj).getX() && edge.P1.getY() == pointArrayList.get(jj).getY() || edge.P2.getX() == pointArrayList.get(jj).getX() && edge.P2.getY() == pointArrayList.get(jj).getY()){
                            //System.out.println("before removal 3");
                            pointArrayList.remove(jj);
                            //jj--;
                        }
                        else jj++;

                        /*else if(edge.P2.getX() == pointArrayList.get(j).getX() && edge.P2.getY() == pointArrayList.get(j).getY()){
                            pointArrayList.remove(j);
                            //j--;
                        }*/
                    }

                    Point thirdPoint = pointArrayList.get(0);
                    Triangle one = new Triangle(points.get(i), thirdPoint, edge.P1);
                    Triangle two = new Triangle(points.get(i), edge.P2, thirdPoint);

                    Triangle oppositeTriangle = new Triangle();
                    for(int j = 0; j < outTriangle.opposites.size(); j++){
                        if((edge.P1.getX() == outTriangle.opposites.get(j).edge.P1.getX() && edge.P1.getY() == outTriangle.opposites.get(j).edge.P1.getY() && edge.P2.getX() == outTriangle.opposites.get(j).edge.P2.getX() && edge.P2.getY() == outTriangle.opposites.get(j).edge.P2.getY()) || (edge.P1.getX() == outTriangle.opposites.get(j).edge.P2.getX() && edge.P1.getY() == outTriangle.opposites.get(j).edge.P2.getY() && edge.P2.getX() == outTriangle.opposites.get(j).edge.P1.getX() && edge.P2.getY() == outTriangle.opposites.get(j).edge.P1.getY())){
                            oppositeTriangle = outTriangle.opposites.get(j).triangle;
                        }
                    }

                    //ArrayList<Point> pointArrayList1 = oppositeTriangle.trianglePoints;
                    ArrayList<Point> pointArrayList1 = new ArrayList<>();
                    for(int j = 0; j < 3; j++){
                        pointArrayList1.add(oppositeTriangle.trianglePoints.get(j));
                    }
                    jj = 0;
                    while (pointArrayList1.size() > 1){
                        if(edge.P1.getX() == pointArrayList1.get(jj).getX() && edge.P1.getY() == pointArrayList1.get(jj).getY() || edge.P2.getX() == pointArrayList1.get(jj).getX() && edge.P2.getY() == pointArrayList1.get(jj).getY()){
                            //System.out.println("before removal 4");
                            pointArrayList1.remove(jj);
                            //jj--;
                        }
                        else jj++;

                        /*else if(edge.P2.getX() == pointArrayList.get(j).getX() && edge.P2.getY() == pointArrayList.get(j).getY()){
                            pointArrayList.remove(j);
                            //j--;
                        }*/
                    }

                    Point sixthPoint = pointArrayList1.get(0);
                    System.out.println("sixth point " + sixthPoint.getX() + " " + sixthPoint.getY() + " " + sixthPoint.getSpecial());

                    Triangle three = new Triangle(points.get(i), edge.P1, sixthPoint);
                    Triangle four = new Triangle(points.get(i), sixthPoint, edge.P2);

                    triangulation.triangles.add(one);
                    triangulation.triangles.add(two);
                    triangulation.triangles.add(three);
                    triangulation.triangles.add(four);
                    triangulation.fix();
                    /*legalize*/
                    System.out.println("before legal 1");
                    legalizeEdge(points.get(i), maxPoint, new Edge(outTriangle.trianglePoints.get(0), sixthPoint));
                    System.out.println("before legal 2");
                    legalizeEdge(points.get(i), maxPoint, new Edge(sixthPoint, outTriangle.trianglePoints.get(1)));
                    legalizeEdge(points.get(i), maxPoint, new Edge(outTriangle.trianglePoints.get(1), outTriangle.trianglePoints.get(2)));
                    legalizeEdge(points.get(i), maxPoint, new Edge(outTriangle.trianglePoints.get(2), outTriangle.trianglePoints.get(0)));
                }

                //if(ind != 0) triangulation.triangles.remove(ind);
            }

            for(int i = 0; i < points.size(); i++){
                if(points.get(i).getSpecial()){
                    points.remove(i);
                }
            }
            triangulation.triangles.remove(0);

            for(int j = 0; j < triangulation.triangles.size(); j++){
                Triangle triangle = triangulation.triangles.get(j);
                for(int k = 0; k < 3; k++){
                    if(triangle.trianglePoints.get(k).getSpecial()){
                        //System.out.println(triangle.trianglePoints.get(k).getX());
                        triangulation.triangles.remove(j);
                        j--;
                        break;
                    }
                }
            }
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/1305074_Delaunay_Output.txt"));
                for(int j = 0; j < triangulation.triangles.size(); j++) {
                    Triangle triangle = triangulation.triangles.get(j);
                    for(int i = 0; i < 3; i++){
                        String m = triangle.trianglePoints.get(i).getX() + ", " + triangle.trianglePoints.get(i).getY();
                        bufferedWriter.newLine();
                        bufferedWriter.write(m);
                    }
                    bufferedWriter.newLine();
                }
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
