package ws.model;

import java.util.ArrayList;

public class PageClient {
    int count;
    int current_page;
    ArrayList<Client> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public ArrayList<Client> getList() {
        return list;
    }

    public void setList(ArrayList<Client> list) {
        this.list = list;
    }
    
}