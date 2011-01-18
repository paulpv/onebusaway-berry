package org.onebusaway.berry.util;

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.applicationcontrol.ReasonProvider;
import net.rim.device.api.i18n.ResourceBundle;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.ControlledAccessException;

import org.onebusaway.berry.OBAResource;

/**
 * Excellent code ideas borrowed from LogicMail's PermissionsHandler.java
 * 
 * @author pv
 *
 */
public class ObaPermissions
{
    /**
     * List here *all* of the permissions we will attempt to request.
     */
    private static final ApplicationPermissions permissionsRequested = new ApplicationPermissions();

    static
    {
        permissionsRequested.addPermission(ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION);
        permissionsRequested.addPermission(ApplicationPermissions.PERMISSION_FILE_API);
        permissionsRequested.addPermission(ApplicationPermissions.PERMISSION_SERVER_NETWORK);
        permissionsRequested.addPermission(ApplicationPermissions.PERMISSION_INTERNET);
        permissionsRequested.addPermission(ApplicationPermissions.PERMISSION_WIFI);
        permissionsRequested.addPermission(ApplicationPermissions.PERMISSION_ORGANIZER_DATA);

    }

    /**
     * List here the permissionsRequested that are required.
     */
    private static boolean hasMinimumPermissions(ApplicationPermissions permissions)
    {
        return permissions.getPermission(ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION) == ApplicationPermissions.VALUE_ALLOW;
    }

    /**
     * List here the permissionsRequested that are optional.
     */
    private static boolean hasExtendedPermissions(ApplicationPermissions permissions)
    {
        return permissions.getPermission(ApplicationPermissions.PERMISSION_SERVER_NETWORK) == ApplicationPermissions.VALUE_ALLOW
                        && permissions.getPermission(ApplicationPermissions.PERMISSION_INTERNET) == ApplicationPermissions.VALUE_ALLOW
                        && permissions.getPermission(ApplicationPermissions.PERMISSION_WIFI) == ApplicationPermissions.VALUE_ALLOW
                        && permissions.getPermission(ApplicationPermissions.PERMISSION_LOCATION_DATA) == ApplicationPermissions.VALUE_ALLOW
                        && permissions.getPermission(ApplicationPermissions.PERMISSION_IDLE_TIMER) == ApplicationPermissions.VALUE_ALLOW;
    }

    /**
     * List here the permissionsRequested to display a custom reason string for.
     */
    private static class ObaPermissionReasonProvider implements ReasonProvider
    {
        ResourceBundle resources = ResourceBundle.getBundle(OBAResource.BUNDLE_ID, OBAResource.BUNDLE_NAME);

        public String getMessage(int permissionID)
        {
            switch (permissionID)
            {
                case ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION:
                    return resources.getString(OBAResource.PERMISSION_REASON_CROSS_APPLICATION_COMMUNICATION);
                case ApplicationPermissions.PERMISSION_SERVER_NETWORK:
                    return resources.getString(OBAResource.PERMISSION_REASON_SERVER_NETWORK);
                case ApplicationPermissions.PERMISSION_INTERNET:
                    return resources.getString(OBAResource.PERMISSION_REASON_INTERNET);
                case ApplicationPermissions.PERMISSION_WIFI:
                    return resources.getString(OBAResource.PERMISSION_REASON_WIFI);
                default:
                    return resources.getString(OBAResource.PERMISSION_REASON_OTHER);
            }
        }
    }

    private static final ObaPermissionReasonProvider reasonProvider = new ObaPermissionReasonProvider();

    public static void registerReasonProvider()
    {
        try
        {
            ApplicationPermissionsManager apm = ApplicationPermissionsManager.getInstance();
            ApplicationDescriptor ad = ApplicationDescriptor.currentApplicationDescriptor();
            apm.addReasonProvider(ad, reasonProvider);
        }
        catch (ControlledAccessException e)
        {
            // Apparently lack of IPC permissions makes this call non-functional
        }
    }

    public static void unregisterReasonProvider()
    {
        try
        {
            ApplicationPermissionsManager apm = ApplicationPermissionsManager.getInstance();
            apm.removeReasonProvider(reasonProvider);
        }
        catch (ControlledAccessException e)
        {
            // Apparently lack of IPC permissions makes this call non-functional
        }
    }

    /**
     * Check application startup permissions, prompting if necessary.
     * 
     * NOTE: If permissions are required this method WILL BLOCK THE UI/EventQueue!
     * It is highly recommended that this be called at either:
     * 1) Startup, prior to ever creating the UI.
     * -or-
     * 2) In its own thread.
     * 
     * This can also cause undesirable results on BB OS < 5.0; See:
     * http://supportforums.blackberry.com/t5/Java-Development/How-to-Invoke-a-Permissions-request-with-other-applications-also/ta-p/652397
     *
     * @param checkExtended true, if extended permissions should be verified
     * @return true, if the application has sufficient permissions to start
     */
    public static boolean checkStartupPermissions(boolean checkExtended)
    {
        ApplicationPermissionsManager permissionsManager = ApplicationPermissionsManager.getInstance();
        ApplicationPermissions originalPermissions = permissionsManager.getApplicationPermissions();

        boolean permissionsUsable;
        if (checkExtended)
        {
            permissionsUsable = hasMinimumPermissions(originalPermissions) && hasExtendedPermissions(originalPermissions);
        }
        else
        {
            permissionsUsable = hasMinimumPermissions(originalPermissions);
        }

        if (permissionsUsable)
        {
            return true;
        }

        // Request that the user change permissions
        boolean acceptance = permissionsManager.invokePermissionsRequest(permissionsRequested);
        if (!acceptance)
        {
            // If the complete request was not accepted, make sure we at least
            // got the minimum required permissions before starting.
            return hasMinimumPermissions(permissionsManager.getApplicationPermissions());
        }
        else
        {
            return true;
        }
    }
}
