package de.udos.android_demo_async;

public class Item {

    private String name;
    private String imageUrl;

    public Item(String name, String imageUrl) {

        this.name = name;
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIconURL(String iconURL) {
        this.imageUrl = iconURL;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return name;
    }
}
