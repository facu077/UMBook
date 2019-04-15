import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UmbookMensajeModule } from '../mensaje/mensaje.module';

import { UmbookSharedModule } from 'app/shared';
import {
    MuroComponent,
    MuroDetailComponent,
    MuroUpdateComponent,
    MuroDeletePopupComponent,
    MuroDeleteDialogComponent,
    muroRoute,
    muroPopupRoute
} from './';

const ENTITY_STATES = [...muroRoute, ...muroPopupRoute];

@NgModule({
    imports: [UmbookMensajeModule, UmbookSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [MuroComponent, MuroDetailComponent, MuroUpdateComponent, MuroDeleteDialogComponent, MuroDeletePopupComponent],
    entryComponents: [MuroComponent, MuroUpdateComponent, MuroDeleteDialogComponent, MuroDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    exports: [MuroComponent]
})
export class UmbookMuroModule {}
