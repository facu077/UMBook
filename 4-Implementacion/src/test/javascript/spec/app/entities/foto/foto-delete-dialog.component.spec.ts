/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { UmbookTestModule } from '../../../test.module';
import { FotoDeleteDialogComponent } from 'app/entities/foto/foto-delete-dialog.component';
import { FotoService } from 'app/entities/foto/foto.service';

describe('Component Tests', () => {
    describe('Foto Management Delete Component', () => {
        let comp: FotoDeleteDialogComponent;
        let fixture: ComponentFixture<FotoDeleteDialogComponent>;
        let service: FotoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UmbookTestModule],
                declarations: [FotoDeleteDialogComponent]
            })
                .overrideTemplate(FotoDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FotoDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FotoService);
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
