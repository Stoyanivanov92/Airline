package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import softuni.exam.models.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
