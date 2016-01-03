package g0v.ly.android.voterguide.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WebRequest {

    public enum HttpMethod {
        GET, POST, HEAD
    }

    private static final Logger logger = LoggerFactory.getLogger(WebRequest.class);
    private static final int CONNECT_TIMEOUT_IN_MILLIS = 10000;
    private static final int SOCKET_TIMEOUT_IN_MILLIS = 10000;

    private HttpMethod httpMethod;
    private String basicAuth;

    /**
     * Create {@Link WebRequest}
     *
     * @return a {@link WebRequest}
     */
    public static WebRequest create() {
        return new WebRequest();
    }

    private WebRequest() {
        httpMethod = HttpMethod.GET;
    }

    /**
     * @param method {@linke HttpMethod]
     * @return self
     */
    public WebRequest setHttpMethod(HttpMethod method) {
        httpMethod = method;
        return this;
    }

    /**
     * Send http/https request.
     * Use readLine() to read inputs so all line feed '\n' and carriage return '\r' will be eliminate.
     *
     * @param url
     * @param queryString
     * @return response string or null if failed
     */
    public String sendHttpRequestForResponse(String url, String queryString) {
        InputStream inputStream = sendHttpRequestForInputStream(url, queryString);
        if (inputStream == null) {
            return null;
        }

        String response = null;
        try {
            // Stream for string response
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            response = stringBuilder.toString();

            reader.close();
        }
        catch (IOException e) {
            logger.warn("URL connection IOException: {}", e.getMessage());
        }

        return response;
    }

    /**
     * Send http/https request.
     * Remember to close inputStream.
     *
     * @param url
     * @param queryString
     * @return input stream or null if failed
     */
    public InputStream sendHttpRequestForInputStream(String url, String queryString) {
        HttpURLConnection httpUrlConnection = buildHttpUrlConnection(url, queryString);
        if (httpUrlConnection == null) {
            return null;
        }

        InputStream inputStream = null;
        try {
            int responseCode = httpUrlConnection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                // Handle request status OK
                inputStream = httpUrlConnection.getInputStream();
            }
            else {
                // Handle request status not OK
                logger.warn("URL connection failed with response code {}", responseCode);
            }
        }
        catch (IOException e) {
            logger.warn("URL connection IOException: {}", e.getMessage());
        }

        return inputStream;
    }

    public HttpURLConnection buildHttpUrlConnection(String urlString, String queryString) {
        URL url;
        try {
            // Compose GET url
            if (httpMethod == HttpMethod.GET && queryString != null && queryString.length() > 0) {
                urlString += "?" + queryString;
            }
            url = new URL(urlString);
        }
        catch (MalformedURLException e) {
            logger.warn("Malformed URL {}", urlString);
            return null;
        }

        HttpURLConnection httpUrlConnection = null;
        try {
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setConnectTimeout(CONNECT_TIMEOUT_IN_MILLIS);
            httpUrlConnection.setReadTimeout(SOCKET_TIMEOUT_IN_MILLIS);

            // Detect http or https
            /*
            if (httpUrlConnection instanceof HttpsURLConnection) {
                SSLSocketFactory sslSocketFactory = buildSslSocketFactory();
                ((HttpsURLConnection) httpUrlConnection).setSSLSocketFactory(sslSocketFactory);
            }
            */

            // Basic authorization
            if (basicAuth != null) {
                httpUrlConnection.setRequestProperty("Authorization", basicAuth);
            }

            // Compose POST data
            if (httpMethod == HttpMethod.POST && queryString != null && queryString.length() > 0) {
                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setDoOutput(true);

                DataOutputStream out = new DataOutputStream(httpUrlConnection.getOutputStream());
                out.writeBytes(queryString);
                out.flush();
                out.close();
            }
            if (httpMethod == HttpMethod.HEAD) {
                httpUrlConnection.setRequestMethod("HEAD");
            }
        }
        catch (IOException e) {
            logger.warn("URL connection IOException: {}", e.getMessage());
            httpUrlConnection = null;
        }
        catch (Exception e) {
            logger.warn("URL connection Exception: {}", e.getMessage());
            httpUrlConnection = null;
        }

        return httpUrlConnection;
    }

    /*
    private static SSLSocketFactory buildSslSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustManagers = {byPassTrustManager};

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, new SecureRandom());
        return sslContext.getSocketFactory();
    }

    private static TrustManager byPassTrustManager = new X509TrustManager() {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };
    */
}
