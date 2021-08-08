package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "tickets")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketRootDto {
    @XmlElement(name = "ticket")
   private List<TicketDto> ticketDtos;

    public List<TicketDto> getTicketDtos() {
        return ticketDtos;
    }

    public void setTicketDtos(List<TicketDto> ticketDtos) {
        this.ticketDtos = ticketDtos;
    }
}
