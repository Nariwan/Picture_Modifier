package Picker;


// Imported Libraries

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;

import static Picker.Main.Scene_FX_Pane;


public class DataChooseFunction {

    // Some variables, IntelliJ might gonna nag of some,
    // but that's okay.

    private static boolean DataPickCriteria = false;
    private static Group DataGroup_For_FXPane;
    private static FileChooser DataChoose;
    private static File DataVar;
    private static Button Button_PickFile, Button_For_Saving_The_Pic, Button_For_Exit;
    private static Label sPath_Label, ThresDescription_Label, NameDesc_Label;
    private static boolean HasPic;
    private static boolean b_Is_ImageView_Setup = false;

    private static InputStream Stream_Input;
    private static ImageView Show_Image;

    public static TextField NameGiver_txtField;
    public static Slider Slider_Threshold;

    // Constructor of the Chooser
    public DataChooseFunction(){

        String btn_Format = "-fx-background-color: #1C1A1A; -fx-text-fill: red; -fx-font-size: 16px;";
        String txt_Format = "-fx-background-color: #1C1A1A; -fx-text-fill: yellow; -fx-font-size: 16px;";
        int Y_Pos = 30;
        double NameGiver_txtField_Width = Scene_FX_Pane.getWidth()/3;

        // Did the layout, window, already got created?
        if(!DataPickCriteria){


            // Desc. for Input Label
            ThresDescription_Label = new Label();
            ThresDescription_Label.setPrefWidth(145);
            ThresDescription_Label.setLayoutX(10);
            ThresDescription_Label.setLayoutY(Y_Pos);
            ThresDescription_Label.setTextFill(Color.YELLOW);

            // Text field to input the Threshold
            Slider_Threshold = new Slider();
            Slider_Threshold.setPrefWidth(170);
            Slider_Threshold.setLayoutX(150);
            Slider_Threshold.setLayoutY(Y_Pos);
            Slider_Threshold.setMax(255.0);
            Slider_Threshold.setMin(0.0);
            Slider_Threshold.setValue(100.0);
            Slider_Threshold.setBlockIncrement(0.1f);

            ThresDescription_Label.setText("Threshold : "+ String.format("%.2f",Slider_Threshold.getValue()));

            Slider_Threshold.valueProperty().addListener((observable, oldValue, newValue) -> {
                if(HasPic){
                    Threshold_Image_Refresh(DataVar);
                }
                ThresDescription_Label.setText("Threshold : "+ String.format("%.2f",Slider_Threshold.getValue()));
            });


            // Desc. for Save-Name Label
            NameDesc_Label = new Label("Save Name");
            NameDesc_Label.setPrefWidth(90);
            NameDesc_Label.setLayoutY(Y_Pos);
            NameDesc_Label.setLayoutX(350);
            NameDesc_Label.setTextFill(Color.YELLOW);

            // The input field of how to name the Output File
            NameGiver_txtField = new TextField("Test.png");
            NameGiver_txtField.setPrefWidth(NameGiver_txtField_Width);
            NameGiver_txtField.setLayoutX(460);
            NameGiver_txtField.setLayoutY(Y_Pos-5);
            NameGiver_txtField.setStyle((txt_Format));



            /*
                The FileChooser Object gets created.
                After this some restrictions which kind of
                Data is allowed.
            */
            DataChoose = new FileChooser();

            DataChoose.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("*", "*.jpg","*.jpeg","*.png","*.gif","*.bmp","*.tiff"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
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
            sPath_Label.setLayoutX(10);
            sPath_Label.setLayoutY(Y_Pos+40);

            /*
                The button, and boolean, for the File picker.
             */
            HasPic = false;
            Button_PickFile = new Button();
            Button_PickFile.setText("Pick Image");
            Button_PickFile.setPrefWidth(130);
            Button_PickFile.setLayoutX(900);
            Button_PickFile.setLayoutY(Y_Pos-5);
            Button_PickFile.setStyle((btn_Format));

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
                if(DataVar == null && sPath_Label.getText().equals("NO FILE!!")) {
                    System.out.println("No File!");
                    sPath_Label.setText("NO FILE!!");
                    HasPic = false;
                } else if(DataVar == null && !sPath_Label.getText().equals("NO FILE!!")) {
                    DataVar = new File(sPath_Label.getText());
                    HasPic = true;
                } else {
                    //System.out.println(DataVar);
                    sPath_Label.setText(DataVar.toString());
                    sPath_Label.setLayoutX(10);
                    Threshold_Image_Refresh(DataVar);
                    HasPic = true;
                    }
                });

            /*
                Here the button with the modify function
                gets created, and arranged in the Pane.
             */
            Button_For_Saving_The_Pic = new Button();
            Button_For_Saving_The_Pic.setText("Save");
            Button_For_Saving_The_Pic.setPrefWidth(100);
            Button_For_Saving_The_Pic.setLayoutX(1040);
            Button_For_Saving_The_Pic.setLayoutY(Y_Pos-5);
            Button_For_Saving_The_Pic.setStyle((btn_Format));

            /*
                What happens, if the Modify button gets clicked?
                Well..
                It checks again, if there's a picture available ( Might be
                redundant, but just to make sure. ), and then it starts the
                entire modifying process.
             */
            Button_For_Saving_The_Pic.setOnAction(event -> {

                // Wenn der Button gedrückt wird, erscheint das Dateiauswahlfenster.
                if (!HasPic) {
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
            Button_For_Exit.setLayoutX(1160);
            Button_For_Exit.setLayoutY(Y_Pos-5);
            Button_For_Exit.setStyle((btn_Format));

            /*
                If clicked -> Exit
             */
            Button_For_Exit.setOnAction(event -> System.exit(0));
            // ---------------------------------------

            // All the former created objects are being put into Group.
            DataGroup_For_FXPane = new Group();
            DataGroup_For_FXPane.getChildren().add(Button_PickFile);
            DataGroup_For_FXPane.getChildren().add(Button_For_Saving_The_Pic);
            DataGroup_For_FXPane.getChildren().add(Button_For_Exit);
            DataGroup_For_FXPane.getChildren().add(sPath_Label);
            DataGroup_For_FXPane.getChildren().add(Slider_Threshold);
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
            Just_Modify(TestMat);
            Save_Modified_Image(TestMat);
        }
    }
    /*
        Copy of Yay, just for Threshold refresh
    */
    private static void Threshold_Image_Refresh(File Data){
        Mat TestMat;
        TestMat = Imgcodecs.imread(Data.getAbsolutePath());
        if(!TestMat.empty()){
            Just_Modify(TestMat);
        }
    }

    /*
        This Method is for re-freshing, and setting, the imageview.
        Input is the File
     */

    private static void Image_Preview_Set_Refresh(String Data) {
        try {
            Stream_Input = new FileInputStream(Data);
            if (!b_Is_ImageView_Setup) {
                Show_Image = new ImageView();
                Show_Image.setX(20);
                Show_Image.setY(100);
                Show_Image.setFitHeight(600);
                Show_Image.setFitWidth(1260);
                b_Is_ImageView_Setup = true;
                DataGroup_For_FXPane.getChildren().add(Show_Image);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DataGroup_For_FXPane.getChildren().remove(Show_Image);
        Image displayed_Image = new Image(Stream_Input);
        Show_Image.setImage(displayed_Image);
        DataGroup_For_FXPane.getChildren().add(Show_Image);
        Show_Image.setPreserveRatio(true);
    }

    /*
    At first a new Mat is being created.
    Then we set the SaveFileName via testing the String in the NameGiver
    Input field, for format error. BUT.. the output file will always be PNG!

    Afterwards the input-image is being processed and being turned from a
    colour picture into a monochrome picture, in which the threshold
    is being taken to account.
    */
    private static void Just_Modify(Mat GrayMe){
        Mat BufferMat = new Mat();
        String String_to_File = StringTEST_And_Prepare("TempPic");
        Imgproc.cvtColor(GrayMe, BufferMat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(BufferMat, GrayMe,Slider_Threshold.getValue(), 255, Imgproc.THRESH_BINARY);
        Imgcodecs.imwrite(String_to_File, GrayMe);
        Image_Preview_Set_Refresh(String_to_File);
    }

    /*
        Saves the modified image.
     */
    private static void Save_Modified_Image(Mat GrayMe){

        String SaveFileName = NameGiver_txtField.getText();
        if(!SaveFileName.toUpperCase(Locale.ROOT).contains("TEMPPIC")) {
            SaveFileName = StringTEST_And_Prepare(NameGiver_txtField.getText());
        } else {
            NameGiver_txtField.setText("TempPic_as_name_Not_allowed.png");
            SaveFileName = StringTEST_And_Prepare(NameGiver_txtField.getText());
        }

        Imgcodecs.imwrite(SaveFileName, GrayMe);
        Image_Preview_Set_Refresh(SaveFileName);
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
