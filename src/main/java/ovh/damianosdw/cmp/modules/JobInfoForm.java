/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.modules;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;
import ovh.damianosdw.cmp.utils.AppUtils;
import ovh.damianosdw.cmp.utils.DatabaseManager;
import ovh.damianosdw.cmp.utils.database.models.JobTitle;
import ovh.damianosdw.cmp.utils.database.models.builders.JobTitleBuilder;

import java.sql.SQLException;

public class JobInfoForm
{
    @FXML
    private TextField jobName;
    @FXML
    private TextField minSalary;
    @FXML
    private TextField maxSalary;
    @FXML
    private TextArea responsibilities;

    @Setter
    private static EmployeesModule employeesModule;
    @Setter
    private static JobTitle jobTitle;

    @FXML
    void initialize()
    {
        fillFormWithJobInfo();
    }

    private void fillFormWithJobInfo()
    {
        String salaryRange = jobTitle.getSalaryRange();

        jobName.setText(jobTitle.getName());

        if(salaryRange.contains("-"))
        {
            minSalary.setText(salaryRange.substring(0, salaryRange.indexOf("-")).trim());
            maxSalary.setText(salaryRange.substring(salaryRange.indexOf("-") + 1).trim());
        }
        else
            minSalary.setText(salaryRange);

        responsibilities.setText(jobTitle.getResponsibilities());
    }

    @FXML
    public void updateJobTitle()
    {
        boolean fieldsContainData = !jobName.getText().isEmpty() && !minSalary.getText().isEmpty() && !maxSalary.getText().isEmpty() && !responsibilities.getText().isEmpty();

        if(fieldsContainData)
        {
            try {
                Dao<JobTitle, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), JobTitle.class);
                JobTitle jobTitle = JobTitleBuilder.builder()
                        .name(jobName.getText())
                        .salaryRange(minSalary.getText() + " - " + maxSalary.getText())
                        .responsibilities(responsibilities.getText())
                        .build();

                DatabaseManager.INSTANCE.updateDataInDatabase(dao, jobTitle);
                employeesModule.showJobTitles();
                AppUtils.showInformationAlert("Zaktualizowano stanowisko!");
            } catch(SQLException e) {
                AppUtils.showWarningAlert("Nie udało się zaktualizować stanowiska! Błąd:\n" + e.getMessage());
            }
        }
        else
            AppUtils.showWarningAlert("Nie uzupełniłeś/aś wszystkich pól formularza!");
    }
}
