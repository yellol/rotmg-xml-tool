import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import types.Tile;
import types.XMLTemplate;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ObjectHandler {
    private ComboBox<String> objectDropdown = null;
    private HBox tabContainer = null;
    private GridPane mainPanel = null;
    private final Stage primaryStage;
    private List<Object> openObjects = null;
    private List<Button> buttons = null;
    private int currentID = 0;
    private int selectedID = 0;

    private static final HashMap<String, String> properLabelText = new HashMap<>();
    
    public ObjectHandler(Stage stage, List<Object> list) {
        objectDropdown = new ComboBox<>();
        objectDropdown.getItems().addAll("Create", "Tile", "Tile Group", "Object", "Character", "Enemy", "Equipment", "Projectile");
        objectDropdown.setStyle("-fx-font-size: 10px; -fx-pref-height: 10px; -fx-background-color: #707F6F;");
        objectDropdown.setValue("Create");

        objectDropdown.setOnAction(e -> createObject(objectDropdown.getValue()));

        this.primaryStage = stage;
        this.openObjects = list;
        this.buttons = new ArrayList<>();

        // XMLContainer generics
        properLabelText.put("internalName", "Object Name");
        properLabelText.put("spriteWidth", "Sprite Width");
        properLabelText.put("spriteHeight", "Sprite Height");
        properLabelText.put("spriteX", "Sprite X");
        properLabelText.put("spriteY", "Sprite Y");

    }

    public ComboBox<String> getDropdown() {
        if (objectDropdown != null)
        {
            return objectDropdown;
        }
        return null;
    }

    public void setTabContainer(HBox tabContainer) {
        this.tabContainer = tabContainer;
    }

    private void createObject(String action)
    {
        if (tabContainer == null || primaryStage == null || action.equals("Create")) { return; }
        XMLContainer newObject = new XMLContainer();
        newObject.setInternalID(++currentID);
        if (!ProjectInfo.prefix.isEmpty())
        {
            newObject.setInternalName(ProjectInfo.prefix + " " + action);
        }
        else
        {
            newObject.setInternalName("New " + action);
        }

        openObjects.add(newObject);

        Button newTab = new Button(newObject.getInternalName());
        newTab.setId(String.valueOf(newObject.getInternalID()));
        tabContainer.getChildren().add(0, newTab);
        buttons.add(newTab);

        newTab.setStyle(ProjectInfo.tabButtonStyleSelected);
        selectedID = Integer.parseInt(newTab.getId());
        ProjectInfo.currentObject = newObject.getInternalName();
        // update status
        Label status = (Label)primaryStage.getScene().lookup("#StatusLabel");
        status.setText("RotMG XML Tool: Currently editing " + ProjectInfo.currentObject);


        // unselect all other tabs
        for (Button b : buttons) {
            if (selectedID != Integer.parseInt(b.getId())) {
                b.setStyle(ProjectInfo.tabButtonStyle);
            }
        }

        // populate main panel
        if (mainPanel != null) {
            mainPanel.getChildren().clear();
            System.out.println("Panel cleared...");
        }
        else
        {
            initializeMainPanel();
        }

        openObject(newObject.getInternalID(), action);

        newTab.setOnAction(e -> {
            mainPanel.getChildren().clear();
            newTab.setStyle(ProjectInfo.tabButtonStyleSelected);
            selectedID = Integer.parseInt(newTab.getId());
            ProjectInfo.currentObject = newObject.getInternalName();

            // update status
            status.setText("RotMG XML Tool: Currently editing " + ProjectInfo.currentObject);

            openObject(selectedID, action);

            // unselect all other tabs
            for (Button b : buttons) {
                if (selectedID != Integer.parseInt(b.getId())) {
                    b.setStyle(ProjectInfo.tabButtonStyle);
                }
            }
        });

        objectDropdown.setValue("Create");
    }

    // Generate fields for an object in OpenObject
    private void openObject(int id, String type)
    {
        // get properties of selected action
        XMLContainer newObject = null;
        for (Object o : openObjects)
        {
            if (o instanceof XMLContainer)
            {
                if (((XMLContainer)o).getInternalID() == id)
                {
                    newObject = (XMLContainer)o;
                }
            }
        }

        if (newObject == null) { return; }

        switch(type)
        {
            case "Tile":
                Tile tile = new Tile();
                newObject.setXMLBase(tile);
                newObject.setSpriteHeight(8);
                newObject.setSpriteWidth(8);
                break;
            case "Tile Group":
                break;
            case "Object":
                break;
        }

        // generic container fields
        Field[] fields = newObject.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            // exclude
            if (fieldName.equals("XMLBase") || fieldName.equals("extraProperties") || fieldName.equals("internalID")) { continue; }
            String methodName = "get" + capitalize(fieldName);
            try
            {
                Method getter = newObject.getClass().getMethod(methodName);

                if (properLabelText.get(fieldName) != null)
                {
                    createPropertyField(properLabelText.get(fieldName), mainPanel, getter.invoke(newObject).toString());
                }
                else
                {
                    createPropertyField(fieldName, mainPanel, getter.invoke(newObject).toString());
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        Object XMLBase = newObject.getXMLBase();
        if (XMLBase instanceof XMLTemplate)
        {
            Field[] extraFields = XMLBase.getClass().getDeclaredFields();
            for (Field field : extraFields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                // exclude
                if (fieldName.equals("XMLBase") || fieldName.equals("extraProperties") || fieldName.equals("internalID")) { continue; }
                String methodName = "get" + capitalize(fieldName);
                Class<?> value = field.getType();

                if (value.getSimpleName().equals("boolean"))
                {
                    methodName = "is" + capitalize(fieldName);
                }

                try
                {
                    Method getter = XMLBase.getClass().getMethod(methodName);
                    if (properLabelText.get(fieldName) != null)
                    {
                        createPropertyField(properLabelText.get(fieldName), mainPanel, getter.invoke(XMLBase));
                    }
                    else
                    {
                        createPropertyField(fieldName, mainPanel, getter.invoke(XMLBase));
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }

        // XML base parsing
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private TextField createPropertyField(String fieldName, GridPane fields)
    {
        if (fields == null) { return null; }
        Label newLabel = new Label(fieldName);
        newLabel.setStyle("-fx-text-fill: #707F6F");
        TextField newPropField = new TextField();
        newPropField.setMaxWidth(320);
        newPropField.setPrefWidth(320);
        newPropField.setStyle("-fx-text-fill: #707F6F; -fx-background-color: #2C3034;");
        newPropField.setPromptText("Enter something...");

        int verticalIndex = fields.getRowCount();
        fields.add(newLabel, 0, verticalIndex+1);
        fields.add(newPropField, 1, verticalIndex+1);

        return newPropField;
    }

    // overloaded for when switching tabs, loads old content in
    private void createPropertyField(String fieldName, GridPane fields, Object input)
    {
        if (fields == null) { return; }
        Label newLabel = new Label(fieldName);
        newLabel.setStyle("-fx-text-fill: #707F6F");
        int verticalIndex = fields.getRowCount();

        if (input instanceof String || input instanceof Integer || input instanceof Double)
        {
            TextField newPropField = new TextField();
            newPropField.setMaxWidth(300);
            newPropField.setPrefWidth(300);
            newPropField.setStyle("-fx-text-fill: #707F6F; -fx-background-color: #2C3034;");
            newPropField.setPromptText("Enter something...");
            newPropField.setText(input.toString());
            newPropField.setId(fieldName);
            fields.add(newPropField, 1, verticalIndex+1);
        }
        else if (input instanceof Boolean)
        {
            ComboBox<String> newPropField = new ComboBox<>();
            newPropField.getItems().addAll("true", "false");
            newPropField.setMaxWidth(300);
            newPropField.setPrefWidth(300);
            newPropField.setStyle("-fx-text-fill: #707F6F; -fx-background-color: #707F6F;");
            newPropField.setId(fieldName);
            fields.add(newPropField, 1, verticalIndex+1);
        }

        fields.add(newLabel, 0, verticalIndex+1);

    }

    private void initializeMainPanel()
    {
        // Property fields
        HBox mainLayout = new HBox();
        GridPane fields = new GridPane();
        fields.setId("FieldPanel");
        fields.setHgap(5);
        fields.setVgap(2);
        fields.setPadding(new Insets(5.0d));
        fields.setPrefSize(430, 470);

        mainPanel = fields;

        // image
        VBox imagePanel = new VBox();
        imagePanel.setId("ImagePanel");
        imagePanel.setPrefSize(370, 470);
        imagePanel.setSpacing(5);
        imagePanel.setPadding(new Insets(5.0d, 25.0d, 5.0d, 25.0d));
        imagePanel.setAlignment(Pos.BASELINE_CENTER);

        ImageView currentSprite = new ImageView();
        Image placeholder = new Image(Objects.requireNonNull(ObjectHandler.class.getResourceAsStream("placeholder.png")));
        currentSprite.setFitWidth(80);
        currentSprite.setFitHeight(80);
        currentSprite.setImage(placeholder);

        ImageView spriteSheetDisplay = new ImageView();
        spriteSheetDisplay.setFitWidth(320);
        spriteSheetDisplay.setFitHeight(320);
        spriteSheetDisplay.setImage(placeholder);

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"));

        Button loadImage = new Button("Load Spritesheet...");
        loadImage.setStyle(ProjectInfo.tabButtonStyle);
        loadImage.setOnAction(e -> {
            // Show the file chooser and get the selected file
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                // Load the image
                Image image = new Image("file:" + selectedFile.getAbsolutePath());
                Image scaledImage = scaleImageNearestNeighbor(image, (int)image.getWidth()*2, (int)image.getHeight()*2);
                spriteSheetDisplay.setImage(scaledImage);

                // display options
                spriteSheetDisplay.setFitWidth(scaledImage.getHeight());
                spriteSheetDisplay.setFitHeight(scaledImage.getWidth());
                spriteSheetDisplay.setPreserveRatio(true);
                spriteSheetDisplay.setSmooth(false);
            }
        });

        imagePanel.getChildren().addAll(currentSprite, spriteSheetDisplay, loadImage);

        ScrollPane scrollPane = new ScrollPane(fields);
        scrollPane.setMaxWidth(430);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        ScrollPane imageScrollPane = new ScrollPane(imagePanel);
        imageScrollPane.setMaxWidth(370);
        imageScrollPane.setPrefWidth(370);
        imageScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        imageScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        imageScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        imageScrollPane.setPannable(true);
        mainLayout.getChildren().addAll(scrollPane, imageScrollPane);

        StackPane mainPanel = (StackPane) primaryStage.getScene().lookup("#MainPanel");
        mainPanel.getChildren().add(mainLayout);
    }

    public static Image scaleImageNearestNeighbor(Image input, int targetWidth, int targetHeight) {
        WritableImage output = new WritableImage(targetWidth, targetHeight);
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        double scaleX = input.getWidth() / targetWidth;
        double scaleY = input.getHeight() / targetHeight;

        for (int y = 0; y < targetHeight; y++) {
            for (int x = 0; x < targetWidth; x++) {
                int srcX = (int)(x * scaleX);
                int srcY = (int)(y * scaleY);
                writer.setArgb(x, y, reader.getArgb(srcX, srcY));
            }
        }

        return output;
    }

}
