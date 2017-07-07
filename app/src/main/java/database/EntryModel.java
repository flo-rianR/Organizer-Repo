package database;

/**
 * Created by Florian on 10.06.2017.
 */

public class EntryModel
{
    private int id;
    private String name;
    private String description;
    private String created_At;
    private String date;
    private String location;
    private double latitute;
    private double longitute;
    private int foreign_key;

    public EntryModel(){}

    public EntryModel(String list, String description, String created_At, String location, int latitute, int longitute)
    {
        this.description = description;
        this.created_At = created_At;
        this.location = location;
        this.latitute = latitute;
        this.longitute = longitute;
    }

    public int getID() {return id;}
    public void setID(int id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getCreated_At() {return created_At;}
    public void setCreated_At(String created_At) {this.created_At = created_At;}
    public String getDate() {return date;}
    public void setDate(String date){this.date = date;}
    public String getLocation() {return location;}
    public void setLocation(String location) {this.location = location;}
    public void setLatitute(double latitute) {this.latitute = latitute;}
    public double getLatitute() {return latitute;}
    public void setLongitute(double longitute) {this.longitute = longitute;}
    public double getLongitute() {return longitute;}
    public int getForeign_key() {return foreign_key;}
    public void setForeign_key(int foreign_key){this.foreign_key = foreign_key;}
}
