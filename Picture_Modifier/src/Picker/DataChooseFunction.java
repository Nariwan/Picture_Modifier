package Picker;


// Imported Libraries

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

import static Picker.Main.Scene_FX_Pane;


public class DataChooseFunction {

    // Some variables, IntelliJ might gonna nag of some,
    // but that's okay.

    private static boolean DataPickCriteria = false;
    private static Group DataGroup_For_FXPane;
    private static FileChooser DataChoose;
    private static File DataVar;
    private static Button Button_PickFile, Button_Modify, Button_For_Exit;
    private static Label sPath_Label, ThresDescription_Label, NameDesc_Label;
    private static boolean HasPic;

    public static TextField InputThresh_txtField, NameGiver_txtField;

    // Constructor of the Chooser
    public DataChooseFunction(){


        // Did the layout, window, already got created?
        if(!DataPickCriteria){


            // Text field to input the Threshold
            InputThresh_txtField = new TextField("100");
            InputThresh_txtField.setPrefWidth(50);
            InputThresh_txtField.setLayoutX((Scene_FX_Pane.getWidth()/2) - (InputThresh_txtField.getPrefWidth()/2));
            InputThresh_txtField.setLayoutY(200);


            // The input field of how to name the Output File
            NameGiver_txtField = new TextField("Test.png");
            NameGiver_txtField.setPrefWidth((Scene_FX_Pane.getWidth())-20);
            NameGiver_txtField.setLayoutX(10);
            NameGiver_txtField.setLayoutY(InputThresh_txtField.getLayoutY()+80);


            // Desc. for Input Label
            ThresDescription_Label = new Label("Threshold (0-255 best Values)");
            ThresDescription_Label.setLayoutX((Scene_FX_Pane.getWidth()/5));
            ThresDescription_Label.setLayoutY(InputThresh_txtField.getLayoutY()-25);
            ThresDescription_Label.setTextFill(Color.YELLOW);

            // Desc. for Save-Name Label
            NameDesc_Label = new Label("Save Name");
            NameDesc_Label.setLayoutX((Scene_FX_Pane.getWidth()/2)-50);
            NameDesc_Label.setLayoutY(NameGiver_txtField.getLayoutY()-25);
            NameDesc_Label.setTextFill(Color.YELLOW);


            /*
                The FileChooser Object gets created.
                After this some restrictions which kind of
                Data is allowed.
            */
            DataChoose = new FileChooser();

            DataChoose.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("*", "*.jpg","*.png","*.gif","*.bmp","*.tiff"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png"),
                    new FileChooser.ExtensionFilter("GIF", "*.gif"),
                    new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                    new FileChooser.ExtensionFilter("TIFF", "*.tiff")
            );

            DataChoose.setInitialDirectory(new File(System.getProperty("user.home")));


            /*
                Here's the Label which tells the user, if a File got
                chosen, and which file it is.
             */
            sPath_Label = new Label();
            sPath_Label.setText("NO FILE!!");
            sPath_Label.setTextFill(Color.YELLOW);
            sPath_Label.setLayoutX((Scene_FX_Pane.getWidth()/2)-20);
            sPath_Label.setLayoutY((Scene_FX_Pane.getHeight()/6));

            /*
                The button, and boolean, for the File picker.
             */
            HasPic = false;
            Button_PickFile = new Button();
            Button_PickFile.setText("Pick Image");
            Button_PickFile.setPrefWidth(100);
            Button_PickFile.setLayoutX((Scene_FX_Pane.getWidth() - Button_PickFile.getPrefWidth())/2);
            Button_PickFile.setLayoutY((Scene_FX_Pane.getHeight()/5));

            /*
                Now we get to the function of the Button, what happens
                when it got clicked?
                Well it opens a window in which the user can pick a file,
                if nothing got chosen, it returns "No file."
                If it has something, it sets the sPath_Label to the
                File-Path, and also remembers the path.
             */
            Button_PickFile.setOnAction(event -> {

                DataVar = DataChoose.showOpenDialog(Main.Window_FX_Var);
                if(DataVar == null){
                    System.out.println("No File!");
                    HasPic = false;
                } else {
                    //System.out.println(DataVar);
                    sPath_Label.setText(DataVar.toString());
                    sPath_Label.setLayoutX(10);
                    HasPic = true;
                    }
                });

            /*
                Here the button with the modify function
                gets created, and arranged in the Pane.
             */
            Button_Modify = new Button();
            Button_Modify.setText("Modify");
            Button_Modify.setPrefWidth(100);
            Button_Modify.setLayoutX((Scene_FX_Pane.getWidth() - Button_PickFile.getPrefWidth())/2);
            Button_Modify.setLayoutY((Scene_FX_Pane.getHeight()/4));

            /*
                What happens, if the Modify button gets clicked?
                Well..
                It checks again, if there's a picture available ( Might be
                redundant, but just to make sure. ), and then it starts the
                entire modifying process.
             */
            Button_Modify.setOnAction(event -> {

                // Wenn der Button gedrückt wird, erscheint das Dateiauswahlfenster.
                if (HasPic == false) {
                    System.out.println("No Pic.");
                } else {
                    Yay(DataVar);
                }
            });


            /*
                Creation, and setup, for the exit button.
             */
            Button_For_Exit = new Button();
            Button_For_Exit.setText("Exit");
            Button_For_Exit.setPrefWidth(100);
            Button_For_Exit.setLayoutX((Scene_FX_Pane.getWidth() - Button_For_Exit.getPrefWidth())/2);
            Button_For_Exit.setLayoutY(Scene_FX_Pane.getHeight()-100);

            /*
                If clicked -> Exit
             */
            Button_For_Exit.setOnAction(event -> System.exit(0));


            // All the former created objects are being put into Group.
            DataGroup_For_FXPane = new Group();
            DataGroup_For_FXPane.getChildren().add(Button_PickFile);
            DataGroup_For_FXPane.getChildren().add(Button_Modify);
            DataGroup_For_FXPane.getChildren().add(Button_For_Exit);
            DataGroup_For_FXPane.getChildren().add(sPath_Label);
            DataGroup_For_FXPane.getChildren().add(InputThresh_txtField);
            DataGroup_For_FXPane.getChildren().add(NameGiver_txtField);
            DataGroup_For_FXPane.getChildren().add(ThresDescription_Label);
            DataGroup_For_FXPane.getChildren().add(NameDesc_Label);

            DataPickCriteria = true;
        }

        // In the end we add the DataGroup to the Pane.
        Scene_FX_Pane.getChildren().add(DataGroup_For_FXPane);


    }

