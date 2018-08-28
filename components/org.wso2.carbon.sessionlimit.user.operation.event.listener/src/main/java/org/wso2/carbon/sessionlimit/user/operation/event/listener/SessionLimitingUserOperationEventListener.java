package org.wso2.carbon.sessionlimit.user.operation.event.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.sessionlimit.user.operation.event.listener.util.DataPublisherDbUtil;
import org.wso2.carbon.sessionlimit.user.operation.event.listener.util.SessionCache;
import org.wso2.carbon.user.api.Permission;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.common.AbstractUserOperationEventListener;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SessionLimitingUserOperationEventListener extends AbstractUserOperationEventListener {

    private static Log log = LogFactory.getLog(SessionLimitingUserOperationEventListener.class);

    @Override
    public int getExecutionOrderId() {

        //This listener should execute before the IdentityMgtEventListener
        //Hence the number should be < 1357 (Execution order ID of IdentityMgtEventListener)
        return 1356;
    }


    @Override
    public boolean doPreAuthenticate(String userName, Object credential, UserStoreManager userStoreManager) throws UserStoreException {
        boolean isAllowed = false;
        try {
            int cachedActiveSessions = SessionCache.getActiveSessionCount(userName);
            if (cachedActiveSessions < 1) {
                SessionCache.updateActiveSessionCount(userName);
                int activeSessions = SessionCache.getActiveSessionCount(userName);

                if (activeSessions < 1) {
                    isAllowed = true;
                }
            } else {
                log.warn("Authentication blocked for user: " + userName + ", Reason: Active session limit exceeded.");
            }
        } catch (ExecutionException e) {
            log.error("Error retrieving session information of user [" + userName + "]", e);
        }
        return isAllowed;
    }

    @Override
    public boolean doPostAuthenticate(String userName, boolean authenticated, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPostAuthenticate(userName, authenticated, userStoreManager);
    }

    @Override
    public boolean doPreAddUser(String userName, Object credential, String[] roleList, Map<String, String> claims, String profile, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPreAddUser(userName, credential, roleList, claims, profile, userStoreManager);
    }

    @Override
    public boolean doPostAddUser(String userName, Object credential, String[] roleList, Map<String, String> claims, String profile, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPostAddUser(userName, credential, roleList, claims, profile, userStoreManager);
    }

    @Override
    public boolean doPreUpdateCredential(String userName, Object newCredential, Object oldCredential, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPreUpdateCredential(userName, newCredential, oldCredential, userStoreManager);
    }

    @Override
    public boolean doPostUpdateCredential(String userName, Object credential, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPostUpdateCredential(userName, credential, userStoreManager);
    }

    @Override
    public boolean doPreUpdateCredentialByAdmin(String userName, Object newCredential, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPreUpdateCredentialByAdmin(userName, newCredential, userStoreManager);
    }

    @Override
    public boolean doPostUpdateCredentialByAdmin(String userName, Object credential, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPostUpdateCredentialByAdmin(userName, credential, userStoreManager);
    }

    @Override
    public boolean doPreDeleteUser(String userName, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPreDeleteUser(userName, userStoreManager);
    }

    @Override
    public boolean doPostDeleteUser(String userName, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPostDeleteUser(userName, userStoreManager);
    }

    @Override
    public boolean doPreSetUserClaimValue(String userName, String claimURI, String claimValue, String profileName, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPreSetUserClaimValue(userName, claimURI, claimValue, profileName, userStoreManager);
    }

    @Override
    public boolean doPostSetUserClaimValue(String userName, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPostSetUserClaimValue(userName, userStoreManager);
    }

    @Override
    public boolean doPreSetUserClaimValues(String userName, Map<String, String> claims, String profileName, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPreSetUserClaimValues(userName, claims, profileName, userStoreManager);
    }

    @Override
    public boolean doPostSetUserClaimValues(String userName, Map<String, String> claims, String profileName, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPostSetUserClaimValues(userName, claims, profileName, userStoreManager);
    }

    @Override
    public boolean doPreDeleteUserClaimValues(String userName, String[] claims, String profileName, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPreDeleteUserClaimValues(userName, claims, profileName, userStoreManager);
    }

    @Override
    public boolean doPostDeleteUserClaimValues(String userName, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPostDeleteUserClaimValues(userName, userStoreManager);
    }

    @Override
    public boolean doPreDeleteUserClaimValue(String userName, String claimURI, String profileName, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPreDeleteUserClaimValue(userName, claimURI, profileName, userStoreManager);
    }

    @Override
    public boolean doPostDeleteUserClaimValue(String userName, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPostDeleteUserClaimValue(userName, userStoreManager);
    }

    @Override
    public boolean doPreAddRole(String roleName, String[] userList, Permission[] permissions, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPreAddRole(roleName, userList, permissions, userStoreManager);
    }

    @Override
    public boolean doPostAddRole(String roleName, String[] userList, Permission[] permissions, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPostAddRole(roleName, userList, permissions, userStoreManager);
    }

    @Override
    public boolean doPreDeleteRole(String roleName, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPreDeleteRole(roleName, userStoreManager);
    }

    @Override
    public boolean doPostDeleteRole(String roleName, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPostDeleteRole(roleName, userStoreManager);
    }

    @Override
    public boolean doPreUpdateRoleName(String roleName, String newRoleName, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPreUpdateRoleName(roleName, newRoleName, userStoreManager);
    }

    @Override
    public boolean doPostUpdateRoleName(String roleName, String newRoleName, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPostUpdateRoleName(roleName, newRoleName, userStoreManager);
    }

    @Override
    public boolean doPreUpdateUserListOfRole(String roleName, String[] deletedUsers, String[] newUsers, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPreUpdateUserListOfRole(roleName, deletedUsers, newUsers, userStoreManager);
    }

    @Override
    public boolean doPostUpdateUserListOfRole(String roleName, String[] deletedUsers, String[] newUsers, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPostUpdateUserListOfRole(roleName, deletedUsers, newUsers, userStoreManager);
    }

    @Override
    public boolean doPreUpdateRoleListOfUser(String userName, String[] deletedRoles, String[] newRoles, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPreUpdateRoleListOfUser(userName, deletedRoles, newRoles, userStoreManager);
    }

    @Override
    public boolean doPostUpdateRoleListOfUser(String userName, String[] deletedRoles, String[] newRoles, UserStoreManager userStoreManager) throws UserStoreException {
        return super.doPostUpdateRoleListOfUser(userName, deletedRoles, newRoles, userStoreManager);
    }

    @Override
    public boolean doPreGetUserClaimValue(String userName, String claim, String profileName, UserStoreManager storeManager) throws UserStoreException {
        return super.doPreGetUserClaimValue(userName, claim, profileName, storeManager);
    }

    @Override
    public boolean doPreGetUserClaimValues(String userName, String[] claims, String profileName, Map<String, String> claimMap, UserStoreManager storeManager) throws UserStoreException {
        return super.doPreGetUserClaimValues(userName, claims, profileName, claimMap, storeManager);
    }

    @Override
    public boolean doPostGetUserClaimValue(String userName, String claim, List<String> claimValue, String profileName, UserStoreManager storeManager) throws UserStoreException {
        return super.doPostGetUserClaimValue(userName, claim, claimValue, profileName, storeManager);
    }

    @Override
    public boolean doPostGetUserClaimValues(String userName, String[] claims, String profileName, Map<String, String> claimMap, UserStoreManager storeManager) throws UserStoreException {
        return super.doPostGetUserClaimValues(userName, claims, profileName, claimMap, storeManager);
    }

    /**
     * Get the logged in user's username who is calling the operation
     * @return username
     */

    private String getUser() {
        return CarbonContext.getThreadLocalCarbonContext().getUsername() + "@" +
                CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
    }
}
