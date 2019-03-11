import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UmbookSharedModule } from 'app/shared';
import { HOME_ROUTE, HomeComponent } from './';
import { UmbookEntityModule } from '../entities/entity.module';

@NgModule({
    imports: [UmbookSharedModule, RouterModule.forChild([HOME_ROUTE]), UmbookEntityModule],
    declarations: [HomeComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UmbookHomeModule {}
