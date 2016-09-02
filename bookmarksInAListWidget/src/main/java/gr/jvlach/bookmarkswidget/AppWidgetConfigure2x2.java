package gr.jvlach.bookmarkswidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

import java.util.HashMap;
import java.util.Map;


public class AppWidgetConfigure2x2 extends Activity {
	//initial colors for colorpicker//v.1.7:
	int color1=Color.BLACK;
	int color2=Color.WHITE;
	int color3=Color.parseColor("#0099CC");
	
	
	 private static final String PREFS_NAME
     = "gr.jvlach.bookmarkswidget.BookmarkWidgetProvider";
private static final String PREF_PREFIX_KEY = "prefix_";
private static final String PREF_PREFIX_BACK_KEY = "prefixback_";
private static final String PREF_PREFIX_TITLE_KEY = "prefixtitle_";
//v1.6 for dark or light background
private static final String PREF_PREFIX_THEME_KEY = "prefixtheme_";
//v1.6 for sortorder
private static final String PREF_PREFIX_SORT_KEY = "prefixsort_";
//v1.7 for colors
private static final String PREF_PREFIX_COLOR1_KEY = "prefixcolor1_";
private static final String PREF_PREFIX_COLOR2_KEY = "prefixcolor2_";
private static final String PREF_PREFIX_COLOR3_KEY = "prefixcolor3_";

Button configOkButton;
int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

private AdView adView;
private Map<String,Integer> groupIds=new HashMap<String, Integer>();

private boolean isNetworkConnected() {
	  
	ConnectivityManager cm = (ConnectivityManager) getSystemService(
	        Context.CONNECTIVITY_SERVICE);

	    NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    if (wifiNetwork != null && wifiNetwork.isConnected()) {
	      return true;
	    }

	    NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    if (mobileNetwork != null && mobileNetwork.isConnected()) {
	      return true;
	    }

	    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	    if (activeNetwork != null && activeNetwork.isConnected()) {
	      return true;
	    }

	    return false;
	  
	  
	  
	 }


@Override
protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
super.onCreate(savedInstanceState);
//Log.d("getcount", "AppWidgetConfigure");
try{
	groupIds.clear();
}catch(Exception e){
	//do nothing
}

setContentView(R.layout.appwidgetconfigure2x2);

//v. 1.7:
//load custom colors if they have been saved:
int colorBack=loadCustomColor1(this);
int colorText=loadCustomColor2(this);
int colorTextLinks=loadCustomColor3(this);

CheckBox ch1 = (CheckBox) findViewById(R.id.checkBox1);

if (colorBack!=-1){
	//check the checkbox
	
	ch1.setChecked(true);
	//put the color
	color1=colorBack;
	
}else{
	//uncheck the checkbox
	
	ch1.setChecked(false);
}

if (colorText!=-1){
	
	//put the color
	color2=colorText;
	
}

if (colorTextLinks!=-1){
	
	//put the color
	color3=colorTextLinks;
	
}

//theme radio buttons:
RadioButton button1 = (RadioButton) findViewById(R.id.radiotheme0);

button1.setOnClickListener(radio_listener);

//v.1.7:
button1.setChecked(true);
button1.performClick();

button1 = (RadioButton) findViewById(R.id.radiotheme1);

button1.setOnClickListener(radio_listener);
button1 = (RadioButton) findViewById(R.id.radiotheme2);

button1.setOnClickListener(radio_listener);

//back trans radio buttons:
button1 = (RadioButton) findViewById(R.id.radiotransFull);
button1.setOnClickListener(radio_listener_back);
button1 = (RadioButton) findViewById(R.id.radiotransNone);
button1.setOnClickListener(radio_listener_back);
button1 = (RadioButton) findViewById(R.id.radiotrans0);
button1.setOnClickListener(radio_listener_back);
button1 = (RadioButton) findViewById(R.id.radiotrans1);
button1.setOnClickListener(radio_listener_back);

//custom theme textviews:

TextView textView1 = (TextView) findViewById(R.id.textViewCustom1_1);

textView1.setOnClickListener(customTextViewsOnClickListener);

textView1 = (TextView) findViewById(R.id.textViewCustom1_2);

textView1.setOnClickListener(customTextViewsOnClickListener);
textView1 = (TextView) findViewById(R.id.textViewCustom2_1);

textView1.setOnClickListener(customTextViewsOnClickListener);
textView1 = (TextView) findViewById(R.id.textViewCustom2_2);

textView1.setOnClickListener(customTextViewsOnClickListener);
textView1 = (TextView) findViewById(R.id.textViewCustom3_1);

textView1.setOnClickListener(customTextViewsOnClickListener);
textView1 = (TextView) findViewById(R.id.textViewCustom3_2);

textView1.setOnClickListener(customTextViewsOnClickListener);

//v. 1.6
//setResult(RESULT_CANCELED);

     
  
     configOkButton = (Button)findViewById(R.id.okconfig);
     configOkButton.setOnClickListener(configOkButtonOnClickListener);
  
