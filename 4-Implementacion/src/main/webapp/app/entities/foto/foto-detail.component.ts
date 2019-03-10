import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IFoto } from 'app/shared/model/foto.model';

@Component({
    selector: 'jhi-foto-detail',
    templateUrl: './foto-detail.component.html'
})
export class FotoDetailComponent implements OnInit {
    foto: IFoto;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ foto }) => {
            this.foto = foto;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
