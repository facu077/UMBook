import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UmbookSharedModule } from 'app/shared';
import {
    FotoComponent,
    FotoDetailComponent,
    FotoUpdateComponent,
    FotoDeletePopupComponent,
    FotoDeleteDialogComponent,
    fotoRoute,
    fotoPopupRoute
} from './';

const ENTITY_STATES = [...fotoRoute, ...fotoPopupRoute];

@NgModule({
    imports: [UmbookSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [FotoComponent, FotoDetailComponent, FotoUpdateComponent, FotoDeleteDialogComponent, FotoDeletePopupComponent],
    entryComponents: [FotoComponent, FotoUpdateComponent, FotoDeleteDialogComponent, FotoDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UmbookFotoModule {}
