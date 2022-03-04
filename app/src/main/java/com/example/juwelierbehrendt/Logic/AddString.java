package com.example.juwelierbehrendt.Logic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.juwelierbehrendt.EntitiesAndValueObjects.Brand;
import com.example.juwelierbehrendt.EntitiesAndValueObjects.Kind;
import com.example.juwelierbehrendt.R;

public class AddString extends AppCompatActivity {

    EditText eTEditString;
    Button bEditStringSave,bEditStringDelete;
    TextView tVAddStringHead;
    Brand string2Save;
    Kind kind;
    String action,objectID;
    boolean isBrandAction=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_string);

        eTEditString = findViewById(R.id.eTEditString);
        bEditStringSave = findViewById(R.id.bEditStringSave);
        bEditStringDelete = findViewById(R.id.bEditStringDelete);
        tVAddStringHead = findViewById(R.id.tVAddStringHead);

        eTEditString.setText(getIntent().getStringExtra("string"));

        action = getIntent().getStringExtra("action");
        objectID = getIntent().getStringExtra("objectID");
        string2Save = new Brand();
        kind = new Kind();
        if(action.equals("edit brand"))
        {
            tVAddStringHead.setText("Edit Brand");
            string2Save.setObjectId(objectID);
            isBrandAction=true;

        }else if(action.equals("add brand"))
        {
            tVAddStringHead.setText("Add Brand");
            bEditStringDelete.setVisibility(View.GONE);
            isBrandAction=true;
        }else if(action.equals("add kind"))
        {
            tVAddStringHead.setText("Add Kind");
            bEditStringDelete.setVisibility(View.GONE);
        }else if(action.equals("edit kind"))
        {
            tVAddStringHead.setText("Edit Kind");
            kind.setObjectId(objectID);
        }
        bEditStringSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String s = eTEditString.getText().toString();
                if(isBrandAction) {
                    string2Save.setStringtext(s);
                    Backendless.Data.of(Brand.class).save(string2Save, new AsyncCallback<Brand>() {
                        @Override
                        public void handleResponse(Brand response) {
                            Toast.makeText(AddString.this, "Brand edited!", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(AddString.this, "Error:" + fault.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                else
                {
                    kind.setStringtext(s);
                    Backendless.Data.of(Kind.class).save(kind, new AsyncCallback<Kind>() {
                        @Override
                        public void handleResponse(Kind response) {
                            Toast.makeText(AddString.this, "Kind edited!", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(AddString.this, "Error:" + fault.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

        bEditStringDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBrandAction) {
                    Backendless.Data.of(Brand.class).remove(string2Save, new AsyncCallback<Long>() {
                        @Override
                        public void handleResponse(Long response) {
                            Toast.makeText(AddString.this, "Brand deleted!", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(AddString.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    Backendless.Data.of(Kind.class).remove(kind, new AsyncCallback<Long>() {
                        @Override
                        public void handleResponse(Long response) {
                            Toast.makeText(AddString.this, "Kind deleted!", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(AddString.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}