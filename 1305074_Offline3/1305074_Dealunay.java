import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

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
                        if(this.triangleLines.get(j).P1.getX() == rev.triangleLines.get(k).P1.getX() && this.triangleLines.get(j).P1.getY() == rev.triangleLines.get(k).P1.getY()){
                            if(this.triangleLines.get(j).P2.getX() == rev.triangleLines.get(k).P2.getX() && this.triangleLines.get(j).P2.getY() == rev.triangleLines.get(k).P2.getY()){
                                opposites.add(new edgeAndOpposite(this.triangleLines.get(j), rev));
                            }
                        }
                    }
                }
            }
        }
    }

    private String decide(int a, int b){
        if(trianglePoints.get(a).getY() > trianglePoints.get(b).getY() || (trianglePoints.get(a).getY() == trianglePoints.get(b).getY() && trianglePoints.get(a).getX() > trianglePoints.get(b).getX())){
            return a + " " + b;
        }
        return b + " " + a;
    }

    private void setLines(){
        for(int i = 0; i < 2; i++){
            String d = decide(i, i+1);
            triangleLines.add(new Edge(trianglePoints.get(Integer.parseInt(d.split(" ")[0])), trianglePoints.get(Integer.parseInt(d.split(" ")[1]))));
        }
        String d = decide(2, 0);
        triangleLines.add(new Edge(trianglePoints.get(Integer.parseInt(d.split(" ")[0])), trianglePoints.get(Integer.parseInt(d.split(" ")[1]))));
        //triangleLines.add(new Edge((trianglePoints.get(0).getY()>trianglePoints.get(1).getY()?trianglePoints.get(0):(trianglePoints.get(1).getX()>trianglePoints.get(0).getX()?trianglePoints.get(0):trianglePoints.get(1))), (trianglePoints.get(0).getX()>trianglePoints.get(1).getX()?trianglePoints.get(0):trianglePoints.get(1))));
        //triangleLines.add(new Edge((trianglePoints.get(1).getX()<trianglePoints.get(2).getX()?trianglePoints.get(1):trianglePoints.get(2)), (trianglePoints.get(1).getX()>=trianglePoints.get(2).getX()?trianglePoints.get(1):trianglePoints.get(2))));
        //triangleLines.add(new Edge((trianglePoints.get(2).getX()<trianglePoints.get(0).getX()?trianglePoints.get(2):trianglePoints.get(0)), (trianglePoints.get(2).getX()>=trianglePoints.get(0).getX()?trianglePoints.get(2):trianglePoints.get(0))));
        //triangleLines.add(new Edge(trianglePoints.get(1), trianglePoints.get(2)));
        //triangleLines.add(new Edge(trianglePoints.get(2), trianglePoints.get(0)));
        //setOpposites();
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
            if(Double.compare(points.get(i).getX(), point.getX()) == 0 && Double.compare(points.get(i).getY(), point.getY()) == 0){
                ind = i;
                break;
            }
        }
        return ind;
    }

    private static int side(Point point, Edge edge)
    {
        /*if(edge.P1.getSpecial() && !edge.P2.getSpecial()){

        }*/
        if(!edge.P1.getSpecial() && !edge.P2.getSpecial()) {
            double a = (edge.P2.getX() - edge.P1.getX()) * (point.getY() - edge.P1.getY()) - (edge.P2.getY() - edge.P1.getY()) * (point.getX() - edge.P1.getX());
            if (Double.compare(a, 0.0000) > 0) {
                return 1;
            } else if (Double.compare(a, 0.0000) < 0) {
                return -1;
            }

            return 0;
        }
        else{
            if(edge.P1.getSpecial() && !edge.P2.getSpecial()){
//                if(edge.P1.special == -1){
//                    //System.out.println("nop");
//                    if(lexicograph(edge.P2, point).getX() == point.getX() && lexicograph(edge.P2, point).getY() == point.getY()){
//                        return -1;
//                    }
//                    if(lexicograph(edge.P2, point).getX() == edge.P2.getX() && lexicograph(edge.P2, point).getY() == edge.P2.getY()){
//                        return 1;
//                    }
//                }
                /*else*/if(edge.P1.special == -2){
                    if(lexicograph(edge.P2, point).getX() == point.getX() && lexicograph(edge.P2, point).getY() == point.getY()){
                        return 1;
                    }
                    if(lexicograph(edge.P2, point).getX() == edge.P2.getX() && lexicograph(edge.P2, point).getY() == edge.P2.getY()){
                        return -1;
                    }
                }
            }
            else if(!edge.P1.getSpecial() && edge.P2.getSpecial()){
                if(edge.P2.special == -1){
                    //System.out.println("nop");
                    if(lexicograph(edge.P1, point).getX() == point.getX() && lexicograph(edge.P1, point).getY() == point.getY()){
                        return -1;
                    }
                    if(lexicograph(edge.P1, point).getX() == edge.P2.getX() && lexicograph(edge.P1, point).getY() == edge.P2.getY()){
                        return 1;
                    }
                }
//                else if(edge.P2.special == -2){
//                    if(lexicograph(edge.P2, point).getX() == point.getX() && lexicograph(edge.P2, point).getY() == point.getY()){
//                        return 1;
//                    }
//                    if(lexicograph(edge.P2, point).getX() == edge.P2.getX() && lexicograph(edge.P2, point).getY() == edge.P2.getY()){
//                        return -1;
//                    }
//                }
            }
            else{
                return 1;
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
                System.out.println("inside");
                for(int j = 0; j < 3; j++){
                    if(newTr.trianglePoints.get(j).getX() != init.trianglePoints.get(j).getX() && newTr.trianglePoints.get(j).getY() != init.trianglePoints.get(j).getY()){
                        //return 0 + ", " + 1;
                        found = false;
                    }
                }
                if(found) return 0 + ", " + 1;
            }

            ArrayList<Edge> edges = newTr.triangleLines;
            int pos = side(focus, edges.get(0));
            System.out.println("pos is " + pos);
            if(pos == 0) return i + ", " + pos + ", " + 0;
            for(int j = 1; j < edges.size(); j++){
                int s = side(focus, edges.get(j));
                System.out.println("s is " + s);
                if(s == 0) return i + ", " + s + ", " + j;
                if(s != pos){
                    found = false;
                }
            }
            if(found) {
                System.out.println("here");
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

    private static void legalizeEdge(Point thePoint, Point maxPoint, Edge edge)
    {
        if(edge.P1.getSpecial() && edge.P2.getX() == maxPoint.getX() && edge.P2.getY() == maxPoint.getY()){
            return;
        }
        else if(edge.P2.getSpecial() && edge.P1.getX() == maxPoint.getX() && edge.P1.getY() == maxPoint.getY()){
            return;
        }
        else if(edge.P1.getSpecial() && edge.P2.getSpecial()){
            return;
        }
        else{
            ArrayList<Triangle> common = new ArrayList<>();
            ArrayList<Point> unique = new ArrayList<>();
            ArrayList<Point> overlap = new ArrayList<>();

            System.out.println(triangulation.triangles.size());
            for(int i = 0; i < triangulation.triangles.size(); i++) {
                ArrayList<Edge> one = triangulation.triangles.get(i).triangleLines;
                for (int j = 0; j < one.size(); j++) {
                    System.out.println(one.get(j).P2.getX() == edge.P2.getX());
                    if ((one.get(j).P1.getX() == edge.P1.getX() && one.get(j).P1.getY() == edge.P1.getY() && one.get(j).P2.getX() == edge.P2.getX() && one.get(j).P2.getY() == edge.P2.getY()) || (one.get(j).P2.getX() == edge.P1.getX() && one.get(j).P2.getY() == edge.P1.getY() && one.get(j).P1.getX() == edge.P2.getX() && one.get(j).P1.getY() == edge.P2.getY())) {
                        System.out.println("inside condition");
                        common.add(triangulation.triangles.get(i));
                        if (overlap.isEmpty()) {
                            overlap.add(edge.P1);
                            overlap.add(edge.P2);
                        }
                        System.out.println("size of triangulation " + triangulation.triangles.size());
                        for (int k = 0; k < 3; k++) {
                            System.out.println("size of trianglePoints " + triangulation.triangles.get(i).trianglePoints.size());
                            if (triangulation.triangles.get(i).trianglePoints.get(k).getX() != edge.P1.getX() && triangulation.triangles.get(i).trianglePoints.get(k).getY() != edge.P1.getY() && triangulation.triangles.get(i).trianglePoints.get(k).getX() != edge.P2.getX() && triangulation.triangles.get(i).trianglePoints.get(k).getY() != edge.P2.getY()) {
                                unique.add(triangulation.triangles.get(i).trianglePoints.get(k));
                            }
                        }
                    }
                }
                if (common.size() >= 2 && unique.size() > 1) {
                    boolean regular = true;

                    for (int j = 0; j < unique.size(); j++) {
                        if (unique.get(j).getSpecial()) {
                            regular = false;
                        }
                    }
                    for (int j = 0; j < overlap.size(); j++) {
                        if (overlap.get(j).getSpecial()) {
                            regular = false;
                        }
                    }

                    if (regular) {
                        //for(int j = 0; j < common.size(); j++){
                        Triangle triangle = common.get(0);

                        Edge e = triangle.triangleLines.get(0);
                        Point mid1 = new Point((e.P1.getX() + e.P2.getX() / 2), ((e.P1.getY() + e.P2.getY()) / 2));
                        double slope1 = (e.P2.getY() - e.P1.getY()) / (e.P2.getX() - e.P1.getX());
                        double bisectorSlope1 = -1 / slope1;

                        for (int k = 1; k < triangle.triangleLines.size(); k++) {
                            if (e.P1.getX() == triangle.triangleLines.get(k).P1.getX() && e.P1.getY() == triangle.triangleLines.get(k).P1.getY()) {
                                e = triangle.triangleLines.get(k);
                            } else if (e.P1.getX() == triangle.triangleLines.get(k).P2.getX() && e.P1.getY() == triangle.triangleLines.get(k).P2.getY()) {
                                e = triangle.triangleLines.get(k);
                            }
                        }


                        Point mid2 = new Point((e.P1.getX() + e.P2.getX() / 2), ((e.P1.getY() + e.P2.getY()) / 2));
                        double slope2 = (e.P2.getY() - e.P1.getY()) / (e.P2.getX() - e.P1.getX());
                        double bisectorSlope2 = -1 / slope2;

                        double circumX = (bisectorSlope2 * mid2.getX() - mid2.getY() - bisectorSlope1 * mid1.getX() + mid1.getY()) / (bisectorSlope2 - bisectorSlope1);
                        double circumY = bisectorSlope1 * (circumX - mid1.getX()) + mid1.getY();
                        double circumRadius = Math.sqrt((circumX - e.P1.getX()) * (circumX - e.P1.getX()) + (circumY - e.P1.getY()) * (circumY - e.P1.getY()));

                        Point aPoint = new Point();
                        for (int k = 0; k < unique.size(); k++) {
                            if (unique.get(k).getX() != thePoint.getX() && unique.get(k).getY() != thePoint.getY()) {
                                aPoint.setX(unique.get(k).getX());
                                aPoint.setY(unique.get(k).getY());
                            }
                        }
                        double pointDistance = Math.sqrt((circumX - aPoint.getX()) * (circumX - aPoint.getX()) + (circumY - aPoint.getY()) * (circumY - aPoint.getY()));
                        if (Double.compare(circumRadius, pointDistance) > 0) {
                            /*illegal. need to flip, don't know how to*/
                            int deleter1 = triangleIndex(common.get(0));
                            int deleter2 = triangleIndex(common.get(1));
                            //System.out.println("before removal");
                            triangulation.triangles.remove(deleter1);
                            triangulation.triangles.remove(deleter2);
                            triangulation.triangles.add(new Triangle(unique.get(0), unique.get(1), edge.P1));
                            triangulation.triangles.add(new Triangle(unique.get(0), unique.get(1), edge.P2));
                            triangulation.fix();
                        }
                        //}
                    } else {
                        int val1 = 0, val2 = 0;
                        if (unique.get(0).getSpecial()) {
                            val1 = unique.get(0).special;
                        } else if (unique.get(1).getSpecial()) {
                            val1 = unique.get(1).special;
                        }

                        if (edge.P1.getSpecial()) {
                            val2 = edge.P1.special;
                        } else if (edge.P2.getSpecial()) {
                            val2 = edge.P2.special;
                        }

                        if (val1 >= val2) {
                        /*illegal*/
                            int deleter1 = triangleIndex(common.get(0));
                            int deleter2 = triangleIndex(common.get(1));
                            System.out.println("before removal 2");
                            triangulation.triangles.remove(deleter1);
                            triangulation.triangles.remove(deleter2);
                            triangulation.triangles.add(new Triangle(unique.get(0), unique.get(1), edge.P1));
                            triangulation.triangles.add(new Triangle(unique.get(0), unique.get(1), edge.P2));
                            triangulation.fix();

                        }
                    /*Point a = lexicograph(unique.get(0), unique.get(1));
                    Point b = lexicograph(edge.P1, edge.P2);
                    Point c = lexicograph(a, b);
                    if(c.getX() != a.getX() && c.getY() != a.getY()){

                        int deleter1 = triangleIndex(common.get(0));
                        int deleter2 = triangleIndex(common.get(1));
                        triangulation.triangles.remove(deleter1);
                        triangulation.triangles.remove(deleter2);

                    }*/
                    }
                    Point otherPoint = (thePoint.getX() == unique.get(0).getX() && thePoint.getY() == unique.get(0).getY()) ? unique.get(1) : unique.get(0);
                    Edge otherEdge1;
                    Point lex1 = lexicograph(overlap.get(0), otherPoint);
                    if (lex1.getX() == overlap.get(0).getX() && lex1.getY() == overlap.get(0).getY()) {
                        otherEdge1 = new Edge(overlap.get(0), otherPoint);
                    }
                    else otherEdge1 = new Edge(otherPoint, overlap.get(0));
                    legalizeEdge(thePoint, maxPoint, otherEdge1);

                    Edge otherEdge2;
                    Point lex2 = lexicograph(overlap.get(1), otherPoint);
                    if (lex2.getX() == overlap.get(0).getX() && lex2.getY() == overlap.get(1).getY())
                        otherEdge2 = new Edge(overlap.get(1), otherPoint);
                    else otherEdge2 = new Edge(otherPoint, overlap.get(1));
                    legalizeEdge(thePoint, maxPoint, otherEdge2);
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
            System.out.println(maxPoint.getX() + " " + maxPoint.getY());

            init = new Triangle(maxPoint, P_1, P_2);
            int maxIndex = index(maxPoint);
            System.out.println(maxIndex);
            points.remove(maxIndex);

            Collections.shuffle(points);

            triangulation.triangles.add(init);
            points.add(0, maxPoint);
            for(int i = 1; i < points.size(); i++){
                String indexAndDirection = triangleInsideOf(i);
                System.out.println(indexAndDirection);
                int ind = Integer.parseInt(indexAndDirection.split(", ")[0]);
                //System.out.println(ind);
                Triangle outTriangle = triangulation.triangles.get(ind);
                int position = Integer.parseInt(indexAndDirection.split(", ")[1]);
                if(position != 0){
                    Triangle one = new Triangle(outTriangle.trianglePoints.get(0), outTriangle.trianglePoints.get(1), points.get(i));
                    Triangle two = new Triangle(outTriangle.trianglePoints.get(1), outTriangle.trianglePoints.get(2), points.get(i));
                    Triangle three = new Triangle(outTriangle.trianglePoints.get(2), outTriangle.trianglePoints.get(0), points.get(i));
                    /*consider removing the parent triangle*/
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

                    Edge edge = outTriangle.triangleLines.get(Integer.parseInt(indexAndDirection.split(", ")[2]));
                    ArrayList<Point> pointArrayList = new ArrayList<>();
                    for(int j = 0; j < 3; j++){
                        pointArrayList.add(outTriangle.trianglePoints.get(j));
                    }
                    //System.out.println(pointArrayList.size());
                    int jj = 0;
                    while (pointArrayList.size() > 1){
                        System.out.println(jj);
                        if(edge.P1.getX() == pointArrayList.get(jj).getX() && edge.P1.getY() == pointArrayList.get(jj).getY() || edge.P2.getX() == pointArrayList.get(jj).getX() && edge.P2.getY() == pointArrayList.get(jj).getY()){
                            System.out.println("before removal 3");
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
                    Triangle two = new Triangle(points.get(i), thirdPoint, edge.P2);

                    Triangle oppositeTriangle = new Triangle();
                    for(int j = 0; j < outTriangle.opposites.size(); j++){
                        if(edge.P1.getX() == outTriangle.opposites.get(j).edge.P1.getX() && edge.P1.getY() == outTriangle.opposites.get(j).edge.P1.getY() && edge.P2.getX() == outTriangle.opposites.get(j).edge.P2.getX() && edge.P2.getY() == outTriangle.opposites.get(j).edge.P2.getY()){
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
                            System.out.println("before removal 4");
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

                    Triangle three = new Triangle(points.get(i), sixthPoint, edge.P1);
                    Triangle four = new Triangle(points.get(i), sixthPoint, edge.P2);
                    /*consider deleting old triangle*/
                    triangulation.triangles.add(one);
                    triangulation.triangles.add(two);
                    triangulation.triangles.add(three);
                    triangulation.triangles.add(four);
                    triangulation.fix();
                    /*legalize*/
                    legalizeEdge(points.get(i), maxPoint, new Edge(outTriangle.trianglePoints.get(0), sixthPoint));
                    legalizeEdge(points.get(i), maxPoint, new Edge(sixthPoint, outTriangle.trianglePoints.get(1)));
                    legalizeEdge(points.get(i), maxPoint, new Edge(outTriangle.trianglePoints.get(1), outTriangle.trianglePoints.get(2)));
                    legalizeEdge(points.get(i), maxPoint, new Edge(outTriangle.trianglePoints.get(2), outTriangle.trianglePoints.get(0)));
                }
            }

            for(int i = 0; i < points.size(); i++){
                if(points.get(i).getSpecial()){
                    points.remove(i);
                }
            }
            triangulation.triangles.remove(0);

            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/1305074_Delaunay_Output.txt"));
                for(int j = 0; j < triangulation.triangles.size(); j++) {
                    Triangle triangle = triangulation.triangles.get(j);
                    for(int i = 0; i < 3; i++){
                        String m = triangle.trianglePoints.get(i).getX() + ", " + triangle.trianglePoints.get(i).getY() + "\n";
                        bufferedWriter.write(m);
                    }
                }
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
