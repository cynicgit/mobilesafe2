package ip.cynic.mobilesafe.splash;

import ip.cynic.mobile.utils.StreamUtils;
import ip.cynic.mobilesafe.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	
	protected static final int CODE_UPLOAD_DIALOG = 0;
	protected static final int CODE_ENTRY_HOME = 1;
	protected static final int CODE_URL_ERROR = 2;
	protected static final int CODE_NET_ERROR = 3;
	protected static final int CODE_JSON_ERROR = 4;
	private String versionName;
	private int versionCode;
	private String msg;
	private String downUrl;

	private TextView tvVersion;
	
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case CODE_UPLOAD_DIALOG:
					showDialog();
					break;
				case CODE_ENTRY_HOME:
					enterHome();			
					break;
				case CODE_URL_ERROR:
					Toast.makeText(SplashActivity.this, "路径错误", Toast.LENGTH_SHORT).show();
					enterHome();	
					break;
				case CODE_NET_ERROR:
					Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
					enterHome();	
					break;
				case CODE_JSON_ERROR:
					Toast.makeText(SplashActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
					enterHome();	
					break;
	
				default:
					break;
			}
		}
	};
	private ProgressDialog mypDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("版本名:"+getVersion());
		checkVersion();
		
	}
	
	
	/**
	 * 从服务器端获取信息，是否升级
	 */
	private void checkVersion(){
		Thread t = new Thread(){
			long start = System.currentTimeMillis();
			private HttpURLConnection conn;

			public void run() {
				String path = "http://192.168.1.12:8080/upload.json";
				Message message = new Message();
				try {
					URL url = new URL(path);
					conn = (HttpURLConnection) url.openConnection();
					//设置请求方法
					conn.setRequestMethod("GET");
					//设置超时时间与响应时间
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					if(conn.getResponseCode()==200){
						InputStream in = conn.getInputStream();
						String json = StreamUtils.streamToString(in);
						System.out.println(json);
						JSONObject jo = new JSONObject(json);
						versionName = jo.getString("versionName");
						versionCode = jo.getInt("versionCode");
						msg = jo.getString("message");
						downUrl = jo.getString("downUrl");
						
						//判断当前应用版本与服务器版本号的大小
						if(versionCode>getVersionCode()){//升级
							message.what = CODE_UPLOAD_DIALOG;
						}else{//跳的主页面
							message.what = CODE_ENTRY_HOME;
						}
					}
					
				} catch (MalformedURLException e) {
					// 路径异常
					message.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// 网络异常
					message.what = CODE_NET_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// json数据异常
					message.what = CODE_JSON_ERROR;
					e.printStackTrace();
				}finally{
					long end = System.currentTimeMillis();
					if(start - end<2000){
						try {
							sleep(2000-(start - end));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					handler.sendMessage(message);
					if(conn!=null){
						conn.disconnect();
					}
					
				}
				
			};
		};
		t.start();
	}
	
	
	private void showDialog(){
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("软件更新");
		builder.setMessage(msg);
		builder.setPositiveButton("更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				down();
			}
		});
		
		builder.setNegativeButton("以后再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});
		
		builder.show();
	}
	
	private void down(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			String path = Environment.getExternalStorageDirectory()+"/mobilesafe.apk";
			HttpUtils http = new HttpUtils();
			showProgessBar();
			http.download(downUrl, path, new RequestCallBack<File>() {
				
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					System.out.println(total+":"+current);
					mypDialog.setMax((int)total);
					mypDialog.setProgress((int)current);
					
				}
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					Toast.makeText(SplashActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, arg1, Toast.LENGTH_SHORT).show();
				}
			});
		}else{
			Toast.makeText(SplashActivity.this, "SD卡未就绪", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void showProgessBar(){
		mypDialog = new ProgressDialog(this);
		//实例化
		mypDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		//设置进度条风格，风格为长形，有刻度的
		mypDialog.setTitle("手机卫士");
		
		//设置ProgressDialog 标题
		//mypDialog.setMessage(getResources().getString(R.string.second));
		//设置ProgressDialog 提示信息
		mypDialog.setIcon(R.drawable.ic_launcher);
		//设置ProgressDialog 标题图标
		//mypDialog.setProgress(progress);
		//设置ProgressDialog 进度条进度
		//mypDialog.setButton(whichButton, text, listener);
		//设置ProgressDialog 的一个Button
		mypDialog.setIndeterminate(false);
		//设置ProgressDialog 的进度条是否不明确
		mypDialog.setCancelable(true);
		//设置ProgressDialog 是否可以按退回按键取消
		mypDialog.show();
		//让ProgressDialog显示
	}
	
	/**
	 * 进入主页面
	 */
	private void enterHome(){
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	/**
	 * 获取版本号
	 * @return
	 */
	private int getVersionCode(){
		//包管理器
		PackageManager manager = getPackageManager();
		try {
			//包信息
			PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
			//获取版本号 版本名
			int code = packageInfo.versionCode;
			return code;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 获取版本名
	 * @return
	 */
	private String getVersion(){
		//包管理器
		PackageManager manager = getPackageManager();
		try {
			//包信息
			PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
			//获取版本号 版本名
			int code = packageInfo.versionCode;
			String name = packageInfo.versionName;
			System.out.println(code+"  " +name);
			return name;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
}
