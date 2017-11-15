package co.ceiba.domain.enumeration;

/**
 * The TipoVehiculo enumeration.
 */
public enum TipoVehiculo {
    CARRO, MOTO;
	
	private static final int CUPO_MOTOS = 10;
	private static final int CUPO_CARROS = 20;
	
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

}
