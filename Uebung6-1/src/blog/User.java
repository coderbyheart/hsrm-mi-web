package blog;

public class User {
	
	public static final int NOT_LOGGED_IN = 1;
	public static final int EMAIL_UNKNOWN = 2;
	public static final int PASSWORD_MISMATCH = 3;
	
	private String email;
	private String passwordHash;
	private String passwordSeed;
	private boolean loggedIn = false;
	private int loginState = NOT_LOGGED_IN;

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setPasswordSeed(String passwordSeed) {
		this.passwordSeed = passwordSeed;
	}

	public String getPasswordSeed() {
		return passwordSeed;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoginState(int loginState) {
		this.loginState = loginState;
	}

	public int getLoginState() {
		return loginState;
	}
}
