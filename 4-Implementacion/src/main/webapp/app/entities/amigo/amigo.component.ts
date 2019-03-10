import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IAmigo } from 'app/shared/model/amigo.model';
import { AccountService } from 'app/core';
import { AmigoService } from './amigo.service';

@Component({
    selector: 'jhi-amigo',
    templateUrl: './amigo.component.html'
})
export class AmigoComponent implements OnInit, OnDestroy {
    amigos: IAmigo[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected amigoService: AmigoService,
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
            this.amigoService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IAmigo[]>) => res.ok),
                    map((res: HttpResponse<IAmigo[]>) => res.body)
                )
                .subscribe((res: IAmigo[]) => (this.amigos = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.amigoService
            .query()
            .pipe(
                filter((res: HttpResponse<IAmigo[]>) => res.ok),
                map((res: HttpResponse<IAmigo[]>) => res.body)
            )
            .subscribe(
                (res: IAmigo[]) => {
                    this.amigos = res;
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
        this.registerChangeInAmigos();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IAmigo) {
        return item.id;
    }

    registerChangeInAmigos() {
        this.eventSubscriber = this.eventManager.subscribe('amigoListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
