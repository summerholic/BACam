package android.softpower.com.bacam15;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by hoon on 2018-02-16.
 */

public class FroyoAlbumDirFactory extends AlbumStorageDirFactory {
    @Override
    public File getAlbumStorageDir(String albumName) {
        File file = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ),
                albumName
        );

        if (!file.mkdirs()) {
            Log.d("BACam15", "Directory not created");
        }

        return file;
    }
}
