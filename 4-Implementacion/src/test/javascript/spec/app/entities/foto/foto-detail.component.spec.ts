/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UmbookTestModule } from '../../../test.module';
import { FotoDetailComponent } from 'app/entities/foto/foto-detail.component';
import { Foto } from 'app/shared/model/foto.model';

describe('Component Tests', () => {
    describe('Foto Management Detail Component', () => {
        let comp: FotoDetailComponent;
        let fixture: ComponentFixture<FotoDetailComponent>;
        const route = ({ data: of({ foto: new Foto(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UmbookTestModule],
                declarations: [FotoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(FotoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FotoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.foto).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
