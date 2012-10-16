package ru.translatordev.enrutranslator;

import java.io.*;
import java.net.*;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WindowTranslator extends Activity {
    String inStr = "";
    String [] ans = new String[10];
    TextView outputWord, txRes;
    ProgressDialog pr;
    ImageView imgView;
    Drawable pic;
    int k = 0, ind = 0;
    boolean flag = true;
    class TranslateWord extends AsyncTask<Void, Void, Void> {
		String translatedText = "Bad gateway", webContentFull = "";
		StringBuilder websiteContent = new StringBuilder();
		
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
			BingSearch();
			ParseText();
			if(k != 0) {
				try {
					pic = Drawable.createFromStream((InputStream) new URL(ans[ind]).getContent(), "src");
				} catch (Exception e) { e.printStackTrace(); }
			}
			return null;
	    }
		public void BingSearch() {
			URLConnection connection = null;
			BufferedReader bufferReader = null;
			String u1 = "http://www.bing.com/images/search?q=";
			String u2 = "&filt=all&view=large&FORM=VBCIRL";
			String tt = "";
			for(int i = 0; i < inStr.length(); i++) {
				if(inStr.charAt(i) == ' ') {
					tt += '+';
				} else {
					tt += inStr.charAt(i);
				}
			}
			inStr = tt;
			try {
				URL urlWeb = new URL(u1 + inStr + u2);
				connection = urlWeb.openConnection();
				connection.setRequestProperty ("User-Agent", "Mozilla/5.0");
				connection.connect();
				bufferReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				String inputLine = "";
				while ((inputLine = bufferReader.readLine()) != null) {
					websiteContent.append(inputLine);
				}
				webContentFull = websiteContent.toString();
			} catch (Exception e) {
				Log.e("GetSite", Log.getStackTraceString(e));
			} finally {
				if (bufferReader != null) {
					try {
						bufferReader.close();
					} catch (IOException e) {
						Log.e("GetSite", Log.getStackTraceString(e));
					}
				}
				if (connection != null) {
					((HttpURLConnection) connection).disconnect();
				}
			}
		}
		public void ParseText() {
			int n = webContentFull.length();
			int indE = 0;
			boolean flagpng, flagjpg, flaghttp;
			String maskpng = ".png&quot", maskjpg = ".jpg&quot", maskhttp = "http://";
			for(int i = 0; i < n - 10 && k < 10; i++) {
				flagpng = true;
				flagjpg = true;
				
				for(int j = 0; j < 9; j++) {
					if(flagjpg == true && webContentFull.charAt(i + j) != maskjpg.charAt(j)) {
						flagjpg = false;
					}
					if(flagpng == true && webContentFull.charAt(i + j) != maskpng.charAt(j)) {
						flagpng = false;
					}
				}
				if(flagpng == true || flagjpg == true) {
					indE = i + 4;
				} else {
					continue;
				}
				for(int t = i; t >= 0; t--) {
					flaghttp = true;
					if(webContentFull.charAt(t) == '&' || 
						(webContentFull.charAt(t) == ':' && webContentFull.charAt(t + 1) != '/'))
							break;
					for(int j = 0; j < 7; j++) {
						if(webContentFull.charAt(t + j) != maskhttp.charAt(j)) {
							flaghttp = false; 
							break;
						}
					}
					if(flaghttp == true) {
						ans[k] = "";
						for(int j = t; j < indE; j++) {
							ans[k] += webContentFull.charAt(j);
						}
						k++;
						break;
					}
				}
			}
		}
		
		@Override
	    protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pr.dismiss();
			outputWord.setText(translatedText);
			if(k != 0) {
				txRes.setText(Integer.toString(ind + 1) + " of " + Integer.toString(k));
				imgView.setImageDrawable(pic);
			}
	    }
		
    }
	
    class LoadImage extends AsyncTask<Void, Void, Void> {
    	@Override
	    protected void onPreExecute() {
	    	super.onPreExecute();
	    	pr = ProgressDialog.show(WindowTranslator.this, "In progress", "Loading");
	    }
    	@Override
    	protected Void doInBackground(Void... params) {
			if(k != 0) {
				if(flag == true) {
					ind = (ind + 1) % k;
				} else {
					ind--;
					if(ind < 0) ind = k - 1;
				}
				try {
					pic = Drawable.createFromStream((InputStream) new URL(ans[ind]).getContent(), "src");
				} catch (Exception e) { e.printStackTrace(); }
			}
			return null;
	    }
    	@Override
	    protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pr.dismiss();
			if(k != 0) {
				txRes.setText(Integer.toString(ind + 1) + " of " + Integer.toString(k));
				imgView.setImageDrawable(pic);
			}
	    }
    }
    
    public void OnClBack(View v) {
		super.onBackPressed();
	}
	
    public void OnClNext(View v) {
    	flag = true;
    	LoadImage mt = new LoadImage();
    	mt.execute();
    }
    public void OnClPrevious(View v) {
    	flag = false;
    	LoadImage mt = new LoadImage();
    	mt.execute();
    }
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_window);
        inStr = getIntent().getExtras().getString("input");
        outputWord = (TextView) findViewById(R.id.TranslatedWord);
        txRes = (TextView) findViewById(R.id.textRes);
        imgView = (ImageView) findViewById(R.id.imageView1);
        TranslateWord urlTh = new TranslateWord();
		urlTh.execute();
    }
}
