import java.io.*;
import java.util.*;

public class Calendar {
    private Activity[][][][][] calendar;

    public Calendar() {
        calendar = new Activity[12][][][][];

        for (int month = 0; month < 12; month++) {
            int days = daysInMonth(month + 1);
            calendar[month] = new Activity[days][][][];

            for (int day = 0; day < days; day++) {
                calendar[month][day] = new Activity[24][][];

                for (int hour = 0; hour < 24; hour++) {
                    calendar[month][day][hour] = new Activity[1][];
                    calendar[month][day][hour][0] = new Activity[0];
                }
            }
        }
    }

    public void addActivity(int month, int day, int hour, Activity activity) {
        if (hour + activity.getDuration() > 24) {
            throw new IllegalArgumentException("Activity duration exceeds the day limit.");
        }
        for (int i = hour; i < hour + activity.getDuration(); i++) {
            calendar[month - 1][day - 1][i][0] = addToArray(calendar[month - 1][day - 1][i][0], activity);
        }
    }

    private Activity[] addToArray(Activity[] array, Activity element) {
        Activity[] newArray = new Activity[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = element;
        return newArray;
    }

    public List<Activity> listUserActivities(String userId, int startMonth, int startDay, int endMonth, int endDay) {
        List<Activity> activities = new ArrayList<>();

        for (int month = startMonth - 1; month <= endMonth - 1; month++) {
            for (int day = (month == startMonth - 1 ? startDay - 1 : 0); day < (month == endMonth - 1 ? endDay : calendar[month].length); day++) {
                for (int hour = 0; hour < 24; hour++) {
                    for (Activity act : calendar[month][day][hour][0]) {
                        if (act.getUserId().equals(userId)) {
                            activities.add(act);
                        }
                    }
                }
            }
        }
        return activities;
    }

    public List<Activity> listTopActivities(String userId, int startMonth, int startDay, int endMonth, int endDay, int topN) {
        List<Activity> activities = listUserActivities(userId, startMonth, startDay, endMonth, endDay);
        activities.sort(Comparator.comparing(Activity::getPriority).reversed());
        return activities.subList(0, Math.min(topN, activities.size()));
    }

    public List<Activity> listClashingActivities(String userId1, String userId2, int startMonth, int startDay, int endMonth, int endDay) {
        List<Activity> clashingActivities = new ArrayList<>();

        for (int month = startMonth - 1; month <= endMonth - 1; month++) {
            for (int day = (month == startMonth - 1 ? startDay - 1 : 0); day < (month == endMonth - 1 ? endDay : calendar[month].length); day++) {
                for (int hour = 0; hour < 24; hour++) {
                    List<Activity> user1Activities = new ArrayList<>();
                    List<Activity> user2Activities = new ArrayList<>();

                    for (Activity act : calendar[month][day][hour][0]) {
                        if (act.getUserId().equals(userId1)) {
                            user1Activities.add(act);
                        } else if (act.getUserId().equals(userId2)) {
                            user2Activities.add(act);
                        }
                    }

                    if (!user1Activities.isEmpty() && !user2Activities.isEmpty()) {
                        clashingActivities.addAll(user1Activities);
                        clashingActivities.addAll(user2Activities);
                    }
                }
            }
        }
        return clashingActivities;
    }

    public List<String> listFreeSlotsForUsers(String[] userIds, int startMonth, int startDay, int endMonth, int endDay) {
        List<String> freeSlots = new ArrayList<>();

        for (int month = startMonth - 1; month <= endMonth - 1; month++) {
            for (int day = (month == startMonth - 1 ? startDay - 1 : 0); day < (month == endMonth - 1 ? endDay : calendar[month].length); day++) {
                for (int hour = 0; hour < 24; hour++) {
                    boolean slotFree = true;

                    for (String userId : userIds) {
                        for (Activity act : calendar[month][day][hour][0]) {
                            if (act.getUserId().equals(userId)) {
                                slotFree = false;
                                break;
                            }
                        }
                        if (!slotFree) {
                            break;
                        }
                    }

                    if (slotFree) {
                        freeSlots.add("Month: " + (month + 1) + ", Day: " + (day + 1) + ", Hour: " + hour);
                    }
                }
            }
        }
        return freeSlots;
    }

    public void removeUserActivities(String userId) {
        for (int month = 0; month < 12; month++) {
            for (int day = 0; day < calendar[month].length; day++) {
                for (int hour = 0; hour < 24; hour++) {
                    calendar[month][day][hour][0] = Arrays.stream(calendar[month][day][hour][0])
                            .filter(act -> !act.getUserId().equals(userId))
                            .toArray(Activity[]::new);
                }
            }
        }
    }

    public void saveToTheFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int month = 0; month < 12; month++) {
                for (int day = 0; day < calendar[month].length; day++) {
                    for (int hour = 0; hour < 24; hour++) {
                        for (Activity act : calendar[month][day][hour][0]) {
                            writer.write((day + 1) + "/" + (month + 1) + "," + hour + "," + (hour + act.getDuration()) + ","
                                    + act.getUserId() + "," + act.getTitle() + "," + act.getPriority());
                            writer.newLine();
                        }
                    }
                }
            }
            System.out.println("Calendar saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadToTheFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String[] dateParts = parts[0].split("/");
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int startHour = Integer.parseInt(parts[1]);
                int endHour = Integer.parseInt(parts[2]);
                String userId = parts[3];
                String title = parts[4];
                float priority = Float.parseFloat(parts[5]);
                int duration = endHour - startHour;

                Activity activity = new Activity(title, priority, userId, duration);
                addActivity(month, day, startHour, activity);
            }
            System.out.println("Calendar loaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MonthStats getMonthStats(int month) {
        int totalActivities = 0;
        int maxActivitiesInDay = 0;
        int busiestDay = 0;
        int highestPriorityDay = 0;
        float highestPriority = 0;

        for (int day = 0; day < calendar[month - 1].length; day++) {
            int dailyActivities = 0;
            float dailyPriority = 0;

            for (int hour = 0; hour < 24; hour++) {
                dailyActivities += calendar[month - 1][day][hour][0].length;
                for (Activity act : calendar[month - 1][day][hour][0]) {
                    dailyPriority += act.getPriority();
                }
            }

            if (dailyActivities > maxActivitiesInDay) {
                maxActivitiesInDay = dailyActivities;
                busiestDay = day + 1;
            }

            if (dailyPriority > highestPriority) {
                highestPriority = dailyPriority;
                highestPriorityDay = day + 1;
            }

            totalActivities += dailyActivities;
        }

        return new MonthStats(totalActivities, maxActivitiesInDay, busiestDay, highestPriorityDay, highestPriority);
    }

