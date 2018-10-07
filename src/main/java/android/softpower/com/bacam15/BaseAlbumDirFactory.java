package android.softpower.com.bacam15;

import android.os.Environment;

import java.io.File;

/**
 * Created by hoon on 2018-02-16.
 */

public class BaseAlbumDirFactory extends AlbumStorageDirFactory {
    private static final String CAMERA_DIR = "/dcim/";

    @Override
    public File getAlbumStorageDir(String albumName) {
        return new File (
                Environment.getExternalStorageDirectory()
                        + CAMERA_DIR
                        + albumName
        );
    }
}
