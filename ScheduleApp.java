package assignment;

import java.util.*;

public class ScheduleApp {

    // Days and shifts
    static String[] days = {
            "Monday","Tuesday","Wednesday",
            "Thursday","Friday","Saturday","Sunday"
    };

    static String[] shifts = {
            "Morning","Afternoon","Evening"
    };

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        // employee -> (day -> preference)
        Map<String, Map<String,String>> preferences = new HashMap<>();

        // employee -> worked days count
        Map<String,Integer> workDays = new HashMap<>();

        System.out.print("Enter number of employees: ");
        int n = sc.nextInt();
        sc.nextLine();

        // ================= INPUT =================
        for(int i=0;i<n;i++){

            System.out.print("Employee name: ");
            String name = sc.nextLine();

            Map<String,String> pref = new HashMap<>();

            for(String day : days){
                System.out.print(name +
                        "'s preferred shift on " + day + ": ");
                pref.put(day, sc.nextLine());
            }

            preferences.put(name, pref);
            workDays.put(name, 0);
        }

        // ================= SCHEDULE STRUCTURE =================
        Map<String, Map<String, List<String>>> schedule =
                new HashMap<>();

        for(String day : days){
            Map<String,List<String>> shiftMap =
                    new HashMap<>();

            for(String shift : shifts)
                shiftMap.put(shift,new ArrayList<>());

            schedule.put(day,shiftMap);
        }

        // ================= ASSIGN PREFERRED SHIFTS =================
        for(String emp : preferences.keySet()){

            for(String day : days){

                if(workDays.get(emp) >= 5)
                    continue; // prefer limit

                String pref =
                        preferences.get(emp).get(day);

                boolean assigned = false;

                for(String s : shifts){
                    if(schedule.get(day)
                            .get(s).contains(emp)){
                        assigned = true;
                        break;
                    }
                }

                if(!assigned){
                    schedule.get(day)
                            .get(pref)
                            .add(emp);

                    workDays.put(emp,
                            workDays.get(emp)+1);
                }
            }
        }

        // ================= ENSURE MINIMUM STAFF =================
        for(String day : days){

            for(String shift : shifts){

                while(schedule.get(day)
                        .get(shift).size() < 2){

                    List<String> candidates =
                            new ArrayList<>();

                    for(String emp : workDays.keySet()){

                        boolean free = true;

                        for(String s : shifts){
                            if(schedule.get(day)
                                    .get(s).contains(emp)){
                                free = false;
                                break;
                            }
                        }

                        if(free)
                            candidates.add(emp);
                    }

                    if(candidates.isEmpty())
                        break;

                    String chosen =
                            candidates.get(
                                    rand.nextInt(
                                            candidates.size()));

                    schedule.get(day)
                            .get(shift)
                            .add(chosen);

                    workDays.put(chosen,
                            workDays.get(chosen)+1);
                }
            }
        }

        // ================= OUTPUT =================
        System.out.println(
                "\n===== FINAL WEEKLY SCHEDULE =====");

        for(String day : days){

            System.out.println("\n" + day);

            for(String shift : shifts){
                System.out.println(
                        "  " + shift + ": "
                                + schedule.get(day)
                                .get(shift));
            }
        }

        sc.close();
    }
}