package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PlaneDto;
import softuni.exam.models.dto.PlaneRootDto;
import softuni.exam.models.entity.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class PlaneServiceImpl implements PlaneService {
    private static final String PATH_FILE_PLANES ="src/main/resources/files/xml/planes.xml";

    private final PlaneRepository planeRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public PlaneServiceImpl(PlaneRepository planeRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.planeRepository = planeRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        if (planeRepository.count() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String readPlanesFileContent() throws IOException {
       return Files.readString(Path.of(PATH_FILE_PLANES));
    }

    @Override
    public String importPlanes() throws JAXBException, FileNotFoundException {
        PlaneRootDto planeRootDto = xmlParser.fromFile(PATH_FILE_PLANES, PlaneRootDto.class);
        StringBuilder stringBuilder = new StringBuilder();
        List<PlaneDto> planeDtos = planeRootDto.getPlaneDtos();
        for (PlaneDto planeDto : planeDtos) {
            if (!validationUtil.isValid(planeDto)) {
                stringBuilder.append("Invalid Plane");
                stringBuilder.append(System.lineSeparator());
            } else {
                Plane plane = modelMapper.map(planeDto, Plane.class);
                stringBuilder.append(String.format("Successfully imported Plane %s", plane.getRegisterNumber()));
                stringBuilder.append(System.lineSeparator());
                planeRepository.save(plane);
            }
        }
        return stringBuilder.toString().trim();
    }
}
