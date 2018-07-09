import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Ahmed on 20/04/2018 at 9:40 AM.
 */

public class Graphical extends Application {
    @Override public void start(Stage stage) {
        nodes[] realNode = new nodes[0];
        nodes[] realNode1 = new nodes[0];
        try {
            String line;
            FileReader fileReader = new FileReader("src/P/1305074_Result.txt");
            FileReader fileReader1 = new FileReader("src/P/1305074_Discard.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
            int i = 0;
            nodes[] graphNode = new nodes[100];
            while ((line = bufferedReader.readLine()) != null) {
                graphNode[i++] = new nodes(Integer.parseInt(line.split(" ")[0]), Integer.parseInt(line.split(" ")[1]));
            }
            realNode = new nodes[i];
            System.arraycopy(graphNode, 0, realNode, 0, i);
            int j = 0;
            nodes[] graphNode1 = new nodes[100];
            while((line = bufferedReader1.readLine()) != null){
                graphNode1[j++] = new nodes(Integer.parseInt(line.split(" ")[0]), Integer.parseInt(line.split(" ")[1]));
            }
            realNode1 = new nodes[j];
            System.arraycopy(graphNode1, 0, realNode1, 0, j);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        final LineChart<Number,Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        for (nodes aRealNode : realNode) {
            series1.getData().add(new XYChart.Data<>(aRealNode.getX(), aRealNode.getY()));
        }
        series1.getData().add(new XYChart.Data<>(realNode[0].getX(), realNode[0].getY()));

        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();

        for (nodes aRealNode : realNode1) {
            series2.getData().add(new XYChart.Data<>(aRealNode.getX(), aRealNode.getY()));
        }
        //series2.getData().add(new XYChart.Data<>(2, 2));
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