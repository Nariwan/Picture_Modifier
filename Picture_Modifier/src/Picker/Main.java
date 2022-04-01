package Picker;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.opencv.core.Core;

import static Picker.DataChooseFunction.Window_Is_resized;

public class Main extends Application {

    public static Window Window_FX_Var;
    public static Scene Scene_FX_Var;
    public static Pane Scene_FX_Pane;

    @Override
    public void start(Stage primaryStage) {

        double Window_Height = (2*Screen.getPrimary().getBounds().getHeight())/3;
        double Window_Width = (2*Screen.getPrimary().getBounds().getWidth())/3;

        primaryStage.setTitle("Picture Modifier!");
        // OpenCV Startup thing
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Scene_FX_Pane.prefHeight(Window_Height);
        Scene_FX_Pane.prefWidth(Window_Width);


        Scene_FX_Var = new Scene(Scene_FX_Pane, Window_Width,Window_Height);
        Scene_FX_Var.getStylesheets().add("Style.css");

        primaryStage.setScene(Scene_FX_Var);
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
        primaryStage.setResizable(true);
        primaryStage.show();
        // If the Windows is resized, call this Method
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> Window_Is_resized());
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> Window_Is_resized());

        Window_FX_Var = Scene_FX_Var.getWindow();
        Window_FX_Var.sizeToScene();


        new DataChooseFunction();


    }

    public static void main(String[] args) {
        Scene_FX_Pane = new Pane();
        launch(args);
    }
}
