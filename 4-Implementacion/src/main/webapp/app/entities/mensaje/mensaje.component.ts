import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IMensaje } from 'app/shared/model/mensaje.model';
import { AccountService } from 'app/core';
import { MensajeService } from './mensaje.service';

@Component({
    selector: 'jhi-mensaje',
    templateUrl: './mensaje.component.html'
})
export class MensajeComponent implements OnInit, OnDestroy {
    mensajes: IMensaje[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected mensajeService: MensajeService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.mensajeService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IMensaje[]>) => res.ok),
                    map((res: HttpResponse<IMensaje[]>) => res.body)
                )
                .subscribe((res: IMensaje[]) => (this.mensajes = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.mensajeService
            .query()
            .pipe(
                filter((res: HttpResponse<IMensaje[]>) => res.ok),
                map((res: HttpResponse<IMensaje[]>) => res.body)
            )
            .subscribe(
                (res: IMensaje[]) => {
                    this.mensajes = res;
                    this.currentSearch = '';
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
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
