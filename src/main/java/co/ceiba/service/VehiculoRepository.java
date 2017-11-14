package co.ceiba.service;

import co.ceiba.domain.Vehiculo;
import co.ceiba.domain.enumeration.TipoVehiculo;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Vehiculo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

	List<Vehiculo> findByPlacaAndTipo(String placa, TipoVehiculo tipo);
	
	List<Vehiculo> findByTipo(TipoVehiculo tipo);

}