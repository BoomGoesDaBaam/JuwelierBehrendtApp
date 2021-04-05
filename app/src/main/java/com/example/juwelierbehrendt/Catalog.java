package com.example.juwelierbehrendt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import weborb.util.ObjectProperty;

public class Catalog extends AppCompatActivity implements AdapterView.OnItemSelectedListener,EntryAdapter.ItemClicked {
    //Spinner
    Spinner spinnerBrands;
    //Recyclerview
    RecyclerView recyclerView;
    RecyclerView.Adapter adapterRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Product> products;
    //
    EditText eTName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        //Spinner
        spinnerBrands = findViewById(R.id.spinnerBrands);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.brands, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerBrands.setAdapter(adapter);
        spinnerBrands.setOnItemSelectedListener(this);
        //Setting up Recyclerview
        recyclerView = findViewById(R.id.listProducts);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //Get Products and update Recyclerview
        eTName = findViewById(R.id.eTSearchName);
        loadProductsFilter("All", "", false);
        //Listener
        spinnerBrands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String brand = parent.getItemAtPosition(position).toString();
                loadProductsFilter(brand, eTName.getText().toString(), false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        eTName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String brand = spinnerBrands.getSelectedItem().toString();
                loadProductsFilter(brand, eTName.getText().toString(), false);
                return false;
            }
        });
    }
    public void loadProductsFilter(String brand, String incString, boolean discounted)
    {
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setPageSize(50);
        Backendless.Persistence.of(Product.class).find(builder, new AsyncCallback<List<Product>>() {
            @Override
            public void handleResponse(List<Product> response) {
                ArrayList selection = new ArrayList<>(response);
                products = new ArrayList<Product>();
                for(int i=0;i<response.size();i++)
                {
                    Product p = response.get(i);
                    boolean brandd = (p.getBrand().equals(brand) || brand.equals("All"));
                    boolean namee = p.getName().contains(incString);
                    boolean discount = ((discounted && p.getDiscount() > 0) || !discounted);
                    if( (p.getBrand().equals(brand) || brand.equals("All")) &&
                            p.getName().contains(incString) &&
                            ((discounted && p.getDiscount() > 0) || !discounted))
                    {
                        products.add(p);
                    }
                }
                updateRecyclerView();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }
    public void updateRecyclerView()
    {
        adapterRecyclerView = new EntryAdapter(this,products);
        recyclerView.setAdapter(adapterRecyclerView);
    }
    //Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String s = parent.getItemAtPosition(position).toString();
        Toast.makeText(Catalog.this,s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    //Recyclerview
    @Override
    public void onItemClicked(int index) {
        Intent i = new Intent(Catalog.this, ProductDisplay.class);
        i.putExtra("objectID",products.get(index).getObjectId());
        startActivityForResult(i,4);    //4 = Update after return
    }
    //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 4 && resultCode == RESULT_OK)
        {
            String brand = spinnerBrands.getSelectedItem().toString();
            loadProductsFilter(brand, eTName.getText().toString(), false);
        }
    }
}