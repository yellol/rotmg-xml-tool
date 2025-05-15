package types;

public class XMLTemplate {
    protected String id = "";
    protected String name = "";
    protected String spriteIndex = "";
    protected String spriteSheet = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpriteIndex() {
        return spriteIndex;
    }

    public void setSpriteIndex(String spriteIndex) {
        this.spriteIndex = spriteIndex;
    }

    public String getSpriteSheet() {
        return spriteSheet;
    }

    public void setSpriteSheet(String spriteSheet) {
        this.spriteSheet = spriteSheet;
    }
}
