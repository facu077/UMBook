import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Amigo } from 'app/shared/model/amigo.model';
import { AmigoService } from './amigo.service';
import { AmigoComponent } from './amigo.component';
import { AmigoDetailComponent } from './amigo-detail.component';
import { AmigoUpdateComponent } from './amigo-update.component';
import { AmigoDeletePopupComponent } from './amigo-delete-dialog.component';
import { IAmigo } from 'app/shared/model/amigo.model';

@Injectable({ providedIn: 'root' })
export class AmigoResolve implements Resolve<IAmigo> {
    constructor(private service: AmigoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAmigo> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Amigo>) => response.ok),
                map((amigo: HttpResponse<Amigo>) => amigo.body)
            );
        }
        return of(new Amigo());
    }
}

export const amigoRoute: Routes = [
    {
        path: '',
        component: AmigoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Amigos'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: AmigoDetailComponent,
        resolve: {
            amigo: AmigoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Amigos'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: AmigoUpdateComponent,
        resolve: {
            amigo: AmigoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Amigos'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: AmigoUpdateComponent,
        resolve: {
            amigo: AmigoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Amigos'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const amigoPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AmigoDeletePopupComponent,
        resolve: {
            amigo: AmigoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Amigos'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
