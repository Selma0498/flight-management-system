package notifications.web.converters;

/**
 * Serves as a converter class from the Passenger class in passenger-microservice
 * i.e. contains all the same data saved within a local object - no interaction with the
 * passenger-microservice or its database
 **/
public class PassengerData {

    // only part of the data from passenger-microservice is relevant for notifications
    // and is therefore saved here
    private Long passengerId;
    private EUserRoleData role;
    private String name;
    private String surname;

    public PassengerData(Long passengerId, String name, String surname) {
        this.passengerId = passengerId;
        this.name = name;
        this.surname = surname;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public EUserRoleData getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

}
