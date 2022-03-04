package com.example.juwelierbehrendt.EntitiesAndValueObjects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Brand {
    private String stringtext;
    private String objectId;
    public Brand(String text)
    {
        this.stringtext = text;
    }
    public Brand()    //Backendless needs empty Konstruktor to save !!!
    {
        stringtext = "no title";
    }
}
