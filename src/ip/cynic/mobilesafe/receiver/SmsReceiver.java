package ip.cynic.mobilesafe.receiver;

import java.io.IOException;

import ip.cynic.mobile.utils.SMSUtil;
import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.service.LocationService;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * @author cynic
 *
 * 2015-12-2
 */
public class SmsReceiver extends BroadcastReceiver{

	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;


	@Override
	public void onReceive(Context context, Intent intent) {
		
		//��ȡ����
		Bundle bundle = intent.getExtras();
		Object[] objects = (Object[]) bundle.get("pdus");
		
		for (Object object : objects) {
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])object);
			String originatingAddress = smsMessage.getOriginatingAddress();//�ֻ�����
			String messageBody = smsMessage.getMessageBody();
			if("#*alarm*#".equals(messageBody)){
				//��������
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setLooping(true);//ѭ��
				player.setVolume(1f, 1f);
				player.start();
				
				abortBroadcast();
			}else if("#*location*#".equals(messageBody)){
				Intent service = new Intent(context, LocationService.class);
				context.startService(service);
				SharedPreferences mPref = context.getSharedPreferences("config", Context.MODE_PRIVATE);
				String x = mPref.getString("locationX", "");
				String y = mPref.getString("locationY", "");
				System.out.println(x+y+"λ��");
				SMSUtil.sendSMS(originatingAddress, "x:"+x+"  Y:"+y);
				
				abortBroadcast();
			}else if("#*wipedata*#".equals(messageBody)){
				initAdmin(context);
				mDPM.wipeData(0);
				abortBroadcast();
			}else if("#*lockscreen*#".equals(messageBody)){
				initAdmin(context);
				mDPM.lockNow();
				abortBroadcast();
			}else{
				mDPM.removeActiveAdmin(mDeviceAdminSample);//ȡ������
			}
		}
		
	}
	
	public void initAdmin(Context ctx){
		//��ȡ�豸���Է���
		mDPM = (DevicePolicyManager) ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);
		//��ȡ�豸�������
		mDeviceAdminSample = new ComponentName(ctx, MyDeviceAdminReceiver.class);
		//openAdmin(ctx);
	}
	
	public void openAdmin(Context ctx){//�����豸
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
				mDeviceAdminSample);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				"������, �������˳����豸������, ��NB!");
		ctx.startActivity(intent);
	}
	
}
