import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UmbookSharedModule } from 'app/shared';
import {
    AmigoComponent,
    AmigoDetailComponent,
    AmigoUpdateComponent,
    AmigoDeletePopupComponent,
    AmigoDeleteDialogComponent,
    amigoRoute,
    amigoPopupRoute
} from './';

const ENTITY_STATES = [...amigoRoute, ...amigoPopupRoute];

@NgModule({
    imports: [UmbookSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [AmigoComponent, AmigoDetailComponent, AmigoUpdateComponent, AmigoDeleteDialogComponent, AmigoDeletePopupComponent],
    entryComponents: [AmigoComponent, AmigoUpdateComponent, AmigoDeleteDialogComponent, AmigoDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UmbookAmigoModule {}
