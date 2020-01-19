/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp;

import javafx.application.Application;
import javafx.stage.Stage;
import ovh.damianosdw.cmp.modules.MainModule;
import ovh.damianosdw.cmp.utils.AppUtils;

public class Main extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        MainModule mainModule = new MainModule();
        mainModule.load(primaryStage);

        primaryStage.setTitle(AppUtils.APP_NAME);
        primaryStage.setResizable(false);
        primaryStage.setMaxWidth(1200);
        primaryStage.setMaxHeight(850);
        primaryStage.setOnCloseRequest(event -> {
            AppUtils.stopApp();
        });
        primaryStage.show();
    }
}
