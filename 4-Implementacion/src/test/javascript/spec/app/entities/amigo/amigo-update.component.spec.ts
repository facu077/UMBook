/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { UmbookTestModule } from '../../../test.module';
import { AmigoUpdateComponent } from 'app/entities/amigo/amigo-update.component';
import { AmigoService } from 'app/entities/amigo/amigo.service';
import { Amigo } from 'app/shared/model/amigo.model';

describe('Component Tests', () => {
    describe('Amigo Management Update Component', () => {
        let comp: AmigoUpdateComponent;
        let fixture: ComponentFixture<AmigoUpdateComponent>;
        let service: AmigoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UmbookTestModule],
                declarations: [AmigoUpdateComponent]
            })
                .overrideTemplate(AmigoUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AmigoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AmigoService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Amigo(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.amigo = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Amigo();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.amigo = entity;
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
