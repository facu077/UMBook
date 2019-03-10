/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { UmbookTestModule } from '../../../test.module';
import { FotoUpdateComponent } from 'app/entities/foto/foto-update.component';
import { FotoService } from 'app/entities/foto/foto.service';
import { Foto } from 'app/shared/model/foto.model';

describe('Component Tests', () => {
    describe('Foto Management Update Component', () => {
        let comp: FotoUpdateComponent;
        let fixture: ComponentFixture<FotoUpdateComponent>;
        let service: FotoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UmbookTestModule],
                declarations: [FotoUpdateComponent]
            })
                .overrideTemplate(FotoUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FotoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FotoService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Foto(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.foto = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Foto();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.foto = entity;
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
