import { Moment } from 'moment';
import { IMuro } from 'app/shared/model/muro.model';
import { IUser } from 'app/core/user/user.model';

export interface IMensaje {
    id?: number;
    texto?: string;
    fecha?: Moment;
    muro?: IMuro;
    usuario?: IUser;
}

export class Mensaje implements IMensaje {
    constructor(public id?: number, public texto?: string, public fecha?: Moment, public muro?: IMuro, public usuario?: IUser) {}
}
