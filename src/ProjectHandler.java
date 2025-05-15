import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProjectHandler {
    private ComboBox<String> projectDropdown = null;
    private Stage configWindow = null;
    private final Stage primaryStage;

    public ProjectHandler(Stage primaryStage) {
        projectDropdown = new ComboBox<>();
        projectDropdown.getItems().addAll("Project", "Configure Project");
        projectDropdown.setStyle("-fx-font-size: 10px; -fx-pref-height: 10px; -fx-background-color: #707F6F;");
        projectDropdown.setValue("Project");

        projectDropdown.setOnAction(e -> handleProject(projectDropdown.getValue()));

        this.primaryStage = primaryStage;
    }

    public ComboBox<String> getDropdown() {
        if (projectDropdown != null)
        {
            return projectDropdown;
        }
        return null;
    }

    private void handleProject(String action)
    {

        if (action.equals("Configure Project"))
        {
            createConfigWindow();
        }

        projectDropdown.setValue("Project");
    }

    private void createConfigWindow()
    {
        if (configWindow != null) {
            configWindow.toFront();
            return;
        }
        configWindow = new Stage();
        configWindow.initModality(Modality.APPLICATION_MODAL);
        configWindow.initOwner(primaryStage);

        // MAIN AREA
        Canvas main = new Canvas(480, 440);
        GraphicsContext mainGC = main.getGraphicsContext2D();

        // Background
        mainGC.setFill(Color.web("#1B1A23"));
        mainGC.fillRect(0, 0, main.getWidth(), main.getHeight());

        GridPane config = new GridPane();
        config.setHgap(5);
        config.setVgap(5);
        config.setPadding(new Insets(5.0d));
        StackPane mainStack = new StackPane();
        mainStack.getChildren().addAll(main, config);

        TextField nameField = createPropertyField("Project Name: ", config);
        TextField prefixField = createPropertyField("Obj. Prefix:  ", config);
        TextField idField = createPropertyField("Starting ID: ", config);

        nameField.setText(ProjectInfo.projectName);
        prefixField.setText(ProjectInfo.prefix);
        idField.setText(ProjectInfo.startingID);

        // bottom bar
        Canvas bar = new Canvas(480, 40);
        GraphicsContext barGC = bar.getGraphicsContext2D();

        // Background
        barGC.setFill(Color.web("#2C3034"));
        barGC.fillRect(0, 0, main.getWidth(), main.getHeight());

        // button container
        HBox buttonBar = new HBox();
        buttonBar.setMaxSize(480, 40);
        buttonBar.setPrefSize(480, 40);
        buttonBar.setPadding(new Insets(5.0d));
        buttonBar.setSpacing(10);
        buttonBar.setAlignment(Pos.BASELINE_RIGHT);

        // buttonz
        Button cancelButton = new Button("Cancel");
        Button applyButton = new Button("Apply");
        cancelButton.setStyle(ProjectInfo.tabButtonStyle);
        applyButton.setStyle(ProjectInfo.tabButtonStyle);
        buttonBar.getChildren().addAll(cancelButton, applyButton);

        applyButton.setOnAction(actionEvent -> {
            // null checks
            if (!nameField.getText().isEmpty())
            {
                ProjectInfo.projectName = nameField.getText();
            }

            if (!prefixField.getText().isEmpty())
            {
                ProjectInfo.prefix = prefixField.getText();
            }

            if (!idField.getText().isEmpty())
            {
                ProjectInfo.startingID = idField.getText();
            }

            // update project name
            Label projectName = (Label)primaryStage.getScene().lookup("#ProjectLabel");
            projectName.setText("Project: " + ProjectInfo.projectName);
            configWindow.close();
            configWindow = null;
        });

        cancelButton.setOnAction(actionEvent -> {
            configWindow.close();
            configWindow = null;
        });

        StackPane barStack = new StackPane();
        barStack.getChildren().addAll(bar, buttonBar);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(mainStack, barStack);

        Scene configScene = new Scene(vbox, 480, 480);
        configWindow.setScene(configScene);
        configWindow.setResizable(false);
        configWindow.setTitle("Configure Project...");
        configWindow.setOnCloseRequest(windowEvent -> {
            configWindow = null;
        });

        configWindow.showAndWait();

    }

    private TextField createPropertyField(String fieldName, GridPane fields)
    {
        if (fields == null) { return null; }
        Label newLabel = new Label(fieldName);
        newLabel.setStyle("-fx-text-fill: #707F6F");
        TextField newPropField = new TextField();
        newPropField.setMaxWidth(380);
        newPropField.setPrefWidth(380);
        newPropField.setStyle("-fx-text-fill: #707F6F; -fx-background-color: #2C3034;");
        newPropField.setPromptText("Enter something...");

        int verticalIndex = fields.getRowCount();
        fields.add(newLabel, 0, verticalIndex+1);
        fields.add(newPropField, 1, verticalIndex+1);

        return newPropField;
    }

}
