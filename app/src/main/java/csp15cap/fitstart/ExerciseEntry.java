package csp15cap.fitstart;

import android.support.annotation.NonNull;

public class ExerciseEntry implements Comparable {

    private String exerciseEntryId;
    private String desc;
    private String saveDate;

    private long calsBurned;
    private long type;

    //empty constructor
    public ExerciseEntry(){
    }

    public ExerciseEntry(String exerciseEntryId, String desc, String saveDate, long calsBurned, long type) {
        this.exerciseEntryId = exerciseEntryId;
        this.desc = desc;
        this.saveDate = saveDate;
        this.calsBurned = calsBurned;
        this.type = type;
    }

    public String getExerciseEntryId() {
        return exerciseEntryId;
    }

    public void setExerciseEntryId(String exerciseEntryId) {
        this.exerciseEntryId = exerciseEntryId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    public long getCalsBurned() {
        return calsBurned;
    }

    public void setCalsBurned(long calsBurned) {
        this.calsBurned = calsBurned;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }
    @Override
    public int compareTo(@NonNull Object ee){
        ExerciseEntry compareTo = (ExerciseEntry) ee;
        int thisType = (int)(getType());
        int eeType = (int) compareTo.getType();
        return thisType-eeType;
    }

}


