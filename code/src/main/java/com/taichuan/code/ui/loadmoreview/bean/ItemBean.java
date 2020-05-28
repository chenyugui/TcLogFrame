package com.taichuan.code.ui.loadmoreview.bean;


import java.io.Serializable;

/**
 * 拥有itemType属性的javaBean
 * @author gui
 */
public class ItemBean implements Serializable {
    public int itemType;
    public boolean isSelected;
    public boolean isGroup;
    public String itemName;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getItemType() {
        return itemType;
    }
}