package com.example.juwelierbehrendt.Logic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.juwelierbehrendt.EntitiesAndValueObjects.Product;
import com.example.juwelierbehrendt.R;

import java.util.ArrayList;

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
                adjustButtons(true);
            }
        });
        bRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picIndex + 1 < product.getPics().size())
                {
                    picIndex++;
                }
                adjustButtons(true);
            }
        });
        product.setObjectId(objectID);
        product.loadAll(tVDisplayName, tVDisplayKind, tVDisplayBrand, tVDisplayAmount, tVDisplayPrice, tVDisplayDiscount, tVDisplayDescription);

        adjustButtons(true);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        tVDisplayPrice.setText(tVDisplayPrice.getText() + " (€):");
        tVDisplayDiscount.setText(tVDisplayDiscount.getText() + ":");
        tVDisplayAmount.setText(tVDisplayAmount.getText() + ":");
    }



    public void adjustButtons(boolean resetPic) {
        /*
        if (picIndex > 0) {
            //bLeft.setVisibility(View.VISIBLE);
        } else {
            // bLeft.setVisibility(View.INVISIBLE);
        }
        if (picIndex + 1 < product.getPics().size()) {
            try {
                //bRight.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                int K = 23;
            }
        } else {
            //bRight.setVisibility(View.INVISIBLE);
        }
        */
        if (product.getPics().size() > 0 && resetPic) {
            ivPic.setVisibility(View.VISIBLE);
            ivPic.setImageBitmap(product.getPicAt(picIndex));
        }
        if(product.getPics().size() == 0)
        {
            tvPiccount.setText("No Pictures");
        }
        else
        {
            tvPiccount.setText("Picture (" + String.valueOf(picIndex + 1) + "/" + product.getPics().size() + ")");
        }
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
            product.setPics(new ArrayList<>());
            product.loadAll(tVDisplayName, tVDisplayKind, tVDisplayBrand, tVDisplayAmount, tVDisplayPrice, tVDisplayDiscount, tVDisplayDescription);
            adjustButtons(true);
        }
    }
}