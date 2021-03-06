package co.ceiba.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;

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
	public static final int MAX_COBRO_HORA = 9;
	public static final BigDecimal EXCEDENTE_CILINDRAJE = new BigDecimal("2000");
	public static final int ALTO_CILINDRAJE = 500;

    private final VehiculoMapper vehiculoMapper;
	
	private final VehiculoRepository vehiculoRepository;
    
	public VehiculoService(VehiculoRepository vehiculoRepository, VehiculoMapper vehiculoMapper) {
		super();
		this.vehiculoRepository = vehiculoRepository;
		this.vehiculoMapper = vehiculoMapper;
	}
	
	public Vehiculo crearVehiculo(VehiculoDTO vehiculoDTO) {
		if (vehiculoDTO.getId() != null) {
            throw new BadRequestAlertException(ErrorMessages.VEHICULO_NO_ID_NEW, ENTITY_NAME, "idexists");
        } else {
	        Vehiculo vehiculo = vehiculoMapper.toEntity(vehiculoDTO);
	        
		        if (!vehiculoRepository.findByPlacaAndTipo(vehiculoDTO.getPlaca(), vehiculoDTO.getTipo()).isEmpty()) {
		        	throw new BadRequestAlertException(ErrorMessages.VEHICULO_YA_INGRESADO, ENTITY_NAME, "placaexist");
		        } else if (!esDiaHabil(vehiculoDTO.getPlaca(), vehiculoDTO.getFechaIngreso())){
		        	throw new BadRequestAlertException(ErrorMessages.VEHICULO_NO_DIA_HABIL, ENTITY_NAME, "dianohabil");
		        } else if (vehiculoDTO.getTipo().equals(TipoVehiculo.MOTO) && !hayCupo(TipoVehiculo.MOTO)){
		            throw new BadRequestAlertException(ErrorMessages.VEHICULO_TOPE_MOTOS, ENTITY_NAME, "motomax");
		        } else if (vehiculoDTO.getTipo().equals(TipoVehiculo.CARRO) && !hayCupo(TipoVehiculo.CARRO)){
		            	throw new BadRequestAlertException(ErrorMessages.VEHICULO_TOPE_CARRROS, ENTITY_NAME, "carromax");
		        } 	        
	        return vehiculoRepository.save(vehiculo);
        }
	}
	
	public boolean hayCupo (TipoVehiculo tipo){
		
		return !(!vehiculoRepository.findByTipo(tipo).isEmpty() && vehiculoRepository.findByTipo(tipo).size() >= tipo.getCupo());
	}
	
	public boolean esDiaHabil(String placa, Instant fecha){

		int dia = LocalDateTime.ofInstant(fecha, ZoneId.systemDefault()).get(ChronoField.DAY_OF_WEEK);
		
		if (placa.substring(0, 1).equals("A")){
			return dia == 1 || dia == 7;
		} else {
			return true;
		}
	}
	
	public BigDecimal sacarVehiculo (Long id) {
		
		Vehiculo vehiculo = vehiculoRepository.findOne(id);
		VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);
		
		long[] tiempo = calcularTiempoEnParqueadero (vehiculoDTO, Instant.now());
		
		if (tiempo.length > 1){
			
			vehiculoRepository.delete(vehiculoDTO.getId());
			
			return calcularValorParqueadero(vehiculoDTO, tiempo[0], tiempo[1]);
		} 
		
		throw new BadRequestAlertException(ErrorMessages.VEHICULO_INCALCULABLE, ENTITY_NAME, "nocalculable");
		
	}
	
	public long[] calcularTiempoEnParqueadero (VehiculoDTO vehiculoDTO, Instant fechaSalida) {
		
		Duration tiempoEnParqueadero = Duration.between(vehiculoDTO.getFechaIngreso(), fechaSalida);
		
		if (!tiempoEnParqueadero.isZero() && !tiempoEnParqueadero.isNegative()){

			long dias = tiempoEnParqueadero.toDays();
			long horas = tiempoEnParqueadero.toHours() - (dias * 24);
			
			if (horas > MAX_COBRO_HORA){
				dias += 1;
				horas = 0;
			}
			
			return new long[]{dias, horas};
		}
		
		return new long[0];
	}
	
	public BigDecimal calcularValorParqueadero (VehiculoDTO vehiculoDTO, long dias, long horas){
		
		BigDecimal valor;
		
		valor = (new BigDecimal(dias).multiply(vehiculoDTO.getTipo().getValorDia())).
				add(new BigDecimal(horas).multiply(vehiculoDTO.getTipo().getValorHora()));
		
		if (dias == 0 && horas == 0){
			valor = BigDecimal.ONE.multiply(vehiculoDTO.getTipo().getValorHora());	
		}
		
		if (vehiculoDTO.getCilindraje() != null && vehiculoDTO.getCilindraje() >= ALTO_CILINDRAJE){
			valor = valor.add(EXCEDENTE_CILINDRAJE);
		}
		
		return valor;
	}
	
}
