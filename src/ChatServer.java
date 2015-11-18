import java.util.*;

/**
 * <b> CS 180 - Project 4 - Chat Server Skeleton </b>
 * <p>
 * 
 * This is the skeleton code for the ChatServer Class. This is a private chat
 * server for you and your friends to communicate.
 * 
 * @author (Your Name) <(YourEmail@purdue.edu)>
 * 
 * @lab (Your Lab Section)
 * 
 * @version (Today's Date)
 *
 */
public class ChatServer {
	private User[] users;
	private CircularBuffer cr;

	public ChatServer(User[] users, int maxMessages) {
        if (users == null || users.length == 0) {
            this.users = new User[1];
            this.users[0] = new User("root", "cs180", null);
        } else {
            this.users = new User[0];
            int n = 0;
            for (int i = 0; i < users.length; i++) {
                if (users[i] != null) {
                    this.users = Arrays.copyOf(this.users, this.users.length + 1);
                    this.users[n] = users[i];
                    n++;
                }
            }

            int j = findUserIndex("root");
            if (j == -1) {
                this.users = Arrays.copyOf(this.users, this.users.length + 1);
                this.users[this.users.length - 1] = new User("root", "cs180", null);
            }

//		this.users = Arrays.copyOf(users, users.length + 1);
//		this.users[0] = new User("root", "cs180", null);
            cr = new CircularBuffer(maxMessages);
        }
	}

	/**
	 * This method begins server execution.
	 */
	public void run() {
		boolean verbose = false;
		System.out.printf("The VERBOSE option is off.\n\n");
		Scanner in = new Scanner(System.in);

		while (true) {
			System.out.printf("Input Server Request: ");
			String command = in.nextLine();

			// this allows students to manually place "\r\n" at end of command
			// in prompt
			command = replaceEscapeChars(command);

			if (command.startsWith("kill"))
				break;

			if (command.startsWith("verbose")) {
				verbose = !verbose;
				System.out.printf("VERBOSE has been turned %s.\n\n", verbose ? "on" : "off");
				continue;
			}

			String response = null;
			try {
				response = parseRequest(command);
			} catch (Exception ex) {
				response = MessageFactory.makeErrorMessage(MessageFactory.UNKNOWN_ERROR,
						String.format("An exception of %s occurred.", ex.getMessage()));
			}

			// change the formatting of the server response so it prints well on
			// the terminal (for testing purposes only)
			if (response.startsWith("SUCCESS\t"))
				response = response.replace("\t", "\n");

			// print the server response
			if (verbose)
				System.out.printf("response:\n");
			System.out.printf("\"%s\"\n\n", response);
		}

		in.close();
	}

	/**
	 * Replaces "poorly formatted" escape characters with their proper values.
	 * For some terminals, when escaped characters are entered, the terminal
	 * includes the "\" as a character instead of entering the escape character.
	 * This function replaces the incorrectly inputed characters with their
	 * proper escaped characters.
	 * 
	 * @param str
	 *            - the string to be edited
	 * @return the properly escaped string
	 */
	private static String replaceEscapeChars(String str) {
		str = str.replace("\\r", "\r");
		str = str.replace("\\n", "\n");
		str = str.replace("\\t", "\t");

		return str;
	}

