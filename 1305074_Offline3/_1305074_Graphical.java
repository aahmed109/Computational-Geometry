import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ahmed on 20/04/2018 at 9:40 AM.
 */

public class _1305074_Graphical extends Application {
    @Override public void start(Stage stage) {
        ArrayList<Point> realNode = new ArrayList<>();
        ArrayList<Point> realNode1 = new ArrayList<>();
        try {
            String line;
            FileReader fileReader = new FileReader("src/1305074_Delaunay.txt");
            FileReader fileReader1 = new FileReader("src/1305074_testcase1.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
            line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                Point s = new Point(Double.parseDouble(line.split(", ")[0]), Double.parseDouble(line.split(", ")[1]));
                if(s.getX() == 9999.0 || s.getX() == -9999.0 || s.getY() == 9999.0 || s.getY() == 9999.0){}
                else realNode.add(s);
            }
            line = bufferedReader1.readLine();
            while((line = bufferedReader1.readLine()) != null){
                realNode1.add(new Point(Double.parseDouble(line.split(", ")[0]), Double.parseDouble(line.split(", ")[1])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        final LineChart<Number,Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        for (Point aRealNode : realNode) {
            series1.getData().add(new XYChart.Data<>(aRealNode.getX(), aRealNode.getY()));
        }
        series1.getData().add(new XYChart.Data<>(realNode.get(0).getX(), realNode.get(0).getY()));

        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();

        for (Point aRealNode : realNode1) {
            series2.getData().add(new XYChart.Data<>(aRealNode.getX(), aRealNode.getY()));
        }
        series2.getData().add(new XYChart.Data<>(realNode1.get(0).getX(), realNode1.get(0).getY()));

        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);

        Scene scene  = new Scene(lineChart,800,800);
        scene.getStylesheets().add(getClass().getResource("1305074_root.css").toExternalForm());
        lineChart.getData().addAll(series1, series2);
        lineChart.setLegendVisible(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}