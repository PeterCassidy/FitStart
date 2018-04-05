package csp15cap.fitstart;

public class FoodEntry {
    private String foodEntryId;
    private String Desc;
    private long cals;
    private long carbs;
    private long protein;
    private long fat;
    private long type;

    //empty constructor
    public FoodEntry() {
    }

    public FoodEntry(String foodEntryId, String desc, long cals, long carbs, long protein, long fat, long type) {
        this.foodEntryId = foodEntryId;
        Desc = desc;
        this.cals = cals;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
        this.type = type;
    }
    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }



    public String getFoodEntryId() {
        return foodEntryId;
    }

    public void setFoodEntryId(String foodEntryId) {
        this.foodEntryId = foodEntryId;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String Desc) {
        this.Desc = Desc;
    }

    public long getCals() {
        return cals;
    }

    public void setCals(long cals) {
        this.cals = cals;
    }

    public long getCarbs() {
        return carbs;
    }

    public void setCarbs(long carbs) {
        this.carbs = carbs;
    }

    public long getProtein() {
        return protein;
    }

    public void setProtein(long protein) {
        this.protein = protein;
    }

    public long getFat() {
        return fat;
    }

    public void setFat(long fat) {
        this.fat = fat;
    }
}
