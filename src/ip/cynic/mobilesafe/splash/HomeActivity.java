package ip.cynic.mobilesafe.splash;


import ip.cynic.mobilesafe.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;



public class HomeActivity extends Activity {

	
	private String menuItem[] = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
	private int menuPirc[] = { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings};  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		GridView gridView = (GridView) findViewById(R.id.gv_menu);
		gridView.setAdapter(new HomeAdapter());
		gridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 8:
					Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}
			}
			
		});
		
	}

	class HomeAdapter extends BaseAdapter{

		

		@Override
		public int getCount() {
			return menuItem.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = null;
			if(convertView==null){
				v = View.inflate(HomeActivity.this, R.layout.activity_home_item, null);
			}else{
				v = convertView;
			}
			System.out.println(v);
			ImageView iv = (ImageView) v.findViewById(R.id.iv_item);
			iv.setImageResource(menuPirc[position]);
			TextView tv= (TextView) v.findViewById(R.id.tv_item);
			tv.setText(menuItem[position]);
			return v;
		}
		
	}
}
