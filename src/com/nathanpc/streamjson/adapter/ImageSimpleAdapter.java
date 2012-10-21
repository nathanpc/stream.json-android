package com.nathanpc.streamjson.adapter;

import java.util.List;
import java.util.Map;

import com.fedorvlasov.lazylist.ImageLoader;

import android.content.Context;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

public class ImageSimpleAdapter extends SimpleAdapter {
	private ImageLoader imageLoader;

	public ImageSimpleAdapter(Context context, ImageLoader imageLoader, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		
		this.imageLoader = imageLoader;
	}

	@Override
	public void setViewImage(final ImageView v, String value) {
		super.setViewImage(v, value);
		
        try {
        	imageLoader.DisplayImage(value, v);
            //URL url = new URL(value);
            //Drawable img = Drawable.createFromStream(url.openStream(), "src");
            //v.setImageDrawable(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
}
