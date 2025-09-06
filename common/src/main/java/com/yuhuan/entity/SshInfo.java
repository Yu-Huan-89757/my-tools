package com.yuhuan.entity;

public class SshInfo {
    private boolean useCookie;
    private String ip;
    private String username;
    private String pwd;
    private String cookie;
    private String forgerydefense;

    public SshInfo(boolean useCookie, String ip, String username, String pwd, String cookie, String forgerydefense) {
        this.useCookie = useCookie;
        this.ip = ip;
        this.username = username;
        this.pwd = pwd;
        this.cookie = cookie;
        this.forgerydefense = forgerydefense;
    }

    public SshInfo(String ip, String username, String pwd) {
        this.useCookie = false;
        this.ip = ip;
        this.username = username;
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "SshInfo{" +
                "ip='" + ip + '\'' +
                ", username='" + username + '\'' +
                ", pwd='" + pwd + '\'' +
                ", useCookie=" + useCookie +
                ", cookie='" + cookie + '\'' +
                ", forgerydefense='" + forgerydefense + '\'' +
                '}';
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean isUseCookie() {
        return useCookie;
    }

    public void setUseCookie(boolean useCookie) {
        this.useCookie = useCookie;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getForgerydefense() {
        return forgerydefense;
    }

    public void setForgerydefense(String forgerydefense) {
        this.forgerydefense = forgerydefense;
    }
}
