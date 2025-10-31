package entity;

public abstract class Users {
    private final String userId;
    private final String name;
    private String password;

    protected Users(String userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }

    // Avoid exposing passwords in production; for assignment simplicity:
    public String getPassword() { return password; }
    public void setPassword(String newPwd) { this.password = newPwd; }

    public boolean login(String input) { return password != null && password.equals(input); }
}
