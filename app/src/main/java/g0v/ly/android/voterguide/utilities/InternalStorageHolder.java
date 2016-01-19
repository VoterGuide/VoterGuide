package g0v.ly.android.voterguide.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import g0v.ly.android.voterguide.R;
import g0v.ly.android.voterguide.model.Candidate;

public class InternalStorageHolder {
    private static final Logger logger = LoggerFactory.getLogger(InternalStorageHolder.class);

    private static final String PATH_OF_CANDIDATES_PHOTO_FOLDER = "candidatePhotos";
    private static final String PHOTO_FILE_EXTENTION = ".jpg";
    private int CANDIDATE_PHOTO_WIDTH;
    private int CANDIDATE_PHOTO_HEIGHT;

    private File photoFolder;

    private static InternalStorageHolder ourInstance = new InternalStorageHolder();
    public static InternalStorageHolder getInstance() {
        return ourInstance;
    }

    private InternalStorageHolder() {
    }

    public void setContext(Context context) {
        photoFolder = new File(context.getFilesDir(), PATH_OF_CANDIDATES_PHOTO_FOLDER);
        boolean isSuccess = true;
        if (!photoFolder.exists()) {
            isSuccess = photoFolder.mkdir();
        }

        logger.debug("Get photo folder {} success = {}", photoFolder.getAbsolutePath(), isSuccess);

        CANDIDATE_PHOTO_WIDTH = (int) context.getResources().getDimension(R.dimen.candidate_photo_width);
        CANDIDATE_PHOTO_HEIGHT = (int) context.getResources().getDimension(R.dimen.candidate_photo_height);
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
        File f = new File(photoFolder, parseCandidatePhotoPath(candidate));
        /*
        try {
            File f = new File(photoFolder, parseCandidatePhotoPath(candidate));
            photo = BitmapFactory.decodeStream(new FileInputStream(f));
            photo = decodeScaledBitmapFromSdCard(f.getAbsolutePath(), CANDIDATE_PHOTO_WIDTH, CANDIDATE_PHOTO_HEIGHT);
        }
        catch (FileNotFoundException e) {
            logger.debug(e.getMessage());
        }
        */
        return decodeScaledBitmapFromSdCard(f.getAbsolutePath(), CANDIDATE_PHOTO_WIDTH, CANDIDATE_PHOTO_HEIGHT);
    }

    private String parseCandidatePhotoPath(Candidate candidate) {
        return candidate.county + candidate.name + PHOTO_FILE_EXTENTION;
    }

    private static Bitmap decodeScaledBitmapFromSdCard(String filePath, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        /*
        The default is RGB-config ARGB8888 which means four 8-bit channels are used (red, green, blue, alhpa).
        Alpha is transparency of the bitmap. This occupy a lot of memory - imagesize X 4.
        So if the imagesize is 4 megapixel 16 megabytes will immidiately be allocated on the heap - quickly exhausting the memory.

        http://stackoverflow.com/a/21394281
         */
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        return BitmapFactory.decodeFile(filePath, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}
