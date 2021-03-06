/**
 * Created by michaelzhu on 11/14/15.
 */
public class SessionCookie {

    public static int timeoutLength = 300; //seconds

    private long id;
    private long lastActivityTime;

    public SessionCookie(long id) {
        this.id = id;
        this.lastActivityTime = System.currentTimeMillis();
    }

    public boolean hasTimedOut() {
        if (System.currentTimeMillis() - lastActivityTime >= timeoutLength * 1000) {
            return true;
        }
        return false;
    }

    public long getID() {
        return this.id;
    }

    public void updateTimeOfActivity() {
        this.lastActivityTime = System.currentTimeMillis();
    }


}
