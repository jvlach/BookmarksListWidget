package gr.jvlach.bookmarkswidget;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.text.util.Linkify.TransformFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appszoom.appszoomsdk.AppsZoom;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.ironsource.mobilcore.AdUnitEventListener;
import com.ironsource.mobilcore.MobileCore;
import com.ironsource.mobilcore.MobileCore.AD_UNITS;
import com.ironsource.mobilcore.MobileCore.EStickeezPosition;
import com.ironsource.mobilcore.MobileCore.LOG_TYPE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class HelpActivity extends Activity {
	//v. 2.1:mobilecore
	private Activity mActivity;
		
	//appzoom:
	boolean	appzoomAdShown=false;
	
	//v. 2.0:
	//Animation animScale;
	
	private AdView adView;
	private InterstitialAd interstitial;//v. 1.8
	private static final String PREFS_NAME
    = "gr.jvlach.bookmarkswidget.BookmarkWidgetProvider";//v. 1.8
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
		super.onCreate(savedInstanceState);
		//v. 2.0:
		//startApp
		//StartAppSDK.init(this, "112766587", "203218064", false);
				
		
		//startApp splash 
		//StartAppAd.showSplash(this, savedInstanceState);
				
				
		setContentView(R.layout.activity_help);
		
		//appszoom:
		AppsZoom.start(this);
		appzoomAdShown=false;
		
		AppsZoom.fetchAd("MY_TAG_NAME");
		
		//mobilecore new
		MobileCore.setAdUnitEventListener(new AdUnitEventListener() {
            @Override
            public void onAdUnitEvent(MobileCore.AD_UNITS adUnit, EVENT_TYPE eventType,
                                      MobileCore.AD_UNIT_TRIGGER... trigger) {
            	
            	
            	
            	
                if(adUnit == MobileCore.AD_UNITS.STICKEEZ &&
                   eventType == AdUnitEventListener.EVENT_TYPE.AD_UNIT_INIT_SUCCEEDED) {
                   MobileCore.loadAdUnit(MobileCore.AD_UNITS.STICKEEZ,
                                         MobileCore.AD_UNIT_TRIGGER.GAME_LEVEL_END_WIN);
                }
                else if(adUnit == MobileCore.AD_UNITS.STICKEEZ &&
                        eventType == AdUnitEventListener.EVENT_TYPE.AD_UNIT_READY) {
                  for(MobileCore.AD_UNIT_TRIGGER myTrigger:trigger){
                      if(myTrigger.equals(MobileCore.AD_UNIT_TRIGGER.GAME_LEVEL_END_WIN)){
                         MobileCore.showStickee(HelpActivity.this,
                                      MobileCore.AD_UNIT_TRIGGER.GAME_LEVEL_END_WIN);
                      }
                  }
                }
                else if(adUnit == MobileCore.AD_UNITS.STICKEEZ &&
                        eventType == AdUnitEventListener.EVENT_TYPE.AD_UNIT_DISMISSED) {
                    for(MobileCore.AD_UNIT_TRIGGER myTrigger:trigger){
                        if(myTrigger.equals(MobileCore.AD_UNIT_TRIGGER.GAME_LEVEL_END_WIN)){
                           MobileCore.loadAdUnit(MobileCore.AD_UNITS.STICKEEZ,
                           MobileCore.AD_UNIT_TRIGGER.MAIN_MENU);
                        }
                    }
            }
            }
		});

		
		//v 2.1:mobilecore ads
		MobileCore.init(this,"GJWT4FBN5V4RA2UEYC6URZQ4RA2B", LOG_TYPE.DEBUG,AD_UNITS.STICKEEZ); 
		mActivity = this;
		
		
		//startApp slider
		//StartAppAd.showSlider(this);
				
		//v. 2.0:
		//animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
		
		
		Button button = (Button)findViewById(R.id.buttonclose);
        button.setOnClickListener(mainClickListener);
        
        button = (Button)findViewById(R.id.buttonrate);
        button.setOnClickListener(mainClickListener);
        
        button = (Button)findViewById(R.id.buttonother);
        button.setOnClickListener(mainClickListener);
		
        
     // Look up the AdView as a resource and load a request.
        //Log.d("netApp", "before msg");
        if (isNetworkConnected()){
        	//ads:
            // Create an ad.
            adView = new AdView(this);
            adView.setAdSize(AdSize.SMART_BANNER);
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
            
            

            // Create the interstitial.
       	    interstitial = new InterstitialAd(this);
       	    interstitial.setAdUnitId(getResources().getString(R.string.ad_unit_id_inter));
       	    
               SharedPreferences settings=getSharedPreferences(PREFS_NAME, 0);
           	int shown=settings.getInt("shown", 0);
           	//Log.d("shown",""+shown);
          //v. 2.0:
           	loadInterstitial();
           	
           	SharedPreferences.Editor editor = settings.edit();
           	if (shown>0){//load ad
           		
           		//loadInterstitial();
           		editor.putInt("shown", 0);
           		
           	}else{
           		shown++;
           		
                   editor.putInt("shown", shown);
                   
           	}
           	editor.commit();
           	
           	//displayInterstitial();
            
          //v. 2.1:mobilecore new
          //setAdUnitsEventListener();
        }
        
        
      //v.2.3: privacy policy
        TransformFilter myTransformFilter = new TransformFilter() {
        	@Override
        	public String transformUrl(Matcher match, String url) {
        		return url.replace("press here.", ""); //remove the press here.
        	}
        };
        
        TransformFilter myTransformFilterEl = new TransformFilter() {//greek
        	@Override
        	public String transformUrl(Matcher match, String url) {
        		return url.replace("������� ���.", ""); //remove the press here.
        	}
        };
        
        TextView privacyPolicy = (TextView) findViewById(R.id.textViewPrivacy);
        
        String policyMessage=getResources().getString(R.string.privacy_policy);
               
        Pattern pattern = Pattern.compile("press here.");
        
        Pattern patternEl = Pattern.compile("������� ���.");
        
        if (policyMessage.startsWith("�� ")){//greek
        	Linkify.addLinks(privacyPolicy,patternEl, "http://jv-mobile.blogspot.gr/p/privacy-policy.html", null, myTransformFilterEl);
        }else{
        	Linkify.addLinks(privacyPolicy,pattern, "http://jv-mobile.blogspot.gr/p/privacy-policy.html", null, myTransformFilter);
        }
        
        
        //test for samsung
       /* String sel = Browser.BookmarkColumns.BOOKMARK + " = 1"; // 0 = history, 1 = bookmark
        String orderby="";//needs to be put in chrome as well
		
		orderby="MODIFIED" + " DESC";
		
        final Uri uri = Uri.parse("content://com.sec.android.app.sbrowser/bookmarks");
        final Cursor c = getContentResolver().query(uri, null, null, null, orderby);

        String[] args=c.getColumnNames();
    	for (int i=0;i<args.length;i++ ){
    		Log.e("bookmarksinalist", args[i]);
    	}
        
        
        if (c!=null && c.moveToFirst()) {
        	
        	args=c.getColumnNames();
        	for (int i=0;i<args.length;i++ ){
        		Log.e("bookmarksinalist", args[i]);
        	}
        	Log.e("bookmarksinalist", "count="+c.getCount());
        	
            do {
            	try{
            	
                Log.e("bookmarksinalist","URL "+ c.getString(c.getColumnIndex("URL")));
                Log.e("bookmarksinalist", "TITLE " +c.getString(c.getColumnIndex("TITLE")));
                Log.e("bookmarksinalist","sURL " + c.getString(c.getColumnIndex("sURL")));
                Log.e("bookmarksinalist","modified " + c.getString(c.getColumnIndex("MODIFIED")));
            	}catch(Exception e){
            		Log.e("bookmarksinalist", "exception");
            	}
            	
            	
            } while(c.moveToNext());

        }*/
        
        
      //to show the exit message again in case
        final SharedPreferences settings =
 		       getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
		   if (settings.getBoolean("isExitShown", false)) {
			//////sweetalert
							SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
			                .setTitleText((String) getResources().getText(R.string.exit_title))
			                .setContentText((String) getResources().getText(R.string.exit_message))                
			                .setConfirmText((String) getResources().getText(R.string.yes))
			                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			                @Override
			                public void onClick(SweetAlertDialog sDialog) {
			                	settings.edit().putBoolean("isExitShown", false).commit();
			             	  sDialog.dismiss();
			             	  finish();
			                }
			                })
			                .setCancelText((String) getResources().getText(R.string.no))
			                .showCancelButton(true)
			                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
			                    @Override
			                    public void onClick(SweetAlertDialog sDialog) {
			                    	settings.edit().putBoolean("isExitShown", false).commit();
			                    	sDialog.dismiss();
			                    	try{
			                    		loadInterstitial();
			                    	}catch(Exception e){
			                    		e.printStackTrace();
			                    	}
			                      
			                    }
			                });
			    			pDialog.setCancelable(false);
			    			pDialog.show();	
		    
		   }
        
        
        
	}

	
	
	private OnClickListener mainClickListener = new OnClickListener() {
	    public void onClick(View v) {
	    	
	    	//displayInterstitial();
	    	
	    	if (v.getId()==R.id.buttonclose){
	    		//v.2.0:
	    		 /*Animation animScale = AnimationUtils.loadAnimation(HelpActivity.this, R.anim.anim_scale);
	    		 animScale.setAnimationListener(animationListener);
	    		v.startAnimation(animScale);*/
	    		
	    		//displayInterstitial();
	    		//v.2.4:
	    		//HelpActivity.this.finish();
	    		onBackPressed();
	    	}
	    	
	    	if (v.getId()==R.id.buttonrate){
	    		//appszoom
	    		
	    		if (AppsZoom.isAdAvailable("MY_TAG_NAME") && !appzoomAdShown){
	    			appzoomAdShown=true;
	    			AppsZoom.showAd(HelpActivity.this,"MY_TAG_NAME");
	    		}
	    		
	    		//open play store:
		    	final String appName = "gr.jvlach.bookmarkswidget";
		    	try {
		    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
		    	} catch (android.content.ActivityNotFoundException anfe) {
		    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
		    	}
	    	}
	    	
	    	if (v.getId()==R.id.buttonother){
	    		//v.2.0:
	    		//Animation animScale = AnimationUtils.loadAnimation(HelpActivity.this, R.anim.anim_scale);
	    		// animScale.setAnimationListener(animationListener);
	    		//v.startAnimation(animScale);
	    		
	    		//appszoom
	    		if (AppsZoom.isAdAvailable("MY_TAG_NAME") && !appzoomAdShown) {
	    			appzoomAdShown=true;
	    			AppsZoom.showAd(HelpActivity.this,"MY_TAG_NAME");
	    		}
	    		
	    		Intent intent = new Intent(getApplicationContext(), OtherAppsActivity.class);
		    	startActivity(intent);
	    	}
	    	

	    }
	};
	
	@Override
	public void onResume(){
		super.onResume();
		
	    if (adView != null) {
	    	adView.resume();
	    }
	    //v. 2.1:mobilecore ads
	    //MobileCore.setStickeezPositionBelowView(mActivity, R.id.play_button);
	    MobileCore.setStickeezPosition(EStickeezPosition.MIDDLE_LEFT);
	  }

	  @Override
	  public void onPause() {
	    if (adView != null) {
	    	adView.pause();
	    }
	    //v. 2.4:
	    /*if (isFinishing()){
	    	displayInterstitial();
	    }*/
	    super.onPause();
	    if (MobileCore.isStickeeShowing() || MobileCore.isStickeeShowingOffers()) {
			//This method will hide the Stickeez element.
			MobileCore.hideStickee();
		}
	  }
	  
	  @Override
	  public void onDestroy() {
	    // Destroy the AdView.
	    if (adView != null) {
	    	adView.destroy();
	    }
	    super.onDestroy();
	  }
	  
	  
	//v. 2.4:
		public void loadInterstitial() {
		    
			if (!isNetworkConnected()){
				return;
			}
			
		    // Check the logcat output for your hashed device ID to get test ads on a physical device.
		    AdRequest adRequest = new AdRequest.Builder()
		        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		        .build();
		    	//.addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE")
		    // Load the interstitial ad.
		    interstitial.loadAd(adRequest);
		    //Log.d("interstitial","interstitial loaded");
		  }
		
		
		// Invoke displayInterstitial() when you are ready to display an interstitial.
		 public void displayInterstitial() {
			 //v. 2.2
			 if (!isNetworkConnected()){
					return;
				}
			 
		    if (interstitial.isLoaded()) {
		      interstitial.show();
		      Log.d("interstitial","interstitial show");
		    }else{
		    	Log.d("interstitial","interstitial not shown");
		    }
		    
		  }
		 
		 
		 //v.2.0:
		/* AnimationListener animationListener = new AnimationListener(){

			  @Override
			  public void onAnimationStart(Animation animation) {
			   // TODO Auto-generated method stub
			   
			  }

			  @Override
			  public void onAnimationEnd(Animation animation) {
				  
				  displayInterstitial();
				  HelpActivity.this.finish();
			  }

			  @Override
			  public void onAnimationRepeat(Animation animation) {
			   // TODO Auto-generated method stub
			   
			  }};*/
		 
		//v. 2.1: mobilecore new
		 /*private void setAdUnitsEventListener() {
				MobileCore.setAdUnitEventListener(new AdUnitEventListener() {

					@Override
					public void onAdUnitEvent(AD_UNITS adUnit, EVENT_TYPE eventType) {

						switch (adUnit) {
						
						case STICKEEZ:
							
							 * Once Stickeez is ready, calling this method will show the Stickeez on top of the host application. 
							 * If the resources are not ready, nothing will happen
							 
							if (EVENT_TYPE.AD_UNIT_READY == eventType) {
								MobileCore.showStickee(mActivity);
							}
							break;
						}

					}
				});

			}*/

		 //v. 2.4:
		 @Override
		 public void onBackPressed() {
			 displayInterstitial();
			 final SharedPreferences settings =
				       getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
			 //Log.e("livewallpaper","back pressed");
			 settings.edit().putBoolean("isExitShown", true).commit();
		//////sweetalert
				SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText((String) getResources().getText(R.string.exit_title))
                .setContentText((String) getResources().getText(R.string.exit_message))                
                .setConfirmText((String) getResources().getText(R.string.yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                	settings.edit().putBoolean("isExitShown", false).commit();
             	  sDialog.dismiss();
             	  finish();
                }
                })
                .setCancelText((String) getResources().getText(R.string.no))
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                    	settings.edit().putBoolean("isExitShown", false).commit();
                    	sDialog.dismiss();
                    	try{
                    		loadInterstitial();
                    	}catch(Exception e){
                    		e.printStackTrace();
                    	}
                      
                    }
                });
    			pDialog.setCancelable(false);
    			pDialog.show();	
		 }
	

}
