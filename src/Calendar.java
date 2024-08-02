import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Calendar {
    private final Activity[][][][][] calendar;

    public Calendar() {
        calendar = new Activity[12][][][][];

        for (int month = 0; month < 12; month++) {
            int daysInMonth = getDaysInMonth(month + 1);
            calendar[month] = new Activity[daysInMonth][24][][];
            for (int days = 0; days < daysInMonth; days++) {
                for (int hours = 0; hours < 24; hours++) {
                    calendar[month][days][hours] = new Activity[1][];
                }
            }
        }
    }

    private int getDaysInMonth(int month) {
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return 28;
            default:
                System.out.println("Invalid month: " + month);
        }
        return month;
    }

    public void addActivity(int month, int day, int hour, Activity activity) {
        List<Activity> activities = new ArrayList<>();
        if (calendar[month - 1][day - 1][hour][0] != null) {
            for (Activity act : calendar[month - 1][day - 1][hour][0]) {
                if (act != null) {
                    activities.add(act);
                }
            }
            activities.add(activity);
            calendar[month - 1][day - 1][hour][0] = new Activity[activities.size()];
            for (int i = 0; i < activities.size(); i++) {
                calendar[month - 1][day - 1][hour][0][i] = activities.get(i);
            }
        }
    }
    public List<Activity> getActivities(int month, int day, int hour){
        List<Activity> activities = new ArrayList<>();
        if (calendar[month - 1][day - 1][hour][0] != null) {
            for (Activity act : calendar[month - 1][day - 1][hour][0]) {
                if(act != null){
                    activities.add(act);
                }
            }
        }
        return activities;
    }
    public List<Activity> listUserActivities(String userId, int startMonth, int startDay, int endMonth, int endDay){
        List<Activity> userActivities = new ArrayList<>();
        for (int month = startMonth - 1; month <= endMonth - 1; month++) {
            for (int day = (month == startMonth - 1 ? startDay - 1 : 0); day<= (month == endMonth -1 ? endDay -1:getDaysInMonth(month+1)-1); day++) {
                for (int hour =0; hour < 24; hour++) {
                    if (calendar[month - 1][day - 1][hour][0] != null) {
                        for (Activity activity : calendar[month - 1][day - 1][hour][0]) {
                            if(activity != null && activity.getUserId().equals(userId)){
                                userActivities.add(activity);
                            }
                        }
                    }
                }
            }
        }
        return userActivities;
    }
    public void removeUserActivities(String userId){
        List<Activity> activities = new ArrayList<>();
        for (int month = 0; month < 12; month++) {
            for (int day = 0; day < getDaysInMonth(month+1); day++) {
                for (int hour = 0; hour < 24; hour++) {
                    if (calendar[month][day][hour][0] != null) {
                        for (Activity activity : calendar[month][day][hour][0]) {
                            if (activity != null && activity.getUserId().equals(userId)) {
                                activities.add(activity);
                            }
                        }
                        calendar[month][day][hour][0] = activities.toArray( new Activity[0]);
                    }
                }
            }
        }
    }
    public void loadToTheFile(String fileName){
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                int month = Integer.parseInt(tokens[0]);
                int day = Integer.parseInt(tokens[1]);
                int hour = Integer.parseInt(tokens[2]);
                String title = tokens[3];
                float priority = Float.parseFloat(tokens[4]);
                String userId = tokens[5];
                int duration = Integer.parseInt(tokens[6]);

                var activity = new Activity(title, priority, userId, duration);
                addActivity(month, day, hour, activity);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public List<Activity> listClashingActivities(String userId1, String userId2,int startMonth, int endMonth, int startDay, int endDay){
        List<Activity> clashingActivities = new ArrayList<>();
        for (int month = startMonth - 1; month <= endMonth - 1; month++) {
            for(int day = (month == startMonth - 1 ? startDay - 1 : 0); day<= (month == endMonth -1 ? endDay -1:getDaysInMonth(month+1)-1); day++) {
                for (int hour = 0; hour < 24; hour++) {
                    Set<String> userIds = new HashSet<>();
                    if (calendar[month][day][hour][0] != null) {
                        for (Activity activity : calendar[month][day][hour][0]) {
                            if (activity != null) {
                                userIds.add(activity.getUserId());
                            }
                        }
                    }
                    if(userIds.contains(userId1) && userIds.contains(userId2)){
                        clashingActivities.addAll(Arrays.asList(calendar[month][day][hour][0]));
                    }
                }

            }
        }
        return clashingActivities;
    }

    public List<Activity> listFreeSlotsForUsers(String[]userIds, int startMonth, int endMonth, int startDay, int endDay){
        List<Activity> freeSlots = new ArrayList<>();
        for (int month = startMonth - 1; month <= endMonth - 1; month++) {
            for (int day = (month == startMonth - 1 ? startDay - 1 : 0); day <= (month == endMonth - 1 ? endDay - 1 : getDaysInMonth(month + 1) - 1); day++){
                for (int hour = 0; hour < 24; hour++) {
                    boolean free = true;
                    for(String userId : userIds ){
                        if(calendar[month][day][hour] != null){
                            for(Activity activity : calendar[month][day][hour][0]){
                                if(activity != null && activity.getUserId().equals(userId)){
                                    free = false;

                                }
                            }
                        }
                        if(!free)
                            break;
                    }
                    }
                }
            }
        return freeSlots;
        }

    }

