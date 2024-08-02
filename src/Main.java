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
            System.out.println("4. Print the longest free period: ");
            System.out.println("5. List all the clashing activities during a time period: ");
            System.out.println("6. List free slots of the users: ");
            System.out.println("7. Print month stats: ");
            System.out.println("8. Print Year Stats: ");
            System.out.println("9. Remove a user from the calendar");
            System.out.println("10. Save the calendar: ");
            System.out.println("11. Load the Calendar: ");
            System.out.println("12. Exit");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter Month (1-12): ");
                    int month = scanner.nextInt();
                    System.out.println("Enter Day: ");
                    int day = scanner.nextInt();
                    System.out.println("Enter Hour: ");
                    int hour = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter Title: ");
                    String title = scanner.nextLine();
                    System.out.println("Enter priority: ");
                    float priority = scanner.nextFloat();
                    System.out.println("Enter UserID: ");
                    String userId = scanner.nextLine();
                    System.out.println("Enter Duration: ");
                    int duration = scanner.nextInt();

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

                    var userActivities = calendar.listUserActivities(userId, startMonth, startDay, endMonth, endDay);
                    for (Activity act : userActivities) {
                        System.out.println(act);
                    }
                    break;
                case 5:
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

                    var clashingActivities = calendar.listClashingActivities(userId1, userId2, startMonth, startDay, endMonth, endDay);
                    for (Activity act : clashingActivities) {
                        System.out.println(act);
                    }
                    break;
                case 9:
                    System.out.println("Enter User Id: ");
                    userId = scanner.nextLine();
                    calendar.removeUserActivities(userId);
                    break;

            }
        }
    }
}