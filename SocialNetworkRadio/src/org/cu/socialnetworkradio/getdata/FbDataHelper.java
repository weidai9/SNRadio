package org.cu.socialnetworkradio.getdata;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

public class FbDataHelper {
	List<NewsFeedInfo> retlist;
	public List<NewsFeedInfo> getNewsFeed(Session session){
		retlist = new ArrayList<NewsFeedInfo>();
		Bundle params = new Bundle();
		params.putString( "fields", "from,message,created_time" );
		final Request request = new Request( Session.getActiveSession(), "/v2.2/me/home", params, HttpMethod.GET, new Request.Callback() {
			@Override
			public void onCompleted(Response response) {
				if(response.getGraphObject()!=null){
					JSONObject jsonobj = response.getGraphObject().getInnerJSONObject();
					try {
						JSONArray jarray = jsonobj.getJSONArray("data");
						for (int i = 0;i<jarray.length();i++){
							JSONObject dataobj = (JSONObject) jarray.get(i);
							if(dataobj.has("message")) {
							NewsFeedInfo nfinfo = new NewsFeedInfo();
							
							nfinfo.setDate(dataobj.getString("created_time"));
							nfinfo.setMessage(dataobj.getString("message"));
							System.out.println(dataobj.getString("message"));
							JSONObject userobj = dataobj.getJSONObject("from");
							nfinfo.setUserid(userobj.getString("id"));
							nfinfo.setUserName(userobj.getString("name"));
							retlist.add(nfinfo);
							}
						}
					} catch(JSONException e){e.printStackTrace();}
				}
			}
		});
		request.executeAsync();
		return retlist;
	}
}
