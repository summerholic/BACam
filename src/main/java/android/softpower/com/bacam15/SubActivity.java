package android.softpower.com.bacam15;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by hoon on 2018-02-16.
 */

public class SubActivity extends AppCompatActivity {
    private Button btnList = null;

    private static final    int ACTION_TAKE_PHOTO_BEF1 = 1;
    private static final    int ACTION_TAKE_PHOTO_BEF2 = 2;
    private static final    int ACTION_TAKE_PHOTO_BEF3 = 3;
    private static final    int ACTION_TAKE_PHOTO_BEF4 = 4;

    private static final    int ACTION_TAKE_PHOTO_AFT1 = 5;
    private static final    int ACTION_TAKE_PHOTO_AFT2 = 6;
    private static final    int ACTION_TAKE_PHOTO_AFT3 = 7;
    private static final    int ACTION_TAKE_PHOTO_AFT4 = 8;

    private String mCurrentPhotoPath;

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    private static final String JPEG_FILE_SUFFIX = ".jpg";

    TableLayout mTlBACam;
    TableRow mTrBef1;
    TableRow mTrBef2;
    ImageButton mBtnBef1;
    ImageButton mBtnBef2;
    ImageButton mBtnBef3;
    ImageButton mBtnBef4;

    TableRow mTrAft1;
    TableRow mTrAft2;
    ImageButton mBtnAft1;
    ImageButton mBtnAft2;
    ImageButton mBtnAft3;
    ImageButton mBtnAft4;

