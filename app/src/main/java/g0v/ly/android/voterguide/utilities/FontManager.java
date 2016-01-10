package g0v.ly.android.voterguide.utilities;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FontManager {
    private static final Logger logger = LoggerFactory.getLogger(FontManager.class);

    private static boolean isContextGet = false;
    private static FontManager instance = null;
    private Typeface robotoRegular;
    private Typeface robotoLight;
    private Typeface beautifulRegularScript;
    private Typeface bookCarving;
    private Typeface mengNaYuYi;
    private Typeface superCoolRegularScript;

    private FontManager() {
    }

    public static FontManager getInstance() {
        synchronized (FontManager.class) {
            if (instance == null) {
                instance = new FontManager();
            } else if (!isContextGet) {
                logger.error("FontManager", "!isContextGet");
            }
        }
        return instance;
    }

    public void setContext(Activity activity) {
        if (Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Light.ttf") == null) {
            logger.error("FontManager", "can't get typeface");
        }

        isContextGet = true;
        AssetManager assetManager = activity.getAssets();
        this.robotoRegular =
                Typeface.createFromAsset(assetManager, "fonts/Roboto/Roboto-Regular.ttf");
        this.robotoLight = Typeface.createFromAsset(assetManager, "fonts/Roboto/Roboto-Light.ttf");
        this.beautifulRegularScript = Typeface.createFromAsset(assetManager, "fonts/beautiful-regular-script.ttf");
        this.bookCarving = Typeface.createFromAsset(assetManager, "fonts/book-carving-font.ttf");
        this.mengNaYuYi = Typeface.createFromAsset(assetManager, "fonts/meng-na-yu-yi.otf");
        this.superCoolRegularScript = Typeface.createFromAsset(assetManager, "fonts/super-cool-regular-script-character.ttf");
    }

    public Typeface getRobotoRegular() {
        return robotoRegular;
    }

    public Typeface getRobotoLight() {
        if (robotoLight == null) {
            logger.error("FontManager", "Roboto-Light = null");
        }
        return robotoLight;
    }

    public Typeface getBeautifulRegularScript() {
        if (beautifulRegularScript == null) {
            logger.error("FontManager", "beautifulRegularScript = null");
        }
        return beautifulRegularScript;
    }

    public Typeface getBookCarving() {
        if (bookCarving == null) {
            logger.error("FontManager", "bookCarving = null");
        }
        return bookCarving;
    }

    public Typeface getMengNaYuYi() {
        if (mengNaYuYi == null) {
            logger.error("FontManager", "mengNaYuYi = null");
        }
        return mengNaYuYi;
    }

    public Typeface getSuperCoolRegularScript() {
        if (superCoolRegularScript == null) {
            logger.error("FontManager", "superCoolRegularScript = null");
        }
        return superCoolRegularScript;
    }
}
