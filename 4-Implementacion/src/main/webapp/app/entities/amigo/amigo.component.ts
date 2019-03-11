import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription, Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IAmigo, Estado } from '../../shared/model/amigo.model';
import { AccountService, IUser, UserService } from 'app/core';
import { AmigoService } from './amigo.service';

@Component({
    selector: 'jhi-amigo',
    templateUrl: './amigo.component.html'
})
export class AmigoComponent implements OnInit, OnDestroy {
    amigos: IAmigo[];
    currentAccount: any;
    eventSubscriber: Subscription;

    users: IUser[];

    isSaving: boolean;

    constructor(
        protected amigoService: AmigoService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService,
        protected userService: UserService
    ) {}

    loadAll() {
        this.amigoService
            .query()
            .pipe(
                filter((res: HttpResponse<IAmigo[]>) => res.ok),
                map((res: HttpResponse<IAmigo[]>) => res.body)
            )
            .subscribe(
                (res: IAmigo[]) => {
                    for (const friend of res) {
                        if (friend.amigo.id !== this.currentAccount.id) {
                            const index = res.indexOf(friend);
                            res.splice(index, 1);
                        }
                    }
                    this.amigos = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );

        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe(
                (res: IUser[]) => {
                    for (const user of res) {
                        if (user.id < 5 || user.id === this.currentAccount.id) {
                            const index = res.indexOf(user);
                            res.splice(index, 1);
                        }
                        for (const amigo of this.amigos) {
                            if (amigo.amigo.id === user.id) {
                                const index = res.indexOf(user);
                                res.splice(index, 1);
                            }
                        }
                    }
                    this.users = res;
                    this.users.reverse();
                    this.users.splice(this.users.indexOf(this.users[0], 1));
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    clear() {
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInAmigos();
    }

    addFriend(user: IUser) {
        this.isSaving = true;

        let amigo: IAmigo = {
            estado: Estado.Aceptado,
            amigo: user,
            usuario: this.currentAccount
        };
        this.subscribeToSaveResponse(this.amigoService.create(amigo));

        amigo = {
            estado: Estado.Aceptado,
            amigo: this.currentAccount,
            usuario: user
        };
        this.subscribeToSaveResponse(this.amigoService.create(amigo));
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAmigo>>) {
        result.subscribe((res: HttpResponse<IAmigo>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.loadAll();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
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
}
