package g0v.ly.android.voterguide.utilities;

import android.content.Context;
import android.content.res.AssetManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AssetsLoader {
    private static final Logger logger = LoggerFactory.getLogger(AssetsLoader.class);

    private static final String ASSET_FILE_PATH = "voterguide.properties"; // in assets

    private static AssetsLoader instance = null;
    private static Context context = null;

    private Properties configProperties;

    /**
     * Set application context to get assets. (Context.getResource().getAssets())
     * @param ctx application context
     */
    public static void setContext(Context ctx) {
        context = ctx;
    }

    /**
     * [P] Singleton
     *
     * @return ConfigLoader
     */
    public static AssetsLoader getInstance() {
        synchronized (AssetsLoader.class) {
            if (instance == null) {
                instance = new AssetsLoader();
            }
        }
        return instance;
    }

    private AssetsLoader() {
        configProperties = new Properties();

        if (context == null) {
            logger.warn("Context == null, set context before use.");
            return;
        }

        AssetManager assetManager = context.getResources().getAssets();
        try {
            InputStream inputStream = assetManager.open(ASSET_FILE_PATH);
            configProperties.load(inputStream);
        }
        catch (IOException e) {
            logger.warn("Load config properties failed: {}", e.getMessage());
        }
    }

    /**
     * Get boolean value of key
     *
     * @param key
     * @return true if value is not null and equal, ignoring case, to the string "true".
     */
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(configProperties.getProperty(key));
    }
}
