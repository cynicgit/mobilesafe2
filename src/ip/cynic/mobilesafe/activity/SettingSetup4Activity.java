package ip.cynic.mobilesafe.activity;

import ip.cynic.mobilesafe.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class SettingSetup4Activity extends BaseSetupActivity {

	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
	}

	@Override
	public void showNextPage() {
		startActivity(new Intent(this,LostFindActivity.class));
		finish();
		mPref.edit().putBoolean("configed", true).commit();
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);// 进入动画和退出动画
	}

	@Override
	public void showPrevious() {
		startActivity(new Intent(this,SettingSetup3Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);// 进入动画和退出动画
	}
}
