package PicModi;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.opencv.core.Core;

import static PicModi.Pic_Modi_Menu.refresh_menu;

public class Main extends Application {

    public static Window Window_FX_Var;
    public static Scene Scene_FX_Var;
    public static Pane Scene_FX_Pane;
    public static double Window_Height, Window_Width;

    @Override
    public void start(Stage primaryStage) {

        Window_Height = (2*Screen.getPrimary().getBounds().getHeight())/3;
        Window_Width = (2*Screen.getPrimary().getBounds().getWidth())/3.5;

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
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> refresh_menu());
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> refresh_menu());

        Window_FX_Var = Scene_FX_Var.getWindow();
        Window_FX_Var.sizeToScene();


        Pic_Modi_Menu Menu = new Pic_Modi_Menu();
        Pic_Modi_Artist Artist = new Pic_Modi_Artist();
        Menu.setArtist(Artist);


    }

    public static void main(String[] args) {
        Scene_FX_Pane = new Pane();
        launch(args);
    }
}
