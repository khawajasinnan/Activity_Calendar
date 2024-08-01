public class Activity {
    private String title;
    private float priority;
    private String userId;
    private int duration;

    public Activity(String title, float priority, String userId, int duration) {
        this.title = title;
        this.priority = priority;
        this.userId = userId;
        this.duration = duration;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setPriority(float priority) {
        this.priority = priority;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public String getTitle() {
        return title;
    }
    public float getPriority() {
        return priority;
    }
    public String getUserId() {
        return userId;
    }
    public int getDuration() {
        return duration;
    }
    @Override
    public String toString() {
        return title + "\t" + priority + "\t" + userId + "\t" + duration;
    }
}
