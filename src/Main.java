import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var calendar = new Calendar();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Add Activity: ");
            System.out.println("2. List Activities for user: ");
            System.out.println("3. List the 5 most important activities of a given user during a time: ");
            System.out.println("4. List all the clashing activities during a time period: ");
            System.out.println("5. List free slots of the users: ");
            System.out.println("6. Print month stats: ");
            System.out.println("7. Print Year Stats: ");
            System.out.println("8. Remove a user from the calendar");
            System.out.println("9. Save the calendar: ");
            System.out.println("10. Load the Calendar: ");
            System.out.println("11. Exit");

            System.out.println("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Enter Month (1-12): ");
                    int month = scanner.nextInt();
                    System.out.println("Enter Day: ");
                    int day = scanner.nextInt();
                    System.out.println("Enter Hour: ");
                    int hour = scanner.nextInt();
                    scanner.nextLine();  // Consume newline left-over
                    System.out.println("Enter Title: ");
                    String title = scanner.nextLine();
                    System.out.println("Enter priority: ");
                    float priority = scanner.nextFloat();
                    scanner.nextLine();  // Consume newline left-over
                    System.out.println("Enter UserID: ");
                    String userId = scanner.nextLine();
                    System.out.println("Enter Duration: ");
                    int duration = scanner.nextInt();
                    scanner.nextLine();  // Consume newline left-over

                    var activity = new Activity(title, priority, userId, duration);
                    calendar.addActivity(month, day, hour, activity);
                    break;
                case 2:
                    System.out.println("Enter User Id: ");
                    userId = scanner.nextLine();
                    System.out.println("Start Month: ");
                    int startMonth = scanner.nextInt();
                    System.out.println("Start Day: ");
                    int startDay = scanner.nextInt();
                    System.out.println("End Month: ");
                    int endMonth = scanner.nextInt();
                    System.out.println("End Day");
                    int endDay = scanner.nextInt();
                    scanner.nextLine();

                    List<Activity> userActivities = calendar.listUserActivities(userId, startMonth, startDay, endMonth, endDay);
                    for (Activity act : userActivities) {
                        System.out.println(act);
                    }
                    break;
                case 3:
                    System.out.println("Enter user ID:");
                    userId = scanner.nextLine();
                    System.out.println("Enter start month (1-12):");
                    startMonth = scanner.nextInt();
                    System.out.println("Enter start day:");
                    startDay = scanner.nextInt();
                    System.out.println("Enter end month (1-12):");
                    endMonth = scanner.nextInt();
                    System.out.println("Enter end day:");
                    endDay = scanner.nextInt();
                    scanner.nextLine();

                    List<Activity> topActivities = calendar.listTopActivities(userId, startMonth, startDay, endMonth, endDay, 5);
                    for (Activity act : topActivities) {
                        System.out.println(act);
                    }
                    break;
                case 4:
                    System.out.println("Enter User Id 1: ");
                    String userId1 = scanner.nextLine();
                    System.out.println("Enter User Id 2: ");
                    String userId2 = scanner.nextLine();
                    System.out.println("Enter Start Month: ");
                    startMonth = scanner.nextInt();
                    System.out.println("Enter Start Day: ");
                    startDay = scanner.nextInt();
                    System.out.println("Enter End Month: ");
                    endMonth = scanner.nextInt();
                    System.out.println("Enter End Day: ");
                    endDay = scanner.nextInt();
                    scanner.nextLine();

                    List<Activity> clashingActivities = calendar.listClashingActivities(userId1, userId2, startMonth, startDay, endMonth, endDay);
                    for (Activity act : clashingActivities) {
                        System.out.println(act);
                    }
                    break;
                case 5:
                    System.out.println("Enter number of users: ");
                    int numberOfUsers = scanner.nextInt();
                    scanner.nextLine();
                    String[] userIds = new String[numberOfUsers];
                    for (int i = 0; i < numberOfUsers; i++) {
                        System.out.println("Enter User Id: " + (i+1));
                        userIds[i] = scanner.nextLine();
                    }
                    System.out.println("Enter Start Month");
                    startMonth = scanner.nextInt();
                    System.out.println("Enter Start Day");
                    startDay = scanner.nextInt();
                    System.out.println("Enter End Month");
                    endMonth = scanner.nextInt();
                    System.out.println("Enter End Day");
                    endDay = scanner.nextInt();
                    scanner.nextLine();  // Consume newline left-over

                    List<String> freeSlots = calendar.listFreeSlotsForUsers(userIds, startMonth, startDay, endMonth, endDay);
                    for (String freeSlot : freeSlots) {
                        System.out.println(freeSlot);
                    }
                    break;
                case 6:
                    System.out.println("Enter month (1-12):");
                    month = scanner.nextInt();
                    Calendar.MonthStats monthStats = calendar.getMonthStats(month);
                    System.out.println("Total Activities: " + monthStats.getTotalActivities());
                    System.out.println("Average Activities Per Day: " + monthStats.getAverageActivitiesPerDay());
                    System.out.println("Busiest Day: " + monthStats.getBusiestDay());
                    System.out.println("Max Activities in a Day: " + monthStats.getMaxActivitiesInDay());
                    System.out.println("Highest Priority Day: " + monthStats.getHighestPriorityDay());
                    System.out.println("Activities in Highest Priority Day: " + monthStats.getActivitiesInHighestPriorityDay());
                    break;
                case 7:
                    Calendar.YearStats yearStats = calendar.getYearStats();
                    System.out.println("Total Activities: " + yearStats.getTotalActivities());
                    System.out.println("Average Activities Per Month: " + yearStats.getAverageActivitiesPerMonth());
                    System.out.println("Busiest Month: " + yearStats.getBusiestMonth());
                    System.out.println("Max Activities in a Month: " + yearStats.getMaxActivitiesInMonth());
                    break;
                case 8:
                    System.out.println("Enter User Id: ");
                    userId = scanner.nextLine();
                    calendar.removeUserActivities(userId);
                    break;

                case 9:
                    System.out.println("Calendar.dat");
                    String saveFileName = scanner.nextLine();
                    calendar.saveToTheFile(saveFileName);
                    break;

                case 10:
                    System.out.println("Calendar.dat");
                    String loadFileName = scanner.nextLine();
                    calendar.loadToTheFile(loadFileName);
                    break;
                     case 11:
                         return;

                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
}