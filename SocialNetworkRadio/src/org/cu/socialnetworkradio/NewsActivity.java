package org.cu.socialnetworkradio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.cu.socialnetworkradio.getdata.NewsDataHelper;
import org.cu.socialnetworkradio.getdata.NbaNewsInfo;
import org.cu.socialnetworkradio.getdata.TechNewsInfo;
import org.cu.socialnetworkradio.getdata.UsNewsInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NewsActivity extends Activity {
	final int NBATYPE=0;
	final int TECHTYPE=1;
	final int USTYPE=2;
	
	NewsDataHelper dbhelper;
	TextView titletext;
	TextView datetext;
	TextView infotext;
	TextView texttext;
	TextView urltext;
	TextView counttext;
	ArrayList<NbaNewsInfo> nbanewslist;
	ArrayList<TechNewsInfo> technewslist;
	ArrayList<UsNewsInfo> usnewslist;
	int nbacount = 0;
	int techcount = 0;
	int uscount = 0;
	int currenttype = NBATYPE;
	private TextToSpeech tts;
	private HashMap<String, String> ttsparams;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		final Handler handler = new Handler();
		dbhelper = new NewsDataHelper(getBaseContext());
		
		Thread th = new Thread(){
			public void run(){
				nbanewslist = dbhelper.getNbaNewsInfoList();
				technewslist = dbhelper.getTechNewsInfoList();
				usnewslist = dbhelper.getUsNewsInfoList();
				handler.post(new Runnable() {
					@Override
					public void run() {
						counttext = (TextView)findViewById(R.id.countText);
						titletext = (TextView)findViewById(R.id.newsTitle);
						datetext = (TextView)findViewById(R.id.newsDate);
						infotext = (TextView)findViewById(R.id.newsInfo);
						texttext = (TextView)findViewById(R.id.newsText);
						urltext = (TextView)findViewById(R.id.newsUrl);
						currenttype = NBATYPE;
						setNewsPage(currenttype, nbacount);
						
					}
					
				});
			}
		};
		th.start();
		
		tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.US);
                    /*if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                    else{
                        //ConvertTextToSpeech();
                    }*/
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });
		
		setContentView(R.layout.activity_news);
	}
	
	@Override
    protected void onPause() {
        // TODO Auto-generated method stub
        if(tts != null){
            tts.stop();
        }
        super.onPause();
    }
	
	
	@Override
	protected void onResume() {
		if(nbanewslist == null || technewslist==null||usnewslist==null){
			super.onResume();
			return;
		}
			
		switch (currenttype) {
		case NBATYPE:
			setNewsPage(currenttype, nbacount);
			break;
		case TECHTYPE:
			setNewsPage(currenttype, techcount);
			break;
		case USTYPE:
			setNewsPage(currenttype, uscount);
			break;
		default:
			break;
		}
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.news_menu, menu);
		
		return true;
	}
	@Override  
    public boolean onOptionsItemSelected(MenuItem item) {  
		tts.stop();
        switch (item.getItemId()) {  
            case R.id.nbaitem:  
            	currenttype = NBATYPE;
            	setNewsPage(currenttype, nbacount);
            return true;     
           case R.id.techitem:  
        	   	currenttype = TECHTYPE;
        	   	setNewsPage(currenttype, techcount);
        	   return true;     
            case R.id.usitem:
            	currenttype = USTYPE;
            	setNewsPage(currenttype, uscount);
            	return true;     
              default:  
                return super.onOptionsItemSelected(item);  
        }  
    }
	
	public void onGoToNewsFeedClick(View view){
		Intent intent = new Intent();
		intent.setClass(NewsActivity.this, MainActivity.class);
		
		startActivity(intent);
	}
	
	public void onNewsStartReadClick(View view){
		String readtext = titletext.getText().toString()+", "
				+ infotext.getText().toString()+", "
				+ texttext.getText().toString();
		
		if(readtext.length()>4000){
			//readtext = readtext.substring(0, 3999);
			int i = 0;
			while(i+3999<readtext.length()) {
				tts.speak(readtext.substring(i, i+3999),TextToSpeech.QUEUE_ADD, null);
				i = i+4000;
			}
			tts.speak(readtext.substring(i,readtext.length()-1), TextToSpeech.QUEUE_ADD, null);
		}
		else
			tts.speak(readtext,TextToSpeech.QUEUE_FLUSH,null);
		}
	
	public void onNewsStopReadClick(View view){
		tts.stop();
	}
	
	public void onPreviousNewsClick(View view){
		tts.stop();
		switch (currenttype) {
		case NBATYPE:
			if(nbacount == 0)
				nbacount = nbanewslist.size()-1;
			else
				nbacount --;
			setNewsPage(currenttype, nbacount);
			break;
		case TECHTYPE:
			if(techcount == 0)
				techcount = technewslist.size()-1;
			else
				techcount --;
			setNewsPage(currenttype, techcount);
			break;
		case USTYPE:
			if(uscount == 0)
				uscount = usnewslist.size()-1;
			else
				uscount --;
			setNewsPage(currenttype, uscount);
			break;
		default:
			break;
		}
	}
	
	public void onNextNewsClick(View view){
		tts.stop();
		switch (currenttype) {
		case NBATYPE:
			if(nbacount == nbanewslist.size()-1)
				nbacount = 0;
			else
				nbacount ++;
			setNewsPage(currenttype, nbacount);
			break;
		case TECHTYPE:
			if(techcount == technewslist.size()-1)
				techcount = 0;
			else
				techcount ++;
			setNewsPage(currenttype, techcount);
			break;
		case USTYPE:
			if(uscount == usnewslist.size()-1)
				uscount = 0;
			else
				uscount ++;
			setNewsPage(currenttype, uscount);
			break;
		default:
			break;
		}
	}
	
	public void setNewsPage(int type, int count) {
		if (type == NBATYPE && count<nbanewslist.size()) {
			titletext.setText(nbanewslist.get(count).getTitle());
			datetext.setText(nbanewslist.get(count).getDate());
			if(!nbanewslist.get(count).getInfo().equals("null"))
				infotext.setText(nbanewslist.get(count).getInfo());
			else
				infotext.setText(" ");
			texttext.setText(nbanewslist.get(count).getText());
			urltext.setText(nbanewslist.get(count).getUrl());
			counttext.setText("NBA News: "+(nbacount+1)+"/"+nbanewslist.size());
			
		} else if(type == TECHTYPE&& count<technewslist.size()) {
			titletext.setText(technewslist.get(count).getTitle());
			datetext.setText(technewslist.get(count).getDate());
			if(!technewslist.get(count).getInfo().equals("null"))
				infotext.setText(technewslist.get(count).getInfo());
			else
				infotext.setText(" ");
			texttext.setText(technewslist.get(count).getText());
			urltext.setText(technewslist.get(count).getUrl());
			counttext.setText("Tech News: "+(techcount+1)+"/"+technewslist.size());
		} else if(type == USTYPE&& count<usnewslist.size()){
			titletext.setText(usnewslist.get(count).getTitle());
			datetext.setText(usnewslist.get(count).getDate());
			if(!usnewslist.get(count).getInfo().equals("null"))
				infotext.setText(usnewslist.get(count).getInfo());
			else
				infotext.setText(" ");
			texttext.setText(usnewslist.get(count).getText());
			urltext.setText(usnewslist.get(count).getUrl());
			counttext.setText("US News: "+(uscount+1)+"/"+usnewslist.size());
		}
	}
}
