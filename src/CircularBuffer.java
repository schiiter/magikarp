import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelzhu on 11/14/15.
 */
public class CircularBuffer {
    private final int size;
    private int count;
    private List<String> messages;

    public CircularBuffer(int size) {
        this.size = size;
        this.count = 0;
        this.messages = new ArrayList<>();
    }

    public void put(String message) {
        String addon;
        String output;
        addon = Integer.toString(count);
        while (addon.length() < 4) {
            addon = "0" + addon;
        }
        if (count < 9999) {
            count++;
        } else {
            count = 0;
        }

        output = addon + ") " + message;
        messages.add(0, output);
        if (messages.size() > size) {
            messages.remove(size);
        }
    }

    public String[] getNewest(int numMessages) {
        int numAvailable = messages.size();
        int newestMessagesSize = Math.min(numMessages, numAvailable);
        String[] newestMessages = new String[newestMessagesSize];
        //todo
        int pos = count % size;

        //Null checker and case of 0
        if (numMessages < 0) return null;
        if (numMessages == 0) return newestMessages;

        for (int i = 0; i < newestMessagesSize; i++) {
            newestMessages[i] = messages.get(i);
        }
        return newestMessages;
    }
}