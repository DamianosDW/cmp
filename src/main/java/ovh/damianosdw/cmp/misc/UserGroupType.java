/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.misc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import lombok.Getter;
import ovh.damianosdw.cmp.utils.AppUtils;
import ovh.damianosdw.cmp.utils.DatabaseManager;
import ovh.damianosdw.cmp.utils.database.models.UserGroup;

import java.sql.SQLException;
import java.util.List;

public enum UserGroupType
{
    ADMIN("ADMIN", getAdminGroup()),
    EMPLOYEE("EMPLOYEE", getEmployeeGroup());

    @Getter
    private String name;
    @Getter
    private UserGroup userGroup;

    UserGroupType(String name, UserGroup userGroup)
    {
        this.name = name;
        this.userGroup = userGroup;
    }

    private static UserGroup getAdminGroup()
    {
        return getUserGroupFromDatabase("ADMIN");
    }

    private static UserGroup getEmployeeGroup()
    {
        return getUserGroupFromDatabase("EMPLOYEE");
    }

    private static UserGroup getUserGroupFromDatabase(String groupName)
    {
        List<UserGroup> result = null;
        try {
            Dao<UserGroup, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), UserGroup.class);

            result = dao.query(dao.queryBuilder().selectColumns("group_id", "name").where().eq("name", groupName).prepare());
        } catch (SQLException e) {
            AppUtils.showWarningAlert("Nie udało się uzyskać informacji o grupie użytkownika! Aplikacja nie może bez tego korzystać i dlatego zostanie automatycznie wyłączona!");
            e.printStackTrace(); //TODO REMOVE IT
            AppUtils.stopApp();
        }

        if(result != null && !result.isEmpty())
            return result.get(0);
        else
            return new UserGroup(-1, groupName + "_ERR");
    }
}
