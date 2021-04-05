package com.example.juwelierbehrendt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Options extends AppCompatActivity {

    private Button bCatalog, bMeeting, bAddProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        bCatalog = findViewById(R.id.bCatalog);
        bMeeting = findViewById(R.id.bMeeting);
        bAddProduct = findViewById(R.id.bAddProduct);

        bCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Options.this, Catalog.class));
            }
        });
        bAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Options.this, EditProduct.class);
                i.putExtra("action","ADD");
                startActivity(i);
            }
        });
    }
}