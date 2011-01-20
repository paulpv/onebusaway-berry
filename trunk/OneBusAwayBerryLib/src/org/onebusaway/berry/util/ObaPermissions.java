package org.onebusaway.berry.util;

import java.util.Enumeration;
import java.util.Hashtable;

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
public class ObaPermissions {
    private static final MyApplicationPermissionsTable permissionsTable = new MyApplicationPermissionsTable();

    public static boolean checkPermissions(boolean checkOptional) {
        return permissionsTable.checkPermissions(checkOptional);
    }

    public static class MyApplicationPermissionsTable extends Hashtable {
        private static ReasonProvider reasonProvider = null;

        static ResourceBundle         resources      = ResourceBundle.getBundle(OBAResource.BUNDLE_ID, OBAResource.BUNDLE_NAME);

        public static String getString(int resourceKey) {
            return resources.getString(resourceKey);
        }

        protected class MyApplicationPermission {
            private final int     id;
            private final String  name;
            private final boolean required;
            private final String  reason;

            public MyApplicationPermission(int id, String name, boolean required, int resourceKey) {
                this.id = id;
                this.name = name;
                this.required = required;
                this.reason = getString(resourceKey);
            }

            public String toString() {
                return "{id=" + id + ", name=\"" + name + "\", required=" + required + ", reason=\"" + reason + "\"";
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public boolean getRequired() {
                return required;
            }

            public String getReason() {
                return reason;
            }
        }

        public MyApplicationPermissionsTable() {
            //
            // NOTE: Some BBs, especially ones under BES policy control, may have these permanently disabled.
            // Think *very* carefully about what permissions you *absolutely require* and
            // which ones you can still run without...even if your app sucks without them.
            //

            //put(new WmcApplicationPermission(ApplicationPermissions.PERMISSION_BLUETOOTH, //
            //                "Required to access bluetooth devices and profiles."));
            //put(new WmcApplicationPermission(ApplicationPermissions.PERMISSION_DEVICE_SETTINGS, //
            //                "TODO:(pv) A workaround for enabling the backlight?"));
            //put(new WmcApplicationPermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION, //
            //                "TODO:(pv) A workaround for enabling the backlight?"));
            //put(new WmcApplicationPermission(ApplicationPermissions.PERMISSION_APPLICATION_MANAGEMENT, //
            //                "TODO:(pv) Could be used to manage the installed app modules."));
            //put(new WmcApplicationPermission(ApplicationPermissions.PERMISSION_FILE_API, //
            //                "TODO:(pv) Required to write battery logging info."));
            //put(new WmcApplicationPermission(ApplicationPermissions.PERMISSION_SERVER_NETWORK, //
            //                "Required for corporate MDS connections."));

            //
            // REQUIRED:
            //
            put(new MyApplicationPermission(
                            ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION, //
                            "PERMISSION_CROSS_APPLICATION_COMMUNICATION", true,
                            OBAResource.PERMISSION_REASON_CROSS_APPLICATION_COMMUNICATION));

            //
            // Undecided: At least ONE of these needs to be enabled...
            //
            //permissionsOptional.addPermission(ApplicationPermissions.PERMISSION_SERVER_NETWORK);
            put(new MyApplicationPermission(ApplicationPermissions.PERMISSION_INTERNET, //
                            "PERMISSION_INTERNET", true, OBAResource.PERMISSION_REASON_INTERNET));
            put(new MyApplicationPermission(ApplicationPermissions.PERMISSION_WIFI, //
                            "PERMISSION_WIFI", true, OBAResource.PERMISSION_REASON_WIFI));

            //
            // Optional:
            //
            put(new MyApplicationPermission(ApplicationPermissions.PERMISSION_LOCATION_DATA, //
                            "PERMISSION_LOCATION_DATA", false, OBAResource.PERMISSION_REASON_LOCATION_DATA));
            put(new MyApplicationPermission(ApplicationPermissions.PERMISSION_IDLE_TIMER, //
                            "PERMISSION_IDLE_TIMER", false, OBAResource.PERMISSION_REASON_IDLE_TIMER));

            registerReasonProvider();
        }

