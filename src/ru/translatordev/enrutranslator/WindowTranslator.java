package ru.translatordev.enrutranslator;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class WindowTranslator extends Activity {
    String inStr = "";
    TextView outputWord;
    class TranslateWord extends AsyncTask<Void, Void, Void> {
		String translatedText = "Слово не существует";

		@Override
	    protected void onPreExecute() {
	    	super.onPreExecute();
	    	outputWord.setText("Begin");
	    }
		@Override
	    protected Void doInBackground(Void... params) {
			// code translator + imageview
			return null;
	    }
		@Override
	    protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			outputWord.setText(translatedText);
	    }
    }
		
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_window);
        inStr = getIntent().getExtras().getString("input");
        outputWord = (TextView) findViewById(R.id.tvTranslated);
        TranslateWord urlTh = new TranslateWord();
		urlTh.execute();
    }
}
