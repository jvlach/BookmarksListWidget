package gr.jvlach.bookmarkswidget;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class OtherAppsActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.otherapps);
		
		//imageviews onclick
		ImageView i=(ImageView) findViewById(R.id.imageView1);
		i.setOnClickListener(this);
		
		i=(ImageView) findViewById(R.id.imageView2);
		i.setOnClickListener(this);
		i=(ImageView) findViewById(R.id.imageView3);
		i.setOnClickListener(this);
		i=(ImageView) findViewById(R.id.imageView4);
		i.setOnClickListener(this);
		i=(ImageView) findViewById(R.id.imageView5);
		i.setOnClickListener(this);
		i=(ImageView) findViewById(R.id.imageView6);
		i.setOnClickListener(this);
		
		i=(ImageView) findViewById(R.id.imageView7);
		i.setOnClickListener(this);
		
		
		i=(ImageView) findViewById(R.id.imageViewLivewallpaper);
		i.setOnClickListener(this);
		
		
		
		i=(ImageView) findViewById(R.id.imageViewdaydream);
		i.setOnClickListener(this);
		
		
		//button onclick
		Button b=(Button) findViewById(R.id.button1);
		b.setOnClickListener(this);
		//textviews onclick
		TextView t=(TextView) findViewById(R.id.textView1);
		t.setOnClickListener(this);
		
		t=(TextView) findViewById(R.id.textView2);
		t.setOnClickListener(this);
		t=(TextView) findViewById(R.id.textView3);
		t.setOnClickListener(this);
		t=(TextView) findViewById(R.id.textView4);
		t.setOnClickListener(this);
		t=(TextView) findViewById(R.id.textView5);
		t.setOnClickListener(this);
		t=(TextView) findViewById(R.id.textView6);
		t.setOnClickListener(this);
		t=(TextView) findViewById(R.id.textView7);
		t.setOnClickListener(this);
		t=(TextView) findViewById(R.id.textView8);
		t.setOnClickListener(this);
		t=(TextView) findViewById(R.id.textView9);
		t.setOnClickListener(this);
		t=(TextView) findViewById(R.id.textView10);
		t.setOnClickListener(this);
		t=(TextView) findViewById(R.id.textView11);
		t.setOnClickListener(this);
		t=(TextView) findViewById(R.id.textView12);
		t.setOnClickListener(this);
		
		t=(TextView) findViewById(R.id.textView13);
		t.setOnClickListener(this);
		t=(TextView) findViewById(R.id.textView14);
		t.setOnClickListener(this);
		
		
		t=(TextView) findViewById(R.id.textViewLivewallpaper1);
		t.setOnClickListener(this);
		t=(TextView) findViewById(R.id.textViewLivewallpaper2);
		t.setOnClickListener(this);
		//v.3.6:END
		
		
		t=(TextView) findViewById(R.id.textViewdaydream);
		t.setOnClickListener(this);
		t=(TextView) findViewById(R.id.textViewdaydreamdesc);
		t.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View arg0) {
		
		//square bird
		if ((arg0.getId() == R.id.imageView7) || (arg0.getId() == R.id.textView13) || (arg0.getId() == R.id.textView14)) {
			//open play store:
			final String appName = "gr.jvlach.squarebird";
	    	try {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
	    	} catch (android.content.ActivityNotFoundException anfe) {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
	    	}
		}
		
		if ((arg0.getId() == R.id.imageView1) || (arg0.getId() == R.id.textView1) || (arg0.getId() == R.id.textView2)) {
			//open play store:
			final String appName = "gr.jvlach.kidsplayint";
	    	try {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
	    	} catch (android.content.ActivityNotFoundException anfe) {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
	    	}
		}
		
		if ((arg0.getId() == R.id.imageView2) || (arg0.getId() == R.id.textView3) || (arg0.getId() == R.id.textView4)) {
			//open play store:
			final String appName = "gr.jvlach.learnenglish";
	    	try {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
	    	} catch (android.content.ActivityNotFoundException anfe) {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
	    	}
		}
		
		if ((arg0.getId() == R.id.imageView3) || (arg0.getId() == R.id.textView5) || (arg0.getId() == R.id.textView6)) {
			//open play store:
			final String appName = "gr.jvlach.memorygame";
	    	try {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
	    	} catch (android.content.ActivityNotFoundException anfe) {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
	    	}
		}
		
		if ((arg0.getId() == R.id.imageView4) || (arg0.getId() == R.id.textView7) || (arg0.getId() == R.id.textView8)) {
			//open play store:
			final String appName = "gr.jvlach.favoritecontactswidget";
	    	try {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
	    	} catch (android.content.ActivityNotFoundException anfe) {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
	    	}
		}
		
		if ((arg0.getId() == R.id.imageView5) || (arg0.getId() == R.id.textView9) || (arg0.getId() == R.id.textView10)) {
			//open play store:
			final String appName = "gr.jvlach.kidsplay";
	    	try {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
	    	} catch (android.content.ActivityNotFoundException anfe) {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
	    	}
		}
		
		if ((arg0.getId() == R.id.imageView6) || (arg0.getId() == R.id.textView11) || (arg0.getId() == R.id.textView12)) {
			//open play store:
			final String appName = "gr.jvlach.bestguitarists";
	    	try {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
	    	} catch (android.content.ActivityNotFoundException anfe) {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
	    	}
		}
		
		
		if ((arg0.getId() == R.id.imageViewLivewallpaper) || (arg0.getId() == R.id.textViewLivewallpaper1) || (arg0.getId() == R.id.textViewLivewallpaper2)) {
			//open play store:
			final String appName = "gr.jvlach.livewallpaper";
	    	try {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
	    	} catch (android.content.ActivityNotFoundException anfe) {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
	    	}
		}
		

		
		if ((arg0.getId() == R.id.imageViewdaydream) || (arg0.getId() == R.id.textViewdaydream) || (arg0.getId() == R.id.textViewdaydreamdesc)) {
			//open play store:
			final String appName = "gr.jvlach.sillyfishdaydream";
	    	try {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
	    	} catch (android.content.ActivityNotFoundException anfe) {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
	    	}
		}
		
		
		
		//exit button
		if (arg0.getId() == R.id.button1) {
			OtherAppsActivity.this.finish();
		}
	}

}
