package co.ceiba.service.mapper;

import co.ceiba.domain.*;
import co.ceiba.service.dto.VehiculoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Vehiculo and its DTO VehiculoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VehiculoMapper extends EntityMapper<VehiculoDTO, Vehiculo> {

    

    

    default Vehiculo fromId(Long id) {
        if (id == null) {
            return null;
        }
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(id);
        return vehiculo;
    }
}
