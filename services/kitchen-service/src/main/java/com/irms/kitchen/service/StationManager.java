package com.irms.kitchen.service;

import com.irms.kitchen.domain.StationType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StationManager {

    /**
     * Determines the appropriate station for a given menu item.
     * In a real application, this might query the menu-service or a local cache
     * to get category information for the menu item.
     */
    public StationType determineStation(UUID menuItemId, String menuItemName) {
        if (menuItemName == null) return StationType.GENERAL;
        
        String lowerName = menuItemName.toLowerCase();
        
        if (lowerName.contains("grill") || lowerName.contains("steak") || lowerName.contains("burger") || lowerName.contains("bbq")) {
            return StationType.GRILL;
        } else if (lowerName.contains("fry") || lowerName.contains("crispy") || lowerName.contains("chips")) {
            return StationType.FRYER;
        } else if (lowerName.contains("cake") || lowerName.contains("ice cream") || lowerName.contains("sweet") || lowerName.contains("dessert")) {
            return StationType.DESSERT;
        } else if (lowerName.contains("drink") || lowerName.contains("juice") || lowerName.contains("coffee") || lowerName.contains("tea") || lowerName.contains("cola") || lowerName.contains("beer")) {
            return StationType.DRINK;
        }
        
        return StationType.GENERAL;
    }
}
