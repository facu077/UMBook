import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IAmigo } from 'app/shared/model/amigo.model';
import { AmigoService } from './amigo.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-amigo-update',
    templateUrl: './amigo-update.component.html'
})
export class AmigoUpdateComponent implements OnInit {
    amigo: IAmigo;
    isSaving: boolean;

    users: IUser[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected amigoService: AmigoService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ amigo }) => {
            this.amigo = amigo;
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
        if (this.amigo.id !== undefined) {
            this.subscribeToSaveResponse(this.amigoService.update(this.amigo));
        } else {
            this.subscribeToSaveResponse(this.amigoService.create(this.amigo));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAmigo>>) {
        result.subscribe((res: HttpResponse<IAmigo>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
