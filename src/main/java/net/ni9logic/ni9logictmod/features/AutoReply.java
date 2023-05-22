package net.ni9logic.ni9logictmod.features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoReply {
    private static class Person {
        private String personName;
        private String personMessage;
        private int timesMessaged;

        public void setPersonData(String pname, String pmsg, int timesMessaged) {
            this.personMessage = pmsg;
            this.personName = pname;
            this.timesMessaged = timesMessaged;
        }

        public String getPersonName() {
            return personName;
        }

        public String getPersonMessage() {
            return personMessage;
        }

        public int getTimesMessaged() {
            return timesMessaged;
        }

        public void incrementTimesMessaged() {
            timesMessaged++;
        }
    }

    static List<Person> personList = new ArrayList<>();
    HashMap<String, String> queryReply = new HashMap<>();

    public static void playAutoReply(String message) {
        if (message.contains(" -> me]")) {
            Pattern pattern = Pattern.compile("\"\\\\[(\\\\w+)\\\\s*->\\\\s*me\\\\]\\\\s*(.*)\"");
            Matcher matcher = pattern.matcher(message);

            if (matcher.find()) {
                String personName = matcher.group(1);
                String personMessage = matcher.group(2);

                if (!isPersonAlreadyExist(personName)) {
                    Person p = new Person();
                    p.setPersonData(personName, personMessage, 1);
                    personList.add(p);
                }
            }
        }
    }

    public static boolean isPersonAlreadyExist(String personName) {
        for (Person p : personList) {
            if (p.getPersonName().equals(personName)) {
                return true;
            }
        }
        return false;
    }

    public void readyQueryReply() {
        queryReply.put("Hey, How are you?", "I'm doing fine how are you?");
        queryReply.put("Hey, you there?", "Yah");
    }
}
