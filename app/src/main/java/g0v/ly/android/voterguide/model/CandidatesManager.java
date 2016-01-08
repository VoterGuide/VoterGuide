package g0v.ly.android.voterguide.model;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import g0v.ly.android.voterguide.net.WebRequest;

public class CandidatesManager {
    private static final Logger logger = LoggerFactory.getLogger(CandidatesManager.class);

    private static CandidatesManager instance;
    private ListeningExecutorService servicePool = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

    private List<Candidate> allCandidates = new ArrayList<>();

    private CandidatesManager() {}

    public static CandidatesManager getInstance() {
        synchronized (CandidatesManager.class) {
            if (instance == null) {
                instance = new CandidatesManager();
            }
        }
        return instance;
    }

    /**
     * Blocking method
     * @param county - Candidate's election county
     * @return Candidate list
     */
    public List<Candidate> getCandidatesWithCounty(String county) {
        boolean hasDownloadBefore = false;
        List<Candidate> candidates = new ArrayList<>();

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
            candidates = downloadCandidatesOfCounty(county);
            allCandidates.addAll(candidates);
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

    private List<Candidate> downloadCandidatesOfCounty(String countyString) {
        List<Candidate> candidates = new ArrayList<>();
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
            }
            catch (JSONException e) {
                logger.debug(e.getMessage());
            }
        }
        while(hasNextPage);

        return candidates;
    }
}
