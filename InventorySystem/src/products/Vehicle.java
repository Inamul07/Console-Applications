package products;

import java.io.Serializable;

public class Vehicle implements Serializable {

    private static final long serialVersionUID = 2L;

    private final String vehicleName;
    private final String manufacturer;
    private final int makeYear;
    private final boolean isCar;

    public Vehicle(String vehicleName, String manufacturer, int makeYear, boolean isCar) {
        this.vehicleName = vehicleName;
        this.manufacturer = manufacturer;
        this.makeYear = makeYear;
        this.isCar = isCar;
    }

    @Override
    public String toString() {
        return "{Vehicle: " + vehicleName + ", " + manufacturer + ", " + makeYear + ", " + (isCar? "Car": "Bike") + "}";
    }
}
