/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.modules;

import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;
import ovh.damianosdw.cmp.exceptions.ModuleLoadErrorException;
import ovh.damianosdw.cmp.utils.database.models.Employee;

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

    @Setter @Getter
    private static Employee loggedInEmployee;
    @Getter
    private static boolean debugMode = checkIfAppIsRunningInDebugMode();

    @FXML
    void initialize()
    {
        giveThisControllerToOtherModules();
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
