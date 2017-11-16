package co.ceiba.domain.enumeration;

import java.math.BigDecimal;

/**
 * The TipoVehiculo enumeration.
 */
public enum TipoVehiculo {
    CARRO, MOTO;
	
	private static final int CUPO_MOTOS = 10;
	private static final int CUPO_CARROS = 20;
	
	private static final BigDecimal VALOR_HORA_CARRO = new BigDecimal("1000");
	private static final BigDecimal VALOR_HORA_MOTO = new BigDecimal("500");
	private static final BigDecimal VALOR_DIA_CARRO = new BigDecimal("8000");
	private static final BigDecimal VALOR_DIA_MOTO = new BigDecimal("600");
	
	public int getCupo(){
		switch(this){
			case CARRO:
				return CUPO_CARROS;
			case MOTO: 
				return CUPO_MOTOS;
			default:
				return 0;
		}
	}
	
	public BigDecimal getValorDia(){
		switch(this){
			case CARRO:
				return VALOR_DIA_CARRO;
			case MOTO: 
				return VALOR_DIA_MOTO;
			default:
				return new BigDecimal("0");
		}
	}
	
	public BigDecimal getValorHora(){
		switch(this){
			case CARRO:
				return VALOR_HORA_CARRO;
			case MOTO: 
				return VALOR_HORA_MOTO;
			default:
				return new BigDecimal("0");
		}
	}
		

}
