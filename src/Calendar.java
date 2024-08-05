import java.io.*;
import java.util.*;

public class Calendar {
    private Activity[][][][][] calendar;

    public Calendar() {
        calendar = new Activity[12][][][][];

        for (int month = 0; month < 12; month++) {
            int daysInMonth = getDaysInMonth(month + 1);
            calendar[month] = new Activity[daysInMonth][24][][];

            for (int day = 0; day < daysInMonth; day++) {
                for (int hour = 0; hour < 24; hour++) {
                    calendar[month][day][hour] = new Activity[1][];
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
                throw new IllegalArgumentException("Invalid month: " + month);
        }
    }

    public void addActivity(int month, int day, int hour, Activity activity) {
        List<Activity> activities = new ArrayList<>();
        if (calendar[month - 1][day - 1][hour][0] != null) {
            for (int i = 0; i < calendar[month - 1][day - 1][hour][0].length; i++) {
                Activity act = calendar[month - 1][day - 1][hour][0][i];
                if (act != null) {
                    activities.add(act);
                }
            }
        }
        activities.add(activity);
        calendar[month - 1][day - 1][hour][0] = new Activity[activities.size()];
        for (int i = 0; i < activities.size(); i++) {
            calendar[month - 1][day - 1][hour][0][i] = activities.get(i);
        }
    }

    public List<Activity> getActivities(int month, int day, int hour) {
        List<Activity> activities = new ArrayList<>();
        if (calendar[month - 1][day - 1][hour][0] != null) {
            for (int i = 0; i < calendar[month - 1][day - 1][hour][0].length; i++) {
                Activity act = calendar[month - 1][day - 1][hour][0][i];
                if (act != null) {
                    activities.add(act);
                }
            }
        }
        return activities;
    }

    public List<Activity> listUserActivities(String userId, int startMonth, int startDay, int endMonth, int endDay) {
        List<Activity> userActivities = new ArrayList<>();
        for (int month = startMonth - 1; month <= endMonth - 1; month++) {
            for (int day = (month == startMonth - 1 ? startDay - 1 : 0); day <= (month == endMonth - 1 ? endDay - 1 : getDaysInMonth(month + 1) - 1); day++) {
                for (int hour = 0; hour < 24; hour++) {
                    if (calendar[month][day][hour][0] != null) {
                        for (Activity activity : calendar[month][day][hour][0]) {
                            if (activity != null && activity.getUserId().equals(userId)) {
                                userActivities.add(activity);
                            }
                        }
                    }
                }
            }
        }
        return userActivities;
    }

    public void removeUserActivities(String userId) {
        for (int month = 0; month < 12; month++) {
            for (int day = 0; day < getDaysInMonth(month + 1); day++) {
                for (int hour = 0; hour < 24; hour++) {
                    if (calendar[month][day][hour][0] != null) {
                        List<Activity> activities = new ArrayList<>();
                        for (Activity activity : calendar[month][day][hour][0]) {
                            if (activity != null && !activity.getUserId().equals(userId)) {
                                activities.add(activity);
                            }
                        }
                        calendar[month][day][hour][0] = activities.toArray(new Activity[0]);
                    }
                }
            }
        }
    }

    public void saveToTheFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int month = 0; month < 12; month++) {
                for (int day = 0; day < getDaysInMonth(month + 1); day++) {
                    for (int hour = 0; hour < 24; hour++) {
                        if (calendar[month][day][hour][0] != null) {
                            for (Activity activity : calendar[month][day][hour][0]) {
                                if (activity != null) {
                                    writer.write((day + 1) + "/" + (month + 1) + "," + hour + "," + (hour + activity.getDuration()) + "," +
                                            activity.getUserId() + "," + "act0" + "," +
                                            activity.getTitle() + "," + activity.getPriority() + "\n");
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadToTheFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                String[] date = tokens[0].split("/");
                int day = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]);
                int startHour = Integer.parseInt(tokens[1]);
                String userId = tokens[3];
                String title = tokens[5];
                float priority = Float.parseFloat(tokens[6]);
                int duration = Integer.parseInt(tokens[2]) - startHour;

                var activity = new Activity(title, priority, userId, duration);
                addActivity(month, day, startHour, activity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Activity> listTopActivities(String userId, int startMonth, int startDay, int endMonth, int endDay, int topN) {
        List<Activity> userActivities = listUserActivities(userId, startMonth, startDay, endMonth, endDay);
        userActivities.sort((a, b) -> Float.compare(b.getPriority(), a.getPriority()));
        return userActivities.subList(0, Math.min(topN, userActivities.size()));
    }



    public List<Activity> listClashingActivities(String userId1, String userId2, int startMonth, int startDay, int endMonth, int endDay) {
        List<Activity> clashingActivities = new ArrayList<>();
        for (int month = startMonth - 1; month <= endMonth - 1; month++) {
            for (int day = (month == startMonth - 1 ? startDay - 1 : 0); day <= (month == endMonth - 1 ? endDay - 1 : getDaysInMonth(month + 1) - 1); day++) {
                for (int hour = 0; hour < 24; hour++) {
                    Set<String> userIds = new HashSet<>();
                    if (calendar[month][day][hour][0] != null) {
                        for (Activity activity : calendar[month][day][hour][0]) {
                            if (activity != null) {
                                userIds.add(activity.getUserId());
                            }
                        }
                    }
                    if (userIds.contains(userId1) && userIds.contains(userId2)) {
                        clashingActivities.addAll(Arrays.asList(calendar[month][day][hour][0]));
                    }
                }
            }
        }
        return clashingActivities;
    }

    public List<String> listFreeSlotsForUsers(String[] userIds, int startMonth, int startDay, int endMonth, int endDay) {
        List<String> freeSlots = new ArrayList<>();
        for (int month = startMonth - 1; month <= endMonth - 1; month++) {
            for (int day = (month == startMonth - 1 ? startDay - 1 : 0); day <= (month == endMonth - 1 ? endDay - 1 : getDaysInMonth(month + 1) - 1); day++) {
                for (int hour = 0; hour < 24; hour++) {
                    boolean allFree = true;
                    for (String userId : userIds) {
                        if (calendar[month][day][hour][0] != null) {
                            for (Activity activity : calendar[month][day][hour][0]) {
                                if (activity != null && activity.getUserId().equals(userId)) {
                                    allFree = false;
                                    break;
                                }
                            }
                        }
                        if (!allFree) break;
                    }
                    if (allFree) {
                        freeSlots.add((month + 1) + "/" + (day + 1) + " " + hour + ":00");
                    }
                }
            }
        }
        return freeSlots;
    }

    public MonthStats getMonthStats(int month) {
        int totalActivities = 0;
        int busiestDay = 0;
        int maxActivitiesInDay = 0;
        int highestPriorityDay = 0;
        int totalDays = getDaysInMonth(month);
        int[] activitiesPerDay = new int[totalDays];
        float[] prioritySumPerDay = new float[totalDays];

        for (int day = 0; day < totalDays; day++) {
            int activitiesInDay = 0;
            float prioritySumInDay = 0;
            for (int hour = 0; hour < 24; hour++) {
                if (calendar[month - 1][day][hour][0] != null) {
                    for (Activity activity : calendar[month - 1][day][hour][0]) {
                        if (activity != null) {
                            activitiesInDay++;
                            prioritySumInDay += activity.getPriority();
                        }
                    }
                }
            }
            activitiesPerDay[day] = activitiesInDay;
            prioritySumPerDay[day] = prioritySumInDay;
            totalActivities += activitiesInDay;

            if (activitiesInDay > maxActivitiesInDay) {
                maxActivitiesInDay = activitiesInDay;
                busiestDay = day + 1;
            }
        }

        float maxAveragePriority = 0;
        for (int day = 0; day < totalDays; day++) {
            float averagePriority = prioritySumPerDay[day] / (activitiesPerDay[day] == 0 ? 1 : activitiesPerDay[day]);
            if (averagePriority > maxAveragePriority) {
                maxAveragePriority = averagePriority;
                highestPriorityDay = day + 1;
            }
        }

        return new MonthStats(totalActivities, totalActivities / totalDays, busiestDay, maxActivitiesInDay, highestPriorityDay, activitiesPerDay[highestPriorityDay - 1]);
    }

    public YearStats getYearStats() {
        int totalActivities = 0;
        int[] activitiesPerMonth = new int[12];

        for (int month = 0; month < 12; month++) {
            for (int day = 0; day < getDaysInMonth(month + 1); day++) {
                for (int hour = 0; hour < 24; hour++) {
                    if (calendar[month][day][hour][0] != null) {
                        activitiesPerMonth[month] += calendar[month][day][hour][0].length;
                        totalActivities += calendar[month][day][hour][0].length;
                    }
                }
            }
        }

        int busiestMonth = 0;
        int maxActivitiesInMonth = 0;
        for (int month = 0; month < 12; month++) {
            if (activitiesPerMonth[month] > maxActivitiesInMonth) {
                maxActivitiesInMonth = activitiesPerMonth[month];
                busiestMonth = month + 1;
            }
        }

        return new YearStats(totalActivities, totalActivities / 12, busiestMonth, maxActivitiesInMonth);
    }

    public static class MonthStats {
        private final int totalActivities;
        private final int averageActivitiesPerDay;
        private final int busiestDay;
        private final int maxActivitiesInDay;
        private final int highestPriorityDay;
        private final int activitiesInHighestPriorityDay;

        public MonthStats(int totalActivities, int averageActivitiesPerDay, int busiestDay, int maxActivitiesInDay, int highestPriorityDay, int activitiesInHighestPriorityDay) {
            this.totalActivities = totalActivities;
            this.averageActivitiesPerDay = averageActivitiesPerDay;
            this.busiestDay = busiestDay;
            this.maxActivitiesInDay = maxActivitiesInDay;
            this.highestPriorityDay = highestPriorityDay;
            this.activitiesInHighestPriorityDay = activitiesInHighestPriorityDay;
        }

        public int getTotalActivities() {
            return totalActivities;
        }

        public int getAverageActivitiesPerDay() {
            return averageActivitiesPerDay;
        }

        public int getBusiestDay() {
            return busiestDay;
        }

        public int getMaxActivitiesInDay() {
            return maxActivitiesInDay;
        }

        public int getHighestPriorityDay() {
            return highestPriorityDay;
        }

        public int getActivitiesInHighestPriorityDay() {
            return activitiesInHighestPriorityDay;
        }
    }

    public static class YearStats {
        private final int totalActivities;
        private final int averageActivitiesPerMonth;
        private final int busiestMonth;
        private final int maxActivitiesInMonth;

        public YearStats(int totalActivities, int averageActivitiesPerMonth, int busiestMonth, int maxActivitiesInMonth) {
            this.totalActivities = totalActivities;
            this.averageActivitiesPerMonth = averageActivitiesPerMonth;
            this.busiestMonth = busiestMonth;
            this.maxActivitiesInMonth = maxActivitiesInMonth;
        }

        public int getTotalActivities() {
            return totalActivities;
        }

        public int getAverageActivitiesPerMonth() {
            return averageActivitiesPerMonth;
        }

        public int getBusiestMonth() {
            return busiestMonth;
        }

        public int getMaxActivitiesInMonth() {
            return maxActivitiesInMonth;
        }
    }
}
