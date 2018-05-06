package server.entity;

import java.io.Serializable;

public class Writer implements Serializable {

    private int writerid;
    private String username;
    private String password;
    private String name;
    private String address;
    private String personalinfo;

    public Writer()
    {

    }

    public int getWriterid() {
        return writerid;
    }

    public void setWriterid(int writerid) {
        this.writerid = writerid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPersonalinfo() {
        return personalinfo;
    }

    public void setPersonalinfo(String personalinfo) {
        this.personalinfo = personalinfo;
    }

    @Override
    public String toString() {
        return "Writer{" +
                "writerid=" + writerid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", personalinfo='" + personalinfo + '\'' +
                '}';
    }
}
