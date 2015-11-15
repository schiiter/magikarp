/**
 * Created by michaelzhu on 11/14/15.
 */
public class CircularBuffer {
    private final int size;
    private int count;

    public CircularBuffer(int size) {
        this.size = size;
        this.count = 0;
        String[] messages = new String[size];
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
    }
}
