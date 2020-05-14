/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatApp;

/**
 *
 * @author ASUS
 */
public class User {
    private String name;
    private String password;
    private String role;
    
    
    public User(String name, String password, String role){
        this.name = name;
        this.password = password;
        this.role = role;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean matchPassword(String password) {
        if (this.password.compareTo(password) == 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public String getRole() {
        return this.role;
    }
}
