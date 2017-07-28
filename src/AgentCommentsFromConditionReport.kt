import java.util.*

/**
 * Created by Pete on 20/02/2017.
 * Class to extract Headers/Items and corresponding Agent Comments from Condition Report Stringsz
 */
class AgentCommentsFromConditionReport constructor(val condReportString: ArrayList<String?>, val inspectionType: String) {

    var headerOrItemAndAgentComments = ArrayList<ConditionReportItemsFromConditionReport>()
    var headerArray = ArrayList<Int>()

    //Function to get Header/Items and Agent Comments
    //Return the ArrayList of HeaderOrItem and Agent Comment
    fun getHeaderAndAgentCommentsArray(): ArrayList<ConditionReportItemsFromConditionReport> {
        //If Inspection is type Routine
        if (inspectionType == "Routine") {
            for (i in 11..condReportString.size - 1 step 5) {
                headerOrItemAndAgentComments.add(ConditionReportItemsFromConditionReport(condReportString[i]!!, condReportString[i + 1]!!.trimStart()))
            }
        }
        //Else if Entry or Exit
        else {
            //Get Array of Header Ids
            for (i in 11..condReportString.size - 1) {
                //Find Headers
                if (!condReportString[i].isNullOrBlank()) {
                    if (condReportString[i]?.get(0)?.isLetter()!!) {
                        headerArray.add(i)
                    }
                }
            }
            val firstId = getFirstIdOfComment(condReportString[headerArray[0] + 4] as String)

            //Add room Items to headerAndAgent Comments Arrary except the last room
            for (i in 0 until headerArray.size - 1) {
                for (j in headerArray[i] + 4..headerArray[i + 1] - 1 step 3) {
                    headerOrItemAndAgentComments.add(ConditionReportItemsFromConditionReport(condReportString[j - 1]!!.trimStart(), condReportString[j]!!.substring(firstId)))
                }
            }
            // Last Room
            for (i in headerArray[headerArray.size - 1] + 4..condReportString.size - 1 step 3) {
                headerOrItemAndAgentComments.add(ConditionReportItemsFromConditionReport(condReportString[i - 1]!!.trimStart(), condReportString[i]!!.substring(firstId)))
            }
        }
        //Return headerOrItemAndAgentComments Array
        return headerOrItemAndAgentComments
    }

    //To assist with trimming start of comment
    //This is broken > Wont work if first word has single character or Int
    fun getFirstIdOfComment(comment: String): Int {
        var id = 0
        for (i in 0..comment.length) {
            if (comment[i].isLetter() and comment[i + 1].isLetter()) {
                id = i
                break
            }
        }
        return id
    }
}