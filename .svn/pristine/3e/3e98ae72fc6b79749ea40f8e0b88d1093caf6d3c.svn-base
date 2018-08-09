package com.carel.supervisor.base.profiling;

public class UserCredential
{
    private String userName;
    private String userPassword;
    private String userChannel;

    public UserCredential(String user, String password, String channel)
        throws InvalidCredentialException
    {
        if (user == null)
        {
            throw new InvalidCredentialException(
                "UserName can't be null(user=null)");
        }

        if (password == null)
        {
            throw new InvalidCredentialException(
                "Password can't be null(password=null)");
        }

        if (channel == null)
        {
            throw new InvalidCredentialException(
                "Channel can't be null(channel=null)");
        }

        userName = user;
        userPassword = password;
        userChannel = channel;
    } //UserCredential

    public String getUserName()
    {
        return userName;
    } //getUserName

    public String getUserPassword()
    {
        return userPassword;
    } //getUserPassword

    public String getUserChannel()
    {
        return userChannel;
    } //getUserChannel

    public String toString()
    {
        String tmp = "";
        tmp = "\nUserName= " + userName + "\nUserPassword= " + userPassword +
            "\nUserChannel= " + userChannel;

        return tmp;
    } //toString
} //UserCredential Class
