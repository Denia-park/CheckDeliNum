package org.example;

public class ActualData {
    String deliveryNumber;
    String orderNumber;
    boolean isValidBarcodeNumInHashMap;

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setValidBarcodeNumInHashMap(boolean validBarcodeNumInHashMap) {
        this.isValidBarcodeNumInHashMap = validBarcodeNumInHashMap;
    }

    public ActualData(String deliveryNumber, String orderNumber, boolean isValidBarcodeNumInHashMap) {
        this.deliveryNumber = deliveryNumber;
        this.orderNumber = orderNumber;
        this.isValidBarcodeNumInHashMap = isValidBarcodeNumInHashMap;
    }
}
