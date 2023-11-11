package ch.hslu.appe.monitoring;

public final class BusinessEvent {
    private String businessId;
    private String businessType;
    private String businessSubtype;
    private String businessDescription;
    public String getBusinessId() {
        return businessId;
    }
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    public String getBusinessType() {
        return businessType;
    }
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
    public String getBusinessSubtype() {
        return businessSubtype;
    }
    public void setBusinessSubtype(String businessSubtype) {
        this.businessSubtype = businessSubtype;
    }
    public String getBusinessDescription() {
        return businessDescription;
    }
    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }
    @Override
    public String toString() {
        return String.format("business_id=\"%s\" business_type=\"%s\" business_subtype=\"%s\" business_description=\"%s\"", businessId, businessType, businessSubtype, businessDescription);
    } 
}
