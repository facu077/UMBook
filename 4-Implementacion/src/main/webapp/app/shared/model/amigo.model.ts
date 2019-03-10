import { IUser } from 'app/core/user/user.model';

export const enum Estado {
    Aceptado = 'Aceptado',
    Rechazado = 'Rechazado',
    Esperando = 'Esperando'
}

export interface IAmigo {
    id?: number;
    estado?: Estado;
    usuario?: IUser;
    amigo?: IUser;
}

export class Amigo implements IAmigo {
    constructor(public id?: number, public estado?: Estado, public usuario?: IUser, public amigo?: IUser) {}
}
