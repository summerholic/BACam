package android.softpower.com.bacam15;

import java.io.File;

/**
 * Created by hoon on 2018-02-16.
 */

public abstract class AlbumStorageDirFactory {
    public abstract File getAlbumStorageDir(String albumName);
}