     Intent intent = getIntent();
     Bundle extras = intent.getExtras();
     if (extras != null) {
         mAppWidgetId = extras.getInt(
                 AppWidgetManager.EXTRA_APPWIDGET_ID,
                 AppWidgetManager.INVALID_APPWIDGET_ID);
     }
  
     // If they gave us an intent without the widget id, just bail.
     if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
         finish();
     }
     
   //v. 1.6
   //change to stop phantom widgets?????
  // make the result intent and set the result to canceled
  Intent resultValue = new Intent();
  resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
  setResult(RESULT_CANCELED, resultValue);
//v. 1.6:END
  
  // Look up the AdView as a resource and load a request.
     //Log.d("netApp", "before msg");
     if (isNetworkConnected()){
    	    	  	    
    	  //ads:
            // Create an ad.
            adView = new AdView(this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(getResources().getString(R.string.ad_unit_id));

            
            adView.setAdListener(new AdListener() {
            	  @Override
            	  public void onAdLoaded() {
            	    // Save app state before going to the ad overlay.
            		  adView.setVisibility(View.VISIBLE);  
            	  }
            	});
            
            // Add the AdView to the view hierarchy. The view will have no size
            // until the ad is loaded.
            LinearLayout layout = (LinearLayout) findViewById(R.id.mainLayout);
            layout.addView(adView);
            adView.setVisibility(View.GONE);
            // Create an ad request. Check logcat output for the hashed device ID to
            // get test ads on a physical device.
            AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                
                .build();
            //.addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE")

            // Start loading the ad in the background.
            adView.loadAd(adRequest);
    	    
    	    
    	    
     }
     
     //check if chrome is installed:
     boolean installed  =   appInstalledOrNot("com.android.chrome");  
     if(installed)
     {
    	 TextView tv=(TextView) findViewById(R.id.textView2);
    	 tv.setVisibility(View.VISIBLE);
    	 
    	 RadioGroup rg=(RadioGroup) findViewById(R.id.radioGroup1);
    	 rg.setVisibility(View.VISIBLE);
     }
     
     
  
     
}

private Button.OnClickListener configOkButtonOnClickListener
= new Button.OnClickListener(){

@Override
public void onClick(View arg0) {

 final Context context = AppWidgetConfigure2x2.this;

 AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

 
 
 
 String create=getString(R.string.creating);

 Toast.makeText(context, create , Toast.LENGTH_LONG).show();
 
//check if chrome is installed:
 boolean installed  =   appInstalledOrNot("com.android.chrome");  
 if(!installed){
	 saveTitlePref(context, mAppWidgetId, "stock");
 }else{
	 
	 RadioButton button0 = (RadioButton) findViewById(R.id.radio0);
	 RadioButton button1 = (RadioButton) findViewById(R.id.radio1);
	 RadioButton button2 = (RadioButton) findViewById(R.id.radio2);
	 
	 String rbText="Android";
	 if (button0.isChecked()){
		 saveTitlePref(context, mAppWidgetId, "all");
		 rbText="All";
	 }else if (button1.isChecked()){
		 saveTitlePref(context, mAppWidgetId, "stock");
		 rbText="Android";
	 }else if (button2.isChecked()){
		 saveTitlePref(context, mAppWidgetId, "chrome");
		 rbText="Chrome";
	 }
	//for the browser name
	 saveTitlePrefTitle(context,mAppWidgetId,rbText);
 }
 
 //v. 1.3: for the background
 RadioButton buttontrans0 = (RadioButton) findViewById(R.id.radiotrans0);
 RadioButton buttontrans1 = (RadioButton) findViewById(R.id.radiotrans1);
 //v. 1.7: more transparency options
 RadioButton buttontransfull = (RadioButton) findViewById(R.id.radiotransFull);
 RadioButton buttontransnone = (RadioButton) findViewById(R.id.radiotransNone);
 
 
 int whichBackground=R.layout.layout_appwidget;
 

 if (buttontrans0.isChecked()){
	 whichBackground=R.layout.layout_appwidget_trans;
	 saveTitlePrefBack(context, mAppWidgetId, "high");
 }else if (buttontrans1.isChecked()){
	 whichBackground=R.layout.layout_appwidget;
	 saveTitlePrefBack(context, mAppWidgetId, "low");
 }else if (buttontransfull.isChecked()){//v. 1.7
	 whichBackground=R.layout.layout_appwidget_full_trans;
	 saveTitlePrefBack(context, mAppWidgetId, "full");
 }else if (buttontransnone.isChecked()){
	 whichBackground=R.layout.layout_appwidget_notrans;
	 saveTitlePrefBack(context, mAppWidgetId, "none");
 }
 
 
//v. 1.6: for the theme
RadioButton buttontheme0 = (RadioButton) findViewById(R.id.radiotheme0);
RadioButton buttontheme1 = (RadioButton) findViewById(R.id.radiotheme1);
RadioButton buttontheme2 = (RadioButton) findViewById(R.id.radiotheme2);

 if (buttontheme0.isChecked()){//dark
	 saveTitlePrefTheme(context, mAppWidgetId, "dark");
 }else if (buttontheme1.isChecked()){//light
	 saveTitlePrefTheme(context, mAppWidgetId, "light");
 }else if (buttontheme2.isChecked()){//custom//v. 1.7
	 saveTitlePrefTheme(context, mAppWidgetId, "custom");
	 saveCustomColors(context,mAppWidgetId,color1,color2,color3);
	 //save custom colors for app if checkbox is checked
	 CheckBox ch1 = (CheckBox) findViewById(R.id.checkBox1);
	 if (ch1.isChecked()){
		 saveCustomColors(context,color1,color2,color3);
	 }else{
		 saveCustomColors(context,-1,-1,-1);
	 }
 }

 
 /////////////////
 //v. 1.6: for the sort order
 RadioButton buttonsort0 = (RadioButton) findViewById(R.id.radioorderby0);
 RadioButton buttonsort1 = (RadioButton) findViewById(R.id.radioorderby1);

  if (buttonsort0.isChecked()){//desc
 	 saveTitlePrefSort(context, mAppWidgetId, "desc");
  }else if (buttonsort1.isChecked()){//date
 	 saveTitlePrefSort(context, mAppWidgetId, "date");
  }
 
 //v.1.3 update here, not in the start 
 BookmarkWidgetProvider2x2.updateAppWidget(context, appWidgetManager, mAppWidgetId);
 
//v. 1.3: for the background:END
 Intent resultValue = new Intent();
 resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
 setResult(RESULT_OK, resultValue);
 finish();
}};

