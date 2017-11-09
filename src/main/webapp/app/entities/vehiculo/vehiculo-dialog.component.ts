import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Vehiculo } from './vehiculo.model';
import { VehiculoPopupService } from './vehiculo-popup.service';
import { VehiculoService } from './vehiculo.service';

@Component({
    selector: 'jhi-vehiculo-dialog',
    templateUrl: './vehiculo-dialog.component.html'
})
export class VehiculoDialogComponent implements OnInit {

    vehiculo: Vehiculo;
    isSaving: boolean;
    fechaIngresoDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private vehiculoService: VehiculoService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.vehiculo.id !== undefined) {
            this.subscribeToSaveResponse(
                this.vehiculoService.update(this.vehiculo));
        } else {
            this.subscribeToSaveResponse(
                this.vehiculoService.create(this.vehiculo));
        }
    }

    private subscribeToSaveResponse(result: Observable<Vehiculo>) {
        result.subscribe((res: Vehiculo) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Vehiculo) {
        this.eventManager.broadcast({ name: 'vehiculoListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-vehiculo-popup',
    template: ''
})
export class VehiculoPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private vehiculoPopupService: VehiculoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.vehiculoPopupService
                    .open(VehiculoDialogComponent as Component, params['id']);
            } else {
                this.vehiculoPopupService
                    .open(VehiculoDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
