import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IMensaje } from 'app/shared/model/mensaje.model';
import { MensajeService } from './mensaje.service';
import { IMuro } from 'app/shared/model/muro.model';
import { MuroService } from 'app/entities/muro';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-mensaje-update',
    templateUrl: './mensaje-update.component.html'
})
export class MensajeUpdateComponent implements OnInit {
    mensaje: IMensaje;
    isSaving: boolean;

    muros: IMuro[];

    users: IUser[];
    fecha: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected mensajeService: MensajeService,
        protected muroService: MuroService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ mensaje }) => {
            this.mensaje = mensaje;
            this.fecha = this.mensaje.fecha != null ? this.mensaje.fecha.format(DATE_TIME_FORMAT) : null;
        });
        this.muroService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IMuro[]>) => mayBeOk.ok),
                map((response: HttpResponse<IMuro[]>) => response.body)
            )
            .subscribe((res: IMuro[]) => (this.muros = res), (res: HttpErrorResponse) => this.onError(res.message));
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
        this.mensaje.fecha = this.fecha != null ? moment(this.fecha, DATE_TIME_FORMAT) : null;
        if (this.mensaje.id !== undefined) {
            this.subscribeToSaveResponse(this.mensajeService.update(this.mensaje));
        } else {
            this.subscribeToSaveResponse(this.mensajeService.create(this.mensaje));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IMensaje>>) {
        result.subscribe((res: HttpResponse<IMensaje>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackMuroById(index: number, item: IMuro) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
