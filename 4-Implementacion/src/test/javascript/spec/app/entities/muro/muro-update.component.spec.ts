/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { UmbookTestModule } from '../../../test.module';
import { MuroUpdateComponent } from 'app/entities/muro/muro-update.component';
import { MuroService } from 'app/entities/muro/muro.service';
import { Muro } from 'app/shared/model/muro.model';

describe('Component Tests', () => {
    describe('Muro Management Update Component', () => {
        let comp: MuroUpdateComponent;
        let fixture: ComponentFixture<MuroUpdateComponent>;
        let service: MuroService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UmbookTestModule],
                declarations: [MuroUpdateComponent]
            })
                .overrideTemplate(MuroUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(MuroUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MuroService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Muro(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.muro = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Muro();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.muro = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
