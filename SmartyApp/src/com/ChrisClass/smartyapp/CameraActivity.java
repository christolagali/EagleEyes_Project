package com.ChrisClass.smartyapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.NativeCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;


public class CameraActivity extends FragmentActivity implements CvCameraViewListener2{
	 private static final int CAMERA_REQUEST = 1888; 
	// A tag for log output.
	private static final String TAG = "MainActivity";
	// A key for storing the index of the active camera.
	private static final String STATE_CAMERA_INDEX = "cameraIndex";
	// The index of the active camera.
	private int mCameraIndex;
	// Whether the active camera is front-facing.
	// If so, the camera view should be mirrored.
	private boolean mIsCameraFrontFacing;
	// The number of cameras on the device.
	private int mNumCameras;
	// The camera view.
	private CameraBridgeViewBase mCameraView;
	// Whether the next camera frame should be saved as a photo.
	private boolean mIsPhotoPending;
	// A matrix that is used when saving photos.
	Mat rgba,orgba;
	int flag=1;
	private Mat mBgr,mGray;
	 Mat lines;
	Mat cropped_image;
	// Whether an asynchronous menu action is in progress.
	// If so, menu interaction should be disabled.
	private boolean mIsMenuLocked;
	Camera camera;
	Point pt1,pt2;
	Mat img,gray;
	 Point[] pts;
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>(5);
		List<MatOfPoint> selected_contours = new ArrayList<MatOfPoint>(5);
	 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_camera);
		
		final Window window=getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		if(savedInstanceState!=null)
		{
			mCameraIndex=savedInstanceState.getInt(STATE_CAMERA_INDEX,0);
			
		}
		else
		{
			mCameraIndex=0;
		}
		
		CameraInfo cameraInfo=new CameraInfo();
		Camera.getCameraInfo(mCameraIndex, cameraInfo);
		mIsCameraFrontFacing=(cameraInfo.facing==CameraInfo.CAMERA_FACING_FRONT);
		mNumCameras=Camera.getNumberOfCameras();
		
		mCameraView=new NativeCameraView(this, mCameraIndex);
		mCameraView.setCvCameraViewListener(this);
		setContentView(mCameraView);
	}

	public boolean onCreateOptionsMenu(final Menu menu)
	{
		getMenuInflater().inflate(R.menu.cameraact, menu);
		
		if(mNumCameras<2)
		{
			menu.removeItem(R.id.menu_next_camera);
		}
		
		return true;
		
	}
	
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		if(mIsMenuLocked)
		{
			return true;
		}
		
		switch (item.getItemId()) {
		case R.id.menu_next_camera:
		//	mIsMenuLocked=true;
			
//			mCameraIndex++;
//			
//			if(mCameraIndex==mNumCameras)
//			{
//				mCameraIndex=0;
//			}
//			recreate();
			send_next();
			return true;
			
		case R.id.menu_take_photo:
			//mIsMenuLocked=true;
			mIsPhotoPending=true;
			takephoto(rgba);
		//	makecircle(rgba);
		//	intersect();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void send_next()
	{
		startActivity(new Intent("com.ChrisClass.smartyapp.showImage"));
	}
	
	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub
		
		Log.d("Alert!!!", "onCameraViewStarted has started!!!!!");
		mGray=new Mat();
//		Imgproc.cvtColor(rgba, rgba, Imgproc.COLOR_BGR2GRAY);
// 	    Imgproc.cvtColor(rgba, rgba, Imgproc.COLOR_GRAY2RGBA, 4);
		
	}

	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub
		Log.d("Alert!!!", "onCameraViewStarted has stopped!!!!!");
	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		// TODO Auto-generated method stub
		rgba=inputFrame.rgba();
		orgba=new Mat();
		if(flag==1)
		{
			orgba=rgba.clone();
		}
		
		Mat origmat=new Mat();
		origmat=rgba.clone();
		final Mat mIntermediateMat=new Mat();
		final Mat mRgbaInnerWindow=new Mat();
		org.opencv.core.Size s= new Size(3,3);
		
		Scalar mColorsRGB=new Scalar(255,0,0);
		Imgproc.cvtColor(rgba, mGray, Imgproc.COLOR_BGRA2GRAY);
		//Gaussian Blur to eliminate noise
		
		Imgproc.GaussianBlur(rgba, rgba, s, 2);
	//	Imgproc.cvtColor(rgba, rgba, Imgproc.COLOR_GRAY2RGBA, 4);
 	    
 	    //Bitmap bmpOut = Bitmap.createBitmap(rgba.cols(), rgba.rows(), Bitmap.Config.ARGB_8888); 

 	   // Utils.matToBitmap(rgba, bmpOut);
 	    
 	    Imgproc.Canny(rgba, mIntermediateMat, 30, 60); //25,30   too much thresholding 70,90 perfect 45,75

 	    Imgproc.cvtColor(mIntermediateMat, mRgbaInnerWindow, Imgproc.COLOR_GRAY2BGRA, 4);

