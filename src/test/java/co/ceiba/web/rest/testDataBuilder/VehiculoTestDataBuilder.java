package co.ceiba.web.rest.testDataBuilder;

import java.time.Instant;
import co.ceiba.domain.enumeration.TipoVehiculo;
import co.ceiba.service.dto.VehiculoDTO;

public class VehiculoTestDataBuilder {

	public static final Long ID = null;
	public static final String PLACA = "BCD123";
	public static final TipoVehiculo TIPO = TipoVehiculo.MOTO;
	public static final Integer CILINDRAJE = 100;
	public static final Instant FECHAINGRESO = Instant.parse("2017-11-08T10:12:35Z");
	
    private Long id;
    private String placa;
    private TipoVehiculo tipo;
    private Integer cilindraje;
    private Instant fechaIngreso;
    
    public VehiculoTestDataBuilder () {
    	this.id = ID;
    	this.placa = PLACA;
    	this.tipo = TIPO;
    	this.cilindraje = CILINDRAJE;
    	this.fechaIngreso = FECHAINGRESO;
    }

    public VehiculoTestDataBuilder conID(long id){
    	this.id = id;
    	return this;
    }
    
    public VehiculoTestDataBuilder conPlaca(String placa){
    	this.placa = placa;
    	return this;
    }

    public VehiculoTestDataBuilder conTipo(TipoVehiculo tipo){
    	this.tipo = tipo;
    	return this;
    }
    
    public VehiculoTestDataBuilder conCilindraje(Integer cilindraje){
    	this.cilindraje = cilindraje;
    	return this;
    }
    
    public VehiculoTestDataBuilder conFechaIngreso(Instant fechaIngreso){
    	this.fechaIngreso = fechaIngreso;
    	return this;
    }
    
    public VehiculoDTO build() {
    	return new VehiculoDTO(this.id, this.placa, this.tipo, this.cilindraje, this.fechaIngreso);
    }
	
}
