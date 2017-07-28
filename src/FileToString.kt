import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * Created by Pete on 25/03/2017.
 * Class to check if filePath exists and convert to String Array List
 */

class FileToString(path: String) {

    val filePath = Paths.get(path)

    val exists = checkPath(path)

    //Convert String to ArrayList
    fun getArrayList(): ArrayList<String?> {

        //Read file path into String ArrayList
        val condReportString = ArrayList<String?>()

        try {
            filePath.toFile().forEachLine { condReportString.add(it) }
        } catch (e: Exception) {
            System.err.println("FilePath exception! ")
            System.err.println(e.message)
        }
        return condReportString
    }

    //Function to check if Path exists
    fun checkPath(filePath: String): Boolean {

        val path = Paths.get(filePath)

        if (Files.exists(path)) {
            println(path.fileName + " File exists")
            return true
        } else {
            println(path.fileName + " doesn't exist")
            return false
        }
    }

}

