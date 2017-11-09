import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NuevoSharedModule } from '../../shared';
import {
    VehiculoService,
    VehiculoPopupService,
    VehiculoComponent,
    VehiculoDetailComponent,
    VehiculoDialogComponent,
    VehiculoPopupComponent,
    VehiculoDeletePopupComponent,
    VehiculoDeleteDialogComponent,
    vehiculoRoute,
    vehiculoPopupRoute,
} from './';

const ENTITY_STATES = [
    ...vehiculoRoute,
    ...vehiculoPopupRoute,
];

@NgModule({
    imports: [
        NuevoSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        VehiculoComponent,
        VehiculoDetailComponent,
        VehiculoDialogComponent,
        VehiculoDeleteDialogComponent,
        VehiculoPopupComponent,
        VehiculoDeletePopupComponent,
    ],
    entryComponents: [
        VehiculoComponent,
        VehiculoDialogComponent,
        VehiculoPopupComponent,
        VehiculoDeleteDialogComponent,
        VehiculoDeletePopupComponent,
    ],
    providers: [
        VehiculoService,
        VehiculoPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NuevoVehiculoModule {}
