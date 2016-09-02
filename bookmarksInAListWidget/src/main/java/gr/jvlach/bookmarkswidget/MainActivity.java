package gr.jvlach.bookmarkswidget;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	
	Hashtable<String, String> sDataNoMultipleURLs=new Hashtable<String, String>();//v.2.3
	
	
	private static final ArrayList<WeatherDataPoint> sData = new ArrayList<WeatherDataPoint>();
	private static final ArrayList<WeatherDataPoint2x2> sData2x2 = new ArrayList<WeatherDataPoint2x2>();
	private ArrayList<WeatherDataPoint> array_sort = new ArrayList<WeatherDataPoint>();
	
	public static String what="stock";
	
	int textlength=0;
	private EditText et;
	private ListView lv;
	
	public static int appwidgetid=0;//v. 2.2
	public static String typeofwidget="4x2";//v.2.2
	
	//private ContactsArrayAdapter adapter;

	public void getBrowser(String selection){
		sData.clear();
		sData2x2.clear();
		final String TOUCH_ICON ="touch_icon";
		//String[] proj = new String[] { Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL, Browser.BookmarkColumns.FAVICON};//,TOUCH_ICON
		String sel = Browser.BookmarkColumns.BOOKMARK + " = 1"; // 0 = history, 1 = bookmark
		
		Uri uriCustom=Browser.BOOKMARKS_URI;
		
		boolean installed  = false;
		if (selection.equals("stock")){
			uriCustom=Browser.BOOKMARKS_URI;
		}else if (selection.equals("chrome")){
			uriCustom=Uri.parse("content://com.android.chrome.browser/bookmarks");
		}
		//for chrome:
		//Uri uriCustom = Uri.parse("content://com.android.chrome.browser/bookmarks");
		
		
		Cursor mCur = getContentResolver().query(uriCustom, null, sel, null, null);//Browser.BOOKMARKS_URI
		//mCur.moveToFirst();
		String title = "";
		String url = "";
		Bitmap favicon;
		
		long date=0;//v.2.3
		
		//ImageView rv=(ImageView)findViewById(R.id.imageView1);
		
		
		//add for samsung devices //v.1.9:
		installed  =   appInstalledOrNot("com.sec.android.app.sbrowser");  
		if(installed && (selection.equals("stock") || selection.equals("all")))
	     {
	    	/* uriCustom=Uri.parse("content://com.sec.android.app.sbrowser/bookmarks");
	    	 sel=null;
	    	 orderby="MODIFIED" + " DESC";*/
	     
	        //String sel = Browser.BookmarkColumns.BOOKMARK + " = 1"; // 0 = history, 1 = bookmark
	        String orderby2="";//needs to be put in chrome as well
			
			orderby2="MODIFIED" + " DESC";
			
	        final Uri uri = Uri.parse("content://com.sec.android.app.sbrowser/bookmarks");
	        final Cursor c =  getContentResolver().query(uri, null, null, null, orderby2);
	
	      
	        
	        if (c!=null && c.moveToFirst()) {
	        	
	        	
	        	Log.e("bookmarksinalist", "count="+c.getCount());
	        	
	            do {
	            	try{
	            		title = c.getString(c.getColumnIndex("TITLE"));
				        url = c.getString(c.getColumnIndex("URL"));
				        
				        if (url==null || url.length()==0){//if url is null
				        	continue;
				        }
				        
				       
				        
				        try{
				        	date = c.getLong(c.getColumnIndex("MODIFIED"));
				        }catch(Exception e){
				        	//caught
				        }
				        
				        byte[] data ;
				        
				        
				        data = c.getBlob(c.getColumnIndex("FAVICON"));
				        
				        //byte[] data = mCur.getBlob(mCur.getColumnIndex(Browser.BookmarkColumns.FAVICON));
				        if (data!=null && data.length>0){
				        	//do nothing
				        }else {//get favicon
				        	data = c.getBlob(c.getColumnIndex("FAVICON"));
				        }
	            	
		                //Log.e("bookmarksinalist","URL "+ c.getString(c.getColumnIndex("URL")));
		                //Log.e("bookmarksinalist", "TITLE " +c.getString(c.getColumnIndex("TITLE")));
		                //Log.e("bookmarksinalist","sURL " + c.getString(c.getColumnIndex("sURL")));
		                //Log.e("bookmarksinalist","modified " + c.getString(c.getColumnIndex("MODIFIED")));
	                
		                String key=title+url;
		                //v.2.3:
	          		  	if (sDataNoMultipleURLs.containsKey(key)){
	          		  		//don't put the duplicate
	          		  	}else{
	          		  		sDataNoMultipleURLs.put(key, key);
	          		  		sData.add(new WeatherDataPoint(url, title,data,date));
	          		  	}
	          		  	//v.2.3:END
		                //sData.add(new WeatherDataPoint(url, title,data,0));
	                
	            	}catch(Exception e){
	            		Log.e("bookmarksinalist", "exception");
	            	}
	            	
	            	
	            } while(c.moveToNext());
	
	        }
	    }else{
	    	if(mCur!=null){
			if (mCur.moveToFirst() && mCur.getCount() > 0) {
			    boolean cont = true;
			    while (mCur.isAfterLast() == false && cont) {
			        title = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.TITLE));
			        url = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));
			        
			      
			        if (url==null || url.length()==0){//if url is null
			        	continue;
			        }
			        try{
			        	//if (installed){//samsung
			        	//	date = mCur.getLong(mCur.getColumnIndex("MODIFIED"));
			        	//}else{
			        		date = mCur.getLong(mCur.getColumnIndex(Browser.BookmarkColumns.DATE));
			        	//}
			        }catch(Exception e){
			        	//caught
			        }
			        
			        byte[] data ;
			        
			        if (selection.equals("stock")){
			        	//we are ok
			        	//v. 1.4:seems that some times touch_icon does not exist
			        	try{
			        		data= mCur.getBlob(mCur.getColumnIndex(TOUCH_ICON));//touch_icon=8
			        	}catch(Exception e1){
			        		data = mCur.getBlob(mCur.getColumnIndex(Browser.BookmarkColumns.FAVICON));
			        	}
			        }else if (selection.equals("chrome")){
			        	data = mCur.getBlob(mCur.getColumnIndex(Browser.BookmarkColumns.FAVICON));
			        }else{
			        	//v. 1.4:seems that some times touch_icon does not exist
			        	try{
			        		data= mCur.getBlob(mCur.getColumnIndex(TOUCH_ICON));//touch_icon=8
			        	}catch(Exception e1){
			        		data = mCur.getBlob(mCur.getColumnIndex(Browser.BookmarkColumns.FAVICON));
			        	}
			        }
			        //byte[] data = mCur.getBlob(mCur.getColumnIndex(Browser.BookmarkColumns.FAVICON));
			        if (data!=null && data.length>0){
			        	//do nothing
			        }else {//get favicon
			        	data = mCur.getBlob(mCur.getColumnIndex(Browser.BookmarkColumns.FAVICON));
			        }
			        
			        //v.2.3:
			        String key=title+url;
          		  	if (sDataNoMultipleURLs.containsKey(key)){
          		  		//don't put the duplicate
          		  	}else{
          		  		sDataNoMultipleURLs.put(key, key);
          		  		sData.add(new WeatherDataPoint(url, title,data,date));
          		  	}
          		  	//v.2.3:END
			        //sData.add(new WeatherDataPoint(url, title,data,0));//v.2.3
			        
			        // Do something with title and url
			        //Log.d("bookmark",title+" " +url);
			        mCur.moveToNext();
			    }
			}
	    }
	}
		
		if (selection.equals("all")){//have done it for stock, now do it for chrome and merge
			uriCustom=Uri.parse("content://com.android.chrome.browser/bookmarks");
			
			Cursor mCur2 = getContentResolver().query(uriCustom, null, sel, null, null);//Browser.BOOKMARKS_URI
			//mCur2.moveToFirst();
			if(mCur2!=null){
				if (mCur2.moveToFirst() && mCur2.getCount() > 0) {
				    boolean cont = true;
				    while (mCur2.isAfterLast() == false && cont) {
				        title = mCur2.getString(mCur2.getColumnIndex(Browser.BookmarkColumns.TITLE));
				        url = mCur2.getString(mCur2.getColumnIndex(Browser.BookmarkColumns.URL));
				        
				        try{
				        	date = mCur2.getLong(mCur2.getColumnIndex(Browser.BookmarkColumns.DATE));
				        }catch(Exception e){
				        	//caught
				        }
				        
				        byte[] data ;
				        
				        
				        data = mCur2.getBlob(mCur2.getColumnIndex(Browser.BookmarkColumns.FAVICON));
				        
				        //byte[] data = mCur.getBlob(mCur.getColumnIndex(Browser.BookmarkColumns.FAVICON));
				        if (data!=null && data.length>0){
				        	//do nothing
				        }else {//get favicon
				        	data = mCur2.getBlob(mCur2.getColumnIndex(Browser.BookmarkColumns.FAVICON));
				        }
				        
				      //v.2.3:
				        String key=title+url;
	          		  	if (sDataNoMultipleURLs.containsKey(key)){
	          		  		//don't put the duplicate
	          		  	}else{
	          		  		sDataNoMultipleURLs.put(key, key);
	          		  		sData.add(new WeatherDataPoint(url, title,data,date));
	          		  	}
	          		  	//v.2.3:END
				        //sData.add(new WeatherDataPoint(url, title,data,0));//v.2.3
				        
				        // Do something with title and url
				        //Log.d("bookmark",title+" " +url);
				        mCur2.moveToNext();
				    }
				}
			}
			
			
		}
	}
	
	//for photos:
    public Bitmap loadContactPhoto(ContentResolver cr, long  id) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        if (input == null) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);//null;
        }
        return BitmapFactory.decodeStream(input);
    }
    
   
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String selection=what;
		
		
		//to solve the problem with background color //v. 1.8
		LinearLayout linLayout = (LinearLayout) findViewById(R.id.linlayoutmain);
		linLayout.setBackgroundColor(Color.parseColor("#000000"));
		
		getBrowser(selection);
		
		sDataNoMultipleURLs.clear();//v.2.3
		
		
		String type="";
		if (typeofwidget.equals("4x2")){
			type="4x2";
		}
		//If sData is null, read the saved one://v. 2.2
				String fileName="sData"+type+appwidgetid;
				  if (sData==null || sData.isEmpty()){
					  
					  if (typeofwidget.equals("4x2")){
						  try {
								FileInputStream fis = openFileInput(fileName);
								ObjectInputStream is = new ObjectInputStream(fis);
								ArrayList<WeatherDataPoint> simpleClass = (ArrayList<WeatherDataPoint>) is.readObject();
								sData.addAll(simpleClass);
								is.close();
								fis.close();
								Log.e("bookmarksInAList", "main activity Size of saved file: " + sData.size());
							  } catch (Exception e) {
									
									Log.e("bookmarksInAList", "main activity Exception occured while reading: " + e.getMessage());
								} 
					}else{
						  try {
								FileInputStream fis = openFileInput(fileName);
								ObjectInputStream is = new ObjectInputStream(fis);
								ArrayList<WeatherDataPoint2x2> simpleClass = (ArrayList<WeatherDataPoint2x2>) is.readObject();
								sData2x2.addAll(simpleClass);
								is.close();
								fis.close();
								Log.e("bookmarksInAList", "main activity Size of saved file: " + sData2x2.size());
							  } catch (Exception e) {
									
									Log.e("bookmarksInAList", "main activity Exception occured while reading: " + e.getMessage());
							} 
							  
					  }
					  
				  }		 
				  
				  
				  
				  //v.2.3:
				  //sort the data:
				  String sortorderlocal=AppWidgetConfigure.loadTitlePrefSort(this, appwidgetid);
				  
				  if (!sortorderlocal.equals("date")){	//for desc
				  	  Collections.sort(sData,
									new Comparator<WeatherDataPoint>() {
										public int compare(WeatherDataPoint o1, WeatherDataPoint o2) {
											if (o1.degrees == null) {
												return -1;
											}
											if (o2.degrees == null) {
												return -1;
											}
											return o1.degrees.toUpperCase().compareTo(
													o2.degrees.toUpperCase());
													//* -1;  //for desc
										}
									});
				  }else{//for date
					  Collections.sort(sData,
								new Comparator<WeatherDataPoint>() {
									public int compare(WeatherDataPoint o1, WeatherDataPoint o2) {
										if (o1.lookUpKey == null) {
											return -1;
										}
										if (o2.lookUpKey == null) {
											return -1;
										}
										return o2.lookUpKey.compareTo(o1.lookUpKey);//for reverse (descending) change o2 to o1
												
									}
								});
					  
				  }
				  
				  //v.2.3:END
		
		 //sort the data:
	  	 /* Collections.sort(sData,
						new Comparator<WeatherDataPoint>() {
							public int compare(WeatherDataPoint o1, WeatherDataPoint o2) {
								if (o1.degrees == null) {
									return -1;
								}
								if (o2.degrees == null) {
									return -1;
								}
								return o1.degrees.toUpperCase().compareTo(
										o2.degrees.toUpperCase());
										//* -1;  //for desc
							}
						});*/
	  	  
	  	  //v.2.3:
				  
		  if (!sortorderlocal.equals("date")){	//for desc 
		  	Collections.sort(sData2x2,
					new Comparator<WeatherDataPoint2x2>() {
						public int compare(WeatherDataPoint2x2 o1, WeatherDataPoint2x2 o2) {
							if (o1.degrees == null) {
								return -1;
							}
							if (o2.degrees == null) {
								return -1;
							}
							return o1.degrees.toUpperCase().compareTo(
									o2.degrees.toUpperCase());
									//* -1;  //for desc
						}
					});
		  }else{
			  Collections.sort(sData2x2,
						new Comparator<WeatherDataPoint2x2>() {
							public int compare(WeatherDataPoint2x2 o1, WeatherDataPoint2x2 o2) {
								if (o1.lookUpKey == null) {
									return -1;
								}
								if (o2.lookUpKey == null) {
									return -1;
								}
								return o2.lookUpKey.compareTo(o1.lookUpKey);//for reverse (descending) change o2 to o1
							}
						});
		  }
	  	  
	  	
	  	
	 
	  	
	  	//v.2.2:
	  	if (typeofwidget.equals("4x2")){
		  	ContactsArrayAdapter adapter=new ContactsArrayAdapter(this.getBaseContext(), sData);//ContactsArrayAdapter 
			
			lv =(ListView) findViewById(R.id.mylist);
			lv.setAdapter(adapter);
	  	}else{
	  		if (sData==null || sData.isEmpty()){
	  			ContactsArrayAdapter2x2 adapter2x2=new ContactsArrayAdapter2x2(this.getBaseContext(), sData2x2);//ContactsArrayAdapter 
				
				lv =(ListView) findViewById(R.id.mylist);
				lv.setAdapter(adapter2x2);
	  		}else{
		  		ContactsArrayAdapter adapter=new ContactsArrayAdapter(this.getBaseContext(), sData);//ContactsArrayAdapter 
				
				lv =(ListView) findViewById(R.id.mylist);
				lv.setAdapter(adapter);
	  		}
	  	}
		
		et = (EditText) findViewById(R.id.textView1);
		
		//for searching:
		et.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				// Abstract Method of TextWatcher Interface.
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// Abstract Method of TextWatcher Interface.
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				textlength = et.getText().length();
				array_sort.clear();
				for (int i = 0; i < sData.size(); i++) {
					if (textlength <= sData.get(i).degrees.length()) {
						/*if (et.getText()
								.toString()
								.equalsIgnoreCase(
										(String) sData.get(i).degrees.subSequence(
												0, textlength)) || et.getText()
												.toString()
												.equalsIgnoreCase(
														(String) sData.get(i).city.subSequence(
																0, textlength))) {
							array_sort.add(sData.get(i));
						}*/
						String inputStr=et.getText().toString();
						String name=(String) sData.get(i).degrees;
						String number=(String) sData.get(i).city;
						if(name.toLowerCase().contains(inputStr.toLowerCase()) || number.toLowerCase().contains(inputStr.toLowerCase())){
							array_sort.add(sData.get(i));
						}
					}
				}
				ContactsArrayAdapter adaptersearch=new ContactsArrayAdapter(getBaseContext(), array_sort);
				
				lv.setAdapter(adaptersearch);
			}
		});
		
		 ImageView iv = (ImageView)findViewById(R.id.imageViewClear);
	     iv.setOnClickListener(mainClickListener);
	     
	     iv = (ImageView)findViewById(R.id.imageViewClose);
	     iv.setOnClickListener(mainClickListener);
	     
	     
	     //Save sData if not null//v. 2.3
		  
		  //String fileName="sData"+mAppWidgetId;
	  	if (typeofwidget.equals("4x2")){
			  if (sData!=null && !sData.isEmpty()){
				  try {
					FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
					  ObjectOutputStream os = new ObjectOutputStream(fos);
					  os.writeObject(sData);
					  os.close();
					  fos.close();
				} catch (Exception e) {
					
					Log.e("bookmarksInAList", "main activity Exception occured while saving: " + e.getMessage());
					
					e.printStackTrace();
				} 
			  }
	  	}else{
	  		if (sData==null || sData.isEmpty()){
		  		if (sData2x2!=null && !sData2x2.isEmpty()){
					  try {
						FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
						  ObjectOutputStream os = new ObjectOutputStream(fos);
						  os.writeObject(sData2x2);
						  os.close();
						  fos.close();
					} catch (Exception e) {
						
						Log.e("bookmarksInAList", "main activity Exception occured while saving 2x2: " + e.getMessage());
						
						e.printStackTrace();
					} 
				  }
	  		}else{
	  			sData2x2.clear();
	  			for (int i=0; i<sData.size();i++){
	  				WeatherDataPoint temp=sData.get(i);
	  				WeatherDataPoint2x2 temp2x2=new WeatherDataPoint2x2(temp.city, temp.degrees, temp.photo, temp.lookUpKey);
	  				sData2x2.add(temp2x2);
	  			}
	  			try {
					FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
					  ObjectOutputStream os = new ObjectOutputStream(fos);
					  os.writeObject(sData2x2);
					  os.close();
					  fos.close();
				} catch (Exception e) {
					
					Log.e("bookmarksInAList", "main activity Exception occured while saving 2x2: " + e.getMessage());
					
					e.printStackTrace();
				} 
	  		}
	  	}
	  //v. 2.3:END
	  	
		
	}

	
	
	
    private OnClickListener mainClickListener = new OnClickListener() {
	    public void onClick(View v) {
	    	
	    	
	    	
	      // do something when the button is clicked
	    	if (v.getId()==R.id.imageViewClear){
		    	et.setText("");
		    	
	    	}
	    	if (v.getId()==R.id.imageViewClose){
		    	MainActivity.this.finish();
		    	
	    	}
	    	    	
	    	

	    }
	};
	
	//check if app is installed
	private boolean appInstalledOrNot(String uri)
	{
	    PackageManager pm = getPackageManager();
	    boolean app_installed = false;
	    try
	    {
	           pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
	           app_installed = true;
	    }
	    catch (PackageManager.NameNotFoundException e)
	    {
	           app_installed = false;
	    }
	    return app_installed ;
	}
	

}
