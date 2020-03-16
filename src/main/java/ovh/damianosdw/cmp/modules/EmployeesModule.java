/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.modules;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.Glyph;
import ovh.damianosdw.cmp.misc.AppStatusType;
import ovh.damianosdw.cmp.utils.AppUtils;
import ovh.damianosdw.cmp.utils.DatabaseManager;
import ovh.damianosdw.cmp.utils.FxmlUtils;
import ovh.damianosdw.cmp.utils.database.models.Employee;
import ovh.damianosdw.cmp.utils.database.models.JobTitle;
import ovh.damianosdw.cmp.utils.database.models.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EmployeesModule extends Module
{
    public EmployeesModule()
    {
        super("/fxml/employees.fxml", "Pracownicy, stanowiska i sprawozdania");
    }

    @FXML
    private Accordion mainContainer;

    @FXML
    private TableView<Employee> employees;
    @FXML
    private TableView<JobTitle> jobTitles;

    @FXML
    private TextField jobName;
    @FXML
    private TextField minSalary;
    @FXML
    private TextField maxSalary;
    @FXML
    private TextArea responsibilities;

    @FXML
    private VBox containerForEmployeeActionButtons;
    @FXML
    private VBox containerForJobActionButtons;

    @FXML
    private Button refreshEmployeesButton;

    @FXML
    private Button refreshJobTitlesButton;

    private static final int iconSize = 14;

    @FXML
    void initialize()
    {
        giveThisControllerToOtherModules();
        configureModule();
    }

    private void giveThisControllerToOtherModules()
    {
        NewEmployeeForm.setEmployeesModule(this);
        EmployeeInfoForm.setEmployeesModule(this);
        NewJobTitleForm.setEmployeesModule(this);
        JobInfoForm.setEmployeesModule(this);
    }

    private void setCellValueFactoryForEmployeeTableColumns()
    {
        employees.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        employees.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("surname"));
        employees.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        employees.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("salary"));
        employees.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("contact"));
    }

    private void setCellValueFactoryForJobTitleTableColumns()
    {
        jobTitles.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        jobTitles.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("salaryRange"));
        jobTitles.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("responsibilities"));
    }

    @FXML
    public void showAllEmployees()
    {
        try {
            List<Employee> allEmployees = getEmployeesFromDatabase();

            containerForEmployeeActionButtons.getChildren().clear();
            allEmployees.forEach(employee -> prepareEmployeeActionButtonsAndAddThemToContainer(employee));

            employees.getItems().clear();
            employees.getItems().addAll(allEmployees);
            AppUtils.disableAndEnableButtonAfterDelay(refreshEmployeesButton);
        } catch(SQLException e) {
            AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się załadować pracowników!");
        }
    }

    public static List<Employee> getEmployeesFromDatabase() throws SQLException
    {
        Dao<Employee, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), Employee.class);
        return DatabaseManager.INSTANCE.getAllRecordsFromTable(dao);
    }

    private void prepareEmployeeActionButtonsAndAddThemToContainer(Employee employee)
    {
        HBox containerForActionButtons = prepareContainerForActionButtons();

        Glyph modify = AppUtils.getFontAwesome().create("\uf044").size(iconSize).color(Color.BLACK);
        Button modifyEmployeeButton = prepareActionButton(modify);
        modifyEmployeeButton.setOnAction(event -> showEmployeeModificationWindow(employee));

        Button employeeAccountButton = prepareEmployeeAccountButton(employee);

        containerForActionButtons.getChildren().addAll(modifyEmployeeButton, employeeAccountButton);
        containerForEmployeeActionButtons.getChildren().add(containerForActionButtons);
    }

    private HBox prepareContainerForActionButtons()
    {
        HBox container = new HBox();
        container.setStyle("-fx-pref-height: 25px; -fx-alignment: center; -fx-spacing: 15;");
        return container;
    }

    private Button prepareEmployeeAccountButton(Employee employee)
    {
        Button employeeAccountButton = null;

        try {
            if(checkIfAccountIsActive(employee.getEmployeeId()))
            {
                Glyph lock = AppUtils.getFontAwesome().create("\uf023").size(iconSize).color(Color.DARKRED);
                employeeAccountButton = prepareActionButton(lock, Color.DARKRED);
                employeeAccountButton.setOnAction(event -> showLockEmployeeAccountConfirmationWindow(employee));
            }
            else
            {
                Glyph unlock = AppUtils.getFontAwesome().create("\uf09c").size(iconSize).color(Color.DARKGREEN);
                employeeAccountButton = prepareActionButton(unlock, Color.DARKGREEN);
                employeeAccountButton.setOnAction(event -> showUnlockEmployeeAccountConfirmationWindow(employee));
            }
        } catch(SQLException e) {
            e.printStackTrace(); //TODO REMOVE IT
        }

        return employeeAccountButton;
    }

    private Button prepareActionButton(Glyph icon)
    {
        Button actionButton = new Button("", icon);
        actionButton.setStyle("-fx-pref-width: 35px; -fx-pref-height: 25px; -fx-font-size: 12px; -fx-background-color: transparent;");
        actionButton.setOnMouseEntered(event -> actionButton.setGraphic(icon.color(Color.valueOf("#0099ff"))));
        actionButton.setOnMouseExited(event -> actionButton.setGraphic(icon.color(Color.BLACK)));
        return actionButton;
    }

    private Button prepareActionButton(Glyph icon, Color iconColor)
    {
        Button actionButton = new Button("", icon);
        actionButton.setStyle("-fx-pref-width: 35px; -fx-pref-height: 25px; -fx-font-size: 12px; -fx-background-color: transparent;");
        actionButton.setOnMouseEntered(event -> actionButton.setGraphic(icon.color(Color.valueOf("#0099ff"))));
        actionButton.setOnMouseExited(event -> actionButton.setGraphic(icon.color(iconColor)));
        return actionButton;
    }

    private void showEmployeeModificationWindow(Employee employee)
    {
        EmployeeInfoForm.setEmployeeInfo(employee);
        Stage stage = new Stage();
        Scene scene = new Scene(FxmlUtils.loadFxmlWindow("/fxml/employeeInfoForm.fxml"));
        stage.setTitle("Aktualizacja danych o pracowniku");
        stage.setScene(scene);
        stage.setResizable(false);
        EmployeeInfoForm.setStage(stage);
        stage.show();
    }

    private boolean checkIfAccountIsActive(long employeeId) throws SQLException
    {
        Dao<User, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), User.class);
        return dao.queryForFirst(dao.queryBuilder().where().eq("employee_id", employeeId).prepare()).isActive();
    }

    private void showLockEmployeeAccountConfirmationWindow(Employee employee)
    {
        Alert confirmActionDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmActionDialog.setTitle("Wyłączenie konta pracownika");
        confirmActionDialog.setHeaderText("Czy chcesz wyłączyć konto pracownika: " + employee.getName() + " " + employee.getSurname() + "?");
        confirmActionDialog.showAndWait().ifPresent(result -> {
            if(result == ButtonType.OK)
                disableEmployeeAccount(employee);
        });
    }

    private void disableEmployeeAccount(Employee employee)
    {
        updateEmployeeAccount(employee, false, "Wyłączono konto pracownika!");
    }

    private void showUnlockEmployeeAccountConfirmationWindow(Employee employee)
    {
        Alert confirmActionDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmActionDialog.setTitle("Włączenie konta pracownika");
        confirmActionDialog.setHeaderText("Czy chcesz włączyć konto pracownika: " + employee.getName() + " " + employee.getSurname() + "?");
        confirmActionDialog.showAndWait().ifPresent(result -> {
            if(result == ButtonType.OK)
                enableEmployeeAccount(employee);
        });
    }

    private void enableEmployeeAccount(Employee employee)
    {
        updateEmployeeAccount(employee, true, "Włączono konto pracownika!");
    }

    private void updateEmployeeAccount(Employee employee, boolean active, String appStatusInfo)
    {
        try {
            Dao<Employee, Long> employeeDao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), Employee.class);
            long employeeId = employeeDao.extractId(employee);

            Dao<User, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), User.class);
            User user = dao.queryForFirst(dao.queryBuilder().where().eq("employee_id", employeeId).prepare());
            user.setActive(active);
            dao.update(user);

            showAllEmployees();
            AppStatus.showAppStatus(AppStatusType.OK, appStatusInfo);
        } catch(SQLException e) {
            AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się włączyć/wyłączyć konta pracownika!");
        }
    }

    @FXML
    public void showJobTitles()
    {
        try {
            List<JobTitle> allJobTitles = getJobTitlesFromDatabase();

            containerForJobActionButtons.getChildren().clear();
            allJobTitles.forEach(jobTitle -> prepareJobActionButtonsAndAddThemToContainer(jobTitle));

            jobTitles.getItems().clear();
            jobTitles.getItems().addAll(allJobTitles);
            AppUtils.disableAndEnableButtonAfterDelay(refreshJobTitlesButton);
        } catch(SQLException e) {
            AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się załadować stanowisk!");
        }
    }

    public List<JobTitle> getJobTitlesFromDatabase() throws SQLException
    {
        Dao<JobTitle, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), JobTitle.class);
        return DatabaseManager.INSTANCE.getAllRecordsFromTable(dao);
    }

    private void prepareJobActionButtonsAndAddThemToContainer(JobTitle jobTitle)
    {
        HBox containerForActionButtons = prepareContainerForActionButtons();

        Glyph modify = AppUtils.getFontAwesome().create("\uf044").size(iconSize).color(Color.BLACK);
        Button modifyJobButton = prepareActionButton(modify);
        modifyJobButton.setOnAction(event -> showJobModificationWindow(jobTitle));

        Glyph remove = AppUtils.getFontAwesome().create("\uf1f8").size(iconSize).color(Color.BLACK);
        Button removeJobButton = prepareActionButton(remove);
        removeJobButton.setOnAction(event -> showRemoveJobConfirmationWindow(jobTitle));

        containerForActionButtons.getChildren().addAll(modifyJobButton, removeJobButton);
        containerForJobActionButtons.getChildren().add(containerForActionButtons);
    }

    private void showJobModificationWindow(JobTitle jobTitle)
    {
        JobInfoForm.setJobTitle(jobTitle);
        Stage stage = new Stage();
        Scene scene = new Scene(FxmlUtils.loadFxmlWindow("/fxml/jobInfoForm.fxml"));
        stage.setTitle("Aktualizacja danych o stanowisku");
        stage.setScene(scene);
        stage.setResizable(false);
        JobInfoForm.setStage(stage);
        stage.show();
    }

    private void showRemoveJobConfirmationWindow(JobTitle jobTitle)
    {
        Alert confirmActionDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmActionDialog.setTitle("Usunięcie stanowiska");
        confirmActionDialog.setHeaderText("Czy chcesz usunąć stanowisko (ważne: żaden pracownik nie może być przypisany do tego stanowiska): " + jobTitle.getName() + "?");
        confirmActionDialog.showAndWait().ifPresent(result -> {
            if(result == ButtonType.OK)
                removeJobFromDatabase(jobTitle);
        });
    }

    private void removeJobFromDatabase(JobTitle jobTitle)
    {
        try {
            if(jobCanBeRemoved(jobTitle))
            {
                Dao<JobTitle, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), JobTitle.class);
                dao.delete(jobTitle);
                showJobTitles();
            }
            else
                AppStatus.showAppStatus(AppStatusType.WARNING, "Nie można usunąć stanowiska, które jest przypisane do pracowników!");
        } catch(SQLException e) {
            AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się usunąć stanowiska!");
            e.printStackTrace();
        }
    }

    private boolean jobCanBeRemoved(JobTitle job) throws SQLException
    {
        List<Employee> employees = getEmployeesFromDatabase();
        for(Employee employee : employees)
        {
            if(employee.getJobTitle().getJobId() == job.getJobId())
                return false;
        }

        return true;
    }

    @FXML
    public void showNewEmployeeWindow() throws IOException
    {
        FXMLLoader loader = FxmlUtils.getFxmlLoader("/fxml/newEmployeeForm.fxml");
        Parent newEmployeeForm = loader.load();
        NewEmployeeForm newEmployeeFormController = loader.getController();

        Alert newEmployeeDialog = new Alert(Alert.AlertType.CONFIRMATION);
        newEmployeeDialog.setTitle("Okno dodawania nowego pracownika");
        newEmployeeDialog.setHeaderText("Dodawanie nowego pracownika");
        newEmployeeDialog.getDialogPane().getStyleClass().add("appBackground");
        newEmployeeDialog.getDialogPane().setContent(newEmployeeForm);
        newEmployeeDialog.showAndWait().ifPresent(result -> {
            if(result == ButtonType.OK)
            {
                newEmployeeFormController.addNewEmployeeToDatabase();
                showAllEmployees();
            }
        });
    }

    @FXML
    public void showNewJobTitleWindow()
    {
        Stage stage = new Stage();
        Scene scene = new Scene(FxmlUtils.loadFxmlWindow("/fxml/newJobTitleForm.fxml"));
        stage.setTitle("Nowe stanowisko");
        stage.setScene(scene);
        stage.setResizable(false);
        NewJobTitleForm.setStage(stage);
        stage.show();
    }

    @Override
    public void configureModule()//TODO
    {
        switch(MainModule.getLoggedInUserGroup())
        {
            case ADMIN:
                // Configure TableView (set proper info when data is not available)
                employees.setPlaceholder(new Label("Brak pracowników!"));
                jobTitles.setPlaceholder(new Label("Brak stanowisk!"));

                setCellValueFactoryForEmployeeTableColumns();
                setCellValueFactoryForJobTitleTableColumns();

                showAllEmployees();
                showJobTitles();

                mainContainer.setExpandedPane(mainContainer.getPanes().get(0));
                break;
            case EMPLOYEE:
                mainContainer.setExpandedPane(mainContainer.getPanes().get(0));
                break;
        }
    }
}
