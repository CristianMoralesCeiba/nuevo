package co.ceiba.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import co.ceiba.NuevoApp;
import co.ceiba.service.dto.VehiculoDTO;
import co.ceiba.web.rest.testDataBuilder.VehiculoTestDataBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NuevoApp.class)
@Transactional
public class VehiculoServiceUnitTest {

	@Autowired
	VehiculoService vehiculoService;
	
	@Test
	public void valorParqueaderoNoCalculable() {

		VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
	    VehiculoDTO vehiculoDTO = vehiculoTestDataBuilder.conFechaIngreso(Instant.parse("2017-11-08T10:12:35Z"))
	    						  .build();
		Instant fechaSalida = Instant.parse("2017-08-08T10:12:35Z");
		
		assertEquals(0, vehiculoService.calcularTiempoEnParqueadero(vehiculoDTO, fechaSalida).length);
	}
	
	@Test
	public void calcularValorMinimo(){
		VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
		VehiculoDTO vehiculoDTO = vehiculoTestDataBuilder.build();
		BigDecimal valorMinimo = BigDecimal.ONE.multiply(vehiculoDTO.getTipo().getValorHora());		
		
		assertEquals(valorMinimo, vehiculoService.calcularValorParqueadero(vehiculoDTO, 0, 0));
	}
	
	@Test
	public void recargoAltoCilindraje(){
		VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
		VehiculoDTO vehiculoDTO = vehiculoTestDataBuilder.conCilindraje(vehiculoService.ALTO_CILINDRAJE)
								  .build();
		BigDecimal valorEsperado = BigDecimal.ONE.multiply(vehiculoDTO.getTipo().getValorHora())
								   .add(vehiculoService.EXCEDENTE_CILINDRAJE);
		
		assertEquals(valorEsperado, vehiculoService.calcularValorParqueadero(vehiculoDTO, 0, 1));
	}
	
	@Test
	public void sinRecargoAltoCilindraje(){
		VehiculoTestDataBuilder vehiculoTestDataBuilder = new VehiculoTestDataBuilder();
		VehiculoDTO vehiculoDTO = vehiculoTestDataBuilder.conCilindraje(0)
								  .build();
		BigDecimal valorEsperado = BigDecimal.ONE.multiply(vehiculoDTO.getTipo().getValorHora());
		
		assertEquals(valorEsperado, vehiculoService.calcularValorParqueadero(vehiculoDTO, 0, 1));
	}
}
