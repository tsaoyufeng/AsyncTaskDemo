package com.tsao.asynctasktest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;


//要使用AsyncTask必须要子类化AsyncTask<Params,Progress,Result>，并且需要实现一些回调方法
//Params:启动任务时传入的参数类型
//Progress：后台任务执行中返回的进度值类型
//Result：后台任务完成后返回结果的类型

//doInBackground:必须实现，任务写在此方法中
//onPreExecute:执行后台耗时操作前被调用，通常在此方法中进行初始化操作
//onPostExecute:doInBackground()完成后调用，并会将返回的值传给该方法
//onProgressUpdate:在doInBackground()方法中调用publishProgress()方法更新任务的执行进度后，会触发该方法
public class MyAsyncTask extends AsyncTask<String, Integer, Bitmap> {
	
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		//执行任务前被调用
		System.out.println("onPreExecute");
	}

	@Override
	protected Bitmap doInBackground(String... arg0) {
		//调用后执行任务
		System.out.println("doInBackground");
		String url = arg0[0];//doInBackground的参数是一个可变长数组，个数来源于execute()方法中的参数个数
		Bitmap bitmap = null;
		try {
			URLConnection connection = new URL(url).openConnection();
			InputStream is = connection.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bitmap = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//模拟进度条的更新，i作为模拟的获取到的进度值
		for(int i = 0; i< 100; i++){
			//更新任务进度，调用该方法传递进度。会触发onProgressUpdate()方法
			publishProgress(i);
			try {
				Thread.sleep(100);//为了不执行太快，看不见进度条移动，故意休眠一段时间
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		//任务执行完后调用
		System.out.println("onPostExecute");
		MainActivity.mImageView.setImageBitmap(result);
		//图片已显示，使进度条不可见
		MainActivity.mProgressBar.setVisibility(MainActivity.mProgressBar.GONE);
		
		////////
		////////
		////////保存图片
		String message = "to External Storage";
        //获取外存状态，检查是否可用，如果返回的状态是MEDIA_MOUNTED,代表可以读写。
        String state = Environment.getExternalStorageState();
        
        if (Environment.MEDIA_MOUNTED.equals(state)) {
        	System.out.println("外部存储可用");
        	//保存Public files
        	//调用getExternalStoragePublicDirectory()方法来获取一个 File 对象来表示存储在external storage的目录
        	//参数为目录类型（系统给的）
        	File file = new File(Environment.getExternalStoragePublicDirectory(
        			Environment.DIRECTORY_PICTURES), "pic");
        	//将file作为一个目录生成,名为pic，没有这句file就是一个文件，可以直接在file上进行读写
        	file.mkdir();
        	//String filePath = file.getAbsolutePath();
        	File file1 = new File(file,"pic.png");
        	
        	FileOutputStream fos;
			try {
				fos = new FileOutputStream(file1);
				result.compress(Bitmap.CompressFormat.PNG, 100, fos);//第二个参数为压缩率，100表示不压缩
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
        }
	}

	

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		System.out.println("onProgressUpdate");
		//publishProgress()只传递了一个参数，所以下标用0取值
		MainActivity.mProgressBar.setProgress(values[0]);
	}
	
	

}
