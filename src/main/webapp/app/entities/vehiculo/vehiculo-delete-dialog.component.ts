import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Vehiculo } from './vehiculo.model';
import { VehiculoPopupService } from './vehiculo-popup.service';
import { VehiculoService } from './vehiculo.service';

@Component({
    selector: 'jhi-vehiculo-delete-dialog',
    templateUrl: './vehiculo-delete-dialog.component.html'
})
export class VehiculoDeleteDialogComponent {

    vehiculo: Vehiculo;

    constructor(
        private vehiculoService: VehiculoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.vehiculoService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'vehiculoListModification',
                content: 'Deleted an vehiculo'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-vehiculo-delete-popup',
    template: ''
})
export class VehiculoDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private vehiculoPopupService: VehiculoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.vehiculoPopupService
                .open(VehiculoDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
