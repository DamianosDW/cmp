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
import org.mindrot.jbcrypt.BCrypt;
import org.passay.CharacterData;
import org.passay.*;
import ovh.damianosdw.cmp.misc.AppStatusType;
import ovh.damianosdw.cmp.misc.CustomDate;
import ovh.damianosdw.cmp.misc.Sex;
import ovh.damianosdw.cmp.utils.AppUtils;
import ovh.damianosdw.cmp.utils.DatabaseManager;
import ovh.damianosdw.cmp.utils.database.models.Employee;
import ovh.damianosdw.cmp.utils.database.models.JobTitle;
import ovh.damianosdw.cmp.utils.database.models.User;
import ovh.damianosdw.cmp.utils.database.models.UserGroup;
import ovh.damianosdw.cmp.utils.database.models.builders.EmployeeBuilder;
import ovh.damianosdw.cmp.utils.database.models.builders.UserBuilder;

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
    private ComboBox<Sex> sex;
    @FXML
    private TextField salary;
    @FXML
    private Label salaryRangeInfo;
    @FXML
    private TextField login;
    @FXML
    private ComboBox<UserGroup> groups;
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

            sex.getItems().addAll(Sex.FEMALE, Sex.MALE);
            groups.getItems().addAll(getUserGroupsFromDatabase());
            // Select "EMPLOYEE" group
            groups.getSelectionModel().select(1);
        } catch(Exception e) {
            AppStatus.showAppStatus(AppStatusType.WARNING, "Nie udało się przygotować formularza!");
        }
    }

    private List<UserGroup> getUserGroupsFromDatabase() throws SQLException
    {
        Dao<UserGroup, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), UserGroup.class);
        return DatabaseManager.INSTANCE.getAllRecordsFromTable(dao);
    }

    @FXML
    public void addNewEmployeeToDatabase()
    {
        boolean fieldsContainData = !name.getText().isEmpty() && !surname.getText().isEmpty() && !jobTitles.getSelectionModel().isEmpty() && !sex.getSelectionModel().isEmpty() && !salary.getText().isEmpty() && !contact.getText().isEmpty();

        if(fieldsContainData)
        {
            JobTitle employeeJobTitle = jobTitles.getSelectionModel().getSelectedItem();
            String employeeName = name.getText();
            String employeeSurname = surname.getText();
            BigDecimal employeeSalary = BigDecimal.valueOf(Double.parseDouble(salary.getText()));
            String employeeContact = contact.getText();
            String employeeSex = sex.getSelectionModel().getSelectedItem().getSex();

            try {
                Employee newEmployee = EmployeeBuilder.builder()
                        .name(employeeName)
                        .surname(employeeSurname)
                        .jobTitle(employeeJobTitle)
                        .salary(employeeSalary)
                        .sex(employeeSex)
                        .workStartDate(new CustomDate())
                        .contact(employeeContact)
                        .build();

                String generatedPassword = generateUserPassword();
                User user = prepareUserInfo(newEmployee);
                user.setPassword(BCrypt.hashpw(generatedPassword, BCrypt.gensalt()));

                Dao<User, Long> dao = DaoManager.createDao(DatabaseManager.INSTANCE.getConnectionSource(), User.class);
                DatabaseManager.INSTANCE.saveDataToDatabase(dao, user);
                AppUtils.showInformationAlert("Konto pracownika zostało utworzone! Szczegółowe informacje:\n" +
                        "Login: " + user.getLogin() + "\n" +
                        "Hasło: " + generatedPassword + "\n" +
                        "Grupa: " + user.getUserGroup());
                AppStatus.showAppStatus(AppStatusType.OK, "Dodano pracownika!");
//                clearForm();
            } catch(SQLException e) {
                AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się dodać pracownika!");
            }
        }
        else
            AppUtils.showWarningAlert("Nie wypełniłeś/aś wszystkich pól!");
    }

    private User prepareUserInfo(Employee employee)
    {
        return UserBuilder.builder()
                .login(login.getText())
                .userGroup(groups.getSelectionModel().getSelectedItem())
                .employee(employee)
                .active(true)
                .build();
    }

    private String generateUserPassword() //TODO RESTORE DEFAULT ACTION
    {
        //return "employee";
        PasswordGenerator generator = new PasswordGenerator();
        CharacterData upperCaseCharacters = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseCharacters);
        upperCaseRule.setNumberOfCharacters(3);

        CharacterData digits = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digits);
        digitRule.setNumberOfCharacters(4);

        CharacterData specialCharacters = new CharacterData() {
            public String getErrorCode()
            {
                return DigestDictionaryRule.ERROR_CODE;
            }

            public String getCharacters()
            {
                return "!@#$%^&*()_+;<>?/";
            }
        };

        CharacterRule specialCharactersRule = new CharacterRule(specialCharacters);
        specialCharactersRule.setNumberOfCharacters(2);

        return generator.generatePassword(10, upperCaseRule, digitRule, specialCharactersRule);
    }
}
