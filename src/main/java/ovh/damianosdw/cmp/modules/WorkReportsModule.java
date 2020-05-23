/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.modules;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ovh.damianosdw.cmp.misc.AppStatusType;
import ovh.damianosdw.cmp.misc.CustomDate;
import ovh.damianosdw.cmp.utils.AppUtils;
import ovh.damianosdw.cmp.utils.DatabaseManager;
import ovh.damianosdw.cmp.utils.database.models.WorkReport;
import ovh.damianosdw.cmp.utils.database.models.builders.WorkReportBuilder;

import java.sql.SQLException;
import java.util.List;

public class WorkReportsModule extends Module
{

    public WorkReportsModule()
    {
        super("/fxml/workReportsModule.fxml", "Sprawozdania z pracy");
    }

    @FXML
    private TabPane mainContainer;
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TextArea workReport;
    @FXML
    private Button sendWorkReportButton;

    @FXML
    private TableView<WorkReport> workReportsTable;
    @FXML
    private Button refreshWorkReportsButton;

    @FXML
    void initialize()
    {
        showLoggedInUserInfo();
        configureModule();
    }

    private void showLoggedInUserInfo()
    {
        name.setText(MainModule.getLoggedInEmployee().getName());
        surname.setText(MainModule.getLoggedInEmployee().getSurname());
    }

    private void setCellValueFactoryForWorkReportsTableColumns()
    {
        workReportsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("sentDate"));
        workReportsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("employee"));
        workReportsTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("workReport"));
    }

    @FXML
    public void showWorkReports()
    {
        try {
            List<WorkReport> workReports = getWorkReportsFromDatabase();

            workReportsTable.getItems().clear();
            workReportsTable.getItems().addAll(workReports);
            AppUtils.disableAndEnableButtonAfterDelay(refreshWorkReportsButton);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<WorkReport> getWorkReportsFromDatabase() throws SQLException
    {
        Dao<WorkReport, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), WorkReport.class);
        return DatabaseManager.INSTANCE.getAllRecordsFromTable(dao);
    }

    @FXML
    public void sendWorkReport()
    {
        try {
            if(!workReport.getText().trim().isEmpty())
            {
                Dao<WorkReport, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), WorkReport.class);
                WorkReport report = WorkReportBuilder.builder()
                        .sentDate(new CustomDate())
                        .employee(MainModule.getLoggedInEmployee())
                        .workReport(workReport.getText().trim())
                        .build();

                DatabaseManager.INSTANCE.saveDataToDatabase(dao, report);
                AppUtils.disableAndEnableButtonAfterDelay(sendWorkReportButton);
                clearWorkReportForm();
                showWorkReports();
            }
            else
                AppUtils.showWarningAlert("Nie uzupełniłeś/aś treści sprawozdania!");
        } catch (SQLException e) {
            AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się wysłać sprawozdania z pracy!");
            e.printStackTrace();
        }
    }

    private void clearWorkReportForm()
    {
        workReport.clear();
    }

    @Override
    public void configureModule()
    {
        switch(MainModule.getLoggedInUserGroup())
        {
            case ADMIN:
                // Configure TableView (set proper info when data is not available)
                workReportsTable.setPlaceholder(new Label("Brak sprawozdań!"));

                setCellValueFactoryForWorkReportsTableColumns();
                showWorkReports();
                break;
            case EMPLOYEE:
                // Remove "Work reports" tab
                mainContainer.getTabs().remove(1);
                break;
        }
    }
}
