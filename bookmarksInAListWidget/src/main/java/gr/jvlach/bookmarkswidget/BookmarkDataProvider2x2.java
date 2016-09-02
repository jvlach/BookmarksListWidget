package gr.jvlach.bookmarkswidget;



import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.Browser;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;


class WeatherDataPoint2x2 implements java.io.Serializable{//v. 2.2
    /**
	 * 
	 */
	private static final long serialVersionUID = 2L;
    String city;
    String degrees;
    byte[] photo;
    Long lookUpKey;
    
    //v1.2:(added l)
    WeatherDataPoint2x2(String c, String d, byte[] p,long l) {
        city = c;
        degrees = d;
        photo=p;
        lookUpKey=l;
    }
}

/**
 * The AppWidgetProvider.
 */
public class BookmarkDataProvider2x2 extends ContentProvider {
	//v. 1.9:
	Hashtable<String, String> sDataNoMultipleURLs=new Hashtable<String, String>();
	
    public static final Uri CONTENT_URI =
        Uri.parse("content://gr.jvlach.bookmarkswidget.provider2x2");
    public static class Columns {
        public static final String ID = "_id";
        public static final String CITY = "city";
        public static final String TEMPERATURE = "temperature";
        public static final String PHOTO = "photo";
        public static final String LOOKUPKEY = "lookupkey";
        //2x2:
        public static final String ID2 = "_id2";
        public static final String CITY2 = "city2";
        public static final String TEMPERATURE2 = "temperature2";
        public static final String PHOTO2 = "photo2";
        public static final String LOOKUPKEY2 = "lookupkey2";
        public static final String PHONETYPE2 = "phonetype2";
    }

  
    private static final ArrayList<WeatherDataPoint2x2> sData = new ArrayList<WeatherDataPoint2x2>();

    public static String what="stock";
    public static String back="high";
    public static String theme="dark";
    public static String sort="desk";
    public static int appwidgetid=0;//v. 2.2
    
