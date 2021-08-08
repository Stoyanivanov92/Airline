package softuni.exam.models.dto;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "planes")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlaneRootDto {
    @XmlElement(name = "plane")
    List<PlaneDto> planeDtos;

    public List<PlaneDto> getPlaneDtos() {
        return planeDtos;
    }

    public void setPlaneDtos(List<PlaneDto> planeDtos) {
        this.planeDtos = planeDtos;
    }
}
