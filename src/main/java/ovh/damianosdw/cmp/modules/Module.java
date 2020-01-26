/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.modules;

import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;
import ovh.damianosdw.cmp.exceptions.ModuleLoadErrorException;
import ovh.damianosdw.cmp.misc.AppStatusType;
import ovh.damianosdw.cmp.utils.FxmlUtils;

public abstract class Module
{
    @Getter
    protected String fxmlPath;
    @Getter
    protected String moduleName;

    @Setter
    protected static MainModule mainModule;

    public Module(String fxmlPath, String moduleName)
    {
        this.fxmlPath = fxmlPath;
        this.moduleName = moduleName;
    }

    public void load() throws ModuleLoadErrorException
    {
        try {
            mainModule.getMainContainer().setCenter(FxmlUtils.loadFxmlWindow(fxmlPath));
            AppStatus.showAppStatus(AppStatusType.OK, "Załadowano moduł: " + moduleName);
        } catch(Exception e) {
            throw new ModuleLoadErrorException(e.getMessage() + "\n" + e.fillInStackTrace());
        }
    }

    public Parent loadModuleToContainer() throws ModuleLoadErrorException
    {
        try {
            Parent parent = FxmlUtils.loadFxmlWindow(fxmlPath);

            if(MainModule.isDebugMode())
                AppStatus.showAppStatus(AppStatusType.DEBUG, "Załadowano moduł do kontenera: " + moduleName);

            return parent;
        } catch(Exception e) {
            throw new ModuleLoadErrorException(e.getMessage() + "\n" + e.fillInStackTrace());
        }
    }

    public void refresh() throws ModuleLoadErrorException
    {
        try {
            mainModule.getMainContainer().setCenter(null);
            mainModule.getMainContainer().setCenter(FxmlUtils.loadFxmlWindow(fxmlPath));
            AppStatus.showAppStatus(AppStatusType.OK, "Ponownie załadowano moduł: " + moduleName);
        } catch(Exception e) {
            throw new ModuleLoadErrorException(e.getMessage() + "\n" + e.fillInStackTrace());
        }
    }

    public abstract void configureModule();
}
