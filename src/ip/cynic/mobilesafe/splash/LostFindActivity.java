package ip.cynic.mobilesafe.splash;

import ip.cynic.mobilesafe.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class LostFindActivity extends Activity {

	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lost_find);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		boolean b = mPref.getBoolean("configed", false);
		if(!b){
			startActivity(new Intent(this, SettingSetup1Activity.class));
		}
	}
	
	public void reEnter(View v){
		startActivity(new Intent(this, SettingSetup1Activity.class));
		mPref.edit().putBoolean("configed", false).commit();
		finish();
	}
	
}