//Write the prefix to the SharedPreferences object for this widget
static void saveTitlePref(Context context, int appWidgetId, String text) {
    SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
    prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
    prefs.commit();
}

// Read the prefix from the SharedPreferences object for this widget.
// If there is no preference saved, get the default from a resource
static String loadTitlePref(Context context, int appWidgetId) {
    SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
    String prefix = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
    
    if (prefix != null) {
    	//Log.d("load transparwhat", prefix);
        return prefix;
    } else {
    	//Log.d("load transparwhat", "null");
        return context.getString(R.string.appwidget_prefix_default);
    }
}

//v. 1.3: for the background
//for the background prefs
//Write the prefix to the SharedPreferences object for this widget
static void saveTitlePrefBack(Context context, int appWidgetId, String text) {
	//Log.d("save transpar", text);
  SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
  prefs.putString(PREF_PREFIX_BACK_KEY + appWidgetId, text);
  prefs.commit();
}

//Read the prefix from the SharedPreferences object for this widget.
//If there is no preference saved, get the default from a resource
static String loadTitlePrefBack(Context context, int appWidgetId) {
  SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
  String prefix = prefs.getString(PREF_PREFIX_BACK_KEY + appWidgetId, null);
  if (prefix != null) {
	  //Log.d("load transpar", prefix);
      return prefix;
  } else {
	  //Log.d("load transpar", "load null");
      return context.getString(R.string.appwidget_prefix_back_default);
  }
}

//v. 1.3: for the background
//for the title prefs
//Write the prefix to the SharedPreferences object for this widget
static void saveTitlePrefTitle(Context context, int appWidgetId, String text) {
	//Log.d("save transpar", text);
SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
prefs.putString(PREF_PREFIX_TITLE_KEY + appWidgetId, text);
prefs.commit();
}

//Read the prefix from the SharedPreferences object for this widget.
//If there is no preference saved, get the default from a resource
static String loadTitlePrefTitle(Context context, int appWidgetId) {
SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
String prefix = prefs.getString(PREF_PREFIX_TITLE_KEY + appWidgetId, null);
if (prefix != null) {
	  //Log.d("load transpar", prefix);
    return prefix;
} else {
	  //Log.d("load transpar", "load null");
    return context.getString(R.string.appwidget_prefix_title_default);
}
}

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

//v. 1.6: for the theme
//for the theme prefs
//Write the prefix to the SharedPreferences object for this widget
static void saveTitlePrefTheme(Context context, int appWidgetId, String text) {
	//Log.d("save transpar", text);
SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
prefs.putString(PREF_PREFIX_THEME_KEY + appWidgetId, text);
prefs.commit();
}

//Read the prefix from the SharedPreferences object for this widget.
//If there is no preference saved, get the default from a resource
static String loadTitlePrefTheme(Context context, int appWidgetId) {
SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
String prefix = prefs.getString(PREF_PREFIX_THEME_KEY + appWidgetId, null);
if (prefix != null) {
	  //Log.d("load transpar", prefix);
  return prefix;
} else {
	  //Log.d("load transpar", "load null");
  return context.getString(R.string.appwidget_prefix_theme_default);
}
}


//////////////////////////
//v. 1.6: for the sortorder
//for the sortorder prefs
//Write the prefix to the SharedPreferences object for this widget
static void saveTitlePrefSort(Context context, int appWidgetId, String text) {
	//Log.d("save transpar", text);
	SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
	prefs.putString(PREF_PREFIX_SORT_KEY + appWidgetId, text);
	prefs.commit();
}

