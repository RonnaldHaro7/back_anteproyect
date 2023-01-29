package uic.api_anteproyecto.generarsolicitudpdf;

import java.io.FileNotFoundException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import uic.api_anteproyecto.estudiante.CustomerEstudient;
import uic.api_anteproyecto.estudiante.CustomerDTO;
import jakarta.transaction.Transactional;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Transactional
@Service
public class SolicitudPDFService {
    @Autowired SolicitudPDFRepository solicitudPDFRepository;
    @Autowired CustomerEstudient customerEstudient;

    @Transactional
    public SolicitudPDF save(SolicitudPDF entity){
        return solicitudPDFRepository.save(entity);
    }

    public SolicitudPDF findById( Long id){
        return solicitudPDFRepository.findById(id).orElse(new SolicitudPDF());
    }

    public void deleteById(Long id){
        solicitudPDFRepository.deleteById(id);
    }

    public List<SolicitudPDF> findAll(){
        return solicitudPDFRepository.findAll();
    }

    public JasperPrint getSolicitudPDFReporte(Long id) {

        Map<String, Object> reportParameters = new HashMap<String, Object>();
        SolicitudPDF solicitudPDF = findById(id);
        if (solicitudPDF.getId()==null)
            return null;
        
        reportParameters.put("fecha",Date.valueOf(solicitudPDF.getFecha()));
        reportParameters.put("tema",Date.valueOf(solicitudPDF.getTema()));
        reportParameters.put("profesor",Date.valueOf(solicitudPDF.getProfesor()));
        reportParameters.put("periodo_lectivo",Date.valueOf(solicitudPDF.getPeriodo_lectivo()));

        CustomerDTO estudiante =  customerEstudient.findCustomerById(solicitudPDF.getEstudianteId());
        reportParameters.put("name", estudiante.getName());
        reportParameters.put("nro_identificacion", estudiante.getNro_identificacion());
        reportParameters.put("date_of_bird", estudiante.getDate_of_bird());
        reportParameters.put("email", estudiante.getEmail());
        reportParameters.put("cellphone", estudiante.getCellphone());
        
        List<Map<String, Object>> dataList = new ArrayList<>();
        
        reportParameters.put("solicitudPDFData", new JRBeanCollectionDataSource(dataList));

        JasperPrint reportJasperPrint = null;
        try {
            reportJasperPrint = JasperFillManager.fillReport(
                    JasperCompileManager.compileReport(
                            ResourceUtils.getFile("classpath:jrxml/factura.jrxml")
                                    .getAbsolutePath()) // path of the jasper report
                    , reportParameters // dynamic parameters
                    , new JREmptyDataSource());
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
        }
        return reportJasperPrint;
    }
}