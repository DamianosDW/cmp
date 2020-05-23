/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.modules;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import org.mindrot.jbcrypt.BCrypt;
import ovh.damianosdw.cmp.misc.AppStatusType;
import ovh.damianosdw.cmp.utils.DatabaseManager;
import ovh.damianosdw.cmp.utils.database.models.User;

import java.sql.SQLException;

public class PasswordChanger extends Module
{
    public PasswordChanger()
    {
        super("/fxml/passwordChanger.fxml", "Zmiana hasła");
    }

    @FXML
    private PasswordField currentPassword;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField repeatedNewPassword;

    @FXML
    void initialize()
    {

    }

    @FXML
    public void changePassword()
    {
        try {
            if(checkIfUserCurrentPasswordIsCorrect())
            {
                if(checkIfNewPasswordsAreTheSame())
                {
                    Dao<User, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), User.class);
                    User currentUser = dao.queryForFirst(dao.queryBuilder().where().eq("employee_id", MainModule.getLoggedInEmployee().getEmployeeId()).prepare());
                    currentUser.setPassword(BCrypt.hashpw(newPassword.getText(), BCrypt.gensalt()));
                    dao.update(currentUser);
                    AppStatus.showAppStatus(AppStatusType.OK, "Zmieniono hasło do konta!");
                }
                else
                    AppStatus.showAppStatus(AppStatusType.WARNING, "Nowe hasła nie są takie same!");
            }
            else
                AppStatus.showAppStatus(AppStatusType.WARNING, "Twoje aktualne hasło nie zgadza się!");

        } catch(SQLException e) {
            e.printStackTrace();
            AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się zmienić hasła!");
        }
    }

    private boolean checkIfUserCurrentPasswordIsCorrect() throws SQLException
    {
        Dao<User, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), User.class);
        return BCrypt.checkpw(currentPassword.getText(), dao.query(dao.queryBuilder().selectColumns("password").where().eq("employee_id", MainModule.getLoggedInEmployee().getEmployeeId()).prepare()).get(0).getPassword());
    }

    private boolean checkIfNewPasswordsAreTheSame()
    {
        return newPassword.getText().equals(repeatedNewPassword.getText());
    }

    @Override
    public void configureModule()
    {

    }
}
