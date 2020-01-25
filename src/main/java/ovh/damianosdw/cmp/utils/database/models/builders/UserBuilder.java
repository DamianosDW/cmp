/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.utils.database.models.builders;

import ovh.damianosdw.cmp.utils.database.models.Employee;
import ovh.damianosdw.cmp.utils.database.models.User;
import ovh.damianosdw.cmp.utils.database.models.UserGroup;

public class UserBuilder
{
    private long userId;
    private Employee employee;
    private String login;
    private String password;
    private UserGroup userGroup;
    private boolean active;

    public static UserBuilder builder()
    {
        return new UserBuilder();
    }

    public UserBuilder userId(long userId)
    {
        this.userId = userId;
        return this;
    }

    public UserBuilder employee(Employee employee)
    {
        this.employee = employee;
        return this;
    }

    public UserBuilder login(String login)
    {
        this.login = login;
        return this;
    }

    public UserBuilder password(String password)
    {
        this.password = password;
        return this;
    }

    public UserBuilder userGroup(UserGroup userGroup)
    {
        this.userGroup = userGroup;
        return this;
    }

    public UserBuilder active(boolean active)
    {
        this.active = active;
        return this;
    }

    public User build()
    {
        return new User(userId, employee, login, password, userGroup, active);
    }
}
