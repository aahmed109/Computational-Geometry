import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ahmed on 20/04/2018 at 9:40 AM.
 */

class depthAndCoordinate
{
    int depth;
    compositeCoordinate compositeCoordinate;
    depthAndCoordinate(int depth, compositeCoordinate compositeCoordinate){
        this.depth = depth;
        this.compositeCoordinate = compositeCoordinate;
    }
}

public class Graphical extends Application {
    @Override public void start(Stage stage) {
        Canvas canvas = new Canvas(500, 500);
        AnchorPane pane = new AnchorPane();
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        int level  = 40;

        ArrayList<compositeCoordinate> realNode = new ArrayList<>();
        ArrayList<depthAndCoordinate> treeNodeList = new ArrayList<>();
        ArrayList<compositeCoordinate> realNode1 = new ArrayList<>();
        File file = new File("src/1305074_buildOutput.txt");
        try {
            String line;
            FileReader fileReader = new FileReader("src/1305074_testcase1.txt");

            FileReader fileReader1 = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
            line = bufferedReader.readLine();
            int size = Integer.parseInt(line);
            int j = 0;
            while (j < size) {
                line = bufferedReader.readLine();
                compositeCoordinate s = new compositeCoordinate(Double.parseDouble(line.split(" ")[0]), Double.parseDouble(line.split(" ")[1]));
                realNode.add(s);
                j++;
            }

            if (file.getName().equals("1305074_buildOutput.txt")) {
                while ((line = bufferedReader1.readLine()) != null) {
                    treeNodeList.add(new depthAndCoordinate(Integer.parseInt(line.split(", ")[0]), new compositeCoordinate(Double.parseDouble(line.split(", ")[1]), Double.parseDouble(line.split(", ")[2]))));
                }
            } else {
                while ((line = bufferedReader1.readLine()) != null) {
                    realNode1.add(new compositeCoordinate(Double.parseDouble(line.split(", ")[0]), Double.parseDouble(line.split(", ")[1])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Line line = new Line();
        ArrayList<Line> lineArrayList = new ArrayList<>();
        if (file.getName().equals("1305074_buildOutput.txt")) {
            double maxY = -9999.0;
            double minY = 9999.0;
            double maxX = -9999.0;
            double minX = 9999.0;


            for (int i = 0; i < treeNodeList.size(); i++) {
                if (treeNodeList.get(i).compositeCoordinate.y > maxY) {
                    maxY = treeNodeList.get(i).compositeCoordinate.y;
                }
                if (treeNodeList.get(i).compositeCoordinate.y < minY) {
                    minY = treeNodeList.get(i).compositeCoordinate.y;
                }

                if (treeNodeList.get(i).compositeCoordinate.x > maxX) {
                    maxX = treeNodeList.get(i).compositeCoordinate.x;
                }
                if (treeNodeList.get(i).compositeCoordinate.x < minX) {
                    minX = treeNodeList.get(i).compositeCoordinate.x;
                }
            }

            System.out.println(maxX + " " + maxY + " " + minX + " " + minY);

            graphicsContext.setLineWidth(1.0);
            for (int i = 0; i < treeNodeList.size(); i++) {
                System.out.println(i);
                depthAndCoordinate depthAndCoordinate = treeNodeList.get(i);
                if (depthAndCoordinate.depth % 2 == 0) {
                    graphicsContext.setStroke(Color.RED);
                    if (depthAndCoordinate.depth == 0) {
                        graphicsContext.strokeLine(400 + depthAndCoordinate.compositeCoordinate.x * level, 400 - maxY * level, 400 + depthAndCoordinate.compositeCoordinate.x * level, 400 - minY * level);
                    }
                    else{
                        graphicsContext.strokeLine(400 + depthAndCoordinate.compositeCoordinate.x * level, 400 - treeNodeList.get(i-1).compositeCoordinate.y * level, 400 + depthAndCoordinate.compositeCoordinate.x * level, 400 - depthAndCoordinate.compositeCoordinate.y * level);
                    }
                }
                else {
                    graphicsContext.setStroke(Color.BLUE);
                    //if(depthAndCoordinate.compositeCoordinate.x < treeNodeList.get(i-1).compositeCoordinate.x){
                        graphicsContext.strokeLine(400 + treeNodeList.get(i-1).compositeCoordinate.x * level, 400 - depthAndCoordinate.compositeCoordinate.y * level, 400 + depthAndCoordinate.compositeCoordinate.x * level, 400 - depthAndCoordinate.compositeCoordinate.y * level);
                    //}

                    /*else{
                        graphicsContext.strokeLine(400 + treeNodeList.get(i-1).compositeCoordinate.x * level, 400 - depthAndCoordinate.compositeCoordinate.y * level, 400 + depthAndCoordinate.compositeCoordinate.x * level, 400 - depthAndCoordinate.compositeCoordinate.y * level);
                    }*/
                }

            }

            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();

            final LineChart<Number, Number> lineChart =
                    new LineChart<>(xAxis, yAxis);

            XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
            for (compositeCoordinate aRealNode : realNode) {
                series1.getData().add(new XYChart.Data<>(aRealNode.x, aRealNode.y));
            }
            series1.getData().add(new XYChart.Data<>(realNode.get(0).x, realNode.get(0).y));

            XYChart.Series<Number, Number> series2 = new XYChart.Series<>();

            for (depthAndCoordinate aRealNode : treeNodeList) {
                series2.getData().add(new XYChart.Data<>(aRealNode.compositeCoordinate.x, aRealNode.compositeCoordinate.y));
            }
            series2.getData().add(new XYChart.Data<>(treeNodeList.get(0).compositeCoordinate.x, treeNodeList.get(0).compositeCoordinate.y));

            lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);


            //Scene scene  = new Scene(lineChart,800,800);
            pane.getChildren().add(canvas);
            Scene scene = new Scene(pane, 800, 800);
            scene.getStylesheets().add(getClass().getResource("1305074_root.css").toExternalForm());

            //lineChart.getData().addAll(series1, series2);
            lineChart.setLegendVisible(false);
            stage.setScene(scene);
            stage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}