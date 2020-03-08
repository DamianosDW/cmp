/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.modules;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ovh.damianosdw.cmp.misc.AppStatusType;
import ovh.damianosdw.cmp.misc.WorkTaskInfo;
import ovh.damianosdw.cmp.misc.WorkTaskStatus;
import ovh.damianosdw.cmp.utils.AppUtils;
import ovh.damianosdw.cmp.utils.DatabaseManager;
import ovh.damianosdw.cmp.utils.database.models.WorkTask;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkTaskSystem extends Module
{
    public WorkTaskSystem()
    {
        super("/fxml/workTaskSystem.fxml", "System zadań");
    }

    @FXML
    private Accordion mainContainer;
    @FXML
    private TableView<WorkTaskInfo> tasksAssignedToMe;
    @FXML
    private Button mineTasksRefreshButton;
    @FXML
    private TableView<WorkTaskInfo> uncompletedTasks;
    @FXML
    private Button tasksRefreshButton;
    @FXML
    private TableView<WorkTaskInfo> completedTasks;
    @FXML
    private Button completedTasksRefreshButton;

    @FXML
    void initialize()
    {
        giveThisControllerToOtherModules();
        configureModule();
    }

    private void giveThisControllerToOtherModules()
    {
        WorkTaskInfoForm.setWorkTaskSystem(this);
        NewWorkTaskForm.setWorkTaskSystem(this);
        WorkTaskAssigner.setWorkTaskSystem(this);
        WorkTaskInfo.setWorkTaskSystem(this);
        NewWorkTaskForm.setWorkTaskSystem(this);
    }

    public void refreshAllTables() throws SQLException
    {
        refreshTasksAssignedToMe();
        refreshTasks();
        refreshCompletedTasks();
    }

    @FXML
    public void refreshTasksAssignedToMe() throws SQLException
    {
        tasksAssignedToMe.getItems().clear();
        tasksAssignedToMe.getItems().addAll(prepareWorkTasksInfo(getTasksAssignedToMeFromDatabase()));
        AppUtils.disableAndEnableButtonAfterDelay(mineTasksRefreshButton);
    }

    private List<WorkTaskInfo> prepareWorkTasksInfo(List<WorkTask> workTasks)
    {
        List<WorkTaskInfo> workTasksInfo = new ArrayList<>();
        for(WorkTask workTask : workTasks)
            workTasksInfo.add(new WorkTaskInfo(workTask));
        return workTasksInfo;
    }

    private List<WorkTask> getTasksAssignedToMeFromDatabase() throws SQLException
    {
        Dao<WorkTask, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), WorkTask.class);
        long employeeId = MainModule.getLoggedInEmployee().getEmployeeId();
        //TODO FIX THIS
        return dao.query(dao.queryBuilder().where().eq("creator", MainModule.getLoggedInEmployee()).or().eq("assigned_to", employeeId).and().notIn("status", WorkTaskStatus.COMPLETED.getStatus()).prepare());
    }

    @FXML
    public void refreshTasks() throws SQLException
    {
        uncompletedTasks.getItems().clear();
        uncompletedTasks.getItems().addAll(prepareWorkTasksInfo(getUncompletedTasksFromDatabase()));
        AppUtils.disableAndEnableButtonAfterDelay(tasksRefreshButton);
    }

    private List<WorkTask> getUncompletedTasksFromDatabase() throws SQLException
    {
        Dao<WorkTask, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), WorkTask.class);
        return dao.query(dao.queryBuilder().where().notIn("status", WorkTaskStatus.COMPLETED.getStatus()).prepare());
    }

    @FXML
    public void refreshCompletedTasks() throws SQLException
    {
        completedTasks.getItems().clear();
        completedTasks.getItems().addAll(prepareWorkTasksInfo(getCompletedTasksFromDatabase()));
        AppUtils.disableAndEnableButtonAfterDelay(completedTasksRefreshButton);
        AppStatus.showAppStatus(AppStatusType.OK, "Odświeżono historię zadań!");
    }

    private List<WorkTask> getCompletedTasksFromDatabase() throws SQLException
    {
        Dao<WorkTask, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), WorkTask.class);
        return dao.query(dao.queryBuilder().where().eq("status", WorkTaskStatus.COMPLETED.getStatus()).prepare());
    }

    @FXML
    public void showNewWorkTaskForm()
    {
        NewWorkTaskForm newWorkTaskForm = new NewWorkTaskForm();
        NewWorkTaskForm.setStage(AppUtils.showModuleInNewWindowAndGetStage("Nowe zadanie", newWorkTaskForm.loadModuleToContainer()));
    }

    private void addPlaceholdersToTables()
    {
        tasksAssignedToMe.setPlaceholder(new Label("Brak zadań!"));
        uncompletedTasks.setPlaceholder(new Label("Brak zadań!"));
        completedTasks.setPlaceholder(new Label("Brak zadań!"));
    }

    private void setCellValueFactoryForTasksAssignedToMeTableColumns()
    {
        tasksAssignedToMe.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        tasksAssignedToMe.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("priority"));
        tasksAssignedToMe.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("status"));
        tasksAssignedToMe.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        tasksAssignedToMe.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("updateDate"));
        tasksAssignedToMe.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("containerForActionButtons"));
    }

    private void setCellValueFactoryForUncompletedTasksTableColumns()
    {
        uncompletedTasks.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        uncompletedTasks.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("creator"));
        uncompletedTasks.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("assignedTo"));
        uncompletedTasks.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("priority"));
        uncompletedTasks.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("status"));
        uncompletedTasks.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("containerForActionButtons"));
    }

    private void setCellValueFactoryForCompletedTasksTableColumns()
    {
        completedTasks.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        completedTasks.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("assignedTo"));
        completedTasks.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("priority"));
        completedTasks.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        completedTasks.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("updateDate"));
        completedTasks.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("containerForActionButtons"));
    }

    @Override
    public void configureModule()
    {
        switch(MainModule.getLoggedInUserGroup())
        {
            case ADMIN:
            case EMPLOYEE:
                addPlaceholdersToTables();
                setCellValueFactoryForTasksAssignedToMeTableColumns();
                setCellValueFactoryForUncompletedTasksTableColumns();
                setCellValueFactoryForCompletedTasksTableColumns();

                try {
                    refreshTasksAssignedToMe();
                    refreshTasks();
                    refreshCompletedTasks();
                    mainContainer.setExpandedPane(mainContainer.getPanes().get(0));
                } catch(SQLException e) {
                    AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się przygotować modułu do pracy!");
                    //TODO USE CUSTOM LOGGER
                }
        }
    }
}
