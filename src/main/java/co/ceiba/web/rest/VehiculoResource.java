package co.ceiba.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.ceiba.domain.Vehiculo;
import co.ceiba.domain.enumeration.TipoVehiculo;
import co.ceiba.repository.VehiculoRepository;
import co.ceiba.web.rest.errors.BadRequestAlertException;
import co.ceiba.web.rest.util.HeaderUtil;
import co.ceiba.service.VehiculoService;
import co.ceiba.service.dto.VehiculoDTO;
import co.ceiba.service.mapper.VehiculoMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

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

		if (vehiculoDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehiculo cannot already have an ID", ENTITY_NAME, "idexists");
        }
		
        Vehiculo vehiculo = vehiculoMapper.toEntity(vehiculoDTO);
        
        if (!vehiculoRepository.findByPlacaAndTipo(vehiculoDTO.getPlaca(), vehiculoDTO.getTipo()).isEmpty()) {
        	throw new BadRequestAlertException("El vehiculo ya se encuentra en el parqueadero", ENTITY_NAME, "placaexist");
        }
        
        if (vehiculoDTO.getTipo().equals(TipoVehiculo.MOTO)){
        	if (vehiculoService.hayCupo(TipoVehiculo.MOTO)) {
            	throw new BadRequestAlertException("Ya no hay cupo para motos", ENTITY_NAME, "motomax");
            }
        } else if (vehiculoDTO.getTipo().equals(TipoVehiculo.CARRO)){
        	if (vehiculoService.hayCupo(TipoVehiculo.CARRO)) {
            	throw new BadRequestAlertException("Ya no hay cupo para carros", ENTITY_NAME, "carromax");
            }
        }
        
        vehiculo = vehiculoRepository.save(vehiculo);
        
        VehiculoDTO result =vehiculoMapper.toDto(vehiculo);
        
        return ResponseEntity.created(new URI("/api/vehiculos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vehiculos : Updates an existing vehiculo.
     *
     * @param vehiculoDTO the vehiculoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vehiculoDTO,
     * or with status 400 (Bad Request) if the vehiculoDTO is not valid,
     * or with status 500 (Internal Server Error) if the vehiculoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vehiculos")
    @Timed
    public ResponseEntity<VehiculoDTO> updateVehiculo(@Valid @RequestBody VehiculoDTO vehiculoDTO) throws URISyntaxException {
        log.debug("REST request to update Vehiculo : {}", vehiculoDTO);
        if (vehiculoDTO.getId() == null) {
            return createVehiculo(vehiculoDTO);
        }
        Vehiculo vehiculo = vehiculoMapper.toEntity(vehiculoDTO);
        vehiculo = vehiculoRepository.save(vehiculo);
        VehiculoDTO result = vehiculoMapper.toDto(vehiculo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vehiculoDTO.getId().toString()))
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
    public ResponseEntity<Void> deleteVehiculo(@PathVariable Long id) {
        log.debug("REST request to delete Vehiculo : {}", id);
        vehiculoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
