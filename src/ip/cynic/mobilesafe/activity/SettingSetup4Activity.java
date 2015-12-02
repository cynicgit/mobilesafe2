package ip.cynic.mobilesafe.activity;

import ip.cynic.mobilesafe.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class SettingSetup4Activity extends BaseSetupActivity {


	private CheckBox cbLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		cbLock = (CheckBox) findViewById(R.id.cb_lock);
		boolean lock = mPref.getBoolean("lock", false);
		if(lock){
			cbLock.setChecked(true);
			cbLock.setText("���������ѿ���");
		}else{
			cbLock.setChecked(false);
			cbLock.setText("���������ѹر�");
		}
	}

	public void checkLock(View v){
		if(cbLock.isChecked()){
			cbLock.setText("���������ѿ���");
		}else{
			cbLock.setText("���������ѹر�");
		}
	}
	
	@Override
	public void showNextPage() {
		startActivity(new Intent(this,LostFindActivity.class));
		finish();
		mPref.edit().putBoolean("lock", cbLock.isChecked()).commit();
		
		mPref.edit().putBoolean("configed", true).commit();
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);// ���붯�����˳�����
	}

	@Override
	public void showPrevious() {
		startActivity(new Intent(this,SettingSetup3Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);// ���붯�����˳�����
	}
}
