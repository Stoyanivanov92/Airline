package softuni.exam.models.dto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
public class TicketDto {
    @XmlElement(name = "serial-number")
    private String serialNumber;
    @XmlElement
    private BigDecimal price;
    @XmlElement(name = "take-off")
    private String takeoff;
    @XmlElement(name = "from-town")
    private TownByNameDto fromTown;
    @XmlElement(name = "to-town")
    private TownByNameDto toTown;
    @XmlElement
    private PassengerByEmailDto passenger;
    @XmlElement
    private PlaneByRegNumberDto plane;

    @Size(min = 2)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    @Positive
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTakeoff() {
        return takeoff;
    }

    public void setTakeoff(String takeoff) {
        this.takeoff = takeoff;
    }

    public TownByNameDto getFromTown() {
        return fromTown;
    }

    public void setFromTown(TownByNameDto fromTown) {
        this.fromTown = fromTown;
    }

    public TownByNameDto getToTown() {
        return toTown;
    }

    public void setToTown(TownByNameDto toTown) {
        this.toTown = toTown;
    }

    public PassengerByEmailDto getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerByEmailDto passenger) {
        this.passenger = passenger;
    }

    public PlaneByRegNumberDto getPlane() {
        return plane;
    }

    public void setPlane(PlaneByRegNumberDto plane) {
        this.plane = plane;
    }
}
