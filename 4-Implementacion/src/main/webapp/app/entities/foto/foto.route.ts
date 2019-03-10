import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Foto } from 'app/shared/model/foto.model';
import { FotoService } from './foto.service';
import { FotoComponent } from './foto.component';
import { FotoDetailComponent } from './foto-detail.component';
import { FotoUpdateComponent } from './foto-update.component';
import { FotoDeletePopupComponent } from './foto-delete-dialog.component';
import { IFoto } from 'app/shared/model/foto.model';

@Injectable({ providedIn: 'root' })
export class FotoResolve implements Resolve<IFoto> {
    constructor(private service: FotoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IFoto> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Foto>) => response.ok),
                map((foto: HttpResponse<Foto>) => foto.body)
            );
        }
        return of(new Foto());
    }
}

export const fotoRoute: Routes = [
    {
        path: '',
        component: FotoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Fotos'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: FotoDetailComponent,
        resolve: {
            foto: FotoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Fotos'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: FotoUpdateComponent,
        resolve: {
            foto: FotoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Fotos'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: FotoUpdateComponent,
        resolve: {
            foto: FotoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Fotos'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const fotoPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: FotoDeletePopupComponent,
        resolve: {
            foto: FotoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Fotos'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
