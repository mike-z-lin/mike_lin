package edu.uottawa.SEG2105.data.model;

public class Category {
    public String _id;
    public String _name;
    public String _desc;
    public Category() {
    }

    public Category(String id, String productname, String description) {
        _id = id;
        _name = productname;
        _desc = description;
    }
}
