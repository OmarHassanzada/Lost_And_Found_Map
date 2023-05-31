package com.example.lost_and_found_map;

//Added longitude and latitude
public class LostFoundItem {
    private long LostFoundItemId;
    private String ItemType;
    private String ItemName;
    private String PhoneNumber;
    private String Description;
    private String Date;
    private String Location;
    private double latitude;
    private double longitude;

    public LostFoundItem(long itemId, String postType, String name, String phone, String description, String date, String location, double latitude, double longitude) {
        this.LostFoundItemId = itemId;
        this.ItemType = postType;
        this.ItemName = name;
        this.PhoneNumber = phone;
        this.Description = description;
        this.Date = date;
        this.Location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getItemId() {
        return LostFoundItemId;
    }

    public String getPostType() {
        return ItemType;
    }

    public String getName() {
        return ItemName;
    }

    public String getPhone() {
        return PhoneNumber;
    }

    public String getDescription() {
        return Description;
    }

    public String getDate() {
        return Date;
    }

    public String getLocation() {
        return Location;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