        protected void registerReasonProvider() {
            synchronized (this) {
                try {
                    if (reasonProvider != null) {
                        unregisterReasonProvider();
                        reasonProvider = null;
                    }
                    reasonProvider = new ReasonProvider()
                    {
                        public String getMessage(int permissionID) {
                            MyApplicationPermission wmcApplicationPermission = (MyApplicationPermission) get(permissionID);
                            if (wmcApplicationPermission == null) {
                                return getString(OBAResource.PERMISSION_REASON_OTHER);
                            }
                            return wmcApplicationPermission.getReason();
                        }
                    };
                    ApplicationPermissionsManager apm = ApplicationPermissionsManager.getInstance();
                    ApplicationDescriptor ad = ApplicationDescriptor.currentApplicationDescriptor();
                    apm.addReasonProvider(ad, reasonProvider);

                }
                catch (ControlledAccessException e) {
                    // Apparently lack of IPC permissions makes this call non-functional
                }
            }
        }

        protected void unregisterReasonProvider() {
            synchronized (this) {
                try {
                    ApplicationPermissionsManager apm = ApplicationPermissionsManager.getInstance();
                    apm.removeReasonProvider(reasonProvider);
                    reasonProvider = null;
                }
                catch (ControlledAccessException e) {
                    // Apparently lack of IPC permissions makes this call non-functional
                }
            }
        }

        public MyApplicationPermission put(int key, MyApplicationPermission value) {
            return (MyApplicationPermission) put(new Integer(key), value);
        }

        public MyApplicationPermission put(MyApplicationPermission wmcApplicationPermission) {
            return put(wmcApplicationPermission.getId(), wmcApplicationPermission);
        }

        public Object put(Object key, Object value) {
            if (!(key instanceof Integer)) {
                throw new IllegalArgumentException("key must be of type Integer");
            }
            if (!(value instanceof MyApplicationPermission)) {
                throw new IllegalArgumentException("key must be of type WmcApplicationPermission");
            }
            return super.put(key, value);
        }

        public MyApplicationPermission get(int key) {
            return (MyApplicationPermission) get(new Integer(key));
        }

        public Object get(Object key) {
            if (!(key instanceof Integer)) {
                throw new IllegalArgumentException("key must be of type Integer");
            }
            return super.get(key);
        }

        private boolean hasPermissions(ApplicationPermissions requestedPermissions, boolean requireAll) {
            MyApplicationPermission permission;
            int permissionId;
            int permissionValue;

            Enumeration enumValues = elements();
            while (enumValues.hasMoreElements()) {
                permission = ((MyApplicationPermission) enumValues.nextElement());
                permissionId = permission.getId();
                permissionValue = requestedPermissions.getPermission(permissionId);
                if (permissionValue != ApplicationPermissions.VALUE_ALLOW) {
                    if (requireAll && permission.getRequired()) {
                        return false;
                    }
                }
            }
            return true;
        }

        private ApplicationPermissions getAllRequestedPermissions() {
            ApplicationPermissions permissions = new ApplicationPermissions();

            MyApplicationPermission wtcPermission;
            Enumeration wtcPermissions = elements();
            while (wtcPermissions.hasMoreElements()) {
                wtcPermission = ((MyApplicationPermission) wtcPermissions.nextElement());
                permissions.addPermission(wtcPermission.getId());
            }

            return permissions;
        }

        /**
         * Checks if there are any new permissions required since the app was last run/upgraded.
         * 
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
        public boolean checkPermissions(boolean requireAll) {
            ApplicationPermissionsManager apm = ApplicationPermissionsManager.getInstance();
            ApplicationPermissions originalPermissions = apm.getApplicationPermissions();

            if (hasPermissions(originalPermissions, requireAll)) {
                return true;
            }

            // Request that the user change permissions
            boolean acceptance = apm.invokePermissionsRequest(getAllRequestedPermissions());
            if (!acceptance) {
                // If the complete request was not accepted, make sure we at least
                // got the minimum required permissions before starting.
                return hasPermissions(apm.getApplicationPermissions(), false);
            }

            return true;
        }
    }
}
