import java.util.ArrayList

/**
 * Created by Pete on 18/02/2017.
 * Class to extract Inspection Information from ConditionReport.txt file
 */

class InspectionInfoFromConditionReport(val file: String) {

    val condReportString = FileToString(file).getArrayList()

    val agent: String
        get() = strip(this.condReportString[1]!!, "Agent: ")

    val startTime: String
        get() = strip(this.condReportString[2]!!, "Start time: ")

    val endTime: String
        get() = strip(this.condReportString[3]!!, "End time: ")

    val inspectionType: String
        get() = strip(this.condReportString[4]!!, "Inspection Type: ")

    val inspectionStatus: String
        get() = strip(this.condReportString[5]!!, "Inspection Status: ")

    val property: String
        get() = strip(this.condReportString[6]!!, "Property: ")

    fun strip(line: String, word: String): String {
        if (line.contains(word)) {
            val wordz = line.drop(word.length)
            return wordz
        } else {
            return "Unable to find " + word
        }
    }


}