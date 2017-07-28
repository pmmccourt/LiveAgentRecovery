import java.util.*

/**
 * Created by Pete on 2/03/2017.
 * Class to insert agent comments from condition report text file into GateWay DB
 */
class CommentsWork(val array: ArrayList<ConditionReportItemsFromDatabase>) {

    val headerIds = getHeaderIdsMod()
    val itemIds = getItemIdsMod()

    //Int array of Header Ids to insert for Routine Inspection Type
    fun getHeaderIdsMod(): ArrayList<Int> {

        val headerIds = arrayListOf<Int>()

        //Get Ids to insert
        for (i in array) {
            if (i.parentId == 0) {
                headerIds.add(i.condReportId - array[0].condReportId)
            }
        }
        return headerIds
    }

    //Int array of Item Ids to insert for Entry/Exit Inspection Types
    fun getItemIdsMod(): ArrayList<Int> {

        val itemIds = arrayListOf<Int>()

        //Get Ids to insert
        for (i in array) {
            if (i.parentId != 0) {
                itemIds.add(i.condReportId - array[0].condReportId)
            }
        }

        return itemIds
    }

    //Insert Comments
    fun insertComments(sql: SqlWork, actionId: Int, inspectionType: String, headerAndAgentComments: ArrayList<ConditionReportItemsFromConditionReport>) {

        sql.deleteExistingIds(actionId)

        //If room type type is Routine
        if (inspectionType == "Routine") {
            //Check to see if Headers from CR and DB Match
            if (conditionReportMatch(headerAndAgentComments, headerIds)) {
                for (i in 0..headerIds.size - 1) {
                    //Check if room has a comment or not
                    if (headerAndAgentComments[i].agentComments != "") {
                        sql.insertComments(actionId, headerIds[i], headerAndAgentComments[i].agentComments, "")
                    }
                }
            }
        }

        //If Inspection Type is Entry or Exit
        else {
            //Check to see if Items from CR and DB match
            if (conditionReportMatch(headerAndAgentComments, itemIds)) {
                for (i in 0..itemIds.size - 1) {
                    //Check if room has a comment or not
                    if (headerAndAgentComments[i].agentComments != "") {
                        sql.insertComments(actionId, itemIds[i], headerAndAgentComments[i].agentComments, "")
                    }
                }
            }
        }
    }

    //Check that ConditionReport.txt file Items match the Database Condition Report fields
    fun conditionReportMatch(headerAndAgentComments: ArrayList<ConditionReportItemsFromConditionReport>, itemsOrHeaders: ArrayList<Int>): Boolean {
        var isTrue = false
        for (i in 0..headerAndAgentComments.size - 1) {
            if (array[itemsOrHeaders[i]].fieldName == headerAndAgentComments[i].roomName) {
                isTrue = true
            } else {
                println("Condition Report txt file does not match Database report")
                println("Database Field: " + array[itemsOrHeaders[i]].fieldName + itemsOrHeaders[i])
                println("CondReport Field: " + headerAndAgentComments[i].roomName + i)
                isTrue = false
                break
            }
        }
        return isTrue
    }
}