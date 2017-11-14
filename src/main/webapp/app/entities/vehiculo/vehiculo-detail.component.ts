import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Vehiculo } from './vehiculo.model';
import { VehiculoService } from './vehiculo.service';

@Component({
    selector: 'jhi-vehiculo-detail',
    templateUrl: './vehiculo-detail.component.html'
})
export class VehiculoDetailComponent implements OnInit, OnDestroy {

    vehiculo: Vehiculo;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private vehiculoService: VehiculoService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInVehiculos();
    }

    load(id) {
        this.vehiculoService.find(id).subscribe((vehiculo) => {
            this.vehiculo = vehiculo;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInVehiculos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'vehiculoListModification',
            (response) => this.load(this.vehiculo.id)
        );
    }
}
