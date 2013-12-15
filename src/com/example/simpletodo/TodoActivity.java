package com.example.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoActivity extends Activity {
	private ArrayList<String> items;
	private ArrayAdapter<String> itemsAdapter;
	private ListView lvItems;
	private EditText etNewItem;
	private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        lvItems = (ListView) findViewById(R.id.lvItems);
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        readItems();
        itemsAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        setupItemEditListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo, menu);
        return true;
    }
    
    // Add the EditText item to ListView
    public void addTodoItem(View v) {
    	if(!etNewItem.getText().toString().isEmpty()) {
        	itemsAdapter.add(etNewItem.getText().toString());
        	etNewItem.setText("");
        	saveItems();
    	}
    }
    
    // listener to listen to the long click item on the ListView 
    private void setupListViewListener() {
    	lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
				items.remove(pos);
				itemsAdapter.notifyDataSetChanged();
				saveItems();
				return true;
			}
		});
    }
    
    // listener to listen to the item click on the ListView
    private void setupItemEditListener() {
    	lvItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {	
				launchEditItemView(pos);
			}    		
    	});
    }

    // bring up the EditItemActivity form using Intent
    private void launchEditItemView(int pos) {
    	Intent i = new Intent(this, EditItemActivity.class);
    	
    	// passing data to the EditItemActivity form
    	i.putExtra("edit_item_value", items.get(pos));
    	i.putExtra("edit_item_pos", pos);
    	
    	// allows the TodoActivity form to retrieve the result based on a code that is returned
    	startActivityForResult(i, REQUEST_CODE);
    }
    
    // read items from todo.txt file
    private void readItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	
    	try {
    		items = new ArrayList<String>(FileUtils.readLines(todoFile));
    	} catch(IOException e) {
    		items = new ArrayList<String>();
    		e.printStackTrace();
    	}
    }
    
    // save data to todo.txt file
    private void saveItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	
    	try {
    		FileUtils.writeLines(todoFile, items);
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    // This function will retrieve the edit text data from the EditItemActivity form
    // and save the modified value back to the todo.txt file.  This function will get called 
    // when EditItemActivity form is finished.   
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String name = data.getStringExtra("edit_item_value");
            int pos = data.getIntExtra("edit_item_pos", -1);
            
            if(pos != -1) {
            	items.set(pos, name);
                itemsAdapter.notifyDataSetChanged();
            	saveItems();
            }
        }
    }
}
