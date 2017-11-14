import { BaseEntity } from './../../shared';

export const enum TipoVehiculo {
    'CARRO',
    'MOTO'
}

export class Vehiculo implements BaseEntity {
    constructor(
        public id?: number,
        public placa?: string,
        public tipo?: TipoVehiculo,
        public cilindraje?: number,
        public fechaIngreso?: any,
    ) {
    }
}
