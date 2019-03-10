import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAmigo } from 'app/shared/model/amigo.model';

type EntityResponseType = HttpResponse<IAmigo>;
type EntityArrayResponseType = HttpResponse<IAmigo[]>;

@Injectable({ providedIn: 'root' })
export class AmigoService {
    public resourceUrl = SERVER_API_URL + 'api/amigos';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/amigos';

    constructor(protected http: HttpClient) {}

    create(amigo: IAmigo): Observable<EntityResponseType> {
        return this.http.post<IAmigo>(this.resourceUrl, amigo, { observe: 'response' });
    }

    update(amigo: IAmigo): Observable<EntityResponseType> {
        return this.http.put<IAmigo>(this.resourceUrl, amigo, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IAmigo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAmigo[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAmigo[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
