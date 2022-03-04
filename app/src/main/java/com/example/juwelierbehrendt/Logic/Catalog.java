package com.example.juwelierbehrendt.Logic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.juwelierbehrendt.EntitiesAndValueObjects.Product;
import com.example.juwelierbehrendt.R;
import com.example.juwelierbehrendt.RecyclerView.EntryAdapter;

import java.util.ArrayList;
import java.util.List;

public class Catalog extends AppCompatActivity implements AdapterView.OnItemSelectedListener, EntryAdapter.ItemClicked {

    //Recyclerview
    RecyclerView recyclerView;
    RecyclerView.Adapter adapterRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Product> products;
    //
    EditText eTSearch;
    //
    boolean activitySleeping = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        //Setting up Recyclerview
        recyclerView = findViewById(R.id.listProducts);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //Get Products and update Recyclerview
        eTSearch = findViewById(R.id.eTSearchName);
        loadProductsFilter("All", "", false);
        //Listener
        eTSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //String brand = spinnerBrands.getSelectedItem().toString();
                loadProductsFilter("", eTSearch.getText().toString(), false);
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
                String strLower = incString.toLowerCase();
                String tmp = strLower.trim();
                tmp = tmp.trim();
                while(tmp.lastIndexOf("  ") != -1)
                {
                    tmp = tmp.replace("  "," ");
                }
                String[] strings = tmp.split(" ");

                for(int i=0;i<response.size();i++)
                {
                    Product p = response.get(i);
                    int count = 0;
                    for(int x=0;x< strings.length;x++) {

                        if (p.isListedInSearch(strings[x]))
                        {
                            count++;
                        }
                    }
                    if(count == strings.length)
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

        if(!activitySleeping)
        {
            activitySleeping = true;
            Intent i = new Intent(Catalog.this, ProductDisplay.class);
            i.putExtra("objectID",products.get(index).getObjectId());
            startActivityForResult(i,4);    //4 = Update after return
        }

    }
    //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activitySleeping = false;
        if(requestCode == 4 && resultCode == RESULT_OK)//4 = Update after return
        {
            //String brand = spinnerBrands.getSelectedItem().toString();
            loadProductsFilter("", eTSearch.getText().toString(), false);
        }
    }
}