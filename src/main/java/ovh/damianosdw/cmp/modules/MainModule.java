/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.modules;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.Getter;
import ovh.damianosdw.cmp.exceptions.ModuleLoadErrorException;
import ovh.damianosdw.cmp.utils.AppUtils;
import ovh.damianosdw.cmp.utils.DatabaseManager;
import ovh.damianosdw.cmp.utils.database.models.Employee;

import java.sql.SQLException;

public class MainModule extends Module
{
    public MainModule()
    {
        super("/fxml/main_new.fxml", "Główny moduł");
    }

    @FXML @Getter
    private BorderPane mainContainer;

    @FXML
    private MenuButton info;

    @FXML @Getter
    private HBox appStatusContainer;

    @Getter
    private static Employee loggedInEmployee = getEmployeeInfo(1); //TODO USE EMPLOYEE ID FROM LOGIN MODULE
    @Getter
    private static boolean debugMode = checkIfAppIsRunningInDebugMode();

    @FXML
    void initialize()
    {
        giveThisControllerToOtherModules();
    }

    private static Employee getEmployeeInfo(long employeeId)
    {
        try {
            Dao<Employee, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), Employee.class);
            return dao.queryForId(employeeId);
        } catch (SQLException e) {
            AppUtils.showWarningAlert("Nie udało się uzyskać informacji o koncie użytkownika!");
            e.printStackTrace(); //TODO REMOVE IT
        }
        return new Employee();
    }

    private static boolean checkIfAppIsRunningInDebugMode() //TODO GET INFO FROM CONFIG FILE
    {
        return true;
    }

    private void giveThisControllerToOtherModules()
    {
        AppStatus.setMainModule(this);
        EmployeesModule.setMainModule(this);
    }

    @Override
    public void load() throws ModuleLoadErrorException
    {
        throw new ModuleLoadErrorException("This method is disabled! Use loadModuleToContainer().");
    }

    @Override
    public void refresh()
    {

    }

    @FXML
    public void loadEmployeesModule()
    {
        EmployeesModule employeesModule = new EmployeesModule();
        employeesModule.load();
    }
}