	/**
	 * Determines which client command the request is using and calls the
	 * function associated with that command.
	 * 
	 * @param oldRequest
	 *            - the full line of the client request (CRLF included)
	 * @return the server response
	 */
	public String parseRequest(String oldRequest) {
		String request = replaceEscapeChars(oldRequest);
//		String command;

		if(!(request.substring(request.length()-2, request.length()).equals("\r\n"))) {
			return MessageFactory.makeErrorMessage(10);
		}
		String[] parameter = request.split("\t");
//		command = temp[0];
//		String[] parameter = new String[temp.length];
//
//		for(int i = 0; i < temp.length - 1; i++) {
//			parameter[i] = temp[i];
//		}

		parameter[parameter.length - 1] = (parameter[parameter.length - 1].split("\r\n"))[0];

		switch (parameter[0]) {
			case "ADD-USER":
				if (parameter.length != 4) {
					return MessageFactory.makeErrorMessage(10);
				} else {
					int i = findUserIndex(Integer.parseInt(parameter[1]));
					//if the user that wants to create account is currently logged in
					if (users[i].getCookie().getID() == Integer.parseInt(parameter[1])) {
						if (!users[i].getCookie().hasTimedOut()) {
							return addUser(parameter);
						} else {
							users[i].setCookie(null);
							return MessageFactory.makeErrorMessage(5);
						}
					}
				}
			case "USER-LOGIN":
				if (parameter.length != 3) {
					return MessageFactory.makeErrorMessage(10);
				} else {
					return userLogin(parameter);
				}
			case "POST-MESSAGE":
				if (parameter.length != 3) {
					return MessageFactory.makeErrorMessage(10);
				} else {
					int i = findUserIndex(Integer.parseInt(parameter[1]));
					//if the user that wants to create account is currently logged in
					if (users[i].getCookie().getID() == Integer.parseInt(parameter[1])) {
						if (!users[i].getCookie().hasTimedOut()) {
							return postMessage(parameter, users[i].getName());
						} else {
							users[i].setCookie(null);
							return MessageFactory.makeErrorMessage(5);
						}
					}
				}
			case "GET-MESSAGES":
				if (parameter.length != 3) {
					return MessageFactory.makeErrorMessage(10);
				} else {
					int i = findUserIndex(Integer.parseInt(parameter[1]));
					//if the user that wants to create account is currently logged in
					if (users[i].getCookie().getID() == Integer.parseInt(parameter[1])) {
						if (!users[i].getCookie().hasTimedOut()) {
							return getMessages(parameter);
						} else {
							users[i].setCookie(null);
							return MessageFactory.makeErrorMessage(5);
						}
					}
				}
			default:
				return MessageFactory.makeErrorMessage(11);
		}
	}

	public String addUser(String[] args) {
        int i = findUserIndex(args[2]);
        if (i != -1) return MessageFactory.makeErrorMessage(22);

        users = Arrays.copyOf(users, users.length + 1);
        users[users.length - 1] = new User(args[2], args[3], null);
        return "SUCCESS\r\n";
	}

	public String userLogin(String[] args) {
        int i = findUserIndex(args[1]);
        if (i == -1) return MessageFactory.makeErrorMessage(20);
        if (users[i].checkPassword(args[2])) {
            SessionCookie sc = new SessionCookie(cookieIDgen());
			users[i].setCookie(sc);
            return String.format("SUCCESS\t%d\r\n", sc.getID());
        } else {
            return MessageFactory.makeErrorMessage(21);
        }
	}

	public String postMessage(String[] args, String name) {
        String s = args[2].replace(" ", "");
        if (s.equals("")) {
            return "FAILURE\t024\t\r\n";
        }
		String modMessage = name + ": " + args[2];
		cr.put(modMessage);
		int i = findUserIndex(name);
        if (i == -1) return MessageFactory.makeErrorMessage(20);
        users[i].setCookie(new SessionCookie(Integer.parseInt(args[0])));
		return "SUCCESS\r\n";
	}

	public String getMessages(String[] args) {
		//todo check invalid input
		if(Integer.parseInt(args[2]) < 1) {
			return MessageFactory.makeErrorMessage(24);
		}
        String[] out = cr.getNewest(Integer.parseInt(args[1]));
		String result = "SUCCESS"; //"SUCCESS\tmessage1\tmessage2\tmessage3\r\n""
		for (int i = 0; i < out.length; i++) {
			result = result + "\t" + out[i];
		}
		result = result + "\r\n";
		return result;
	}

    public int findUserIndex(long cookieID) {
        for (int i = 0; i < users.length; i++) {
			if (users[i].getCookie() != null) {
				if (users[i].getCookie().getID() == cookieID) return i;
			}
        }
        return -1;
    }

    public int findUserIndex(String name) {
        for (int i = 0; i < users.length; i++) {
			if(users[i] != null) {
				if (users[i].getName().equals(name)) return i;
			}
        }
        return -1;
    }

	public int cookieIDgen() {
		int random;
		do {
			random = (int) (Math.random() * 10000);
		} while (findUserIndex(random) != -1);
		return random;
	}
}
