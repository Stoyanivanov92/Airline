package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import softuni.exam.models.entity.Town;

public interface TownRepository extends JpaRepository<Town, Long> {

    Town findByName(String name);

}
