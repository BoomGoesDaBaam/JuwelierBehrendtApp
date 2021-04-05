package com.example.juwelierbehrendt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


/*
        Product clock = new Product();
        clock.setName("Ebel");
        clock.setPrice("399");

        Backendless.Data.of(Product.class).save(clock, new AsyncCallback<Product>() {
            @Override
            public void handleResponse(Product response) {
                int k=2;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                int h=2;
            }
        });
*/
        Intent i = new Intent(MainActivity.this, Login.class);
        startActivity(i);

    }
}