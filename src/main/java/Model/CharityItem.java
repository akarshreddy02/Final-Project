package Model;

public class CharityItem {
    private int id;
    private String name;
    private String description;

    public CharityItem() {}

    public CharityItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CharityItem(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
