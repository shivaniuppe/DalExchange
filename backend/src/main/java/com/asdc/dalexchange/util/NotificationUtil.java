package com.asdc.dalexchange.util;

/**
 * Utility class for generating notification messages and titles based on status.
 */
public class NotificationUtil {

    /**
     * Returns the title for a given status.
     *
     * @param status the status of the request.
     * @return the corresponding title.
     */
    public static String getTitle(String status) {
        return switch (status.toLowerCase()) {
            case "approved" -> "Buy Request Approved";
            case "rejected" -> "Buy Request Rejected";
            case "completed" -> "Purchase Completed";
            case "canceled" -> "Buy Request canceled.";
            default -> "";
        };
    }

    /**
     * Returns the message for a given status and product name.
     *
     * @param status the status of the request.
     * @param productName the name of the product.
     * @return the corresponding message.
     */
    public static String getMessage(String status, String productName) {
        return switch (status.toLowerCase()) {
            case "approved" -> "Your buy request for product " + productName + " has been approved.";
            case "rejected" -> "Your buy request for product " + productName + " has been rejected.";
            case "completed" -> "Congratulations on your new purchase. Your payment for product " + productName + " has been received.";
            case "canceled" -> "The buy request for product " + productName + " has been canceled.";
            default -> "";
        };
    }

    /**
     * Returns the seller's title for a given status.
     *
     * @param status the status of the request.
     * @return the corresponding seller's title.
     */
    public static String getSellerTitle(String status) {
        return switch (status.toLowerCase()) {
            case "approved" -> "Buy Request Approved";
            case "rejected" -> "Buy Request Rejected";
            case "completed" -> "Product Sold";
            case "canceled" -> "Request canceled";
            default -> "";
        };
    }

    /**
     * Returns the seller's message for a given status and product name.
     *
     * @param status the status of the request.
     * @param productName the name of the product.
     * @return the corresponding seller's message.
     */
    public static String getSellerMessage(String status, String productName) {
        return switch (status.toLowerCase()) {
            case "completed" -> "Congratulations! Your product is sold. The buyer has completed the payment for your product " + productName + ".";
            case "canceled" -> "The payment request for your product " + productName + " has been canceled.";
            default -> "";
        };
    }
}
