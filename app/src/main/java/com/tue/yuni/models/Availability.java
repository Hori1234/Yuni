package com.tue.yuni.models;

/**
 * The three levels of availability, as defined in the database
 */
public enum Availability {
    // The product is fully in stock
    IN_STOCK,
    // The product is low on stock and might run out soon
    LOW_STOCK,
    // The product is out of stock and not available anymore for the customer
    OUT_OF_STOCK,
}
