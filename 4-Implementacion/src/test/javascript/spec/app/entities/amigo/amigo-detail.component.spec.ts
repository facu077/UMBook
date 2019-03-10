/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UmbookTestModule } from '../../../test.module';
import { AmigoDetailComponent } from 'app/entities/amigo/amigo-detail.component';
import { Amigo } from 'app/shared/model/amigo.model';

describe('Component Tests', () => {
    describe('Amigo Management Detail Component', () => {
        let comp: AmigoDetailComponent;
        let fixture: ComponentFixture<AmigoDetailComponent>;
        const route = ({ data: of({ amigo: new Amigo(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UmbookTestModule],
                declarations: [AmigoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AmigoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AmigoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.amigo).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
