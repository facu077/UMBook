import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IMuro } from 'app/shared/model/muro.model';
import { MuroService } from './muro.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-muro-update',
    templateUrl: './muro-update.component.html'
})
export class MuroUpdateComponent implements OnInit {
    muro: IMuro;
    isSaving: boolean;

    users: IUser[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected muroService: MuroService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ muro }) => {
            this.muro = muro;
        });
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.muro.id !== undefined) {
            this.subscribeToSaveResponse(this.muroService.update(this.muro));
        } else {
            this.subscribeToSaveResponse(this.muroService.create(this.muro));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IMuro>>) {
        result.subscribe((res: HttpResponse<IMuro>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
