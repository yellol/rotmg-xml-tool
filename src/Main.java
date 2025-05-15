import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends Application {

    public static final int WIDTH = 800;
    public static final HashMap<String, Color> colorTable = new HashMap<>();
    public static final List<Object> openObjects = new ArrayList<>();
    
    private static final String labelStyle = "-fx-text-fill: #707F6F;";

    @Override
    public void start(Stage primaryStage) {

        FileOperations fileop = new FileOperations();
        ProjectHandler projh = new ProjectHandler(primaryStage);
        ObjectHandler objhand = new ObjectHandler(primaryStage, openObjects);

        // TOOLBAR (FILES, ETC.)
        Canvas tools = new Canvas(WIDTH, 30);
        GraphicsContext toolGC = tools.getGraphicsContext2D();
        toolGC.setFill(colorTable.get("Highlight"));
        toolGC.fillRect(0, 0, tools.getWidth(), tools.getHeight());

        Label projectLabel = new Label("Project: " + ProjectInfo.projectName);
        projectLabel.setId("ProjectLabel");
        projectLabel.setStyle(labelStyle);

        ToolBar toolBarLayout = new ToolBar(fileop.getDropdown(), objhand.getDropdown(), projh.getDropdown(), projectLabel);
        toolBarLayout.setPrefHeight(20);
        toolBarLayout.setStyle("-fx-background-color: transparent; -fx-padding: 5px;");

        // TABS FOR SEPARATE OBJECTS
        Canvas nav = new Canvas(WIDTH, 40);
        GraphicsContext tabGC = nav.getGraphicsContext2D();
        tabGC.setFill(colorTable.get("NavBar"));
        tabGC.fillRect(0, 0, nav.getWidth(), nav.getHeight());

        Button addObject = new Button("+");
        addObject.setStyle(ProjectInfo.tabButtonStyle);

        HBox tabs = new HBox();
        tabs.setId("TabBar");
        tabs.setSpacing(5);
        tabs.setPadding(new Insets(5.0d));
        tabs.getChildren().addAll(addObject);

        // Event handler for adding objects
        addObject.setOnAction(e -> {
            objhand.getDropdown().show();
        });

        objhand.setTabContainer(tabs);

        // MAIN AREA
        Canvas main = new Canvas(WIDTH, 480);
        GraphicsContext mainGC = main.getGraphicsContext2D();

        // Background
        mainGC.setFill(colorTable.get("Main"));
        mainGC.fillRect(0, 0, main.getWidth(), main.getHeight());

        StackPane wholeToolBar = new StackPane();
        wholeToolBar.getChildren().addAll(tools, toolBarLayout);

        StackPane wholeNavBar = new StackPane();
        wholeNavBar.getChildren().addAll(nav, tabs);

        StackPane wholeMain = new StackPane();
        wholeMain.setId("MainPanel");
        wholeMain.getChildren().addAll(main);

        // BOTTOM BUTTON BAR
        Canvas bar = new Canvas(WIDTH, 40);
        GraphicsContext barGC = bar.getGraphicsContext2D();

        Label statusLabel = new Label("RotMG XML Tool: Currently editing nothing");
        statusLabel.setId("StatusLabel");
        statusLabel.setStyle(labelStyle);

        // Background
        barGC.setFill(Color.web("#2C3034"));
        barGC.fillRect(0, 0, main.getWidth(), main.getHeight());

        // button container
        HBox buttonBar = new HBox();
        buttonBar.setMaxSize(WIDTH, 40);
        buttonBar.setPrefSize(WIDTH, 40);
        buttonBar.setPadding(new Insets(5.0d));
        buttonBar.setSpacing(10);
        buttonBar.setAlignment(Pos.BASELINE_RIGHT);

        // buttonz
        Button closeButton = new Button("Close Object");
        Button applyButton = new Button("Save Object");
        closeButton.setStyle(ProjectInfo.tabButtonStyle);
        applyButton.setStyle(ProjectInfo.tabButtonStyle);
        buttonBar.getChildren().addAll(statusLabel, closeButton, applyButton);

        StackPane barStack = new StackPane();
        barStack.getChildren().addAll(bar, buttonBar);

        Scale scale = new Scale(1, 1, 0, 0);

        VBox vbox = new VBox();
        vbox.getTransforms().add(scale);
        vbox.setId("Main");
        vbox.getChildren().addAll(wholeToolBar, wholeNavBar, wholeMain, barStack);

        Group root = new Group(vbox);

        Scene scene = new Scene(root, WIDTH, 590);
        scene.setFill(Color.BLACK);

        // keep aspect ratio business
        DoubleBinding uniformScale = new DoubleBinding() {
            {
                super.bind(scene.widthProperty(), scene.heightProperty());
            }

            @Override
            protected double computeValue() {
                double scaleX = scene.getWidth() / 800;
                double scaleY = scene.getHeight() / 590;
                return Math.min(scaleX, scaleY); // Enforce aspect ratio
            }
        };

        scale.xProperty().bind(uniformScale);
        scale.yProperty().bind(uniformScale);

        // Optional: center the content
        root.layoutXProperty().bind(
                uniformScale.multiply(800).subtract(scene.widthProperty()).divide(-2)
        );

        root.layoutYProperty().bind(
                uniformScale.multiply(590).subtract(scene.heightProperty()).divide(-2)
        );

        primaryStage.setScene(scene);
        //primaryStage.setResizable(false);
        primaryStage.setTitle("RotMG XML Tool");
        primaryStage.show();
    }

    public static void main(String[] args) {

        colorTable.put("Main", Color.web("#1B1A23"));
        colorTable.put("NavBar", Color.web("#2C3034"));
        colorTable.put("Highlight", Color.web("#394142"));
        colorTable.put("FontColor", Color.web("#707F6F"));

        launch(args);
    }
}