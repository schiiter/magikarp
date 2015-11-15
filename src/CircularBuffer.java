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
        addon = Integer.toString(count);
        while (addon.length() < 4) {
            addon = "0" + addon;
        }
        if(count < 9999) {
            count++;
        }
        else {
            count = 0;
        }


        messages.add(0, addon + message);
        if(messages.size() > size) {
            messages.remove(size);
        }
    }

    public String[] getNewest(int numMessages) {
        return null;
    }
}
