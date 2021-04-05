package com.example.juwelierbehrendt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.io.IOException;

public class EditProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final int PICK_IMAGE = 1;
    Uri imageUri;

    Spinner spinnerBrands,spinnerWhat;
    Button bLeft,bRight,bAddPic,bDelPic,bSave,bDel;
    EditText eTnameOfProduct, eTprice, eTDiscount, eTotherBrand;
    ImageView iVProd;
    TextView tVHeadline;

    Product product;
    String action = "-";
    int picIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        //SpinnerBrand
        spinnerBrands = findViewById(R.id.spinnerEditBrand);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.brands, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerBrands.setAdapter(adapter);
        spinnerBrands.setOnItemSelectedListener(this);

        //SpinnerWhat
        spinnerBrands = findViewById(R.id.spinnerWhat);
        ArrayAdapter<CharSequence> adapterWhat = ArrayAdapter.createFromResource(this, R.array.jewelryType, R.layout.support_simple_spinner_dropdown_item);
        adapterWhat.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerBrands.setAdapter(adapterWhat);
        spinnerBrands.setOnItemSelectedListener(this);

        bLeft = findViewById(R.id.bLeft);
        bRight = findViewById(R.id.bRight);
        bAddPic = findViewById(R.id.bAddPic);
        bDelPic = findViewById(R.id.bDeletePic);
        bSave = findViewById(R.id.bSaveEdit);
        bDel = findViewById(R.id.bEditDelete);
        eTnameOfProduct = findViewById(R.id.eTNameEditProduct);
        eTprice = findViewById(R.id.eTPrice);
        eTDiscount = findViewById(R.id.eTDiscountEdit);
        iVProd = findViewById(R.id.iVProd);
        tVHeadline = findViewById(R.id.tVEditHeadline);
        eTotherBrand = findViewById(R.id.eTotherBrand);

        action = getIntent().getStringExtra("action");
        if(action.equals("ADD"))
        {
            product = new Product();
            tVHeadline.setText("Add Product");
        }
        else if(action.equals("EDIT"))
        {
            product = new Product();
            String iD = getIntent().getStringExtra("objectID");
            Backendless.Data.of(Product.class).findById(iD, new AsyncCallback<Product>() {
                        @Override
                        public void handleResponse(Product response) {
                            product = response;
                            eTnameOfProduct.setText(product.getName());
                            spinnerBrands.setSelection(1);
                            eTprice.setText(String.valueOf(product.getPrice()));
                            eTDiscount.setText(String.valueOf(product.getDiscount()));
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                        }
                    });
                    tVHeadline.setText("Edit Product");
        }
        adjustButtons();

        bAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
            }
        });
        bDelPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product.getPics().size() > 0 && picIndex >= 0 && picIndex < product.getPics().size()){
                    product.deletePic(picIndex);
                    picIndex--;
                    if(picIndex < 0)
                    {
                        picIndex = 0;
                    }
                    if(product.getPics().size() > 0)
                    {
                        iVProd.setImageBitmap(product.getPics().get(picIndex));
                    }else
                    {
                        iVProd.setImageBitmap(null);
                    }
                    adjustButtons();
                }
            }
        });

        bLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picIndex > 0)
                {
                    picIndex--;
                }
                adjustButtons();
            }
        });
        bRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picIndex + 1 < product.getPics().size())
                {
                    picIndex++;
                }
                adjustButtons();
            }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Setbrand
                if(spinnerBrands.getSelectedItemId() == 0 && eTotherBrand.getText().toString().isEmpty())
                {
                    Toast.makeText(EditProduct.this, "Please select a brand.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    if(spinnerBrands.getSelectedItemId() == 0)
                    {
                        product.setBrand(eTotherBrand.getText().toString());
                    }
                    else
                    {
                        product.setBrand(spinnerBrands.getSelectedItem().toString());
                    }
                }
                //Setname
                if(eTnameOfProduct.getText().toString().isEmpty())
                {
                    Toast.makeText(EditProduct.this, "Please pick a name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    product.setName(eTnameOfProduct.getText().toString());
                }
                //SetPrice
                try {
                    if(eTprice.getText().toString().isEmpty())
                    {
                        Toast.makeText(EditProduct.this, "Please pick a price.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Float newPrice = Float.valueOf(eTprice.getText().toString());
                    product.setPrice(newPrice);
                }catch(NumberFormatException e)
                {
                    Toast.makeText(EditProduct.this, "Price field has invalid input", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Set Discount
                try {
                    if(eTDiscount.getText().toString().isEmpty())
                    {
                        Toast.makeText(EditProduct.this, "Please pick a discount.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int newDis = Integer.valueOf(eTDiscount.getText().toString());
                    product.setDiscount(newDis);
                }catch(NumberFormatException e)
                {
                    Toast.makeText(EditProduct.this, "Discount field has invalid input", Toast.LENGTH_SHORT).show();
                }
                //
                if(action.equals("ADD")) {
                    Backendless.Data.of(Product.class).save(product, new AsyncCallback<Product>() {
                        @Override
                        public void handleResponse(Product response) {
                            Toast.makeText(EditProduct.this, "Product added!", Toast.LENGTH_SHORT).show();
                            product = new Product();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(EditProduct.this, "Error:" + fault.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                if(action.equals("EDIT")) {
                    Backendless.Data.of(Product.class).save(product, new AsyncCallback<Product>() {
                        @Override
                        public void handleResponse(Product response) {
                            Toast.makeText(EditProduct.this, "Product edited!", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {


                        }
                    });
                }
                //Product p = Backendless.Data.of(Product.class).findFirst();
                //p.setPrice(399f);
            }
        });

        bDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Backendless.Data.of(Product.class).remove(product, new AsyncCallback<Long>() {
                    @Override
                    public void handleResponse(Long response) {
                        Toast.makeText(EditProduct.this, "Product deleted!", Toast.LENGTH_SHORT).show();

                        setResult(5);       //DELETED
                        finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(EditProduct.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    //help methods
    public void adjustButtons()
    {
        if(picIndex > 0)
        {
            bLeft.setVisibility(View.VISIBLE);
        }else
        {
            bLeft.setVisibility(View.INVISIBLE);
        }
        if(picIndex + 1 < product.getPics().size())
        {
            bRight.setVisibility(View.VISIBLE);
        }else
        {
            bRight.setVisibility(View.INVISIBLE);
        }
        if(product.getPics().size() > 0) {
            iVProd.setImageBitmap(product.getPics().get(picIndex));
        }
    }

    //Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    //RESULT
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK)
        {
            imageUri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                //iVProd.setImageBitmap(bitmap);
                product.addPic(bitmap);
                adjustButtons();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}