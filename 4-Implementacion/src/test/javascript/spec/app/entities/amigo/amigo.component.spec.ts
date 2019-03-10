/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { UmbookTestModule } from '../../../test.module';
import { AmigoComponent } from 'app/entities/amigo/amigo.component';
import { AmigoService } from 'app/entities/amigo/amigo.service';
import { Amigo } from 'app/shared/model/amigo.model';

describe('Component Tests', () => {
    describe('Amigo Management Component', () => {
        let comp: AmigoComponent;
        let fixture: ComponentFixture<AmigoComponent>;
        let service: AmigoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UmbookTestModule],
                declarations: [AmigoComponent],
                providers: []
            })
                .overrideTemplate(AmigoComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AmigoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AmigoService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Amigo(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.amigos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
