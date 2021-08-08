package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import softuni.exam.models.entity.Passenger;

import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    Passenger findByEmail(String email);

    @Query("Select p From Passenger p order by p.tickets.size desc, p.email")
    List<Passenger> findByTicketsCountDescThenEmail();
    
}
