import { IUser } from 'app/core/user/user.model';
import { IMensaje } from 'app/shared/model/mensaje.model';

export interface IMuro {
    id?: number;
    usuario?: IUser;
    mensajes?: IMensaje[];
}

export class Muro implements IMuro {
    constructor(public id?: number, public usuario?: IUser, public mensajes?: IMensaje[]) {}
}
