package co.ceiba.web.rest;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import co.ceiba.domain.Vehiculo;
import co.ceiba.repository.VehiculoRepository;
import co.ceiba.service.VehiculoService;
import co.ceiba.service.dto.VehiculoDTO;
import co.ceiba.service.mapper.VehiculoMapper;
import co.ceiba.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Vehiculo.
 */
@RestController
@RequestMapping("/api")
public class VehiculoResource {

    private final Logger log = LoggerFactory.getLogger(VehiculoResource.class);

    private static final String ENTITY_NAME = "vehiculo";

    private final VehiculoRepository vehiculoRepository;

    private final VehiculoMapper vehiculoMapper;
    
    private final VehiculoService vehiculoService;

    public VehiculoResource(VehiculoRepository vehiculoRepository, VehiculoMapper vehiculoMapper, VehiculoService vehiculoService) {
        this.vehiculoRepository = vehiculoRepository;
        this.vehiculoMapper = vehiculoMapper;
        this.vehiculoService = vehiculoService;
    }
    /**
     * POST  /vehiculos : Create a new vehiculo.
     *
     * @param vehiculoDTO the vehiculoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vehiculoDTO, or with status 400 (Bad Request) if the vehiculo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vehiculos")
    @Timed
    public ResponseEntity<VehiculoDTO> createVehiculo(@Valid @RequestBody VehiculoDTO vehiculoDTO) throws URISyntaxException {
        log.debug("REST request to save Vehiculo : {}", vehiculoDTO);

        VehiculoDTO result = vehiculoMapper.toDto(vehiculoService.crearVehiculo(vehiculoDTO));
        
        return ResponseEntity.created(new URI("/api/vehiculos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result); 
    }

    /**
     * GET  /vehiculos : get all the vehiculos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of vehiculos in body
     */
    @GetMapping("/vehiculos")
    @Timed
    public List<VehiculoDTO> getAllVehiculos() {
        log.debug("REST request to get all Vehiculos");
        List<Vehiculo> vehiculos = vehiculoRepository.findAll();
        return vehiculoMapper.toDto(vehiculos);
        }

    /**
     * GET  /vehiculos/:id : get the "id" vehiculo.
     *
     * @param id the id of the vehiculoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vehiculoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/vehiculos/{id}")
    @Timed
    public ResponseEntity<VehiculoDTO> getVehiculo(@PathVariable Long id) {
        log.debug("REST request to get Vehiculo : {}", id);
        Vehiculo vehiculo = vehiculoRepository.findOne(id);
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(vehiculoDTO));
    }

    /**
     * DELETE  /vehiculos/:id : delete the "id" vehiculo.
     *
     * @param id the id of the vehiculoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vehiculos/{id}")
    @Timed
    public ResponseEntity<Void> deleteVehiculo(@PathVariable Long id) throws URISyntaxException{
        log.debug("REST request to delete Vehiculo : {}", id);
        
        BigDecimal valor = vehiculoService.sacarVehiculo(id);
        
        return ResponseEntity.ok().
        		headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).
        		//headers(HeaderUtil.createEntityValueDeletionAlert(valor)).
        		build();
    }
}
