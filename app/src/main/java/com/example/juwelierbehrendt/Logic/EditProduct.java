package com.example.juwelierbehrendt.Logic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.persistence.DataQueryBuilder;
import com.example.juwelierbehrendt.EntitiesAndValueObjects.Brand;
import com.example.juwelierbehrendt.EntitiesAndValueObjects.Kind;
import com.example.juwelierbehrendt.EntitiesAndValueObjects.Product;
import com.example.juwelierbehrendt.R;
import com.example.juwelierbehrendt.RecyclerView.EntryAdapterStringDisplay;

import java.io.IOException;
import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import lombok.AllArgsConstructor;

public class EditProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener, EntryAdapterStringDisplay.ItemClicked, Callable<Product> {

    private static final int PICK_IMAGE = 1;
    private static final int CHANGE_BRAND = 2;
    private static final int CHANGE_KIND = 3;
    Uri imageUri;

    //Spinner spinnerBrands,spinnerWhat;
    Button bLeft,bRight,bAddPic,bDelPic,bSave,bDel,bEditProdAddBrand,bEditProdAddKind;
    EditText eTprice, eTDiscount, etNameOfProduct, eTDescription, etEditProductAmount;//, eTotherBrand;eTnameOfProduct
    ImageView iVProd;
    TextView tVHeadline, tvBrand,tvKind,tvEditProdPiccount;
    Switch swEdit,swEditKind;

    Product product;
    String action = "-", brand="", nameOfProduct="", objectID="";
    int picIndex=0,savedTimes=0;
    boolean busy = false;
    boolean saveProduct = false;
    //Recyclerview
    //#1
    RecyclerView recyclerViewBrand;
    RecyclerView.Adapter adapterBrand;
    RecyclerView.LayoutManager layoutManagerBrand;
    ArrayList<String> brands;
    //#2
    RecyclerView recyclerViewKind;
    RecyclerView.Adapter adapterKind;
    RecyclerView.LayoutManager layoutManagerKind;
    ArrayList<String> kinds;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        loadViewsById();
        setLocale("de");


