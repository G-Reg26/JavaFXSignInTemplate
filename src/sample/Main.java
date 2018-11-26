package sample;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class Main extends Application {

  //The stage we will be showing our scenes in
  public static Stage stage;
  //Map that contains our scenes, using a string as a key
  public static Map<String, String> scenes = new HashMap<>();

  public static FileManager fileManager;

  public static FXMLLoader currentController;

  @Override
  public void start(Stage primaryStage) throws Exception {
    Team.defaultProfilePicFile = new File(".\\src\\sample\\Images\\DefaultProfilePicTemp.png");
    Team.defaultProfilePic = ImageIO.read(Team.defaultProfilePicFile);

    Account.defaultProfilePicFile = new File(".\\src\\sample\\Images\\DefaultProfilePicTemp.png");
    Account.defaultProfilePic = ImageIO.read(Account.defaultProfilePicFile);

    fileManager = new FileManager();

    //Put fxml urls into the scenes hash map with a key that correlates to their file name
    scenes.put("Login", "FXMLDocs/Login.fxml");
    scenes.put("CreateAccount", "FXMLDocs/CreateAccount.fxml");
    scenes.put("HomePageController", "FXMLDocs/HomePage.fxml");
    scenes.put("TeamPage", "FXMLDocs/TeamPage.fxml");
    scenes.put("EditTeamPage", "FXMLDocs/EditTeam.fxml");
    scenes.put("CreateEvent", "FXMLDocs/CreateEvent.fxml");
    scenes.put("EventPage", "FXMLDocs/EventPage.fxml");

    //Set up home page scene
    FXMLLoader loader = new FXMLLoader(getClass().getResource(scenes.get("HomePageController")));

    currentController = loader;

    Parent root = loader.load();

    Scene scene = new Scene(root);

    primaryStage.setScene(scene);
    primaryStage.setTitle("Welcome");
    primaryStage.setResizable(false);

    primaryStage.show();

    //Set stage to primaryStage
    stage = primaryStage;
  }

  public void stop() {
    fileManager.writeFiles();
  }

  public static void loadScene(Parent root) {
    Scene scene = new Scene(root);
    stage.setScene(scene);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
