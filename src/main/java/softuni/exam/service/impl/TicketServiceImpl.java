package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TicketDto;
import softuni.exam.models.dto.TicketRootDto;
import softuni.exam.models.entity.Passenger;
import softuni.exam.models.entity.Plane;
import softuni.exam.models.entity.Ticket;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.repository.TicketRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TicketService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    private static final String PATH_FILE_TICKETS = "src/main/resources/files/xml/tickets.xml";

    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final TownRepository townRepository;
    private final PassengerRepository passengerRepository;
    private final PlaneRepository planeRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, TownRepository townRepository, PassengerRepository passengerRepository, PlaneRepository planeRepository) {
        this.ticketRepository = ticketRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.townRepository = townRepository;
        this.passengerRepository = passengerRepository;
        this.planeRepository = planeRepository;
    }

    @Override
    public boolean areImported() {
        if (ticketRepository.count() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String readTicketsFileContent() throws IOException {
       return Files.readString(Path.of(PATH_FILE_TICKETS));
    }

    @Override
    public String importTickets() throws JAXBException, FileNotFoundException {
        TicketRootDto ticketRootDto = xmlParser.fromFile(PATH_FILE_TICKETS, TicketRootDto.class);
        StringBuilder stringBuilder = new StringBuilder();
        List<TicketDto> ticketDtos = ticketRootDto.getTicketDtos();
        for (TicketDto ticketDto : ticketDtos) {
            if (!validationUtil.isValid(ticketDto)) {
                stringBuilder.append("Invalid Ticket");
                stringBuilder.append(System.lineSeparator());
            } else {
                Ticket ticket = modelMapper.map(ticketDto, Ticket.class);
                ticket.setTakeoff(LocalDateTime.parse(ticketDto.getTakeoff(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                Town townFrom = townRepository.findByName(ticketDto.getFromTown().getName());
                Town townTo = townRepository.findByName(ticketDto.getToTown().getName());
                ticket.setFromTown(townFrom);
                ticket.setToTown(townTo);
                Passenger passenger = passengerRepository.findByEmail(ticketDto.getPassenger().getEmail());
                Plane plane = planeRepository.findByRegisterNumber(ticketDto.getPlane().getRegisterNumber());
                ticket.setPassenger(passenger);
                ticket.setPlane(plane);
                stringBuilder.append(String.format("Successfully imported Ticket %s - %s", ticket.getFromTown().getName(), ticket.getToTown().getName()));
                stringBuilder.append(System.lineSeparator());
                ticketRepository.save(ticket);


            }
        }
        return stringBuilder.toString().trim();
    }
}
