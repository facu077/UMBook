/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { UmbookTestModule } from '../../../test.module';
import { MuroDeleteDialogComponent } from 'app/entities/muro/muro-delete-dialog.component';
import { MuroService } from 'app/entities/muro/muro.service';

describe('Component Tests', () => {
    describe('Muro Management Delete Component', () => {
        let comp: MuroDeleteDialogComponent;
        let fixture: ComponentFixture<MuroDeleteDialogComponent>;
        let service: MuroService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UmbookTestModule],
                declarations: [MuroDeleteDialogComponent]
            })
                .overrideTemplate(MuroDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MuroDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MuroService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
