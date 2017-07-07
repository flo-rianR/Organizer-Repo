package database;

/**
 * Created by Florian on 05.07.2017.
 */

public class ListModel
{
    private int id;
    private String list;
    private String create_At;

    public ListModel(){}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getList() {return list;}
    public void setList(String list) {this.list = list;}
    public String getCreate_At() {return create_At;}
    public void setCreate_At(String create_At) {this.create_At = create_At;}
}
