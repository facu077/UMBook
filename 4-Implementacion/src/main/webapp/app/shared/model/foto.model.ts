import { IAlbum } from 'app/shared/model/album.model';

export interface IFoto {
    id?: number;
    imagenContentType?: string;
    imagen?: any;
    album?: IAlbum;
}

export class Foto implements IFoto {
    constructor(public id?: number, public imagenContentType?: string, public imagen?: any, public album?: IAlbum) {}
}
