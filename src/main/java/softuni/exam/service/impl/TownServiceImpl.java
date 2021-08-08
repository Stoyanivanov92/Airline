package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TownDto;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TownServiceImpl implements TownService {
    private static final String PATH_FILE_TOWNS = "src/main/resources/files/json/towns.json";

    private final TownRepository townRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public TownServiceImpl(TownRepository townRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        if (townRepository.count() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of(PATH_FILE_TOWNS));
    }

    @Override
    public String importTowns() throws IOException {
        TownDto[] townDtos = gson.fromJson(readTownsFileContent(), TownDto[].class);
        StringBuilder stringBuilder = new StringBuilder();
        for (TownDto townDto : townDtos) {
            if (!validationUtil.isValid(townDto)) {
                stringBuilder.append("Invalid Town");
                stringBuilder.append(System.lineSeparator());
            } else {
                Town town = modelMapper.map(townDto, Town.class);
                stringBuilder.append(String.format("Successfully imported Town %s - %d", town.getName(), town.getPopulation()));
                stringBuilder.append(System.lineSeparator());
                townRepository.save(town);
            }
        }
        return stringBuilder.toString().trim();
    }
}
