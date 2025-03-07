package Capstone.Aeroponics.utils;

public class MessageUtils {

    public static final String LOGIN_SUCCESS_MESSAGE = "Successfully logged in user.";

    public static final String LOGIN_ERROR_MESSAGE = "Failed to login user.";

    public static final String LOGIN_FAILED_MESSAGE = "Incorrect Email or Password.";

    public static final String LOGIN_EXCEPTION_MESSAGE = "Failed to login: {}";

    public static final String RETRIEVE_SUCCESS_MESSAGE = "Successfully retrieved %s.";

    public static final String RETRIEVE_ERROR_MESSAGE = "Failed to retrieve %s.";

    public static final String RETRIEVE_EXCEPTION_MESSAGE = "Failed to retrieve records: {}";

    public static final String SAVE_SUCCESS_MESSAGE = "Successfully saved %s.";

    public static final String SAVE_ERROR_MESSAGE = "Failed to save %s.";

    public static final String SAVE_EXCEPTION_MESSAGE = "Failed to save record: {}";

    public static final String UPDATE_POSTED_STATUS_SUCCESS_MESSAGE = "Successfully updated %s posted status.";

    public static final String UPDATE_POSTED_STATUS_ERROR_MESSAGE = "Failed to update %s posted status.";

    public static final String UPDATE_ARCHIVE_STATUS_SUCCESS_MESSAGE = "Successfully updated %s archive status.";

    public static final String UPDATE_ARCHIVE_STATUS_ERROR_MESSAGE = "Failed to update %s archive status.";

    public static final String DELETE_SUCCESS_MESSAGE = "Successfully deleted %s.";

    public static final String DELETE_ERROR_MESSAGE = "Failed to delete %s.";

    public static final String DELETE_EXCEPTION_MESSAGE = "Failed to delete record/s: {}";

    public static final String RETRIEVE_EMPTY_MESSAGE = "There are no %s.";

    public static final String DOES_NOT_EXIST = "%s does not exist.";

    public static final String FEATURED_ALREADY_EXISTS = "Featured %s already exists.";

    public static final String FEATURED_EMPTY_MESSAGE = "No Featured %s.";

    public static final String HAS_NO_CATEGORIES = "%s has no categories.";

    /**
     * Retrieve success message string.
     *
     * @param value the value
     * @return the string
     */
    public static String retrieveSuccessMessage(String value) {
        return String.format(RETRIEVE_SUCCESS_MESSAGE, value);
    }

    /**
     * Retrieve error message string.
     *
     * @param value the value
     * @return the string
     */
    public static String retrieveErrorMessage(String value) {
        return String.format(RETRIEVE_ERROR_MESSAGE, value);
    }

    /**
     * Retrieve empty message string.
     *
     * @param value the value
     * @return the string
     */
    public static String retrieveEmptyMessage(String value) {
        return String.format(RETRIEVE_EMPTY_MESSAGE, value);
    }

    /**
     * Save success message string.
     *
     * @param value the value
     * @return the string
     */
    public static String saveSuccessMessage(String value) {
        return String.format(SAVE_SUCCESS_MESSAGE, value);
    }

    /**
     * Save error message string.
     *
     * @param value the value
     * @return the string
     */
    public static String saveErrorMessage(String value) {
        return String.format(SAVE_ERROR_MESSAGE, value);
    }

    /**
     * Update posted status success message string.
     *
     * @param value the value
     * @return the string
     */
    public static String updatePostedStatusSuccessMessage(String value) {
        return String.format(UPDATE_POSTED_STATUS_SUCCESS_MESSAGE, value);
    }

    /**
     * Update posted status error message string.
     *
     * @param value the value
     * @return the string
     */
    public static String updatePostedStatusErrorMessage(String value) {
        return String.format(UPDATE_POSTED_STATUS_ERROR_MESSAGE, value);
    }

    /**
     * Update archive status success message string.
     *
     * @param value the value
     * @return the string
     */
    public static String updateArchiveStatusSuccessMessage(String value) {
        return String.format(UPDATE_ARCHIVE_STATUS_SUCCESS_MESSAGE, value);
    }

    /**
     * Update archive status error message string.
     *
     * @param value the value
     * @return the string
     */
    public static String updateArchiveStatusErrorMessage(String value) {
        return String.format(UPDATE_ARCHIVE_STATUS_SUCCESS_MESSAGE, value);
    }

    /**
     * Delete success message string.
     *
     * @param value the value
     * @return the string
     */
    public static String deleteSuccessMessage(String value) {
        return String.format(DELETE_SUCCESS_MESSAGE, value);
    }

    /**
     * Delete error message string.
     *
     * @param value the value
     * @return the string
     */
    public static String deleteErrorMessage(String value) {
        return String.format(DELETE_ERROR_MESSAGE, value);
    }

    /**
     * Does not exist string.
     *
     * @param value the value
     * @return the string
     */
    public static String doesNotExist(String value) {
        return String.format(DOES_NOT_EXIST, value);
    }

    /**
     * Featured already exists string.
     *
     * @param value the value
     * @return the string
     */
    public static String featuredAlreadyExists(String value) {
        return String.format(FEATURED_ALREADY_EXISTS, value);
    }

    /**
     * Has no categories string.
     *
     * @param value the value
     * @return the string
     */
    public static String hasNoCategories(String value) {
        return String.format(HAS_NO_CATEGORIES, value);
    }

    /**
     * Featured empty message string.
     *
     * @param value the value
     * @return the string
     */
    public static String FEATURED_EMPTY_MESSAGE(String value) {
        return String.format(FEATURED_EMPTY_MESSAGE, value);
    }

}