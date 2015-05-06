package ind.demo.icondydemo;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageAdapter extends ArrayAdapter {
    private Context mContext;
    List<Drawable> drawableList;
    int selectedPosition = 0;
    
    private int selectedPos = -1;

    /*public ImageAdapter(Context c) {
        mContext = c;
    }*/
    
	public ImageAdapter(Context context, int textViewResourceId, List<Drawable> objects) {
		super(context, textViewResourceId, objects);
		 mContext = context;
		 drawableList = objects;
	}

    /*public int getCount() {
        return pics.length;
    }*/

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    
    public void setSelectedPosition(int pos){
		selectedPos = pos;
		// inform the view of this change
		notifyDataSetChanged();
	}
	
	public int getSelectedPosition(){
		return selectedPos;
	}
    

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
    	View v = convertView;
    	 
        if (v == null) {

        	LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.adapter_item, null);

        }

        final Drawable p = drawableList.get(position);

        if (p != null) {

            ImageView image = (ImageView) v.findViewById(R.id.image);
            LinearLayout ll = (LinearLayout) v.findViewById(R.id.linearlayout);

            if (image != null) {
            	image.setImageDrawable(p);
            }
            
            if(selectedPos == position){
            	ll.setBackgroundColor(Color.RED);
            }else{
            	ll.setBackgroundColor(Color.WHITE);
            }
            
        }

        return v;
    }

}