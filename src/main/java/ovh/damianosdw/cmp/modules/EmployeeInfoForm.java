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
import lombok.Setter;
import ovh.damianosdw.cmp.misc.AppStatusType;
import ovh.damianosdw.cmp.utils.AppUtils;
import ovh.damianosdw.cmp.utils.DatabaseManager;
import ovh.damianosdw.cmp.utils.database.models.Employee;
import ovh.damianosdw.cmp.utils.database.models.JobTitle;
import ovh.damianosdw.cmp.utils.database.models.builders.EmployeeBuilder;

import java.math.BigDecimal;
import java.sql.SQLException;

public class EmployeeInfoForm
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
    private TextArea contact;

    @Setter
    private static EmployeesModule employeesModule;

    @Setter
    private static Employee employeeInfo;

    @FXML
    void initialize()
    {
        try {
            jobTitles.getItems().addAll(employeesModule.getJobTitlesFromDatabase());
            fillFormWithEmployeeInfo();
        } catch(Exception e) {
            AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się przygotować modułu do pracy!");
            e.printStackTrace();
        }
    }

    private void fillFormWithEmployeeInfo()
    {
        name.setText(employeeInfo.getName());
        surname.setText(employeeInfo.getSurname());
        jobTitles.getSelectionModel().select(employeeInfo.getJobTitle());
        salary.setText(employeeInfo.getSalary() + "");
        contact.setText(employeeInfo.getContact());
    }

    @FXML
    public void updateEmployeeInfo() throws SQLException
    {
        boolean fieldsContainData = !name.getText().isEmpty() && !surname.getText().isEmpty() && !salary.getText().isEmpty() && !contact.getText().isEmpty();

        if(fieldsContainData)
        {
            Dao<Employee, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), Employee.class);
            Employee employee = EmployeeBuilder.builder()
                    .name(name.getText())
                    .surname(surname.getText())
                    .jobTitle(jobTitles.getSelectionModel().getSelectedItem())
                    .salary(BigDecimal.valueOf(Double.parseDouble(salary.getText())))
                    .contact(contact.getText())
                    .build();

            dao.update(employee);
        }
        else
            AppUtils.showWarningAlert("Nie uzupełniłeś/aś wszystkich pól!");
    }
}
