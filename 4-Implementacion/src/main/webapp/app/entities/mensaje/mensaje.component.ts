import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription, Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import * as moment from 'moment';

import { IMensaje } from '../../shared/model/mensaje.model';
import { IMuro } from '../../shared/model/muro.model';
import { AccountService } from '../../core';
import { MensajeService } from './mensaje.service';
import { DATE_TIME_FORMAT } from '../../shared/constants/input.constants';

@Component({
    selector: 'jhi-mensaje',
    templateUrl: './mensaje.component.html'
})
export class MensajeComponent implements OnInit, OnDestroy {
    @Input() muro: IMuro;

    mensajes: IMensaje[];
    mensajesMuro: IMensaje[] = [];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentTxt: string;

    isSaving: boolean;

    constructor(
        protected mensajeService: MensajeService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.mensajeService
            .query()
            .pipe(
                filter((res: HttpResponse<IMensaje[]>) => res.ok),
                map((res: HttpResponse<IMensaje[]>) => res.body)
            )
            .subscribe(
                (res: IMensaje[]) => {
                    this.mensajes = res;
                    if (this.mensajes && this.mensajes.length > 0) {
                        for (const msj of this.mensajes) {
                            if (msj.muro.id === this.muro.id) {
                                this.mensajesMuro.push(msj);
                            }
                        }
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    newMsj() {
        this.isSaving = true;
        const newMensaje: IMensaje = {
            fecha: moment(new Date(), DATE_TIME_FORMAT),
            texto: this.currentTxt,
            muro: this.muro
        };
        this.subscribeToSaveResponse(this.mensajeService.create(newMensaje));
        this.currentTxt = '';
    }

    canSendMsj(): boolean {
        return this.currentTxt && this.currentTxt !== '';
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IMensaje>>) {
        result.subscribe((res: HttpResponse<IMensaje>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.loadAll();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    clear() {
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInMensajes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IMensaje) {
        return item.id;
    }

    registerChangeInMensajes() {
        this.eventSubscriber = this.eventManager.subscribe('mensajeListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