    public YearStats getYearStats() {
        int totalActivities = 0;
        int maxActivitiesInMonth = 0;
        int busiestMonth = 0;

        for (int month = 0; month < 12; month++) {
            int monthlyActivities = 0;

            for (int day = 0; day < calendar[month].length; day++) {
                for (int hour = 0; hour < 24; hour++) {
                    monthlyActivities += calendar[month][day][hour][0].length;
                }
            }

            if (monthlyActivities > maxActivitiesInMonth) {
                maxActivitiesInMonth = monthlyActivities;
                busiestMonth = month + 1;
            }

            totalActivities += monthlyActivities;
        }

        return new YearStats(totalActivities, maxActivitiesInMonth, busiestMonth);
    }

    private static int daysInMonth(int month) {
        return switch (month) {
            case 4, 6, 9, 11 -> 30;
            case 2 -> 28;
            default -> 31;
        };
    }

    public static class MonthStats {
        private int totalActivities;
        private int maxActivitiesInDay;
        private int busiestDay;
        private int highestPriorityDay;
        private float activitiesInHighestPriorityDay;

        public MonthStats(int totalActivities, int maxActivitiesInDay, int busiestDay, int highestPriorityDay, float activitiesInHighestPriorityDay) {
            this.totalActivities = totalActivities;
            this.maxActivitiesInDay = maxActivitiesInDay;
            this.busiestDay = busiestDay;
            this.highestPriorityDay = highestPriorityDay;
            this.activitiesInHighestPriorityDay = activitiesInHighestPriorityDay;
        }

        public int getTotalActivities() {
            return totalActivities;
        }

        public int getMaxActivitiesInDay() {
            return maxActivitiesInDay;
        }

        public int getBusiestDay() {
            return busiestDay;
        }

        public int getHighestPriorityDay() {
            return highestPriorityDay;
        }

        public float getActivitiesInHighestPriorityDay() {
            return activitiesInHighestPriorityDay;
        }

        public float getAverageActivitiesPerDay() {
            return totalActivities / (float) daysInMonth(1);
        }
    }

    public static class YearStats {
        private int totalActivities;
        private int maxActivitiesInMonth;
        private int busiestMonth;

        public YearStats(int totalActivities, int maxActivitiesInMonth, int busiestMonth) {
            this.totalActivities = totalActivities;
            this.maxActivitiesInMonth = maxActivitiesInMonth;
            this.busiestMonth = busiestMonth;
        }

        public int getTotalActivities() {
            return totalActivities;
        }

        public int getMaxActivitiesInMonth() {
            return maxActivitiesInMonth;
        }

        public int getBusiestMonth() {
            return busiestMonth;
        }

        public float getAverageActivitiesPerMonth() {
            return totalActivities / 12.0f;
        }
    }
}
