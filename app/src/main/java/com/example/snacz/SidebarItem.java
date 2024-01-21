package com.example.snacz;
public class SidebarItem {
    private int imageResId;
    private String subtext;

    public SidebarItem(int imageResId, String subtext) {
        this.imageResId = imageResId;
        this.subtext = subtext;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getSubtext() {
        return subtext;
    }
}