//Read the prefix from the SharedPreferences object for this widget.
//If there is no preference saved, get the default from a resource
static String loadTitlePrefSort(Context context, int appWidgetId) {
	SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
	String prefix = prefs.getString(PREF_PREFIX_SORT_KEY + appWidgetId, null);
	if (prefix != null) {
		  //Log.d("load transpar", prefix);
	return prefix;
	} else {
		  //Log.d("load transpar", "load null");
	return context.getString(R.string.appwidget_prefix_sort_default);
	}
}

//v. 1.7:
static void saveCustomColors(Context context, int appWidgetId, int color1, int color2, int color3) {
	
	SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
	prefs.putInt(PREF_PREFIX_COLOR1_KEY + appWidgetId, color1);
	prefs.putInt(PREF_PREFIX_COLOR2_KEY + appWidgetId, color2);
	prefs.putInt(PREF_PREFIX_COLOR3_KEY + appWidgetId, color3);
	
	prefs.commit();
	
}

//save selected colors for the app to reuse it next time
static void saveCustomColors(Context context, int color1, int color2, int color3) {
	
	SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
	prefs.putInt(PREF_PREFIX_COLOR1_KEY , color1);
	prefs.putInt(PREF_PREFIX_COLOR2_KEY , color2);
	prefs.putInt(PREF_PREFIX_COLOR3_KEY , color3);
	
	prefs.commit();
	
}

static int loadCustomColor1(Context context, int appWidgetId) {//background
	SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
	int prefix = prefs.getInt(PREF_PREFIX_COLOR1_KEY + appWidgetId, -1);
	if (prefix != -1) {
		  //Log.d("load transpar", prefix);
	return prefix;
	} else {
		  //Log.d("load transpar", "load null");
	return Color.BLACK;
	}
}

static int loadCustomColor2(Context context, int appWidgetId) {//text
	SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
	int prefix = prefs.getInt(PREF_PREFIX_COLOR2_KEY + appWidgetId, -1);
	if (prefix != -1) {
		  //Log.d("load transpar", prefix);
	return prefix;
	} else {
		  //Log.d("load transpar", "load null");
	return Color.WHITE;
	}
}

static int loadCustomColor3(Context context, int appWidgetId) {//text links
	SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
	int prefix = prefs.getInt(PREF_PREFIX_COLOR3_KEY + appWidgetId, -1);
	if (prefix != -1) {
		  //Log.d("load transpar", prefix);
	return prefix;
	} else {
		  //Log.d("load transpar", "load null");
	return Color.parseColor("#0099CC");
	}
}


//load custom colors that have been saved for the app (not specific widget)
static int loadCustomColor1(Context context) {//background
	SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
	int prefix = prefs.getInt(PREF_PREFIX_COLOR1_KEY, -1);
	
	return prefix;
	
}

static int loadCustomColor2(Context context) {//text
	SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
	int prefix = prefs.getInt(PREF_PREFIX_COLOR2_KEY, -1);
	
	return prefix;
	
}

static int loadCustomColor3(Context context) {//text links
	SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
	int prefix = prefs.getInt(PREF_PREFIX_COLOR3_KEY, -1);
	
	return prefix;
	
}


//for custom colors textviews clicklistener
private Button.OnClickListener customTextViewsOnClickListener
= new Button.OnClickListener(){

@Override
public void onClick(View arg0) {
 
 final Context context = AppWidgetConfigure2x2.this;
 
 
 if ((arg0.getId()==R.id.textViewCustom1_1) || (arg0.getId()==R.id.textViewCustom1_2)){
	 ColorPickerDialog cpd=new ColorPickerDialog(context, color1);
	 cpd.setAlphaSliderVisible(true);
	 cpd.setOnColorChangedListener(onColorChangedListener);
	 cpd.show();
 }
 
 if ((arg0.getId()==R.id.textViewCustom2_1) || (arg0.getId()==R.id.textViewCustom2_2)){
	 ColorPickerDialog cpd=new ColorPickerDialog(context, color2);
	 cpd.setAlphaSliderVisible(true);
	 cpd.setOnColorChangedListener(onColorChangedListener2);
	 cpd.show();
 }
 
 if ((arg0.getId()==R.id.textViewCustom3_1) || (arg0.getId()==R.id.textViewCustom3_2)){
	 ColorPickerDialog cpd=new ColorPickerDialog(context, color3);
	 cpd.setAlphaSliderVisible(true);
	 cpd.setOnColorChangedListener(onColorChangedListener3);
	 cpd.show();
 }
 
}};

