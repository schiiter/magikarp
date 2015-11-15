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
	private int maxMessages;


	public ChatServer(User[] users, int maxMessages) {
		this.users = users;
		this.maxMessages = maxMessages;
		users = new User[1];
		//todo cookie id????????
		users[0] = new User("root", "cs180", null);
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
	 * @param request
	 *            - the full line of the client request (CRLF included)
	 * @return the server response
	 */
	public String parseRequest(String request) {
		// TODO: Replace the following code with the actual code
			String command;

			if(!(request.substring(request.length()-4, request.length()).equals("\r\n"))) {
				return "failure 10";
			}

			String[] temp = request.split("/t");
			command = temp[0];
			String[] parameter = new String[temp.length - 1];

			for(int i = 0; i < temp.length - 1; i++) {
				parameter[i] = temp[i + 1];                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             zcx f
			}

			parameter[parameter.length - 1] = (parameter[parameter.length - 1].split("\r\n"))[0];

			switch (command) {
				case "ADD-USER":
					if(parameter.length != 3) {
						return "failure 10";
					}
					else {
						//todo check login session is authenticated
						addUser(parameter);
					}
				case "USER-LOGIN":
					if(parameter.length != 2) {
						return "failure 10";
					}
					else {
						userLogin(parameter);
					}
				case "POST-MESSAGE":
					if(parameter.length != 2) {
						return "failure 10";
					}
					else {
						//todo check login session is authenticated
						//postMessage(parameter, name);
					}
				case "GET-MESSAGES":
					if(parameter.length != 2) {
						return "failure 10";
					}
					else {
						//todo check login session is authenticated
						getMessages(parameter);
					}
				default:
					return "failure 11";
			}
	}

	public String addUser(String[] args) {
		int count = 0;
		for(int i = 0; i < users.length; i++) {
			if (users[i].getName().equals(args[0])) {
				count++;
			}
		}
		if(count > 0){
			return "failure 22";
		}
		else {
			users = Arrays.copyOf(users, users.length + 1);
			//todo cookie id
			users[users.length - 1] = new User(args[1], args[2], null);
			return "successful";
		}
	}

	public String userLogin(String[] args) {
		for(int i = 0; i < users.length; i++) {
			if((users[i].getName()).equals(args[0])) {
				if(users[i].checkPassword(args[1])) {

					//todo cooooooookie!!!!!!
					users[i].setCookie("");
					return "success";
				}
				else {
					return "failure 21";
				}
			}
		}
		return "failure 20";
	}

	public String postMessage(String[] args, String name) {
		return null;
	}

	public String getMessages(String[] args) {

	}
}
