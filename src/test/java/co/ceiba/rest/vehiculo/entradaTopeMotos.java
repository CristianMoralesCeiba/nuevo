package co.ceiba.rest.vehiculo;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import co.ceiba.NuevoApp;
import co.ceiba.repository.VehiculoRepository;
import co.ceiba.rest.util.TestUtil;
import co.ceiba.service.VehiculoService;
import co.ceiba.service.dto.VehiculoDTO;
import co.ceiba.service.mapper.VehiculoMapper;
import co.ceiba.testdatabuilder.vehiculo.VehiculoTestDataBuilder;
import co.ceiba.web.rest.UserResource;
import co.ceiba.web.rest.VehiculoResource;
import co.ceiba.web.rest.errors.ExceptionTranslator;
import co.ceiba.web.rest.util.ErrorMessages;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NuevoApp.class)	
public class entradaTopeMotos {

	@Autowired
	VehiculoRepository vehiculoRepository;
	
	@Autowired
	VehiculoMapper vehiculoMapper;

	@Autowired
	VehiculoService vehiculoService;
	
	@Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
    
    @Autowired
    private ExceptionTranslator exceptionTranslator;
    	
	private MockMvc restUserMockMvc;
	
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VehiculoResource vehiculoResource = new VehiculoResource(vehiculoRepository, vehiculoMapper, vehiculoService);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(vehiculoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter)
            .build();
    }


    /*@Before
    public void initTest() {
        user = createEntity(em);
        user.setLogin(DEFAULT_LOGIN);
        user.setEmail(DEFAULT_EMAIL);
    }*/

    @Test
    public void createUser() throws Exception {
        //int databaseSizeBeforeCreate = vehiculoRepository.findAll().size();
        VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
        VehiculoDTO vehiculo = vehiculoTestDataBuilder.build();
        
        MvcResult result = restUserMockMvc.perform(post("/api/vehiculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculo)))
            .andExpect(status().isBadRequest()).andReturn();

        /* Validate the User in the database
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeCreate + 1);
        Vehiculo testVehiculo = vehiculoList.get(vehiculoList.size() - 1);
        assertThat(testVehiculo.getPlaca()).isEqualTo(vehiculo.getPlaca());*/
        assertEquals(result.getResolvedException().getMessage(), ErrorMessages.VEHICULOS_TOPE_MOTOS);
    }


}
