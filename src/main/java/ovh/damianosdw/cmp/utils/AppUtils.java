/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.utils;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import ovh.damianosdw.cmp.misc.AppStatusType;
import ovh.damianosdw.cmp.modules.AppStatus;

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

    public static void showInformationAlertAndWait(String info)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacja");
        alert.setHeaderText(info);
        alert.showAndWait();
    }

    public static void closeAppWindow(Stage stage)
    {
        if(stage != null)
            stage.close();
    }

    public static HBox prepareContainerForActionButtons()
    {
        HBox container = new HBox();
        container.setStyle("-fx-pref-height: 25px; -fx-alignment: center; -fx-spacing: 5;");
        return container;
    }

    public static Button prepareActionButton(Glyph icon)
    {
        Button actionButton = new Button("", icon);
        actionButton.setStyle("-fx-pref-width: 35px; -fx-pref-height: 25px; -fx-font-size: 12px; -fx-background-color: transparent;");
        actionButton.setOnMouseEntered(event -> actionButton.setGraphic(icon.color(Color.valueOf("#0099ff"))));
        actionButton.setOnMouseExited(event -> actionButton.setGraphic(icon.color(Color.BLACK)));
        return actionButton;
    }

    public static Button prepareActionButton(Glyph icon, Color iconColor)
    {
        Button actionButton = new Button("", icon);
        actionButton.setStyle("-fx-pref-width: 35px; -fx-pref-height: 25px; -fx-font-size: 12px; -fx-background-color: transparent;");
        actionButton.setOnMouseEntered(event -> actionButton.setGraphic(icon.color(Color.valueOf("#0099ff"))));
        actionButton.setOnMouseExited(event -> actionButton.setGraphic(icon.color(iconColor)));
        return actionButton;
    }

    public static Button prepareActionButton(Glyph icon, Color iconColor, EventHandler<ActionEvent> eventHandler)
    {
        Button actionButton = new Button("", icon);
        actionButton.setStyle("-fx-pref-width: 35px; -fx-pref-height: 25px; -fx-font-size: 12px; -fx-background-color: transparent; -fx-padding: 0;");
        actionButton.setOnMouseEntered(event -> actionButton.setGraphic(icon.color(Color.valueOf("#0099ff"))));
        actionButton.setOnMouseExited(event -> actionButton.setGraphic(icon.color(iconColor)));
        actionButton.setOnAction(eventHandler);
        return actionButton;
    }

    public static Stage showModuleInNewWindowAndGetStage(String windowName, Parent container)
    {
        Stage stage = new Stage();
        stage.setTitle(windowName);
        stage.setResizable(false);
        stage.setScene(new Scene(container));
        stage.show();
        AppStatus.showAppStatus(AppStatusType.OK, "Załadowano okno: " + windowName);
        return stage;
    }
}
