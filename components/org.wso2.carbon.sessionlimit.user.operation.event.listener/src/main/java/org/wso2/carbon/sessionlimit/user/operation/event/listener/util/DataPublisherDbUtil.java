package org.wso2.carbon.sessionlimit.user.operation.event.listener.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataPublisherDbUtil {
    private static volatile DataSource carbonDbDatasource = null;
    private static final Log log = LogFactory.getLog(DataPublisherDbUtil.class);

    private static void initializeConnectDatasource() throws NamingException {
        if (carbonDbDatasource != null) {
            return;
        }

        String dataSourceName = null;
        try {
            Context ctx = new InitialContext();
            carbonDbDatasource = (DataSource) ctx.lookup("jdbc/WSO2CarbonDB");
        } catch (NamingException e) {
            throw new ConfigurationException("DataSource could not be found in mobile-connect.xml:" + e);
        }
    }

    private static Connection getCarbonDbConnection() throws SQLException {
        try {
            initializeConnectDatasource();
        } catch (NamingException e) {
            log.error("Error initializing carbonDb datasource", e);
            return null;
        }

        return carbonDbDatasource.getConnection();
    }

    public static int getActiveSessionCount(String username) throws SQLException {
        String sql = "SELECT COUNT(SESSION_ID) FROM IDN_AUTH_SESSION_INFO WHERE USERNAME = ? AND " +
                "TERMINATION_TIME > unix_timestamp() * 1000";
        Connection con = getCarbonDbConnection();
        PreparedStatement prep = con.prepareStatement(sql);
        prep.setString(1, username);
        ResultSet res = prep.executeQuery();
        if (res.next()) {
            return res.getInt(1);
        }
        return -1;
    }
}
