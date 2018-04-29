package csp15cap.fitstart;

import android.support.annotation.NonNull;

public class DailyChallengeEntry implements Comparable{

    @Override
    public int compareTo(@NonNull Object dce) {
        DailyChallengeEntry compareTo = (DailyChallengeEntry) dce;
        int date = (Integer.parseInt(getSaveDate()));
        int dcedate = Integer.parseInt(compareTo.getSaveDate());
        return date-dcedate;
    }

    private String challengeEntryId;
    private String challengeName;
    private String challengeDiff;
    private String saveDate;
    private Long experience;



    private String completed;

    private String ex1Name;
    private String ex1Url;
    private String ex2Name;
    private String ex2Url;
    private String ex3Name;
    private String ex3Url;
    private String ex4Name;
    private String ex4Url;

    public DailyChallengeEntry(){
        //empty constructor
    }
    public String getChallengeEntryId() {
        return challengeEntryId;
    }

    public void setChallengeEntryId(String challengeEntryId) {
        this.challengeEntryId = challengeEntryId;
    }

    public String getChallengeName() {
        return challengeName;
    }
    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }



    public String getChallengeDiff() {
        return challengeDiff;
    }

    public void setChallengeDiff(String challengeDiff) {
        this.challengeDiff = challengeDiff;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    public Long getExperience() {
        return experience;
    }

    public void setExperience(Long experience) {
        this.experience = experience;
    }

    public String getEx1Name() {
        return ex1Name;
    }

    public void setEx1Name(String ex1Name) {
        this.ex1Name = ex1Name;
    }

    public String getEx1Url() {
        return ex1Url;
    }

    public void setEx1Url(String ex1Url) {
        this.ex1Url = ex1Url;
    }

    public String getEx2Name() {
        return ex2Name;
    }

    public void setEx2Name(String ex2Name) {
        this.ex2Name = ex2Name;
    }

    public String getEx2Url() {
        return ex2Url;
    }

    public void setEx2Url(String ex2Url) {
        this.ex2Url = ex2Url;
    }

    public String getEx3Name() {
        return ex3Name;
    }

    public void setEx3Name(String ex3Name) {
        this.ex3Name = ex3Name;
    }

    public String getEx3Url() {
        return ex3Url;
    }

    public void setEx3Url(String ex3Url) {
        this.ex3Url = ex3Url;
    }

    public String getEx4Name() {
        return ex4Name;
    }

    public void setEx4Name(String ex4Name) {
        this.ex4Name = ex4Name;
    }

    public String getEx4Url() {
        return ex4Url;
    }

    public void setEx4Url(String ex4Url) {
        this.ex4Url = ex4Url;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }
}
