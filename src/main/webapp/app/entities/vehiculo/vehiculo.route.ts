import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { VehiculoComponent } from './vehiculo.component';
import { VehiculoDetailComponent } from './vehiculo-detail.component';
import { VehiculoPopupComponent } from './vehiculo-dialog.component';
import { VehiculoDeletePopupComponent } from './vehiculo-delete-dialog.component';

export const vehiculoRoute: Routes = [
    {
        path: 'vehiculo',
        component: VehiculoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nuevoApp.vehiculo.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'vehiculo/:id',
        component: VehiculoDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nuevoApp.vehiculo.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const vehiculoPopupRoute: Routes = [
    {
        path: 'vehiculo-new',
        component: VehiculoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nuevoApp.vehiculo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vehiculo/:id/edit',
        component: VehiculoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nuevoApp.vehiculo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vehiculo/:id/delete',
        component: VehiculoDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nuevoApp.vehiculo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
