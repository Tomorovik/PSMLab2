package com.example.tomorovik.psmlab2;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private SimpleCursorAdapter _cursorAdapter;
    private ListView _phoneList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _phoneList = (ListView) findViewById(R.id.phoneList);
        _phoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditPhoneActivity.class);
                intent.putExtra(DbHelper.ID,Long.valueOf(id));
                startActivityForResult(intent,RESULT_OK);
            }
        });

        _phoneList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        _phoneList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                toolbar.setVisibility(View.GONE);
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.context_bar_list,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.delete:
                        deleteSelected();
                        return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                toolbar.setVisibility(View.VISIBLE);
            }
        });
        startLoader();
    }

    private void startLoader() {
        getLoaderManager().initLoader(0,null,this);
        String[] mapFrom = new String[]{DbHelper.COL1,DbHelper.COL2};
        int[] mapTo = new int[]{R.id.producent, R.id.model};
        _cursorAdapter = new android.widget.SimpleCursorAdapter(this, R.layout.phone_list_row, null, mapFrom, mapTo, 0);
        _phoneList.setAdapter(_cursorAdapter);
    }

    private void deleteSelected() {
        long[] selected = _phoneList.getCheckedItemIds();
        for (int i = 0;i<selected.length;i++){
            getContentResolver().delete(ContentUris.withAppendedId(DataProvider.URI_CONTENT,selected[i]),null,null);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {
            Intent intent = new Intent(MainActivity.this, EditPhoneActivity.class);
            intent.putExtra(DbHelper.ID, (long)-1);
            startActivityForResult(intent,0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {DbHelper.ID,DbHelper.COL1,DbHelper.COL2};
        CursorLoader loaderKursora = new CursorLoader(this, DataProvider.URI_CONTENT, projection, null, null, null);
        return loaderKursora;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        _cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        _cursorAdapter.swapCursor(null);
    }
}
