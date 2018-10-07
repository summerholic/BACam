package android.softpower.com.bacam15;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private List<String> fileNameList;
    private FlAdapter mAdapter;
    private File file;

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnPicture = (Button)findViewById( R.id.btnPicture);
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("onClick", "callSubActivity");
                Intent intentSubActivity = new Intent(MainActivity.this, SubActivity.class);
                startActivity(intentSubActivity);
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        Boolean isGranted = isWriteStoragePermissionGranted();
        if( isGranted ) {
            LoadImgList();
        }
    }

    private void LoadImgList() {
        mListView = (ListView) findViewById(R.id.listview1);
        File albumF = getAlbumDir();
        List<String> fileNameList = imageReader(albumF);
        mAdapter = new FlAdapter(this, R.layout.fl_list_item, fileNameList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView listText = (TextView) view.findViewById(R.id.fname);
                String fname = listText.getText().toString();
                String[] fnameArr = null;
                String fileSeq = null;
                if (fname != null) {
                    fnameArr = fname.split("_");
                    if (fnameArr.length > 0) {
                        fileSeq = fnameArr[0];
                    }
                }
//                Intent intentSubActivity2 = new Intent(MainActivity.this, SubActivity2.class);
//                intentSubActivity2.putExtra("selected-file", fileSeq);
//                startActivity(intentSubActivity2);
            }
        });
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

    List<String> imageReader(File root) {
        List<String> a = new ArrayList<>();
        File[] files = root.listFiles();
        List<String> fileSeqList = new ArrayList<>();
        for( int i=0; i<files.length; i++ ) {
            String fileName = files[i].getName();
            String fileSeq = null;
            if( fileName != null && fileName.length() > 0 ) {
                fileSeq = fileName.substring(0,1);
                if(!fileSeqList.contains(fileSeq)) {
                    fileSeqList.add(fileSeq);
                    a.add( files[i].getName() );
                }
            }
        }
        return a;
    }

    public class FlAdapter extends ArrayAdapter<String> {

        private List<String> fLst;
        private Context adapContext;

        public FlAdapter(Context context, int textViewResourceId,
                         List<String> fLst) {
            super(context, textViewResourceId, fLst);
            this.fLst = fLst;
            adapContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            FHolder fHolder = null;

            if (convertView == null) {
                view = View.inflate(adapContext, R.layout.fl_list_item, null);

                fHolder = new FHolder();
                fHolder.fNameView = (TextView) view.findViewById(R.id.fname);

                view.setTag(fHolder);
            } else {
                fHolder = (FHolder) view.getTag();
            }
            String fileName = fLst.get(position);
            fHolder.fNameView.setText(fileName);

            return view;
        }
    }

    static class FHolder {
        public TextView fNameView;
    }

    /**
     * Permission check & request in run time for external storage
     */
    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d("BACam15","Permission is granted2");
                return true;
            } else {
                Log.d("BACam15","Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.d("BACam15","Permission is granted2");
            return true;
        }
    }

    /**
     * Getting permission response in our Activity
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d("BACam15", "External storage2");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.d("BACam15","Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    LoadImgList();
                }else{
//                    progress.dismiss();
                }
                break;
        }
    }
}
