package edu.uottawa.SEG2105.data.model;

public class Item {
    public String _id;
	public String _category;
    public String _name;
    public String _desc;
	public String _fee;
	public String _begin;
	public String _end;
    public String _Lessor;
    public String _Renter;

    public String _Response;
    public Item() {
    }

    public Item(String id, String category, String name, String description, String fee, String begin, String end, String lessor) {
        _id = id;
		_category = category;
        _name = name;
        _desc = description;
		_fee = fee;
		_begin = begin;
		_end = end;
        _Response = "";
        _Renter = "";
        _Lessor = lessor;
    }
}
