package gr.jvlach.bookmarkswidget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.ContactsContract.Groups;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Our data observer just notifies an update for all Favorites widgets when it detects a change.
 */
class FavoritesDataProviderObserver2x2 extends ContentObserver {
    private AppWidgetManager mAppWidgetManager;
    private ComponentName mComponentName;
    

    FavoritesDataProviderObserver2x2(AppWidgetManager mgr, ComponentName cn, Handler h) {
        super(h);
        mAppWidgetManager = mgr;
        mComponentName = cn;
    }

    @Override
    public void onChange(boolean selfChange) {
        // The data has changed, so notify the widget that the collection view needs to be updated.
        // In response, the factory's onDataSetChanged() will be called which will requery the
        // cursor for the new data.
        mAppWidgetManager.notifyAppWidgetViewDataChanged(
        mAppWidgetManager.getAppWidgetIds(mComponentName), R.id.mylist2x2);
        //Log.d("getcount", "FavoritesWidgetProvider-onchange");
    }
}

/**
 * The Favorites widget's AppWidgetProvider.
 */
public class BookmarkWidgetProvider2x2 extends AppWidgetProvider {
    public static String CLICK_ACTION = "gr.jvlach.bookmarkswidget.CLICK";
    public static String CLICK_ACTIONSMS = "gr.jvlach.bookmarkswidget.CLICKSMS";
    public static String REFRESH_ACTION = "gr.jvlach.bookmarkswidget.REFRESH";//REFRESH_UPDATE_ACTION
    
    //v1.6
    public static String REFRESH_UPDATE_ACTION = "gr.jvlach.bookmarkswidget.REFRESH_UPDATE";//REFRESH_UPDATE_ACTION
    
    public static String SEARCH_ACTION = "gr.jvlach.bookmarkswidget.SEARCH";
    public static String EXTRA_CITY_ID = "gr.jvlach.bookmarkswidget.city";
    public static String EXTRA_BUTTON_ID = "gr.jvlach.bookmarkswidget.button";
    //v1.2:
    public static String EXTRA_LOOKUP_KEY = "gr.jvlach.bookmarkswidget.lookupkey";

    private static HandlerThread sWorkerThread;
    private static Handler sWorkerQueue;
    private static FavoritesDataProviderObserver2x2 sDataObserver;
    private Cursor mCursor;
    
    
    
    public static String getGroupNameFor(String groupId,Context mcontext){
        Uri uri = Groups.CONTENT_URI;
        String where = String.format("%s = ?", Groups._ID);
        String[] whereParams = new String[]{groupId};
        String[] selectColumns = {Groups.TITLE};
        Cursor c = mcontext.getContentResolver().query(
                uri, 
                selectColumns,
                where, 
                whereParams, 
                null);

        try{
            if (c.moveToFirst()){
                return c.getString(0);  
            }
            return null;
        }finally{
            c.close();
        }
    }

    public BookmarkWidgetProvider2x2() {
        // Start the worker thread
        sWorkerThread = new HandlerThread("FavoritesWidgetProvider-worker");
        sWorkerThread.start();
        sWorkerQueue = new Handler(sWorkerThread.getLooper());
        //Log.d("getcount", "FavoritesWidget");
    }

