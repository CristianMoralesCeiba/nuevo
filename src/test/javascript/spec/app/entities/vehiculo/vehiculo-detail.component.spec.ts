/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { NuevoTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { VehiculoDetailComponent } from '../../../../../../main/webapp/app/entities/vehiculo/vehiculo-detail.component';
import { VehiculoService } from '../../../../../../main/webapp/app/entities/vehiculo/vehiculo.service';
import { Vehiculo } from '../../../../../../main/webapp/app/entities/vehiculo/vehiculo.model';

describe('Component Tests', () => {

    describe('Vehiculo Management Detail Component', () => {
        let comp: VehiculoDetailComponent;
        let fixture: ComponentFixture<VehiculoDetailComponent>;
        let service: VehiculoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NuevoTestModule],
                declarations: [VehiculoDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    VehiculoService,
                    JhiEventManager
                ]
            }).overrideTemplate(VehiculoDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VehiculoDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VehiculoService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Vehiculo(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.vehiculo).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
