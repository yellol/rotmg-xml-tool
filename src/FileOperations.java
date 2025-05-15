import javafx.scene.control.ComboBox;

public class FileOperations {
    private ComboBox<String> fileDropdown = null;

    public FileOperations() {
        fileDropdown = new ComboBox<>();
        fileDropdown.getItems().addAll("File", "Import", "Save", "Export");
        fileDropdown.setStyle("-fx-font-size: 10px; -fx-pref-height: 10px; -fx-background-color: #707F6F;");
        fileDropdown.setValue("File");

        fileDropdown.setOnAction(e -> fileHandler(fileDropdown.getValue()));
    }

    public ComboBox<String> getDropdown() {
        if (fileDropdown != null)
        {
            return fileDropdown;
        }
        return null;
    }

    private void fileHandler(String action)
    {
        fileDropdown.setValue("File");
    }

}
