package com.example.juwelierbehrendt.EntitiesAndValueObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.juwelierbehrendt.ExternalLibs.EasyNW;
import com.example.juwelierbehrendt.ExternalLibs.StartApplication;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {
    private String name;
    private float price;
    private int discount;
    private String brand;
    private String what;
    private String description;
    private ArrayList<Bitmap> pics;
    private Date created;
    private Date updated;
    private String objectId;    //identifier
    private int amount=1;
    private int hasNPics=0;

    public boolean isListedInSearch(String s)
    {
        //boolean brandComp=brand.toLowerCase().contains(s);
        //boolean nameComp=brand.toLowerCase().contains(s);
        //return true;
        return  name.toLowerCase().contains(s) ||
                brand.toLowerCase().contains(s) ||
                what.toLowerCase().contains(s) ||
                description.toLowerCase().contains(s);// ||
                //objectId.toString().toLowerCase().contains(s);*
    }

    public void copyAssignment(Product rhs)
    {
        pics = rhs.pics;
        name = rhs.name;
        price= rhs.price;
        discount= rhs.discount;
        brand= rhs.brand;
        what= rhs.what;
        description= rhs.description;
        created= rhs.created;
        updated= rhs.updated;
        objectId= rhs.objectId;
        amount= rhs.amount;
        hasNPics = rhs.hasNPics;
    }
    public boolean isInitilized()
    {
        return name != "-";
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Product()
    {
        name = "-";
        price = 0;
        discount = 0;
        brand = "brandless";
        what = "nothing";
        pics = new ArrayList<>();
        created = new Date();
        updated = new Date();
        hasNPics = 0;
    }

    public void setPics(ArrayList<Bitmap> pics) {
        this.pics = pics;
    }

    public ArrayList<Bitmap> getPics() {
        return pics;
    }
    public Bitmap getPicAt(int index)
    {
        return pics.get(index);
    }
    public void addPic(Bitmap pic) {
        pics.add(pic);
        hasNPics++;
    }
    public void deletePic(int index) {
        this.pics.remove(index);
        hasNPics--;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }
    public float getPriceIncDiscount() {
        return price * ((100f - discount)/100);
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public void loadAll(TextView tVDisplayName,
                        TextView tVDisplayKind,
                        TextView tVDisplayBrand,
                        TextView tVDisplayAmount,
                        TextView tVDisplayPrice,
                        TextView tVDisplayDiscount,
                        TextView tVDisplayDescription)
    {
        loadData(tVDisplayName, tVDisplayKind, tVDisplayBrand, tVDisplayAmount, tVDisplayPrice, tVDisplayDiscount, tVDisplayDescription);
        loadPictures(false);
    }
    public void loadBasicComponents(TextView tVDisplayName,
                                    TextView tVDisplayKind,
                                    TextView tVDisplayBrand,
                                    TextView tVDisplayAmount,
                                    TextView tVDisplayPrice,
                                    TextView tVDisplayDiscount,
                                    TextView tVDisplayDescription)
    {
        tVDisplayName.setText(name);
        tVDisplayKind.setText(what);
        tVDisplayBrand.setText(brand);
        tVDisplayAmount.setText(String.valueOf(amount));
        tVDisplayPrice.setText(String.valueOf(price));
        tVDisplayDiscount.setText(String.valueOf(discount));
        tVDisplayDescription.setText(description);
    }
    public void loadData(TextView tVDisplayName,
                         TextView tVDisplayKind,
                         TextView tVDisplayBrand,
                         TextView tVDisplayAmount,
                         TextView tVDisplayPrice,
                         TextView tVDisplayDiscount,
                         TextView tVDisplayDescription)
    {
        Backendless.Data.of(Product.class).findById(objectId, new AsyncCallback<Product>() {
            @Override
            public void handleResponse(Product response) {
                ArrayList<Bitmap> pics = getPics();
                copyAssignment(response);
                setPics(pics);
                loadBasicComponents(tVDisplayName, tVDisplayKind, tVDisplayBrand, tVDisplayAmount, tVDisplayPrice, tVDisplayDiscount, tVDisplayDescription);
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                int k=23;
            }
        });
    }
    public void loadPictures(boolean deleteAllPics)
    {
        if(hasNPics == 0)
        {
            return;
        }

        Thread[] threads = new Thread[hasNPics];
        for(int i=0;i<hasNPics;i++) {
            threads[i] = new Thread(() -> {
                int keepLoading = 0;
                int curIndex = 0;
                while (keepLoading < 1) {
                    boolean succeded = LoadPicture(curIndex, deleteAllPics);
                    if (succeded) {
                        curIndex++;
                    } else if (!succeded) {
                        keepLoading++;
                    }
                }
            });
        }
        int nPics = threads.length;
        for(int i=0;i<nPics;i++) {
            threads[i].start();
        }

            boolean working = true;
        int nFinished = 0;
        while(working)
        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i=0;i<hasNPics;i++)
            {
                if(threads[i] != null && !threads[i].isAlive())
                {
                    threads[i] = null;
                    nFinished++;
                }
            }
            if(nFinished == hasNPics)
            {
                working = false;
            }
        }
       // Thread threadLoadPictures = new Thread(()->{
        /*
            int keepLoading = 0;
            int curIndex=0;
            while(keepLoading < 1) {
                boolean succeded = LoadPicture(curIndex, deleteAllPics);
                if(succeded)
                {
                    curIndex ++;
                }else if (!succeded) {
                    keepLoading++;
                }
            }
           /*
        });
        threadLoadPictures.start();

        while(threadLoadPictures.isAlive())
        {
            try {
                Thread.sleep(2000);
            }catch (Exception e)
            {

            }
        }
        */
    }
    public boolean LoadPicture(int picID, boolean delete)   //(delete == true) => delete pic from backendless
    {                                                       //(delete == true) => load pic into this
        for(int i=0;i<3;i++) {
            EasyNW easyNWtmp = new EasyNW("https://eu.backendlessappcontent.com/"+ StartApplication.APPLICATION_ID+"/"+StartApplication.API_KEY+"/files/");
            String action = "ProductsPics/Product" + objectId + "Pic" + String.valueOf(picID);
            EasyNW.nwResponse response1 = easyNWtmp.sendNwRequest("GET", action, new HashMap<>(), new HashMap<>());
            Bitmap bmp = BitmapFactory.decodeByteArray(response1.getBytes(), 0, response1.getBytes().length);
            if (response1.getSucceeded() && bmp != null) {
                if(delete)
                {
                    easyNWtmp.sendNwRequest("DELETE", action, new HashMap<>(), new HashMap<>());
                }
                else

                {
                    addPic(bmp);
                }
                return true;
                //float scaling = ((float)screenWidth/2)/bmp.getWidth();
                //view.getLayoutParams().height=(int)(bmp.getHeight()*scaling);
                //view.getLayoutParams().width=(int)(bmp.getWidth()*scaling);
            }
        }
        return false;
    }
    public void removePictures()
    {
        loadPictures(true);
        pics = new ArrayList<>();
    }
}
