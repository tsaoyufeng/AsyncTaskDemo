package com.tsao.asynctasktest;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
	
	public static ImageView mImageView;
	public static ProgressBar mProgressBar;
	public Button btn_down;
	
	private static String URL = "http://ww2.sinaimg.cn/crop.80.0.800.800.1024/6a27e5d9jw8e8wa5zczwkj20qo0m875m.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mImageView = (ImageView) findViewById(R.id.iv_image);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_jindu);
        btn_down = (Button) findViewById(R.id.button);
        btn_down.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				MyAsyncTask task = new MyAsyncTask();//创建AsyncTask实例
		        task.execute(URL);//调用execute()方法执行任务,参数会被传递至doInBackground()方法中，可以传递多个参数，这里只传一个
			}
		});
        
        
    }


    
    
}
