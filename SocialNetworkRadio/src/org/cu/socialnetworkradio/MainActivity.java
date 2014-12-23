package org.cu.socialnetworkradio;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.cu.socialnetworkradio.adapter.NewFeedAdapter;
import org.cu.socialnetworkradio.getdata.NewsFeedInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("NewApi") public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private NewFeedAdapter nfadapter;
	private ListView nflistview;
	List<NewsFeedInfo> nfdatalist;
	LoginButton loginButton;
	Button startReadButton;
	Button stopReadButton;
	TextView testtext, greetingtext;
	GraphUser user;
	private TextToSpeech tts;
	private HashMap<String, String> ttsparams;
	private int readcount = 0;
	private boolean stopclicked = false;
	private boolean ttspaused = false;
	private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	 	nfdatalist = new ArrayList<NewsFeedInfo>();
		initialUI();
		uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        ttsparams = new HashMap<String, String>();
        ttsparams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"stringId");
        tts=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
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
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener(){

 					@Override
 					public void onStart(String utteranceId) {
 						// TODO Auto-generated method stub
 						System.out.println("ReadId: "+readcount);
 						nfdatalist.get(readcount).setNowread(true);
 						//nfadapter.notifyDataSetChanged();
 						//nflistview.smoothScrollToPosition(readcount);
 						new ChangeThread().start();

 					}

 					@Override
 					public void onDone(String utteranceId) {
 						if(ttspaused)
 							return;
 						if(stopclicked)
 							return;
 						nfdatalist.get(readcount).setNowread(false);
 						//nfadapter.notifyDataSetChanged();
 						new ChangeThread().start();
 						readcount++;
 						if (readcount >= nfdatalist.size()) {
 							readcount = 0;
 						}
 						else {
 						String readtext = nfdatalist.get(readcount).getUserName()+", "
 								+nfdatalist.get(readcount).getStory()+", "
 								+nfdatalist.get(readcount).getMessage()+", "
 								+nfdatalist.get(readcount).getDescription();
 						tts.speak(readtext,TextToSpeech.QUEUE_FLUSH, ttsparams);
 						}
 					}

 					@Override
 					@Deprecated
 					public void onError(String utteranceId) {

 					}
                	
                });
            }
        });
	}
	
	@Override
    public void onResume() {
        super.onResume();
		Session session = Session.getActiveSession();
		if (session != null &&
				(session.isOpened() || session.isClosed()) ) {
			onSessionStateChange(session, session.getState(), null);
		}
        uiHelper.onResume();
        ttspaused = false;
    }
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }
	
	@Override
    public void onPause() {
		ttspaused = true;
		tts.stop();
        super.onPause();
        uiHelper.onPause();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
    
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
    	if (state.isOpened()) {
    		Log.i(TAG, "Logged in...");
            if(user != null)
            	greetingtext.setText("hello,"+ user.getFirstName());
    		nfdatalist.clear();
    		getData();
    		nfadapter.notifyDataSetChanged();
    		startReadButton.setEnabled(true);
    		stopReadButton.setEnabled(true);
        } else if (state.isClosed()) {
        	Log.i(TAG, "Logged out...");
        	nfdatalist.clear();
        	nfadapter.notifyDataSetChanged();
        	greetingtext.setText("Please login with your facebook account.");
        	startReadButton.setEnabled(false);
    		stopReadButton.setEnabled(false);
    		stopclicked = true;
    		tts.stop();
        }
    }
    
   public void onReadClick(View view) {
	   stopclicked = false;
	   String readtext = nfdatalist.get(readcount).getUserName()
				+nfdatalist.get(readcount).getStory()+","
				+nfdatalist.get(readcount).getMessage()+","
				+nfdatalist.get(readcount).getDescription();
	   tts.speak(readtext,TextToSpeech.QUEUE_FLUSH, ttsparams);
   }
   
   public void onGoToNewsClick(View view) {
	   Intent intent = new Intent();
	   intent.setClass(MainActivity.this, NewsActivity.class);
	   startActivity(intent);
   }
   
   public void onStopClick(View view) {
	   stopclicked = true;
	   tts.stop();
	  // tts.shutdown();
   }
	
	private void initialUI(){
		testtext = (TextView) findViewById(R.id.testtext);
		greetingtext = (TextView) findViewById(R.id.greetingtext);
		nflistview = (ListView)findViewById(R.id.fsListView);
		nfadapter = new NewFeedAdapter(this, nfdatalist);
		nflistview.setAdapter(nfadapter);
		startReadButton = (Button)findViewById(R.id.readButton);
		stopReadButton = (Button)findViewById(R.id.stopButton);
		startReadButton.setEnabled(false);
		stopReadButton.setEnabled(false);
		loginButton = (LoginButton) findViewById(R.id.authButton);
		List<String> perlist = new ArrayList<String>();
		perlist.add("user_status");
		perlist.add("read_stream");
		loginButton.setReadPermissions(perlist);
		loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                MainActivity.this.user = user;
                getData();
        		
                updateUI();
                
            }
        });
	}
	
	 private void getData() {
			Bundle params = new Bundle();
			params.putString( "fields", "from,message,created_time,story,description" );
			final Request request = new Request( Session.getActiveSession(), "/me/home", params, HttpMethod.GET, new Request.Callback() {
				@Override
				public void onCompleted(Response response) {
					if(response.getGraphObject()!=null){
						nfdatalist.clear();
						nfadapter.notifyDataSetChanged();
						JSONObject jsonobj = response.getGraphObject().getInnerJSONObject();
						try {
							JSONArray jarray = jsonobj.getJSONArray("data");
							for (int i = 0;i<jarray.length();i++){
								JSONObject dataobj = (JSONObject) jarray.get(i);
								NewsFeedInfo nfinfo = new NewsFeedInfo();
								nfinfo.setDate(dataobj.getString("created_time"));
								if(dataobj.has("message"))
									nfinfo.setMessage(dataobj.getString("message"));
								if(dataobj.has("story"))
									nfinfo.setStory(dataobj.getString("story"));
								if(dataobj.has("description"))
									nfinfo.setDescription(dataobj.getString("description"));
								JSONObject userobj = dataobj.getJSONObject("from");
								nfinfo.setUserid(userobj.getString("id"));
								nfinfo.setUserName(userobj.getString("name"));
								nfinfo.setNowread(false);
								nfdatalist.add(nfinfo);
							}
							
							nfadapter.notifyDataSetChanged();
			        		
						} catch(JSONException e){e.printStackTrace();}
					}
				}
			});
			request.executeAsync();
	 }
	
	private void updateUI(){
		Session session = Session.getActiveSession();
        if(user != null)
        	greetingtext.setText("hello,"+ user.getFirstName());
	}
	private class ChangeThread extends Thread {
		@Override
		public void run() { 
		MainActivity.this.runOnUiThread(new Runnable() {
		        @Override
		        public void run() {
		        	nfadapter.notifyDataSetChanged();
					nflistview.smoothScrollToPosition(readcount);
		        }
		    });
		}
	}
	
}
