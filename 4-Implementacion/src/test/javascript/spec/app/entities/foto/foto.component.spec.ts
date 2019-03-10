/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { UmbookTestModule } from '../../../test.module';
import { FotoComponent } from 'app/entities/foto/foto.component';
import { FotoService } from 'app/entities/foto/foto.service';
import { Foto } from 'app/shared/model/foto.model';

describe('Component Tests', () => {
    describe('Foto Management Component', () => {
        let comp: FotoComponent;
        let fixture: ComponentFixture<FotoComponent>;
        let service: FotoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UmbookTestModule],
                declarations: [FotoComponent],
                providers: []
            })
                .overrideTemplate(FotoComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FotoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FotoService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Foto(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.fotos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