    /*
        The Yay Method! ( I keep calling them Functions )
        The Method reads the File into the TestMat and checks
        if the TestMat is empty ( Just to make sure there's no
        NULL-Mat )
        Then it starts the Modify_And_Save method.

        Why 'Yay'? Well, for some reason it made me go "Yay" after
        I made it work.
     */
    private static void Yay(File Data){
        Mat TestMat;
        TestMat = Imgcodecs.imread(Data.getAbsolutePath());
        if(!TestMat.empty()){
            Modify_And_Save(TestMat);
        }
    }

    /*
        At first a new Mat is being created.
        Then we set the SaveFileName via testing the String in the NameGiver
        Input field, for format error. BUT.. the output file will always be PNG!

        Afterwards the input-image is being processed and being turned from a
        colour picture into a monochrome picture, in which the threshold
        is being taken to account.

        After the process the image file is being saved.
     */
    private static void Modify_And_Save(Mat GrayMe){
        Mat BufferMat = new Mat();

        String SaveFileName = StringTEST_And_Prepare(NameGiver_txtField.getText());
        //System.out.println(SaveFileName);

        Imgproc.cvtColor(GrayMe, BufferMat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(BufferMat, GrayMe,Integer.parseInt(InputThresh_txtField.getText()), 255, Imgproc.THRESH_BINARY);

        Imgcodecs.imwrite(SaveFileName, GrayMe);
    }

    /*
        A Method which modifies the savefile name.
        How does it do that?
        Well, let's say the input strung is "Test.txt", the
        ZumTest checks if a . is present, if so it takes the "Test"
        String and adds .png at the end. ( Outcome : Test.png )

        If the input is only "Test" then it automatically adds .png at the end.
     */
    private static String StringTEST_And_Prepare(String ZumTest){
        if(ZumTest.contains(".")){
            String[] Buffer = ZumTest.split("\\.");
            ZumTest = Buffer[0].concat(".png");
        } else {
            ZumTest = ZumTest.concat(".png");
        }
        return ZumTest;
    }
}
