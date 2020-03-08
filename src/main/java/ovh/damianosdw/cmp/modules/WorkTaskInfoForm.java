/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.modules;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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

import java.sql.SQLException;

public class WorkTaskInfoForm extends Module
{
    public WorkTaskInfoForm()
    {
        super("/fxml/workTaskInfo.fxml", "Informacje o zadaniu");
    }

    @FXML
    private VBox mainContainer;
    @FXML
    private TextField taskName;
    @FXML
    private TextField creator;
    @FXML
    private ComboBox<Employee> assignedTo;
    @FXML
    private ComboBox<WorkTaskStatus> status;
    @FXML
    private ComboBox<WorkTaskPriority> priority;
    @FXML
    private TextField creationDate;
    @FXML
    private TextField updateDate;
    @FXML
    private TextArea additionalInfo;
    @FXML
    private Button updateButton;

    @Setter
    private static WorkTask workTask;
    @Setter
    private static Stage stage;
    @Setter
    private static WorkTaskSystem workTaskSystem;

    @FXML
    void initialize()
    {
        fillForm();
        configureModule();
    }

    private void fillForm()
    {
        taskName.setText(workTask.getName());
        creator.setText(workTask.getCreator().getName() + " " + workTask.getCreator().getSurname());
        creationDate.setText(workTask.getCreationDate().toString());
        if(workTask.getUpdateDate() != null)
            updateDate.setText(workTask.getUpdateDate().toString());
        if(workTask.getAdditionalInfo() != null)
            additionalInfo.setText(workTask.getAdditionalInfo());
        fillEmployeesComboBoxAndSelectProperOne();
        fillStatusComboBoxAndSelectProperOne();
    }

    private void fillEmployeesComboBoxAndSelectProperOne()
    {
        try
        {
            assignedTo.getItems().addAll(EmployeesModule.getEmployeesFromDatabase());
            assignedTo.getSelectionModel().select(workTask.getAssignedTo());
        } catch(SQLException e)
        {
            e.printStackTrace();
            //TODO USE CUSTOM LOGGER
        }
    }

    private void fillPrioritiesComboBoxAndSelectProperOne()
    {
        priority.getItems().addAll(WorkTaskPriority.getAllWorkTaskPriorities());
        priority.getSelectionModel().select(WorkTaskPriority.getWorkTaskPriorityValue("Normalny"));
    }

    private void fillStatusComboBoxAndSelectProperOne()
    {
        status.getItems().addAll(WorkTaskStatus.getAllWorkTaskStatuses());
        status.getSelectionModel().select(WorkTaskStatus.getWorkTaskStatus(workTask.getStatus()));
    }

    @FXML
    public void updateWorkTask()
    {
        if(!taskName.getText().isEmpty())
        {
            workTask.setName(taskName.getText());
            workTask.setAssignedTo(assignedTo.getValue());
            workTask.setAdditionalInfo(additionalInfo.getText());
            workTask.setUpdateDate(new CustomDate());

            if(assignedTo.getValue() != null && workTask.getStatus().equals(WorkTaskStatus.NEW.getStatus()))
                workTask.setStatus(WorkTaskStatus.ASSIGNED.getStatus());
            else
                workTask.setStatus(status.getValue().getStatus());

            try {
                Dao<WorkTask, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), WorkTask.class);
                DatabaseManager.INSTANCE.updateDataInDatabase(dao, workTask);
                workTaskSystem.refreshAllTables();
                AppUtils.closeAppWindow(stage);
                AppStatus.showAppStatus(AppStatusType.OK, "Zmodyfikowano zadanie!");
            } catch(SQLException e) {
                AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się zmodyfikować zadania!");
            }
        }
        else
            AppStatus.showAppStatus(AppStatusType.WARNING, "Nie uzupełniłeś/aś wymaganego pola (temat)!");
    }

    @Override
    public void configureModule()
    {
        switch(MainModule.getLoggedInUserGroup())
        {
            case ADMIN:
                break;
            case EMPLOYEE:
                // Disable proper fields
                taskName.setEditable(false);
                assignedTo.setDisable(true);

                if(workTask.getAssignedTo().getEmployeeId() != MainModule.getLoggedInEmployee().getEmployeeId())
                    mainContainer.getChildren().remove(updateButton);
                break;
        }
    }
}
