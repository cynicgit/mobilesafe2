package ip.cynic.mobilesafe.activity;

import ip.cynic.mobilesafe.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {

	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lost_find);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		TextView tvSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
		ImageView ivLock = (ImageView) findViewById(R.id.iv_lock);
		boolean b = mPref.getBoolean("lock", false);
		if(b){
			ivLock.setImageResource(R.drawable.lock);
		} else {
			ivLock.setImageResource(R.drawable.unlock);
		}
		
		if(!"".equals(mPref.getString("phone", ""))){
			tvSafePhone.setText(mPref.getString("phone", ""));
		}
		
		
		//如果没有配置过则进入向导页
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		boolean configed = mPref.getBoolean("configed", false);
		if(!configed){
			startActivity(new Intent(this, SettingSetup1Activity.class));
		}
	}
	
	public void reEnter(View v){
		startActivity(new Intent(this, SettingSetup1Activity.class));
		finish();
	}
	
}
