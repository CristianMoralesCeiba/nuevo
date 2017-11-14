import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { NuevoVehiculoModule } from './vehiculo/vehiculo.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        NuevoVehiculoModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NuevoEntityModule {}