//radiobutton theme listener:
private OnClickListener radio_listener = new OnClickListener() {
    public void onClick(View v) {
    	RadioButton rd=(RadioButton) v;
    	if (rd.getId()==R.id.radiotheme2){//custom
    		if (rd.isChecked()){
    			TableRow tb=(TableRow) findViewById(R.id.tableRow1);
    			tb.setVisibility(View.VISIBLE);
    			
    			TextView textView1 = (TextView) findViewById(R.id.textViewCustom1_2);    			
    			textView1.setBackgroundColor(color1);
    			
    			tb=(TableRow) findViewById(R.id.tableRow2);
    			tb.setVisibility(View.VISIBLE);
    			
    			textView1 = (TextView) findViewById(R.id.textViewCustom2_2);    			
    			textView1.setBackgroundColor(color2);
    			
    			tb=(TableRow) findViewById(R.id.tableRow3);
    			tb.setVisibility(View.VISIBLE);
    			
    			textView1 = (TextView) findViewById(R.id.textViewCustom3_2);   			
    			textView1.setBackgroundColor(color3);
    			    			
    			tb=(TableRow) findViewById(R.id.tableRowPreselect);
    			tb.setVisibility(View.VISIBLE);
    			
    			//hide background transparency:
    			textView1 = (TextView) findViewById(R.id.textView1);
    			textView1.setVisibility(View.GONE);
    			
    			RadioGroup rb1 = (RadioGroup) findViewById(R.id.radioGroupTrans);
    			rb1.setVisibility(View.GONE);
    			
    			//put correct colors:
    			
    			LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
    			linLayout.setBackgroundColor(color1);
    			
    			linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
    			linLayout.setBackgroundColor(Color.parseColor("#00000000"));
    			
    			//textView1 = (TextView) findViewById(R.id.textViewPr1);
    			//textView1.setTextColor(color2);
    			
    			textView1 = (TextView) findViewById(R.id.textViewName);
    			textView1.setTextColor(color2);
    			//textView1 = (TextView) findViewById(R.id.username);
    			//textView1.setTextColor(color2);
    			
    			//textView1.setShadowLayer(6, 3, 3, Color.BLACK);
    			
    			textView1 = (TextView) findViewById(R.id.email1);
    			textView1.setTextColor(color3);
    			
    			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
    			
    			textView1 = (TextView) findViewById(R.id.email2);
    			textView1.setTextColor(color3);
    			
    			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
    		}
    	}else if (rd.getId()==R.id.radiotheme1){//light
    		if (rd.isChecked()){
    			TableRow tb=(TableRow) findViewById(R.id.tableRow1);
    			tb.setVisibility(View.GONE);
    			tb=(TableRow) findViewById(R.id.tableRow2);
    			tb.setVisibility(View.GONE);
    			tb=(TableRow) findViewById(R.id.tableRow3);
    			tb.setVisibility(View.GONE);
    			tb=(TableRow) findViewById(R.id.tableRowPreselect);
    			tb.setVisibility(View.GONE);
    			
    			//show background transparency:
    			TextView textView1 = (TextView) findViewById(R.id.textView1);
    			textView1.setVisibility(View.VISIBLE);
    			
    			RadioGroup rb1 = (RadioGroup) findViewById(R.id.radioGroupTrans);
    			rb1.setVisibility(View.VISIBLE);
    			
    			//for the preview pane:
    			//check transparency selected:
    			RadioButton button1 = (RadioButton) findViewById(R.id.radiotransFull);
    			if (button1.isChecked()){//full trans
    				//put correct colors:
        			  
        			LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
        			linLayout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        			
        			linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
        			linLayout.setBackgroundColor(Color.parseColor("#0533B5E5"));
        			
        			//textView1 = (TextView) findViewById(R.id.textViewPr1);
        			//textView1.setTextColor(Color.WHITE);
        			
        			textView1 = (TextView) findViewById(R.id.textViewName);
        			textView1.setTextColor(Color.WHITE);
        			//textView1 = (TextView) findViewById(R.id.username);
        			//textView1.setTextColor(Color.WHITE);
        			
        			//textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email1);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.WHITE);
        			
        			textView1 = (TextView) findViewById(R.id.email2);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.WHITE);
    			}
    			
    			button1 = (RadioButton) findViewById(R.id.radiotrans0);
    			if (button1.isChecked()){//high trans
    				//put correct colors:
      			  
        			LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
        			linLayout.setBackgroundColor(Color.parseColor("#19FFFFFF"));
        			
        			linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
        			linLayout.setBackgroundColor(Color.parseColor("#3033B5E5"));
        			
        			//textView1 = (TextView) findViewById(R.id.textViewPr1);
        			//textView1.setTextColor(Color.WHITE);
        			
        			textView1 = (TextView) findViewById(R.id.textViewName);
        			textView1.setTextColor(Color.WHITE);
        			//textView1 = (TextView) findViewById(R.id.username);
        			//textView1.setTextColor(Color.WHITE);
        			
        			//textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email1);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.WHITE);
        			
        			textView1 = (TextView) findViewById(R.id.email2);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.WHITE);
    				
    			}
    			button1 = (RadioButton) findViewById(R.id.radiotrans1);
    			if (button1.isChecked()){//low trans
    				//put correct colors:
        			  
        			LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
        			linLayout.setBackgroundColor(Color.parseColor("#80FFFFFF"));
        			
        			linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
        			linLayout.setBackgroundColor(Color.parseColor("#8033B5E5"));
        			
        			//textView1 = (TextView) findViewById(R.id.textViewPr1);
        			//textView1.setTextColor(Color.WHITE);
        			
        			textView1 = (TextView) findViewById(R.id.textViewName);
        			textView1.setTextColor(Color.WHITE);
        			//textView1 = (TextView) findViewById(R.id.username);
        			//textView1.setTextColor(Color.WHITE);
        			
        			//textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email1);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.WHITE);
        			

        			textView1 = (TextView) findViewById(R.id.email2);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.WHITE);
    				
    			}
    			button1 = (RadioButton) findViewById(R.id.radiotransNone);
    			if (button1.isChecked()){//none trans
    				//put correct colors:
      			  
        			LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
        			linLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        			
        			linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
        			linLayout.setBackgroundColor(Color.parseColor("#33B5E5"));
        			
        			//textView1 = (TextView) findViewById(R.id.textViewPr1);
        			//textView1.setTextColor(Color.WHITE);
        			
        			textView1 = (TextView) findViewById(R.id.textViewName);
        			textView1.setTextColor(Color.WHITE);
        			//textView1 = (TextView) findViewById(R.id.username);
        			//textView1.setTextColor(Color.parseColor("#a8a8a8"));
        			//no shadow in this case
        			//textView1.setShadowLayer(0, 0, 0, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email1);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.WHITE);
        			
        			textView1 = (TextView) findViewById(R.id.email2);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.WHITE);
    				
    			}
    			
    		}
    	}else if (rd.getId()==R.id.radiotheme0){//dark
    		if (rd.isChecked()){
    			TableRow tb=(TableRow) findViewById(R.id.tableRow1);
    			tb.setVisibility(View.GONE);
    			tb=(TableRow) findViewById(R.id.tableRow2);
    			tb.setVisibility(View.GONE);
    			tb=(TableRow) findViewById(R.id.tableRow3);
    			tb.setVisibility(View.GONE);
    			tb=(TableRow) findViewById(R.id.tableRowPreselect);
    			tb.setVisibility(View.GONE);
    			
    			//show background transparency:
    			TextView textView1 = (TextView) findViewById(R.id.textView1);
    			textView1.setVisibility(View.VISIBLE);
    			
    			RadioGroup rb1 = (RadioGroup) findViewById(R.id.radioGroupTrans);
    			rb1.setVisibility(View.VISIBLE);
    			
    			
    			//for the preview pane:
    			//check transparency selected:
    			RadioButton button1 = (RadioButton) findViewById(R.id.radiotransFull);
    			if (button1.isChecked()){//full trans
    				//put correct colors:
        			  
        			LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
        			linLayout.setBackgroundColor(Color.parseColor("#00000000"));
        			
        			linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
        			linLayout.setBackgroundColor(Color.parseColor("#00000000"));
        			
        			//textView1 = (TextView) findViewById(R.id.textViewPr1);
        			//textView1.setTextColor(Color.WHITE);
        			
        			textView1 = (TextView) findViewById(R.id.textViewName);
        			textView1.setTextColor(Color.WHITE);
        			//textView1 = (TextView) findViewById(R.id.username);
        			//textView1.setTextColor(Color.WHITE);
        			
        			//textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email1);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email2);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
    			}
    			
    			button1 = (RadioButton) findViewById(R.id.radiotrans0);
    			if (button1.isChecked()){//high trans
    				//put correct colors:
      			  
    				LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
        			linLayout.setBackgroundColor(Color.parseColor("#19000000"));
        			
        			linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
        			linLayout.setBackgroundColor(Color.parseColor("#00000000"));
        			
        			//textView1 = (TextView) findViewById(R.id.textViewPr1);
        			//textView1.setTextColor(Color.WHITE);
        			
        			textView1 = (TextView) findViewById(R.id.textViewName);
        			textView1.setTextColor(Color.WHITE);
        			//textView1 = (TextView) findViewById(R.id.username);
        			//textView1.setTextColor(Color.WHITE);
        			
        			//textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email1);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email2);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
    				
    			}
    			button1 = (RadioButton) findViewById(R.id.radiotrans1);
    			if (button1.isChecked()){//low trans
    				//put correct colors:
        			  
    				LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
        			linLayout.setBackgroundColor(Color.parseColor("#80000000"));
        			
        			linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
        			linLayout.setBackgroundColor(Color.parseColor("#00000000"));
        			
        			//textView1 = (TextView) findViewById(R.id.textViewPr1);
        			//textView1.setTextColor(Color.WHITE);
        			
        			textView1 = (TextView) findViewById(R.id.textViewName);
        			textView1.setTextColor(Color.WHITE);
        			//textView1 = (TextView) findViewById(R.id.username);
        			//textView1.setTextColor(Color.WHITE);
        			
        			//textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email1);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email2);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
    				
    			}
    			button1 = (RadioButton) findViewById(R.id.radiotransNone);
    			if (button1.isChecked()){//none trans
    				//put correct colors:
      			  
    				LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
        			linLayout.setBackgroundColor(Color.parseColor("#000000"));
        			
        			linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
        			linLayout.setBackgroundColor(Color.parseColor("#000000"));
        			
        			//textView1 = (TextView) findViewById(R.id.textViewPr1);
        			//textView1.setTextColor(Color.WHITE);
        			
        			textView1 = (TextView) findViewById(R.id.textViewName);
        			textView1.setTextColor(Color.WHITE);
        			//textView1 = (TextView) findViewById(R.id.username);
        			//textView1.setTextColor(Color.WHITE);
        			
        			//textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email1);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email2);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
    				
    			}

    			
    			
    		}
    	}


        
    	
    }
};

