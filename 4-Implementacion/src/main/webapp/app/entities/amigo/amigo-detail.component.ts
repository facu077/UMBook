import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAmigo } from 'app/shared/model/amigo.model';

@Component({
    selector: 'jhi-amigo-detail',
    templateUrl: './amigo-detail.component.html'
})
export class AmigoDetailComponent implements OnInit {
    amigo: IAmigo;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ amigo }) => {
            this.amigo = amigo;
        });
    }

    previousState() {
        window.history.back();
    }
}
