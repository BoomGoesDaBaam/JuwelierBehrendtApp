package com.example.juwelierbehrendt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class ProductDisplay extends AppCompatActivity {

    Button bEdit;
    String objectID;
    Product product;

    public static int RESULT_EDIT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);

        bEdit = findViewById(R.id.bEditProduct);
        objectID = getIntent().getStringExtra("objectID");

        bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductDisplay.this, EditProduct.class);
		        i.putExtra("action","EDIT");
		        i.putExtra("objectID",objectID);
                startActivityForResult(i,RESULT_EDIT);
            }
        });

        Backendless.Data.of(Product.class).findById(getIntent().getStringExtra("objectID"), new AsyncCallback<Product>() {
            @Override
            public void handleResponse(Product response) {
                /*
                product = response;
                eTnameOfProduct.setText(product.getName());
                spinnerBrands.setSelection(1);
                eTprice.setText(String.valueOf(product.getPrice()));
                eTDiscount.setText(String.valueOf(product.getDiscount()));

                 */
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(RESULT_OK);
        if(requestCode == RESULT_EDIT && resultCode == 5)   //DELETED
        {
            finish();
        }
    }
}