/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.modules;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import ovh.damianosdw.cmp.misc.AppStatusType;
import ovh.damianosdw.cmp.misc.CustomDate;
import ovh.damianosdw.cmp.misc.WorkTaskPriority;
import ovh.damianosdw.cmp.misc.WorkTaskStatus;
import ovh.damianosdw.cmp.utils.AppUtils;
import ovh.damianosdw.cmp.utils.DatabaseManager;
import ovh.damianosdw.cmp.utils.database.models.Employee;
import ovh.damianosdw.cmp.utils.database.models.WorkTask;
import ovh.damianosdw.cmp.utils.database.models.builders.WorkTaskBuilder;

import java.sql.SQLException;

public class NewWorkTaskForm extends Module
{
    public NewWorkTaskForm()
    {
        super("/fxml/newWorkTaskForm.fxml", "Tworzenie nowego zadania");
    }

    @FXML
    private TextField taskName;
    @FXML
    private ComboBox<Employee> employees;
    @FXML
    private ComboBox<WorkTaskPriority> priorities;
    @FXML
    private TextArea additionalInfo;

    @Setter
    private static Stage stage;
    @Setter
    private static WorkTaskSystem workTaskSystem;

    @FXML
    void initialize()
    {
        try {
            employees.getItems().addAll(EmployeesModule.getEmployeesFromDatabase());
            priorities.getItems().addAll(WorkTaskPriority.getAllWorkTaskPriorities());
            priorities.getSelectionModel().select(WorkTaskPriority.getWorkTaskPriorityValue("Normalny"));
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addWorkTask()
    {
        if(!taskName.getText().isEmpty())
        {
            try {
                WorkTask workTask = WorkTaskBuilder.builder()
                        .name(taskName.getText())
                        .creator(MainModule.getLoggedInEmployee())
                        .assignedTo(employees.getValue())
                        .priority(priorities.getValue().getPriority())
                        .creationDate(new CustomDate())
                        .additionalInfo(additionalInfo.getText())
                        .build();

                if(employees.getValue() != null)
                    workTask.setStatus(WorkTaskStatus.ASSIGNED.getStatus());
                else
                    workTask.setStatus(WorkTaskStatus.NEW.getStatus());

                Dao<WorkTask, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), WorkTask.class);
                DatabaseManager.INSTANCE.saveDataToDatabase(dao, workTask);
                workTaskSystem.refreshAllTables();
                AppUtils.closeAppWindow(stage);
                AppStatus.showAppStatus(AppStatusType.OK, "Utworzono zadanie!");
            } catch(SQLException e) {
                AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się utworzyć zadania!");
            }
        }
        else
            AppUtils.showWarningAlert("Nie uzupełniłeś/aś tematu zadania!");
    }

    @Override
    public void configureModule()
    {

    }
}
