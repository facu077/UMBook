/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { UmbookTestModule } from '../../../test.module';
import { MuroComponent } from 'app/entities/muro/muro.component';
import { MuroService } from 'app/entities/muro/muro.service';
import { Muro } from 'app/shared/model/muro.model';

describe('Component Tests', () => {
    describe('Muro Management Component', () => {
        let comp: MuroComponent;
        let fixture: ComponentFixture<MuroComponent>;
        let service: MuroService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UmbookTestModule],
                declarations: [MuroComponent],
                providers: []
            })
                .overrideTemplate(MuroComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(MuroComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MuroService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Muro(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.muros[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
