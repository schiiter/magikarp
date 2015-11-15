/**
 * Created by michaelzhu on 11/14/15.
 */
public class CircularBuffer {
    private final int size;
    private int count = 0;
    private int numStamp = 0;
    private String[] messages;
    private int numAvailable = 0;

    public CircularBuffer(int size) {
        this.size = size;
        this.count = 0;
        messages = new String[size];
    }

    public void put(String message) {
        String addon;
        addon = Integer.toString(numStamp);

        if(numStamp < 9999) {
            numStamp++;
        } else {
            numStamp = 0;
        }
        count++;

        if (numAvailable < size) numAvailable++;

        while (addon.length() < 4) {
            addon = "0" + addon;
        }

        messages[count % size] = addon + ") " + message;
    }

    public String[] getNewest(int numMessages) {
        int newestMessagesSize = Math.min(numMessages, numAvailable);
        String[] newestMessages = new String[newestMessagesSize];
        int pos = count % size;

        //Null checker and case of 0
        if (numMessages < 0) return null;
        if (numMessages == 0) return newestMessages;

        int i = pos;
        int newestMessagesIndex = newestMessagesSize - 1;

        while (newestMessagesIndex >= 0 && messages[i] != null) {
            newestMessages[newestMessagesIndex] = messages[i];
            i--;
            if (i == -1) {
                i = size - 1;
            }
            newestMessagesIndex--;
        }
        return newestMessages;
    }
//        if (numMessages < numAvailable){
//            int i = pos;
//            int newestMessagesIndex = newestMessagesSize - 1;
//            while (newestMessagesIndex >= 0 && messages[i] != null) {
//                newestMessages[newestMessagesIndex] = messages[i];
//                i--;
//                if (i == -1) {
//                    i = size - 1;
//                }
//                newestMessagesIndex--;
//            }
}
