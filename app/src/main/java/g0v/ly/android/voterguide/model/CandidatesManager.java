package g0v.ly.android.voterguide.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CandidatesManager {
    private static final Logger logger = LoggerFactory.getLogger(CandidatesManager.class);

    private static CandidatesManager instance;

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

    public void addCandidates(JSONObject rawObject) {
        try {
            JSONArray candidatesArray = rawObject.getJSONArray("results");

            for (int i = 0; i < candidatesArray.length(); i++) {
                JSONObject candidateObject = candidatesArray.getJSONObject(i);
                Candidate candidate = new Candidate(candidateObject);
                allCandidates.add(candidate);
            }
        }
        catch (JSONException e) {
            logger.debug(e.getMessage());
        }
    }

    public List<Candidate> getCandidatesWithCounty(String county) {
        List<Candidate> candidates = new ArrayList<>();
        for (Candidate candidate : allCandidates) {
            if (candidate.county.equals(county)) {
                candidates.add(candidate);
            }
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
}
