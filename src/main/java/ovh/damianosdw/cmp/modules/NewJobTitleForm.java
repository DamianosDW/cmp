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
import javafx.stage.Stage;
import lombok.Setter;
import ovh.damianosdw.cmp.misc.AppStatusType;
import ovh.damianosdw.cmp.utils.AppUtils;
import ovh.damianosdw.cmp.utils.DatabaseManager;
import ovh.damianosdw.cmp.utils.database.models.JobTitle;
import ovh.damianosdw.cmp.utils.database.models.builders.JobTitleBuilder;

import java.sql.SQLException;

public class NewJobTitleForm
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
    private static Stage stage;

    @FXML
    void initialize()
    {

    }

    @FXML
    public void addJobTitle()
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

                DatabaseManager.INSTANCE.saveDataToDatabase(dao, jobTitle);
                employeesModule.showJobTitles();
                AppUtils.closeAppWindow(stage);
                AppStatus.showAppStatus(AppStatusType.OK, "Dodano nowe stanowisko!");
            } catch(SQLException e) {
                AppUtils.showWarningAlert("Nie udało się zapisać nowego stanowiska!");
            }
        }
        else
            AppUtils.showWarningAlert("Nie uzupełniłeś/aś wszystkich pól formularza!");
    }
}
