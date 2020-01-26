/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.Getter;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AppUtils
{
    @Getter
    private static final GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");

    public static final String APP_NAME = "Company Management Panel";

    public static void stopApp()
    {
        DatabaseManager.INSTANCE.disconnect();
        Platform.exit();
        System.exit(0);
    }

    public static void disableAndEnableButtonAfterDelay(Button button)
    {
        ExecutorService task = Executors.newSingleThreadExecutor();

        task.execute(() -> {
            Platform.runLater(() -> button.setDisable(true));

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException ignored) { }

            Platform.runLater(() -> button.setDisable(false));
        });

        task.shutdown();
    }

    public static void showWarningAlert(String warning)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ostrzeżenie");
        alert.setHeaderText(warning);
        alert.show();
    }

    public static void showInformationAlert(String info)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacja");
        alert.setHeaderText(info);
        alert.show();
    }

    public static void closeAppWindow(Stage stage)
    {
        if(stage != null)
            stage.close();
    }
}
