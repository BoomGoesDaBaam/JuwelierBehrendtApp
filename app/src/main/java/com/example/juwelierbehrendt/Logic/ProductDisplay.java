package com.example.juwelierbehrendt.Logic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.juwelierbehrendt.EntitiesAndValueObjects.Product;
import com.example.juwelierbehrendt.R;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class ProductDisplay extends AppCompatActivity {
    Button bEdit,bRight,bLeft;
    String objectID;    //productID
    Product product = new Product();
    TextView tVDisplayName,tVDisplayKind,tVDisplayBrand,tVDisplayAmount,tVDisplayPrice,tVDisplayDiscount,tVDisplayDescription, tvPiccount;
    ImageView ivPic;

    int picIndex = 0, durIndex=1;
    public static int RESULT_EDIT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);

        bEdit = findViewById(R.id.bEditProduct);
        tVDisplayName= findViewById(R.id.tVDisplayName);
        tVDisplayKind= findViewById(R.id.tVDisplayKind);
        tVDisplayBrand= findViewById(R.id.tVDisplayBrand);
        tVDisplayAmount= findViewById(R.id.tVDisplayAmount);
        tVDisplayPrice= findViewById(R.id.tVDisplayPrice);
        tVDisplayDiscount= findViewById(R.id.tVDisplayDiscount);
        tVDisplayDescription= findViewById(R.id.tVDisplayDescription);
        ivPic= findViewById(R.id.ivDisplayPic);
        bRight= findViewById(R.id.bDisplayRight);
        bLeft= findViewById(R.id.bDisplayLeft);
        tvPiccount= findViewById(R.id.tvDisplayPiccount);

        objectID = getIntent().getStringExtra("objectID");
        product.setObjectId(objectID);


        bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductDisplay.this, EditProduct.class);
		        i.putExtra("action","EDIT");
		        i.putExtra("objectID",objectID);
                startActivityForResult(i,RESULT_EDIT);
            }
        });
        bLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picIndex > 0)
                {
                    picIndex--;
                }
                EditProduct.adjustButtons(ivPic, tvPiccount, product,picIndex,true);
            }
        });
        bRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picIndex + 1 < product.getPics().size())
                {
                    picIndex++;
                }
                EditProduct.adjustButtons(ivPic, tvPiccount, product,picIndex,true);
            }
        });

        Backendless.Data.of(Product.class).findById(objectID, new AsyncCallback<Product>() {
            @Override
            public void handleResponse(Product response) {
                ArrayList<Bitmap> pics = product.getPics();
                product.copyAssigne(response);
                product.setPics(pics);
                product.loadPictures(false);

                loadProduct();
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                int k=23;
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        tVDisplayPrice.setText(tVDisplayPrice.getText() + " (€):");
        tVDisplayDiscount.setText(tVDisplayDiscount.getText() + ":");
        tVDisplayAmount.setText(tVDisplayAmount.getText() + ":");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(RESULT_OK);
        if(requestCode == RESULT_EDIT && resultCode == 5)   //DELETED
        {
            finish();
        }
        if(requestCode == RESULT_EDIT && resultCode == RESULT_OK)
        {
            loadProduct();
        }
    }
    public void loadProduct()
    {
        product = EditProduct.pullProduct(objectID, ivPic, tvPiccount, picIndex, true,
                new ViewsInitilizer(tVDisplayName, tVDisplayKind,tVDisplayBrand, tVDisplayAmount, tVDisplayPrice, tVDisplayDiscount, tVDisplayDescription));
        EditProduct.adjustButtons(ivPic, tvPiccount, product,picIndex,true);
    }
}