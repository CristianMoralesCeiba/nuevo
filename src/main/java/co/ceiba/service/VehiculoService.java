package co.ceiba.service;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.ceiba.domain.Vehiculo;
import co.ceiba.domain.enumeration.TipoVehiculo;
import co.ceiba.repository.VehiculoRepository;
import co.ceiba.service.dto.VehiculoDTO;
import co.ceiba.service.mapper.VehiculoMapper;
import co.ceiba.web.rest.errors.BadRequestAlertException;
import co.ceiba.web.rest.util.ErrorMessages;

@Service
public class VehiculoService {

	private static final String ENTITY_NAME = "vehiculo";
	private final int MAX_COBRO_HORA = 9;
	private final BigDecimal EXCEDENTE_CILINDRAJE = new BigDecimal("2000");
	private final int ALTO_CILINDRAJE = 500;

    private final Logger log = LoggerFactory.getLogger(VehiculoService.class);
    private final VehiculoMapper vehiculoMapper;
	
	private final VehiculoRepository vehiculoRepository;
    
	public VehiculoService(VehiculoRepository vehiculoRepository, VehiculoMapper vehiculoMapper) {
		super();
		this.vehiculoRepository = vehiculoRepository;
		this.vehiculoMapper = vehiculoMapper;
	}
	
	public Vehiculo crearVehiculo(VehiculoDTO vehiculoDTO) throws URISyntaxException {
		if (vehiculoDTO.getId() != null) {
            throw new BadRequestAlertException(ErrorMessages.VEHICULO_NO_ID_NEW, ENTITY_NAME, "idexists");
        } else {
	        Vehiculo vehiculo = vehiculoMapper.toEntity(vehiculoDTO);
	        
		        if (!vehiculoRepository.findByPlacaAndTipo(vehiculoDTO.getPlaca(), vehiculoDTO.getTipo()).isEmpty()) {
		        	throw new BadRequestAlertException(ErrorMessages.VEHICULO_YA_INGRESADO, ENTITY_NAME, "placaexist");
		        } else if (!esDiaHabil(vehiculoDTO.getPlaca(), vehiculoDTO.getFechaIngreso())){
		        	throw new BadRequestAlertException(ErrorMessages.VEHICULO_NO_DIA_HABIL, ENTITY_NAME, "dianohabil");
		        } else if (vehiculoDTO.getTipo().equals(TipoVehiculo.MOTO)){
		        	if (!hayCupo(TipoVehiculo.MOTO)) {
		            	throw new BadRequestAlertException(ErrorMessages.VEHICULO_TOPE_MOTOS, ENTITY_NAME, "motomax");
		            }
		        } else if (vehiculoDTO.getTipo().equals(TipoVehiculo.CARRO)){
		        	if (!hayCupo(TipoVehiculo.CARRO)) {
		            	throw new BadRequestAlertException(ErrorMessages.VEHICULO_TOPE_CARRROS, ENTITY_NAME, "carromax");
		            }
		        } 	        
	        return vehiculoRepository.save(vehiculo);
        }
	}
	
	public boolean hayCupo (TipoVehiculo tipo){
		
		if (!vehiculoRepository.findByTipo(tipo).isEmpty() && vehiculoRepository.findByTipo(tipo).size() <= tipo.getCupo()) {
        	return false;
        }
		return true;
	}
	
	public boolean esDiaHabil(String placa, Instant fecha){

		int dia = LocalDateTime.ofInstant(fecha, ZoneId.systemDefault()).get(ChronoField.DAY_OF_WEEK);
		
		switch (placa.substring(0, 1)){
			case "A":
				if (dia == 1 || dia == 7){  
					return true;
				} else {
					return false;
				}
		}
			
		return true;
	}
	
	public BigDecimal sacarVehiculo (Long id) throws URISyntaxException {
		
		Vehiculo vehiculo = vehiculoRepository.findOne(id);
		
		if (vehiculo == null){
			throw new BadRequestAlertException(ErrorMessages.VEHICULO_NO_EXISTE, ENTITY_NAME, "vehiculonoexiste");
		} else {
			VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);
			
			Duration tiempoEnParqueadero = Duration.between(Instant.now(), vehiculoDTO.getFechaIngreso());
			
			if (!tiempoEnParqueadero.isZero() && 
				!tiempoEnParqueadero.isNegative()){
				
				BigDecimal valor = BigDecimal.ZERO;
	
				long dias = tiempoEnParqueadero.toDays();
				long horas = (dias * 24) - tiempoEnParqueadero.toHours();
				
				if (horas > MAX_COBRO_HORA){
					dias += 1;
					horas = 0;
				}
				
				log.debug("Sacar vehiculo : dias " + dias + " horas " + horas);
				
				valor = (new BigDecimal(dias).multiply(vehiculoDTO.getTipo().getValorDia())).
						add(new BigDecimal(horas).multiply(vehiculoDTO.getTipo().getValorHora()));
				
				if (dias == 0  && horas == 0){
					valor = BigDecimal.ONE.multiply(vehiculoDTO.getTipo().getValorHora());	
				}
				
				if (vehiculoDTO.getCilindraje() >= ALTO_CILINDRAJE){
					valor = valor.add(EXCEDENTE_CILINDRAJE);
				}
				
				log.debug("Sacar vehiculo : El valor es: " + valor + " dias5 " + dias + " horas " + horas);
				
				vehiculoRepository.delete(vehiculoDTO.getId());
				
				return valor;
				
			} 
			
			return new BigDecimal(tiempoEnParqueadero.toString());
		}
	}
	
}
