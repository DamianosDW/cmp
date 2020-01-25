/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import ovh.damianosdw.cmp.modules.LoginSystem;
import ovh.damianosdw.cmp.utils.AppUtils;

public class Main extends Application
{
    @Getter
    private static Stage mainStage;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        LoginSystem loginSystem = new LoginSystem();

        primaryStage.setScene(new Scene(loginSystem.loadModuleToContainer()));
        primaryStage.setTitle(AppUtils.APP_NAME);
        primaryStage.setResizable(false);
        primaryStage.setMaxWidth(1200);
        primaryStage.setMaxHeight(850);
        primaryStage.setOnCloseRequest(event -> {
            AppUtils.stopApp();
        });
        mainStage = primaryStage;
        primaryStage.show();
    }
}
