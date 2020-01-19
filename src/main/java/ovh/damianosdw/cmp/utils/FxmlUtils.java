package ovh.damianosdw.cmp.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ovh.damianosdw.cmp.exceptions.ModuleLoadErrorException;

public class FxmlUtils
{
    private static final Logger logger = LoggerFactory.getLogger(FxmlUtils.class);

    public static AnchorPane loadFxmlWindow(String fxmlPath) throws ModuleLoadErrorException
    {
        try {
            FXMLLoader loader = new FXMLLoader(FxmlUtils.class.getResource(fxmlPath));
            return loader.load();
        } catch (Exception e) {
            logger.error("[CMP] Nie udało się załadować okna modułu! Szczegółowe informacje: " + e.fillInStackTrace());
            throw new ModuleLoadErrorException(e.getMessage() + "\n" + e.fillInStackTrace());
        }
    }

    public static FXMLLoader getFxmlLoader(String fxmlPath)
    {
        return new FXMLLoader(FxmlUtils.class.getResource(fxmlPath));
    }
}
