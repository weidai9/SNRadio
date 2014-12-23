package org.cu.socialnetworkradio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.facebook.widget.ProfilePictureView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ListActivity extends Activity {
	private ListView nflistview;
	private List<Map<String, ?>> lstItems;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		this.nflistview = (ListView)findViewById(R.id.fsListView);
		this.lstItems = getData();
		SimpleAdapter adapter = initialAdapter();
		this.nflistview.setAdapter(adapter);
		
	}
	private SimpleAdapter initialAdapter(){
		SimpleAdapter adapter = new SimpleAdapter(this, this.lstItems,
				R.layout.view_item, new String[] { "photo", "nickName",
						"publish", "content" }, new int[] {
						R.id.profilePicture, R.id.nickName, R.id.publish, 
						R.id.nf_content });
		adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
			
			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if (view instanceof ProfilePictureView && data instanceof Bitmap) {
					ImageView imageView = (ImageView) view;
					imageView.setImageBitmap((Bitmap) data);
					return true;
				}
				return false;
			}
		});
		return adapter;
	}
	private List<Map<String, ?>> getData() {
		List<Map<String, ?>> retItems = new ArrayList<Map<String, ?>>();
		return retItems;
	}
}
