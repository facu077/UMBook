import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IAlbum } from 'app/shared/model/album.model';
import { AccountService } from 'app/core';
import { AlbumService } from './album.service';

@Component({
    selector: 'jhi-album',
    templateUrl: './album.component.html'
})
export class AlbumComponent implements OnInit, OnDestroy {
    albums: IAlbum[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected albumService: AlbumService,
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
            this.albumService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IAlbum[]>) => res.ok),
                    map((res: HttpResponse<IAlbum[]>) => res.body)
                )
                .subscribe((res: IAlbum[]) => (this.albums = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.albumService
            .query()
            .pipe(
                filter((res: HttpResponse<IAlbum[]>) => res.ok),
                map((res: HttpResponse<IAlbum[]>) => res.body)
            )
            .subscribe(
                (res: IAlbum[]) => {
                    this.albums = res;
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
        this.registerChangeInAlbums();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IAlbum) {
        return item.id;
    }

    registerChangeInAlbums() {
        this.eventSubscriber = this.eventManager.subscribe('albumListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
