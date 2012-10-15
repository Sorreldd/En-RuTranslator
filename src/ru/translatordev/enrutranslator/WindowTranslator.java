package ru.translatordev.enrutranslator;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WindowTranslator extends Activity {
    String inStr = "";
    TextView outputWord;
    ProgressDialog pr;
    class TranslateWord extends AsyncTask<Void, Void, Void> {
		String translatedText = "Слово не существует";
		
		@Override
	    protected void onPreExecute() {
	    	super.onPreExecute();
	    	outputWord.setText("Loading...");
	    	pr = ProgressDialog.show(WindowTranslator.this, "In progress", "Loading");
	    }
		
		@Override
	    protected Void doInBackground(Void... params) {
			Translate.setClientId("56e53588-ecbe-44e6-9205-d1857d8d0b96");
	        Translate.setClientSecret("C40nsYBg5BhoeyMCZHEenqGN3UzlKTiSEgBregaXp6g=");
			try {
				translatedText = Translate.execute(inStr, Language.ENGLISH, Language.RUSSIAN);
			} catch (Exception e) { e.printStackTrace(); }
			return null;
	    }
		
		@Override
	    protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pr.dismiss();
			outputWord.setText(translatedText);
			
	    }
		
    }
	public void OnClBack(View v) {
		super.onBackPressed();
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_window);
        inStr = getIntent().getExtras().getString("input");
        outputWord = (TextView) findViewById(R.id.TranslatedWord);
        
        TranslateWord urlTh = new TranslateWord();
		urlTh.execute();
    }
}
