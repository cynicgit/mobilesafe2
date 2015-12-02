package ip.cynic.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * @author cynic
 *
 * 2015-12-2
 */
public class LocationService extends Service{

	private LocationManager lm;
	private SharedPreferences mPref;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);//������(ʹ������)
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // ����Ϊ��󾫶�
		String bestProvider = lm.getBestProvider(criteria, true);//��ȡ��ѵ�λ���ṩ��
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		lm.requestLocationUpdates(bestProvider, 0, 0, new LocationListener(){

			//λ�ñ仯
			@Override
			public void onLocationChanged(Location location) {
				String longitude = "����"+location.getLongitude();//����
				String latitude = "γ��"+location.getLatitude();//γ��
				String accuracy = "��ȷ��"+location.getAccuracy();//γ��
				mPref.edit().putString("locationX", longitude).putString("locationY", latitude).commit();
			}

			//״̬�仯���źţ�
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				
			}
			//GPS����
			@Override
			public void onProviderEnabled(String provider) {
				
			}
			//GPS�ر�
			@Override
			public void onProviderDisabled(String provider) {
				
			}
			
		});
	}


}
