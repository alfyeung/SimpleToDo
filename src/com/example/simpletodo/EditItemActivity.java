package com.example.simpletodo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {
	private EditText etItem;
	private int itemPos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		etItem = (EditText) findViewById(R.id.etEditItemValue);
		etItem.setText(getIntent().getStringExtra("edit_item_value"));
		etItem.setSelection(etItem.getText().length());	
		
		itemPos = getIntent().getIntExtra("edit_item_pos", -1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}
	
	// Save the EditText value to the ListView in TodoActivity form
	public void saveEditItem(View v) {
		Intent data = new Intent();
		data.putExtra("edit_item_value", etItem.getText().toString());
		data.putExtra("edit_item_pos", itemPos);
		setResult(RESULT_OK, data);		
		finish();
	}
}
