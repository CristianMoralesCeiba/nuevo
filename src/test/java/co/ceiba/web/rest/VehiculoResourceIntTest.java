package co.ceiba.web.rest;

import co.ceiba.NuevoApp;

import co.ceiba.domain.Vehiculo;
import co.ceiba.repository.VehiculoRepository;
import co.ceiba.service.VehiculoService;
import co.ceiba.service.dto.VehiculoDTO;
import co.ceiba.service.mapper.VehiculoMapper;
import co.ceiba.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static co.ceiba.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.ceiba.domain.enumeration.TipoVehiculo;
/**
 * Test class for the VehiculoResource REST controller.
 *
 * @see VehiculoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NuevoApp.class)
public class VehiculoResourceIntTest {

    private static final String DEFAULT_PLACA = "AAAAAAAAAA";
    private static final String UPDATED_PLACA = "BBBBBBBBBB";

    private static final TipoVehiculo DEFAULT_TIPO = TipoVehiculo.CARRO;
    private static final TipoVehiculo UPDATED_TIPO = TipoVehiculo.MOTO;

    private static final Integer DEFAULT_CILINDRAJE = 1;
    private static final Integer UPDATED_CILINDRAJE = 2;

    private static final Instant DEFAULT_FECHA_INGRESO = Instant.parse("2017-11-08T10:12:35Z");
    private static final Instant UPDATED_FECHA_INGRESO = Instant.parse("2017-11-08T10:12:35Z");

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private VehiculoMapper vehiculoMapper;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVehiculoMockMvc;

    private Vehiculo vehiculo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VehiculoResource vehiculoResource = new VehiculoResource(vehiculoRepository, vehiculoMapper, vehiculoService);
        this.restVehiculoMockMvc = MockMvcBuilders.standaloneSetup(vehiculoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehiculo createEntity(EntityManager em) {
        Vehiculo vehiculo = new Vehiculo()
            .placa(DEFAULT_PLACA)
            .tipo(DEFAULT_TIPO)
            .cilindraje(DEFAULT_CILINDRAJE)
            .fechaIngreso(DEFAULT_FECHA_INGRESO);
        return vehiculo;
    }

    @Before
    public void initTest() {
        vehiculo = createEntity(em);
    }

    @Test
    @Transactional
    public void createVehiculo() throws Exception {
        int databaseSizeBeforeCreate = vehiculoRepository.findAll().size();

        // Create the Vehiculo
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);
        restVehiculoMockMvc.perform(post("/api/vehiculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isCreated());

        // Validate the Vehiculo in the database
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeCreate + 1);
        Vehiculo testVehiculo = vehiculoList.get(vehiculoList.size() - 1);
        assertThat(testVehiculo.getPlaca()).isEqualTo(DEFAULT_PLACA);
        assertThat(testVehiculo.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testVehiculo.getCilindraje()).isEqualTo(DEFAULT_CILINDRAJE);
        assertThat(testVehiculo.getFechaIngreso()).isEqualTo(DEFAULT_FECHA_INGRESO);
    }

    @Test
    @Transactional
    public void createVehiculoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vehiculoRepository.findAll().size();

        // Create the Vehiculo with an existing ID
        vehiculo.setId(1L);
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehiculoMockMvc.perform(post("/api/vehiculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vehiculo in the database
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPlacaIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculoRepository.findAll().size();
        // set the field null
        vehiculo.setPlaca(null);

        // Create the Vehiculo, which fails.
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);

        restVehiculoMockMvc.perform(post("/api/vehiculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isBadRequest());

        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculoRepository.findAll().size();
        // set the field null
        vehiculo.setTipo(null);

        // Create the Vehiculo, which fails.
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);

        restVehiculoMockMvc.perform(post("/api/vehiculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isBadRequest());

        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaIngresoIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculoRepository.findAll().size();
        // set the field null
        vehiculo.setFechaIngreso(null);

        // Create the Vehiculo, which fails.
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);

        restVehiculoMockMvc.perform(post("/api/vehiculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isBadRequest());

        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVehiculos() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList
        restVehiculoMockMvc.perform(get("/api/vehiculos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehiculo.getId().intValue())))
            .andExpect(jsonPath("$.[*].placa").value(hasItem(DEFAULT_PLACA.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].cilindraje").value(hasItem(DEFAULT_CILINDRAJE)))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())));
    }

    @Test
    @Transactional
    public void getVehiculo() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get the vehiculo
        restVehiculoMockMvc.perform(get("/api/vehiculos/{id}", vehiculo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vehiculo.getId().intValue()))
            .andExpect(jsonPath("$.placa").value(DEFAULT_PLACA.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.cilindraje").value(DEFAULT_CILINDRAJE))
            .andExpect(jsonPath("$.fechaIngreso").value(DEFAULT_FECHA_INGRESO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVehiculo() throws Exception {
        // Get the vehiculo
        restVehiculoMockMvc.perform(get("/api/vehiculos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVehiculo() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);
        int databaseSizeBeforeUpdate = vehiculoRepository.findAll().size();

        // Update the vehiculo
        Vehiculo updatedVehiculo = vehiculoRepository.findOne(vehiculo.getId());
        updatedVehiculo
            .placa(UPDATED_PLACA)
            .tipo(UPDATED_TIPO)
            .cilindraje(UPDATED_CILINDRAJE)
            .fechaIngreso(UPDATED_FECHA_INGRESO);
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(updatedVehiculo);

        restVehiculoMockMvc.perform(put("/api/vehiculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isOk());

        // Validate the Vehiculo in the database
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeUpdate);
        Vehiculo testVehiculo = vehiculoList.get(vehiculoList.size() - 1);
        assertThat(testVehiculo.getPlaca()).isEqualTo(UPDATED_PLACA);
        assertThat(testVehiculo.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testVehiculo.getCilindraje()).isEqualTo(UPDATED_CILINDRAJE);
        assertThat(testVehiculo.getFechaIngreso()).isEqualTo(UPDATED_FECHA_INGRESO);
    }

    @Test
    @Transactional
    public void updateNonExistingVehiculo() throws Exception {
        int databaseSizeBeforeUpdate = vehiculoRepository.findAll().size();

        // Create the Vehiculo
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVehiculoMockMvc.perform(put("/api/vehiculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isCreated());

        // Validate the Vehiculo in the database
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVehiculo() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);
        int databaseSizeBeforeDelete = vehiculoRepository.findAll().size();

        // Get the vehiculo
        restVehiculoMockMvc.perform(delete("/api/vehiculos/{id}", vehiculo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vehiculo.class);
        Vehiculo vehiculo1 = new Vehiculo();
        vehiculo1.setId(1L);
        Vehiculo vehiculo2 = new Vehiculo();
        vehiculo2.setId(vehiculo1.getId());
        assertThat(vehiculo1).isEqualTo(vehiculo2);
        vehiculo2.setId(2L);
        assertThat(vehiculo1).isNotEqualTo(vehiculo2);
        vehiculo1.setId(null);
        assertThat(vehiculo1).isNotEqualTo(vehiculo2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehiculoDTO.class);
        VehiculoDTO vehiculoDTO1 = new VehiculoDTO();
        vehiculoDTO1.setId(1L);
        VehiculoDTO vehiculoDTO2 = new VehiculoDTO();
        assertThat(vehiculoDTO1).isNotEqualTo(vehiculoDTO2);
        vehiculoDTO2.setId(vehiculoDTO1.getId());
        assertThat(vehiculoDTO1).isEqualTo(vehiculoDTO2);
        vehiculoDTO2.setId(2L);
        assertThat(vehiculoDTO1).isNotEqualTo(vehiculoDTO2);
        vehiculoDTO1.setId(null);
        assertThat(vehiculoDTO1).isNotEqualTo(vehiculoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(vehiculoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(vehiculoMapper.fromId(null)).isNull();
    }
}
