import { IFoto } from 'app/shared/model/foto.model';
import { IUser } from 'app/core/user/user.model';

export interface IAlbum {
    id?: number;
    fotos?: IFoto[];
    usuario?: IUser;
}

export class Album implements IAlbum {
    constructor(public id?: number, public fotos?: IFoto[], public usuario?: IUser) {}
}
