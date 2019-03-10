import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'amigo',
                loadChildren: './amigo/amigo.module#UmbookAmigoModule'
            },
            {
                path: 'muro',
                loadChildren: './muro/muro.module#UmbookMuroModule'
            },
            {
                path: 'mensaje',
                loadChildren: './mensaje/mensaje.module#UmbookMensajeModule'
            },
            {
                path: 'album',
                loadChildren: './album/album.module#UmbookAlbumModule'
            },
            {
                path: 'foto',
                loadChildren: './foto/foto.module#UmbookFotoModule'
            },
            {
                path: 'amigo',
                loadChildren: './amigo/amigo.module#UmbookAmigoModule'
            },
            {
                path: 'amigo',
                loadChildren: './amigo/amigo.module#UmbookAmigoModule'
            },
            {
                path: 'amigo',
                loadChildren: './amigo/amigo.module#UmbookAmigoModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UmbookEntityModule {}
