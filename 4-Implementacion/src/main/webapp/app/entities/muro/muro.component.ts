import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IMuro } from 'app/shared/model/muro.model';
import { AccountService } from 'app/core';
import { MuroService } from './muro.service';

@Component({
    selector: 'jhi-muro',
    templateUrl: './muro.component.html'
})
export class MuroComponent implements OnInit, OnDestroy {
    muros: IMuro[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected muroService: MuroService,
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
            this.muroService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IMuro[]>) => res.ok),
                    map((res: HttpResponse<IMuro[]>) => res.body)
                )
                .subscribe((res: IMuro[]) => (this.muros = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.muroService
            .query()
            .pipe(
                filter((res: HttpResponse<IMuro[]>) => res.ok),
                map((res: HttpResponse<IMuro[]>) => res.body)
            )
            .subscribe(
                (res: IMuro[]) => {
                    this.muros = res;
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
        this.registerChangeInMuros();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IMuro) {
        return item.id;
    }

    registerChangeInMuros() {
        this.eventSubscriber = this.eventManager.subscribe('muroListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
