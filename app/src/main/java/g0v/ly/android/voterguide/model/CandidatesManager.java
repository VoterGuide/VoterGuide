package g0v.ly.android.voterguide.model;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import android.os.Handler;
import android.os.HandlerThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Executors;

import g0v.ly.android.voterguide.net.WebRequest;

public class CandidatesManager extends Observable {
    private static final Logger logger = LoggerFactory.getLogger(CandidatesManager.class);

    private static CandidatesManager instance;

    private ListeningExecutorService servicePool = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    private Handler handler;

    private List<String> downloadedCounties = new ArrayList<>();
    private List<Candidate> allCandidates = new ArrayList<>();

    private CandidatesManager() {
        HandlerThread handlerThread = new HandlerThread("thread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    public static CandidatesManager getInstance() {
        synchronized (CandidatesManager.class) {
            if (instance == null) {
                instance = new CandidatesManager();
            }
        }
        return instance;
    }

    /**
     * Return select county's candidate list, return empty list and start download if select county's list not download before.
     * @param county - Candidate's election county
     */
    public List<Candidate> getCandidatesWithCounty(String county) {
        List<Candidate> candidates = new ArrayList<>();
        boolean hasDownloadBefore = false;

        for (Candidate candidate : allCandidates) {
            if (candidate.county.equals(county)) {
                hasDownloadBefore = true;
                break;
            }
        }

        if (hasDownloadBefore) {
            for (Candidate candidate : allCandidates) {
                if (candidate.county.equals(county)) {
                    candidates.add(candidate);
                }
            }
        }
        else {
            downloadCandidatesOfCounty(county);
        }

        return candidates;
    }

    public Candidate getCandidateWithName(String name) {
        for (Candidate tempCandidate : allCandidates) {
            if (tempCandidate.name.equals(name)) {
                return tempCandidate;
            }
        }

        return null;
    }

    private void downloadCandidatesOfCounty(final String countyString) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (downloadedCounties.contains(countyString)) {
                    return;
                }

                List<Candidate> candidates;
                String countryStringInEnglish = "";
                try {
                    countryStringInEnglish = URLEncoder.encode(countyString, "UTF-8");
                }
                catch (UnsupportedEncodingException e) {
                    logger.debug(e.getMessage());
                }

                boolean hasNextPage = false;
                int page = 0;
                do {
                    page++;
                    candidates = new ArrayList<>();
                    logger.debug("Load {} candidate list, page: {}", countyString, page);

                    String rawResultString = WebRequest.create()
                            .sendHttpRequestForResponse(WebRequest.G0V_LY_VOTE_API_URL, "ad=9&page=" + page + "&county=" + countryStringInEnglish);
                    try {
                        JSONObject rawObject = new JSONObject(rawResultString);
                        JSONArray candidatesArray = rawObject.getJSONArray("results");

                        for (int i = 0; i < candidatesArray.length(); i++) {
                            JSONObject candidateObject = candidatesArray.getJSONObject(i);
                            Candidate candidate = new Candidate(candidateObject, servicePool);
                            candidates.add(candidate);
                        }

                        hasNextPage = rawObject.has("next") && !rawObject.getString("next").equals("null");

                        setChanged();
                        notifyObservers();
                        allCandidates.addAll(candidates);
                    }
                    catch (JSONException e) {
                        logger.debug(e.getMessage());
                    }
                }
                while (hasNextPage);

                downloadedCounties.add(countyString);
            }
        });
    }
}
