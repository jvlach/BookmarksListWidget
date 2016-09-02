package gr.jvlach.bookmarkswidget;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.InputStream;

/**
 * This is the service that provides the factory to be bound to the collection service.
 */
public class BookmarkWidgetService2x2 extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory2x2(this.getApplicationContext(), intent);
    }
}



/**
 * This is the factory that will provide data to the collection widget.
 */
class StackRemoteViewsFactory2x2 implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor mCursor;
    private int mAppWidgetId;
    
  //for photos:
    public Bitmap loadContactPhoto(ContentResolver cr, long  id) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        if (input == null) {
            return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);//null;
        }
        return BitmapFactory.decodeStream(input);
    }

    public StackRemoteViewsFactory2x2(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        //Log.d("getcount", "StackRemoteViewsFactory");
    }

    public void onCreate() {
        // Since we reload the cursor in onDataSetChanged() which gets called immediately after
        // onCreate(), we do nothing here.
    }

    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    public int getCount() {
    	//Log.d("getcount", "StackRemoteViewsFactory-getCount "+mCursor.getCount());
    	try{
    		return mCursor.getCount();
    	}catch (Exception e){
    		Log.d("widexception","getcount");
    		return 0;
    	}
    }

    public RemoteViews getViewAt(int position) {
    	//Log.d("getcount", "StackRemoteViewsFactory-getViewat"+position);
        // Get the data for this position from the content provider
        String city = "No bookmarks";
        String temp = "No bookmarks";
        byte[] photoid=null;//v.1.5
        String lookupkey="";//v.1.2
        Bitmap photo=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);
        
        
        //v. 2.0:
        String city2 = "No bookmarks";
        String temp2 = "No bookmarks";
        byte[] photoid2=null;//v.1.5
        String lookupkey2="";//v.1.2
        Bitmap photo2=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);
        
        
        if (mCursor.moveToPosition(position)) {
            final int cityColIndex = mCursor.getColumnIndex(BookmarkDataProvider2x2.Columns.CITY);
            final int tempColIndex = mCursor.getColumnIndex(
            		BookmarkDataProvider2x2.Columns.TEMPERATURE);
            final int photoColIndex = mCursor.getColumnIndex(
            		BookmarkDataProvider2x2.Columns.PHOTO);
            city = mCursor.getString(cityColIndex);
            temp = mCursor.getString(tempColIndex);
            
            //2x2: when there is no title
            if (temp==null || temp.equals("") || temp.length()==0){
            	if (!(city==null || city.equals("") || city.length()==0)){
            		temp=city;
            	}
            }
            
            try{//v.1.5
            	photoid=mCursor.getBlob(photoColIndex);
            }catch (Exception e){//v.1.5
            	//no getblob
            }//v.1.5
            //v.1.2:
            final int lookupColIndex = mCursor.getColumnIndex(
            		BookmarkDataProvider2x2.Columns.LOOKUPKEY);
            lookupkey=mCursor.getString(lookupColIndex);
            
            if (photoid!=null && photoid.length>0){
    	    	photo=BitmapFactory.decodeByteArray(photoid, 0, photoid.length, null);
    	    	
            }
            
            
            
            //v. 2.0:
            final int cityColIndex2 = mCursor.getColumnIndex(BookmarkDataProvider2x2.Columns.CITY2);
            final int tempColIndex2 = mCursor.getColumnIndex(
            		BookmarkDataProvider2x2.Columns.TEMPERATURE2);
            final int photoColIndex2 = mCursor.getColumnIndex(
            		BookmarkDataProvider2x2.Columns.PHOTO2);
            city2 = mCursor.getString(cityColIndex2);
            temp2 = mCursor.getString(tempColIndex2);
            
          //2x2: when there is no title
            if (temp2==null || temp2.equals("") || temp2.length()==0){
            	if (!(city2==null || city2.equals("") || city2.length()==0)){
            		temp2=city2;
            	}
            }
            
            try{//v.1.5
            	photoid2=mCursor.getBlob(photoColIndex2);
            }catch (Exception e){//v.1.5
            	//no getblob
            	photoid2=null;
            }//v.1.5
            //v.1.2:
            final int lookupColIndex2 = mCursor.getColumnIndex(
            		BookmarkDataProvider2x2.Columns.LOOKUPKEY2);
            lookupkey2=mCursor.getString(lookupColIndex2);
            
            if (photoid2!=null && photoid2.length>0){
    	    	photo2=BitmapFactory.decodeByteArray(photoid2, 0, photoid2.length, null);
    	    	
            }
            
           
        }

        // Return a proper item with the proper city and temperature.  Just for fun, we alternate
        // the items to make the list easier to read.
        final String formatStr = mContext.getResources().getString(R.string.item_format_string);
        
        String theme=AppWidgetConfigure.loadTitlePrefTheme(mContext, mAppWidgetId);
        
        //v. 1.7:
        String back=AppWidgetConfigure.loadTitlePrefBack(mContext, mAppWidgetId);
        
        int itId=((theme.equals("dark") || theme.equals("custom")) ? R.layout.listitem2perline  //v.1.7
                : R.layout.listitem2perline_white);//v. 2.0
                
        
        if (theme.equals("light") && back.equals("none")){
        	itId=R.layout.listitem2perline_white_notrans;
        }
        
        final int itemId = itId;//(theme.equals("dark") ? R.layout.listitem
                //: R.layout.listitem_white);
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), itemId);
        //rv.setTextViewText(R.id.username, String.format(formatStr, temp, city));
        //v. 2.0:
        rv.setTextViewText(R.id.email1, temp);
        
        
        //v. 2.0:
        rv.setTextViewText(R.id.email2, temp2);
        //rv.setTextViewText(R.id.username, temp);
        //rv.setTextViewText(R.id.email, city);
        
      //v.1.7:
        //custom colors
        if (theme.equals("custom")){
        	//int colorText=AppWidgetConfigure.loadCustomColor2(mContext, mAppWidgetId);
        	//rv.setTextColor(R.id.username, colorText);
        	int colorLink=AppWidgetConfigure.loadCustomColor3(mContext, mAppWidgetId);
        	//rv.setTextColor(R.id.email, colorLink);
        	rv.setTextColor(R.id.email1, colorLink);//v. 2.0
        	rv.setTextColor(R.id.email2, colorLink);
        }
        
        //v. 2.0:
        //rv.setImageViewBitmap(R.id.avatar, photo);
        rv.setImageViewBitmap(R.id.avatar1, photo);
        
        rv.setImageViewBitmap(R.id.avatar2, photo2);
        

        // Set the click intent so that we can handle it and call the number
        /*final Intent fillInIntent = new Intent();
        final Bundle extras = new Bundle();
       extras.putString(BookmarkWidgetProvider.EXTRA_CITY_ID, city);
       extras.putString(BookmarkWidgetProvider.EXTRA_BUTTON_ID, "tel");
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.username, fillInIntent);*/
        
     // Set the click intent so that we can handle it and send sms
        final Intent fillInIntent2 = new Intent();
        final Bundle extras2 = new Bundle();
       extras2.putString(BookmarkWidgetProvider2x2.EXTRA_CITY_ID, city);
       extras2.putString(BookmarkWidgetProvider2x2.EXTRA_BUTTON_ID, "sms");
        fillInIntent2.putExtras(extras2);
        rv.setOnClickFillInIntent(R.id.email1, fillInIntent2);//v.2.0
        
        //v.1.2:
     // Set the click intent on the avatar
        final Intent fillInIntent3 = new Intent();
        final Bundle extras3 = new Bundle();
       extras3.putString(BookmarkWidgetProvider2x2.EXTRA_CITY_ID, city);
       extras3.putString(BookmarkWidgetProvider2x2.EXTRA_BUTTON_ID, "avatar");
       extras3.putString(BookmarkWidgetProvider2x2.EXTRA_LOOKUP_KEY, lookupkey);
        fillInIntent3.putExtras(extras3);
        rv.setOnClickFillInIntent(R.id.avatar1, fillInIntent3);//v.2.0
        
        
        //v. 2.0:
      
        final Intent fillInIntent2x2 = new Intent();
        final Bundle extras2x2 = new Bundle();
       extras2x2.putString(BookmarkWidgetProvider2x2.EXTRA_CITY_ID, city2);
       extras2x2.putString(BookmarkWidgetProvider2x2.EXTRA_BUTTON_ID, "sms");
     //duplicates
       //extras2x2.putString(BookmarkWidgetProvider2x2.EXTRA_LOOKUP_KEY, lookupkey2);
        fillInIntent2x2.putExtras(extras2x2);
        rv.setOnClickFillInIntent(R.id.email2, fillInIntent2x2);
       
        
        //v.1.2:
     // Set the click intent on the avatar
        final Intent fillInIntent3x2 = new Intent();
        final Bundle extras3x2 = new Bundle();
       extras3x2.putString(BookmarkWidgetProvider2x2.EXTRA_CITY_ID, city2);
       extras3x2.putString(BookmarkWidgetProvider2x2.EXTRA_BUTTON_ID, "avatar");
       extras3x2.putString(BookmarkWidgetProvider2x2.EXTRA_LOOKUP_KEY, lookupkey2);
        fillInIntent3x2.putExtras(extras3x2);
        rv.setOnClickFillInIntent(R.id.avatar2, fillInIntent3x2);
        

        if (temp2.equals("")){//nothing on last line
        	rv.setViewVisibility(R.id.avatar2, View.GONE);
        	rv.setViewVisibility(R.id.email2, View.GONE);
        }else{
        	rv.setViewVisibility(R.id.avatar2, View.VISIBLE);
        	rv.setViewVisibility(R.id.email2, View.VISIBLE);
        }

        
        return rv;
    }
    public RemoteViews getLoadingView() {
        // We aren't going to return a default loading view in this sample
        return null;
    }

    public int getViewTypeCount() {
        // Technically, we have two types of views (the dark and light background views)
        return 2;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {
        // Refresh the cursor
        if (mCursor != null) {
            mCursor.close();
        }
        String what=AppWidgetConfigure.loadTitlePref(mContext, mAppWidgetId);
    	//v.1.2
    	//if (what.equals("fav")){
    		BookmarkDataProvider2x2.what=what;//"fav";
    	//}else{
    	//	FavoritesDataProvider.what="all";
    	//}
    		
		//v. 1.3
    	String back=AppWidgetConfigure.loadTitlePrefBack(mContext, mAppWidgetId);
    	BookmarkDataProvider2x2.back=back;
    	//Log.d("load transpar service", back+" what="+what+" mAppWidgetId="+mAppWidgetId);
    	
    	//v. 1.6
    	String theme=AppWidgetConfigure.loadTitlePrefTheme(mContext, mAppWidgetId);
    	BookmarkDataProvider2x2.theme=theme;
    	
    	String sort=AppWidgetConfigure.loadTitlePrefSort(mContext, mAppWidgetId);
    	BookmarkDataProvider2x2.sort=sort;
    	
    	//v. 2.2:
    	BookmarkDataProvider2x2.appwidgetid=mAppWidgetId;
    	
    	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
    	
    	
    	
        mCursor = mContext.getContentResolver().query(BookmarkDataProvider2x2.CONTENT_URI, null, what,
                null, sort);
        
        //Log.d("bookmarkWidget", "onDataSetChanged="+mAppWidgetId);
        //v.1.3:
        //FavoritesWidgetProvider.updateAppWidget(mContext, appWidgetManager, mAppWidgetId);
    }
}