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
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.InputStream;

/**
 * This is the service that provides the factory to be bound to the collection service.
 */
public class BookmarkWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}



/**
 * This is the factory that will provide data to the collection widget.
 */
class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
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

    public StackRemoteViewsFactory(Context context, Intent intent) {
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
        if (mCursor.moveToPosition(position)) {
            final int cityColIndex = mCursor.getColumnIndex(BookmarkDataProvider.Columns.CITY);
            final int tempColIndex = mCursor.getColumnIndex(
            		BookmarkDataProvider.Columns.TEMPERATURE);
            final int photoColIndex = mCursor.getColumnIndex(
            		BookmarkDataProvider.Columns.PHOTO);
            city = mCursor.getString(cityColIndex);
            temp = mCursor.getString(tempColIndex);
            try{//v.1.5
            	photoid=mCursor.getBlob(photoColIndex);
            }catch (Exception e){//v.1.5
            	//no getblob
            }//v.1.5
            //v.1.2:
            final int lookupColIndex = mCursor.getColumnIndex(
            		BookmarkDataProvider.Columns.LOOKUPKEY);
            lookupkey=mCursor.getString(lookupColIndex);
            
            if (photoid!=null && photoid.length>0){
    	    	photo=BitmapFactory.decodeByteArray(photoid, 0, photoid.length, null);
    	    	
            }
            
           
        }

        // Return a proper item with the proper city and temperature.  Just for fun, we alternate
        // the items to make the list easier to read.
        final String formatStr = mContext.getResources().getString(R.string.item_format_string);
        
        String theme=AppWidgetConfigure.loadTitlePrefTheme(mContext, mAppWidgetId);
        
        //v. 1.7:
        String back=AppWidgetConfigure.loadTitlePrefBack(mContext, mAppWidgetId);
        
        int itId=((theme.equals("dark") || theme.equals("custom")) ? R.layout.listitem  //v.1.7
                : R.layout.listitem_white);
                
        
        if (theme.equals("light") && back.equals("none")){
        	itId=R.layout.listitem_white_notrans;
        }
        
        final int itemId = itId;//(theme.equals("dark") ? R.layout.listitem
                //: R.layout.listitem_white);
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), itemId);
        //rv.setTextViewText(R.id.username, String.format(formatStr, temp, city));
        rv.setTextViewText(R.id.username, temp);
        rv.setTextViewText(R.id.email, city);
        
      //v.1.7:
        //custom colors
        if (theme.equals("custom")){
        	int colorText=AppWidgetConfigure.loadCustomColor2(mContext, mAppWidgetId);
        	rv.setTextColor(R.id.username, colorText);
        	int colorLink=AppWidgetConfigure.loadCustomColor3(mContext, mAppWidgetId);
        	rv.setTextColor(R.id.email, colorLink);
        }
        
        rv.setImageViewBitmap(R.id.avatar, photo);
        

        // Set the click intent so that we can handle it and call the number
        final Intent fillInIntent = new Intent();
        final Bundle extras = new Bundle();
       extras.putString(BookmarkWidgetProvider.EXTRA_CITY_ID, city);
       extras.putString(BookmarkWidgetProvider.EXTRA_BUTTON_ID, "tel");
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.username, fillInIntent);
        
     // Set the click intent so that we can handle it and send sms
        final Intent fillInIntent2 = new Intent();
        final Bundle extras2 = new Bundle();
       extras2.putString(BookmarkWidgetProvider.EXTRA_CITY_ID, city);
       extras2.putString(BookmarkWidgetProvider.EXTRA_BUTTON_ID, "sms");
        fillInIntent2.putExtras(extras2);
        rv.setOnClickFillInIntent(R.id.email, fillInIntent2);
        
        //v.1.2:
     // Set the click intent on the avatar
        final Intent fillInIntent3 = new Intent();
        final Bundle extras3 = new Bundle();
       extras3.putString(BookmarkWidgetProvider.EXTRA_CITY_ID, city);
       extras3.putString(BookmarkWidgetProvider.EXTRA_BUTTON_ID, "avatar");
       extras3.putString(BookmarkWidgetProvider.EXTRA_LOOKUP_KEY, lookupkey);
        fillInIntent3.putExtras(extras3);
        rv.setOnClickFillInIntent(R.id.avatar, fillInIntent3);

        

        
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
    		BookmarkDataProvider.what=what;//"fav";
    	//}else{
    	//	FavoritesDataProvider.what="all";
    	//}
    		
		//v. 1.3
    	String back=AppWidgetConfigure.loadTitlePrefBack(mContext, mAppWidgetId);
    	BookmarkDataProvider.back=back;
    	//Log.d("load transpar service", back+" what="+what+" mAppWidgetId="+mAppWidgetId);
    	
    	//v. 1.6
    	String theme=AppWidgetConfigure.loadTitlePrefTheme(mContext, mAppWidgetId);
    	BookmarkDataProvider.theme=theme;
    	
    	String sort=AppWidgetConfigure.loadTitlePrefSort(mContext, mAppWidgetId);
    	BookmarkDataProvider.sort=sort;
    	
    	//v. 2.2:
    	BookmarkDataProvider.appwidgetid=mAppWidgetId;
    	
    	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
    	
    	String[] selectionArgs=new String[]{""+mAppWidgetId};
    	
        mCursor = mContext.getContentResolver().query(BookmarkDataProvider.CONTENT_URI, null, what,
        		selectionArgs, sort);
        
        //Log.d("bookmarkWidget", "onDataSetChanged="+mAppWidgetId);
        //v.1.3:
        //FavoritesWidgetProvider.updateAppWidget(mContext, appWidgetManager, mAppWidgetId);
    }
}