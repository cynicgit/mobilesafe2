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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

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
					Toast.makeText(SplashActivity.this, "·������", Toast.LENGTH_SHORT).show();
					enterHome();	
					break;
				case CODE_NET_ERROR:
					Toast.makeText(SplashActivity.this, "����������", Toast.LENGTH_SHORT).show();
					enterHome();	
					break;
				case CODE_JSON_ERROR:
					Toast.makeText(SplashActivity.this, "���ݴ���", Toast.LENGTH_SHORT).show();
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
		tvVersion.setText("�汾��:"+getVersion());
		boolean b = getSharedPreferences("config", MODE_PRIVATE).getBoolean("auto_update", true);
		if(b){
			checkVersion();
		}else{
			handler.sendEmptyMessageDelayed(CODE_ENTRY_HOME, 2000);
		}
		
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_splash);
		//���ý��䶯�� 2s
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1f);
		alphaAnimation.setDuration(2000);
		rl.setAnimation(alphaAnimation);
		
	}
	
	
	/**
	 * �ӷ������˻�ȡ��Ϣ���Ƿ�����
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
					//�������󷽷�
					conn.setRequestMethod("GET");
					//���ó�ʱʱ������Ӧʱ��
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
						
						//�жϵ�ǰӦ�ð汾��������汾�ŵĴ�С
						if(versionCode>getVersionCode()){//����
							message.what = CODE_UPLOAD_DIALOG;
						}else{//������ҳ��
							message.what = CODE_ENTRY_HOME;
						}
					}
					
				} catch (MalformedURLException e) {
					// ·���쳣
					message.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// �����쳣
					message.what = CODE_NET_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// json�����쳣
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
		builder.setTitle("�������");
		builder.setMessage(msg);
		builder.setPositiveButton("����", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				down();
			}
		});
		
		builder.setNegativeButton("�Ժ���˵", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});
		
		//������ذ��� ������ҳ��
		builder.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
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
					//������ذ��� ������ҳ��
					mypDialog.setOnCancelListener(new OnCancelListener() {
						public void onCancel(DialogInterface dialog) {
							enterHome();
						}
					});
				}
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					mypDialog = null;
					Toast.makeText(SplashActivity.this, "�������", Toast.LENGTH_SHORT).show();
					install(Uri.fromFile(arg0.result),"application/vnd.android.package-archive");
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, arg1, Toast.LENGTH_SHORT).show();
				}
			});
		}else{
			Toast.makeText(SplashActivity.this, "SD��δ����", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void install(Uri data,String type){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setDataAndType(data, type);
		startActivityForResult(intent, 0);
	}
	
	public void showProgessBar(){
		mypDialog = new ProgressDialog(this);
		//ʵ����
		mypDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		//���ý�������񣬷��Ϊ���Σ��п̶ȵ�
		mypDialog.setTitle("�ֻ���ʿ");
		
		//����ProgressDialog ����
		//mypDialog.setMessage(getResources().getString(R.string.second));
		//����ProgressDialog ��ʾ��Ϣ
		mypDialog.setIcon(R.drawable.ic_launcher);
		//����ProgressDialog ����ͼ��
		//mypDialog.setProgress(progress);
		//����ProgressDialog ����������
		//mypDialog.setButton(whichButton, text, listener);
		//����ProgressDialog ��һ��Button
		mypDialog.setIndeterminate(false);
		//����ProgressDialog �Ľ������Ƿ���ȷ
		mypDialog.setCancelable(true);
		//����ProgressDialog �Ƿ���԰��˻ذ���ȡ��
		
		mypDialog.setCanceledOnTouchOutside(false);
		mypDialog.show();
		//��ProgressDialog��ʾ
	}
	
	@Override
	public void finish() {
		if(mypDialog!=null){
			Toast.makeText(SplashActivity.this, "������ȡ��", Toast.LENGTH_SHORT).show();
			
		}else{			
			super.finish();
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		enterHome();
		
		Toast.makeText(SplashActivity.this, "��װ��ȡ��", Toast.LENGTH_SHORT).show();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * ������ҳ��
	 */
	private void enterHome(){
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	/**
	 * ��ȡ�汾��
	 * @return
	 */
	private int getVersionCode(){
		//��������
		PackageManager manager = getPackageManager();
		try {
			//����Ϣ
			PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
			//��ȡ�汾�� �汾��
			int code = packageInfo.versionCode;
			return code;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * ��ȡ�汾��
	 * @return
	 */
	private String getVersion(){
		//��������
		PackageManager manager = getPackageManager();
		try {
			//����Ϣ
			PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
			//��ȡ�汾�� �汾��
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
