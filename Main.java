package a;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox vBox = new VBox();
        primaryStage.setTitle("HTML Text Editor");
        primaryStage.getIcons().add(new Image("file:icon.png"));

        // Create a menu bar
        MenuBar menuBar = new MenuBar();

        // Create menus
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");

        // Create a text editor
        HTMLEditor htmlEditor = new HTMLEditor();
        htmlEditor.requestFocus();
        WebView webView = (WebView) htmlEditor.lookup("WebView");
        WebEngine webEngine = webView.getEngine();
        FadeTransition ft = new FadeTransition(Duration.millis(3000), htmlEditor);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();


        // Create menu items for the "File" menu
        MenuItem newFile = new MenuItem("New");
        MenuItem openFile = new MenuItem("Open");
        MenuItem saveFile = new MenuItem("Save");
        MenuItem exit = new MenuItem("Exit");

        // Add functionality to "File" menu items
        newFile.setOnAction(e -> htmlEditor.setHtmlText(""));
        openFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    htmlEditor.setHtmlText(content);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        saveFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    String content = htmlEditor.getHtmlText();
                    Files.write(file.toPath(), content.getBytes());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        exit.setOnAction(e -> System.exit(0));

        // Add menu items to "File" menu
        fileMenu.getItems().addAll(newFile, openFile, saveFile, new SeparatorMenuItem(), exit);

        // Create menu items for the "Edit" menu
        MenuItem undo = new MenuItem("Undo");
        MenuItem redo = new MenuItem("Redo");
        MenuItem delete = new MenuItem("Delete");
        MenuItem selectAll = new MenuItem("Select All");

        // Add functionality to "Edit" menu items
        undo.setOnAction(e -> webEngine.executeScript("document.execCommand('undo')"));
        redo.setOnAction(e -> webEngine.executeScript("document.execCommand('redo')"));
        delete.setOnAction(e -> webEngine.executeScript("document.execCommand('delete')"));
        selectAll.setOnAction(e -> webEngine.executeScript("document.execCommand('selectAll')"));

        // Add menu items to "Edit" menu
        editMenu.getItems().addAll(undo, redo, new SeparatorMenuItem(), delete, new SeparatorMenuItem(), selectAll);

        // Add menus to the menu bar
        menuBar.getMenus().addAll(fileMenu, editMenu);

        // Add the menu bar and text editor to the VBox
        vBox.getChildren().addAll(menuBar, htmlEditor);

        // Create a scene
        Scene scene = new Scene(vBox, 800, 600);

        // Apply CSS
        scene.getStylesheets().add("a/style.css");

        // Add the scene to the stage
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
