/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.modules;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Setter;
import ovh.damianosdw.cmp.misc.AppStatusType;
import ovh.damianosdw.cmp.misc.CustomDate;
import ovh.damianosdw.cmp.misc.WorkTaskStatus;
import ovh.damianosdw.cmp.utils.AppUtils;
import ovh.damianosdw.cmp.utils.DatabaseManager;
import ovh.damianosdw.cmp.utils.database.models.Employee;
import ovh.damianosdw.cmp.utils.database.models.WorkTask;

import java.sql.SQLException;

public class WorkTaskAssigner extends Module
{
    public WorkTaskAssigner()
    {
        super("/fxml/workTaskAssigner.fxml", "Przydzielenie zadania pracownikowi");
    }

    @FXML
    private ComboBox<Employee> employees;

    @Setter
    private static WorkTask workTask;
    @Setter
    private static Stage stage;
    @Setter
    private static WorkTaskSystem workTaskSystem;

    @FXML
    void initialize()
    {
        configureModule();
    }

    @FXML
    public void assignTaskToEmployee()
    {
        try {
            workTask.setAssignedTo(employees.getValue());
            workTask.setStatus(employees.getValue() != null ? WorkTaskStatus.ASSIGNED.getStatus() : WorkTaskStatus.NEW.getStatus());
            workTask.setUpdateDate(new CustomDate());

            Dao<WorkTask, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), WorkTask.class);
            dao.update(workTask);
            workTaskSystem.refreshAllTables();
            AppUtils.closeAppWindow(stage);
            AppStatus.showAppStatus(AppStatusType.OK, "Przypisałeś/aś zadanie pracownikowi!");
        } catch(SQLException e) {
            AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się przypisać zadania pracownikowi!");
            e.printStackTrace();
            //TODO USE CUSTOM LOGGER
        }
    }

    @FXML
    public void assignTaskToYourself()
    {
        try {
            workTask.setAssignedTo(MainModule.getLoggedInEmployee());
            workTask.setStatus(WorkTaskStatus.ASSIGNED.getStatus());
            workTask.setUpdateDate(new CustomDate());

            Dao<WorkTask, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), WorkTask.class);
            dao.update(workTask);
            workTaskSystem.refreshAllTables();
            AppUtils.closeAppWindow(stage);
            AppStatus.showAppStatus(AppStatusType.OK, "Przypisałeś/aś sobie zadanie!");
        } catch(SQLException e) {
            AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się przypisać zadania!");
            e.printStackTrace();
            //TODO USE CUSTOM LOGGER
        }
    }

    @Override
    public void configureModule()
    {
        try {
            employees.setPlaceholder(new Label("Brak pracowników!"));
            employees.getItems().addAll(EmployeesModule.getEmployeesFromDatabase());
            employees.getSelectionModel().select(workTask.getAssignedTo());
        } catch(SQLException e) {
            e.printStackTrace();
            //TODO USE CUSTOM LOGGER
        }
    }
}
