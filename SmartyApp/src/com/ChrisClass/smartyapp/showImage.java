package com.ChrisClass.smartyapp;

import java.io.File;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

@SuppressLint("SdCardPath")
public class showImage extends Activity{
	 private static final int CAMERA_REQUEST = 1888; 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.showimage);
		
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public void ClickMe(View view)
	{
//		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
//		startActivityForResult(cameraIntent, CAMERA_REQUEST);
//		
		
		 Mat imgToProcess = new Mat();
         ImageView imageView=(ImageView)findViewById(R.id.imageView1);
     	
         /*Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.mybox);

 	    Utils.bitmapToMat(bmp, imgToProcess);

 	    //******
 	    //right here I need to convert this imgToProcess to grayscale for future opencv processes
 	    //******

 	    Imgproc.cvtColor(imgToProcess, imgToProcess, Imgproc.COLOR_BGR2GRAY);
 	    Imgproc.cvtColor(imgToProcess, imgToProcess, Imgproc.COLOR_GRAY2RGBA, 4);
 	    
 	    Bitmap bmpOut = Bitmap.createBitmap(imgToProcess.cols(), imgToProcess.rows(), Bitmap.Config.ARGB_8888); 

 	    Utils.matToBitmap(imgToProcess, bmpOut);
 	    imageView.setImageBitmap(bmpOut);
 	    
 	    Utils.bitmapToMat(bmpOut, imgToProcess);
 	    doCanny(imgToProcess,bmpOut);*/
         File img=new File("/sdcard/SmartyAppPics/smarty.jpg");
         if(img.exists()){

        	    Bitmap myBitmap = BitmapFactory.decodeFile(img.getAbsolutePath());

        	   

        	    imageView.setImageBitmap(myBitmap);

        	}
         
         
        // imageView.setImageBitmap(bmp);
         
	}
	
	
	public void doCanny(Mat mRgba,Bitmap bmp)
	{
		Mat mIntermediateMat=new Mat();
		Mat mRgbaInnerWindow=new Mat();
		Scalar mColorsRGB=new Scalar(255,255,255);
	
		
		 ImageView imageView=(ImageView)findViewById(R.id.imageView1);
		
		  Imgproc.Canny(mRgba, mIntermediateMat, 25, 30);

          Imgproc.cvtColor(mIntermediateMat, mRgbaInnerWindow, Imgproc.COLOR_GRAY2BGRA, 4);
          
          Mat lines = new Mat();
          Imgproc.HoughLines(mIntermediateMat, lines, 1, Math.PI/180, 150);
          
          double[] data;
          double rho, theta;
          Point pt1 = new Point();
          Point pt2 = new Point();
          double a, b;
          double x0, y0;
          for (int i = 0; i < lines.cols(); i++)
          {
              data = lines.get(0, i);
              rho = data[0];
              theta = data[1];
              a = Math.cos(theta);
              b = Math.sin(theta);
              x0 = a*rho;
              y0 = b*rho;
              pt1.x = Math.round(x0 + 1000*(-b));
              pt1.y = Math.round(y0 + 1000*a);
              pt2.x = Math.round(x0 - 1000*(-b));
              pt2.y = Math.round(y0 - 1000 *a);
              //Core.line(mIntermediateMat, pt1, pt2, mColorsRGB, 3); This Works Awsmmee
              Core.line(mRgba, pt1, pt2, mColorsRGB, 3);
              
          }
         // Utils.matToBitmap(mIntermediateMat, bmp); This works awsomee
          Utils.matToBitmap(mRgba, bmp);
   	    imageView.setImageBitmap(bmp);
          
	}
//	
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
//        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
//           
//    	   
//        }
//	}
}