    private static int btnWidth = 0;
    private static int btnHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        btnList = (Button) findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("onClick", "go to the Lists");
                Intent intentMainActivity = new Intent(SubActivity.this, MainActivity.class);
                startActivity(intentMainActivity);
                finish();
            }
        });

        mTlBACam = (TableLayout) findViewById(R.id.tlBACam);
        mTrBef1 = (TableRow) findViewById(R.id.trBef1);
        mTrBef2 = (TableRow) findViewById(R.id.trBef2);

        mBtnBef1 = (ImageButton) findViewById(R.id.btnBef1);
        mBtnBef1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_BEF1);
            }
        });

        mBtnBef2 = (ImageButton) findViewById(R.id.btnBef2);
        mBtnBef2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_BEF2);
            }
        });

        mBtnBef3 = (ImageButton) findViewById(R.id.btnBef3);
        mBtnBef3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_BEF3);
            }
        });

        mBtnBef4 = (ImageButton) findViewById(R.id.btnBef4);
        mBtnBef4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_BEF4);
            }
        });

        mTrAft1 = (TableRow) findViewById(R.id.trAft1);
        mTrAft2 = (TableRow) findViewById(R.id.trAft2);

        mBtnAft1 = (ImageButton) findViewById(R.id.btnAft1);
        mBtnAft1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_AFT1);
            }
        });

        mBtnAft2 = (ImageButton) findViewById(R.id.btnAft2);
        mBtnAft2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_AFT2);
            }
        });

        mBtnAft3 = (ImageButton) findViewById(R.id.btnAft3);
        mBtnAft3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_AFT3);
            }
        });

        mBtnAft4 = (ImageButton) findViewById(R.id.btnAft4);
        mBtnAft4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_AFT4);
            }
        });
    }

    private void dispatchTakePictureIntent(int actionCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        btnWidth = mBtnAft4.getWidth();
//        btnHeight = mBtnAft4.getHeight();
//        if( btnWidth == 0 || btnHeight == 0 ) {
            /* Get button size through device size  */
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            btnWidth = metrics.widthPixels ;
            Double tmpDouble = (metrics.heightPixels * 0.8) / 4;
            btnHeight = tmpDouble.intValue();
//        }

        File f = null;
        try {
            f = createImageFile(actionCode);
            mCurrentPhotoPath = f.getAbsolutePath();
            Uri photoURI = FileProvider.getUriForFile(this, "android.softpower.com.bacam15.fileProvider", f);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
            mCurrentPhotoPath = null;
        }

        startActivityForResult(takePictureIntent, actionCode);
    }

    private File createImageFile(int actionCode) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String imageFileName = actionCode + "_" + timeStamp;
        File albumF = getAlbumDir();
        ArrayList<File> list = imageReader( albumF );
        int numOfFile = 0;
        int fileSeq = 0;
        if( list != null ) {
            numOfFile = list.size();
            switch ( numOfFile % 8 ) {
                case 0 : fileSeq = numOfFile / 8 + 1; break;
                case 1 : fileSeq = (numOfFile + 7)/ 8; break;
                case 2 : fileSeq = (numOfFile + 6)/ 8; break;
                case 3 : fileSeq = (numOfFile + 5)/ 8; break;
                case 4 : fileSeq = (numOfFile + 4)/ 8; break;
                case 5 : fileSeq = (numOfFile + 3)/ 8; break;
                case 6 : fileSeq = (numOfFile + 2)/ 8; break;
                case 7 : fileSeq = (numOfFile + 1)/ 8; break;
            }
        }
        imageFileName = fileSeq + "_" + imageFileName;
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir("BACam15");
            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()) {
                        Log.d("BACam15", "failed to create directory");
                        return null;
                    }
                }
            }
        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    ArrayList<File> imageReader(File root) {
        ArrayList<File> a = new ArrayList<>();
        File[] files = root.listFiles();
        for( int i=0; i<files.length; i++ ) {
            a.add( files[i] );
        }
        return a;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK ) {
            if (mCurrentPhotoPath != null) {
                Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                File f = new File(mCurrentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

//                btnWidth = mBtnBef1.getWidth();
//                btnHeight = mBtnBef1.getHeight();
                /* There isn't enough memory to open up more than a couple camera photos */
		        /* So pre-scale the target bitmap into which the file is decoded */
		        /* Get the original picture */
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize = 1;
                Bitmap originBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//                bmOptions.inJustDecodeBounds = true;
                float photoW = originBitmap.getWidth();
                float photoH = originBitmap.getHeight();


                /**** 2/3. Scaling Bitmap
                if(photoH > btnHeight)
                {
                    float percente = (float)(photoH / 100);
                    float scale = (float)(btnWidth / percente);
                    photoW *= (scale / 100);
                    photoH *= (scale / 100);
                }
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originBitmap, (int)photoW, (int)photoH, true);
//                Bitmap scaledBitmap = Bitmap.createScaledBitmap(rotatedBitmap, 504, 720, false);
                int scaledWidth = scaledBitmap.getWidth();
                int scaledHeight = scaledBitmap.getHeight();*/

                //**** 3/3. Crobbing Bitmap
//                int crobedX = (scaledWidth - btnHeight) / 2;
                int crobedX = 0;
                int crobedY = 0;
                Bitmap crobedBitmap = Bitmap.createBitmap(originBitmap, crobedX, crobedY, 250, 150); // Crobbed bitmap
                //originBitmap = rotatedBitmap;
                photoW = crobedBitmap.getWidth();
                photoH = crobedBitmap.getHeight();

                /**** 1/3. Rotating Bitmap
                Bitmap rotatedBitmap = null;
                if( photoH > photoW ) {  // Rotate bitmap in case of landscape
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    rotatedBitmap = Bitmap.createBitmap(crobedBitmap, 0, 0, (int)photoW, (int)photoH, matrix, true);
                */


		        /* Set bitmap options to scale the image decode target */
//                bmOptions.inJustDecodeBounds = false;
//                bmOptions.inPurgeable = true;

                if( requestCode == ACTION_TAKE_PHOTO_BEF1 ) {
//                    ImageButton mBtnTmp = (ImageButton)findViewById(R.id.btnBef1);
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), crobedBitmap);
                    mBtnBef1.setBackgroundDrawable(bitmapDrawable);
//                    mBtnTmp.setBackgroundDrawable(bitmapDrawable);
//                    mBtnTmp.setImageResource(bitmapDrawable);
                } else if( requestCode == ACTION_TAKE_PHOTO_BEF2 ) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), crobedBitmap);
                    mBtnBef2.setBackgroundDrawable(bitmapDrawable);
                } else if( requestCode == ACTION_TAKE_PHOTO_BEF3 ) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), crobedBitmap);
                    mBtnBef3.setBackgroundDrawable(bitmapDrawable);
                } else if( requestCode == ACTION_TAKE_PHOTO_BEF4 ) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), crobedBitmap);
                    mBtnBef4.setBackgroundDrawable(bitmapDrawable);
                }
            }
        }
    }
}
