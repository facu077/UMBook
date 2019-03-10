import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IFoto } from 'app/shared/model/foto.model';
import { AccountService } from 'app/core';
import { FotoService } from './foto.service';

@Component({
    selector: 'jhi-foto',
    templateUrl: './foto.component.html'
})
export class FotoComponent implements OnInit, OnDestroy {
    fotos: IFoto[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected fotoService: FotoService,
        protected jhiAlertService: JhiAlertService,
        protected dataUtils: JhiDataUtils,
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
            this.fotoService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IFoto[]>) => res.ok),
                    map((res: HttpResponse<IFoto[]>) => res.body)
                )
                .subscribe((res: IFoto[]) => (this.fotos = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.fotoService
            .query()
            .pipe(
                filter((res: HttpResponse<IFoto[]>) => res.ok),
                map((res: HttpResponse<IFoto[]>) => res.body)
            )
            .subscribe(
                (res: IFoto[]) => {
                    this.fotos = res;
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
        this.registerChangeInFotos();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IFoto) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInFotos() {
        this.eventSubscriber = this.eventManager.subscribe('fotoListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
