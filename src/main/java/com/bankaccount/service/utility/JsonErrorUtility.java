package com.bankaccount.service.utility;

/**
 * Author: Claudio Menin
 * Utility class for extracting error code and description from the JSON response
**/
public class JsonErrorUtility {

     public static String extractCodeAndDescription(String errorResponseJson) {
        // Define keys to search for in JSON response
        String codeKey = "\"code\":\"";
        String descriptionKey = "\"description\":\"";

        // Find the indices of the code and description keys in the JSON response
        int codeIndex = errorResponseJson.indexOf(codeKey);
        int descriptionIndex = errorResponseJson.indexOf(descriptionKey);

        // Check if both code and description keys are found
        if (codeIndex != -1 && descriptionIndex != -1) {

            // Extract the code and description from the JSON response
            int codeStartIndex = codeIndex + codeKey.length();
            int codeEndIndex = errorResponseJson.indexOf("\"", codeStartIndex);
            int descriptionStartIndex = descriptionIndex + descriptionKey.length();
            int descriptionEndIndex = errorResponseJson.indexOf("\"", descriptionStartIndex);

            // Check if both code and description are successfully extracted
            if (codeEndIndex != -1 && descriptionEndIndex != -1) {
                String code = errorResponseJson.substring(codeStartIndex, codeEndIndex);
                String description = errorResponseJson.substring(descriptionStartIndex, descriptionEndIndex);
                return code + "-" + description;
            }
        }
        // If extraction fails...
        return "Generic error";
    }

}
