import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMensaje } from 'app/shared/model/mensaje.model';

type EntityResponseType = HttpResponse<IMensaje>;
type EntityArrayResponseType = HttpResponse<IMensaje[]>;

@Injectable({ providedIn: 'root' })
export class MensajeService {
    public resourceUrl = SERVER_API_URL + 'api/mensajes';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/mensajes';

    constructor(protected http: HttpClient) {}

    create(mensaje: IMensaje): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mensaje);
        return this.http
            .post<IMensaje>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(mensaje: IMensaje): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mensaje);
        return this.http
            .put<IMensaje>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IMensaje>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMensaje[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMensaje[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(mensaje: IMensaje): IMensaje {
        const copy: IMensaje = Object.assign({}, mensaje, {
            fecha: mensaje.fecha != null && mensaje.fecha.isValid() ? mensaje.fecha.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.fecha = res.body.fecha != null ? moment(res.body.fecha) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((mensaje: IMensaje) => {
                mensaje.fecha = mensaje.fecha != null ? moment(mensaje.fecha) : null;
            });
        }
        return res;
    }
}
