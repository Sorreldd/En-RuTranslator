package ru.translatordev.enrutranslator;

import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.content.Intent;

public class MainActivity extends Activity {
	EditText inputWord;
    public void OnClTranslate(View v) {
    	String s = inputWord.getText().toString();
    	int len = s.length();
    	boolean flag = false;
    	if(len == 0) flag = true;
    	for(int i = 0; i < len; i++) {
    		char c = s.charAt(i);
    		if((c > 'z' || c < 'a') && (c > 'Z' || c < 'A') && c != ' ') {
    			flag = true;
    		}
    	}
    	if(flag == true) {
    		Toast toast = Toast.makeText(getApplicationContext(), 
    				"Некорректный ввод", Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER, 0, 0);
    		toast.show();
    	} else {
    		Intent intent = new Intent(MainActivity.this, WindowTranslator.class);
    		intent.putExtra("input", s);
    		startActivity(intent);
    	}
    }
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputWord = (EditText) findViewById(R.id.InField);
    }
}