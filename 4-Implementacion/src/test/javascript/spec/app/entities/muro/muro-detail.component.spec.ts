/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UmbookTestModule } from '../../../test.module';
import { MuroDetailComponent } from 'app/entities/muro/muro-detail.component';
import { Muro } from 'app/shared/model/muro.model';

describe('Component Tests', () => {
    describe('Muro Management Detail Component', () => {
        let comp: MuroDetailComponent;
        let fixture: ComponentFixture<MuroDetailComponent>;
        const route = ({ data: of({ muro: new Muro(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UmbookTestModule],
                declarations: [MuroDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(MuroDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MuroDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.muro).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