        if(action.equals("ADD"))
        {
            tVHeadline.setText(R.string.add_product);
            Backendless.Data.of(Product.class).save(product, new AsyncCallback<Product>() {
                @Override
                public void handleResponse(Product response) {
                    product.setObjectId(response.getObjectId());
                }
                @Override
                public void handleFault(BackendlessFault fault) {
                }
            });
        }
        else if(action.equals("EDIT"))
        {
            tVHeadline.setText(R.string.edit_product);
            product = pullProduct(objectID, iVProd, tvEditProdPiccount, picIndex, true
                    , new ViewsInitilizer(etNameOfProduct, tvKind,tvBrand, etEditProductAmount, eTprice, eTDiscount, eTDescription));
        }
        loadListener();
    }
    public void delPicFromDataBase(int picIndex) {
        Backendless.Files.remove("ProductsPics/Product" + objectID + "Pic" + String.valueOf(picIndex), new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
                Toast.makeText(EditProduct.this, R.string.pic_deleted, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(EditProduct.this, R.string.pic_not_deleted, Toast.LENGTH_SHORT).show();
            }
        });
    }
    //help methods
    public static void adjustButtons(ImageView iVProd, TextView tvEditProdPiccount, Product product, int picIndex, boolean resetPic)
    {
        /*
        if(picIndex > 0)
        {
            //bLeft.setVisibility(View.VISIBLE);
        }else
        {
           // bLeft.setVisibility(View.INVISIBLE);
        }
        if(picIndex + 1 < product.getPics().size())
        {
            try {
                //bRight.setVisibility(View.VISIBLE);
            }catch (Exception e)
            {
                int K=23;
            }
        }
        else
        {
            //bRight.setVisibility(View.INVISIBLE);
        }
         */

        if(product.getPics().size() > 0 && resetPic) {
            iVProd.setVisibility(View.VISIBLE);
            iVProd.setImageBitmap(product.getPics().get(picIndex));
        }
        if(product.getPics().size() > 0)
        {
            tvEditProdPiccount.setText(iVProd.getContext().getText(R.string.picture)+": ("+String.valueOf(picIndex+1)+"/"+product.getPics().size()+")");
        }
        else
            tvEditProdPiccount.setText(iVProd.getContext().getText(R.string.picture)+": (0/0)");

    }

    static Product staticProd;
    public static Product pullProduct(String objectID,
                                     ImageView iVProd,
                                     TextView tvEditProdPiccount,
                                     int picIndex,
                                     boolean resetPic,
                                     ViewsInitilizer viewsInitilizer)
    {
        staticProd = new Product();
        Backendless.Data.of(Product.class).findById(objectID, new AsyncCallback<Product>() {
            @Override
            public void handleResponse(Product response) {
                ArrayList<Bitmap> pics = staticProd.getPics();
                staticProd.copyAssigne(response);
                staticProd.setPics(pics);

                staticProd.loadPictures(false);
                adjustButtons(iVProd, tvEditProdPiccount, staticProd,picIndex,resetPic);
                viewsInitilizer.initilize(staticProd);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
        return staticProd;
    }
    public void saveData()
    {
        saveData(0, false);
    }
    public void saveData(@StringRes int successResponse, boolean terminateActivity)
    {
        savePics();
        Backendless.Data.of(Product.class).save(product, new AsyncCallback<Product>() {
            @Override
            public void handleResponse(Product response) {
                if(successResponse != 0)
                    Toast.makeText(EditProduct.this, successResponse, Toast.LENGTH_SHORT).show();
                if(terminateActivity)
                {
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(EditProduct.this, R.string.save_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void savePics()
    {
        busy = true;
        savedTimes=0;
        for(int i=0;i<product.getPics().size();i++) {
            String filename ="Product"+product.getObjectId()+"Pic"+i;
            Backendless.Files.Android.upload( product.getPics().get(i),
                    Bitmap.CompressFormat.PNG,
                    100,
                    filename,
                    "ProductsPics",
                    true,
                    new AsyncCallback<BackendlessFile>()
                    {
                        @Override
                        public void handleResponse( final BackendlessFile backendlessFile )
                        {
                            savedTimes++;
                            if(savedTimes == product.getPics().size())
                            {
                                Toast.makeText(EditProduct.this, R.string.picture_saved, Toast.LENGTH_SHORT).show();
                            }
                            busy = false;
                        }

                        @Override
                        public void handleFault( BackendlessFault backendlessFault )
                        {
                            Toast.makeText(EditProduct.this, R.string.picture_not_saved, Toast.LENGTH_SHORT).show();
                            busy = false;
                        }
                    });

        }
    }
    //Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK)
        {
            imageUri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                //iVProd.setImageBitmap(bitmap);
                product.addPic(bitmap, true);
                savePics();
                saveData();
                adjustButtons(iVProd, tvEditProdPiccount, product,picIndex,true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode == CHANGE_BRAND && resultCode == RESULT_OK)
        {
            loadBrands();
        }
        if(requestCode == CHANGE_KIND && resultCode == RESULT_OK)
        {
            loadKinds();
        }
    }
    //Recyclerview
    @Override
    public void onItemClicked(int index) {

        EntryAdapterStringDisplay eaBrand = (EntryAdapterStringDisplay)adapterBrand;
        EntryAdapterStringDisplay eaKind = (EntryAdapterStringDisplay)adapterKind;
        //RecyclerView.Adapter<EntryAdapterStringDisplay.ViewHolderString>
        if(eaBrand.getLastPress() > eaKind.getLastPress() && swEdit.isChecked())
        {
            DataQueryBuilder builder = DataQueryBuilder.create();
            builder.setPageSize(50);
            Backendless.Persistence.of(Brand.class).find(builder, new AsyncCallback<List<Brand>>() {
                @Override
                public void handleResponse(List<Brand> response) {
                    for(int i=0;i<response.size();i++)
                    {
                        brands.add(response.get(i).getStringtext());
                        if(response.get(i).getStringtext().equals(brands.get(index)))
                        {
                            Intent intent = new Intent(EditProduct.this, AddString.class);
                            intent.putExtra("string",brands.get(index));
                            intent.putExtra("objectID",response.get(i).getObjectId());
                            intent.putExtra("action","edit brand");
                            startActivityForResult(intent, CHANGE_BRAND);
                            break;
                        }
                    }
                }

                @Override
                public void handleFault(BackendlessFault fault) {

                }
            });
        }
        else if(eaBrand.getLastPress() > eaKind.getLastPress()){
            tvBrand.setText(brands.get(index));
        }
        if(eaBrand.getLastPress() < eaKind.getLastPress() && swEditKind.isChecked())
        {
            DataQueryBuilder builder = DataQueryBuilder.create();
            builder.setPageSize(50);
            Backendless.Persistence.of(Kind.class).find(builder, new AsyncCallback<List<Kind>>() {
                @Override
                public void handleResponse(List<Kind> response) {
                    for(int i=0;i<response.size();i++)
                    {
                        kinds.add(response.get(i).getStringtext());
                        if(response.get(i).getStringtext().equals(kinds.get(index)))
                        {
                            Intent intent = new Intent(EditProduct.this, AddString.class);
                            intent.putExtra("string",kinds.get(index));
                            intent.putExtra("objectID",response.get(i).getObjectId());
                            intent.putExtra("action","edit kind");
                            startActivityForResult(intent, CHANGE_KIND);
                            break;
                        }
                    }
                }

                @Override
                public void handleFault(BackendlessFault fault) {

                }
            });
        }
        else if(eaBrand.getLastPress() < eaKind.getLastPress()){
            tvKind.setText(kinds.get(index));
        }
        //Intent i = new Intent(EditProduct.this, ProductDisplay.class);
        //i.putExtra("objectID",products.get(index).getObjectId());
        //startActivityForResult(i,4);    //4 = Update after return
    }
    public void loadBrands()
    {
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setPageSize(50);
        Backendless.Persistence.of(Brand.class).find(builder, new AsyncCallback<List<Brand>>() {
            @Override
            public void handleResponse(List<Brand> response) {
                ArrayList selection = new ArrayList<>(response);
                brands = new ArrayList<String>();
                for(int i=0;i<response.size();i++)
                {
                    brands.add(response.get(i).getStringtext());
                }
                updateRecyclerViewBrands();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                int failed=0;
            }
        });
    }
    public void updateRecyclerViewBrands()
    {
        adapterBrand = new EntryAdapterStringDisplay(this,brands);
        recyclerViewBrand.setAdapter(adapterBrand);
    }
    public void loadKinds()
    {
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setPageSize(50);
        Backendless.Persistence.of(Kind.class).find(builder, new AsyncCallback<List<Kind>>() {
            @Override
            public void handleResponse(List<Kind> response) {
                ArrayList selection = new ArrayList<>(response);
                kinds = new ArrayList<String>();
                for(int i=0;i<response.size();i++)
                {
                    kinds.add(response.get(i).getStringtext());
                }
                updateRecyclerViewKinds();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }
    public void updateRecyclerViewKinds()
    {
        adapterKind = new EntryAdapterStringDisplay(this,kinds);
        recyclerViewKind.setAdapter(adapterKind);
    }
    private void loadViewsById()
    {
        //#1
        brands = new ArrayList<>();
        recyclerViewBrand = findViewById(R.id.recyV_Brand);
        recyclerViewBrand.setHasFixedSize(true);
        layoutManagerBrand = new LinearLayoutManager(this);
        recyclerViewBrand.setLayoutManager(layoutManagerBrand);
        adapterBrand = new EntryAdapterStringDisplay(this,brands);
        recyclerViewBrand.setAdapter(adapterBrand);
        loadBrands();
        //#2
        kinds = new ArrayList<>();
        recyclerViewKind = findViewById(R.id.recyV_Kind);
        recyclerViewKind.setHasFixedSize(true);
        layoutManagerKind = new LinearLayoutManager(this);
        recyclerViewKind.setLayoutManager(layoutManagerKind);
        adapterKind = new EntryAdapterStringDisplay(this,kinds);
        recyclerViewKind.setAdapter(adapterKind);
        loadKinds();
        /*
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
        */
        etNameOfProduct = findViewById(R.id.eTNameEditProduct);
        eTDescription = findViewById(R.id.eTEditDiscr);
        bLeft = findViewById(R.id.bLeft);
        bRight = findViewById(R.id.bRight);
        bAddPic = findViewById(R.id.bAddPic);
        bDelPic = findViewById(R.id.bDeletePic);
        bSave = findViewById(R.id.bSaveEdit);
        bDel = findViewById(R.id.bEditDelete);
        eTprice = findViewById(R.id.eTPrice);
        eTDiscount = findViewById(R.id.eTDiscountEdit);
        iVProd = findViewById(R.id.iVProd);
        tVHeadline = findViewById(R.id.tVEditHeadline);
        tvBrand = findViewById(R.id.tvBrand);
        swEdit = findViewById(R.id.swEditProdEdit);
        bEditProdAddBrand = findViewById(R.id.bEditProdAddBrand);
        tvEditProdPiccount = findViewById(R.id.tvEditProdPiccount);

        swEditKind = findViewById(R.id.swEditKind);
        tvKind = findViewById(R.id.tvKind);
        bEditProdAddKind = findViewById(R.id.bEditProdAddKind);
        etEditProductAmount = findViewById(R.id.etEditProductAmount);

        objectID = getIntent().getStringExtra("objectID");
        action = getIntent().getStringExtra("action");
        product = new Product();
        product.setObjectId(objectID);
    }
    private void loadListener()
    {
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
                    delPicFromDataBase(product.getPics().size()-1);
                    product.deletePic(picIndex, true);


                    saveData();
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
                    adjustButtons(iVProd, tvEditProdPiccount, product,picIndex,true);
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
                adjustButtons(iVProd, tvEditProdPiccount, product,picIndex,true);
            }
        });
        bRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picIndex + 1 < product.getPics().size())
                {
                    picIndex++;
                }
                adjustButtons(iVProd, tvEditProdPiccount, product,picIndex,true);
            }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(busy)
                {
                    return;
                }
                product.setAmount(Integer.parseInt(etEditProductAmount.getText().toString()));
                product.setBrand(tvBrand.getText().toString());
                product.setWhat(tvKind.getText().toString());
                //Set description
                product.setDescription(eTDescription.getText().toString());
                //Setname
                if(etNameOfProduct.getText().toString().isEmpty())
                {
                    Toast.makeText(EditProduct.this, R.string.pleasePickProdName, Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    product.setName(etNameOfProduct.getText().toString());
                }
                //SetPrice
                try {
                    if(eTprice.getText().toString().isEmpty())
                    {
                        Toast.makeText(EditProduct.this, R.string.pleasePickProdPrice, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Float newPrice = Float.valueOf(eTprice.getText().toString());
                    product.setPrice(newPrice);
                }catch(NumberFormatException e)
                {
                    Toast.makeText(EditProduct.this, R.string.priceInvalidInput, Toast.LENGTH_SHORT).show();
                    return;
                }
                //Set Discount
                try {
                    int newDis = (eTDiscount.getText().toString().isEmpty())?0:Integer.valueOf(eTDiscount.getText().toString());
                    product.setDiscount(newDis);
                }catch(NumberFormatException e)
                {
                    Toast.makeText(EditProduct.this, R.string.disInvalidInput, Toast.LENGTH_SHORT).show();
                }
                //
                if(action.equals("ADD")) {
                    saveData(R.string.prodAdded, true);
                }
                if(action.equals("EDIT")) {
                    saveData(R.string.prodEdited, true);
                }
                //Product p = Backendless.Data.of(Product.class).findFirst();
                //p.setPrice(399f);
            }
        });

        bDel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //int k=23;
                product.removePictures();
                Backendless.Data.of(Product.class).remove(product, new AsyncCallback<Long>() {
                    @Override
                    public void handleResponse(Long response) {
                        Toast.makeText(EditProduct.this, R.string.prodDeleted, Toast.LENGTH_SHORT).show();
                        product = null;
                        setResult(5);       //DELETED
                        finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(EditProduct.this, R.string.error + ": "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        bEditProdAddBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProduct.this, AddString.class);
                i.putExtra("string","");
                i.putExtra("action","add brand");
                startActivityForResult(i, CHANGE_BRAND);
            }
        });
        bEditProdAddKind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProduct.this, AddString.class);
                i.putExtra("string","");
                i.putExtra("action","add kind");
                startActivityForResult(i, CHANGE_KIND);
            }
        });
        adjustButtons(iVProd, tvEditProdPiccount, product,picIndex,true);
    }
    public void setLocale(String language)
    {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = new Locale(language);
        resources.updateConfiguration(configuration, metrics);
        onConfigurationChanged(configuration);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        tvBrand.setText(R.string.app_name);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!(action.equals("EDIT") || saveProduct || product == null))
        {
            Backendless.Data.of(Product.class).remove(product, new AsyncCallback<Long>() {
                @Override
                public void handleResponse(Long response) {
                    setResult(5);       //DELETED
                    finish();
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(EditProduct.this, R.string.errorCode + ": 1", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public Product call() throws Exception {
        return null;
    }
}