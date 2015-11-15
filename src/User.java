/**
 * Created by michaelzhu on 11/14/15.
 */
public class User {

    private String username;
    private String password;
    private SessionCookie cookie;

    public User(String username, String password, SessionCookie cookie) {
        this.username = username;
        this.password = password;
        this.cookie = cookie;
    }

    public String getName() {
        return username;
    }

    public SessionCookie getCookie() {
        return cookie;
    }

    public boolean checkPassword(String password) {
        if(password.equals(this.password)) {
            return true;
        }
        else {
            return false;
        }
    }

    public void setCookie(SessionCookie cookie) {
        //todo cookie ?????
        this.cookie = cookie;
    }

}
