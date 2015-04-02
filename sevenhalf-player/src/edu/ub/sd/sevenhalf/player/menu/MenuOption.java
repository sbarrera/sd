package edu.ub.sd.sevenhalf.player.menu;

public class MenuOption {

    private int id;
    private String description;

    public MenuOption(String description) {
        this.id = -1;
        this.description = description;
    }

    public MenuOption(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void onSelect() {}
}
