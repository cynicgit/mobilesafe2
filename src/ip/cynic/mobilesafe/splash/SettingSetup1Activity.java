package ip.cynic.mobilesafe.splash;

import ip.cynic.mobilesafe.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingSetup1Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}

	public void next(View v) {
		startActivity(new Intent(this, SettingSetup2Activity.class));
		finish();
		// ���������л��Ķ���
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);// ���붯�����˳�����
	}
}
