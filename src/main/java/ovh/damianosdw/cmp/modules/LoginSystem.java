/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.modules;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;
import ovh.damianosdw.cmp.Main;
import ovh.damianosdw.cmp.exceptions.DatabaseErrorException;
import ovh.damianosdw.cmp.exceptions.ModuleLoadErrorException;
import ovh.damianosdw.cmp.misc.AppStatusType;
import ovh.damianosdw.cmp.misc.UserGroupType;
import ovh.damianosdw.cmp.utils.AppUtils;
import ovh.damianosdw.cmp.utils.DatabaseManager;
import ovh.damianosdw.cmp.utils.database.models.User;

import java.sql.SQLException;
import java.util.List;

public class LoginSystem extends Module
{
    public LoginSystem()
    {
        super("/fxml/loginSystem.fxml", "System logowania");
    }

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML @Getter
    private HBox appStatusContainer;

    private StringProperty loginFieldProperty = new SimpleStringProperty();
    private StringProperty passwordFieldProperty = new SimpleStringProperty();
    private BooleanProperty disablePasswordFieldProperty = new SimpleBooleanProperty(true);
    private BooleanProperty disableLoginButtonProperty = new SimpleBooleanProperty(false);

    @FXML
    void initialize()
    {
        //DatabaseManager.INSTANCE.clearDatabase();
        ConfigFileModule.prepareConfigFile();
        DatabaseManager.INSTANCE.initDatabase();
        bindElementsToProperProperties();
        loadAppStatusModule();
    }

    private void bindElementsToProperProperties()
    {
        bindProperties();
        bindTextFieldsToProperties();
        enablePasswordFieldWhenUserFilledLoginField();
        enableLoginButtonWhenAllTextFieldsContainData();
    }

    private void bindProperties()
    {
        disablePasswordFieldProperty.bind(loginFieldProperty.isEmpty());
        disableLoginButtonProperty.bind(loginFieldProperty.isNotEmpty().and(passwordFieldProperty.isNotEmpty()).not());
    }

    private void bindTextFieldsToProperties()
    {
        loginField.textProperty().bindBidirectional(loginFieldProperty);
        passwordField.textProperty().bindBidirectional(passwordFieldProperty);
    }

    private void enablePasswordFieldWhenUserFilledLoginField()
    {
        passwordField.disableProperty().bind(disablePasswordFieldProperty);
    }

    private void enableLoginButtonWhenAllTextFieldsContainData()
    {
        loginButton.disableProperty().bind(disableLoginButtonProperty);
    }

    private void loadAppStatusModule()
    {
        AppStatus.setLoginSystem(this);
        AppStatus.showAppStatus(AppStatusType.OK, "Załadowano system logowania!");
    }

    @FXML
    public void login()
    {
        try {
            String login = loginField.getText().trim();
            User user = getUserCredentials(login);

            if(user != null)
            {
                if(user.isActive())
                {
                    if(BCrypt.checkpw(passwordField.getText().trim(), user.getPassword()))
                    {
                        MainModule mainModule = new MainModule();
                        MainModule.setLoggedInEmployee(user.getEmployee());

                        if(user.getUserGroup().getName().equals("ADMIN"))
                            MainModule.setLoggedInUserGroup(UserGroupType.ADMIN);
                        else if(user.getUserGroup().getName().equals("EMPLOYEE"))
                            MainModule.setLoggedInUserGroup(UserGroupType.EMPLOYEE);

                        Main.getMainStage().setScene(new Scene(mainModule.loadModuleToContainer()));
                        AppStatus.showAppStatus(AppStatusType.OK, "Zalogowano pomyślnie!");
                    }
                    else
                        AppStatus.showAppStatus(AppStatusType.WARNING, "Nieprawidłowy login lub hasło!");
                }
                else
                    AppStatus.showAppStatus(AppStatusType.WARNING, "Twoje konto zostało zablokowane!");
            }
            else
                throw new DatabaseErrorException("Invalid login!");
        } catch(SQLException e) {
            AppStatus.showAppStatus(AppStatusType.ERROR, "Wystąpił problem z bazą danych! Nie można się zalogować.");
            e.printStackTrace(); //TODO REMOVE IT
        } catch(DatabaseErrorException e) {
            AppStatus.showAppStatus(AppStatusType.WARNING, "Nieprawidłowy login lub hasło!");
            AppUtils.disableAndEnableButtonAfterDelay(loginButton);
            e.printStackTrace(); //TODO REMOVE IT
        }
    }

    private User getUserCredentials(String login) throws SQLException, DatabaseErrorException
    {
        Dao<User, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), User.class);
        List<User> result = dao.query(dao.queryBuilder().where().eq("login", login).prepare());

        if(result != null && !result.isEmpty())
            return result.get(0);
        else
            throw new DatabaseErrorException("Invalid account!");
    }

    @Override
    public void load() throws ModuleLoadErrorException
    {
        throw new ModuleLoadErrorException("This method is disabled! Use loadModuleToContainer().");
    }

    @Override
    public void configureModule()
    {

    }
}
