package ip.cynic.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseSetupActivity extends Activity{

	private GestureDetector mDetector;
	public SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		//����ʶ����
		mDetector = new GestureDetector(this, new SimpleOnGestureListener(){
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				System.out.println(velocityX);
				
				//y�Ử�����ȹ����򲻴���
				if(Math.abs(e1.getRawY()-e2.getRawY())<100){
					//�󻬽�����һҳ
					if(e1.getRawX()-e2.getRawX()>100){
						showNextPage();
						return true;
					}
					
					//�һ�������һҳ
					if(e2.getRawX()-e1.getRawX()>100){
						showPrevious();
						return true;
					}
				}
				
				
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}
	    
	
	public abstract void showNextPage();
	
	public abstract void showPrevious();
	
	public void next(View v){
		showNextPage();
	}

	public void previous(View v){
		showPrevious();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//��activity�Ĵ����¼�����GestureDetector
		mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