    @Override
    public void onEnabled(Context context) {
        // Register for external updates to the data to trigger an update of the widget.  When using
        // content providers, the data is often updated via a background service, or in response to
        // user interaction in the main app.  To ensure that the widget always reflects the current
        // state of the data, we must listen for changes and update ourselves accordingly.
        final ContentResolver r = context.getContentResolver();
        if (sDataObserver == null) {
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context, BookmarkWidgetProvider2x2.class);
            sDataObserver = new FavoritesDataProviderObserver2x2(mgr, cn, sWorkerQueue);
            r.registerContentObserver(BookmarkDataProvider2x2.CONTENT_URI, true, sDataObserver);
          //test//
            
            try {
    			Uri u=Uri.parse("content://com.android.chrome.browser/bookmarks");
    			boolean b=checkContentProvider(u,context);
    			u=Uri.parse("content://com.android.chrome.browser/history");
    			b=checkContentProvider(u,context);
    		} catch (Exception e) {
    			
    			e.printStackTrace();
    		}
            
            //test:END//
        }
    }

    @Override
    public void onReceive(Context ctx, Intent intent) {
    	//Log.d("getcount", "FavoritesWidgetProvider-onreceive");
        final String action = intent.getAction();
        final int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        //v1.6
       
        
        if (action.equals(REFRESH_UPDATE_ACTION)) {
            // BroadcastReceivers have a limited amount of time to do work, so  we
            // are triggering an update of the data on another thread.  This update
            // can also be triggered from a background service, or perhaps as a result of user actions
            // inside the main application.
            final Context context = ctx;
            sWorkerQueue.removeMessages(0);
            sWorkerQueue.post(new Runnable() {
                @Override
                public void run() {
                    final ContentResolver r = context.getContentResolver();
                    final Cursor c = r.query(BookmarkDataProvider2x2.CONTENT_URI, null, null, null, 
                            null);
                   
                    
                    final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                    final ComponentName cn = new ComponentName(context, BookmarkWidgetProvider2x2.class);
                    
                    
                    
                    
                    String what=AppWidgetConfigure.loadTitlePref(context, appWidgetId);
                    BookmarkDataProvider2x2.what=what;
                	                		
                	//v. 1.3
                	String back=AppWidgetConfigure.loadTitlePrefBack(context, appWidgetId);
                	BookmarkDataProvider2x2.back=back;
                	
                	//v. 1.6
                	String theme=AppWidgetConfigure.loadTitlePrefTheme(context, appWidgetId);
                	BookmarkDataProvider2x2.theme=theme;
                	//Log.d("bookmarkWidget receive update", back+" what="+what+" mAppWidgetId="+appWidgetId);
                	
                	//v. 1.6
                	String sort=AppWidgetConfigure.loadTitlePrefSort(context, appWidgetId);
                	BookmarkDataProvider2x2.sort=sort;
                	
                	//v. 2.2:
                	BookmarkDataProvider2x2.appwidgetid=appWidgetId;
                	
                	//Toast.makeText(context, context.getText(R.string.refresh), Toast.LENGTH_SHORT).show();
                    mgr.notifyAppWidgetViewDataChanged(appWidgetId, R.id.mylist2x2);//mgr.getAppWidgetIds(cn)
                    
                    c.close();
                    //mgr.updateAppWidget(cn, updateViews);
                }
            });
        }else if (action.equals(REFRESH_ACTION)) {
            
            final Context context = ctx;
            sWorkerQueue.removeMessages(0);
            sWorkerQueue.post(new Runnable() {
                @Override
                public void run() {
                    final ContentResolver r = context.getContentResolver();
                    final Cursor c = r.query(BookmarkDataProvider2x2.CONTENT_URI, null, null, null, 
                            null);
                   
                    final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                    final ComponentName cn = new ComponentName(context, BookmarkWidgetProvider2x2.class);
                    
                    
                    
                    
                    String what=AppWidgetConfigure.loadTitlePref(context, appWidgetId);
                    BookmarkDataProvider2x2.what=what;
                	
                		
                	//v. 1.3
                	String back=AppWidgetConfigure.loadTitlePrefBack(context, appWidgetId);
                	BookmarkDataProvider2x2.back=back;
                	
                	//v. 1.6
                	String theme=AppWidgetConfigure.loadTitlePrefTheme(context, appWidgetId);
                	BookmarkDataProvider2x2.theme=theme;
                	//Log.d("bookmarkWidget receive", back+" what="+what+" mAppWidgetId="+appWidgetId);
                	
                	//v. 1.6
                	String sort=AppWidgetConfigure.loadTitlePrefSort(context, appWidgetId);
                	BookmarkDataProvider2x2.sort=sort;
                	
                	//v. 2.2:
                	BookmarkDataProvider2x2.appwidgetid=appWidgetId;
                	
                	
                	//if (whereFrom==1)
                		Toast.makeText(context, context.getText(R.string.refresh), Toast.LENGTH_SHORT).show();
                    mgr.notifyAppWidgetViewDataChanged(appWidgetId, R.id.mylist2x2);//mgr.getAppWidgetIds(cn)
                    
                    c.close();
                    //mgr.updateAppWidget(cn, updateViews);
                }
            });
        } else if (action.equals(CLICK_ACTION)) {
        
        	final String city = intent.getStringExtra(EXTRA_CITY_ID);
        	final String formatStr = ctx.getResources().getString(R.string.toast_format_string);
            Toast.makeText(ctx, String.format(formatStr, city), Toast.LENGTH_SHORT).show();
            
            
        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(city));
        	browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	ctx.startActivity(browserIntent);
            
        	
            
        }else if (action.equals(SEARCH_ACTION)) {
        	
        	String what=AppWidgetConfigure.loadTitlePref(ctx, appWidgetId);
        	
        	MainActivity.what=what;//"fav";
        	MainActivity.appwidgetid=appWidgetId;//v. 2.2
        	MainActivity.typeofwidget="2x2";//v. 2.2
        	
        	Intent searchIntent = new Intent(new Intent(ctx.getApplicationContext(), MainActivity.class));
        	
        	searchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(searchIntent);
        }

        super.onReceive(ctx, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    	// To prevent any ANR timeouts, we perform the update in a service
       // context.startService(new Intent(context, BookmarkWidgetService2x2.class));
        // Update each of the widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; ++i) {
   
        	
        	final Intent refreshIntent = new Intent(context, BookmarkWidgetProvider2x2.class);
            refreshIntent.setAction(BookmarkWidgetProvider2x2.REFRESH_UPDATE_ACTION);
   
            Uri data = Uri.withAppendedPath(
            	    Uri.parse("FavWidget" + "://widget/id/")//put Up to distinguish
            	    ,String.valueOf(appWidgetIds[i]));
            refreshIntent.setData(data);
            
            refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            context.sendBroadcast(refreshIntent);
        	//change:END
        	updateAppWidget(context,appWidgetManager,appWidgetIds[i]);
         
        }
        Log.d("bookmarkWidget","in onupdate=onupdate");
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
    
    //for config
    @SuppressLint("NewApi")
	static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
          int appWidgetId) {//, String titlePrefix
    	
    	String what=AppWidgetConfigure.loadTitlePref(context, appWidgetId);
    	
    	BookmarkDataProvider2x2.what=what;//"fav";
    	
		//v. 1.3
    	String back=AppWidgetConfigure.loadTitlePrefBack(context, appWidgetId);
    	BookmarkDataProvider2x2.back=back;
    	
    	final Intent intent = new Intent(context, BookmarkWidgetService2x2.class);
    	//to make list clickable
    	//intent.setAction(BookmarkWidgetProvider.CLICK_ACTION);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        
      //v. 1.3
    	//Log.d("transpar updateappwidget", back+" what="+what+" mAppWidgetId="+appWidgetId);
      	int whichBackground=R.layout.widget_trans_white2x2;
      	
      	 //v. 2.2:
    	BookmarkDataProvider2x2.appwidgetid=appWidgetId;

    	
    	
    	//v. 1.6 theme
    	String theme=AppWidgetConfigure.loadTitlePrefTheme(context, appWidgetId);
    	BookmarkDataProvider2x2.theme=theme;
    	
    	if (theme.equals("dark")){
    		if (back.equals("high")){
        		whichBackground=R.layout.widget_trans2x2;
        	}else if (back.equals("low")){
        		whichBackground=R.layout.widget_2x2;
        	}else if (back.equals("full")){//v. 1.7
        		whichBackground=R.layout.widget_full_trans2x2;
        	}else if (back.equals("none")){//v. 1.7
        		whichBackground=R.layout.widget_notrans2x2;
        	}
    	}else if (theme.equals("light")){//v.1.7
    		if (back.equals("high")){
        		whichBackground=R.layout.widget_trans_white2x2;
        	}else if (back.equals("low")){
        		whichBackground=R.layout.widget_white2x2;
        	}else if (back.equals("full")){//v. 1.7
        		whichBackground=R.layout.widget_full_trans_white2x2;
        	}else if (back.equals("none")){//v. 1.7
        		whichBackground=R.layout.widget_notrans_white2x2;
        	}
    	}else{//custom v.1.7
    		whichBackground=R.layout.widget_notrans2x2;//v. 1.8
    	}
    	
    	//v. 1.6 sort
    	String sort=AppWidgetConfigure.loadTitlePrefSort(context, appWidgetId);
    	BookmarkDataProvider2x2.sort=sort;
    	
        
        final RemoteViews rv = new RemoteViews(context.getPackageName(),whichBackground);//v. 1.3(change) R.layout.layout_appwidget);
        
        
        //v. 1.6:let's see if it works
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        	rv.setRemoteAdapter(R.id.mylist2x2, intent);
        }else{
        	rv.setRemoteAdapter(appWidgetId, R.id.mylist2x2, intent);
        }
        // Set the empty view to be displayed if the collection is empty.  It must be a sibling
        // view of the collection view.
        rv.setEmptyView(R.id.mylist2x2, R.id.empty_view);
        
        //v.1.3:for the title
        String groupname=AppWidgetConfigure.loadTitlePrefTitle(context, appWidgetId);
        String forstock=(String) context.getResources().getText(R.string.stock );
        String forchrome=(String) context.getResources().getText(R.string.chrome );
        String forall=(String) context.getResources().getText(R.string.all );
        if (what.equals("stock")){
        	
        	rv.setTextViewText(R.id.textViewName, forstock);
        }else if (what.equals("all")){
        	rv.setTextViewText(R.id.textViewName, forall);
        }else if (what.equals("chrome")){
        	rv.setTextViewText(R.id.textViewName, forchrome);
        }else{
        	//for old widgets
        	rv.setTextViewText(R.id.textViewName, forstock);
        }
        
        if (theme.equals("custom")){//v.1.7:get custom colors
        	int colorBack=AppWidgetConfigure.loadCustomColor1(context, appWidgetId);
        	rv.setInt(R.id.linlayoutcustom, "setBackgroundColor", colorBack);//v. 1.8
        	int colorText=AppWidgetConfigure.loadCustomColor2(context, appWidgetId);
        	rv.setTextColor(R.id.textViewName, colorText);
        	//rv.setTextColor(R.id.textView1, colorText);//2x2
        	//rv.setTextColor(R.id.username, colorText);
        	int colorLink=AppWidgetConfigure.loadCustomColor3(context, appWidgetId);
        	//rv.setTextColor(R.id.username, colorLink);
        	rv.setTextColor(R.id.email1, colorLink);//2x2
        	rv.setTextColor(R.id.email2, colorLink);
        }
        
        //v. 1.8: for the background problem
        if (theme.equals("dark") && back.equals("none")){
        	rv.setInt(R.id.linlayout, "setBackgroundColor", Color.BLACK);//v. 1.8;
        }
        //set the background color:
        //rv.setInt(R.id.linlayout, "setBackgroundColor", Color.parseColor("#9933CC"));
        
        // Bind a click listener template for the contents of the Favorites list.  Note that we
        // need to update the intent's data if we set an extra, since the extras will be
        // ignored otherwise.
        final Intent onClickIntent = new Intent(context, BookmarkWidgetProvider2x2.class);
        onClickIntent.setAction(BookmarkWidgetProvider2x2.CLICK_ACTION);
        onClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        onClickIntent.setData(Uri.parse(onClickIntent.toUri(Intent.URI_INTENT_SCHEME)));
        final PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(context, 0,
                onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.mylist2x2, onClickPendingIntent);//listbutton
        
        

        // Bind the click intent for the refresh button on the widget
        final Intent refreshIntent = new Intent(context, BookmarkWidgetProvider2x2.class);
        refreshIntent.setAction(BookmarkWidgetProvider2x2.REFRESH_ACTION);
////////////////
        Uri data = Uri.withAppendedPath(
        	    Uri.parse("FavWidget" + "://widget/id/")
        	    ,String.valueOf(appWidgetId));
        refreshIntent.setData(data);
        //refreshIntent.putExtra("refreshupdate", 1);
        refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //////////////////
        final PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0,
                refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.refresh, refreshPendingIntent);
        
     // Bind the click intent for the search button on the widget
        //Log.d("whatbefore",""+appWidgetId);
        
       final Intent searchIntent = new Intent(context, BookmarkWidgetProvider2x2.class);
        
        searchIntent.setData(data);
        searchIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        searchIntent.setAction(BookmarkWidgetProvider2x2.SEARCH_ACTION);
        
        final PendingIntent searchPendingIntent = PendingIntent.getBroadcast(context, 0,
                searchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.textViewName, searchPendingIntent);//2x2

                   
        
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    private boolean checkContentProvider(Uri uri,Context context) //uri = content://com.android.chrome.browser/history

    {
        
         int getcount=0;
         try{
        	 Cursor mCur = context.getContentResolver().query(uri, null, null, null, null);
        	 getcount= mCur.getCount();
         }catch(Exception e){
        	 getcount=0;
         }
         return (getcount > 0);
    }
    
}