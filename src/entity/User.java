package entity;

import filter.CompositeFilter;


public abstract class User {
    private final String userId;
    private final String name;
    private String password;
    private CompositeFilter<Internship> filter;
    
    public User(String userId, String name, String password) {
        if (userId == null || name == null || password == null) {
            throw new IllegalArgumentException("userId, name, and password must not be null");
        }
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.filter = null;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
 
    public void setPassword(String newPwd) {
        if (newPwd == null) throw new IllegalArgumentException("newPwd must not be null");
        this.password = newPwd;
    }
    
    public boolean verifyPassword(String raw) {
        return raw != null && raw.equals(this.password); // swap to PasswordHasher later if you add one
    }
    
    public String getPassword() {
    	return password;
    }
    
    public CompositeFilter<Internship> getFilter() {
		return filter;
	}
    public void setFilter(CompositeFilter<Internship> filter) {this.filter = filter;}
    
}
