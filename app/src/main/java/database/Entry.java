package database;

/**
 * Created by Florian on 10.06.2017.
 */

public class Entry
{
    private int id;
    protected String list;
    protected String description;
    protected String created_At;
    protected String location;
    protected double latitute;
    protected double longitute;

    public Entry(){}

    public Entry( String list, String description, String created_At, String location, int latitute, int longitute)
    {
        this.list = list;
        this.description = description;
        this.created_At = created_At;
        this.location = location;
        this.latitute = latitute;
        this.longitute = longitute;
    }

    public int getID() {return id;}
    public void setID(int id) {this.id = id;}
    public String getList() {return list;}
    public void setList(String list) {this.list = list;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getCreated_At() {return created_At;}
    public void setCreated_At(String created_At) {this.created_At = created_At;}
    public String getLocation() {return location;}
    public void setLocation(String location) {this.location = location;}
    public void setLatitute(double latitute) {this.latitute = latitute;}
    public double getLatitute() {return latitute;}
    public void setLongitute(double longitute) {this.longitute = longitute;}
    public double getLongitute() {return longitute;}
}
