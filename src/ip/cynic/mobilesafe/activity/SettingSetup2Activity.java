package ip.cynic.mobilesafe.activity;

import ip.cynic.mobiesafe.view.SettingItemView;
import ip.cynic.mobile.utils.ToastUtil;
import ip.cynic.mobilesafe.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingSetup2Activity extends BaseSetupActivity {

	private SettingItemView siv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		siv = (SettingItemView) findViewById(R.id.siv_sim);
		boolean bind = mPref.getBoolean("bind_sim", false);
		siv.setCheck(bind);
	}

	@Override
	public void showNextPage() {
		if(!siv.isChecked()){
			ToastUtil.showToasr(this, "必须绑定sim卡");
			return ;
		}
		startActivity(new Intent(this,SettingSetup3Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);// 进入动画和退出动画
	}

	@Override
	public void showPrevious() {
		startActivity(new Intent(this,SettingSetup1Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);// 进入动画和退出动画
	}
	
	public void bindSim(View v){
		if(siv.isChecked()){
			mPref.edit().putBoolean("bind_sim", true);
			siv.setCheck(true);
		}else{
			mPref.edit().putBoolean("bind_sim", false);
			siv.setCheck(false);
		}
	}
}