    //for photos:
    public Bitmap loadContactPhoto(ContentResolver cr, long  id) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        if (input == null) {
            return BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.ic_launcher);//null;
        }
        return BitmapFactory.decodeStream(input);
    }
    
    
    @Override
    public boolean onCreate() {
    	       
        return true;
    }

    @Override
    public synchronized Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        assert(uri.getPathSegments().isEmpty());
        //Log.d("getcount", "FavoritesDataProvider-query");
        
        int mAppWidgetId=appwidgetid;//Integer.parseInt(selectionArgs[0]);//v. 2.2
        
        if (selectionArgs!=null && selectionArgs.length>0){
        	mAppWidgetId=Integer.parseInt(selectionArgs[0]);
        }
        /////////////////////////////////  THIS IS WHERE THE JOB IS DONE  //////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
      //test//
        
        try {
			Uri u=Uri.parse("content://com.android.chrome.browser/bookmarks");
			boolean b=checkContentProvider(u);
			u=Uri.parse("content://com.android.chrome.browser/history");
			b=checkContentProvider(u);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
        
        //test:END//
        
        
        
        sData.clear();
       
        String localwhat=what;
		//selection is used to pass the group id
		if (selection!=null){
			localwhat=selection;
		}
		
		getBrowser(localwhat);
		
		sDataNoMultipleURLs.clear();
		
		String sortorderlocal="";
		if (sortOrder!=null){
			sortorderlocal=sortOrder;
		}
		
		//If sData is null, read the saved one://v. 2.2
				String fileName="sData"+mAppWidgetId;
				  if (sData==null || sData.isEmpty()){
					  try {
						FileInputStream fis = this.getContext().openFileInput(fileName);
						ObjectInputStream is = new ObjectInputStream(fis);
						ArrayList<WeatherDataPoint2x2> simpleClass = (ArrayList<WeatherDataPoint2x2>) is.readObject();
						sData.addAll(simpleClass);
						is.close();
						fis.close();
						Log.e("bookmarksInAList", "Size of saved file: " + sData.size());
					  } catch (Exception e) {
							
							Log.e("bookmarksInAList", "Exception occured while reading: " + e.getMessage());
						} 
					  }
		
		
		 //sort the data:
	  if (!sortorderlocal.equals("date")){	//for desc
	  	  Collections.sort(sData,
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
	  }else{//for date
		  Collections.sort(sData,
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
	  
	  
 //Save sData if not null//v. 2.2
	  
	  //String fileName="sData"+mAppWidgetId;
	  if (sData!=null && !sData.isEmpty()){
		  try {
			FileOutputStream fos = this.getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
			  ObjectOutputStream os = new ObjectOutputStream(fos);
			  os.writeObject(sData);
			  os.close();
			  fos.close();
		} catch (Exception e) {
			
			Log.e("bookmarksInAList", "Exception occured while saving: " + e.getMessage());
			
			e.printStackTrace();
		} 
	  }
	  
		
		//2x2: change
    	final MatrixCursor c = new MatrixCursor(//v.1.2:(change)
                new String[]{ Columns.ID, Columns.CITY, Columns.TEMPERATURE ,Columns.PHOTO,Columns.LOOKUPKEY   , Columns.CITY2, Columns.TEMPERATURE2 ,Columns.PHOTO2,Columns.LOOKUPKEY2});
        /*for (int i = 0; i < sData.size(); ++i) {
            final WeatherDataPoint2x2 data = sData.get(i);
            //Log.d("bookmarks data=",i+" "+data.city);
            c.addRow(new Object[]{ new Integer(i), data.city, data.degrees, data.photo,data.lookUpKey });//new Integer(
            
        }*/
    	
    	
    	for (int i = 0; i < sData.size(); i+=2) {
         	
            WeatherDataPoint2x2 data =null;
            
        	try {
				data= sData.get(i);
			} catch (Exception e) {
				
				//e.printStackTrace();
			}
            
        	WeatherDataPoint2x2 data1 = null;
        	
        	try {
				data1=sData.get(i+1);
			} catch (Exception e) {
				
				//e.printStackTrace();
			}
        	
        	if (data1!=null){
        		c.addRow(new Object[]{ new Integer(i), data.city, data.degrees, data.photo,data.lookUpKey,      data1.city, data1.degrees, data1.photo,data1.lookUpKey});//new Integer(
        	}else{
        		c.addRow(new Object[]{ new Integer(i), data.city, data.degrees, data.photo,data.lookUpKey,   "", "", "",""});//new Integer(
        	}
       }
    	
    	
        return c;
    }
    
    public void getBrowser(String selection){
    	
    	//Log.d("bookmarks selection=",""+selection);
    	final String TOUCH_ICON ="touch_icon";
		//String[] proj = new String[] { Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL, Browser.BookmarkColumns.FAVICON};//,TOUCH_ICON
		String sel = Browser.BookmarkColumns.BOOKMARK + " = 1"; // 0 = history, 1 = bookmark
		
		Uri uriCustom=Browser.BOOKMARKS_URI;
		
		String orderby="";//needs to be put in chrome as well
		
		orderby=Browser.BookmarkColumns.DATE + " DESC";
		
		boolean installed  = false;
		if (selection.equals("stock")){
			uriCustom=Browser.BOOKMARKS_URI;
			//check for samsung browser
			
		}else if (selection.equals("chrome")){
			uriCustom=Uri.parse("content://com.android.chrome.browser/bookmarks");
		}
		//for chrome:
		//Uri uriCustom = Uri.parse("content://com.android.chrome.browser/bookmarks");
		String title = "";
		String url = "";
		long date=0;
		Bitmap favicon;
		
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
	        final Cursor c =  this.getContext().getContentResolver().query(uri, null, null, null, orderby2);
	
	      
	        
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
	          		  	if (sDataNoMultipleURLs.containsKey(key)){
	          		  		//don't put the duplicate
	          		  	}else{
	          		  		sDataNoMultipleURLs.put(key, key);
	          		  		sData.add(new WeatherDataPoint2x2(url, title,data,date));
	          		  	}
	                
	            	}catch(Exception e){
	            		Log.e("bookmarksinalist", "exception");
	            	}
	            	
	            	
	            } while(c.moveToNext());
	
	        }
	    }else{
	    	Cursor mCur = this.getContext().getContentResolver().query(uriCustom, null, sel, null, orderby);//null);//Browser.BOOKMARKS_URI
			//mCur.moveToFirst();//v1.5
			
			
			
			if(mCur!=null){//v.1.5
				if (mCur.moveToFirst() && mCur.getCount() > 0) {
				    boolean cont = true;
				    while (mCur.isAfterLast() == false && cont) {
				        title = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.TITLE));
				        url = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));
				        
				      //Log.e("bookmarksinalist","URL "+ url);
		              //Log.e("bookmarksinalist", "TITLE " +title);
		                //Log.e("bookmarksinalist","sURL " + c.getString(c.getColumnIndex("sURL")));
		                //Log.e("bookmarksinalist","modified " + c.getString(c.getColumnIndex("MODIFIED")));
				        
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
				        	if (data!=null && data.length>0){
					        	//do nothing
					        }else {//get favicon
					        	data = mCur.getBlob(mCur.getColumnIndex(Browser.BookmarkColumns.FAVICON));
					        }
				        }else if (selection.equals("chrome")){
				        	data = mCur.getBlob(mCur.getColumnIndex(Browser.BookmarkColumns.FAVICON));
				        }else{
				        	//seems that some times touch_icon does not exist
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
				        
				        
				        String key=title+url;
	          		  	if (sDataNoMultipleURLs.containsKey(key)){
	          		  		//don't put the duplicate
	          		  	}else{
	          		  		sDataNoMultipleURLs.put(key, key);
	          		  		sData.add(new WeatherDataPoint2x2(url, title,data,date));
	          		  	}
				        // Do something with title and url
				        //Log.d("bookmark",title+" " +url);
				        mCur.moveToNext();
				    }
				}
				
		    }//v1.5
	    }
		
		
		
		if (selection.equals("all")){//have done it for stock, now do it for chrome and merge
			uriCustom=Uri.parse("content://com.android.chrome.browser/bookmarks");
			
			Cursor mCur2 = this.getContext().getContentResolver().query(uriCustom, null, sel, null, orderby);//null);//Browser.BOOKMARKS_URI
			//mCur2.moveToFirst();//v1.5
			if (mCur2!=null){//v1.5
				if (mCur2.moveToFirst() && mCur2.getCount() > 0) {
				    boolean cont = true;
				    while (mCur2.isAfterLast() == false && cont) {
				        title = mCur2.getString(mCur2.getColumnIndex(Browser.BookmarkColumns.TITLE));
				        url = mCur2.getString(mCur2.getColumnIndex(Browser.BookmarkColumns.URL));
				        //Log.e("bookmarksinalist","Chrome URL "+ url);
			            //  Log.e("bookmarksinalist", "Chrome TITLE " +title);
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
				        
				        String key=title+url;
	          		  	if (sDataNoMultipleURLs.containsKey(key)){
	          		  		//don't put the duplicate
	          		  	}else{
	          		  		sDataNoMultipleURLs.put(key, key);
	          		  		sData.add(new WeatherDataPoint2x2(url, title,data,date));
	          		  	}
				        
				        
				        mCur2.moveToNext();
				    }
				}
				
			}//v1.5
			
			
		}
		
		

		
		
		
	}

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd.favoritecontactswidget.citytemperature";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // This example code does not support inserting
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // This example code does not support deleting
        return 0;
    }

    @Override
    public synchronized int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        assert(uri.getPathSegments().size() == 1);
        
        final int index = Integer.parseInt(uri.getPathSegments().get(0));
        final MatrixCursor c = new MatrixCursor(//v.1.2:(change)
                new String[]{ Columns.ID, Columns.CITY, Columns.TEMPERATURE,Columns.PHOTO,Columns.LOOKUPKEY });
        assert(0 <= index && index < sData.size());
        final WeatherDataPoint2x2 data = sData.get(index);
        data.degrees = values.getAsString(Columns.TEMPERATURE);

        // Notify any listeners that the data backing the content provider has changed, and return
        // the number of rows affected.
        getContext().getContentResolver().notifyChange(uri, null);
        return 1;
    }
    
    
  //check if app is installed
    private boolean appInstalledOrNot(String uri)
    {
        PackageManager pm = this.getContext().getPackageManager();
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
    
    private boolean checkContentProvider(Uri uri) //uri = content://com.android.chrome.browser/history

    {
        
         int getcount=0;
         try{
        	 Cursor mCur = this.getContext().getContentResolver().query(uri, null, null, null, null);
        	 getcount= mCur.getCount();
         }catch(Exception e){
        	 getcount=0;
         }
         return (getcount > 0);
    }

}
