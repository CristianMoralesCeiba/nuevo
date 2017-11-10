package co.ceiba.service;

import org.springframework.stereotype.Service;

import co.ceiba.domain.enumeration.TipoVehiculo;
import co.ceiba.repository.VehiculoRepository;
import co.ceiba.service.mapper.VehiculoMapper;

@Service
public class VehiculoService {

	//private final Logger log = LoggerFactory.getLogger(UserService.class);
	
	private final VehiculoRepository vehiculoRepository;
	
	private static final String ENTITY_NAME = "vehiculo";
	
    private final VehiculoMapper vehiculoMapper;
    
	public VehiculoService(VehiculoRepository vehiculoRepository, VehiculoMapper vehiculoMapper) {
		super();
		this.vehiculoRepository = vehiculoRepository;
		this.vehiculoMapper = vehiculoMapper;
	}
	
	public boolean hayCupo (TipoVehiculo tipo){
		
		if (vehiculoRepository.findByTipo(tipo).isEmpty() && vehiculoRepository.findByTipo(tipo).size() > tipo.getCupo()) {
        	return false;
        }
		return true;
	}
	
}
