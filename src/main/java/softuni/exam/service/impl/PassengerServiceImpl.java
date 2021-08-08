package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PassengerDto;
import softuni.exam.models.entity.Passenger;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class PassengerServiceImpl implements PassengerService {
    private static final String PATH_FILE_PASSENGERS = "src/main/resources/files/json/passengers.json";

    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final TownRepository townRepository;

    public PassengerServiceImpl(PassengerRepository passengerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, TownRepository townRepository) {
        this.passengerRepository = passengerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        if (passengerRepository.count() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String readPassengersFileContent() throws IOException {
      return Files.readString(Path.of(PATH_FILE_PASSENGERS));
    }

    @Override
    public String importPassengers() throws IOException {
        PassengerDto[] passengerDtos = gson.fromJson(readPassengersFileContent(), PassengerDto[].class);
        StringBuilder stringBuilder = new StringBuilder();
        for (PassengerDto passengerDto : passengerDtos) {
            if (!validationUtil.isValid(passengerDto)) {
                stringBuilder.append("Invalid Passenger");
                stringBuilder.append(System.lineSeparator());
            } else {
                Passenger passenger = modelMapper.map(passengerDto, Passenger.class);
                Town town = townRepository.findByName(passengerDto.getTown());
                passenger.setTown(town);
                stringBuilder.append(String.format("Successfully imported Passenger %s - %s", passenger.getLastName(), passenger.getEmail()));
                stringBuilder.append(System.lineSeparator());
                passengerRepository.save(passenger);
            }
        }
        return stringBuilder.toString().trim();
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        List<Passenger> passengers = passengerRepository.findByTicketsCountDescThenEmail();
        StringBuilder stringBuilder = new StringBuilder();
        for (Passenger passenger : passengers) {
            stringBuilder.append(String.format("Passenger %s %s%n" +
                    "\tEmail - %s%n" +
                    "\tPhone - %s%n" +
                    "\tNumber of tickets - %d%n", passenger.getFirstName(), passenger.getLastName(), passenger.getEmail(), passenger.getPhoneNumber(), passenger.getTickets().size()));
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString().trim();
    }
}
