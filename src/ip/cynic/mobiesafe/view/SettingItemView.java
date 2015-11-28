package ip.cynic.mobiesafe.view;

import ip.cynic.mobilesafe.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout{

	private CheckBox cbStatus;
	private TextView tv_desc;
	private TextView tv_title;

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SettingItemView(Context context) {
		super(context);
		init();
	}
	
	
	public void init(){
		View.inflate(getContext(), R.layout.activity_setting_item, this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		cbStatus = (CheckBox) findViewById(R.id.cb_status);
	}
	
	public void setTitle(String text){
		tv_title.setText(text);
	}
	
	public void setDesc(String desc){
		tv_desc.setText(desc);
	}
	
	public boolean isChecked(){
		return cbStatus.isChecked();
	}
	
	public void setCheck(boolean check){
		cbStatus.setChecked(check);
	}
	
}
