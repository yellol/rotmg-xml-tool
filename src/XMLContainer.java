import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// metadata for each object
public class XMLContainer {
    // object info
    private Object XMLBase = null;
    private int internalID = 0;
    private String internalName = "";

    // display vars
    private int spriteWidth = 0;
    private int spriteHeight = 0;
    private int spriteX = 0;
    private int spriteY = 0;

    // extra properties
    private final HashMap<String, String> extraProperties;

    public XMLContainer() {
        extraProperties = new HashMap<>();
    }

    public int getInternalID() {
        return internalID;
    }

    public void setInternalID(int internalID) {
        this.internalID = internalID;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public void addExtraProperty(String key, String value) {
        extraProperties.put(key, value);
    }

    public HashMap<String, String> getExtraProperties(String key, String value) {
        return extraProperties;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public void setSpriteWidth(int spriteWidth) {
        this.spriteWidth = spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public void setSpriteHeight(int spriteHeight) {
        this.spriteHeight = spriteHeight;
    }

    public Object getXMLBase() {
        return XMLBase;
    }

    public void setXMLBase(Object XMLBase) {
        this.XMLBase = XMLBase;
    }

    public int getSpriteX() {
        return spriteX;
    }

    public void setSpriteX(int spriteX) {
        this.spriteX = spriteX;
    }

    public int getSpriteY() {
        return spriteY;
    }

    public void setSpriteY(int spriteY) {
        this.spriteY = spriteY;
    }
}
