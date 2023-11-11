package ch.hslu.appe.messages;

import java.util.ArrayList;
import java.util.List;

import ch.hslu.appe.entities.Restocking;

public class RestockFulfilledMessage extends Message {
    private List<Restocking> restockings = new ArrayList<>();

    public List<Restocking> getRestockings() {
        return restockings;
    }

    public void setRestockings(List<Restocking> restockings) {
        this.restockings = restockings;
    }    
}
