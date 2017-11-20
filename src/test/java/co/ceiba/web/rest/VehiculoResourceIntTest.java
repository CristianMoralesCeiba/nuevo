package co.ceiba.web.rest;

import static co.ceiba.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasItem;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import co.ceiba.NuevoApp;
import co.ceiba.domain.Vehiculo;
import co.ceiba.domain.enumeration.TipoVehiculo;
import co.ceiba.repository.VehiculoRepository;
import co.ceiba.service.VehiculoService;
import co.ceiba.service.dto.VehiculoDTO;
import co.ceiba.service.mapper.VehiculoMapper;
import co.ceiba.web.rest.errors.ExceptionTranslator;
import co.ceiba.web.rest.testDataBuilder.VehiculoTestDataBuilder;
import co.ceiba.web.rest.util.ErrorMessages;
/**
 * Test class for the VehiculoResource REST controller.
 *
 * @see VehiculoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NuevoApp.class)
public class VehiculoResourceIntTest {

	
    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private VehiculoMapper vehiculoMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private VehiculoService vehiculoService;

    private MockMvc restVehiculoMockMvc;
    
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
 
    @Test
    @Transactional
    public void createVehiculoWithExistingId() throws Exception {
    	//arrage
        VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
                
        VehiculoDTO vehiculoDTO = vehiculoTestDataBuilder.conID(1L).build();

        MvcResult result = restVehiculoMockMvc.perform(post("/api/vehiculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isBadRequest()).andReturn();

        assertEquals(ErrorMessages.VEHICULO_NO_ID_NEW, result.getResolvedException().getMessage());	
    }
    
    @Test
    @Transactional
    public void crearVehiculoSinCupoMoto() throws Exception {
    	//arrage
        VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
        VehiculoDTO vehiculoDTO = vehiculoTestDataBuilder.build();
        
        //act
        for (int i = 0; i < vehiculoDTO.getTipo().getCupo(); i++){
            vehiculoDTO = vehiculoTestDataBuilder.conPlaca(i + vehiculoTestDataBuilder.PLACA).build();
            
        	vehiculoRepository.saveAndFlush(vehiculoMapper.toEntity(vehiculoDTO));
        }
        
        //equals
        vehiculoDTO = vehiculoTestDataBuilder.conPlaca(TipoVehiculo.CARRO.getCupo() + vehiculoTestDataBuilder.PLACA).build();
        
        MvcResult result = restVehiculoMockMvc.perform(post("/api/vehiculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isBadRequest()).andReturn();

        assertEquals(ErrorMessages.VEHICULO_TOPE_MOTOS, result.getResolvedException().getMessage());	
    }
    
    @Test
    @Transactional
    public void crearVehiculoSinCupoCarro() throws Exception {
    	//arrage
        VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
        VehiculoDTO vehiculoDTO = vehiculoTestDataBuilder.conTipo(TipoVehiculo.CARRO).build();

        //act
        for (int i = 0; i < vehiculoDTO.getTipo().getCupo(); i++){
            vehiculoDTO = vehiculoTestDataBuilder.conTipo(TipoVehiculo.CARRO)
            			.conPlaca(i + vehiculoTestDataBuilder.PLACA).build();
            
        	vehiculoRepository.saveAndFlush(vehiculoMapper.toEntity(vehiculoDTO));
        }
        
      	//equals
        vehiculoDTO = vehiculoTestDataBuilder.conPlaca(TipoVehiculo.CARRO.getCupo() + vehiculoTestDataBuilder.PLACA).build();
        
        MvcResult result = restVehiculoMockMvc.perform(post("/api/vehiculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isBadRequest()).andReturn();

        assertEquals(ErrorMessages.VEHICULO_TOPE_CARRROS, result.getResolvedException().getMessage());	
    }
    
    @Test
    @Transactional
    public void crearVehiculoDiaHabil() throws Exception {
    	//arrage
        VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
        VehiculoDTO vehiculoDTO = vehiculoTestDataBuilder.
        						  conPlaca("A123").
        						  conFechaIngreso(Instant.parse("2017-11-13T10:12:35Z")).
        						  build();
        
      	//equals
        restVehiculoMockMvc.perform(post("/api/vehiculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isCreated());
    }
    
    @Test
    @Transactional
    public void crearVehiculoNoDiaHabil() throws Exception {
    	//arrage
        VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
        VehiculoDTO vehiculoDTO = vehiculoTestDataBuilder.
        						  conPlaca("A123").
        						  conFechaIngreso(Instant.parse("2017-11-15T10:12:35Z")).
        						  build();
        
      	//equals
        MvcResult result = restVehiculoMockMvc.perform(post("/api/vehiculos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
                .andExpect(status().isBadRequest()).andReturn();

       assertEquals(ErrorMessages.VEHICULO_NO_DIA_HABIL, result.getResolvedException().getMessage());	
    }
    
    @Test
    @Transactional
    public void crearVehiculoYaIngresado() throws Exception {
    	//arrage
        VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
        VehiculoDTO vehiculoDTO = vehiculoTestDataBuilder.build();
        
        //act
        vehiculoRepository.saveAndFlush(vehiculoMapper.toEntity(vehiculoDTO));
        
      	//equals
        MvcResult result = restVehiculoMockMvc.perform(post("/api/vehiculos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
                .andExpect(status().isBadRequest()).andReturn();

       assertEquals(ErrorMessages.VEHICULO_YA_INGRESADO, result.getResolvedException().getMessage());	
    }
    
    @Test
    @Transactional
    public void sacarVehiculoSoloHoras() throws Exception {
    	//arrage
        VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
        BigDecimal valorEsperado = BigDecimal.ZERO, horasPrueba = new BigDecimal("3");
        Duration horas = Duration.ofHours(horasPrueba.longValue());
        VehiculoDTO vehiculoDTO = vehiculoTestDataBuilder.conFechaIngreso(Instant.now().minus(horas)).build();
        Vehiculo vehiculo;
        
    	// Initialize the database
        vehiculo = vehiculoRepository.saveAndFlush(vehiculoMapper.toEntity(vehiculoDTO));;
        valorEsperado = vehiculoDTO.getTipo().getValorHora().multiply(horasPrueba);
        
      	//equals
        MvcResult result = restVehiculoMockMvc.perform(delete("/api/vehiculos/{id}", vehiculo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andReturn();

        boolean valor = result.getResponse().getHeaders("X-nuevoApp-alert").contains(VehiculoResource.MENSAJE_VALOR + NumberFormat.getCurrencyInstance().format(valorEsperado) + " COP");
        boolean params = result.getResponse().getHeaders("X-nuevoApp-params").contains("valor");
        
        assertTrue(valor && params);
    }
    
    @Test
    @Transactional
    public void sacarVehiculoSoloDias() throws Exception {
    	//arrage
        VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
        BigDecimal valorEsperado = BigDecimal.ZERO, diasPrueba = new BigDecimal("3");
        Duration dias = Duration.ofDays(diasPrueba.longValue());
        VehiculoDTO vehiculoDTO = vehiculoTestDataBuilder.conFechaIngreso(Instant.now().minus(dias)).build();
        Vehiculo vehiculo;
        
    	// Initialize the database
        vehiculo = vehiculoRepository.saveAndFlush(vehiculoMapper.toEntity(vehiculoDTO));;
        valorEsperado = vehiculo.getTipo().getValorDia().multiply(diasPrueba);
        
      	//equals
        MvcResult result = restVehiculoMockMvc.perform(delete("/api/vehiculos/{id}", vehiculo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andReturn();

        boolean valor = result.getResponse().getHeaders("X-nuevoApp-alert").contains(VehiculoResource.MENSAJE_VALOR + NumberFormat.getCurrencyInstance().format(valorEsperado) + " COP");
        boolean params = result.getResponse().getHeaders("X-nuevoApp-params").contains("valor");
        
        assertTrue(valor && params);
    }
    
    @Test
    @Transactional
    public void sacarVehiculoDiasHoras() throws Exception {
    	//arrage
        VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
        BigDecimal valorEsperado = BigDecimal.ZERO, diasPrueba = new BigDecimal("1"), horasPrueba = new BigDecimal("3");
        Duration dias = Duration.ofDays(diasPrueba.longValue());
        Duration horas = Duration.ofHours(horasPrueba.longValue());
        VehiculoDTO vehiculoDTO = vehiculoTestDataBuilder.conFechaIngreso(Instant.now().minus(dias).minus(horas)).build();
        Vehiculo vehiculo;
        
    	// Initialize the database
        vehiculo = vehiculoRepository.saveAndFlush(vehiculoMapper.toEntity(vehiculoDTO));
        valorEsperado = (vehiculo.getTipo().getValorDia().multiply(diasPrueba)).
        				 add(vehiculo.getTipo().getValorHora().multiply(horasPrueba));
        
      	//equals
        MvcResult result = restVehiculoMockMvc.perform(delete("/api/vehiculos/{id}", vehiculo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andReturn();

        boolean valor = result.getResponse().getHeaders("X-nuevoApp-alert").contains(VehiculoResource.MENSAJE_VALOR + NumberFormat.getCurrencyInstance().format(valorEsperado) + " COP");
        boolean params = result.getResponse().getHeaders("X-nuevoApp-params").contains("valor");
        
        assertTrue(valor && params);
    }
       

    @Test
    @Transactional
    public void deleteVehiculo() throws Exception {
    	 VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
         VehiculoDTO vehiculoDTO = vehiculoTestDataBuilder.build();
         Vehiculo vehiculo;
         
    	// Initialize the database
        vehiculo = vehiculoRepository.saveAndFlush(vehiculoMapper.toEntity(vehiculoDTO));
        int databaseSizeBeforeDelete = vehiculoRepository.findAll().size();

        // Get the vehiculo
        restVehiculoMockMvc.perform(delete("/api/vehiculos/{id}", vehiculo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertEquals(vehiculoList.size(), databaseSizeBeforeDelete - 1);
    }
    
    @Test
    @Transactional
    public void getAllVehiculos() throws Exception {
    	//arrage
        VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
        VehiculoDTO vehiculoDTO = vehiculoTestDataBuilder.build();
    	Vehiculo vehiculo;
        
        // Initialize the database
    	vehiculo = vehiculoRepository.saveAndFlush(vehiculoMapper.toEntity(vehiculoDTO));

        // Get all the vehiculoList
        restVehiculoMockMvc.perform(get("/api/vehiculos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehiculo.getId().intValue())))
            .andExpect(jsonPath("$.[*].placa").value(hasItem(vehiculo.getPlaca())));
    }

    @Test
    @Transactional
    public void getVehiculo() throws Exception {
    	//arrage
        VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
        VehiculoDTO vehiculoDTO = vehiculoTestDataBuilder.build();
    	Vehiculo vehiculo;
        
        // Initialize the database
    	vehiculo = vehiculoRepository.saveAndFlush(vehiculoMapper.toEntity(vehiculoDTO));

        // Get all the vehiculoList
    	restVehiculoMockMvc.perform(get("/api/vehiculos/{id}", vehiculo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vehiculo.getId().intValue()))
            .andExpect(jsonPath("$.placa").value(vehiculo.getPlaca()));
    }

    @Test
    @Transactional
    public void createVehiculo() throws Exception {
    	
    	//arrage

        VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
    	
        int databaseSizeBeforeCreate = vehiculoRepository.findAll().size();
        // Create the Vehiculo
        VehiculoDTO vehiculoDTO = vehiculoTestDataBuilder.build();
        restVehiculoMockMvc.perform(post("/api/vehiculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isCreated());

        // Validate the Vehiculo in the database
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeCreate + 1);
        Vehiculo testVehiculo = vehiculoList.get(vehiculoList.size() - 1);
        assertThat(testVehiculo.getPlaca()).isEqualTo(vehiculoTestDataBuilder.PLACA);
        assertThat(testVehiculo.getTipo()).isEqualTo(vehiculoTestDataBuilder.TIPO);
        assertThat(testVehiculo.getCilindraje()).isEqualTo(vehiculoTestDataBuilder.CILINDRAJE);
        assertThat(testVehiculo.getFechaIngreso()).isEqualTo(vehiculoTestDataBuilder.FECHAINGRESO);
    }

}
