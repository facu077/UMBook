import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFoto } from 'app/shared/model/foto.model';

type EntityResponseType = HttpResponse<IFoto>;
type EntityArrayResponseType = HttpResponse<IFoto[]>;

@Injectable({ providedIn: 'root' })
export class FotoService {
    public resourceUrl = SERVER_API_URL + 'api/fotos';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/fotos';

    constructor(protected http: HttpClient) {}

    create(foto: IFoto): Observable<EntityResponseType> {
        return this.http.post<IFoto>(this.resourceUrl, foto, { observe: 'response' });
    }

    update(foto: IFoto): Observable<EntityResponseType> {
        return this.http.put<IFoto>(this.resourceUrl, foto, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IFoto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFoto[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFoto[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
