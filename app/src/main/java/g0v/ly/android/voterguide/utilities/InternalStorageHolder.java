package g0v.ly.android.voterguide.utilities;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import g0v.ly.android.voterguide.model.Candidate;

public class InternalStorageHolder {
    private static final Logger logger = LoggerFactory.getLogger(InternalStorageHolder.class);

    private static final String PATH_OF_CANDIDATES_PHOTO_FOLDER = "candidatePhotos";
    private static final String PHOTO_FILE_EXTENTION = ".jpg";
    private File photoFolder;

    private static InternalStorageHolder ourInstance = new InternalStorageHolder();
    public static InternalStorageHolder getInstance() {
        return ourInstance;
    }

    private InternalStorageHolder() {
    }

    public void setContext(Context context) {
        //ContextWrapper cw = new ContextWrapper(context);
        //photoFolder = cw.getDir(PATH_OF_CANDIDATES_PHOTO_FOLDER, Context.MODE_PRIVATE);
        photoFolder = new File(context.getFilesDir(), PATH_OF_CANDIDATES_PHOTO_FOLDER);
        boolean isSuccess = true;
        if (!photoFolder.exists()) {
            isSuccess = photoFolder.mkdir();
        }

        logger.debug("Get photo folder {} success = {}", photoFolder.getAbsolutePath(), isSuccess);
    }

    public String saveToInternalSorage(Candidate candidate, Bitmap bitmapImage) {
        File myPath=new File(photoFolder, parseCandidatePhotoPath(candidate));
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                logger.debug(e.getMessage());
            }
        }
        return photoFolder.getAbsolutePath();
    }

    public Bitmap loadImageFromStorage(Candidate candidate) {
        Bitmap photo = null;
        try {
            File f = new File(photoFolder, parseCandidatePhotoPath(candidate));
            photo = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e) {
            logger.debug(e.getMessage());
        }
        return photo;
    }

    private String parseCandidatePhotoPath(Candidate candidate) {
        return candidate.county + candidate.name + PHOTO_FILE_EXTENTION;
    }
}
