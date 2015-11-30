package ip.cynic.mobilesafe.splash;

import ip.cynic.mobilesafe.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingSetup2Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
	}
	
	public void next(View v){
		startActivity(new Intent(this,SettingSetup3Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);// 进入动画和退出动画
	}
	
	public void previous(View v){
		startActivity(new Intent(this,SettingSetup1Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);// 进入动画和退出动画
	}
}
