package gr.jvlach.bookmarkswidget;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsArrayAdapter2x2 extends ArrayAdapter<WeatherDataPoint2x2> {
	  private final Context context;
	  private final ArrayList<WeatherDataPoint2x2> values;

	  public ContactsArrayAdapter2x2(Context context, ArrayList<WeatherDataPoint2x2> sdata) {
	    super(context, R.layout.listitem, sdata);
	    this.context = context;
	    this.values = sdata;
	  }
	  
	//for photos:
	    public Bitmap loadContactPhoto(ContentResolver cr, long  id) {
	        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
	        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
	        if (input == null) {
	            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);//null;
	        }
	        return BitmapFactory.decodeStream(input);
	    }

	  public ArrayList<WeatherDataPoint2x2> getItems(){
		  return values;
	  }
	  
	 
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		 // Log.d("getcount", "ContactsArrayAdapter");
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.listitem, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.email);
	    TextView textView2 = (TextView) rowView.findViewById(R.id.username);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.avatar);
	    //ImageView imageView2 = (ImageView) rowView.findViewById(R.id.listbutton);
	    //ImageView imageVie3 = (ImageView) rowView.findViewById(R.id.messagebutton);
	    WeatherDataPoint2x2 row=(WeatherDataPoint2x2) values.get(position);
	    
	    textView2.setText(row.degrees);
	    final String phonenumber=row.city;
	    textView.setText(row.city);
	    // Change the icon for Windows and iPhone
	    byte[] s = row.photo;
	    //Bitmap photo=loadContactPhoto(context.getContentResolver(), s);
	    Bitmap photo;
	    if (s!=null && s.length>0){
	    	photo=BitmapFactory.decodeByteArray(s, 0, s.length, null);
	    	
        }else{
        	photo=BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        }
	    imageView.setImageBitmap(photo);
	    
	    //v.1.2:
	    final String city=row.city;
	    
	    imageView.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	final String formatStr = context.getResources().getString(R.string.toast_format_string);
	            Toast.makeText(context, String.format(formatStr, city), Toast.LENGTH_SHORT).show();
	            
	            
	        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(city));
	        	browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	context.startActivity(browserIntent);
	        }
	    });
	    
	    //TextView tel=(TextView) rowView.findViewById(R.id.listbutton);
	    
	    textView.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	final String formatStr = context.getResources().getString(R.string.toast_format_string);
	            Toast.makeText(context, String.format(formatStr, city), Toast.LENGTH_SHORT).show();
	            
	            
	        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(city));
	        	browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	context.startActivity(browserIntent);
	        }
	    });
	    
	    //ImageView sms=(ImageView) rowView.findViewById(R.id.messagebutton);
	    
	    textView2.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	final String formatStr = context.getResources().getString(R.string.toast_format_string);
	            Toast.makeText(context, String.format(formatStr, city), Toast.LENGTH_SHORT).show();
	            
	            
	        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(city));
	        	browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	context.startActivity(browserIntent);
	        }
	    });

	    return rowView;
	  }
	  
	  

	} 
