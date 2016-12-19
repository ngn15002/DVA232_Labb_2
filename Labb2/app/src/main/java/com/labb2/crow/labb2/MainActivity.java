package com.labb2.crow.labb2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.ImageView.ScaleType.CENTER_INSIDE;

public class MainActivity extends AppCompatActivity {


	private Button takePictureButton;
	private ImageView imageView;
	public Uri file;
	public Bitmap bmp;
	int debugcheck = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		takePictureButton = (Button) findViewById(R.id.take_photo_button);
		imageView = (ImageView) findViewById(R.id.photo_view);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			takePictureButton.setEnabled(false);
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
		}

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == 0) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
					&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				takePictureButton.setEnabled(true);
			}
		}
	}

	public void take_photo(View view) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		file = Uri.fromFile(getOutputMediaFile());
		intent.putExtra(MediaStore.EXTRA_OUTPUT, file);


		startActivityForResult(intent, 100);


	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100) {
			if (resultCode == RESULT_OK) {


				imageView.setImageURI(file);



				//to be used for better quality in the long future
				//bmp = BitmapFactory.decodeFile(file.getPath());
				bmp = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
				Bitmap tempbitmap = bmp.copy(Bitmap.Config.ARGB_8888,true);
				bmp = tempbitmap;



			}
		}
	}
	private static File getOutputMediaFile(){
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), "CameraDemo");

		if (!mediaStorageDir.exists()){
			if (!mediaStorageDir.mkdirs()){
				return null;
			}
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		return new File(mediaStorageDir.getPath() + File.separator +
				"IMG_"+ timeStamp + ".jpg");
	}

	public void invert(View view) {
		if ( bmp != null ) {
			for ( int i = 0; i < bmp.getHeight(); i++ ) {
				for ( int j = 0; j < bmp.getWidth(); j++ ) {
					int clr = bmp.getPixel( j, i );
					int r = 255 - Color.red( clr ) ;
					int g = 255 - Color.green( clr );
					int b = 255 -Color.blue( clr );
					clr = Color.argb( Color.alpha( clr ), r, g, b );
					bmp.setPixel( j, i, clr);

				}
			}
			imageView.setImageBitmap( bmp );
		}

	}

	public void grey_scale(View view) {

		if ( bmp != null ){
			for ( int i = 0; i < bmp.getHeight(); i++ ) {
				for ( int j = 0; j < bmp.getWidth(); j++ ) {
					int clr = bmp.getPixel( j, i );
					int r = Color.red( clr );
					int g = Color.green( clr );
					int b = Color.blue( clr );

					int grey = ( r + g + b ) / 3;

					clr = Color.argb( Color.alpha( clr ), grey, grey, grey );
					bmp.setPixel( j, i, clr);
				}
			}
		}
		imageView.setImageBitmap( bmp );
	}

	public void save(View view) {

		try {
			FileOutputStream out = new FileOutputStream( file.getPath() );
			bmp.compress( Bitmap.CompressFormat.JPEG, 100, out );
			out.flush();
			out.close();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public void Revert(View view)
	{

		imageView.setImageURI(file);
	}
	public void Cancel(View view)
	{}
	private Drawable resize(Drawable image) {
		Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
		Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap,
				(int) (bitmap.getWidth() * 0.5), (int) (bitmap.getHeight() * 0.5), false);
		return new BitmapDrawable(getResources(), bitmapResized);
	}
}