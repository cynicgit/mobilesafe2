package ip.cynic.mobilesafe.activity;

import ip.cynic.mobiesafe.view.SettingItemView;
import ip.cynic.mobilesafe.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity{

	private SettingItemView siv;
	private Editor edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		siv = (SettingItemView) findViewById(R.id.siv_update);
		SharedPreferences mPref = getSharedPreferences("config", MODE_PRIVATE);
		edit = mPref.edit();
		boolean b = mPref.getBoolean("auto_update", true);
		if(b){
			siv.setCheck(true);
			//siv.setDesc("自动更新已开启");
		}else{
			siv.setCheck(false);
			//siv.setDesc("自动更新已关闭");
		}
		siv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(siv.isChecked()){
					edit.putBoolean("auto_update", false);
					siv.setCheck(false);
					//siv.setDesc("自动更新已关闭");
				}else{
					edit.putBoolean("auto_update", true);
					siv.setCheck(true);
					//siv.setDesc("自动更新已开启");
				}
				edit.commit();
			}
		});
	}
}
