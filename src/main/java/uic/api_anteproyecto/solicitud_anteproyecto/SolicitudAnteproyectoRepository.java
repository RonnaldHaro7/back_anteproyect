package uic.api_anteproyecto.solicitud_anteproyecto;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SolicitudAnteproyectoRepository extends JpaRepository<SolicitudAnteproyecto, Long> {
 public List<SolicitudAnteproyecto> findAll();

}