private ColorPickerDialog.OnColorChangedListener onColorChangedListener=new ColorPickerDialog.OnColorChangedListener(){

	@Override
	public void onColorChanged(int color) {
		TextView textView1 = (TextView) findViewById(R.id.textViewCustom1_2);
		
		textView1.setBackgroundColor(color);
		color1=color;
		
		LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
		linLayout.setBackgroundColor(color);
		
		//linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
		//linLayout.setBackgroundColor(color);
		
	}
	
};

private ColorPickerDialog.OnColorChangedListener onColorChangedListener2=new ColorPickerDialog.OnColorChangedListener(){

	@Override
	public void onColorChanged(int color) {
		TextView textView1 = (TextView) findViewById(R.id.textViewCustom2_2);
		
		textView1.setBackgroundColor(color);
		color2=color;
		
		//textView1 = (TextView) findViewById(R.id.textViewPr1);
		//textView1.setTextColor(color);
		
		textView1 = (TextView) findViewById(R.id.textViewName);
		textView1.setTextColor(color);
		//textView1 = (TextView) findViewById(R.id.username);
		//textView1.setTextColor(color);
	}
	
};

private ColorPickerDialog.OnColorChangedListener onColorChangedListener3=new ColorPickerDialog.OnColorChangedListener(){

	@Override
	public void onColorChanged(int color) {
		TextView textView1 = (TextView) findViewById(R.id.textViewCustom3_2);
		
		textView1.setBackgroundColor(color);
		color3=color;
		
		textView1 = (TextView) findViewById(R.id.email1);
		textView1.setTextColor(color);
		
		textView1 = (TextView) findViewById(R.id.email2);
		textView1.setTextColor(color);
		
	}
	
};

