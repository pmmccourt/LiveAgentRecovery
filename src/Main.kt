import java.util.ArrayList
import java.io.File
import java.nio.file.Paths
import javax.swing.JFileChooser

/**
 * Created by Pete on 17/02/2017.
 * Read in a ConditionReport.txt file and insert comments into SQL DB Gateway Inspection
 */

fun main(args: Array<String>) {

    //FilePath of ConditionReport.txt
    val file = """C:\Users\Pete\Desktop\Pete\Pete Programming\ConditionReport.txt"""

    //Rearrange into Inspection Information and Comments
    val condReportInspectionInfo = InspectionInfoFromConditionReport(file)
    val agentCommentsFromCondReport = AgentCommentsFromConditionReport(condReportInspectionInfo.condReportString, condReportInspectionInfo.inspectionType).getHeaderAndAgentCommentsArray()

    //Variables to establish connection with SQL database
    val sqlServer = "PeterZoePc\\Console"
    val databaseName = "PMSampleDB"//"PMSampleDB"
    val user = "sa"
    val userPassword = "T8twayO74U"

    //Create SQL class and test connection
    val sql = SqlWork(sqlServer, databaseName, user, userPassword)
    val connectionTest = sql.checkConnection()

    //Get and Print list of all possible inspections to match
    val arrayOfPossibleInspections = sql.possibleInspections(condReportInspectionInfo.property)
    println("Subject | Tenant | Property | Owner")

    for (x in 0..arrayOfPossibleInspections.size - 1) {
        println("[$x]: " + arrayOfPossibleInspections[x].subject + " | " + arrayOfPossibleInspections[x].tenantFA + " | " +
                arrayOfPossibleInspections[x].propertyFA + " | " + arrayOfPossibleInspections[x].ownerFA)
    }

    println("Please select Inspection to insert comments into: ")
    var x = readLine()!!.toInt()
    while (x !in 0..arrayOfPossibleInspections.size - 1) {
        println("Please select Inspection to insert comments into: ")
        x = readLine()!!.toInt()
    }

    val inspection = arrayOfPossibleInspections[x]

    println("You have selected Inspection: " + inspection.subject + " | " + inspection.tenantFA + " | " +
            inspection.propertyFA + " | " + inspection.ownerFA)

    //Confirm CR and DB Items/Headers match and insert comments
    val condReportItems = CommentsWork(sql.getConditionReportItemsInformation(inspection.condReportId))
    condReportItems.insertComments(sql, inspection.actionId, condReportInspectionInfo.inspectionType, agentCommentsFromCondReport)

}

