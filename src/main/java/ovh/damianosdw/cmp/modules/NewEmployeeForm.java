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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;
import ovh.damianosdw.cmp.misc.AppStatusType;
import ovh.damianosdw.cmp.utils.AppUtils;
import ovh.damianosdw.cmp.utils.DatabaseManager;
import ovh.damianosdw.cmp.utils.database.models.Employee;
import ovh.damianosdw.cmp.utils.database.models.JobTitle;
import ovh.damianosdw.cmp.utils.database.models.builders.EmployeeBuilder;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewEmployeeForm
{
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private ComboBox<JobTitle> jobTitles;
    @FXML
    private TextField salary;
    @FXML
    private Label salaryRangeInfo;
    @FXML
    private TextArea contact;

    @Setter
    private static EmployeesModule employeesModule;
    private Map<String, String> jobSalaryRanges = new HashMap<>();

    @FXML
    void initialize()
    {
        try {
            List<JobTitle> jobsInfo = employeesModule.getJobTitlesFromDatabase();

            jobsInfo.forEach(jobTitle -> jobSalaryRanges.put(jobTitle.getName(), jobTitle.getSalaryRange()));

            jobTitles.getItems().addAll(jobsInfo);
            jobTitles.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
                salaryRangeInfo.setText(jobSalaryRanges.get(newValue.getName()));
            }));
        } catch(Exception e) {
            AppStatus.showAppStatus(AppStatusType.WARNING, "Nie udało się przygotować formularza!");
        }
    }

    @FXML
    public void addNewEmployeeToDatabase()
    {
        boolean fieldsContainData = !name.getText().isEmpty() && !surname.getText().isEmpty() && !jobTitles.getSelectionModel().isEmpty() && !salary.getText().isEmpty() && !contact.getText().isEmpty();

        if(fieldsContainData)
        {
            JobTitle employeeJobTitle = jobTitles.getSelectionModel().getSelectedItem();
            String employeeName = name.getText();
            String employeeSurname = surname.getText();
            BigDecimal employeeSalary = BigDecimal.valueOf(Double.parseDouble(salary.getText()));
            String employeeContact = contact.getText();

            Employee newEmployee = EmployeeBuilder.builder()
                    .name(employeeName)
                    .surname(employeeSurname)
                    .jobTitle(employeeJobTitle)
                    .salary(employeeSalary)
                    .contact(employeeContact)
                    .build();

            try {
                Dao<Employee, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), Employee.class);
                DatabaseManager.INSTANCE.saveDataToDatabase(dao, newEmployee);
                AppStatus.showAppStatus(AppStatusType.OK, "Dodano pracownika!");
//                clearForm();
            } catch(SQLException e) {
                AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się dodać pracownika!");
            }
        }
        else
            AppUtils.showWarningAlert("Nie wypełniłeś/aś wszystkich pól!");
    }

    private void clearForm()
    {
        name.clear();
        surname.clear();
        jobTitles.getSelectionModel().selectFirst();
        salary.clear();
        contact.clear();
    }
}
