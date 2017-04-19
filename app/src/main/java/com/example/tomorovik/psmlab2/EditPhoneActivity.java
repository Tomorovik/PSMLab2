package com.example.tomorovik.psmlab2;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
*/
public class EditPhoneActivity extends Activity {

    private long rowId;
    private EditText ETproducent;
    private EditText ETmodel;
    private EditText ETandroidVersion;
    private EditText ETweb;
    private Button BTNweb;
    private Button BTNcancel;
    private Button BTNsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);

        ETproducent = (EditText)findViewById(R.id.ETproducent);
        ETmodel = (EditText)findViewById(R.id.ETmodel);
        ETandroidVersion = (EditText)findViewById(R.id.ETandroidVersion);
        ETweb = (EditText)findViewById(R.id.ETweb);

        BTNweb= (Button) findViewById(R.id.BTNweb);
        BTNcancel= (Button)findViewById(R.id.BTNcancel);
        BTNsave= (Button)findViewById(R.id.BTNsave);

        rowId = getIntent().getExtras().getLong(DbHelper.ID);

        if(rowId != -1)
            fillFields();


        BTNweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String website = ETweb.getText().toString();
                if(!(website.startsWith("http://") || website.startsWith("https://")))
                    website = "http://";
                Intent browser = new Intent("android.intent.action.VIEW", Uri.parse(website));
                startActivity(browser);
            }
        });

        BTNcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        BTNsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        if(checkFields()){
            ContentValues wartosc = new ContentValues();
            wartosc.put(DbHelper.COL1, ETproducent.getText().toString());
            wartosc.put(DbHelper.COL2, ETmodel.getText().toString());
            wartosc.put(DbHelper.COL3, ETandroidVersion.getText().toString());
            wartosc.put(DbHelper.COL4, ETweb.getText().toString());
            if(rowId == -1) {
                ContentResolver cr = getContentResolver();
                Uri uriNowego = cr.insert(DataProvider.URI_CONTENT, wartosc);
                uriNowego.getLastPathSegment();
            }
            else
                getContentResolver().update(ContentUris.withAppendedId(DataProvider.URI_CONTENT, rowId), wartosc, null, null);
            setResult(RESULT_OK);
            finish();
        }
        else
            Toast.makeText(this, R.string.fill, Toast.LENGTH_SHORT).show();
    }

    private boolean checkFields() {
        return !(ETproducent.getText().toString().equals("")
                || ETmodel.getText().toString().equals("")
                || ETandroidVersion.getText().toString().equals("")
                || ETweb.getText().toString().equals(""));
    }

    private void fillFields() {
        String projekcja[] = { DbHelper.COL1,
                DbHelper.COL2,
                DbHelper.COL3,
                DbHelper.COL4 };
        Cursor kursor = getContentResolver().query(ContentUris.withAppendedId(DataProvider.URI_CONTENT, rowId), projekcja, null, null, null);
        kursor.moveToFirst();
        int indeksKolumny = kursor.getColumnIndexOrThrow(DbHelper.COL1);
        String wartosc = kursor.getString(indeksKolumny);
        ETproducent.setText(wartosc);
        indeksKolumny = kursor.getColumnIndexOrThrow(DbHelper.COL2);
        wartosc = kursor.getString(indeksKolumny);
        ETmodel.setText(wartosc);
        indeksKolumny = kursor.getColumnIndexOrThrow(DbHelper.COL3);
        wartosc = kursor.getString(indeksKolumny);
        ETandroidVersion.setText(wartosc);
        indeksKolumny = kursor.getColumnIndexOrThrow(DbHelper.COL4);
        wartosc = kursor.getString(indeksKolumny);
        ETweb.setText(wartosc);
    }
}
