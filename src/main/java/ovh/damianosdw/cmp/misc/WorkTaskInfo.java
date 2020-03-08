/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.misc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.controlsfx.glyphfont.Glyph;
import ovh.damianosdw.cmp.modules.*;
import ovh.damianosdw.cmp.utils.AppUtils;
import ovh.damianosdw.cmp.utils.DatabaseManager;
import ovh.damianosdw.cmp.utils.FxmlUtils;
import ovh.damianosdw.cmp.utils.database.models.Employee;
import ovh.damianosdw.cmp.utils.database.models.WorkTask;

import java.sql.SQLException;

@Data
@AllArgsConstructor
public class WorkTaskInfo
{
    private long taskId;
    private Hyperlink name;
    private WorkTaskStatus status;
    private Hyperlink creator;
    private Hyperlink assignedTo;
    private CustomDate creationDate;
    private CustomDate updateDate;
    private HBox containerForActionButtons;
    private WorkTask workTask;

    private static final int iconSize = 14;
    @Setter
    private static WorkTaskSystem workTaskSystem;

    public WorkTaskInfo(WorkTask task)
    {
        this.taskId = task.getTaskId();
        this.name = prepareTaskHyperlink(task.getName());
        this.status = WorkTaskStatus.getWorkTaskStatus(task.getStatus());
        this.creator = prepareEmployeeHyperlink(task.getCreator());
        this.assignedTo = prepareEmployeeHyperlink(task.getAssignedTo());
        this.creationDate = task.getCreationDate();
        this.updateDate = task.getUpdateDate();
        this.workTask = task;
        this.containerForActionButtons = prepareActionButtonsAndContainer();
    }

    private Hyperlink prepareTaskHyperlink(String taskName)
    {
        Hyperlink hyperlink = new Hyperlink(taskName);
        hyperlink.setOnAction(event -> showWorkTaskInfo());
        return hyperlink;
    }

    private void showWorkTaskInfo()
    {
        WorkTaskInfoForm.setWorkTask(workTask);
        WorkTaskInfoForm workTaskInfoForm = new WorkTaskInfoForm();
        WorkTaskInfoForm.setStage(AppUtils.showModuleInNewWindowAndGetStage("Informacje o zadaniu", workTaskInfoForm.loadModuleToContainer()));
    }

    private Hyperlink prepareEmployeeHyperlink(Employee employee)
    {
        Hyperlink hyperlink;
        if(employee == null)
        {
            hyperlink = new Hyperlink("-");
            hyperlink.setDisable(true);
        }
        else
        {
            hyperlink = new Hyperlink(employee.getName() + " " + employee.getSurname());
            hyperlink.setOnAction(event -> showEmployeeInfo(employee));
        }
        return hyperlink;
    }

    private void showEmployeeInfo(Employee employee)
    {
        FXMLLoader loader = FxmlUtils.getFxmlLoader("/fxml/employeeInfoForm.fxml");
        EmployeeInfoForm.setEmployeesModule(loader.getController());
        EmployeeInfoForm.setEmployeeInfo(employee);
        EmployeeInfoForm.setStage(AppUtils.showModuleInNewWindowAndGetStage("Informacje o pracowniku", FxmlUtils.loadFxmlWindow("/fxml/employeeInfoForm.fxml")));
    }

    private HBox prepareActionButtonsAndContainer()
    {
        HBox mainContainer = new HBox();
        mainContainer.setStyle("-fx-alignment: center; -fx-spacing: 15;");

        Glyph edit = AppUtils.getFontAwesome().create("\uf044").size(iconSize).color(Color.BLACK);
        Button editTask = AppUtils.prepareActionButton(edit, Color.BLACK, event -> showWorkTaskInfo());

        Glyph assign = AppUtils.getFontAwesome().create("\uf234").size(iconSize).color(Color.GREEN);
        Button assignTask = AppUtils.prepareActionButton(assign, Color.GREEN, event -> assignTaskToEmployee());

        mainContainer.getChildren().addAll(editTask, assignTask);
        return mainContainer;
    }

    private void assignTaskToEmployee()
    {
        try {
            if(MainModule.getLoggedInUserGroup() == UserGroupType.EMPLOYEE)
            {
                assignTaskToLoggedInUser();
                AppStatus.showAppStatus(AppStatusType.OK, "Przypisałeś/aś sobie zadanie!");
            }
            else
            {
                WorkTaskAssigner.setWorkTask(workTask);
                WorkTaskAssigner workTaskAssigner = new WorkTaskAssigner();
                WorkTaskAssigner.setStage(AppUtils.showModuleInNewWindowAndGetStage("Przypisanie zadania do pracownika", workTaskAssigner.loadModuleToContainer()));
            }
        } catch(SQLException e) {
            AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się przypisać zadania do pracownika!");
            //TODO USE CUSTOM LOGGER
        }
    }

    private void assignTaskToLoggedInUser() throws SQLException
    {
        workTask.setAssignedTo(MainModule.getLoggedInEmployee());
        workTask.setStatus(WorkTaskStatus.ASSIGNED.getStatus());

        Dao<WorkTask, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), WorkTask.class);
        dao.update(workTask);
        workTaskSystem.refreshAllTables();
    }
}
