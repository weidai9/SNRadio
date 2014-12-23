package org.cu.socialnetworkradio.adapter;

import java.util.List;

import org.cu.socialnetworkradio.R;
import org.cu.socialnetworkradio.getdata.NewsFeedInfo;

import com.facebook.widget.ProfilePictureView;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewFeedAdapter extends BaseAdapter {
	private List<NewsFeedInfo> nflist;
	private Context context;
	
	public NewFeedAdapter(Context context, List<NewsFeedInfo> nfi){
		this.nflist = nfi;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return (nflist==null)?0:nflist.size();
	}

	@Override
	public Object getItem(int position) {
		return nflist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	public class ViewHolder{  
        TextView nickName;  
        ProfilePictureView profilePictureView;  
        TextView date;  
        TextView message;  
        TextView story;
        TextView description;
        LinearLayout itemback;
    }
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final NewsFeedInfo nfinfo = (NewsFeedInfo)getItem(position);
		ViewHolder viewHolder = null;
		if(convertView==null){
			Log.d("NewsFeedAdapter", "Created convertView,position="+position);
			convertView = LayoutInflater.from(context).inflate(
					R.layout.view_item, null);
			viewHolder = new ViewHolder();
			viewHolder.nickName = (TextView)convertView.findViewById(R.id.nickName);
			viewHolder.date = (TextView)convertView.findViewById(R.id.publish);
			viewHolder.message = (TextView)convertView.findViewById(R.id.nf_content);
			viewHolder.profilePictureView = (ProfilePictureView)convertView.findViewById(R.id.profilePicture);
			viewHolder.story = (TextView)convertView.findViewById(R.id.nf_story);
			viewHolder.description = (TextView)convertView.findViewById(R.id.nf_description);
			viewHolder.itemback = (LinearLayout)convertView.findViewById(R.id.item_back);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
			Log.d("NewsFeedAdapter", "old convertView,position="+position);
		}
		
		viewHolder.nickName.setText(nfinfo.getUserName());
		viewHolder.date.setText(nfinfo.getDate());
		viewHolder.message.setText(nfinfo.getMessage());
		viewHolder.story.setText(nfinfo.getStory());
		viewHolder.description.setText(nfinfo.getDescription());
		viewHolder.profilePictureView.setProfileId(nfinfo.getUserid());
		if(nfinfo.isNowread())
			viewHolder.itemback.setBackgroundColor(Color.YELLOW);
		else
			viewHolder.itemback.setBackgroundColor(Color.WHITE);
		return convertView;
	}

}
