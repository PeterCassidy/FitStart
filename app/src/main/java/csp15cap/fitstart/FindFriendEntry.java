package csp15cap.fitstart;

import android.support.annotation.NonNull;

public class FindFriendEntry implements Comparable {

    private String UniqueId;
    private String name;
    private String profilePicUrl;
    private long exp;

    //empty constructor
    public FindFriendEntry(){}

    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    @Override
    public int compareTo(@NonNull Object ffe) {
        FindFriendEntry compareTo  = (FindFriendEntry) ffe;
        int thisExp = (int)(getExp());
        int ffeExp = (int) compareTo.getExp();
        return ffeExp - thisExp;

    }
}
