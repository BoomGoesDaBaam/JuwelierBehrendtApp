package com.example.juwelierbehrendt.EntitiesAndValueObjects;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Kind {
    private String stringtext;
    private String objectId;
    public Kind(String text)
    {
        this.stringtext = text;
    }
    public Kind()    //Backendless needs empty Konstruktor to save !!!
    {
        stringtext = "no title";
    }
}
