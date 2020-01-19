/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.modules;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import lombok.Setter;
import org.controlsfx.glyphfont.Glyph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ovh.damianosdw.cmp.misc.AppStatusType;
import ovh.damianosdw.cmp.utils.AppUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AppStatus
{
    private static Logger logger = LoggerFactory.getLogger(AppStatus.class);
    @Setter
    private static MainModule mainModule;

    private static final double iconSize = 36;
    private static final Glyph ok = AppUtils.getFontAwesome().create("\uf14a").size(iconSize).color(Color.GREEN);
    private static final Glyph warning = AppUtils.getFontAwesome().create("\uf071").size(iconSize).color(Color.valueOf("#ff9900"));
    private static final Glyph error = AppUtils.getFontAwesome().create("\uf057").size(iconSize).color(Color.valueOf("#e60000"));
    private static final Glyph debug = AppUtils.getFontAwesome().create("\uf188").size(iconSize).color(Color.valueOf("#3399ff"));
    private static final Glyph undefined = AppUtils.getFontAwesome().create("\uf059").size(iconSize).color(Color.DARKGRAY);

//    private static Label dbStatus = prepareDbStatusIndicator();

    public static void showAppStatus(AppStatusType type, String message)
    {
        Label appStatus = prepareAppStatusLabel(type, message);
        mainModule.getAppStatusContainer().getChildren().clear();
        mainModule.getAppStatusContainer().getChildren().add(appStatus);
        hideAppStatus();

        //TODO REMOVE IT
//        if(mainModule.getAppStatusContainer().getChildren().contains(dbStatus))
//        {
//            mainModule.getAppStatusContainer().getChildren().remove(dbStatus);
//            mainModule.getAppStatusContainer().getChildren().add(dbStatus);
//        }
//        else
//            mainModule.getAppStatusContainer().getChildren().add(dbStatus);
//
//
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(5);
//                changeDbStatus("orange");
//                TimeUnit.SECONDS.sleep(5);
//                changeDbStatus("red");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
        //TODO REMOVE IT
    }

    private static Label prepareAppStatusLabel(AppStatusType type, String message)
    {
        Label appStatus;

        switch(type.getType())
        {
            case "OK":
                appStatus = new Label("", ok);
                appStatus.setStyle("-fx-text-fill: green;");
                break;
            case "WARNING":
                appStatus = new Label("", warning);
                appStatus.setStyle("-fx-text-fill: #ff9900;");
                break;
            case "ERROR":
                appStatus = new Label("", error);
                appStatus.setStyle("-fx-text-fill: #e60000;");
                break;
            case "DEBUG":
                appStatus = new Label("", debug);
                appStatus.setStyle("-fx-text-fill: #3399ff;");
                break;
            case "UNDEFINED":
            default:
                appStatus = new Label("", undefined);
                appStatus.setStyle("-fx-text-fill: darkgrey;");
                break;
        }

        appStatus.setText(message);
        appStatus.setStyle(appStatus.getStyle() + "-fx-font-size: 18px; -fx-font-weight: bold;");

        return appStatus;
    }

    private static void hideAppStatus()
    {
        ExecutorService hideAppStatusTask = Executors.newSingleThreadExecutor();

        hideAppStatusTask.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                logger.warn("[App Status] AppStatus task interrupted!");
            }

            Platform.runLater(() -> {
                mainModule.getAppStatusContainer().getChildren().clear();

//                if(!mainModule.getAppStatusContainer().getChildren().contains(dbStatus)) //TODO REMOVE IT
//                    mainModule.getAppStatusContainer().getChildren().add(dbStatus);
            });
        });

        hideAppStatusTask.shutdown();
    }
//
//    private static Label prepareDbStatusIndicator() //TODO ADD FUNCTIONALITY
//    {
//        Label dbStatus = new Label();
//        dbStatus.setStyle("-fx-background-radius: 25; -fx-pref-width: 25; -fx-pref-height: 25; -fx-background-color: green;");
//
//        return dbStatus;
//    }
//
//    private static void changeDbStatus(String color)
//    {
//        Platform.runLater(() -> dbStatus.setStyle("-fx-background-radius: 25; -fx-pref-width: 25; -fx-pref-height: 25; -fx-background-color: " + color + ";"));
//    }
}
