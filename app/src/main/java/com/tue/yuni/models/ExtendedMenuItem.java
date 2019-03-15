package com.tue.yuni.models;

public class ExtendedMenuItem extends MenuItem {
    private int menuId;
    private Schedule schedule;

    public ExtendedMenuItem(
            int id,
            String name,
            String description,
            String category,
            int menuId,
            Schedule schedule
    ) {
        super(id, name, description, category);
        this.menuId = menuId;
        this.schedule = schedule;
    }

    public int getMenuId() {
        return menuId;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
