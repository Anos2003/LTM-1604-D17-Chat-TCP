package common.model;

import java.io.Serializable;

public class ChatGroup implements Serializable {
    private String name;

    public ChatGroup(String name){
        this.name = name;
    }

    public String getName(){ return name; }
}
