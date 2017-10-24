package com.example;

public class Kisi {
    private int id;
    private String sfid;
    private String first;
    private String last;
    private String username;
    private String password;
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSfid(String sfid) {
        this.sfid = sfid;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public void setLast(String last) {
        this.last = last;
    }public Kisi(){}
    public Kisi(int id, String sfid, String first, String last, String email,String password,String username){
        super();
        this.id = id;
        this.sfid = sfid;
        this.first = first;
        this.last = last;
        this.email = email;
         this.password=password;
          this.email=email; }


    public int getId()
    {
        return id;
    }

    public String getSfid()
    {
        return sfid;
    }

    public String getLast()
    {
        return this.last;
    }

    public String getFirst()
    {
        return this.first;
    }

    public String getEmail()
    {
        return this.email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