//radiobutton back trans listener:
private OnClickListener radio_listener_back = new OnClickListener() {

	
	@Override
	public void onClick(View arg0) {
		
		RadioButton button0 = (RadioButton) findViewById(R.id.radiotheme0);
		RadioButton button1 = (RadioButton) findViewById(R.id.radiotheme1);
		
		
		RadioButton rd=(RadioButton) arg0;
    	if (rd.getId()==R.id.radiotransFull){
    		if (rd.isChecked()){
    			if (button0.isChecked()){//dark
    				LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
        			linLayout.setBackgroundColor(Color.parseColor("#00000000"));
        			
        			linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
        			linLayout.setBackgroundColor(Color.parseColor("#00000000"));
        			
        			//TextView textView1 = (TextView) findViewById(R.id.textViewPr1);
        			//textView1.setTextColor(Color.WHITE);
        			
        			TextView textView1 = (TextView) findViewById(R.id.textViewName);
        			textView1.setTextColor(Color.WHITE);
        			//textView1 = (TextView) findViewById(R.id.username);
        			//textView1.setTextColor(Color.WHITE);
        			
        			//textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email1);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email2);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        		}
    			if (button1.isChecked()){//light
    				LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
        			linLayout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        			
        			linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
        			linLayout.setBackgroundColor(Color.parseColor("#0533B5E5"));
        			
        			//TextView textView1 = (TextView) findViewById(R.id.textViewPr1);
        			//textView1.setTextColor(Color.WHITE);
        			
        			TextView textView1 = (TextView) findViewById(R.id.textViewName);
        			textView1.setTextColor(Color.WHITE);
        			//textView1 = (TextView) findViewById(R.id.username);
        			//textView1.setTextColor(Color.WHITE);
        			
        			//textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email1);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.WHITE);
        			
        			textView1 = (TextView) findViewById(R.id.email2);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.WHITE);
        		}
    		}
    	}
    	
    	if (rd.getId()==R.id.radiotrans0){//high
    		if (rd.isChecked()){
    			if (button0.isChecked()){//dark
    				LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
        			linLayout.setBackgroundColor(Color.parseColor("#19000000"));
        			
        			linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
        			linLayout.setBackgroundColor(Color.parseColor("#00000000"));
        			
        			//TextView textView1 = (TextView) findViewById(R.id.textViewPr1);
        			//textView1.setTextColor(Color.WHITE);
        			
        			TextView textView1 = (TextView) findViewById(R.id.textViewName);
        			textView1.setTextColor(Color.WHITE);
        			//textView1 = (TextView) findViewById(R.id.username);
        			//textView1.setTextColor(Color.WHITE);
        			
        			//textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email1);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email2);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        		}
    			if (button1.isChecked()){//light
    				LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
        			linLayout.setBackgroundColor(Color.parseColor("#19FFFFFF"));
        			
        			linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
        			linLayout.setBackgroundColor(Color.parseColor("#3033B5E5"));
        			
        			//TextView textView1 = (TextView) findViewById(R.id.textViewPr1);
        			//textView1.setTextColor(Color.WHITE);
        			
        			TextView textView1 = (TextView) findViewById(R.id.textViewName);
        			textView1.setTextColor(Color.WHITE);
        			//textView1 = (TextView) findViewById(R.id.username);
        			//textView1.setTextColor(Color.WHITE);
        			
        			//textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email1);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.WHITE);
        			
        			textView1 = (TextView) findViewById(R.id.email2);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.WHITE);
        		}
    		}
    	}
    	
    	if (rd.getId()==R.id.radiotrans1){//low
    		if (rd.isChecked()){
    			if (button0.isChecked()){//dark
    				LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
        			linLayout.setBackgroundColor(Color.parseColor("#80000000"));
        			
        			linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
        			linLayout.setBackgroundColor(Color.parseColor("#00000000"));
        			
        			//TextView textView1 = (TextView) findViewById(R.id.textViewPr1);
        			//textView1.setTextColor(Color.WHITE);
        			
        			TextView textView1 = (TextView) findViewById(R.id.textViewName);
        			textView1.setTextColor(Color.WHITE);
        			//textView1 = (TextView) findViewById(R.id.username);
        			//textView1.setTextColor(Color.WHITE);
        			
        			//textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email1);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email2);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        		}
    			if (button1.isChecked()){//light
    				LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
        			linLayout.setBackgroundColor(Color.parseColor("#80FFFFFF"));
        			
        			linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
        			linLayout.setBackgroundColor(Color.parseColor("#8033B5E5"));
        			
        			//TextView textView1 = (TextView) findViewById(R.id.textViewPr1);
        			//textView1.setTextColor(Color.WHITE);
        			
        			TextView textView1 = (TextView) findViewById(R.id.textViewName);
        			textView1.setTextColor(Color.WHITE);
        			//textView1 = (TextView) findViewById(R.id.username);
        			//textView1.setTextColor(Color.WHITE);
        			
        			//textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email1);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.WHITE);
        			
        			textView1 = (TextView) findViewById(R.id.email2);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.WHITE);
        		}
    		}
    	}
    	
    	if (rd.getId()==R.id.radiotransNone){
    		if (rd.isChecked()){
    			if (button0.isChecked()){//dark
    				LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
        			linLayout.setBackgroundColor(Color.parseColor("#000000"));
        			
        			linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
        			linLayout.setBackgroundColor(Color.parseColor("#000000"));
        			
        			//TextView textView1 = (TextView) findViewById(R.id.textViewPr1);
        			//textView1.setTextColor(Color.WHITE);
        			
        			TextView textView1 = (TextView) findViewById(R.id.textViewName);
        			textView1.setTextColor(Color.WHITE);
        			//textView1 = (TextView) findViewById(R.id.username);
        			//textView1.setTextColor(Color.WHITE);
        			
        			//textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email1);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email2);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.BLACK);
        		}
    			if (button1.isChecked()){//light
    				LinearLayout linLayout = (LinearLayout) findViewById(R.id.previewView);
        			linLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        			
        			linLayout = (LinearLayout) findViewById(R.id.innerPreviewView);
        			linLayout.setBackgroundColor(Color.parseColor("#33B5E5"));
        			
        			//TextView textView1 = (TextView) findViewById(R.id.textViewPr1);
        			//textView1.setTextColor(Color.WHITE);
        			
        			TextView textView1 = (TextView) findViewById(R.id.textViewName);
        			textView1.setTextColor(Color.WHITE);
        			//textView1 = (TextView) findViewById(R.id.username);
        			//textView1.setTextColor(Color.parseColor("#a8a8a8"));
        			//no shadow in this case
        			//textView1.setShadowLayer(0, 0, 0, Color.BLACK);
        			
        			textView1 = (TextView) findViewById(R.id.email1);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.WHITE);
        			
        			textView1 = (TextView) findViewById(R.id.email2);
        			textView1.setTextColor(Color.parseColor("#0099CC"));
        			
        			textView1.setShadowLayer(6, 3, 3, Color.WHITE);
        		}
    		}
    	}
		
	}
	
};

@Override
public void onResume(){
	super.onResume();
	
    if (adView != null) {
    	adView.resume();
    }
  }

  @Override
  public void onPause() {
    if (adView != null) {
    	adView.pause();
    }
    super.onPause();
  }
  
  @Override
  public void onDestroy() {
    // Destroy the AdView.
    if (adView != null) {
    	adView.destroy();
    }
    super.onDestroy();
  }

}