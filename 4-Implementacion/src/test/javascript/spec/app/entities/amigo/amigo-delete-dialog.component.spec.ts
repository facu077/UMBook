/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { UmbookTestModule } from '../../../test.module';
import { AmigoDeleteDialogComponent } from 'app/entities/amigo/amigo-delete-dialog.component';
import { AmigoService } from 'app/entities/amigo/amigo.service';

describe('Component Tests', () => {
    describe('Amigo Management Delete Component', () => {
        let comp: AmigoDeleteDialogComponent;
        let fixture: ComponentFixture<AmigoDeleteDialogComponent>;
        let service: AmigoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UmbookTestModule],
                declarations: [AmigoDeleteDialogComponent]
            })
                .overrideTemplate(AmigoDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AmigoDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AmigoService);
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
