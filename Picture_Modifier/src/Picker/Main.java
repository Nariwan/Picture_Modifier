package Picker;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.opencv.core.Core;

public class Main extends Application {

    public static Window Window_FX_Var;
    public static Scene Scene_FX_Var;
    public static Pane Scene_FX_Pane;

    @Override
    public void start(Stage primaryStage) {


        primaryStage.setTitle("Picture Modifier!");
        // OpenCV Startup thing
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Scene_FX_Pane.styleProperty().set("-fx-background-color: BLACK; -fx-border-color: BLACK;");
        Scene_FX_Pane.prefHeight(300);
        Scene_FX_Pane.prefWidth(480);


        Scene_FX_Var = new Scene(Scene_FX_Pane, 300,480);


        primaryStage.setScene(Scene_FX_Var);
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.show();

        Window_FX_Var = Scene_FX_Var.getWindow();
        Window_FX_Var.sizeToScene();

        new DataChooseFunction();

    }


    public static void main(String[] args) {
        Scene_FX_Pane = new Pane();
        launch(args);
    }
}