//		
//		if(mIsPhotoPending)
//		{
//			Log.d("Alert!!!", "onCameraFrame has started!!!!!");
//			
//		
//			mIsPhotoPending=false;
//			takephoto(rgba);
//		}
//		
		if(mIsCameraFrontFacing)
		{
			Core.flip(rgba, rgba, 1);
		}
		
		lines = new Mat();
         Imgproc.HoughLines(mIntermediateMat, lines, 1, Math.PI/180, 150);
         
         pts=new Point[100];
         double[] data;
         double rho, theta;
          pt1 = new Point();
          pt2 = new Point();
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
             pts[i]=pt1;
             pts[i+1]=pt2;
             //Core.line(mIntermediateMat, pt1, pt2, mColorsRGB, 3); This Works Awsmmee
             Core.line(rgba, pt1, pt2, mColorsRGB, 3);
             

         }
      

         /*if(flag==0)
 		{
 			rgba=cropped_image.clone();
 		}*/
		return rgba;
	}

	public void intersect()
	{
		
		
		
	}
	private BaseLoaderCallback mLoaderCallback= new BaseLoaderCallback(this) {
		
		public void onManagerConnected(final int status)
		{
			switch(status)
			{
			case LoaderCallbackInterface.SUCCESS:
				Log.d("Christo Debug","OpenCv Loaded successfully!");
				mCameraView.enableView();
				mBgr=new Mat();
				break;
				default:
					super.onManagerConnected(status);
					break;
			}
		}
	};
	
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save the current camera index.
		savedInstanceState.putInt(STATE_CAMERA_INDEX, mCameraIndex);
		super.onSaveInstanceState(savedInstanceState);
		}
	
	public void onPause() {
		if (mCameraView != null) {
		mCameraView.disableView();
		}
		super.onPause();
		}
	
	@Override
	public void onResume() {
	super.onResume();
	OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3,
	this, mLoaderCallback);
	mIsMenuLocked = false;
	}
	@Override
	public void onDestroy() {
	super.onDestroy();
	if (mCameraView != null) {
	mCameraView.disableView();
	}
	}
	
	public void makecircle(Mat rgba)
	{
		final Mat mIntermediateMat=new Mat();
		final Mat mRgbaInnerWindow=new Mat();
		 Imgproc.Canny(rgba, mIntermediateMat, 30, 60); //25,30   too much thresholding 70,90 perfect 45,75

	 	   mGray.convertTo(mIntermediateMat, CvType.CV_8UC1);
		Scalar mColorsRGB=new Scalar(255,180,0);
     MinMaxLocResult loc=Core.minMaxLoc(mIntermediateMat);
      double maxval=loc.maxVal;
      Point maxLoc=loc.maxLoc;
     
		
      Core.circle(rgba, maxLoc,7 , mColorsRGB);
      
    Core.circle(rgba, pt1,3 , mColorsRGB);
    Core.circle(rgba, pt2, 3, mColorsRGB);
    saveImg(rgba);
	}
	
	public Mat takephoto(Mat rgba)
	{
		
	
		
		Mat mHSVMat = new Mat();
		// Convert the image into an HSV image  
		Imgproc.cvtColor(rgba, mHSVMat, Imgproc.COLOR_RGB2HSV, 3);  
		     
		// HSV values around 20,100,100 to 40,255,255 are yellow(ish)  
		// I use the middle value of 27, plus or minus 10 on Hue  
		Core.circle(rgba, new  Point(rgba.cols()/2, rgba.rows()/2), 3, new  Scalar(0,255,0));
		Point pt = new  Point(rgba.cols()/2, rgba.rows()/2);
		double  cul[] = mHSVMat.get(mHSVMat.rows()/2,mHSVMat.cols()/2);
		Log.i("Color NOW",cul.length  +" = "  + cul[0] + " "  + cul[1] +  " "  + cul[2] + " "); 
		int byteColourTrackCentreHue[] = new int[4];
		byteColourTrackCentreHue[0] = 34;
		int threshold = 72;  //72 is ideal but have to reduce or increase it
		Core.inRange(mHSVMat, new Scalar(cul[0]-threshold, cul[1]-threshold, cul[2]-threshold), new Scalar(cul[0]+threshold, cul[1]+threshold, cul[2]+threshold), mHSVMat);  
		         
		Mat mContours = new Mat(); 	 
		
		Mat mErodeKernel = new Mat();
		Imgproc.erode(mHSVMat, mHSVMat, mErodeKernel);  
		contours.clear();  
		selected_contours.clear();
		Imgproc.findContours(mHSVMat, contours, mContours, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);  
		List<MatOfPoint> largest_contours = new ArrayList<MatOfPoint>(); 
	    double maxarea=-1;
	    int maxAreaIdx = -1;
		double d;
		MatOfPoint2f approxCurve = new MatOfPoint2f();
		MatOfPoint2f maxCurve = new MatOfPoint2f();
		for (int x = 0; x < contours.size(); x++) {
			

		        //point is inside polygon
				MatOfPoint temp_con=contours.get(x);
			    d = Imgproc.contourArea (temp_con);  
				  
				// ignore very small contours, which may be spurious, by  
				// only being interested in contours over a minimum area  
				// of 1000 pixels

				int iContourAreaMin = 12;
				
				if (d > maxarea) {  
				    
					
					MatOfPoint2f new_Mat=new MatOfPoint2f(temp_con.toArray());
					int countsize=(int)temp_con.total();

					Imgproc.approxPolyDP(new_Mat, approxCurve, countsize*0.5, true);
	
				    
				    // draw the contour itself  
					if(approxCurve.total()==4)
					{
						maxCurve = approxCurve;
						maxarea=d;
						maxAreaIdx=x;
						largest_contours.add(temp_con);
					}
				
				    
				   
		        
		    }

		}
		
	    Mat new_image = new Mat(rgba.size(), CvType.CV_8U); //we will create a new black blank image with the largest contour
	    Imgproc.cvtColor(new_image, new_image, Imgproc.COLOR_BayerBG2RGB);
	    Imgproc.drawContours(new_image, contours, maxAreaIdx, new Scalar(255, 255, 255), 1); //will draw the largest square/rectangle

	    double temp_double[] = maxCurve.get(0, 0);
	    Point p1 = new Point(temp_double[0], temp_double[1]);
	    Core.circle(new_image, new Point(p1.x, p1.y), 20, new Scalar(255, 0, 0), 5); //p1 is colored red
	 

	    temp_double = maxCurve.get(1, 0);
	    Point p2 = new Point(temp_double[0], temp_double[1]);
	    Core.circle(new_image, new Point(p2.x, p2.y), 20, new Scalar(0, 255, 0), 5); //p2 is colored green
	   

	    temp_double = maxCurve.get(2, 0);       
	    Point p3 = new Point(temp_double[0], temp_double[1]);
	    Core.circle(new_image, new Point(p3.x, p3.y), 20, new Scalar(0, 0, 255), 5); //p3 is colored blue
	 

	    temp_double = maxCurve.get(3, 0);
	    Point p4 = new Point(temp_double[0], temp_double[1]);
	    Core.circle(new_image, new Point(p4.x, p4.y), 20, new Scalar(0, 255, 255), 5); //p1 is colored violet
		
		
		
		
		//rgba=mHSVMat.clone();
		Mat src_mat = new Mat(4,1,CvType.CV_32FC2);
		//rgba.convertTo(src_mat, CvType.CV_32FC2); //new Mat(4,1,CvType.CV_32FC2);
	    Mat dst_mat=new Mat(4,1,CvType.CV_32FC2);


	    src_mat.put(0,0,(int)p1.y,(int)p1.x, (int)p2.y,(int)p2.x, (int)p4.y,(int)p4.x, (int)p3.y,(int)p3.x); //
	    dst_mat.put(0,0,0.0,0.0,1600.0,0.0, 0.0,2500.0,1600.0,2500.0);  //values tht work 0,0, 0.0,0.0, 3.0, 0.0, 3.0,3.0, 0.0, 3.0
	    Mat perspectiveTransform=Imgproc.getPerspectiveTransform(src_mat, dst_mat);

	    
	    Mat cropped_image = new Mat();
	    cropped_image= rgba.clone();

	    Imgproc.warpPerspective(rgba, cropped_image, perspectiveTransform, new Size(3264,1952));

		
	    
/*	    Mat src = new Mat(4,1,CvType.CV_32FC2);
	    src.put((int)p1.y,(int)p1.x, (int)p2.y,(int)p2.x, (int)p4.y,(int)p4.x, (int)p3.y,(int)p3.x);
	    Mat dst = new Mat(4,1,CvType.CV_32FC2);
	    dst.put(0,0, 0,orgba.width(), orgba.height(),orgba.width(), orgba.height(),0);

	    Mat perspectiveTransform = Imgproc.getPerspectiveTransform(src, dst);
	    Mat cropped_image = orgba.clone();
	    Imgproc.warpPerspective(orgba, cropped_image, perspectiveTransform, new Size(512,512));
	    saveImg(cropped_image);*/
	    
	    saveImg(cropped_image);
		return cropped_image;

	}
	
	public void saveImg(Mat new_image)
	{
		MatOfByte data = new MatOfByte();
		 Mat mat = new_image.clone();
		 String fname="smarty";
		 Highgui.imencode(".jpeg", mat, data);
		 byte[] bytes = data.toArray();
		 try {
			   Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,bytes.length);
			   
			   File direct = new File(Environment.getExternalStorageDirectory() + "/SmartyAppPics");
			   if (!direct.exists()) {
	                File wallpaperDirectory = new File("/sdcard/SmartyAppPics/");
	                wallpaperDirectory.mkdirs();
	            }

			   File path = new File("/sdcard/SmartyAppPics/");
			   File file = new File(path, fname  + ".jpg");
			   
			   if(file.exists())
			   {
				   file.delete();
			   }
			  
			     FileOutputStream fOut = new FileOutputStream(file); 
			     bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut); 
			     fOut.flush(); 
			     fOut.close(); 
			     Toast.makeText(getBaseContext(), "Smarty has been written to external storage.", Toast.LENGTH_SHORT).show(); 
			  } 
			  catch (IOException e) 
			  {
			   e.printStackTrace();
			  }
	}
    
}
