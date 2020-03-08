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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.Glyph;
import ovh.damianosdw.cmp.exceptions.InvalidDataException;
import ovh.damianosdw.cmp.misc.*;
import ovh.damianosdw.cmp.utils.AppUtils;
import ovh.damianosdw.cmp.utils.DatabaseManager;
import ovh.damianosdw.cmp.utils.database.models.Employee;
import ovh.damianosdw.cmp.utils.database.models.WorkHoliday;
import ovh.damianosdw.cmp.utils.database.models.builders.WorkHolidayBuilder;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class WorkHolidayModule extends Module
{
    public WorkHolidayModule()
    {
        super("/fxml/workHoliday.fxml", "Urlopy");
    }

    @FXML
    private TabPane mainContainer;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private ComboBox<WorkHolidayType> type;
    @FXML
    private TextArea reason;
    @FXML
    private Button sendButton;

    @FXML
    private TableView<WorkHoliday> workHolidaysHistory;

    @FXML
    private VBox containerForActionButtons;
    @FXML
    private TableView<WorkHoliday> allWorkHolidaysTable;
    @FXML
    private Button refreshWorkHolidaysButton;

    private static final int iconSize = 14;

    @FXML
    void initialize()
    {
        addWorkHolidayTypesToCheckBox();
        configureModule();
    }

    private void addWorkHolidayTypesToCheckBox()
    {
        if(MainModule.getLoggedInEmployee().getSex().equals(Sex.MALE.getSex()))
        {
            type.getItems().addAll(WorkHolidayType.REST, WorkHolidayType.FREE, WorkHolidayType.ON_DEMAND, WorkHolidayType.OCCASIONAL, WorkHolidayType.PARENTAL_LEAVE, WorkHolidayType.PATERNITY_LEAVE, WorkHolidayType.RAISING);
        }
        else if(MainModule.getLoggedInEmployee().getSex().equals(Sex.FEMALE.getSex()))
        {
            type.getItems().addAll(WorkHolidayType.REST, WorkHolidayType.FREE, WorkHolidayType.ON_DEMAND, WorkHolidayType.OCCASIONAL, WorkHolidayType.MATERNITY_LEAVE, WorkHolidayType.PARENTAL_LEAVE, WorkHolidayType.RAISING);
        }
    }

    @FXML
    public void sendWorkHolidayInfo() throws ParseException, SQLException
    {
        if(startDate.getValue() != null && endDate.getValue() != null && type.getValue() != null)
        {
            if(checkIfEmployeeCanGetWorkHoliday(MainModule.getLoggedInEmployee()))
            {
                DateFormat dateFormat = new SimpleDateFormat(CustomDate.DEFAULT_DATE_FORMAT);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(CustomDate.DEFAULT_DATE_FORMAT);
                LocalDate holidayStartDate = startDate.getValue();
                LocalDate holidayEndDate = endDate.getValue();

                WorkHoliday workHoliday = WorkHolidayBuilder.builder()
                        .employee(MainModule.getLoggedInEmployee())
                        .startDate(new CustomDate(dateFormat.parse(holidayStartDate.format(dateTimeFormatter)).getTime()))
                        .endDate(new CustomDate(dateFormat.parse(holidayEndDate.format(dateTimeFormatter)).getTime()))
                        .type(type.getValue())
                        .build();

                if(reason.getText().isEmpty())
                    workHoliday.setReason("-");
                else
                    workHoliday.setReason(reason.getText());

                Dao<WorkHoliday, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), WorkHoliday.class);
                DatabaseManager.INSTANCE.saveDataToDatabase(dao, workHoliday);
                clearForm();
                AppUtils.disableAndEnableButtonAfterDelay(sendButton);

                showLoggedInUserWorkHolidays();
                if(MainModule.getLoggedInUserGroup().equals(UserGroupType.ADMIN))
                    showAllWorkHolidays();
            }
        }
        else
            AppStatus.showAppStatus(AppStatusType.WARNING, "Nie uzupełniłeś/aś wymaganych pól!");
    }

    private boolean checkIfEmployeeCanGetWorkHoliday(Employee employee)//TODO FINISH IT
    {
        LocalDate currentDate = LocalDate.now();
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();
        LocalDate employeeWorkStartDate = employee.getWorkStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // startDate can't be before now
        if(start.isBefore(currentDate) || end.isBefore(currentDate))
        {
            AppStatus.showAppStatus(AppStatusType.WARNING, "Nie można wybrać przeszłej daty!");
            return false;
        }
        
        // startDate can't be after endDate
        if(start.isAfter(end))
        {
            AppStatus.showAppStatus(AppStatusType.WARNING, "Termin urlopu (od ... do ...) jest niepoprawny!");
            return false;
        }

        if(start.isEqual(end))
        {
            AppStatus.showAppStatus(AppStatusType.WARNING, "Daty nie mogą być takie same!");
            return false;
        }

        switch(type.getValue())
        {
            case REST:
                if(ChronoUnit.DAYS.between(start, end) > 26)
                {
                    AppStatus.showAppStatus(AppStatusType.WARNING, "Urlop wypoczynkowy nie może trwać dłużej niż 26 dni!");
                    return false;
                }
                break;
            case MATERNITY_LEAVE:
                if(ChronoUnit.WEEKS.between(start, end) > 22)
                {
                    AppStatus.showAppStatus(AppStatusType.WARNING, "Urlop macierzyński nie może trwać dłużej niż 22 tygodnie!");
                    return false;
                }
                break;
            case PATERNITY_LEAVE:
                if(ChronoUnit.WEEKS.between(start, end) < 6 || ChronoUnit.WEEKS.between(start, end) > 14)
                {
                    AppStatus.showAppStatus(AppStatusType.WARNING, "Urlop tacierzyński może trwać 6-14 tygodni!");
                    return false;
                }
                break;
            case PATERNITY_LEAVE_SHORT:
                if(ChronoUnit.DAYS.between(start, end) > 14)
                {
                    AppStatus.showAppStatus(AppStatusType.WARNING, "Urlop ojcowski nie może trwać dłużej niż 14 dni!");
                    return false;
                }
                break;
            case PARENTAL_LEAVE:
                boolean employeeUsedRaisingHoliday = true;//TODO GET INFO FROM DATABASE

                if(employeeUsedRaisingHoliday)
                {
                    if(ChronoUnit.WEEKS.between(start, end) > 34)
                    {
                        AppStatus.showAppStatus(AppStatusType.WARNING, "Urlop rodzicielski nie może trwać dłużej niż 34 tygodnie!");
                        return false;
                    }
                }
                else
                {
                    AppStatus.showAppStatus(AppStatusType.WARNING, "Nie można pójść na urlop rodzicielski, gdy nie wykorzystało się urlopu wychowawczego!");
                    return false;
                }
                break;
            case RAISING:
                if(ChronoUnit.MONTHS.between(employeeWorkStartDate, currentDate) < 6)
                {
                    AppStatus.showAppStatus(AppStatusType.WARNING, "Nie możesz wziąć urlopu wychowawczego - pracujesz za krótko (wymagane 6 miesięcy)!");
                    return false;
                }

                if(ChronoUnit.YEARS.between(start, end) > 3)
                {
                    AppStatus.showAppStatus(AppStatusType.WARNING, "Urlop wychowawczy nie może trwać dłużej niż 3 lata!");
                    return false;
                }
                break;
            case ON_DEMAND:
                if(ChronoUnit.DAYS.between(start, end) > 5)
                {
                    AppStatus.showAppStatus(AppStatusType.WARNING, "Urlop na żądanie nie może trwać dłużej niż 4 dni!");
                    return false;
                }
                break;
            case OCCASIONAL:
                if(ChronoUnit.DAYS.between(start, end) > 2)
                {
                    AppStatus.showAppStatus(AppStatusType.WARNING, "Urlop okazjonalny nie może trwać dłużej niż 2 dni!");
                    return false;
                }
                break;
            case FREE:
                break;
            default:
                throw new InvalidDataException("Invalid WorkHolidayType! Value: " + type.getValue());
        }

        return true;
    }

    private void clearForm()
    {
        startDate.setValue(null);
        endDate.setValue(null);
        type.getSelectionModel().clearSelection();
        reason.clear();
    }

    @FXML
    public void showAllWorkHolidays()
    {
        try {
            List<WorkHoliday> allWorkHolidays = getAllWorkHolidays();
            containerForActionButtons.getChildren().clear();
            allWorkHolidays.forEach(workHoliday -> prepareActionButtonsAndAddThemToContainer(workHoliday));

            allWorkHolidaysTable.getItems().clear();
            allWorkHolidaysTable.getItems().addAll(allWorkHolidays);
            AppUtils.disableAndEnableButtonAfterDelay(refreshWorkHolidaysButton);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private List<WorkHoliday> getAllWorkHolidays() throws SQLException
    {
        Dao<WorkHoliday, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), WorkHoliday.class);
        return DatabaseManager.INSTANCE.getAllRecordsFromTable(dao);
    }

    private void prepareActionButtonsAndAddThemToContainer(WorkHoliday workHoliday)
    {
        HBox tempContainer = AppUtils.prepareContainerForActionButtons();

        Glyph accept = AppUtils.getFontAwesome().create("\uf00c").size(iconSize).color(Color.GREEN);
        Button acceptButton = AppUtils.prepareActionButton(accept, Color.GREEN);
        acceptButton.setOnAction(event -> acceptWorkHoliday(workHoliday));

        Glyph decline = AppUtils.getFontAwesome().create("\uf00d").size(iconSize).color(Color.RED);
        Button declineButton = AppUtils.prepareActionButton(decline, Color.RED);
        declineButton.setOnAction(event -> declineWorkHoliday(workHoliday));

        if(workHoliday.getStatus().equals(WorkHolidayStatus.ACCEPTED.getStatus()) || workHoliday.getStatus().equals(WorkHolidayStatus.DECLINED.getStatus()))
        {
            acceptButton.setDisable(true);
            declineButton.setDisable(true);
        }

        tempContainer.getChildren().addAll(acceptButton, declineButton);
        containerForActionButtons.getChildren().add(tempContainer);
    }

    private void acceptWorkHoliday(WorkHoliday workHoliday) //TODO SEND NOTIFICATION TO EMPLOYEE
    {
        try {
            Dao<WorkHoliday, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), WorkHoliday.class);

            workHoliday.setStatus(WorkHolidayStatus.ACCEPTED.getStatus());
            dao.update(workHoliday);
            AppStatus.showAppStatus(AppStatusType.OK, "Zaakceptowano urlop!");
        } catch(SQLException e) {
            AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się zaakceptować urlopu!");
        }
    }

    private void declineWorkHoliday(WorkHoliday workHoliday) //TODO SEND NOTIFICATION TO EMPLOYEE
    {
        try {
            Dao<WorkHoliday, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), WorkHoliday.class);

            workHoliday.setStatus(WorkHolidayStatus.DECLINED.getStatus());
            dao.update(workHoliday);
            AppStatus.showAppStatus(AppStatusType.OK, "Odrzucono urlop!");
        } catch(SQLException e) {
            AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się odrzucić urlopu!");
        }
    }

    private void setCellValueFactoryForAllWorkHolidaysTableColumns()
    {
        allWorkHolidaysTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("employee"));
        allWorkHolidaysTable.getColumns().get(1).getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("startDate"));
        allWorkHolidaysTable.getColumns().get(1).getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("endDate"));
        allWorkHolidaysTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("type"));
        allWorkHolidaysTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("reason"));
    }

    private void setCellValueFactoryForLoggedInUserTableColumns()
    {
        workHolidaysHistory.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("startDate"));
        workHolidaysHistory.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("endDate"));
        workHolidaysHistory.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("type"));
        workHolidaysHistory.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("reason"));
    }

    private void showLoggedInUserWorkHolidays()
    {
        try {
            List<WorkHoliday> workHolidays = getLoggedInUserWorkHolidays();
            workHolidaysHistory.getItems().clear();
            workHolidaysHistory.getItems().addAll(workHolidays);
        } catch(SQLException e) {
            e.printStackTrace(); //TODO REMOVE IT
        }
    }

    private List<WorkHoliday> getLoggedInUserWorkHolidays() throws SQLException
    {
        Dao<WorkHoliday, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), WorkHoliday.class);
        return dao.query(dao.queryBuilder().where().eq("employee_id", MainModule.getLoggedInEmployee().getEmployeeId()).prepare());
    }

    @Override
    public void configureModule()
    {
        switch(MainModule.getLoggedInUserGroup())
        {
            case ADMIN:
                // Configure TableView (set proper info when data is not available)
                workHolidaysHistory.setPlaceholder(new Label("Brak historii!"));
                allWorkHolidaysTable.setPlaceholder(new Label("Brak urlopów!"));

                setCellValueFactoryForLoggedInUserTableColumns();
                setCellValueFactoryForAllWorkHolidaysTableColumns();
                showAllWorkHolidays();
                showLoggedInUserWorkHolidays();
                break;
            case EMPLOYEE:
                // Configure TableView (set proper info when data is not available)
                workHolidaysHistory.setPlaceholder(new Label("Brak historii!"));
                // Remove 'All work holidays' tab
                mainContainer.getTabs().remove(2);

                setCellValueFactoryForLoggedInUserTableColumns();
                showLoggedInUserWorkHolidays();
                break;
        }
    }
}
