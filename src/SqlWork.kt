import java.sql.DriverManager
import java.util.*

/**
 * Created by Pete on 21/02/2017.
 * Class for all SQL Worktest
 */
class SqlWork(val sqlServer: String, val databaseName: String, val user: String, val userPassword: String) {

    //Try to establish connection with SQL Database
    fun checkConnection() {
        try {
            //Try to establish connection with SQL Database
            val connectionUrl = "jdbc:sqlserver://" + sqlServer + ";" + "databaseName=" + databaseName + "; user=" + user + ";password=" + userPassword + ";"
            val con = DriverManager.getConnection(connectionUrl)
            con.close()
        } catch (e: Exception) {
            System.err.println("SQL Connection Error")
            System.err.println(e.message)
        }
    }

    //Find all possible Inspections for user to match correct
    fun possibleInspections(property: String): ArrayList<InspectionInfoFromDatabase> {

        val sqlResults = ArrayList<InspectionInfoFromDatabase>()
        val property = property.replace("'", "''")

        try {
            //Try to establish connection with SQL Database
            val connectionUrl = "jdbc:sqlserver://" + sqlServer + ";" + "databaseName=" + databaseName + "; user=" + user + ";password=" + userPassword + ";"
            val con = DriverManager.getConnection(connectionUrl)

            //Create and Process SQL query
            val stmt = con.createStatement()

            val rs = stmt.executeQuery("SELECT DISTINCT act.ID as actID, act.ConditionReportID, prop.ID as propID, act.Subject, own.FileAs as ownFA, prop.FileAs as propFA, ten.FileAs as tenFA " +
                    "from Actions act " +
                    "INNER JOIN Properties prop " +
                    "on act.PropertyID = prop.ID " +
                    "INNER JOIN Owners own " +
                    "on own.ID = prop.OwnerID " +
                    "INNER Join Tenants ten " +
                    "on ten.PropertyID = prop.ID " +
                    "where (act.Action = 3) " +
                    "and (act.Deleted = 0) " +
                    "and (act.Completed is null) " +
                    "and prop.Street like '%" + property + "%'")

            //Convert query to ArrayList
            while (rs.next()) {
                val actionId = rs.getInt("actID")
                val condReportId = rs.getInt("ConditionReportID")
                val propertyId = rs.getInt("propID")
                val subject = rs.getString("Subject")
                val ownerFA = rs.getString("ownFA")
                val propertyFA = rs.getString("propFA")
                val tenantFA = rs.getString("tenFA")
                sqlResults.add(InspectionInfoFromDatabase(actionId, condReportId, propertyId, subject, ownerFA, propertyFA, tenantFA))
            }
            con.close()

            //Catch Exception
        } catch (e: Exception) {
            System.err.println("SQL Statement Error")
            System.err.println(e.message)
        }
        return sqlResults
    }

    //Deletes all comments from Inspection
    fun deleteExistingIds(actionId: Int) {
        try {
            //Try to establish connection with SQL Database
            val connectionUrl = "jdbc:sqlserver://" + sqlServer + ";" + "databaseName=" + databaseName + "; user=" + user + ";password=" + userPassword + ";"
            val con = DriverManager.getConnection(connectionUrl)

            //Create and Process SQL query
            val stmt = con.createStatement()

            //Delete Existing Comments from InspectionComment
            stmt.executeUpdate("delete from ConditionReports where ActionID = " + actionId)
            con.close()

            //Catch Exception
        } catch (e: Exception) {
            System.err.println("SQL Statement Error")
            System.err.println(e.message)
        }
    }

    //Condition Report Information
    fun getConditionReportItemsInformation(conditionReportId: Int): ArrayList<ConditionReportItemsFromDatabase> {

        val sqlResults = ArrayList<ConditionReportItemsFromDatabase>()

        try {
            //Try to establish connection with SQL Database
            val connectionUrl = "jdbc:sqlserver://" + sqlServer + ";" + "databaseName=" + databaseName + "; user=" + user + ";password=" + userPassword + ";"
            val con = DriverManager.getConnection(connectionUrl)

            //Create and Process SQL query
            val stmt = con.createStatement()
            val rs = stmt.executeQuery("select ID, Fieldname, Type, ParentID from ConditionReportItems where ConditionReportFormatID = " + conditionReportId + " and (Type = 0 or Type = 1) order by ID asc")

            //Convert query to ArrayList
            while (rs.next()) {
                val id = rs.getInt("ID")
                val fieldName = rs.getString("Fieldname")
                val type = rs.getInt("Type")
                val parentId = rs.getInt("ParentId")
                sqlResults.add(ConditionReportItemsFromDatabase(id, fieldName, type, parentId))
            }
            con.close()

            //Catch Exception
        } catch (e: Exception) {
            System.err.println("SQL Statement Error")
            System.err.println(e.message)
        }
        return sqlResults
    }

    //Insert Comments
    fun insertComments(actionId: Int, itemNumber: Int, agentComment: String, tenantComment: String) {

        val agentComment = agentComment.replace("'", "''")
        val tenantComment = tenantComment.replace("'", "''")

        try {
            //Try to establish connection with SQL Database
            val connectionUrl = "jdbc:sqlserver://" + sqlServer + ";" + "databaseName=" + databaseName + "; user=" + user + ";password=" + userPassword + ";"
            val con = DriverManager.getConnection(connectionUrl)

            //Create and Process SQL query
            val stmt = con.createStatement()

            //Delete Existing Comments from InspectionComment
            stmt.executeUpdate("insert into ConditionReports (ActionID, ItemNumber, Check1, Check2, Check3, Check4, Check5, TenantComment , AgentComment, GUID, ModifiedDateUTC) " +
                    "values (" + actionId + ", " + itemNumber + ", 0, 0, 0, 0, 0, '" + tenantComment + "', '" + agentComment + "', NEWID(), CURRENT_TIMESTAMP)")

            con.close()

            //Catch Exception
        } catch (e: Exception) {
            System.err.println("SQL Statement Error")
            System.err.println(e.message)
        }
    }

}